package cn.talins.mybatis.max.sdk.repository.service;

import cn.talins.mybatis.max.sdk.repository.DataPermissionRepository;

/**
 * 抽象数据权限仓库服务类 - 带数据权限控制的服务抽象类
 * <p>
 * 该类继承自{@link AbstractBaseRepositoryService}，使用{@link DataPermissionRepository}
 * 作为底层仓库，自动应用数据权限控制。
 * </p>
 * 
 * <p>
 * 使用场景：
 * <ul>
 *     <li>需要数据权限控制的业务服务</li>
 *     <li>多租户应用的数据隔离</li>
 *     <li>需要字段级权限控制的场景</li>
 * </ul>
 * </p>
 * 
 * <p>
 * 使用示例：
 * <pre>
 * &#64;Service
 * public class OrderService extends AbstractDataPermissionRepositoryService {
 *     
 *     public OrderService(DataPermissionRepository dataPermissionRepository) {
 *         super(dataPermissionRepository);
 *     }
 *     
 *     &#64;Override
 *     protected String getTableName() {
 *         return "order";
 *     }
 * }
 * </pre>
 * </p>
 * 
 * @author talins
 * @see AbstractBaseRepositoryService 基础服务抽象类
 * @see DataPermissionRepository 数据权限仓库
 */
public abstract class AbstractDataPermissionRepositoryService extends AbstractBaseRepositoryService {
    
    /**
     * 构造函数
     * 
     * @param dataPermissionRepository 数据权限仓库实例
     */
    public AbstractDataPermissionRepositoryService(DataPermissionRepository dataPermissionRepository) {
        super(dataPermissionRepository);
    }
}
