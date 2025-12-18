package cn.talins.mybatis.max.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 数据权限处理器接口 - 实现行级和列级数据权限控制
 * <p>
 * 该接口用于在数据访问层实现细粒度的数据权限控制，支持：
 * <ul>
 *     <li>行级权限：通过修改QueryWrapper添加额外的查询条件，限制用户只能访问特定范围的数据</li>
 *     <li>列级权限：通过修改实体对象，隐藏或脱敏用户无权访问的字段</li>
 * </ul>
 * </p>
 * 
 * <p>
 * 典型应用场景：
 * <ul>
 *     <li>多租户数据隔离：自动添加租户ID条件</li>
 *     <li>部门数据隔离：用户只能查看本部门及下级部门的数据</li>
 *     <li>敏感字段脱敏：对手机号、身份证号等敏感信息进行脱敏处理</li>
 *     <li>字段级权限控制：根据用户角色隐藏特定字段</li>
 * </ul>
 * </p>
 * 
 * <p>
 * 框架提供了空实现作为默认行为，用户可以自定义实现并注册为Spring Bean来启用数据权限功能。
 * </p>
 * 
 * @author talins
 * @see cn.talins.mybatis.max.sdk.repository.DataPermissionRepository 数据权限仓库实现
 */
public interface IDataPermissionHandler {

    /**
     * 添加行级数据权限
     * <p>
     * 在执行查询、更新、删除操作前调用，通过修改QueryWrapper添加额外的过滤条件。
     * </p>
     * 
     * <p>
     * 示例实现：
     * <pre>
     * public void addRowPermission(String tableName, QueryWrapper queryWrapper) {
     *     // 添加租户ID条件
     *     queryWrapper.eq("tenant_id", getCurrentTenantId());
     *     // 添加部门数据范围条件
     *     queryWrapper.in("dept_id", getCurrentUserDeptIds());
     * }
     * </pre>
     * </p>
     * 
     * @param tableName 表名
     * @param queryWrapper 查询条件包装器，可在其上添加额外的过滤条件
     * @param <T> 实体类型
     */
    <T> void addRowPermission(@NotBlank(message = "表名不能为空") String tableName, 
                              @NotNull(message = "查询条件不能为空") QueryWrapper<T> queryWrapper);

    /**
     * 添加列级数据权限（单个实体）
     * <p>
     * 在查询结果返回前调用，用于对单个实体进行字段级别的权限处理。
     * </p>
     * 
     * <p>
     * 示例实现：
     * <pre>
     * public User addColumnPermission(String tableName, User entity) {
     *     // 手机号脱敏
     *     entity.setPhone(maskPhone(entity.getPhone()));
     *     // 隐藏敏感字段
     *     if (!hasPermission("view_salary")) {
     *         entity.setSalary(null);
     *     }
     *     return entity;
     * }
     * </pre>
     * </p>
     * 
     * @param tableName 表名
     * @param entity 待处理的实体对象
     * @param <T> 实体类型
     * @return 处理后的实体对象
     */
    <T> T addColumnPermission(@NotBlank(message = "表名不能为空") String tableName, T entity);

    /**
     * 添加列级数据权限（实体列表）
     * <p>
     * 在查询结果返回前调用，用于对实体列表进行字段级别的权限处理。
     * 默认实现会遍历列表并调用单个实体的处理方法。
     * </p>
     * 
     * @param tableName 表名
     * @param entityList 待处理的实体列表
     * @param <T> 实体类型
     * @return 处理后的实体列表
     */
    <T> List<T> addColumnPermission(@NotBlank(message = "表名不能为空") String tableName, List<T> entityList);

}
