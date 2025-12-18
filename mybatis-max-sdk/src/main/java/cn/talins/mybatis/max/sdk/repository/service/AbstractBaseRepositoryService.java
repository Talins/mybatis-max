package cn.talins.mybatis.max.sdk.repository.service;

import cn.talins.mybatis.max.api.IRepository;
import cn.talins.mybatis.max.api.IRepositoryService;
import cn.talins.mybatis.max.sdk.repository.BaseRepository;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 抽象基础仓库服务类 - IRepositoryService接口的抽象实现
 * <p>
 * 该类封装了{@link BaseRepository}的调用，子类只需实现{@link #getTableName()}方法
 * 指定操作的表名，即可获得完整的CRUD功能。
 * </p>
 * 
 * <p>
 * 使用示例：
 * <pre>
 * &#64;Service
 * public class UserService extends AbstractBaseRepositoryService {
 *     
 *     public UserService(BaseRepository baseRepository) {
 *         super(baseRepository);
 *     }
 *     
 *     &#64;Override
 *     protected String getTableName() {
 *         return "user";
 *     }
 *     
 *     // 可以添加自定义业务方法
 *     public User findByUsername(String username) {
 *         return selectOneByMap(Map.of("username", username), User.class);
 *     }
 * }
 * </pre>
 * </p>
 * 
 * @author talins
 * @see IRepositoryService 服务接口定义
 * @see AbstractDataPermissionRepositoryService 带数据权限的服务抽象类
 */
@Validated
public abstract class AbstractBaseRepositoryService implements IRepositoryService {

    /**
     * 底层数据仓库
     */
    private final IRepository baseRepository;

    /**
     * 构造函数
     * 
     * @param baseRepository 基础仓库实例
     */
    public AbstractBaseRepositoryService(BaseRepository baseRepository) {
        this.baseRepository = baseRepository;
    }

    /**
     * 获取操作的表名
     * <p>
     * 子类必须实现此方法，返回要操作的数据库表名。
     * </p>
     * 
     * @return 表名
     */
    protected abstract String getTableName();
    @Override
    public <T> Long insert(T entity) {
        return baseRepository.insert(getTableName(),entity);
    }

    @Override
    public int deleteById(Long id) {
        return baseRepository.deleteById(getTableName(), id);
    }

    @Override
    public int deleteByMap(Map<String, Object> columnMap) {
        return baseRepository.deleteByMap(getTableName(), columnMap);
    }

    @Override
    public <T> int updateById(T entity) {
        return baseRepository.updateById(getTableName(), entity);
    }

    @Override
    public <T> int delete(QueryWrapper<T> queryWrapper) {
        return baseRepository.delete(getTableName(), queryWrapper);
    }

    @Override
    public <T> int update(T entity, QueryWrapper<T> queryWrapper) {
        return baseRepository.update(getTableName(), entity, queryWrapper);
    }

    @Override
    public <T> T selectOne(QueryWrapper<T> queryWrapper) {
        return baseRepository.selectOne(getTableName(), queryWrapper);
    }

    @Override
    public <T> Collection<T> selectList(QueryWrapper<T> queryWrapper) {
        return baseRepository.selectList(getTableName(), queryWrapper);
    }

    @Override
    public int deleteBatchIds(Collection<Long> idList) {
        return baseRepository.deleteBatchIds(getTableName(), idList);
    }

    @Override
    public <T> T selectById(Long id, Class<T> clazz) {
        return baseRepository.selectById(getTableName(), id, clazz);
    }

    @Override
    public <T> Collection<T> selectBatchIds(Collection<Long> idList, Class<T> clazz) {
        return baseRepository.selectBatchIds(getTableName(), idList, clazz);
    }

    @Override
    public <T> List<T> selectByMap(Map<String, Object> columnMap, Class<T> clazz) {
        return baseRepository.selectByMap(getTableName(), columnMap, clazz);
    }

    @Override
    public <T> T selectOneByMap(Map<String, Object> columnMap, Class<T> clazz) {
        return baseRepository.selectOneByMap(getTableName(), columnMap, clazz);
    }

    @Override
    public <T> T selectOne(QueryWrapper<T> queryWrapper, boolean throwEx) {
        return baseRepository.selectOne(getTableName(), queryWrapper, throwEx);
    }

    @Override
    public <T> boolean exists(QueryWrapper<T> queryWrapper) {
        return baseRepository.exists(getTableName(), queryWrapper);
    }

    @Override
    public <T> Long selectCount(QueryWrapper<T> queryWrapper) {
        return baseRepository.selectCount(getTableName(), queryWrapper);
    }

    @Override
    public Long selectCountByMap(Map<String, Object> columnMap) {
        return baseRepository.selectCountByMap(getTableName(), columnMap);
    }

    @Override
    public <T> List<T> selectList(IPage<T> page, QueryWrapper<T> queryWrapper) {
        return baseRepository.selectList(getTableName(), page, queryWrapper);
    }

    @Override
    public <T, P extends IPage<T>> P selectPage(P page, QueryWrapper<T> queryWrapper) {
        return baseRepository.selectPage(getTableName(), page, queryWrapper);
    }
}
