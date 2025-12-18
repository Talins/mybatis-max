package cn.talins.mybatis.max.test;

import cn.talins.mybatis.max.sdk.common.Constant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Constant 常量类测试
 * 测试常量定义和模板引擎
 *
 * @author talins
 */
@DisplayName("Constant常量类测试")
public class ConstantTest {

    @Test
    @DisplayName("测试TEMPLATE_ENGINE不为空")
    void testTemplateEngineNotNull() {
        assertNotNull(Constant.TEMPLATE_ENGINE, "模板引擎不应为空");
    }

    @Test
    @DisplayName("测试ENTITY_TEMPLATE不为空")
    void testEntityTemplateNotNull() {
        assertNotNull(Constant.ENTITY_TEMPLATE, "Entity模板不应为空");
    }

    @Test
    @DisplayName("测试MAPPER_TEMPLATE不为空")
    void testMapperTemplateNotNull() {
        assertNotNull(Constant.MAPPER_TEMPLATE, "Mapper模板不应为空");
    }

    @Test
    @DisplayName("测试TABLE_DATASOURCE_MAP初始化")
    void testTableDatasourceMapInitialized() {
        assertNotNull(Constant.TABLE_DATASOURCE_MAP, "TABLE_DATASOURCE_MAP不应为空");
    }

    @Test
    @DisplayName("测试TABLE_DATASOURCE_MAP可操作")
    void testTableDatasourceMapOperations() {
        String testKey = "test_table_" + System.currentTimeMillis();
        String testValue = "test_datasource";

        // 测试put操作
        Constant.TABLE_DATASOURCE_MAP.put(testKey, testValue);
        assertEquals(testValue, Constant.TABLE_DATASOURCE_MAP.get(testKey));

        // 测试containsKey
        assertTrue(Constant.TABLE_DATASOURCE_MAP.containsKey(testKey));

        // 清理测试数据
        Constant.TABLE_DATASOURCE_MAP.remove(testKey);
        assertFalse(Constant.TABLE_DATASOURCE_MAP.containsKey(testKey));
    }
}
