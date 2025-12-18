package cn.talins.mybatis.max.sdk.repository;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.StaticLog;
import cn.talins.mybatis.max.api.IRepository;
import cn.talins.mybatis.max.api.IRepositoryHandler;
import cn.talins.mybatis.max.api.enums.Booleans;
import cn.talins.mybatis.max.api.pojo.BaseEntity;
import cn.talins.mybatis.max.sdk.CacheUtil;
import cn.talins.mybatis.max.sdk.DynamicDataSource;
import cn.talins.mybatis.max.sdk.DynamicMapperUtil;
import cn.talins.mybatis.max.sdk.common.Constant;
import cn.talins.mybatis.max.sdk.event.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import net.oschina.j2cache.CacheChannel;
import net.oschina.j2cache.CacheObject;
import net.oschina.j2cache.J2Cache;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 基础数据仓库实现类 - IRepository接口的核心实现
 * <p>
 * 该类实现了通用的数据访问逻辑，包括：
 * <ul>
 *     <li>CRUD操作的具体实现</li>
 *     <li>自动切换数据源（多数据源场景）</li>
 *     <li>缓存的自动管理（读取、更新、清除）</li>
 *     <li>实体操作事件的发布（支持前置/后置事件）</li>
 *     <li>逻辑删除的自动处理（normal字段）</li>
 * </ul>
 * </p>
 * 
 * <p>
 * 事件机制：
 * 每个数据操作都会发布两次事件：
 * <ul>
 *     <li>前置事件（isBefore=1）：在执行SQL之前发布</li>
 *     <li>后置事件（isBefore=0）：在执行SQL之后发布</li>
 * </ul>
 * 可以通过Spring的@EventListener监听这些事件实现业务扩展。
 * </p>
 * 
 * <p>
 * 缓存机制：
 * <ul>
 *     <li>如果表对应的缓存区域存在，则自动进行缓存操作</li>
 *     <li>插入时：将新记录加入缓存</li>
 *     <li>更新时：更新缓存中的记录</li>
 *     <li>删除时：从缓存中移除记录</li>
 *     <li>查询时：优先从缓存读取</li>
 * </ul>
 * </p>
 * 
 * @author talins
 * @see IRepository 接口定义
 * @see DataPermissionRepository 带数据权限的仓库实现
 */
@SuppressWarnings({"unchecked", "rawtypes"})
@Validated
public class BaseRepository implements IRepository, ApplicationEventPublisherAware {

    /**
     * 仓库处理器，用于获取Mapper和填充实体字段
     */
    private final IRepositoryHandler repositoryHandler;

    /**
     * Spring事件发布器，用于发布实体操作事件
     */
    private ApplicationEventPublisher publisher;

    /**
     * 构造函数
     * 
     * @param repositoryHandler 仓库处理器
     */
    public BaseRepository(IRepositoryHandler repositoryHandler) {
        this.repositoryHandler = repositoryHandler;
    }

    @Override
    public <T> Long insert(String tableName, T entity) {
        try {
            BaseMapper<BaseEntity> mapper = repositoryHandler.getMapper(tableName);
            BaseEntity baseEntity = BeanUtil.toBean(entity, DynamicMapperUtil.getLoaderClass(tableName));
            repositoryHandler.fillInsertEntity(baseEntity);
            publisher.publishEvent(new EntityInsertEvent(tableName, baseEntity, Booleans.TRUE.getValue()));
            if(Constant.TABLE_DATASOURCE_MAP.containsKey(tableName)) {
                DynamicDataSource.push(Constant.TABLE_DATASOURCE_MAP.get(tableName));
            }
            mapper.insert(baseEntity);
            if(CacheUtil.exists(tableName)) {
                CacheUtil.set(tableName, String.valueOf(baseEntity.getId()),
                        JSONUtil.parseObj(baseEntity));
                StaticLog.info("cache insert: {} {}", tableName, baseEntity.getId());
            }
            publisher.publishEvent(new EntityInsertEvent(tableName, baseEntity, Booleans.FALSE.getValue()));
            return baseEntity.getId();
        } finally {
            if(Constant.TABLE_DATASOURCE_MAP.containsKey(tableName)) {
                DynamicDataSource.poll();
            }
        }
    }

