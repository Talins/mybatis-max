package cn.talins.mybatis.max.api.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 布尔值枚举 - 用于数据库中的布尔类型字段
 * <p>
 * 由于数据库中通常使用整数表示布尔值，该枚举提供了统一的映射：
 * <ul>
 *     <li>TRUE(1): 表示真/是/正常</li>
 *     <li>FALSE(0): 表示假/否/删除</li>
 * </ul>
 * </p>
 * 
 * <p>
 * 主要用于{@link cn.talins.mybatis.max.api.pojo.BaseEntity#getNormal()}字段，
 * 实现逻辑删除功能。
 * </p>
 * 
 * @author talins
 */
@Getter
public enum Booleans {
    /**
     * 真/是/正常状态
     */
    TRUE(1),

    /**
     * 假/否/删除状态
     */
    FALSE(0);

    /**
     * 数据库存储值
     */
    private final Integer value;

    Booleans(Integer value) {
        this.value = value;
    }

    /**
     * 值到枚举的映射
     */
    public static final Map<Integer, Booleans> MAP = new HashMap<>();
    
    /**
     * 名称到枚举的映射
     */
    public static final Map<String, Booleans> NAME_MAP = new HashMap<>();

    static {
        for(Booleans booleans : Booleans.values()) {
            MAP.put(booleans.getValue(), booleans);
            NAME_MAP.put(booleans.name(), booleans);
        }
    }
}
