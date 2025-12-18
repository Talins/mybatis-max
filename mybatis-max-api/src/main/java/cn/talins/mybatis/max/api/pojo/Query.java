package cn.talins.mybatis.max.api.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import cn.talins.mybatis.max.api.enums.Order;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 查询对象类 - 用于构建完整的查询请求
 * <p>
 * 该类封装了一个完整的查询请求，包含：
 * <ul>
 *     <li>查询列：指定要返回的字段</li>
 *     <li>查询条件：多个Condition组成的过滤条件</li>
 *     <li>排序规则：指定结果的排序方式</li>
 * </ul>
 * </p>
 * 
 * <p>
 * 使用示例：
 * <pre>
 * // 构建查询：SELECT name, age FROM user WHERE status = 1 ORDER BY create_time DESC
 * Query query = Query.newInstance()
 *     .addColumn("name")
 *     .addColumn("age")
 *     .addCondition("status", 1)
 *     .addOrderBy("createTime", Order.DESC);
 * 
 * // 复杂查询示例
 * Query complexQuery = Query.newInstance()
 *     .addCondition(Condition.newInstance()
 *         .setColumn("name")
 *         .setOperator(Operator.LIKE)
 *         .addParam("%张%"))
 *     .addCondition(Condition.newInstance()
 *         .setColumn("age")
 *         .setOperator(Operator.BETWEEN)
 *         .addParam(18)
 *         .addParam(30)
 *         .setConnect(Connect.AND));
 * </pre>
 * </p>
 * 
 * <p>
 * 该对象通常用于REST API的请求参数，框架会通过{@link cn.talins.mybatis.max.web.util.QueryUtil}
 * 将其转换为MyBatis-Plus的QueryWrapper。
 * </p>
 * 
 * @author talins
 * @see Condition 查询条件
 * @see cn.talins.mybatis.max.web.util.QueryUtil 查询工具类
 */
@Data
public class Query implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 查询列列表
     * <p>
     * 指定要返回的字段，为空时返回所有字段。
     * 支持驼峰命名，框架会自动转换为下划线命名。
     * </p>
     */
    @NotNull(message = "查询列列表不能为null")
    @Size(max = 100, message = "查询列数量不能超过100")
    private List<String> columnList = new ArrayList<>();

    /**
     * 排序规则映射
     * <p>
     * Key为列名，Value为排序方式（ASC/DESC）。
     * 使用LinkedHashMap保证排序字段的顺序。
     * </p>
     */
    @NotNull(message = "排序规则不能为null")
    @Size(max = 10, message = "排序字段数量不能超过10")
    private LinkedHashMap<String, Order> orderMap = new LinkedHashMap<>();

    /**
     * 查询条件列表
     * <p>
     * 存储所有的查询条件，按添加顺序组合。
     * </p>
     */
    @NotNull(message = "查询条件列表不能为null")
    @Size(max = 50, message = "查询条件数量不能超过50")
    @Valid
    private List<Condition> conditionList = new ArrayList<>();

    /**
     * 创建新的Query实例
     * 
     * @return 新的Query对象
     */
    public static Query newInstance() {
        return new Query();
    }

    /**
     * 添加查询列（支持链式调用）
     * 
     * @param column 列名
     * @return 当前Query对象
     */
    public Query addColumn(String column) {
        this.columnList.add(column);
        return this;
    }

    /**
     * 添加查询条件（支持链式调用）
     * 
     * @param condition 条件对象
     * @return 当前Query对象
     */
    public Query addCondition(Condition condition) {
        this.conditionList.add(condition);
        return this;
    }

    /**
     * 添加等值查询条件（简化方法，支持链式调用）
     * <p>
     * 等价于：addCondition(Condition.newInstance().setColumn(column).addParam(param))
     * </p>
     * 
     * @param column 列名
     * @param param 参数值
     * @return 当前Query对象
     */
    public Query addCondition(String column, Object param) {
        this.conditionList.add(Condition.newInstance().setColumn(column).addParam(param));
        return this;
    }

    /**
     * 添加排序规则（支持链式调用）
     * 
     * @param column 列名
     * @param order 排序方式
     * @return 当前Query对象
     */
    public Query addOrderBy(String column, Order order) {
        this.orderMap.put(column, order);
        return this;
    }

    /**
     * 添加排序规则（支持链式调用）
     * <p>
     * 与addOrderBy功能相同，保留用于兼容性。
     * </p>
     * 
     * @param column 列名
     * @param order 排序方式
     * @return 当前Query对象
     */
    public Query addOrderByName(String column, Order order) {
        this.orderMap.put(column, order);
        return this;
    }

}
