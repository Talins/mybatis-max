package cn.talins.mybatis.max.api.pojo;

import cn.talins.mybatis.max.api.enums.Connect;
import cn.talins.mybatis.max.api.enums.Operator;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 查询条件类 - 用于构建动态查询条件
 * <p>
 * 该类表示一个查询条件，包含列名、操作符、参数值和连接方式。
 * 多个Condition可以组合成复杂的查询条件。
 * </p>
 * 
 * <p>
 * 支持的操作符：
 * <ul>
 *     <li>EQUAL: 等于 (=)</li>
 *     <li>NOT_EQUAL: 不等于 (!=)</li>
 *     <li>LIKE: 模糊匹配</li>
 *     <li>IN: 包含</li>
 *     <li>BETWEEN: 范围查询</li>
 *     <li>GREAT/LESS: 大于/小于</li>
 *     <li>IS_NULL/IS_NOT_NULL: 空值判断</li>
 * </ul>
 * </p>
 * 
 * <p>
 * 使用示例（链式调用）：
 * <pre>
 * // 构建条件：name = '张三'
 * Condition condition1 = Condition.newInstance()
 *     .setColumn("name")
 *     .setOperator(Operator.EQUAL)
 *     .addParam("张三");
 * 
 * // 构建条件：age BETWEEN 18 AND 30
 * Condition condition2 = Condition.newInstance()
 *     .setColumn("age")
 *     .setOperator(Operator.BETWEEN)
 *     .addParam(18)
 *     .addParam(30);
 * 
 * // 构建条件：status IN (1, 2, 3)
 * Condition condition3 = Condition.newInstance()
 *     .setColumn("status")
 *     .setOperator(Operator.IN)
 *     .addParams(Arrays.asList(1, 2, 3));
 * </pre>
 * </p>
 * 
 * @author talins
 * @see Query 查询对象，包含多个Condition
 * @see Operator 操作符枚举
 * @see Connect 连接方式枚举
 */
@Data
public class Condition implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 条件连接方式
     * <p>
     * 指定当前条件与前一个条件的连接方式：
     * <ul>
     *     <li>AND: 与（默认）</li>
     *     <li>OR: 或</li>
     * </ul>
     * </p>
     */
    @NotNull(message = "条件连接方式不能为空")
    private Connect connect = Connect.AND;

    /**
     * 列名（字段名）
     * <p>
     * 支持驼峰命名，框架会自动转换为下划线命名。
     * 例如：userName -> user_name
     * </p>
     */
    @NotBlank(message = "列名不能为空")
    @Pattern(regexp = "^[a-zA-Z_][a-zA-Z0-9_]*$", message = "列名格式不正确，只能包含字母、数字和下划线，且不能以数字开头")
    private String column = null;

    /**
     * 操作符
     * <p>
     * 指定查询的比较方式，默认为等于(EQUAL)。
     * </p>
     */
    @NotNull(message = "操作符不能为空")
    private Operator operator = Operator.EQUAL;

    /**
     * 参数值列表
     * <p>
     * 存储条件的参数值：
     * <ul>
     *     <li>EQUAL/LIKE等：使用第一个参数</li>
     *     <li>BETWEEN：使用前两个参数（起始值和结束值）</li>
     *     <li>IN：使用所有参数</li>
     *     <li>IS_NULL：不需要参数</li>
     * </ul>
     * </p>
     */
    @NotNull(message = "参数列表不能为空")
    private List<Object> paramList = new ArrayList<>();

    /**
     * 创建新的Condition实例
     * 
     * @return 新的Condition对象
     */
    public static Condition newInstance() {
        return new Condition();
    }

    /**
     * 设置列名（支持链式调用）
     * 
     * @param column 列名
     * @return 当前Condition对象
     */
    public Condition setColumn(String column) {
        this.column = column;
        return this;
    }

    /**
     * 设置操作符（支持链式调用）
     * 
     * @param operator 操作符
     * @return 当前Condition对象
     */
    public Condition setOperator(Operator operator) {
        this.operator = operator;
        return this;
    }

    /**
     * 添加单个参数值（支持链式调用）
     * 
     * @param param 参数值
     * @return 当前Condition对象
     */
    public Condition addParam(Object param) {
        this.paramList.add(param);
        return this;
    }

    /**
     * 批量添加参数值（支持链式调用）
     * 
     * @param param 参数值列表
     * @return 当前Condition对象
     */
    public Condition addParams(List<?> param) {
        this.paramList.addAll(param);
        return this;
    }

    /**
     * 设置连接方式（支持链式调用）
     * 
     * @param connect 连接方式
     * @return 当前Condition对象
     */
    public Condition setConnect(Connect connect) {
        this.connect = connect;
        return this;
    }

}
