package cn.talins.mybatis.max.sdk.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.Collection;


/**
 * 实体批量删除事件 - 在执行批量ID删除操作时发布
 * <p>
 * 该事件在{@link cn.talins.mybatis.max.sdk.repository.BaseRepository#deleteBatchIds}方法中发布，
 * 用于根据ID列表批量删除的操作。
 * </p>
 * 
 * <p>
 * 监听示例：
 * <pre>
 * &#64;Component
 * public class DeleteBatchEventListener {
 *     
 *     &#64;EventListener
 *     public void onDeleteBatch(EntityDeleteBatchEvent event) {
 *         if (event.getIsBefore() == 0) {
 *             // 删除后清理相关缓存或资源
 *             log.info("已删除{}条记录", event.getIdList().size());
 *         }
 *     }
 * }
 * </pre>
 * </p>
 * 
 * @author talins
 * @see EntityDeleteEvent 条件删除事件
 */
@Getter
@AllArgsConstructor
public class EntityDeleteBatchEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 操作的表名
     */
    private String tableName;

    /**
     * 要删除的ID列表
     */
    private Collection<Long> idList;

    /**
     * 是否为前置事件
     * <p>
     * 1: 前置事件（SQL执行前）
     * 0: 后置事件（SQL执行后）
     * </p>
     */
    private Integer isBefore;
}
