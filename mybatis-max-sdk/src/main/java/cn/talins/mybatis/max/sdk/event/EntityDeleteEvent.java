package cn.talins.mybatis.max.sdk.event;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;


/**
 * 实体删除事件（条件删除） - 在执行条件DELETE操作时发布
 * <p>
 * 该事件在{@link cn.talins.mybatis.max.sdk.repository.BaseRepository#delete}方法中发布，
 * 用于基于QueryWrapper条件的删除操作。
 * </p>
 * 
 * <p>
 * 监听示例：
 * <pre>
 * &#64;Component
 * public class DeleteEventListener {
 *     
 *     &#64;EventListener
 *     public void onDelete(EntityDeleteEvent event) {
 *         if (event.getIsBefore() == 1) {
 *             // 删除前可以记录将要删除的数据
 *             log.info("即将删除表: {}", event.getTableName());
 *         }
 *     }
 * }
 * </pre>
 * </p>
 * 
 * @param <T> 实体类型
 * @author talins
 * @see EntityDeleteBatchEvent 批量ID删除事件
 */
@Getter
@AllArgsConstructor
public class EntityDeleteEvent<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 操作的表名
     */
    private String tableName;

    /**
     * 删除条件
     */
    private QueryWrapper<T> queryWrapper;

    /**
     * 是否为前置事件
     * <p>
     * 1: 前置事件（SQL执行前）
     * 0: 后置事件（SQL执行后）
     * </p>
     */
    private Integer isBefore;
}
