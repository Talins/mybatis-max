package cn.talins.mybatis.max.test;

import cn.hutool.json.JSONObject;
import cn.talins.mybatis.max.App;
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
 * BaseRepository 单元测试
 * 测试基础的CRUD操作
 *
 * @author talins
 */
@SpringBootTest(classes = App.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BaseRepositoryTest {

    @Autowired
    private BaseRepository baseRepository;

    private static final String TEST_TABLE = "sys_user";
    private static Long insertedId;

    @Test
    @Order(1)
    @DisplayName("测试插入单条记录")
    void testInsert() {
        JSONObject entity = new JSONObject();
        entity.set("username", "test_user_" + System.currentTimeMillis());
        entity.set("nickname", "测试用户");
        entity.set("email", "test@example.com");

        Long id = baseRepository.insert(TEST_TABLE, entity);

        assertNotNull(id, "插入后应返回ID");
        assertTrue(id > 0, "ID应该大于0");
        insertedId = id;
    }

    @Test
    @Order(2)
    @DisplayName("测试根据ID查询")
    void testSelectById() {
        assertNotNull(insertedId, "需要先执行插入测试");

        JSONObject result = baseRepository.selectById(TEST_TABLE, insertedId, JSONObject.class);

        assertNotNull(result, "查询结果不应为空");
        assertEquals(insertedId, result.getLong("id"), "ID应该匹配");
    }

    @Test
    @Order(3)
    @DisplayName("测试根据ID批量查询")
    void testSelectBatchIds() {
        assertNotNull(insertedId, "需要先执行插入测试");

        List<Long> ids = Collections.singletonList(insertedId);
        List<JSONObject> results = baseRepository.selectBatchIds(TEST_TABLE, ids, JSONObject.class);

        assertNotNull(results, "查询结果不应为空");
        assertFalse(results.isEmpty(), "结果列表不应为空");
    }

    @Test
    @Order(4)
    @DisplayName("测试根据Map条件查询")
    void testSelectByMap() {
        assertNotNull(insertedId, "需要先执行插入测试");

        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("id", insertedId);

        List<JSONObject> results = baseRepository.selectByMap(TEST_TABLE, columnMap, JSONObject.class);

        assertNotNull(results, "查询结果不应为空");
        assertFalse(results.isEmpty(), "结果列表不应为空");
    }

    @Test
    @Order(5)
    @DisplayName("测试根据ID更新")
    void testUpdateById() {
        assertNotNull(insertedId, "需要先执行插入测试");

        JSONObject entity = new JSONObject();
        entity.set("id", insertedId);
        entity.set("nickname", "更新后的昵称");

        int count = baseRepository.updateById(TEST_TABLE, entity);

        assertTrue(count >= 0, "更新操作应该成功");
    }

    @Test
    @Order(6)
    @DisplayName("测试条件查询单条记录")
    void testSelectOne() {
        assertNotNull(insertedId, "需要先执行插入测试");

        QueryWrapper<JSONObject> queryWrapper = Wrappers.query(JSONObject.class);
        queryWrapper.eq("id", insertedId);

        JSONObject result = baseRepository.selectOne(TEST_TABLE, queryWrapper);

        assertNotNull(result, "查询结果不应为空");
    }

    @Test
    @Order(7)
    @DisplayName("测试条件查询列表")
    void testSelectList() {
        QueryWrapper<JSONObject> queryWrapper = Wrappers.query(JSONObject.class);
        queryWrapper.last("LIMIT 10");

        List<JSONObject> results = baseRepository.selectList(TEST_TABLE, queryWrapper);

        assertNotNull(results, "查询结果不应为空");
    }

    @Test
    @Order(8)
    @DisplayName("测试统计查询")
    void testSelectCount() {
        QueryWrapper<JSONObject> queryWrapper = Wrappers.query(JSONObject.class);

        Long count = baseRepository.selectCount(TEST_TABLE, queryWrapper);

        assertNotNull(count, "统计结果不应为空");
        assertTrue(count >= 0, "统计数量应该大于等于0");
    }

    @Test
    @Order(9)
    @DisplayName("测试分页查询")
    void testSelectPage() {
        Page<JSONObject> page = new Page<>(1, 10);
        QueryWrapper<JSONObject> queryWrapper = Wrappers.query(JSONObject.class);

        Page<JSONObject> result = baseRepository.selectPage(TEST_TABLE, page, queryWrapper);

        assertNotNull(result, "分页结果不应为空");
        assertNotNull(result.getRecords(), "记录列表不应为空");
    }

    @Test
    @Order(10)
    @DisplayName("测试exists判断")
    void testExists() {
        assertNotNull(insertedId, "需要先执行插入测试");

        QueryWrapper<JSONObject> queryWrapper = Wrappers.query(JSONObject.class);
        queryWrapper.eq("id", insertedId);

        boolean exists = baseRepository.exists(TEST_TABLE, queryWrapper);

        assertTrue(exists, "记录应该存在");
    }

    @Test
    @Order(11)
    @DisplayName("测试根据Map条件查询单条")
    void testSelectOneByMap() {
        assertNotNull(insertedId, "需要先执行插入测试");

        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("id", insertedId);

        JSONObject result = baseRepository.selectOneByMap(TEST_TABLE, columnMap, JSONObject.class);

        assertNotNull(result, "查询结果不应为空");
    }

    @Test
    @Order(12)
    @DisplayName("测试根据Map条件统计")
    void testSelectCountByMap() {
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("normal", 1);

        Long count = baseRepository.selectCountByMap(TEST_TABLE, columnMap);

        assertNotNull(count, "统计结果不应为空");
        assertTrue(count >= 0, "统计数量应该大于等于0");
    }

    @Test
    @Order(98)
    @DisplayName("测试根据ID删除")
    void testDeleteById() {
        assertNotNull(insertedId, "需要先执行插入测试");

        int count = baseRepository.deleteById(TEST_TABLE, insertedId);

        assertTrue(count >= 0, "删除操作应该成功");
    }

    @Test
    @Order(99)
    @DisplayName("测试批量删除")
    void testDeleteBatchIds() {
        // 先插入测试数据
        JSONObject entity = new JSONObject();
        entity.set("username", "batch_delete_test_" + System.currentTimeMillis());
        entity.set("nickname", "批量删除测试");

        Long id = baseRepository.insert(TEST_TABLE, entity);
        assertNotNull(id);

        List<Long> ids = Collections.singletonList(id);
        int count = baseRepository.deleteBatchIds(TEST_TABLE, ids);

        assertTrue(count >= 0, "批量删除操作应该成功");
    }
}
