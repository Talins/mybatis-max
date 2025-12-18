package cn.talins.mybatis.max.test;

import cn.hutool.json.JSONObject;
import cn.talins.mybatis.max.api.IRepository;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * IRepository 接口默认方法测试
 * 测试接口中定义的默认方法逻辑
 *
 * @author talins
 */
@DisplayName("IRepository接口测试")
public class IRepositoryInterfaceTest {

    @Test
    @DisplayName("测试selectOne - 单条结果")
    void testSelectOneSingleResult() {
        // 创建mock实现
        IRepository repository = mock(IRepository.class);

        JSONObject expected = new JSONObject();
        expected.set("id", 1L);

        QueryWrapper<JSONObject> queryWrapper = Wrappers.query(JSONObject.class);

        when(repository.selectList(anyString(), eq(queryWrapper)))
                .thenReturn(Collections.singletonList(expected));
        when(repository.selectOne(anyString(), eq(queryWrapper)))
                .thenCallRealMethod();
        when(repository.selectOne(anyString(), eq(queryWrapper), eq(true)))
                .thenCallRealMethod();

        JSONObject result = repository.selectOne("test_table", queryWrapper);

        assertEquals(expected, result);
    }

    @Test
    @DisplayName("测试selectOne - 空结果")
    void testSelectOneEmptyResult() {
        IRepository repository = mock(IRepository.class);

        QueryWrapper<JSONObject> queryWrapper = Wrappers.query(JSONObject.class);

        when(repository.selectList(anyString(), eq(queryWrapper)))
                .thenReturn(Collections.emptyList());
        when(repository.selectOne(anyString(), eq(queryWrapper)))
                .thenCallRealMethod();
        when(repository.selectOne(anyString(), eq(queryWrapper), eq(true)))
                .thenCallRealMethod();

        JSONObject result = repository.selectOne("test_table", queryWrapper);

        assertNull(result);
    }

    @Test
    @DisplayName("测试selectOne - 多条结果抛出异常")
    void testSelectOneMultipleResultsThrowException() {
        IRepository repository = mock(IRepository.class);

        JSONObject obj1 = new JSONObject();
        obj1.set("id", 1L);
        JSONObject obj2 = new JSONObject();
        obj2.set("id", 2L);

        QueryWrapper<JSONObject> queryWrapper = Wrappers.query(JSONObject.class);

        when(repository.selectList(anyString(), eq(queryWrapper)))
                .thenReturn(Arrays.asList(obj1, obj2));
        when(repository.selectOne(anyString(), eq(queryWrapper)))
                .thenCallRealMethod();
        when(repository.selectOne(anyString(), eq(queryWrapper), eq(true)))
                .thenCallRealMethod();

        assertThrows(TooManyResultsException.class, () -> {
            repository.selectOne("test_table", queryWrapper);
        });
    }

    @Test
    @DisplayName("测试selectOne - 多条结果不抛异常返回第一条")
    void testSelectOneMultipleResultsNoThrow() {
        IRepository repository = mock(IRepository.class);

        JSONObject obj1 = new JSONObject();
        obj1.set("id", 1L);
        JSONObject obj2 = new JSONObject();
        obj2.set("id", 2L);

        QueryWrapper<JSONObject> queryWrapper = Wrappers.query(JSONObject.class);

        when(repository.selectList(anyString(), eq(queryWrapper)))
                .thenReturn(Arrays.asList(obj1, obj2));
        when(repository.selectOne(anyString(), eq(queryWrapper), eq(false)))
                .thenCallRealMethod();

        JSONObject result = repository.selectOne("test_table", queryWrapper, false);

        assertEquals(obj1, result);
    }

    @Test
    @DisplayName("测试exists - 存在记录")
    void testExistsTrue() {
        IRepository repository = mock(IRepository.class);

        QueryWrapper<JSONObject> queryWrapper = Wrappers.query(JSONObject.class);

        when(repository.selectCount(anyString(), eq(queryWrapper)))
                .thenReturn(5L);
        when(repository.exists(anyString(), eq(queryWrapper)))
                .thenCallRealMethod();

        boolean result = repository.exists("test_table", queryWrapper);

        assertTrue(result);
    }

    @Test
    @DisplayName("测试exists - 不存在记录")
    void testExistsFalse() {
        IRepository repository = mock(IRepository.class);

        QueryWrapper<JSONObject> queryWrapper = Wrappers.query(JSONObject.class);

        when(repository.selectCount(anyString(), eq(queryWrapper)))
                .thenReturn(0L);
        when(repository.exists(anyString(), eq(queryWrapper)))
                .thenCallRealMethod();

        boolean result = repository.exists("test_table", queryWrapper);

        assertFalse(result);
    }

    @Test
    @DisplayName("测试exists - count为null")
    void testExistsNull() {
        IRepository repository = mock(IRepository.class);

        QueryWrapper<JSONObject> queryWrapper = Wrappers.query(JSONObject.class);

        when(repository.selectCount(anyString(), eq(queryWrapper)))
                .thenReturn(null);
        when(repository.exists(anyString(), eq(queryWrapper)))
                .thenCallRealMethod();

        boolean result = repository.exists("test_table", queryWrapper);

        assertFalse(result);
    }

    @Test
    @DisplayName("测试deleteById调用deleteBatchIds")
    void testDeleteByIdCallsDeleteBatchIds() {
        IRepository repository = mock(IRepository.class);

        when(repository.deleteById(anyString(), anyLong()))
                .thenCallRealMethod();
        when(repository.deleteBatchIds(anyString(), anyCollection()))
                .thenReturn(1);

        int result = repository.deleteById("test_table", 1L);

        assertEquals(1, result);
        verify(repository).deleteBatchIds(eq("test_table"), eq(Collections.singletonList(1L)));
    }

    @Test
    @DisplayName("测试selectById调用selectBatchIds")
    void testSelectByIdCallsSelectBatchIds() {
        IRepository repository = mock(IRepository.class);

        JSONObject expected = new JSONObject();
        expected.set("id", 1L);

        when(repository.selectById(anyString(), anyLong(), any()))
                .thenCallRealMethod();
        when(repository.selectBatchIds(anyString(), anyCollection(), any()))
                .thenReturn(Collections.singletonList(expected));

        JSONObject result = repository.selectById("test_table", 1L, JSONObject.class);

        assertEquals(expected, result);
    }

    @Test
    @DisplayName("测试selectOneByMap - 有结果")
    void testSelectOneByMapWithResult() {
        IRepository repository = mock(IRepository.class);

        JSONObject expected = new JSONObject();
        expected.set("id", 1L);

        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("id", 1L);

        when(repository.selectOneByMap(anyString(), anyMap(), any()))
                .thenCallRealMethod();
        when(repository.selectByMap(anyString(), anyMap(), any()))
                .thenReturn(Collections.singletonList(expected));

        JSONObject result = repository.selectOneByMap("test_table", columnMap, JSONObject.class);

        assertEquals(expected, result);
    }

    @Test
    @DisplayName("测试selectOneByMap - 无结果")
    void testSelectOneByMapNoResult() {
        IRepository repository = mock(IRepository.class);

        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("id", 999L);

        when(repository.selectOneByMap(anyString(), anyMap(), any()))
                .thenCallRealMethod();
        when(repository.selectByMap(anyString(), anyMap(), any()))
                .thenReturn(Collections.emptyList());

        JSONObject result = repository.selectOneByMap("test_table", columnMap, JSONObject.class);

        assertNull(result);
    }
}
