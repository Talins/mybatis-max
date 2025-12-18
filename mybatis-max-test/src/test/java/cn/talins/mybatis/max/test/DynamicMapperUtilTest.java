package cn.talins.mybatis.max.test;

import cn.talins.mybatis.max.api.pojo.BaseEntity;
import cn.talins.mybatis.max.api.pojo.ColumnMetaData;
import cn.talins.mybatis.max.api.pojo.TableMetaData;
import cn.talins.mybatis.max.sdk.DynamicMapperUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * DynamicMapperUtil 单元测试
 * 测试动态Mapper生成工具类
 *
 * @author talins
 */
@DisplayName("DynamicMapperUtil测试")
public class DynamicMapperUtilTest {

    @Test
    @DisplayName("测试FIELD_NAME_SET包含BaseEntity字段")
    void testFieldNameSetContainsBaseEntityFields() {
        Set<String> fieldNameSet = DynamicMapperUtil.FIELD_NAME_SET;

        assertNotNull(fieldNameSet, "FIELD_NAME_SET不应为空");
        assertTrue(fieldNameSet.contains("id"), "应包含id字段");
        assertTrue(fieldNameSet.contains("normal"), "应包含normal字段");
        assertTrue(fieldNameSet.contains("version"), "应包含version字段");
        assertTrue(fieldNameSet.contains("updateTime"), "应包含updateTime字段");
        assertTrue(fieldNameSet.contains("extra"), "应包含extra字段");
    }

    @Test
    @DisplayName("测试FIELD_NAME_SET字段数量")
    void testFieldNameSetSize() {
        Set<String> fieldNameSet = DynamicMapperUtil.FIELD_NAME_SET;

        assertEquals(5, fieldNameSet.size(), "BaseEntity应该有5个字段");
    }

    @Test
    @DisplayName("测试generateMapperClass生成Mapper类")
    void testGenerateMapperClass() {
        TableMetaData tableMetaData = createTestTableMetaData("test_user");

        Class<?> mapperClass = DynamicMapperUtil.generateMapperClass(tableMetaData);

        assertNotNull(mapperClass, "生成的Mapper类不应为空");
        assertTrue(mapperClass.isInterface(), "生成的应该是接口");
        assertEquals("TestUserMapper", mapperClass.getSimpleName(), "类名应该正确");
    }

    @Test
    @DisplayName("测试generateMapperClass生成Entity类")
    void testGenerateMapperClassCreatesEntity() {
        TableMetaData tableMetaData = createTestTableMetaData("test_order");

        DynamicMapperUtil.generateMapperClass(tableMetaData);

        // 验证Entity类也被生成
        Class<? extends BaseEntity> entityClass = DynamicMapperUtil.getLoaderClass("test_order");
        assertNotNull(entityClass, "Entity类应该被生成");
        assertEquals("TestOrder", entityClass.getSimpleName(), "Entity类名应该正确");
    }

    @Test
    @DisplayName("测试getLoaderClass获取已生成的类")
    void testGetLoaderClass() {
        TableMetaData tableMetaData = createTestTableMetaData("test_product");
        DynamicMapperUtil.generateMapperClass(tableMetaData);

        Class<? extends BaseEntity> loaderClass = DynamicMapperUtil.getLoaderClass("test_product");

        assertNotNull(loaderClass, "应该能获取到已生成的类");
        assertTrue(BaseEntity.class.isAssignableFrom(loaderClass), "应该继承自BaseEntity");
    }

    @Test
    @DisplayName("测试getLoaderClass - 类不存在抛出异常")
    void testGetLoaderClassNotFound() {
        assertThrows(RuntimeException.class, () -> {
            DynamicMapperUtil.getLoaderClass("non_existent_table_xyz");
        }, "获取不存在的类应该抛出异常");
    }

    @Test
    @DisplayName("测试表名转换为类名 - 下划线转驼峰")
    void testTableNameToCamelCase() {
        TableMetaData tableMetaData = createTestTableMetaData("sys_user_role");

        Class<?> mapperClass = DynamicMapperUtil.generateMapperClass(tableMetaData);

        assertEquals("SysUserRoleMapper", mapperClass.getSimpleName());
    }

    @Test
    @DisplayName("测试生成的Entity包含自定义字段")
    void testGeneratedEntityContainsCustomFields() throws Exception {
        TableMetaData tableMetaData = new TableMetaData();
        tableMetaData.setTableName("custom_entity_test");

        List<ColumnMetaData> columnList = new ArrayList<>();

        // 添加BaseEntity已有的字段
        addColumn(columnList, "id", 4, "custom_entity_test");
        addColumn(columnList, "normal", 4, "custom_entity_test");
        addColumn(columnList, "version", -5, "custom_entity_test");
        addColumn(columnList, "update_time", 12, "custom_entity_test");
        addColumn(columnList, "extra", 12, "custom_entity_test");

        // 添加自定义字段
        addColumn(columnList, "custom_name", 12, "custom_entity_test"); // VARCHAR
        addColumn(columnList, "custom_age", 4, "custom_entity_test"); // INTEGER

        tableMetaData.setColumnList(columnList);

        DynamicMapperUtil.generateMapperClass(tableMetaData);

        Class<? extends BaseEntity> entityClass = DynamicMapperUtil.getLoaderClass("custom_entity_test");

        // 验证自定义字段的getter方法存在
        assertNotNull(entityClass.getMethod("getCustomName"), "应该有getCustomName方法");
        assertNotNull(entityClass.getMethod("getCustomAge"), "应该有getCustomAge方法");
    }

    @Test
    @DisplayName("测试生成的Entity继承BaseEntity")
    void testGeneratedEntityExtendsBaseEntity() {
        TableMetaData tableMetaData = createTestTableMetaData("inheritance_test");

        DynamicMapperUtil.generateMapperClass(tableMetaData);

        Class<? extends BaseEntity> entityClass = DynamicMapperUtil.getLoaderClass("inheritance_test");

        assertTrue(BaseEntity.class.isAssignableFrom(entityClass), "生成的Entity应该继承BaseEntity");
    }

    /**
     * 创建测试用的TableMetaData
     */
    private TableMetaData createTestTableMetaData(String tableName) {
        TableMetaData tableMetaData = new TableMetaData();
        tableMetaData.setTableName(tableName);

        List<ColumnMetaData> columnList = new ArrayList<>();
        addColumn(columnList, "id", -5, tableName); // BIGINT
        addColumn(columnList, "normal", 4, tableName); // INTEGER
        addColumn(columnList, "version", -5, tableName); // BIGINT
        addColumn(columnList, "update_time", 12, tableName); // VARCHAR
        addColumn(columnList, "extra", 12, tableName); // VARCHAR
        addColumn(columnList, "name", 12, tableName); // VARCHAR - 自定义字段

        tableMetaData.setColumnList(columnList);
        return tableMetaData;
    }

    /**
     * 添加列元数据
     */
    private void addColumn(List<ColumnMetaData> columnList, String columnName, int typeCode, String tableName) {
        ColumnMetaData column = new ColumnMetaData();
        column.setColumnName(columnName);
        column.setTypeCode(typeCode);
        column.setTableName(tableName);
        columnList.add(column);
    }
}
