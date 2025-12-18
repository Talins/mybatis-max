package cn.talins.mybatis.max.api;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.ibatis.exceptions.TooManyResultsException;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通用数据仓库接口 - MyBatis-Max框架的核心数据访问接口
 * <p>
 * 该接口定义了对数据库表进行CRUD操作的通用方法，支持：
 * <ul>
 *     <li>基于表名的动态数据访问，无需为每个表创建单独的Mapper</li>
 *     <li>支持单条和批量的增删改查操作</li>
 *     <li>支持基于QueryWrapper的条件查询</li>
 *     <li>支持分页查询</li>
 *     <li>支持基于Map的条件查询（自动转换驼峰命名为下划线命名）</li>
 * </ul>
 * </p>
 * 
 * <p>
 * 使用示例：
 * <pre>
 * // 插入记录
 * repository.insert("user", userEntity);
 * 
 * // 根据ID查询
 * User user = repository.selectById("user", 1L, User.class);
 * 
 * // 条件查询
 * QueryWrapper&lt;User&gt; wrapper = Wrappers.query();
 * wrapper.eq("status", 1);
 * List&lt;User&gt; users = repository.selectList("user", wrapper);
 * </pre>
 * </p>
 * 
 * @author talins
 * @see IRepositoryService 业务层服务接口
 * @see IRepositoryHandler 仓库处理器接口
 */
public interface IRepository {

    /**
     * 插入一条记录
     * <p>
     * 插入前会自动填充BaseEntity的公共字段（id、normal、version、updateTime）。
     * </p>
     *
     * @param tableName 表名
     * @param entity 实体对象（Map或POJO）
     * @param <T> 实体类型
     * @return 插入记录的ID
     */
    <T> Long insert(@NotNull String tableName, @NotNull T entity);

    /**
     * 根据ID删除单条记录
     * <p>
     * 内部调用{@link #deleteBatchIds}实现。
     * </p>
     *
     * @param tableName 表名
     * @param id 主键ID
     * @return 删除的记录数（0或1）
     */
    default int deleteById(@NotNull String tableName, @NotNull Long id) {
        return deleteBatchIds(tableName, Collections.singletonList(id));
    }

    /**
     * 根据Map条件删除记录
     * <p>
     * Map的key为字段名（支持驼峰命名，自动转换为下划线），value为字段值。
     * 多个条件之间使用AND连接。
     * </p>
     *
     * @param tableName 表名
     * @param columnMap 条件Map（字段名 -> 字段值）
     * @param <T> 实体类型
     * @return 删除的记录数
     */
    default <T> int deleteByMap(@NotNull String tableName, @NotNull Map<String, Object> columnMap) {
        QueryWrapper<T> qw = Wrappers.query();
        return this.delete(tableName, qw.allEq(processColumnMap(columnMap)));
    }

    /**
     * 根据QueryWrapper条件删除记录
     * <p>
     * 会自动添加normal=1的条件，只删除正常状态的记录。
     * </p>
     *
     * @param tableName 表名
     * @param queryWrapper 查询条件包装器
     * @param <T> 实体类型
     * @return 删除的记录数
     */
    <T> int delete(@NotNull String tableName, @NotNull QueryWrapper<T> queryWrapper);

    /**
     * 根据ID列表批量删除记录
     *
     * @param tableName 表名
     * @param idList 主键ID列表（不能为空）
     * @return 删除的记录数
     */
    int deleteBatchIds(@NotNull String tableName, @NotEmpty Collection<Long> idList);

    /**
     * 根据ID更新记录
     * <p>
     * 实体对象必须包含id字段，更新前会自动填充version和updateTime字段。
     * </p>
     *
     * @param tableName 表名
     * @param entity 实体对象（必须包含id字段）
     * @param <T> 实体类型
     * @return 更新的记录数（0或1）
     */
    <T> int updateById(@NotNull String tableName, @NotNull T entity);

