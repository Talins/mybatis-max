package cn.talins.mybatis.max.test;

import cn.talins.mybatis.max.starter.MybatisMaxProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * MybatisMaxProperties 配置属性测试
 *
 * @author talins
 */
@DisplayName("MybatisMaxProperties测试")
public class MybatisMaxPropertiesTest {

    @Test
    @DisplayName("测试默认workerId为null")
    void testDefaultWorkerIdIsNull() {
        MybatisMaxProperties properties = new MybatisMaxProperties();

        assertNull(properties.getWorkerId(), "默认workerId应该为null");
    }

    @Test
    @DisplayName("测试设置workerId")
    void testSetWorkerId() {
        MybatisMaxProperties properties = new MybatisMaxProperties();
        properties.setWorkerId((short) 1);

        assertEquals((short) 1, properties.getWorkerId());
    }

    @Test
    @DisplayName("测试workerId边界值 - 最小值")
    void testWorkerIdMinValue() {
        MybatisMaxProperties properties = new MybatisMaxProperties();
        properties.setWorkerId(Short.MIN_VALUE);

        assertEquals(Short.MIN_VALUE, properties.getWorkerId());
    }

    @Test
    @DisplayName("测试workerId边界值 - 最大值")
    void testWorkerIdMaxValue() {
        MybatisMaxProperties properties = new MybatisMaxProperties();
        properties.setWorkerId(Short.MAX_VALUE);

        assertEquals(Short.MAX_VALUE, properties.getWorkerId());
    }

    @Test
    @DisplayName("测试workerId为0")
    void testWorkerIdZero() {
        MybatisMaxProperties properties = new MybatisMaxProperties();
        properties.setWorkerId((short) 0);

        assertEquals((short) 0, properties.getWorkerId());
    }
}
