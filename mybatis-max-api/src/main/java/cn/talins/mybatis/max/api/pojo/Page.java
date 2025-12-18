package cn.talins.mybatis.max.api.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 分页信息类 - 用于封装分页查询的元数据
 * <p>
 * 该类包含分页查询结果的基本信息，通常作为{@link PageResult}的一部分返回给前端。
 * </p>
 * 
 * <p>
 * 响应示例：
 * <pre>
 * {
 *     "code": 200,
 *     "message": "success",
 *     "data": [...],
 *     "page": {
 *         "pageSize": 10,
 *         "currentPage": 1,
 *         "total": 100
 *     }
 * }
 * </pre>
 * </p>
 * 
 * @author talins
 * @see PageResult 分页结果包装类
 * @see PageRequest 分页请求包装类
 */
@Data
@AllArgsConstructor
public class Page implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 每页记录数
     */
    private Long pageSize;

    /**
     * 当前页码（从1开始）
     */
    private Long currentPage;

    /**
     * 总记录数
     */
    private Long total;
}
