package cn.talins.mybatis.max.test;

import cn.talins.mybatis.max.sdk.CacheUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * CacheUtil 单元测试
 * 测试缓存工具类的功能
 * 注意：这些测试在没有配置J2Cache的情况下会返回默认值
 *
 * @author talins
 */
@DisplayName("CacheUtil测试")
public class CacheUtilTest {

    private static final String TEST_REGION = "test_region";
    private static final String TEST_KEY = "test_key";

    @Test
    @DisplayName("测试exists方法 - region不存在")
    void testExistsRegionNotExists() {
        // 在没有配置J2Cache的情况下，应该返回false
        Boolean result = CacheUtil.exists("non_existent_region");

        assertNotNull(result);
        assertFalse(result, "不存在的region应该返回false");
    }

    @Test
    @DisplayName("测试exists方法 - key不存在")
    void testExistsKeyNotExists() {
        Boolean result = CacheUtil.exists(TEST_REGION, "non_existent_key");

        assertNotNull(result);
        assertFalse(result, "不存在的key应该返回false");
    }

    @Test
    @DisplayName("测试get方法 - 缓存未配置")
    void testGetWithoutCache() {
        Object result = CacheUtil.get(TEST_REGION, TEST_KEY);

        // 在没有配置J2Cache的情况下，应该返回null
        assertNull(result, "未配置缓存时应该返回null");
    }

    @Test
    @DisplayName("测试set方法 - 缓存未配置不抛异常")
    void testSetWithoutCache() {
        // 在没有配置J2Cache的情况下，set操作不应该抛出异常
        assertDoesNotThrow(() -> {
            CacheUtil.set(TEST_REGION, TEST_KEY, "test_value");
        }, "set操作不应该抛出异常");
    }

    @Test
    @DisplayName("测试remove方法 - 缓存未配置不抛异常")
    void testRemoveWithoutCache() {
        assertDoesNotThrow(() -> {
            CacheUtil.remove(TEST_REGION, TEST_KEY);
        }, "remove操作不应该抛出异常");
    }

    @Test
    @DisplayName("测试remove方法 - 多个key")
    void testRemoveMultipleKeys() {
        assertDoesNotThrow(() -> {
            CacheUtil.remove(TEST_REGION, "key1", "key2", "key3");
        }, "remove多个key不应该抛出异常");
    }

    @Test
    @DisplayName("测试clear方法 - 缓存未配置不抛异常")
    void testClearWithoutCache() {
        assertDoesNotThrow(() -> {
            CacheUtil.clear(TEST_REGION);
        }, "clear操作不应该抛出异常");
    }

    @Test
    @DisplayName("测试exists方法返回类型")
    void testExistsReturnType() {
        Boolean result = CacheUtil.exists(TEST_REGION);

        assertNotNull(result, "exists方法不应该返回null");
        assertTrue(result instanceof Boolean, "exists方法应该返回Boolean类型");
    }

    @Test
    @DisplayName("测试缓存操作的空安全性")
    void testNullSafety() {
        // 测试各种边界情况不会抛出NullPointerException
        assertDoesNotThrow(() -> {
            CacheUtil.exists("");
            CacheUtil.exists("", "");
            CacheUtil.get("", "");
            CacheUtil.set("", "", null);
            CacheUtil.remove("");
            CacheUtil.clear("");
        }, "缓存操作应该是空安全的");
    }
}