    /**
     * 根据QueryWrapper条件更新记录
     * <p>
     * 会自动添加normal=1的条件，只更新正常状态的记录。
     * 更新前会自动填充version和updateTime字段。
     * </p>
     *
     * @param tableName 表名
     * @param entity 实体对象（SET子句的内容）
     * @param updateWrapper 更新条件包装器（WHERE子句的内容）
     * @param <T> 实体类型
     * @return 更新的记录数
     */
    <T> int update(@NotNull String tableName, @NotNull T entity, @NotNull QueryWrapper<T> updateWrapper);

    /**
     * 根据ID查询单条记录
     * <p>
     * 如果启用了缓存，会优先从缓存读取。
     * </p>
     *
     * @param tableName 表名
     * @param id 主键ID
     * @param clazz 返回类型的Class对象
     * @param <T> 返回类型
     * @return 查询结果，不存在返回null
     */
    default <T> T selectById(@NotNull String tableName, @NotNull Long id, @NotNull Class<T> clazz) {
        return CollUtil.getFirst(selectBatchIds(tableName, Collections.singletonList(id), clazz));
    }

    /**
     * 根据ID列表批量查询记录
     * <p>
     * 如果启用了缓存，会优先从缓存读取。
     * </p>
     *
     * @param tableName 表名
     * @param idList 主键ID列表（不能为空）
     * @param clazz 返回类型的Class对象
     * @param <T> 返回类型
     * @return 查询结果列表
     */
    <T> List<T> selectBatchIds(@NotNull String tableName, @NotEmpty Collection<Long> idList, @NotNull Class<T> clazz);

    /**
     * 根据Map条件查询记录列表
     * <p>
     * Map的key为字段名（支持驼峰命名，自动转换为下划线），value为字段值。
     * 多个条件之间使用AND连接。
     * </p>
     *
     * @param tableName 表名
     * @param columnMap 条件Map（字段名 -> 字段值）
     * @param clazz 返回类型的Class对象
     * @param <T> 返回类型
     * @return 查询结果列表
     */
    default <T> List<T> selectByMap(@NotNull String tableName, @NotNull Map<String, Object> columnMap, @NotNull Class<T> clazz) {
        QueryWrapper<T> qw = Wrappers.query(clazz);
        Map<String, Object> processedMap = processColumnMap(columnMap);
        return this.selectList(tableName, qw.allEq(processedMap));
    }

    /**
     * 处理columnMap，将驼峰命名转换为下划线命名
     *
     * @param columnMap 原始Map
     * @return 转换后的Map
     */
    private Map<String, Object> processColumnMap(Map<String, Object> columnMap) {
        Map<String, Object> result = new HashMap<>();
        for (Map.Entry<String, Object> entry : columnMap.entrySet()) {
            String key = entry.getKey();
            String underlineKey = StrUtil.toUnderlineCase(key);
            result.put(underlineKey, entry.getValue());
        }
        return result;
    }


    /**
     * 根据QueryWrapper条件查询单条记录
     * <p>
     * 如果查询结果超过一条，会抛出{@link TooManyResultsException}异常。
     * </p>
     *
     * @param tableName 表名
     * @param queryWrapper 查询条件包装器
     * @param <T> 返回类型
     * @return 查询结果，不存在返回null
     * @throws TooManyResultsException 如果结果超过一条
     */
    default <T> T selectOne(@NotNull String tableName, @NotNull QueryWrapper<T> queryWrapper) {
        return this.selectOne(tableName, queryWrapper, true);
    }

