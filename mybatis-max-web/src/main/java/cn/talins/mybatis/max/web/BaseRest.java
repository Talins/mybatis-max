package cn.talins.mybatis.max.web;


import cn.talins.mybatis.max.api.IRepository;
import cn.talins.mybatis.max.api.pojo.Query;
import cn.talins.mybatis.max.api.pojo.MapRequest;
import cn.talins.mybatis.max.api.pojo.PageRequest;
import cn.talins.mybatis.max.api.pojo.BaseRequest;
import cn.talins.mybatis.max.api.pojo.Result;
import cn.talins.mybatis.max.api.pojo.PageResult;
import cn.talins.mybatis.max.web.util.QueryUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 基础REST控制器 - 提供通用的数据访问REST API
 * <p>
 * 该控制器提供了一套完整的RESTful API，支持对任意表进行CRUD操作。
 * 所有接口都使用POST方法，通过URL路径参数指定表名。
 * </p>
 * 
 * <p>
 * API路径格式：/mybatis-max/{operation}/{tableName}
 * </p>
 * 
 * <p>
 * 支持的操作：
 * <ul>
 *     <li>insert: 插入记录</li>
 *     <li>deleteById: 根据ID删除</li>
 *     <li>deleteByMap: 根据条件删除</li>
 *     <li>delete: 根据QueryWrapper删除</li>
 *     <li>deleteBatchIds: 批量删除</li>
 *     <li>updateById: 根据ID更新</li>
 *     <li>update: 根据条件更新</li>
 *     <li>selectById: 根据ID查询</li>
 *     <li>selectBatchIds: 批量ID查询</li>
 *     <li>selectByMap: 根据Map条件查询</li>
 *     <li>selectOne: 查询单条记录</li>
 *     <li>selectList: 查询列表</li>
 *     <li>selectPage: 分页查询</li>
 *     <li>selectCount: 统计数量</li>
 *     <li>exists: 判断是否存在</li>
 * </ul>
 * </p>
 * 
 * <p>
 * 注意：该控制器使用DataPermissionRepository，自动应用数据权限控制。
 * </p>
 * 
 * @author talins
 * @see IRepository 数据仓库接口
 * @see QueryUtil 查询工具类
 */
@SuppressWarnings("unchecked")
@RestController
@RequestMapping("mybatis-max")
@Validated
public class BaseRest {

    /**
     * 数据权限仓库，自动应用行级和列级数据权限
     */
    @Resource
    private IRepository dataPermissionRepository;

    /**
     * 插入一条记录
     *
     * @param tableName 表名
     * @param request 请求参数
     */
    @PostMapping("insert/{tableName}")
    public Result<Long> insert(
            @PathVariable @NotBlank(message = "表名不能为空") String tableName,
            @RequestBody @Validated BaseRequest<Map<String, Object>> request) {
        return Result.success(dataPermissionRepository.insert(tableName, request.getParam()));
    }

    /**
     * 根据 ID 删除
     *
     * @param tableName 表名
     * @param request 请求参数
     */
    @PostMapping("deleteById/{tableName}")
    public Result<Integer> deleteById(
            @PathVariable @NotBlank(message = "表名不能为空") String tableName,
            @RequestBody @Validated BaseRequest<Long> request) {
        return Result.success(dataPermissionRepository.deleteById(tableName, request.getParam()));
    }

    /**
     * 根据 columnMap 条件，删除记录
     *
     * @param tableName 表名
     * @param request 请求参数
     */
    @PostMapping("deleteByMap/{tableName}")
    public Result<Integer> deleteByMap(
            @PathVariable @NotBlank(message = "表名不能为空") String tableName,
            @RequestBody @Validated BaseRequest<Map<String, Object>> request) {
        return Result.success(dataPermissionRepository.deleteByMap(tableName, request.getParam()));
    }

    /**
     * 根据 entity 条件，删除记录
     *
     * @param tableName 表名
     * @param request 请求参数
     */
    @PostMapping("delete/{tableName}")
    public Result<Integer> delete(
            @PathVariable @NotBlank(message = "表名不能为空") String tableName,
            @RequestBody @Validated BaseRequest<Query> request) {
        return Result.success(dataPermissionRepository.delete(tableName, QueryUtil.toQueryWrapper(request.getParam())));
    }

    /**
     * 删除（根据ID或实体 批量删除）
     *
     * @param tableName 表名
     * @param request 请求参数
     */
    @PostMapping("deleteBatchIds/{tableName}")
    public Result<Integer> deleteBatchIds(
            @PathVariable @NotBlank(message = "表名不能为空") String tableName,
            @RequestBody @Validated BaseRequest<Collection<Long>> request) {
        return Result.success(dataPermissionRepository.deleteBatchIds(tableName, request.getParam()));
    }

    /**
     * 根据 ID 修改
     *
     * @param tableName 表名
     * @param request 请求参数
     */
    @PostMapping("updateById/{tableName}")
    public Result<Integer> updateById(
            @PathVariable @NotBlank(message = "表名不能为空") String tableName,
            @RequestBody @Validated BaseRequest<Map<String, Object>> request) {
        return Result.success(dataPermissionRepository.updateById(tableName, request.getParam()));
    }

    /**
     * 根据 entity 条件，更新记录
     *
     * @param tableName 表名
     * @param request 请求参数
     */
    @PostMapping("update/{tableName}")
    public Result<Integer> update(
            @PathVariable @NotBlank(message = "表名不能为空") String tableName,
            @RequestBody @Validated MapRequest<Query> request) {
        return Result.success(dataPermissionRepository.update(tableName,
                request.getEntity(), QueryUtil.toQueryWrapper(request.getParam())));
    }

