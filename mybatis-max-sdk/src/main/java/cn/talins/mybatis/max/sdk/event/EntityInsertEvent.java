package cn.talins.mybatis.max.sdk.event;

import cn.talins.mybatis.max.api.pojo.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;


/**
 * 实体插入事件 - 在执行INSERT操作时发布
 * <p>
 * 该事件在{@link cn.talins.mybatis.max.sdk.repository.BaseRepository#insert}方法中发布，
 * 分为前置事件和后置事件两次发布。
 * </p>
 * 
 * <p>
 * 监听示例：
 * <pre>
 * &#64;Component
 * public class InsertEventListener {
 *     
 *     &#64;EventListener
 *     public void onInsert(EntityInsertEvent event) {
 *         if (event.getIsBefore() == 1) {
 *             // 插入前处理
 *             log.info("即将插入: {}", event.getTableName());
 *         } else {
 *             // 插入后处理
 *             log.info("已插入ID: {}", event.getEntity().getId());
 *         }
 *     }
 * }
 * </pre>
 * </p>
 * 
 * @author talins
 * @see cn.talins.mybatis.max.sdk.repository.BaseRepository#insert 事件发布位置
 */
@Getter
@AllArgsConstructor
public class EntityInsertEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 操作的表名
     */
    private String tableName;

    /**
     * 插入的实体对象
     * <p>
     * 前置事件中包含待插入的数据，后置事件中包含已填充ID的数据。
     * </p>
     */
    private BaseEntity entity;

    /**
     * 是否为前置事件
     * <p>
     * 1: 前置事件（SQL执行前）
     * 0: 后置事件（SQL执行后）
     * </p>
     */
    private Integer isBefore;
}
