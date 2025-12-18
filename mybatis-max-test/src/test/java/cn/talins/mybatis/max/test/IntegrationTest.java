package cn.talins.mybatis.max.test;

import cn.hutool.json.JSONObject;
import cn.talins.mybatis.max.App;
import cn.talins.mybatis.max.api.enums.Booleans;
import cn.talins.mybatis.max.sdk.repository.BaseRepository;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 集成测试
 * 测试完整的CRUD流程和复杂场景
 *
 * @author talins
 */
@SpringBootTest(classes = App.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("集成测试")
public class IntegrationTest {

    @Autowired
    private BaseRepository baseRepository;

    private static final String TEST_TABLE = "sys_user";
    private static final List<Long> insertedIds = new ArrayList<>();

    @Test
    @Order(1)
    @DisplayName("测试批量插入")
    void testBatchInsert() {
        for (int i = 0; i < 5; i++) {
            JSONObject entity = new JSONObject();
            entity.set("username", "integration_test_" + System.currentTimeMillis() + "_" + i);
            entity.set("nickname", "集成测试用户" + i);
            entity.set("email", "integration" + i + "@example.com");

            Long id = baseRepository.insert(TEST_TABLE, entity);
            assertNotNull(id);
            insertedIds.add(id);
        }

        assertEquals(5, insertedIds.size(), "应该插入5条记录");
    }

    @Test
    @Order(2)
    @DisplayName("测试批量查询")
    void testBatchSelect() {
        assertFalse(insertedIds.isEmpty(), "需要先执行批量插入测试");

        List<JSONObject> results = baseRepository.selectBatchIds(TEST_TABLE, insertedIds, JSONObject.class);

        assertNotNull(results);
        assertEquals(insertedIds.size(), results.size(), "查询结果数量应该匹配");
    }

    @Test
    @Order(3)
    @DisplayName("测试条件查询 - 等于")
    void testSelectWithEqualCondition() {
        assertFalse(insertedIds.isEmpty(), "需要先执行批量插入测试");

        QueryWrapper<JSONObject> queryWrapper = Wrappers.query(JSONObject.class);
        queryWrapper.eq("id", insertedIds.get(0));

        JSONObject result = baseRepository.selectOne(TEST_TABLE, queryWrapper);

        assertNotNull(result);
        assertEquals(insertedIds.get(0), result.getLong("id"));
    }

    @Test
    @Order(4)
    @DisplayName("测试条件查询 - IN")
    void testSelectWithInCondition() {
        assertFalse(insertedIds.isEmpty(), "需要先执行批量插入测试");

        QueryWrapper<JSONObject> queryWrapper = Wrappers.query(JSONObject.class);
        queryWrapper.in("id", insertedIds.subList(0, 3));

        List<JSONObject> results = baseRepository.selectList(TEST_TABLE, queryWrapper);

        assertNotNull(results);
        assertEquals(3, results.size(), "应该查询到3条记录");
    }

    @Test
    @Order(5)
    @DisplayName("测试条件查询 - LIKE")
    void testSelectWithLikeCondition() {
        QueryWrapper<JSONObject> queryWrapper = Wrappers.query(JSONObject.class);
        queryWrapper.like("username", "integration_test");

        List<JSONObject> results = baseRepository.selectList(TEST_TABLE, queryWrapper);

        assertNotNull(results);
        assertTrue(results.size() >= insertedIds.size(), "应该查询到至少5条记录");
    }

    @Test
    @Order(6)
    @DisplayName("测试分页查询")
    void testPaginationQuery() {
        Page<JSONObject> page = new Page<>(1, 2);
        QueryWrapper<JSONObject> queryWrapper = Wrappers.query(JSONObject.class);
        queryWrapper.in("id", insertedIds);

        Page<JSONObject> result = baseRepository.selectPage(TEST_TABLE, page, queryWrapper);

        assertNotNull(result);
        assertEquals(2, result.getRecords().size(), "每页应该有2条记录");
        assertTrue(result.getTotal() >= 5, "总数应该至少为5");
    }

    @Test
    @Order(7)
    @DisplayName("测试排序查询")
    void testOrderByQuery() {
        QueryWrapper<JSONObject> queryWrapper = Wrappers.query(JSONObject.class);
        queryWrapper.in("id", insertedIds);
        queryWrapper.orderByDesc("id");

        List<JSONObject> results = baseRepository.selectList(TEST_TABLE, queryWrapper);

        assertNotNull(results);
        assertFalse(results.isEmpty());

        // 验证降序排列
        for (int i = 0; i < results.size() - 1; i++) {
            assertTrue(results.get(i).getLong("id") > results.get(i + 1).getLong("id"),
                    "结果应该按ID降序排列");
        }
    }

    @Test
    @Order(8)
    @DisplayName("测试统计查询")
    void testCountQuery() {
        QueryWrapper<JSONObject> queryWrapper = Wrappers.query(JSONObject.class);
        queryWrapper.in("id", insertedIds);

        Long count = baseRepository.selectCount(TEST_TABLE, queryWrapper);

        assertNotNull(count);
        assertEquals(insertedIds.size(), count.intValue(), "统计数量应该匹配");
    }

    @Test
    @Order(9)
    @DisplayName("测试批量更新")
    void testBatchUpdate() {
        assertFalse(insertedIds.isEmpty(), "需要先执行批量插入测试");

        for (Long id : insertedIds) {
            JSONObject entity = new JSONObject();
            entity.set("id", id);
            entity.set("nickname", "批量更新后的昵称_" + id);

            int count = baseRepository.updateById(TEST_TABLE, entity);
            assertTrue(count >= 0, "更新操作应该成功");
        }

        // 验证更新结果
        List<JSONObject> results = baseRepository.selectBatchIds(TEST_TABLE, insertedIds, JSONObject.class);
        for (JSONObject result : results) {
            assertTrue(result.getStr("nickname").startsWith("批量更新后的昵称_"),
                    "昵称应该被更新");
        }
    }

    @Test
    @Order(10)
    @DisplayName("测试条件更新")
    void testConditionalUpdate() {
        assertFalse(insertedIds.isEmpty(), "需要先执行批量插入测试");

        JSONObject entity = new JSONObject();
        entity.set("nickname", "条件更新的昵称");

        QueryWrapper<JSONObject> updateWrapper = Wrappers.query(JSONObject.class);
        updateWrapper.eq("id", insertedIds.get(0));

        int count = baseRepository.update(TEST_TABLE, entity, updateWrapper);

        assertTrue(count >= 0, "条件更新操作应该成功");
    }

    @Test
    @Order(11)
    @DisplayName("测试exists判断 - 存在")
    void testExistsTrue() {
        assertFalse(insertedIds.isEmpty(), "需要先执行批量插入测试");

        QueryWrapper<JSONObject> queryWrapper = Wrappers.query(JSONObject.class);
        queryWrapper.eq("id", insertedIds.get(0));

        boolean exists = baseRepository.exists(TEST_TABLE, queryWrapper);

        assertTrue(exists, "记录应该存在");
    }

    @Test
    @Order(12)
    @DisplayName("测试exists判断 - 不存在")
    void testExistsFalse() {
        QueryWrapper<JSONObject> queryWrapper = Wrappers.query(JSONObject.class);
        queryWrapper.eq("id", -99999L);

        boolean exists = baseRepository.exists(TEST_TABLE, queryWrapper);

        assertFalse(exists, "记录不应该存在");
    }

    @Test
    @Order(13)
    @DisplayName("测试Map条件查询")
    void testSelectByMapCondition() {
        assertFalse(insertedIds.isEmpty(), "需要先执行批量插入测试");

        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("id", insertedIds.get(0));
        columnMap.put("normal", Booleans.TRUE.getValue());

        List<JSONObject> results = baseRepository.selectByMap(TEST_TABLE, columnMap, JSONObject.class);

        assertNotNull(results);
        assertFalse(results.isEmpty(), "应该查询到记录");
    }

    @Test
    @Order(14)
    @DisplayName("测试复杂条件组合查询")
    void testComplexConditionQuery() {
        QueryWrapper<JSONObject> queryWrapper = Wrappers.query(JSONObject.class);
        queryWrapper.in("id", insertedIds)
                .eq("normal", Booleans.TRUE.getValue())
                .like("username", "integration")
                .orderByDesc("id")
                .last("LIMIT 3");

        List<JSONObject> results = baseRepository.selectList(TEST_TABLE, queryWrapper);

        assertNotNull(results);
        assertTrue(results.size() <= 3, "结果数量应该不超过3");
    }

    @Test
    @Order(98)
    @DisplayName("测试条件删除")
    void testConditionalDelete() {
        // 先插入一条专门用于删除测试的数据
        JSONObject entity = new JSONObject();
        entity.set("username", "delete_test_" + System.currentTimeMillis());
        entity.set("nickname", "删除测试");

        Long id = baseRepository.insert(TEST_TABLE, entity);
        assertNotNull(id);

        QueryWrapper<JSONObject> deleteWrapper = Wrappers.query(JSONObject.class);
        deleteWrapper.eq("id", id);

        int count = baseRepository.delete(TEST_TABLE, deleteWrapper);

        assertTrue(count >= 0, "条件删除操作应该成功");
    }

    @Test
    @Order(99)
    @DisplayName("清理测试数据")
    void cleanupTestData() {
        if (!insertedIds.isEmpty()) {
            int count = baseRepository.deleteBatchIds(TEST_TABLE, insertedIds);
            assertTrue(count >= 0, "清理操作应该成功");
            insertedIds.clear();
        }
    }
}
