package cn.talins.mybatis.max.api;

import cn.talins.mybatis.max.api.pojo.BaseEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 数据仓库处理器接口 - 定义数据持久化的核心处理逻辑
 * <p>
 * 该接口负责：
 * <ul>
 *     <li>根据表名获取对应的MyBatis Mapper实例（由框架在启动时动态生成）</li>
 *     <li>在数据插入前自动填充实体的公共字段（如ID、创建时间、版本号等）</li>
 *     <li>在数据更新前自动填充实体的公共字段（如更新时间、版本号等）</li>
 * </ul>
 * </p>
 * 
 * <p>
 * 框架提供了默认实现{@link cn.talins.mybatis.max.starter.DefaultRepositoryHandler}，
 * 用户也可以自定义实现以满足特殊需求（如自定义ID生成策略、添加租户字段等）。
 * </p>
 * 
 * @author talins
 * @see cn.talins.mybatis.max.starter.DefaultRepositoryHandler 默认实现
 * @see IIdGenerator ID生成器接口
 */
public interface IRepositoryHandler {

    /**
     * 获取表对应的Mapper实例
     * <p>
     * Mapper实例在应用启动时由{@link cn.talins.mybatis.max.starter.DynamicMapperBeanFactoryPostProcessor}
     * 根据数据库表结构动态生成并注册到Spring容器中。
     * </p>
     * 
     * @param tableName 数据库表名（如：user、order_info）
     * @return 对应表的BaseMapper实例，用于执行具体的SQL操作
     */
    BaseMapper<BaseEntity> getMapper(String tableName);

    /**
     * 填充插入实体的公共字段
     * <p>
     * 在执行INSERT操作前调用，通常用于：
     * <ul>
     *     <li>生成并设置主键ID（如果为空）</li>
     *     <li>设置normal字段为正常状态（逻辑删除标识）</li>
     *     <li>设置version字段（乐观锁版本号）</li>
     *     <li>设置updateTime字段为当前时间</li>
     * </ul>
     * </p>
     * 
     * @param entity 待插入的实体对象
     */
    void fillInsertEntity(BaseEntity entity);

    /**
     * 填充更新实体的公共字段
     * <p>
     * 在执行UPDATE操作前调用，通常用于：
     * <ul>
     *     <li>更新version字段（乐观锁版本号）</li>
     *     <li>更新updateTime字段为当前时间</li>
     * </ul>
     * </p>
     * 
     * @param entity 待更新的实体对象
     */
    void fillUpdateEntity(BaseEntity entity);

}
