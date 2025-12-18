package cn.talins.mybatis.max.api.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 基础请求包装类 - 用于封装API请求参数
 * <p>
 * 该类是一个通用的请求参数包装器，用于统一API接口的请求格式。
 * 所有通过REST API传入的参数都会被封装在param字段中。
 * </p>
 * 
 * <p>
 * 使用示例：
 * <pre>
 * // 请求JSON格式
 * {
 *     "param": {
 *         "name": "张三",
 *         "age": 25
 *     }
 * }
 * 
 * // Controller接收
 * &#64;PostMapping("/insert")
 * public Result insert(&#64;RequestBody &#64;Validated BaseRequest&lt;User&gt; request) {
 *     User user = request.getParam();
 *     // 处理业务逻辑
 * }
 * </pre>
 * </p>
 * 
 * @param <T> 请求参数的类型
 * @author talins
 * @see PageRequest 分页请求包装类
 * @see MapRequest 带实体的请求包装类
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BaseRequest<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 请求参数
     * <p>
     * 封装具体的业务请求数据，类型由泛型T指定。
     * </p>
     */
    @NotNull(message = "请求参数不能为空")
    @Valid
    private T param;

}
