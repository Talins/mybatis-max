package cn.talins.mybatis.max.sdk;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.talins.mybatis.max.api.pojo.BaseEntity;
import cn.talins.mybatis.max.api.pojo.ColumnMetaData;
import cn.talins.mybatis.max.api.pojo.TableMetaData;
import groovy.lang.GroovyClassLoader;
import org.apache.ibatis.type.IntegerTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.apache.ibatis.type.TypeReference;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static cn.talins.mybatis.max.sdk.common.Constant.ENTITY_TEMPLATE;
import static cn.talins.mybatis.max.sdk.common.Constant.MAPPER_TEMPLATE;


/**
 * 动态Mapper工具类 - MyBatis-Max框架的核心组件
 * <p>
 * 该类负责在应用启动时：
 * <ol>
 *     <li>从数据库读取所有表的元数据信息</li>
 *     <li>使用Groovy动态生成每个表对应的Entity类</li>
 *     <li>使用Groovy动态生成每个表对应的Mapper接口</li>
 * </ol>
 * </p>
 * 
 * <p>
 * 工作流程：
 * <pre>
 * 1. 获取数据库连接 -> 读取DatabaseMetaData
 * 2. 遍历所有表 -> 获取表名、列信息、主键、索引
 * 3. 根据表结构 -> 使用模板生成Entity类源码
 * 4. 根据Entity -> 使用模板生成Mapper接口源码
 * 5. 使用GroovyClassLoader -> 动态编译并加载类
 * </pre>
 * </p>
 * 
 * <p>
 * 生成的Entity类示例：
 * <pre>
 * package entity;
 * public class User extends BaseEntity {
 *     private String name;
 *     private Integer age;
 *     // getter/setter...
 * }
 * </pre>
 * </p>
 * 
 * <p>
 * 生成的Mapper接口示例：
 * <pre>
 * package mapper;
 * public interface UserMapper extends BaseMapper&lt;User&gt; {}
 * </pre>
 * </p>
 * 
 * @author talins
 * @see cn.talins.mybatis.max.starter.DynamicMapperBeanFactoryPostProcessor 使用此工具类的处理器
 * @see cn.talins.mybatis.max.sdk.common.Constant 模板定义
 */
public class DynamicMapperUtil {
    /**
     * Groovy类加载器，用于动态编译和加载生成的类
     */
    private static final GroovyClassLoader CLASS_LOADER = new GroovyClassLoader();
    
    /**
     * MyBatis类型处理器注册表，用于JDBC类型到Java类型的映射
     */
    private static final TypeHandlerRegistry TYPE_HANDLER_REGISTRY = new TypeHandlerRegistry();

    static {
        // 注册TINYINT类型使用Integer处理器（MySQL的TINYINT映射为Integer）
        TYPE_HANDLER_REGISTRY.register(JdbcType.TINYINT, new IntegerTypeHandler());
    }
    
    /**
     * BaseEntity中已定义的字段名集合，生成Entity时需要排除这些字段
     */
    public static final Set<String> FIELD_NAME_SET = Arrays.stream(ReflectUtil.getFields(BaseEntity.class))
            .map(Field::getName).collect(Collectors.toSet());

    /**
     * 获取数据源中所有表的元数据信息
     * <p>
     * 支持单数据源和动态数据源两种模式：
     * <ul>
     *     <li>单数据源：直接读取该数据源的表信息</li>
     *     <li>动态数据源：遍历所有子数据源，合并表信息</li>
     * </ul>
     * </p>
     * 
     * @param dataSource 数据源（可以是普通DataSource或DynamicDataSource）
     * @return 表名到TableMetaData的映射
     * @throws SQLException 数据库访问异常
     */
    public static Map<String, TableMetaData> getMetaDataMap(DataSource dataSource) throws SQLException {
        if(dataSource instanceof DynamicDataSource) {
            Map<String, TableMetaData> metaDataMap = new HashMap<>();
            DynamicDataSource dynamicDataSource = (DynamicDataSource) dataSource;
            for(Map.Entry<String, DataSource> ds : dynamicDataSource.getDataSourceMap().entrySet()) {
                metaDataMap.putAll(getSingleMetaDataMap(ds.getKey(), ds.getValue()));
            }
            return metaDataMap;
        } else {
            return getSingleMetaDataMap(null, dataSource);
        }
    }

