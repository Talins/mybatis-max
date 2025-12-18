package cn.talins.mybatis.max.sdk.repository;

import java.util.Collection;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import cn.talins.mybatis.max.api.IDataPermissionHandler;
import cn.talins.mybatis.max.api.IRepositoryHandler;

/**
 * 数据权限仓库实现类 - 在BaseRepository基础上增加数据权限控制
 * <p>
 * 该类继承自{@link BaseRepository}，在执行数据操作前后自动应用数据权限：
 * <ul>
 *     <li>查询操作：自动添加行级权限条件，并对结果进行列级权限处理</li>
 *     <li>更新操作：自动添加行级权限条件，并对更新数据进行列级权限处理</li>
 *     <li>删除操作：自动添加行级权限条件</li>
 * </ul>
 * </p>
 * 
 * <p>
 * 使用场景：
 * <ul>
 *     <li>多租户数据隔离</li>
 *     <li>部门级数据权限控制</li>
 *     <li>敏感字段脱敏</li>
 * </ul>
 * </p>
 * 
 * <p>
 * 注意：需要自定义实现{@link IDataPermissionHandler}接口并注册为Spring Bean，
 * 否则使用默认的空实现（不进行任何权限控制）。
 * </p>
 * 
 * @author talins
 * @see BaseRepository 基础仓库实现
 * @see IDataPermissionHandler 数据权限处理器接口
 */
public class DataPermissionRepository extends BaseRepository {

    /**
     * 数据权限处理器
     */
    private final IDataPermissionHandler dataPermissionHandler;
    
    /**
     * 构造函数
     * 
     * @param repositoryHandler 仓库处理器
     * @param dataPermissionHandler 数据权限处理器
     */
    public DataPermissionRepository(IRepositoryHandler repositoryHandler, IDataPermissionHandler dataPermissionHandler) {
        super(repositoryHandler);
        this.dataPermissionHandler = dataPermissionHandler;
    }

    @Override
    public <T> int delete(String tableName, QueryWrapper<T> queryWrapper) {
        dataPermissionHandler.addRowPermission(tableName, queryWrapper);
        return super.delete(tableName, queryWrapper);
    }

    @Override
    public <T> int updateById(String tableName, T entity) {
        return super.updateById(tableName, dataPermissionHandler.addColumnPermission(tableName, entity));
    }

    @Override
    public <T> int update(String tableName, T entity, QueryWrapper<T> updateWrapper) {
        dataPermissionHandler.addRowPermission(tableName, updateWrapper);
        return super.update(tableName, dataPermissionHandler.addColumnPermission(tableName, entity), updateWrapper);
    }

    @Override
    public <T> List<T> selectBatchIds(String tableName, Collection<Long> idList, Class<T> clazz) {
        List<T> result = super.selectBatchIds(tableName, idList, clazz);
        return dataPermissionHandler.addColumnPermission(tableName, result);
    }

    @Override
    public <T> Long selectCount(String tableName, QueryWrapper<T> queryWrapper) {
        dataPermissionHandler.addRowPermission(tableName, queryWrapper);
        return super.selectCount(tableName, queryWrapper);
    }

    @Override
    public <T> List<T> selectList(String tableName, IPage<T> page, QueryWrapper<T> queryWrapper) {
        dataPermissionHandler.addRowPermission(tableName, queryWrapper);
        List<T> result = super.selectList(tableName, page, queryWrapper);
        return dataPermissionHandler.addColumnPermission(tableName, result);
    }
}
