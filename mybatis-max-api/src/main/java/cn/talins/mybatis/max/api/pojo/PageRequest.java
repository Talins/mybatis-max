package cn.talins.mybatis.max.api.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 分页请求包装类 - 用于封装分页查询的请求参数
 * <p>
 * 该类继承了{@link BaseRequest}的功能，并添加了分页参数。
 * 用于需要分页的API接口。
 * </p>
 * 
 * <p>
 * 请求示例：
 * <pre>
 * {
 *     "pageNum": 1,
 *     "pageSize": 10,
 *     "param": {
 *         "conditionList": [
 *             {"column": "status", "operator": "EQUAL", "paramList": [1]}
 *         ],
 *         "orderMap": {"createTime": "DESC"}
 *     }
 * }
 * </pre>
 * </p>
 * 
 * @param <T> 查询参数的类型，通常为{@link Query}
 * @author talins
 * @see PageResult 分页结果包装类
 * @see Query 查询对象
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PageRequest<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 页码（从1开始）
     */
    @NotNull(message = "页码不能为空")
    @Min(value = 1, message = "页码最小值为1")
    private Integer pageNum;

    /**
     * 每页记录数
     */
    @NotNull(message = "每页记录数不能为空")
    @Min(value = 1, message = "每页记录数最小值为1")
    @Max(value = 1000, message = "每页记录数最大值为1000")
    private Integer pageSize;

    /**
     * 查询参数
     */
    @Valid
    private T param;

}