    @Override
    public <T> int delete(String tableName, QueryWrapper<T> queryWrapper) {
        try {
            BaseMapper mapper = repositoryHandler.getMapper(tableName);
            queryWrapper.eq("normal", Booleans.TRUE.getValue());
            publisher.publishEvent(new EntityDeleteEvent(tableName, queryWrapper, Booleans.TRUE.getValue()));
            if(Constant.TABLE_DATASOURCE_MAP.containsKey(tableName)) {
                DynamicDataSource.push(Constant.TABLE_DATASOURCE_MAP.get(tableName));
            }
            int count = mapper.delete(queryWrapper);
            if(CacheUtil.exists(tableName)) {
                refreshCache(tableName);
            }
            publisher.publishEvent(new EntityDeleteEvent(tableName, queryWrapper, Booleans.FALSE.getValue()));
            return count;
        } finally {
            if(Constant.TABLE_DATASOURCE_MAP.containsKey(tableName)) {
                DynamicDataSource.poll();
            }
        }
    }

    private void refreshCache(String tableName) {
        CacheChannel cache = J2Cache.getChannel();
        if(cache == null) {
            return;
        }
        cache.clear(tableName);
        Map<String, Object> cacheMap = selectList(tableName,
                Wrappers.query(JSONObject.class)).stream()
                .collect(Collectors.toMap(item -> item.getStr("id"), item -> item));
        cache.set(tableName, cacheMap);
        StaticLog.info("cache refresh: {}", tableName);
    }

    @Override
    public int deleteBatchIds(String tableName, Collection<Long> idList) {
        try {
            BaseMapper<BaseEntity> mapper = repositoryHandler.getMapper(tableName);
            publisher.publishEvent(new EntityDeleteBatchEvent(tableName, idList, Booleans.TRUE.getValue()));
            if(Constant.TABLE_DATASOURCE_MAP.containsKey(tableName)) {
                DynamicDataSource.push(Constant.TABLE_DATASOURCE_MAP.get(tableName));
            }
            int count = mapper.deleteBatchIds(idList);
            if(CacheUtil.exists(tableName)) {
                CacheUtil.remove(tableName, idList.stream()
                        .map(String::valueOf).toArray(String[]::new));
                StaticLog.info("cache evict: {}", tableName);
            }
            publisher.publishEvent(new EntityDeleteBatchEvent(tableName, idList, Booleans.FALSE.getValue()));
            return count;
        } finally {
            if(Constant.TABLE_DATASOURCE_MAP.containsKey(tableName)) {
                DynamicDataSource.poll();
            }
        }
    }

    @Override
    public <T> int updateById(String tableName, T entity) {
        try {
            BaseEntity baseEntity = BeanUtil.toBean(entity, DynamicMapperUtil.getLoaderClass(tableName));
            Assert.notNull(baseEntity.getId(), "entity没有id");
            BaseMapper<BaseEntity> mapper = repositoryHandler.getMapper(tableName);
            repositoryHandler.fillUpdateEntity(baseEntity);
            publisher.publishEvent(new EntityUpdateByIdEvent(tableName, entity, Booleans.TRUE.getValue()));
            if(Constant.TABLE_DATASOURCE_MAP.containsKey(tableName)) {
                DynamicDataSource.push(Constant.TABLE_DATASOURCE_MAP.get(tableName));
            }
            int count = mapper.updateById(baseEntity);
            if(CacheUtil.exists(tableName)) {
                Long id = baseEntity.getId();
                CacheUtil.set(tableName, String.valueOf(id), selectList(tableName,
                        Wrappers.query(JSONObject.class).eq("id", id)).get(0));
                StaticLog.info("cache update: {} {}", tableName, id);
            }
            publisher.publishEvent(new EntityUpdateByIdEvent(tableName, entity, Booleans.FALSE.getValue()));
            return count;
        } finally {
            if(Constant.TABLE_DATASOURCE_MAP.containsKey(tableName)) {
                DynamicDataSource.poll();
            }
        }
    }

    @Override
    public <T> int update(String tableName, T entity, QueryWrapper<T> updateWrapper) {
        try {
            BaseEntity baseEntity = BeanUtil.toBean(entity, DynamicMapperUtil.getLoaderClass(tableName));
            BaseMapper mapper = repositoryHandler.getMapper(tableName);
            repositoryHandler.fillUpdateEntity(baseEntity);
            updateWrapper.eq("normal", Booleans.TRUE.getValue());
            publisher.publishEvent(new EntityUpdateEvent(tableName, entity, updateWrapper, Booleans.TRUE.getValue()));
            if(Constant.TABLE_DATASOURCE_MAP.containsKey(tableName)) {
                DynamicDataSource.push(Constant.TABLE_DATASOURCE_MAP.get(tableName));
            }
            int count = mapper.update(baseEntity, updateWrapper);
            if(CacheUtil.exists(tableName)) {
                refreshCache(tableName);
            }
            publisher.publishEvent(new EntityUpdateEvent(tableName, entity, updateWrapper, Booleans.FALSE.getValue()));
            return count;
        } finally {
            if(Constant.TABLE_DATASOURCE_MAP.containsKey(tableName)) {
                DynamicDataSource.poll();
            }
        }
    }

