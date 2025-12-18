package cn.talins.mybatis.max.api.pojo;

import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * 表元数据类 - 存储数据库表的结构信息
 * <p>
 * 该类用于在应用启动时从数据库读取表结构信息，
 * 并用于动态生成Entity类和Mapper接口。
 * </p>
 * 
 * <p>
 * 框架通过{@link cn.talins.mybatis.max.sdk.DynamicMapperUtil#getMetaDataMap}
 * 从数据库元数据中读取所有表的结构信息。
 * </p>
 * 
 * @author talins
 * @see ColumnMetaData 列元数据
 * @see cn.talins.mybatis.max.sdk.DynamicMapperUtil 动态Mapper工具类
 */
@Data
public class TableMetaData {

    /**
     * 表名
     */
    private String tableName;

    /**
     * 列信息列表
     */
    private List<ColumnMetaData> columnList;

    /**
     * 主键列名集合
     */
    private Set<String> primaryKeySet;

    /**
     * 索引列名集合
     */
    private Set<String> indexColumnSet;

    /**
     * 表注释
     */
    private String comment;

    /**
     * 数据源名称
     * <p>
     * 在多数据源场景下，标识该表属于哪个数据源。
     * </p>
     */
    private String dataSourceName;

}
