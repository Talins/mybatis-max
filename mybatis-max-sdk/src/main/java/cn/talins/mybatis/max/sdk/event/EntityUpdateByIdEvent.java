package cn.talins.mybatis.max.sdk.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;


/**
 * 实体更新事件（根据ID更新） - 在执行根据ID的UPDATE操作时发布
 * <p>
 * 该事件在{@link cn.talins.mybatis.max.sdk.repository.BaseRepository#updateById}方法中发布，
 * 用于根据实体ID更新的操作。
 * </p>
 * 
 * <p>
 * 监听示例：
 * <pre>
 * &#64;Component
 * public class UpdateByIdEventListener {
 *     
 *     &#64;EventListener
 *     public void onUpdateById(EntityUpdateByIdEvent event) {
 *         if (event.getIsBefore() == 0) {
 *             // 更新后发送通知
 *             log.info("已更新记录");
 *         }
 *     }
 * }
 * </pre>
 * </p>
 * 
 * @param <T> 实体类型
 * @author talins
 * @see EntityUpdateEvent 条件更新事件
 */
@Getter
@AllArgsConstructor
public class EntityUpdateByIdEvent<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 操作的表名
     */
    private String tableName;

    /**
     * 更新的实体数据（必须包含ID字段）
     */
    private T entity;

    /**
     * 是否为前置事件
     * <p>
     * 1: 前置事件（SQL执行前）
     * 0: 后置事件（SQL执行后）
     * </p>
     */
    private Integer isBefore;
}
