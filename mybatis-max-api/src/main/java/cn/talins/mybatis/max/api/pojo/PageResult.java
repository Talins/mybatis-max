package cn.talins.mybatis.max.api.pojo;

import cn.hutool.http.HttpStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 分页结果包装类 - 用于封装分页查询的响应结果
 * <p>
 * 该类在{@link Result}的基础上添加了分页信息，用于分页查询接口的响应。
 * </p>
 * 
 * <p>
 * 响应示例：
 * <pre>
 * {
 *     "code": 200,
 *     "message": "success",
 *     "data": [
 *         {"id": 1, "name": "张三"},
 *         {"id": 2, "name": "李四"}
 *     ],
 *     "page": {
 *         "pageSize": 10,
 *         "currentPage": 1,
 *         "total": 100
 *     }
 * }
 * </pre>
 * </p>
 * 
 * @param <T> 数据类型，通常为List
 * @author talins
 * @see Result 普通结果包装类
 * @see Page 分页信息类
 * @see PageRequest 分页请求包装类
 */
@Data
@AllArgsConstructor
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 响应状态码
     * <p>
     * 200表示成功，其他值表示失败。
     * </p>
     */
    private int code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 分页信息
     */
    private Page page;

    /**
     * 创建成功的分页响应
     * 
     * @param data 数据列表
     * @param pageSize 每页记录数
     * @param currentPage 当前页码
     * @param total 总记录数
     * @param <T> 数据类型
     * @return 分页响应对象
     */
    public static <T> PageResult<T> success(T data, Long pageSize, Long currentPage, Long total) {
        return new PageResult<>(HttpStatus.HTTP_OK, "success", data, new Page(pageSize, currentPage, total));
    }
}
