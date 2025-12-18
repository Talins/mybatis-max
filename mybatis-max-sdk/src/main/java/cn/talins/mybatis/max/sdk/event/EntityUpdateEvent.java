package cn.talins.mybatis.max.sdk.event;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;


/**
 * 实体更新事件（条件更新） - 在执行条件UPDATE操作时发布
 * <p>
 * 该事件在{@link cn.talins.mybatis.max.sdk.repository.BaseRepository#update}方法中发布，
 * 用于基于QueryWrapper条件的更新操作。
 * </p>
 * 
 * <p>
 * 监听示例：
 * <pre>
 * &#64;Component
 * public class UpdateEventListener {
 *     
 *     &#64;EventListener
 *     public void onUpdate(EntityUpdateEvent event) {
 *         if (event.getIsBefore() == 1) {
 *             // 更新前记录原始数据
 *             log.info("即将更新表: {}", event.getTableName());
 *         }
 *     }
 * }
 * </pre>
 * </p>
 * 
 * @param <T> 实体类型
 * @author talins
 * @see EntityUpdateByIdEvent 根据ID更新事件
 */
@Getter
@AllArgsConstructor
public class EntityUpdateEvent<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 操作的表名
     */
    private String tableName;

    /**
     * 更新的实体数据（SET子句的内容）
     */
    private T entity;

    /**
     * 更新条件（WHERE子句的内容）
     */
    private QueryWrapper<T> updateWrapper;

    /**
     * 是否为前置事件
     * <p>
     * 1: 前置事件（SQL执行前）
     * 0: 后置事件（SQL执行后）
     * </p>
     */
    private Integer isBefore;
}