    @Override
    public <T> List<T> selectBatchIds(String tableName, Collection<Long> idList, Class<T> clazz) {
        try {
            if(Constant.TABLE_DATASOURCE_MAP.containsKey(tableName)) {
                DynamicDataSource.push(Constant.TABLE_DATASOURCE_MAP.get(tableName));
            }
            if(!CacheUtil.exists(tableName)) {
                return selectList(tableName, Wrappers.query(clazz).in("id", idList));
            }
            CacheChannel cache = J2Cache.getChannel();
            if(cache == null) {
                return selectList(tableName, Wrappers.query(clazz).in("id", idList));
            }
            Map<String, CacheObject> cacheMap = cache.get(tableName, idList.stream().map(String::valueOf).collect(Collectors.toList()));
            return idList.stream().map(id -> {
                CacheObject cacheObject = cacheMap.get(String.valueOf(id));
                if(cacheObject == null) {
                    return null;
                }
                StaticLog.info("cache get: {} {}", tableName, id);
                return JSONUtil.toBean((JSONObject) cacheObject.getValue(), clazz);
            }).collect(Collectors.toList());
        } finally {
            if(Constant.TABLE_DATASOURCE_MAP.containsKey(tableName)) {
                DynamicDataSource.poll();
            }
        }
    }

    @Override
    public <T> List<T> selectByMap(String tableName, Map<String, Object> columnMap, Class<T> clazz) {
        try {
            if(Constant.TABLE_DATASOURCE_MAP.containsKey(tableName)) {
                DynamicDataSource.push(Constant.TABLE_DATASOURCE_MAP.get(tableName));
            }
            if(!CacheUtil.exists(tableName)) {
                return IRepository.super.selectByMap(tableName, columnMap, clazz);
            }
            CacheChannel cache = J2Cache.getChannel();
            if(cache == null) {
                return IRepository.super.selectByMap(tableName, columnMap, clazz);
            }
            Map<String, CacheObject> cacheMap = cache.get(tableName, cache.keys(tableName));
            StaticLog.info("cache get all: {}", tableName);
            return cacheMap.values().stream().filter(item -> {
                JSONObject json = (JSONObject) item.getValue();
                for (Map.Entry<String, Object> entry : columnMap.entrySet()) {
                    if (!json.get(entry.getKey()).equals(entry.getValue())) {
                        return false;
                    }
                }
                return true;
            }).map(item -> JSONUtil.toBean((JSONObject) item.getValue(), clazz)).collect(Collectors.toList());
        } finally {
            if(Constant.TABLE_DATASOURCE_MAP.containsKey(tableName)) {
                DynamicDataSource.poll();
            }
        }
    }

    @Override
    public <T> Long selectCount(String tableName, QueryWrapper<T> queryWrapper) {
        try {
            if(Constant.TABLE_DATASOURCE_MAP.containsKey(tableName)) {
                DynamicDataSource.push(Constant.TABLE_DATASOURCE_MAP.get(tableName));
            }
            BaseMapper mapper = repositoryHandler.getMapper(tableName);
            queryWrapper.eq("normal", Booleans.TRUE.getValue());
            return mapper.selectCount(queryWrapper);
        } finally {
            if(Constant.TABLE_DATASOURCE_MAP.containsKey(tableName)) {
                DynamicDataSource.poll();
            }
        }
    }

    @Override
    public <T> List<T> selectList(String tableName, IPage<T> page, QueryWrapper<T> queryWrapper) {
        try {
            if(Constant.TABLE_DATASOURCE_MAP.containsKey(tableName)) {
                DynamicDataSource.push(Constant.TABLE_DATASOURCE_MAP.get(tableName));
            }
            BaseMapper mapper = repositoryHandler.getMapper(tableName);
            queryWrapper.eq("normal", Booleans.TRUE.getValue());
            List<T> list = mapper.selectList(page, queryWrapper);
            return BeanUtil.copyToList(list, queryWrapper.getEntityClass());
        } finally {
            if(Constant.TABLE_DATASOURCE_MAP.containsKey(tableName)) {
                DynamicDataSource.poll();
            }
        }
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }
}