    /**
     * 根据 ID 查询
     *
     * @param tableName 表名
     * @param request 请求参数
     */
    @PostMapping("selectById/{tableName}")
    public Result<Map<String, Object>> selectById(
            @PathVariable @NotBlank(message = "表名不能为空") String tableName,
            @RequestBody @Validated BaseRequest<Long> request) {
        return Result.success(dataPermissionRepository.selectById(tableName, request.getParam(), Map.class));
    }

    /**
     * 查询（根据ID 批量查询）
     *
     * @param tableName 表名
     * @param request 请求参数
     */
    @PostMapping("selectBatchIds/{tableName}")
    public Result<List<Map<String, Object>>> selectBatchIds(
            @PathVariable @NotBlank(message = "表名不能为空") String tableName,
            @RequestBody @Validated BaseRequest<Collection<Long>> request) {
        return Result.success(dataPermissionRepository.selectBatchIds(tableName, request.getParam(), Map.class).stream()
                .map(m -> (Map<String, Object>) m)
                .collect(Collectors.toList()));
    }

    /**
     * 根据 columnMap 条件，查询记录
     *
     * @param tableName 表名
     * @param request 请求参数
     */
    @PostMapping("selectByMap/{tableName}")
    public Result<List<Map<String, Object>>> selectByMap(
            @PathVariable @NotBlank(message = "表名不能为空") String tableName,
            @RequestBody @Validated BaseRequest<Map<String, Object>> request) {
        return Result.success(dataPermissionRepository.selectByMap(tableName, request.getParam(), Map.class).stream()
                .map(m -> (Map<String, Object>) m)
                .collect(Collectors.toList()));
    }

    /**
     * 根据 columnMap 条件，查询单条记录
     *
     * @param tableName 表名
     * @param request 请求参数
     */
    @PostMapping("selectOneByMap/{tableName}")
    public Result<Map<String, Object>> selectOneByMap(
            @PathVariable @NotBlank(message = "表名不能为空") String tableName,
            @RequestBody @Validated BaseRequest<Map<String, Object>> request) {
        return Result.success((Map<String, Object>)dataPermissionRepository.selectOneByMap(tableName, request.getParam(), Map.class));
    }

    /**
     * 根据 QueryWrapper 条件，查询单条记录
     *
     * @param tableName 表名
     * @param request 请求参数
     */
    @PostMapping("selectOne/{tableName}")
    public Result<Map<String, Object>> selectOne(
            @PathVariable @NotBlank(message = "表名不能为空") String tableName,
            @RequestBody @Validated BaseRequest<Query> request) {
        return Result.success(dataPermissionRepository.selectOne(tableName, QueryUtil.toQueryWrapper(request.getParam())));
    }

    /**
     * 根据 QueryWrapper 条件，判断是否存在记录
     *
     * @param tableName 表名
     * @param request 请求参数
     * @return 是否存在记录
     */
    @PostMapping("exists/{tableName}")
    public Result<Boolean> exists(
            @PathVariable @NotBlank(message = "表名不能为空") String tableName,
            @RequestBody @Validated BaseRequest<Query> request) {
        return Result.success(dataPermissionRepository.exists(tableName, QueryUtil.toQueryWrapper(request.getParam())));
    }

    /**
     * 根据 QueryWrapper 条件，查询总记录数
     *
     * @param tableName 表名
     * @param request 请求参数
     */
    @PostMapping("selectCount/{tableName}")
    public Result<Long> selectCount(
            @PathVariable @NotBlank(message = "表名不能为空") String tableName,
            @RequestBody @Validated BaseRequest<Query> request) {
        return Result.success(dataPermissionRepository.selectCount(tableName, QueryUtil.toQueryWrapper(request.getParam())));
    }

    /**
     * 根据 columnMap 条件，查询总记录数
     *
     * @param tableName 表名
     * @param request 请求参数
     */
    @PostMapping("selectCountByMap/{tableName}")
    public Result<Long> selectCountByMap(
            @PathVariable @NotBlank(message = "表名不能为空") String tableName,
            @RequestBody @Validated BaseRequest<Map<String, Object>> request) {
        return Result.success(dataPermissionRepository.selectCountByMap(tableName, request.getParam()));
    }

    /**
     * 根据 QueryWrapper 条件，查询记录列表
     *
     * @param tableName 表名
     * @param request 请求参数
     */
    @PostMapping("selectList/{tableName}")
    public Result<List<Map<String, Object>>> selectList(
            @PathVariable @NotBlank(message = "表名不能为空") String tableName,
            @RequestBody @Validated BaseRequest<Query> request) {
        return Result.success(dataPermissionRepository.selectList(tableName, QueryUtil.toQueryWrapper(request.getParam())));
    }

    /**
     * 根据 QueryWrapper 条件，分页查询记录列表
     *
     * @param tableName 表名
     * @param request 请求参数
     */
    @PostMapping("selectPage/{tableName}")
    public PageResult<List<Map<String, Object>>> selectPage(
            @PathVariable @NotBlank(message = "表名不能为空") String tableName,
            @RequestBody @Validated PageRequest<Query> request) {
        Page<Map<String, Object>> page = dataPermissionRepository.selectPage(tableName,
                new Page<>(request.getPageNum(), request.getPageSize()),
                QueryUtil.toQueryWrapper(request.getParam()));

        return PageResult.success(page.getRecords(), page.getSize(), page.getCurrent(), page.getTotal());
    }

}
