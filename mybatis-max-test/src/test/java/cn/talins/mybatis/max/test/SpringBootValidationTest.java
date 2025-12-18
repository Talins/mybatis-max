package cn.talins.mybatis.max.test;

import cn.talins.mybatis.max.App;
import cn.talins.mybatis.max.api.IRepository;
import cn.talins.mybatis.max.api.enums.Order;
import cn.talins.mybatis.max.api.pojo.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.annotation.Resource;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.*;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Spring Boot模式的Validated校验功能测试
 * <p>
 * 该测试类使用Spring Boot Test框架，通过MockMvc模拟HTTP请求，
 * 验证REST接口的参数校验功能和GlobalExceptionHandler的异常处理。
 * </p>
 *
 * @author talins
 */
@SpringBootTest(classes = App.class)
@AutoConfigureMockMvc
@DisplayName("Spring Boot Validated校验功能测试")
public class SpringBootValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Validator validator;

    @Resource
    private IRepository baseRepository;

    private static final String BASE_URL = "/mybatis-max";
    private static final String TEST_TABLE = "sys_user";

    @Nested
    @DisplayName("REST接口参数校验测试")
    class RestApiValidationTest {

        @Test
        @DisplayName("selectList - param为null时应返回校验错误")
        void testSelectListWithNullParam() throws Exception {
            BaseRequest<Query> request = new BaseRequest<>();
            request.setParam(null);

            performPostAndExpectBadRequest("/selectList/" + TEST_TABLE, request)
                    .andExpect(jsonPath("$.message").value(containsString("请求参数不能为空")));
        }

        @Test
        @DisplayName("selectList - 有效请求应返回成功")
        void testSelectListWithValidParam() throws Exception {
            Query query = Query.newInstance()
                    .addColumn("id")
                    .addColumn("userName");

            BaseRequest<Query> request = new BaseRequest<>(query);

            mockMvc.perform(post(BASE_URL + "/selectList/" + TEST_TABLE)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(200));
        }

        @Test
        @DisplayName("selectPage - pageNum为null时应返回校验错误")
        void testSelectPageWithNullPageNum() throws Exception {
            PageRequest<Query> request = new PageRequest<>();
            request.setPageNum(null);
            request.setPageSize(10);
            request.setParam(Query.newInstance());

            performPostAndExpectBadRequest("/selectPage/" + TEST_TABLE, request)
                    .andExpect(jsonPath("$.message").value(containsString("页码不能为空")));
        }

        @Test
        @DisplayName("selectPage - pageNum小于1时应返回校验错误")
        void testSelectPageWithInvalidPageNum() throws Exception {
            PageRequest<Query> request = new PageRequest<>();
            request.setPageNum(0);
            request.setPageSize(10);
            request.setParam(Query.newInstance());

            performPostAndExpectBadRequest("/selectPage/" + TEST_TABLE, request)
                    .andExpect(jsonPath("$.message").value(containsString("页码最小值为1")));
        }

        @Test
        @DisplayName("selectPage - pageSize为null时应返回校验错误")
        void testSelectPageWithNullPageSize() throws Exception {
            PageRequest<Query> request = new PageRequest<>();
            request.setPageNum(1);
            request.setPageSize(null);
            request.setParam(Query.newInstance());

            performPostAndExpectBadRequest("/selectPage/" + TEST_TABLE, request)
                    .andExpect(jsonPath("$.message").value(containsString("每页记录数不能为空")));
        }

        @Test
        @DisplayName("selectPage - pageSize超过最大值时应返回校验错误")
        void testSelectPageWithExceedingPageSize() throws Exception {
            PageRequest<Query> request = new PageRequest<>();
            request.setPageNum(1);
            request.setPageSize(1001);
            request.setParam(Query.newInstance());

            performPostAndExpectBadRequest("/selectPage/" + TEST_TABLE, request)
                    .andExpect(jsonPath("$.message").value(containsString("每页记录数最大值为1000")));
        }

        @Test
        @DisplayName("selectPage - 有效分页请求应返回成功")
        void testSelectPageWithValidRequest() throws Exception {
            PageRequest<Query> request = new PageRequest<>(1, 10, Query.newInstance());

            mockMvc.perform(post(BASE_URL + "/selectPage/" + TEST_TABLE)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(200));
        }

        @Test
        @DisplayName("update - entity为空Map时应返回校验错误")
        void testUpdateWithEmptyEntity() throws Exception {
            MapRequest<Query> request = new MapRequest<>();
            request.setEntity(new HashMap<>());
            request.setParam(Query.newInstance().addCondition("id", 1L));

            performPostAndExpectBadRequest("/update/" + TEST_TABLE, request)
                    .andExpect(jsonPath("$.message").value(containsString("实体数据不能为空")));
        }

        @Test
        @DisplayName("update - param为null时应返回校验错误")
        void testUpdateWithNullParam() throws Exception {
            Map<String, Object> entity = new HashMap<>();
            entity.put("username", "test");

            MapRequest<Query> request = new MapRequest<>();
            request.setEntity(entity);
            request.setParam(null);

            performPostAndExpectBadRequest("/update/" + TEST_TABLE, request)
                    .andExpect(jsonPath("$.message").value(containsString("查询参数不能为空")));
        }

        @Test
        @DisplayName("insert - param为null时应返回校验错误")
        void testInsertWithNullParam() throws Exception {
            BaseRequest<Map<String, Object>> request = new BaseRequest<>();
            request.setParam(null);

            performPostAndExpectBadRequest("/insert/" + TEST_TABLE, request)
                    .andExpect(jsonPath("$.message").value(containsString("请求参数不能为空")));
        }

        @Test
        @DisplayName("deleteById - param为null时应返回校验错误")
        void testDeleteByIdWithNullParam() throws Exception {
            BaseRequest<Long> request = new BaseRequest<>();
            request.setParam(null);

            performPostAndExpectBadRequest("/deleteById/" + TEST_TABLE, request)
                    .andExpect(jsonPath("$.message").value(containsString("请求参数不能为空")));
        }

        private ResultActions performPostAndExpectBadRequest(String url, Object request) throws Exception {
            return mockMvc.perform(post(BASE_URL + url)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("嵌套对象校验测试")
    class NestedValidationTest {

        @Test
        @DisplayName("Query中Condition列名格式错误时应返回校验错误")
        void testQueryWithInvalidConditionColumn() throws Exception {
            Condition invalidCondition = Condition.newInstance()
                    .setColumn("123invalid")  // 以数字开头，不合法
                    .addParam("value");

            Query query = Query.newInstance();
            query.getConditionList().add(invalidCondition);

            BaseRequest<Query> request = new BaseRequest<>(query);

            performPostAndExpectBadRequest("/selectList/" + TEST_TABLE, request)
                    .andExpect(jsonPath("$.message").value(containsString("列名格式不正确")));
        }

        @Test
        @DisplayName("Query中Condition列名为空时应返回校验错误")
        void testQueryWithBlankConditionColumn() throws Exception {
            Condition invalidCondition = new Condition();
            invalidCondition.setColumn("");

            Query query = Query.newInstance();
            query.getConditionList().add(invalidCondition);

            BaseRequest<Query> request = new BaseRequest<>(query);

            performPostAndExpectBadRequest("/selectList/" + TEST_TABLE, request)
                    .andExpect(jsonPath("$.message").value(containsString("列名不能为空")));
        }

        @Test
        @DisplayName("Query中columnList超过100个时应返回校验错误")
        void testQueryWithTooManyColumns() throws Exception {
            Query query = Query.newInstance();
            for (int i = 0; i < 101; i++) {
                query.addColumn("column" + i);
            }

            BaseRequest<Query> request = new BaseRequest<>(query);

            performPostAndExpectBadRequest("/selectList/" + TEST_TABLE, request)
                    .andExpect(jsonPath("$.message").value(containsString("查询列数量不能超过100")));
        }

        @Test
        @DisplayName("Query中conditionList超过50个时应返回校验错误")
        void testQueryWithTooManyConditions() throws Exception {
            Query query = Query.newInstance();
            for (int i = 0; i < 51; i++) {
                query.addCondition("column" + i, "value" + i);
            }

            BaseRequest<Query> request = new BaseRequest<>(query);

            performPostAndExpectBadRequest("/selectList/" + TEST_TABLE, request)
                    .andExpect(jsonPath("$.message").value(containsString("查询条件数量不能超过50")));
        }

        @Test
        @DisplayName("Query中orderMap超过10个时应返回校验错误")
        void testQueryWithTooManyOrderBy() throws Exception {
            Query query = Query.newInstance();
            for (int i = 0; i < 11; i++) {
                query.addOrderBy("column" + i, Order.ASC);
            }

            BaseRequest<Query> request = new BaseRequest<>(query);

            performPostAndExpectBadRequest("/selectList/" + TEST_TABLE, request)
                    .andExpect(jsonPath("$.message").value(containsString("排序字段数量不能超过10")));
        }

        private ResultActions performPostAndExpectBadRequest(String url, Object request) throws Exception {
            return mockMvc.perform(post(BASE_URL + url)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("Spring Validator Bean校验测试")
    class SpringValidatorBeanTest {

        @Test
        @DisplayName("验证Spring Validator Bean已正确注入")
        void testValidatorBeanInjected() {
            assertNotNull(validator, "Validator Bean应该被正确注入");
        }

        @Test
        @DisplayName("使用Spring Validator校验BaseRequest")
        void testValidateBaseRequest() {
            BaseRequest<String> request = new BaseRequest<>();
            request.setParam(null);

            Set<ConstraintViolation<BaseRequest<String>>> violations = validator.validate(request);

            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                    .anyMatch(v -> v.getMessage().contains("请求参数不能为空")));
        }

        @Test
        @DisplayName("使用Spring Validator校验PageRequest")
        void testValidatePageRequest() {
            PageRequest<Query> request = new PageRequest<>();
            request.setPageNum(0);
            request.setPageSize(1001);

            Set<ConstraintViolation<PageRequest<Query>>> violations = validator.validate(request);

            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                    .anyMatch(v -> v.getMessage().contains("页码最小值为1")));
            assertTrue(violations.stream()
                    .anyMatch(v -> v.getMessage().contains("每页记录数最大值为1000")));
        }

        @Test
        @DisplayName("使用Spring Validator校验Condition")
        void testValidateCondition() {
            Condition condition = Condition.newInstance()
                    .setColumn("1invalid_column")
                    .addParam("value");

            Set<ConstraintViolation<Condition>> violations = validator.validate(condition);

            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                    .anyMatch(v -> v.getMessage().contains("列名格式不正确")));
        }

        @Test
        @DisplayName("使用Spring Validator校验Query嵌套Condition")
        void testValidateQueryWithNestedCondition() {
            Condition invalidCondition = new Condition();
            invalidCondition.setColumn(null);

            Query query = Query.newInstance();
            query.getConditionList().add(invalidCondition);

            Set<ConstraintViolation<Query>> violations = validator.validate(query);

            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                    .anyMatch(v -> v.getMessage().contains("列名不能为空")));
        }

        @Test
        @DisplayName("使用Spring Validator校验MapRequest")
        void testValidateMapRequest() {
            MapRequest<Query> request = new MapRequest<>();
            request.setEntity(new HashMap<>());
            request.setParam(null);

            Set<ConstraintViolation<MapRequest<Query>>> violations = validator.validate(request);

            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                    .anyMatch(v -> v.getMessage().contains("实体数据不能为空")));
            assertTrue(violations.stream()
                    .anyMatch(v -> v.getMessage().contains("查询参数不能为空")));
        }

        @Test
        @DisplayName("使用Spring Validator校验BaseEntity")
        void testValidateBaseEntity() {
            BaseEntity entity = new BaseEntity();
            entity.setNormal(-1);
            entity.setUpdateTime(new Date());

            Set<ConstraintViolation<BaseEntity>> violations = validator.validate(entity);

            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                    .anyMatch(v -> v.getMessage().contains("normal值最小为0")));
            assertTrue(violations.stream()
                    .anyMatch(v -> v.getMessage().contains("更新时间长度不能超过32")));
        }
    }

    @Nested
    @DisplayName("边界值校验测试")
    class BoundaryValidationTest {

        @Test
        @DisplayName("pageNum边界值-1应校验通过")
        void testPageNumBoundaryMin() throws Exception {
            PageRequest<Query> request = new PageRequest<>(1, 10, Query.newInstance());

            mockMvc.perform(post(BASE_URL + "/selectPage/" + TEST_TABLE)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("pageSize边界值-1应校验通过")
        void testPageSizeBoundaryMin() throws Exception {
            PageRequest<Query> request = new PageRequest<>(1, 1, Query.newInstance());

            mockMvc.perform(post(BASE_URL + "/selectPage/" + TEST_TABLE)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("pageSize边界值-1000应校验通过")
        void testPageSizeBoundaryMax() throws Exception {
            PageRequest<Query> request = new PageRequest<>(1, 1000, Query.newInstance());

            mockMvc.perform(post(BASE_URL + "/selectPage/" + TEST_TABLE)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("columnList边界值-100个应校验通过")
        void testColumnListBoundaryMax() throws Exception {
            Query query = Query.newInstance();
            for (int i = 0; i < 100; i++) {
                query.addColumn("column" + i);
            }

            BaseRequest<Query> request = new BaseRequest<>(query);

            mockMvc.perform(post(BASE_URL + "/selectList/" + TEST_TABLE)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("conditionList边界值-50个应校验通过")
        void testConditionListBoundaryMax() throws Exception {
            Query query = Query.newInstance();
            for (int i = 0; i < 50; i++) {
                query.addCondition("column" + i, "value" + i);
            }

            BaseRequest<Query> request = new BaseRequest<>(query);

            mockMvc.perform(post(BASE_URL + "/selectList/" + TEST_TABLE)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("orderMap边界值-10个应校验通过")
        void testOrderMapBoundaryMax() throws Exception {
            Query query = Query.newInstance();
            for (int i = 0; i < 10; i++) {
                query.addOrderBy("column" + i, Order.ASC);
            }

            BaseRequest<Query> request = new BaseRequest<>(query);

            mockMvc.perform(post(BASE_URL + "/selectList/" + TEST_TABLE)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("SQL注入防护校验测试")
    class SqlInjectionPreventionTest {

        @Test
        @DisplayName("列名包含SQL注入字符时应校验失败")
        void testColumnWithSqlInjection() throws Exception {
            Condition condition = Condition.newInstance()
                    .setColumn("name; DROP TABLE users;--")
                    .addParam("value");

            Query query = Query.newInstance();
            query.getConditionList().add(condition);

            BaseRequest<Query> request = new BaseRequest<>(query);

            mockMvc.perform(post(BASE_URL + "/selectList/" + TEST_TABLE)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(containsString("列名格式不正确")));
        }

        @Test
        @DisplayName("列名包含特殊字符时应校验失败")
        void testColumnWithSpecialChars() throws Exception {
            Condition condition = Condition.newInstance()
                    .setColumn("column-name")
                    .addParam("value");

            Query query = Query.newInstance();
            query.getConditionList().add(condition);

            BaseRequest<Query> request = new BaseRequest<>(query);

            mockMvc.perform(post(BASE_URL + "/selectList/" + TEST_TABLE)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(containsString("列名格式不正确")));
        }

        @Test
        @DisplayName("合法的下划线列名应校验通过")
        void testValidUnderscoreColumn() throws Exception {
            Query query = Query.newInstance()
                    .addCondition("user_name", "test");

            BaseRequest<Query> request = new BaseRequest<>(query);

            mockMvc.perform(post(BASE_URL + "/selectList/" + TEST_TABLE)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("合法的驼峰列名应校验通过")
        void testValidCamelCaseColumn() throws Exception {
            Query query = Query.newInstance()
                    .addCondition("userName", "test");

            BaseRequest<Query> request = new BaseRequest<>(query);

            mockMvc.perform(post(BASE_URL + "/selectList/" + TEST_TABLE)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("复杂场景校验测试")
    class ComplexScenarioValidationTest {

        @Test
        @DisplayName("完整的分页查询请求应校验通过")
        void testCompletePageRequest() throws Exception {
            Query query = Query.newInstance()
                    .addColumn("id")
                    .addColumn("username")
                    .addColumn("email")
                    .addCondition("normal", 1)
                    .addCondition(Condition.newInstance()
                            .setColumn("username")
                            .addParam("%test%"))
                    .addOrderBy("createTime", Order.DESC)
                    .addOrderBy("id", Order.ASC);

            PageRequest<Query> request = new PageRequest<>(1, 20, query);

            mockMvc.perform(post(BASE_URL + "/selectPage/" + TEST_TABLE)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(200));
        }

        @Test
        @DisplayName("多个校验错误应全部返回")
        void testMultipleValidationErrors() {
            PageRequest<Query> request = new PageRequest<>();
            request.setPageNum(0);  // 错误1
            request.setPageSize(1001);  // 错误2
            request.setParam(null);

            Set<ConstraintViolation<PageRequest<Query>>> violations = validator.validate(request);

            assertTrue(violations.size() >= 2, "应该有多个校验错误");
        }

        @Test
        @DisplayName("空Query对象应校验通过")
        void testEmptyQueryValid() throws Exception {
            BaseRequest<Query> request = new BaseRequest<>(Query.newInstance());

            mockMvc.perform(post(BASE_URL + "/selectList/" + TEST_TABLE)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());
        }
    }

}
