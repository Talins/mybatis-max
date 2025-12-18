package cn.talins.mybatis.max.api.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 查询操作符枚举 - 定义查询条件的比较方式
 * <p>
 * 用于{@link cn.talins.mybatis.max.api.pojo.Condition}中，
 * 指定字段与值之间的比较关系。
 * </p>
 * 
 * <p>
 * 支持的操作符及其SQL对应：
 * <ul>
 *     <li>EQUAL: column = value</li>
 *     <li>NOT_EQUAL: column != value</li>
 *     <li>LIKE: column LIKE '%value%'</li>
 *     <li>IN: column IN (value1, value2, ...)</li>
 *     <li>BETWEEN: column BETWEEN value1 AND value2</li>
 *     <li>IS_NULL: column IS NULL</li>
 * </ul>
 * </p>
 * 
 * @author talins
 * @see cn.talins.mybatis.max.api.pojo.Condition 查询条件类
 * @see cn.talins.mybatis.max.web.util.QueryUtil 查询工具类
 */
@Getter
public enum Operator {
    /**
     * 等于 (=)
     * <p>参数数量：1</p>
     */
    EQUAL("="),
    
    /**
     * 小于 (<)
     * <p>参数数量：1</p>
     */
    LESS("<"),
    
    /**
     * 大于 (>)
     * <p>参数数量：1</p>
     */
    GREAT(">"),
    
    /**
     * 小于等于 (<=)
     * <p>参数数量：1</p>
     */
    LESS_EQUAL("<="),
    
    /**
     * 大于等于 (>=)
     * <p>参数数量：1</p>
     */
    GREAT_EQUAL(">="),
    
    /**
     * 不等于 (!=)
     * <p>参数数量：1</p>
     */
    NOT_EQUAL("!="),
    
    /**
     * 模糊匹配 (LIKE)
     * <p>参数数量：1，需要自行添加%通配符</p>
     */
    LIKE("LIKE"),
    
    /**
     * 不匹配 (NOT LIKE)
     * <p>参数数量：1，需要自行添加%通配符</p>
     */
    NOT_LIKE("NOT LIKE"),
    
    /**
     * 为空 (IS NULL)
     * <p>参数数量：0</p>
     */
    IS_NULL("IS NULL"),
    
    /**
     * 不为空 (IS NOT NULL)
     * <p>参数数量：0</p>
     */
    IS_NOT_NULL("IS NOT NULL"),
    
    /**
     * 包含 (IN)
     * <p>参数数量：多个</p>
     */
    IN("IN"),
    
    /**
     * 不包含 (NOT IN)
     * <p>参数数量：多个</p>
     */
    NOT_IN("NOT IN"),
    
    /**
     * 范围内 (BETWEEN)
     * <p>参数数量：2（起始值和结束值）</p>
     */
    BETWEEN("BETWEEN"),
    
    /**
     * 范围外 (NOT BETWEEN)
     * <p>参数数量：2（起始值和结束值）</p>
     */
    NOT_BETWEEN("NOT BETWEEN");

    /**
     * SQL操作符字符串
     */
    private final String value;

    Operator(String value) {
        this.value = value;
    }

    /**
     * SQL操作符到枚举的映射
     */
    public static Map<String, Operator> MAP = new HashMap<>();
    
    /**
     * 枚举名称到枚举的映射
     */
    public static Map<String, Operator> NAME_MAP = new HashMap<>();

    static {
        for(Operator operator : Operator.values()) {
            MAP.put(operator.getValue(), operator);
            NAME_MAP.put(operator.name(), operator);
        }
    }

}