    /**
     * 从单个数据源获取表元数据
     * <p>
     * 通过JDBC的DatabaseMetaData接口读取表结构信息，包括：
     * <ul>
     *     <li>表名和表注释</li>
     *     <li>列名、类型和注释</li>
     *     <li>主键信息</li>
     *     <li>索引信息</li>
     * </ul>
     * </p>
     * 
     * @param dataSourceName 数据源名称（用于多数据源场景）
     * @param dataSource 数据源实例
     * @return 表名到TableMetaData的映射
     * @throws SQLException 数据库访问异常
     */
    private static Map<String, TableMetaData> getSingleMetaDataMap(String dataSourceName, DataSource dataSource) throws SQLException {
        Connection connection = dataSource.getConnection();
        DatabaseMetaData metaData = connection.getMetaData();
        // 获取所有表信息
        ResultSet tablesResult = metaData.getTables(connection.getCatalog(), null, null, null);
        // 获取所有列信息
        ResultSet columnsResult = metaData.getColumns(connection.getCatalog(), null, null, null);

        // 解析表信息
        Map<String, TableMetaData> tableMetaDataMap = getTableMetaDataMap(tablesResult);
        // 解析列信息并按表名分组
        Map<String, List<ColumnMetaData>> columnMetaDataListMap = getColumnMetaDataListMap(columnsResult);

        // 组装完整的表元数据
        for(Map.Entry<String, TableMetaData> entry : tableMetaDataMap.entrySet()) {
            List<ColumnMetaData> columnMetaDataList = columnMetaDataListMap.get(entry.getKey());
            if(CollUtil.isEmpty(columnMetaDataList)) {
                continue;
            }
            TableMetaData tableMetaData = entry.getValue();
            tableMetaData.setDataSourceName(dataSourceName);
            tableMetaData.setColumnList(columnMetaDataList);
            // 获取主键和索引信息
            Map<String, Set<String>> otherColumnInfoMap = getOtherColumnInfoMap(metaData, connection.getCatalog(), entry.getKey());
            tableMetaData.setIndexColumnSet(otherColumnInfoMap.get("indexColumnSet"));
            tableMetaData.setPrimaryKeySet(otherColumnInfoMap.get("primaryKeySet"));
        }
        return tableMetaDataMap;
    }

    /**
     * 获取表的主键和索引信息
     * 
     * @param metaData 数据库元数据
     * @param catalog 数据库目录名
     * @param tableName 表名
     * @return 包含primaryKeySet和indexColumnSet的映射
     * @throws SQLException 数据库访问异常
     */
    private static Map<String, Set<String>> getOtherColumnInfoMap(DatabaseMetaData metaData, String catalog, String tableName) throws SQLException {
        ResultSet indexInfoResult = metaData.getIndexInfo(catalog, null, tableName, false, false);
        Map<String, Set<String>> result = new LinkedHashMap<>();
        Set<String> primaryKeySet = new HashSet<>();
        Set<String> indexColumnSet = new HashSet<>();
        while (indexInfoResult.next()) {
            // 只处理单列索引（ORDINAL_POSITION为1）
            if(indexInfoResult.getInt("ORDINAL_POSITION") > 1) {
                continue;
            }
            // 区分主键和普通索引
            if("PRIMARY".equals(indexInfoResult.getString("INDEX_NAME"))) {
                primaryKeySet.add(indexInfoResult.getString("COLUMN_NAME"));
            } else {
                indexColumnSet.add(indexInfoResult.getString("COLUMN_NAME"));
            }
        }
        result.put("indexColumnSet", indexColumnSet);
        result.put("primaryKeySet", primaryKeySet);
        return result;
    }

