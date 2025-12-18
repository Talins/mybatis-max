package cn.talins.mybatis.max.test;

import cn.talins.mybatis.max.sdk.DynamicDataSource;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * DynamicDataSource 单元测试
 * 测试动态数据源切换功能
 *
 * @author talins
 */
@DisplayName("动态数据源测试")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DynamicDataSourceTest {

    @BeforeEach
    void setUp() {
        // 确保每个测试前清空ThreadLocal
        DynamicDataSource.clear();
    }

    @AfterEach
    void tearDown() {
        // 确保每个测试后清空ThreadLocal
        DynamicDataSource.clear();
    }

    @Test
    @Order(1)
    @DisplayName("测试push数据源")
    void testPush() {
        String result = DynamicDataSource.push("master");

        assertEquals("master", result);
        assertEquals("master", DynamicDataSource.peek());
    }

    @Test
    @Order(2)
    @DisplayName("测试push空数据源")
    void testPushNull() {
        String result = DynamicDataSource.push(null);

        assertEquals("", result);
        assertEquals("", DynamicDataSource.peek());
    }

    @Test
    @Order(3)
    @DisplayName("测试peek数据源")
    void testPeek() {
        DynamicDataSource.push("slave");

        String result = DynamicDataSource.peek();

        assertEquals("slave", result);
        // peek不应该移除数据源
        assertEquals("slave", DynamicDataSource.peek());
    }

    @Test
    @Order(4)
    @DisplayName("测试peek空栈")
    void testPeekEmpty() {
        String result = DynamicDataSource.peek();

        assertNull(result);
    }

    @Test
    @Order(5)
    @DisplayName("测试poll数据源")
    void testPoll() {
        DynamicDataSource.push("master");
        DynamicDataSource.push("slave");

        assertEquals("slave", DynamicDataSource.peek());

        DynamicDataSource.poll();

        assertEquals("master", DynamicDataSource.peek());
    }

    @Test
    @Order(6)
    @DisplayName("测试poll清空栈")
    void testPollClearStack() {
        DynamicDataSource.push("master");

        DynamicDataSource.poll();

        assertNull(DynamicDataSource.peek());
    }

    @Test
    @Order(7)
    @DisplayName("测试clear清空数据源")
    void testClear() {
        DynamicDataSource.push("master");
        DynamicDataSource.push("slave");

        DynamicDataSource.clear();

        assertNull(DynamicDataSource.peek());
    }

    @Test
    @Order(8)
    @DisplayName("测试数据源栈操作")
    void testStackOperations() {
        // 模拟嵌套数据源切换
        DynamicDataSource.push("master");
        assertEquals("master", DynamicDataSource.peek());

        DynamicDataSource.push("slave1");
        assertEquals("slave1", DynamicDataSource.peek());

        DynamicDataSource.push("slave2");
        assertEquals("slave2", DynamicDataSource.peek());

        // 依次弹出
        DynamicDataSource.poll();
        assertEquals("slave1", DynamicDataSource.peek());

        DynamicDataSource.poll();
        assertEquals("master", DynamicDataSource.peek());

        DynamicDataSource.poll();
        assertNull(DynamicDataSource.peek());
    }

    @Test
    @Order(9)
    @DisplayName("测试线程隔离")
    void testThreadIsolation() throws InterruptedException {
        DynamicDataSource.push("main-thread-ds");

        Thread thread = new Thread(() -> {
            // 子线程应该有独立的数据源栈
            assertNull(DynamicDataSource.peek());

            DynamicDataSource.push("sub-thread-ds");
            assertEquals("sub-thread-ds", DynamicDataSource.peek());

            DynamicDataSource.clear();
        });

        thread.start();
        thread.join();

        // 主线程的数据源不应受影响
        assertEquals("main-thread-ds", DynamicDataSource.peek());
    }

    @Test
    @Order(10)
    @DisplayName("测试DynamicDataSource实例化")
    void testDynamicDataSourceInstance() {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();

        assertNotNull(dynamicDataSource);
        assertNotNull(dynamicDataSource.getDataSourceMap());
        assertTrue(dynamicDataSource.getDataSourceMap().isEmpty());
    }
}
