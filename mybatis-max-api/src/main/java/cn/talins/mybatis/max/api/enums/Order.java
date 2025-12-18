package cn.talins.mybatis.max.api.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 排序方式枚举 - 定义查询结果的排序方向
 * <p>
 * 用于{@link cn.talins.mybatis.max.api.pojo.Query}中，
 * 指定结果集的排序方式。
 * </p>
 * 
 * <p>
 * 示例：
 * <ul>
 *     <li>ASC: ORDER BY create_time ASC（升序，从小到大）</li>
 *     <li>DESC: ORDER BY create_time DESC（降序，从大到小）</li>
 * </ul>
 * </p>
 * 
 * @author talins
 * @see cn.talins.mybatis.max.api.pojo.Query 查询对象类
 */
@Getter
public enum Order {
    /**
     * 升序（从小到大）
     */
    ASC(1),

    /**
     * 降序（从大到小）
     */
    DESC(2);

    /**
     * 数据库存储值
     */
    private final Integer value;

    Order(Integer value) {
        this.value = value;
    }

    /**
     * 值到枚举的映射
     */
    static public Map<Integer, Order> MAP = new HashMap<>();
    
    /**
     * 名称到枚举的映射
     */
    static public Map<String, Order> NAME_MAP = new HashMap<>();

    static {
        for(Order order : Order.values()) {
            MAP.put(order.getValue(), order);
            NAME_MAP.put(order.name(), order);
        }
    }

}
