package cn.talins.mybatis.max.api.pojo;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.util.Date;

/**
 * 基础实体类 - 所有数据库实体的公共父类
 * <p>
 * 该类定义了MyBatis-Max框架要求的公共字段，所有通过框架动态生成的实体类都会继承此类。
 * 这些字段由框架自动管理，用户通常不需要手动设置。
 * </p>
 * 
 * <p>
 * 数据库表设计建议：
 * <pre>
 * CREATE TABLE your_table (
 *     id BIGINT(19) PRIMARY KEY COMMENT '主键ID',
 *     normal TINYINT(3) DEFAULT 1 COMMENT '正常状态标识：1-正常，0-已删除',
 *     version BIGINT(19) COMMENT '乐观锁版本号',
 *     update_time VARCHAR(32) COMMENT '最后更新时间',
 *     extra TEXT COMMENT '扩展字段（JSON格式）',
 *     -- 其他业务字段...
 * );
 * </pre>
 * </p>
 * 
 * @author talins
 * @see cn.talins.mybatis.max.api.IRepositoryHandler#fillInsertEntity 插入时自动填充
 * @see cn.talins.mybatis.max.api.IRepositoryHandler#fillUpdateEntity 更新时自动填充
 */
@Data
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     * <p>
     * 由框架通过{@link cn.talins.mybatis.max.api.IIdGenerator}自动生成，
     * 默认使用雪花算法，保证全局唯一且趋势递增。
     * </p>
     */
    private Long id;

    /**
     * 正常状态标识（逻辑删除字段）
     * <p>
     * 值说明：
     * <ul>
     *     <li>1 - 正常状态</li>
     *     <li>0 - 已删除状态</li>
     * </ul>
     * 框架在查询时会自动添加 normal=1 的条件，实现逻辑删除功能。
     * </p>
     */
    @Min(value = 0, message = "normal值最小为0")
    @Max(value = 1, message = "normal值最大为1")
    private Integer normal;

    /**
     * 乐观锁版本号
     * <p>
     * 用于实现乐观锁机制，每次更新时会自动更新此字段。
     * 初始值与ID相同，后续更新时使用新生成的ID值。
     * </p>
     */
    private Long version;

    /**
     * 最后更新时间
     * <p>
     * 在插入和更新时由框架自动设置为当前时间。
     * </p>
     */
    private Date updateTime;

    /**
     * 扩展字段
     * <p>
     * 用于存储额外的业务数据，建议使用JSON格式。
     * 适用于存储不确定的、可变的附加信息。
     * </p>
     */
    private String extra;
}
