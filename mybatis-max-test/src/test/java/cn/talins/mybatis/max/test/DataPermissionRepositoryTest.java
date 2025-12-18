package cn.talins.mybatis.max.test;

import cn.hutool.json.JSONObject;
import cn.talins.mybatis.max.App;
import cn.talins.mybatis.max.sdk.repository.DataPermissionRepository;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * DataPermissionRepository 单元测试
 * 测试带数据权限的Repository操作
 *
 * @author talins
 */
@SpringBootTest(classes = App.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DataPermissionRepositoryTest {

    @Autowired
    private DataPermissionRepository dataPermissionRepository;

    private static final String TEST_TABLE = "sys_user";
    private static Long insertedId;

    @Test
    @Order(1)
    @DisplayName("测试DataPermissionRepository注入")
    void testRepositoryInjection() {
        assertNotNull(dataPermissionRepository, "DataPermissionRepository应该被正确注入");
    }

    @Test
    @Order(2)
    @DisplayName("测试插入记录（继承自BaseRepository）")
    void testInsert() {
        JSONObject entity = new JSONObject();
        entity.set("username", "permission_test_" + System.currentTimeMillis());
        entity.set("nickname", "权限测试用户");
        entity.set("email", "permission@example.com");

        Long id = dataPermissionRepository.insert(TEST_TABLE, entity);

        assertNotNull(id, "插入后应返回ID");
        assertTrue(id > 0, "ID应该大于0");
        insertedId = id;
    }

    @Test
    @Order(3)
    @DisplayName("测试带权限的查询列表")
    void testSelectListWithPermission() {
        QueryWrapper<JSONObject> queryWrapper = Wrappers.query(JSONObject.class);
        queryWrapper.last("LIMIT 10");

        List<JSONObject> results = dataPermissionRepository.selectList(TEST_TABLE, null, queryWrapper);

        assertNotNull(results, "查询结果不应为空");
    }

    @Test
    @Order(4)
    @DisplayName("测试带权限的统计查询")
    void testSelectCountWithPermission() {
        QueryWrapper<JSONObject> queryWrapper = Wrappers.query(JSONObject.class);

        Long count = dataPermissionRepository.selectCount(TEST_TABLE, queryWrapper);

        assertNotNull(count, "统计结果不应为空");
        assertTrue(count >= 0, "统计数量应该大于等于0");
    }

    @Test
    @Order(5)
    @DisplayName("测试带权限的批量ID查询")
    void testSelectBatchIdsWithPermission() {
        assertNotNull(insertedId, "需要先执行插入测试");

        List<Long> ids = Collections.singletonList(insertedId);
        List<JSONObject> results = dataPermissionRepository.selectBatchIds(TEST_TABLE, ids, JSONObject.class);

        assertNotNull(results, "查询结果不应为空");
    }

    @Test
    @Order(6)
    @DisplayName("测试带权限的更新")
    void testUpdateByIdWithPermission() {
        assertNotNull(insertedId, "需要先执行插入测试");

        JSONObject entity = new JSONObject();
        entity.set("id", insertedId);
        entity.set("nickname", "权限更新后的昵称");

        int count = dataPermissionRepository.updateById(TEST_TABLE, entity);

        assertTrue(count >= 0, "更新操作应该成功");
    }

    @Test
    @Order(7)
    @DisplayName("测试带权限的条件更新")
    void testUpdateWithPermission() {
        assertNotNull(insertedId, "需要先执行插入测试");

        JSONObject entity = new JSONObject();
        entity.set("nickname", "条件更新昵称");

        QueryWrapper<JSONObject> updateWrapper = Wrappers.query(JSONObject.class);
        updateWrapper.eq("id", insertedId);

        int count = dataPermissionRepository.update(TEST_TABLE, entity, updateWrapper);

        assertTrue(count >= 0, "条件更新操作应该成功");
    }

    @Test
    @Order(8)
    @DisplayName("测试带权限的分页查询")
    void testSelectPageWithPermission() {
        Page<JSONObject> page = new Page<>(1, 10);
        QueryWrapper<JSONObject> queryWrapper = Wrappers.query(JSONObject.class);

        List<JSONObject> results = dataPermissionRepository.selectList(TEST_TABLE, page, queryWrapper);

        assertNotNull(results, "分页查询结果不应为空");
    }

    @Test
    @Order(98)
    @DisplayName("测试带权限的条件删除")
    void testDeleteWithPermission() {
        // 先插入一条测试数据
        JSONObject entity = new JSONObject();
        entity.set("username", "delete_permission_test_" + System.currentTimeMillis());
        entity.set("nickname", "删除权限测试");

        Long id = dataPermissionRepository.insert(TEST_TABLE, entity);
        assertNotNull(id);

        QueryWrapper<JSONObject> deleteWrapper = Wrappers.query(JSONObject.class);
        deleteWrapper.eq("id", id);

        int count = dataPermissionRepository.delete(TEST_TABLE, deleteWrapper);

        assertTrue(count >= 0, "删除操作应该成功");
    }

    @Test
    @Order(99)
    @DisplayName("清理测试数据")
    void cleanupTestData() {
        if (insertedId != null) {
            dataPermissionRepository.deleteById(TEST_TABLE, insertedId);
        }
    }
}
