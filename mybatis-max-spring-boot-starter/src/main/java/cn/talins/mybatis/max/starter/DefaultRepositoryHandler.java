package cn.talins.mybatis.max.starter;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.talins.mybatis.max.api.IIdGenerator;
import cn.talins.mybatis.max.api.IRepositoryHandler;
import cn.talins.mybatis.max.api.enums.Booleans;
import cn.talins.mybatis.max.api.pojo.BaseEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.Date;


/**
 * 默认仓库处理器实现 - IRepositoryHandler接口的默认实现
 * <p>
 * 该类提供了框架默认的仓库处理逻辑：
 * <ul>
 *     <li>根据表名获取对应的Mapper Bean</li>
 *     <li>在插入时自动填充ID、normal、version、updateTime字段</li>
 *     <li>在更新时自动填充version、updateTime字段</li>
 * </ul>
 * </p>
 * 
 * <p>
 * Mapper Bean的命名规则：
 * <ul>
 *     <li>表名：user_info</li>
 *     <li>Mapper Bean名：userInfoMapper</li>
 * </ul>
 * </p>
 * 
 * <p>
 * 用户可以自定义实现IRepositoryHandler接口并注册为Spring Bean来覆盖默认行为，
 * 例如：添加租户ID字段、自定义ID生成策略等。
 * </p>
 * 
 * @author talins
 * @see IRepositoryHandler 接口定义
 * @see BeanConfiguration#mapperHandler 注册位置
 */
public class DefaultRepositoryHandler implements IRepositoryHandler {

    /**
     * ID生成器
     */
    private final IIdGenerator idGenerator;

    /**
     * 构造函数
     * 
     * @param idGenerator ID生成器
     */
    public DefaultRepositoryHandler(IIdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    /**
     * 根据表名获取对应的Mapper实例
     * <p>
     * 通过Spring容器获取动态生成的Mapper Bean。
     * Bean名称规则：表名转驼峰 + "Mapper"
     * </p>
     * 
     * @param tableName 表名（如：user_info）
     * @return Mapper实例
     */
    @Override
    public BaseMapper<BaseEntity> getMapper(String tableName) {
        // 表名转驼峰命名：user_info -> userInfoMapper
        return SpringUtil.getBean(StrUtil.toCamelCase(tableName) + "Mapper");
    }

    /**
     * 填充插入实体的公共字段
     * <p>
     * 自动填充以下字段：
     * <ul>
     *     <li>id: 如果为空，使用ID生成器生成</li>
     *     <li>normal: 如果为空，设置为1（正常状态）</li>
     *     <li>version: 设置为与ID相同的值</li>
     *     <li>updateTime: 设置为当前时间</li>
     * </ul>
     * </p>
     * 
     * @param entity 待插入的实体
     */
    @Override
    public void fillInsertEntity(BaseEntity entity) {
        Long id = entity.getId();
        // 如果ID为空，自动生成
        if(id == null) {
            id = idGenerator.nextId();
            entity.setId(id);
        }
        // 如果normal为空，设置为正常状态
        if(entity.getNormal() == null) {
            entity.setNormal(Booleans.TRUE.getValue());
        }
        // 版本号初始值与ID相同
        entity.setVersion(id);
        // 设置更新时间为当前时间
        entity.setUpdateTime(new Date());
    }

    /**
     * 填充更新实体的公共字段
     * <p>
     * 自动填充以下字段：
     * <ul>
     *     <li>version: 如果为空，使用ID生成器生成新版本号</li>
     *     <li>updateTime: 设置为当前时间</li>
     * </ul>
     * </p>
     * 
     * @param entity 待更新的实体
     */
    @Override
    public void fillUpdateEntity(BaseEntity entity) {
        // 如果版本号为空，生成新版本号
        if(entity.getVersion() == null) {
            entity.setVersion(idGenerator.nextId());
        }
        // 设置更新时间为当前时间
        entity.setUpdateTime(new Date());
    }
}
