package cn.talins.mybatis.max.test;

import cn.hutool.json.JSONObject;
import cn.talins.mybatis.max.App;
import cn.talins.mybatis.max.sdk.repository.BaseRepository;
import cn.talins.mybatis.max.sdk.repository.service.AbstractBaseRepositoryService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * AbstractBaseRepositoryService 单元测试
 * 测试Repository服务层的功能
 *
 * @author talins
 */
@SpringBootTest(classes = App.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("RepositoryService测试")
public class RepositoryServiceTest {

    @Autowired
    private TestUserRepositoryService userService;

    private static Long insertedId;

    @Test
    @Order(1)
    @DisplayName("测试Service注入")
    void testServiceInjection() {
        assertNotNull(userService, "UserService应该被正确注入");
    }

    @Test
    @Order(2)
    @DisplayName("测试insert方法")
    void testInsert() {
        JSONObject entity = new JSONObject();
        entity.set("username", "service_test_" + System.currentTimeMillis());
        entity.set("nickname", "服务层测试用户");
        entity.set("email", "service@example.com");

        Long id = userService.insert(entity);

        assertNotNull(id, "插入后应返回ID");
        assertTrue(id > 0, "ID应该大于0");
        insertedId = id;
    }

    @Test
    @Order(3)
    @DisplayName("测试selectById方法")
    void testSelectById() {
        assertNotNull(insertedId, "需要先执行插入测试");

        JSONObject result = userService.selectById(insertedId, JSONObject.class);

        assertNotNull(result, "查询结果不应为空");
        assertEquals(insertedId, result.getLong("id"), "ID应该匹配");
    }

    @Test
    @Order(4)
    @DisplayName("测试selectBatchIds方法")
    void testSelectBatchIds() {
        assertNotNull(insertedId, "需要先执行插入测试");

        Collection<JSONObject> results = userService.selectBatchIds(
                Collections.singletonList(insertedId), JSONObject.class);

        assertNotNull(results, "查询结果不应为空");
        assertFalse(results.isEmpty(), "结果列表不应为空");
    }

    @Test
    @Order(5)
    @DisplayName("测试selectByMap方法")
    void testSelectByMap() {
        assertNotNull(insertedId, "需要先执行插入测试");

        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("id", insertedId);

        List<JSONObject> results = userService.selectByMap(columnMap, JSONObject.class);

        assertNotNull(results, "查询结果不应为空");
        assertFalse(results.isEmpty(), "结果列表不应为空");
    }

    @Test
    @Order(6)
    @DisplayName("测试selectOneByMap方法")
    void testSelectOneByMap() {
        assertNotNull(insertedId, "需要先执行插入测试");

        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("id", insertedId);

        JSONObject result = userService.selectOneByMap(columnMap, JSONObject.class);

        assertNotNull(result, "查询结果不应为空");
    }

    @Test
    @Order(7)
    @DisplayName("测试updateById方法")
    void testUpdateById() {
        assertNotNull(insertedId, "需要先执行插入测试");

        JSONObject entity = new JSONObject();
        entity.set("id", insertedId);
        entity.set("nickname", "服务层更新后的昵称");

        int count = userService.updateById(entity);

        assertTrue(count >= 0, "更新操作应该成功");
    }

    @Test
    @Order(8)
    @DisplayName("测试selectOne方法")
    void testSelectOne() {
        assertNotNull(insertedId, "需要先执行插入测试");

        QueryWrapper<JSONObject> queryWrapper = Wrappers.query(JSONObject.class);
        queryWrapper.eq("id", insertedId);

        JSONObject result = userService.selectOne(queryWrapper);

        assertNotNull(result, "查询结果不应为空");
    }

    @Test
    @Order(9)
    @DisplayName("测试selectList方法")
    void testSelectList() {
        QueryWrapper<JSONObject> queryWrapper = Wrappers.query(JSONObject.class);
        queryWrapper.last("LIMIT 10");

        Collection<JSONObject> results = userService.selectList(queryWrapper);

        assertNotNull(results, "查询结果不应为空");
    }

    @Test
    @Order(10)
    @DisplayName("测试selectCount方法")
    void testSelectCount() {
        QueryWrapper<JSONObject> queryWrapper = Wrappers.query(JSONObject.class);

        Long count = userService.selectCount(queryWrapper);

        assertNotNull(count, "统计结果不应为空");
        assertTrue(count >= 0, "统计数量应该大于等于0");
    }

    @Test
    @Order(11)
    @DisplayName("测试selectCountByMap方法")
    void testSelectCountByMap() {
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("normal", 1);

        Long count = userService.selectCountByMap(columnMap);

        assertNotNull(count, "统计结果不应为空");
        assertTrue(count >= 0, "统计数量应该大于等于0");
    }

    @Test
    @Order(12)
    @DisplayName("测试exists方法")
    void testExists() {
        assertNotNull(insertedId, "需要先执行插入测试");

        QueryWrapper<JSONObject> queryWrapper = Wrappers.query(JSONObject.class);
        queryWrapper.eq("id", insertedId);

        boolean exists = userService.exists(queryWrapper);

        assertTrue(exists, "记录应该存在");
    }

    @Test
    @Order(13)
    @DisplayName("测试selectPage方法")
    void testSelectPage() {
        Page<JSONObject> page = new Page<>(1, 10);
        QueryWrapper<JSONObject> queryWrapper = Wrappers.query(JSONObject.class);

        Page<JSONObject> result = userService.selectPage(page, queryWrapper);

        assertNotNull(result, "分页结果不应为空");
        assertNotNull(result.getRecords(), "记录列表不应为空");
    }

    @Test
    @Order(14)
    @DisplayName("测试selectList带分页方法")
    void testSelectListWithPage() {
        Page<JSONObject> page = new Page<>(1, 5);
        QueryWrapper<JSONObject> queryWrapper = Wrappers.query(JSONObject.class);

        List<JSONObject> results = userService.selectList(page, queryWrapper);

        assertNotNull(results, "查询结果不应为空");
    }

    @Test
    @Order(98)
    @DisplayName("测试deleteById方法")
    void testDeleteById() {
        assertNotNull(insertedId, "需要先执行插入测试");

        int count = userService.deleteById(insertedId);

        assertTrue(count >= 0, "删除操作应该成功");
    }

    @Test
    @Order(99)
    @DisplayName("测试deleteBatchIds方法")
    void testDeleteBatchIds() {
        // 先插入测试数据
        JSONObject entity = new JSONObject();
        entity.set("username", "batch_delete_service_" + System.currentTimeMillis());
        entity.set("nickname", "批量删除服务测试");

        Long id = userService.insert(entity);
        assertNotNull(id);

        int count = userService.deleteBatchIds(Collections.singletonList(id));

        assertTrue(count >= 0, "批量删除操作应该成功");
    }

    /**
     * 测试用的UserRepositoryService实现
     */
    @Service
    static class TestUserRepositoryService extends AbstractBaseRepositoryService {

        public TestUserRepositoryService(BaseRepository baseRepository) {
            super(baseRepository);
        }

        @Override
        protected String getTableName() {
            return "sys_user";
        }
    }
}
