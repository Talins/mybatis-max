package cn.talins.mybatis.max.test;

import cn.talins.mybatis.max.api.pojo.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * POJO类测试
 * 测试所有数据传输对象的功能
 *
 * @author talins
 */
@DisplayName("POJO类测试")
public class PojoTest {

    @Nested
    @DisplayName("BaseEntity测试")
    class BaseEntityTest {

        @Test
        @DisplayName("测试BaseEntity属性设置和获取")
        void testBaseEntityProperties() {
            BaseEntity entity = new BaseEntity();
            entity.setId(1L);
            entity.setNormal(1);
            entity.setVersion(100L);
            entity.setUpdateTime(new Date());
            entity.setExtra("{\"key\":\"value\"}");

            assertEquals(1L, entity.getId());
            assertEquals(1, entity.getNormal());
            assertEquals(100L, entity.getVersion());
assertNotNull(entity.getUpdateTime());
            assertEquals("{\"key\":\"value\"}", entity.getExtra());
        }

        @Test
        @DisplayName("测试BaseEntity序列化")
        void testBaseEntitySerializable() {
            BaseEntity entity = new BaseEntity();
            assertTrue(entity instanceof java.io.Serializable);
        }
    }

    @Nested
    @DisplayName("Result测试")
    class ResultTest {

        @Test
        @DisplayName("测试成功结果")
        void testSuccessResult() {
            Result<String> result = Result.success("test data");

            assertEquals(200, result.getCode());
            assertEquals("success", result.getMessage());
            assertEquals("test data", result.getData());
        }

        @Test
        @DisplayName("测试错误结果")
        void testErrorResult() {
            Result<String> result = Result.error("发生错误");

            assertEquals(500, result.getCode());
            assertEquals("发生错误", result.getMessage());
            assertNull(result.getData());
        }

        @Test
        @DisplayName("测试Result泛型")
        void testResultGeneric() {
            Map<String, Object> data = new HashMap<>();
            data.put("key", "value");

            Result<Map<String, Object>> result = Result.success(data);

            assertNotNull(result.getData());
            assertEquals("value", result.getData().get("key"));
        }
    }

    @Nested
    @DisplayName("Page测试")
    class PageTest {

        @Test
        @DisplayName("测试Page构造和属性")
        void testPageProperties() {
            Page page = new Page(10L, 1L, 100L);

            assertEquals(10L, page.getPageSize());
            assertEquals(1L, page.getCurrentPage());
            assertEquals(100L, page.getTotal());
        }

        @Test
        @DisplayName("测试Page序列化")
        void testPageSerializable() {
            Page page = new Page(10L, 1L, 100L);
            assertTrue(page instanceof java.io.Serializable);
        }
    }

    @Nested
    @DisplayName("PageResult测试")
    class PageResultTest {

        @Test
        @DisplayName("测试成功的分页结果")
        void testSuccessPageResult() {
            PageResult<String> result = PageResult.success("data", 10L, 1L, 100L);

            assertEquals(200, result.getCode());
            assertEquals("success", result.getMessage());
            assertEquals("data", result.getData());
            assertNotNull(result.getPage());
            assertEquals(10L, result.getPage().getPageSize());
            assertEquals(1L, result.getPage().getCurrentPage());
            assertEquals(100L, result.getPage().getTotal());
        }
    }

    @Nested
    @DisplayName("BaseRequest测试")
    class BaseRequestTest {

        @Test
        @DisplayName("测试BaseRequest无参构造")
        void testBaseRequestNoArgs() {
            BaseRequest<String> request = new BaseRequest<>();
            assertNull(request.getParam());
        }

        @Test
        @DisplayName("测试BaseRequest有参构造")
        void testBaseRequestAllArgs() {
            BaseRequest<String> request = new BaseRequest<>("test param");
            assertEquals("test param", request.getParam());
        }

        @Test
        @DisplayName("测试BaseRequest泛型")
        void testBaseRequestGeneric() {
            Map<String, Object> param = new HashMap<>();
            param.put("key", "value");

            BaseRequest<Map<String, Object>> request = new BaseRequest<>(param);

            assertNotNull(request.getParam());
            assertEquals("value", request.getParam().get("key"));
        }
    }

    @Nested
    @DisplayName("PageRequest测试")
    class PageRequestTest {

        @Test
        @DisplayName("测试PageRequest无参构造")
        void testPageRequestNoArgs() {
            PageRequest<String> request = new PageRequest<>();
            assertNull(request.getPageNum());
            assertNull(request.getPageSize());
            assertNull(request.getParam());
        }

        @Test
        @DisplayName("测试PageRequest有参构造")
        void testPageRequestAllArgs() {
            PageRequest<String> request = new PageRequest<>(1, 10, "test");

            assertEquals(1, request.getPageNum());
            assertEquals(10, request.getPageSize());
            assertEquals("test", request.getParam());
        }

        @Test
        @DisplayName("测试PageRequest属性设置")
        void testPageRequestSetters() {
            PageRequest<String> request = new PageRequest<>();
            request.setPageNum(2);
            request.setPageSize(20);
            request.setParam("param");

            assertEquals(2, request.getPageNum());
            assertEquals(20, request.getPageSize());
            assertEquals("param", request.getParam());
        }
    }

    @Nested
    @DisplayName("MapRequest测试")
    class MapRequestTest {

        @Test
        @DisplayName("测试MapRequest无参构造")
        void testMapRequestNoArgs() {
            MapRequest<String> request = new MapRequest<>();
            assertNull(request.getEntity());
            assertNull(request.getParam());
        }

        @Test
        @DisplayName("测试MapRequest有参构造")
        void testMapRequestAllArgs() {
            Map<String, Object> entity = new HashMap<>();
            entity.put("name", "test");

            MapRequest<String> request = new MapRequest<>(entity, "param");

            assertNotNull(request.getEntity());
            assertEquals("test", request.getEntity().get("name"));
            assertEquals("param", request.getParam());
        }
    }

    @Nested
    @DisplayName("TableMetaData测试")
    class TableMetaDataTest {

        @Test
        @DisplayName("测试TableMetaData属性")
        void testTableMetaDataProperties() {
            TableMetaData metaData = new TableMetaData();
            metaData.setTableName("sys_user");
            metaData.setComment("用户表");
            metaData.setDataSourceName("master");

            assertEquals("sys_user", metaData.getTableName());
            assertEquals("用户表", metaData.getComment());
            assertEquals("master", metaData.getDataSourceName());
        }
    }

    @Nested
    @DisplayName("ColumnMetaData测试")
    class ColumnMetaDataTest {

        @Test
        @DisplayName("测试ColumnMetaData属性")
        void testColumnMetaDataProperties() {
            ColumnMetaData metaData = new ColumnMetaData();
            metaData.setColumnName("username");
            metaData.setTypeCode(12); // VARCHAR
            metaData.setTableName("sys_user");
            metaData.setRemark("用户名");

            assertEquals("username", metaData.getColumnName());
            assertEquals(12, metaData.getTypeCode());
            assertEquals("sys_user", metaData.getTableName());
            assertEquals("用户名", metaData.getRemark());
        }
    }
}
