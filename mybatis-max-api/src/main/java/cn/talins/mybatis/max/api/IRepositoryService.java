package cn.talins.mybatis.max.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 通用数据仓库服务接口 - 业务层数据访问服务抽象
 * <p>
 * 该接口是{@link IRepository}的业务层封装，提供了不需要指定表名的数据访问方法。
 * 具体的表名由实现类通过{@code getTableName()}方法提供。
 * </p>
 * 
 * <p>
 * 主要功能：
 * <ul>
 *     <li>封装底层IRepository的调用，简化业务层代码</li>
 *     <li>提供类型安全的数据访问方法</li>
 *     <li>支持条件查询、分页查询等高级功能</li>
 * </ul>
 * </p>
 * 
 * <p>
 * 使用方式：继承{@link cn.talins.mybatis.max.sdk.repository.service.AbstractBaseRepositoryService}
 * 并实现{@code getTableName()}方法即可获得完整的CRUD功能。
 * </p>
 * 
 * @author talins
 * @see IRepository 底层数据仓库接口
 * @see cn.talins.mybatis.max.sdk.repository.service.AbstractBaseRepositoryService 抽象实现类
 */
public interface IRepositoryService {

    /**
     * 插入一条记录
     *
     * @param entity 实体对象
     * @param <T> 实体类型
     * @return 插入记录的ID
     */
    <T> Long insert(@NotNull(message = "实体对象不能为空") @Valid T entity);

    /**
     * 根据ID删除单条记录
     *
     * @param id 主键ID
     * @return 删除的记录数
     */
    int deleteById(@NotNull(message = "ID不能为空") Long id);

    /**
     * 根据Map条件删除记录
     *
     * @param columnMap 条件Map
     * @return 删除的记录数
     */
    int deleteByMap(@NotNull(message = "条件Map不能为空") Map<String, Object> columnMap);

    /**
     * 根据ID更新记录
     *
     * @param entity 实体对象
     * @param <T> 实体类型
     * @return 更新的记录数
     */
    <T> int updateById(@NotNull(message = "实体对象不能为空") @Valid T entity);

    /**
     * 根据QueryWrapper条件删除记录
     *
     * @param queryWrapper 查询条件包装器
     * @param <T> 实体类型
     * @return 删除的记录数
     */
    <T> int delete(@NotNull(message = "查询条件不能为空") QueryWrapper<T> queryWrapper);

    /**
     * 根据QueryWrapper条件更新记录
     *
     * @param entity 实体对象
     * @param queryWrapper 更新条件包装器
     * @param <T> 实体类型
     * @return 更新的记录数
     */
    <T> int update(@NotNull(message = "实体对象不能为空") @Valid T entity, 
                   @NotNull(message = "更新条件不能为空") QueryWrapper<T> queryWrapper);

    /**
     * 根据QueryWrapper条件查询单条记录
     *
     * @param queryWrapper 查询条件包装器
     * @param <T> 返回类型
     * @return 查询结果
     */
    <T> T selectOne(@NotNull(message = "查询条件不能为空") QueryWrapper<T> queryWrapper);

    /**
     * 根据QueryWrapper条件查询记录列表
     *
     * @param queryWrapper 查询条件包装器
     * @param <T> 返回类型
     * @return 查询结果列表
     */
    <T> Collection<T> selectList(@NotNull(message = "查询条件不能为空") QueryWrapper<T> queryWrapper);

    /**
     * 根据ID列表批量删除记录
     *
     * @param idList 主键ID列表
     * @return 删除的记录数
     */
    int deleteBatchIds(@NotEmpty(message = "ID列表不能为空") Collection<Long> idList);

    /**
     * 根据ID查询单条记录
     *
     * @param id 主键ID
     * @param clazz 返回类型的Class对象
     * @param <T> 返回类型
     * @return 查询结果
     */
    <T> T selectById(@NotNull(message = "ID不能为空") Long id, 
                     @NotNull(message = "返回类型不能为空") Class<T> clazz);

    /**
     * 根据ID列表批量查询记录
     *
     * @param idList 主键ID列表
     * @param clazz 返回类型的Class对象
     * @param <T> 返回类型
     * @return 查询结果列表
     */
    <T> Collection<T> selectBatchIds(@NotEmpty(message = "ID列表不能为空") Collection<Long> idList, 
                                     @NotNull(message = "返回类型不能为空") Class<T> clazz);

    /**
     * 根据Map条件查询记录列表
     *
     * @param columnMap 条件Map
     * @param clazz 返回类型的Class对象
     * @param <T> 返回类型
     * @return 查询结果列表
     */
    <T> List<T> selectByMap(@NotNull(message = "条件Map不能为空") Map<String, Object> columnMap, 
                            @NotNull(message = "返回类型不能为空") Class<T> clazz);

    /**
     * 根据Map条件查询单条记录
     *
     * @param columnMap 条件Map
     * @param clazz 返回类型的Class对象
     * @param <T> 返回类型
     * @return 查询结果
     */
    <T> T selectOneByMap(@NotNull(message = "条件Map不能为空") Map<String, Object> columnMap, 
                         @NotNull(message = "返回类型不能为空") Class<T> clazz);

    /**
     * 根据QueryWrapper条件查询单条记录（可控制是否抛出异常）
     *
     * @param queryWrapper 查询条件包装器
     * @param throwEx 如果结果超过一条是否抛出异常
     * @param <T> 返回类型
     * @return 查询结果
     */
    <T> T selectOne(@NotNull(message = "查询条件不能为空") QueryWrapper<T> queryWrapper, boolean throwEx);

    /**
     * 判断是否存在满足条件的记录
     *
     * @param queryWrapper 查询条件包装器
     * @param <T> 实体类型
     * @return 存在返回true，否则返回false
     */
    <T> boolean exists(@NotNull(message = "查询条件不能为空") QueryWrapper<T> queryWrapper);

    /**
     * 根据QueryWrapper条件查询记录总数
     *
     * @param queryWrapper 查询条件包装器
     * @param <T> 实体类型
     * @return 记录总数
     */
    <T> Long selectCount(@NotNull(message = "查询条件不能为空") QueryWrapper<T> queryWrapper);

    /**
     * 根据Map条件查询记录总数
     *
     * @param columnMap 条件Map
     * @return 记录总数
     */
    Long selectCountByMap(@NotNull(message = "条件Map不能为空") Map<String, Object> columnMap);

    /**
     * 根据QueryWrapper条件分页查询记录列表
     *
     * @param page 分页参数
     * @param queryWrapper 查询条件包装器
     * @param <T> 返回类型
     * @return 查询结果列表
     */
    <T> List<T> selectList(@NotNull(message = "分页参数不能为空") IPage<T> page, 
                           @NotNull(message = "查询条件不能为空") QueryWrapper<T> queryWrapper);

    /**
     * 根据QueryWrapper条件分页查询，返回分页对象
     *
     * @param page 分页参数
     * @param queryWrapper 查询条件包装器
     * @param <T> 返回类型
     * @param <P> 分页对象类型
     * @return 包含查询结果的分页对象
     */
    <T, P extends IPage<T>> P selectPage(@NotNull(message = "分页参数不能为空") P page, 
                                         @NotNull(message = "查询条件不能为空") QueryWrapper<T> queryWrapper);
}
