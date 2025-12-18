package cn.talins.mybatis.max.api.enums;

import lombok.Getter;
import java.util.HashMap;
import java.util.Map;

/**
 * 条件连接方式枚举 - 定义多个查询条件之间的逻辑关系
 * <p>
 * 用于{@link cn.talins.mybatis.max.api.pojo.Condition}中，
 * 指定当前条件与前一个条件的连接方式。
 * </p>
 * 
 * <p>
 * 示例：
 * <ul>
 *     <li>AND: WHERE name = '张三' AND age > 18</li>
 *     <li>OR: WHERE name = '张三' OR name = '李四'</li>
 * </ul>
 * </p>
 * 
 * @author talins
 * @see cn.talins.mybatis.max.api.pojo.Condition 查询条件类
 */
@Getter
public enum Connect {
    /**
     * 与（AND）- 两个条件都必须满足
     */
    AND(1),

    /**
     * 或（OR）- 满足任一条件即可
     */
    OR(2);

    /**
     * 数据库存储值
     */
    private final Integer value;

    Connect(Integer value) {
        this.value = value;
    }

    /**
     * 值到枚举的映射
     */
    public static Map<Integer, Connect> MAP = new HashMap<>();

    /**
     * 名称到枚举的映射
     */
    public static Map<String, Connect> NAME_MAP = new HashMap<>();

    static {
        for(Connect connect : Connect.values()) {
            MAP.put(connect.getValue(), connect);
            NAME_MAP.put(connect.name(), connect);
        }
    }

}