    /**
     * 根据Map条件查询单条记录
     * <p>
     * 如果查询结果超过一条，返回第一条。
     * </p>
     *
     * @param tableName 表名
     * @param columnMap 条件Map（字段名 -> 字段值）
     * @param clazz 返回类型的Class对象
     * @param <T> 返回类型
     * @return 查询结果，不存在返回null
     */
    default <T> T selectOneByMap(@NotNull String tableName, @NotNull Map<String, Object> columnMap, @NotNull Class<T> clazz) {
        List<T> list = selectByMap(tableName, columnMap, clazz);
        int size = list.size();
        if (size >= 1) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 根据QueryWrapper条件查询单条记录（可控制是否抛出异常）
     *
     * @param tableName 表名
     * @param queryWrapper 查询条件包装器
     * @param throwEx 如果结果超过一条是否抛出异常，false则返回第一条
     * @param <T> 返回类型
     * @return 查询结果，不存在返回null
     * @throws TooManyResultsException 如果throwEx为true且结果超过一条
     */
    default <T> T selectOne(@NotNull String tableName, @NotNull QueryWrapper<T> queryWrapper, boolean throwEx) {
        List<T> list = this.selectList(tableName, queryWrapper);
        int size = list.size();
        if (size == 1) {
            return list.get(0);
        } else if (size > 1) {
            if (throwEx) {
                throw new TooManyResultsException("Expected one result (or null) to be returned by selectOne(), but found: " + list.size());
            }
            return list.get(0);
        }
        return null;
    }

    /**
     * 判断是否存在满足条件的记录
     *
     * @param tableName 表名
     * @param queryWrapper 查询条件包装器
     * @param <T> 实体类型
     * @return 存在返回true，否则返回false
     */
    default <T> boolean exists(@NotNull String tableName, @NotNull QueryWrapper<T> queryWrapper) {
        Long count = this.selectCount(tableName, queryWrapper);
        return null != count && count > 0;
    }

    /**
     * 根据QueryWrapper条件查询记录总数
     * <p>
     * 会自动添加normal=1的条件，只统计正常状态的记录。
     * </p>
     *
     * @param tableName 表名
     * @param queryWrapper 查询条件包装器
     * @param <T> 实体类型
     * @return 记录总数
     */
    <T> Long selectCount(@NotNull String tableName, @NotNull QueryWrapper<T> queryWrapper);

    /**
     * 根据Map条件查询记录总数
     *
     * @param tableName 表名
     * @param columnMap 条件Map（字段名 -> 字段值）
     * @param <T> 实体类型
     * @return 记录总数
     */
    default <T> Long selectCountByMap(@NotNull String tableName, @NotNull Map<String, Object> columnMap) {
        QueryWrapper<T> qw = Wrappers.query();
        return this.selectCount(tableName, qw.allEq(processColumnMap(columnMap)));
    }

    /**
     * 根据QueryWrapper条件查询记录列表
     * <p>
     * 会自动添加normal=1的条件，只查询正常状态的记录。
     * </p>
     *
     * @param tableName 表名
     * @param queryWrapper 查询条件包装器
     * @param <T> 返回类型
     * @return 查询结果列表
     */
    default <T> List<T> selectList(@NotNull String tableName, @NotNull QueryWrapper<T> queryWrapper) {
        return selectList(tableName, null, queryWrapper);
    }

    /**
     * 根据QueryWrapper条件分页查询记录列表
     * <p>
     * 会自动添加normal=1的条件，只查询正常状态的记录。
     * </p>
     *
     * @param tableName 表名
     * @param page 分页参数（可为null，表示不分页）
     * @param queryWrapper 查询条件包装器
     * @param <T> 返回类型
     * @return 查询结果列表
     */
    <T> List<T> selectList(@NotNull String tableName, @NotNull IPage<T> page, @NotNull QueryWrapper<T> queryWrapper);

    /**
     * 根据QueryWrapper条件分页查询，返回分页对象
     * <p>
     * 会自动设置分页对象的records属性。
     * </p>
     *
     * @param tableName 表名
     * @param page 分页参数
     * @param queryWrapper 查询条件包装器
     * @param <T> 返回类型
     * @param <P> 分页对象类型
     * @return 包含查询结果的分页对象
     */
    default <T, P extends IPage<T>> P selectPage(@NotNull String tableName, @NotNull P page, @NotNull QueryWrapper<T> queryWrapper) {
        List<T> list = selectList(tableName, page, queryWrapper);
        page.setRecords(list);
        return page;
    }

}