    /**
     * 解析表信息ResultSet，构建表元数据映射
     * 
     * @param tablesResult 表信息结果集
     * @return 表名到TableMetaData的映射
     * @throws SQLException 数据库访问异常
     */
    private static Map<String, TableMetaData> getTableMetaDataMap(ResultSet tablesResult) throws SQLException {
        Map<String, TableMetaData> result = new LinkedHashMap<>();
        while (tablesResult.next()) {
            TableMetaData tableMetaData = new TableMetaData();
            String tableName = tablesResult.getString("TABLE_NAME");
            tableMetaData.setTableName(tableName);
            tableMetaData.setComment(tablesResult.getString("REMARKS"));
            result.put(tableName, tableMetaData);
        }
        return result;
    }

    /**
     * 解析列信息ResultSet，按表名分组
     * 
     * @param columnsResult 列信息结果集
     * @return 表名到列元数据列表的映射
     * @throws SQLException 数据库访问异常
     */
    private static Map<String, List<ColumnMetaData>> getColumnMetaDataListMap(ResultSet columnsResult) throws SQLException {
        List<ColumnMetaData> columnMetaDataList = new ArrayList<>();
        while (columnsResult.next()) {
            ColumnMetaData columnMetaData = new ColumnMetaData();
            columnMetaData.setColumnName(columnsResult.getString("COLUMN_NAME"));
            columnMetaData.setTypeCode(columnsResult.getInt("DATA_TYPE"));
            columnMetaData.setTableName(columnsResult.getString("TABLE_NAME"));
            columnMetaData.setRemark(columnsResult.getString("REMARKS"));
            columnMetaDataList.add(columnMetaData);
        }
        return columnMetaDataList.stream().collect(Collectors.groupingBy(ColumnMetaData::getTableName));
    }

    /**
     * 根据表元数据动态生成Mapper接口类
     * <p>
     * 生成过程：
     * <ol>
     *     <li>根据表名生成Entity类名（下划线转驼峰，首字母大写）</li>
     *     <li>遍历列信息，生成Entity的字段（排除BaseEntity中已有的字段）</li>
     *     <li>使用模板渲染Entity类源码并编译</li>
     *     <li>使用模板渲染Mapper接口源码并编译</li>
     * </ol>
     * </p>
     * 
     * @param tableMetaData 表元数据
     * @return 生成的Mapper接口Class对象
     */
    public static Class<?> generateMapperClass(TableMetaData tableMetaData) {
        Dict tableDict = Dict.create();
        // 表名转换为Entity类名：user_info -> UserInfo
        tableDict.put("entityName", StrUtil.upperFirst(StrUtil.toCamelCase(tableMetaData.getTableName())));

        // 构建字段列表
        List<Dict> fieldList = new ArrayList<>();
        for(ColumnMetaData columnMetaData : tableMetaData.getColumnList()) {
            // 列名转换为字段名：user_name -> userName
            String columnName = StrUtil.toCamelCase(columnMetaData.getColumnName());
            // 跳过BaseEntity中已定义的字段
            if(FIELD_NAME_SET.contains(columnName)) {
                continue;
            }
            Dict columnDict = Dict.create();
            columnDict.put("name", columnName);
            columnDict.put("upperName", StrUtil.upperFirst(columnName));
            // 根据JDBC类型获取对应的Java类型
            columnDict.put("type", ((TypeReference<?>)TYPE_HANDLER_REGISTRY
                    .getTypeHandler(JdbcType.forCode(columnMetaData.getTypeCode()))).getRawType().getTypeName());
            fieldList.add(columnDict);
        }
        tableDict.put("fieldList", fieldList);

        // 渲染并编译Entity类
        String entity = ENTITY_TEMPLATE.render(tableDict);
        // 渲染并编译Mapper接口
        String mapper = MAPPER_TEMPLATE.render(tableDict);
        CLASS_LOADER.parseClass(entity);
        return CLASS_LOADER.parseClass(mapper);

    }

    /**
     * 根据表名获取对应的Entity类
     * <p>
     * 从GroovyClassLoader中加载之前动态生成的Entity类。
     * </p>
     * 
     * @param tableName 表名
     * @return Entity类的Class对象
     * @throws RuntimeException 如果类未找到
     */
    @SuppressWarnings("unchecked")
    public static Class<? extends BaseEntity> getLoaderClass(String tableName) {
        try {
            return (Class<? extends BaseEntity>)CLASS_LOADER.loadClass("entity." + StrUtil.upperFirst(StrUtil.toCamelCase(tableName)));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
