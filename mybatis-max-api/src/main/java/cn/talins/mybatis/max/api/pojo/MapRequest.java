package cn.talins.mybatis.max.api.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Map;

/**
 * Map请求包装类 - 用于封装带实体数据和查询参数的请求
 * <p>
 * 该类用于需要同时传递实体数据和查询条件的场景，如条件更新操作。
 * </p>
 * 
 * <p>
 * 请求示例（条件更新）：
 * <pre>
 * {
 *     "entity": {
 *         "status": 2,
 *         "updateBy": "admin"
 *     },
 *     "param": {
 *         "conditionList": [
 *             {"column": "id", "operator": "IN", "paramList": [1, 2, 3]}
 *         ]
 *     }
 * }
 * </pre>
 * </p>
 * 
 * @param <T> 查询参数的类型，通常为{@link Query}
 * @author talins
 * @see BaseRequest 基础请求包装类
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MapRequest<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 实体数据
     * <p>
     * 用于更新操作时指定要更新的字段和值。
     * </p>
     */
    @NotEmpty(message = "实体数据不能为空")
    private Map<String, Object> entity;

    /**
     * 查询参数
     * <p>
     * 用于指定更新或删除的条件。
     * </p>
     */
    @NotNull(message = "查询参数不能为空")
    @Valid
    private T param;

}
