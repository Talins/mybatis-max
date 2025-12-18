package cn.talins.mybatis.max.api.pojo;

import lombok.Data;

/**
 * 列元数据类 - 存储数据库列的结构信息
 * <p>
 * 该类用于存储单个数据库列的元数据信息，
 * 包括列名、数据类型、所属表名和注释等。
 * </p>
 * 
 * <p>
 * 框架使用此信息动态生成Entity类的字段，
 * 并根据typeCode确定Java类型映射。
 * </p>
 * 
 * @author talins
 * @see TableMetaData 表元数据
 * @see cn.talins.mybatis.max.sdk.DynamicMapperUtil 动态Mapper工具类
 */
@Data
public class ColumnMetaData {

    /**
     * 列名（数据库原始列名，通常为下划线命名）
     */
    private String columnName;

    /**
     * JDBC类型代码
     * <p>
     * 对应{@link java.sql.Types}中的常量值，
     * 用于确定Java类型映射。
     * </p>
     */
    private Integer typeCode;

    /**
     * 所属表名
     */
    private String tableName;

    /**
     * 列注释
     */
    private String remark;

}
