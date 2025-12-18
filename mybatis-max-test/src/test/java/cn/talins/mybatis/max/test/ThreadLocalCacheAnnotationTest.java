package cn.talins.mybatis.max.test;

import cn.talins.mybatis.max.api.annotation.ThreadLocalCache;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ThreadLocalCache 注解测试
 *
 * @author talins
 */
@DisplayName("ThreadLocalCache注解测试")
public class ThreadLocalCacheAnnotationTest {

    @Test
    @DisplayName("测试注解存在")
    void testAnnotationExists() {
        assertNotNull(ThreadLocalCache.class, "ThreadLocalCache注解应该存在");
    }

    @Test
    @DisplayName("测试注解保留策略为RUNTIME")
    void testRetentionPolicy() {
        Retention retention = ThreadLocalCache.class.getAnnotation(Retention.class);

        assertNotNull(retention, "应该有@Retention注解");
        assertEquals(RetentionPolicy.RUNTIME, retention.value(), "保留策略应该是RUNTIME");
    }

    @Test
    @DisplayName("测试注解目标为METHOD")
    void testTargetElement() {
        Target target = ThreadLocalCache.class.getAnnotation(Target.class);

        assertNotNull(target, "应该有@Target注解");
        assertEquals(1, target.value().length, "应该只有一个目标元素");
        assertEquals(ElementType.METHOD, target.value()[0], "目标元素应该是METHOD");
    }

    @Test
    @DisplayName("测试注解可以应用于方法")
    void testAnnotationOnMethod() throws NoSuchMethodException {
        Method method = TestClass.class.getMethod("annotatedMethod");

        ThreadLocalCache annotation = method.getAnnotation(ThreadLocalCache.class);

        assertNotNull(annotation, "方法上应该有ThreadLocalCache注解");
    }

    @Test
    @DisplayName("测试未注解的方法")
    void testMethodWithoutAnnotation() throws NoSuchMethodException {
        Method method = TestClass.class.getMethod("normalMethod");

        ThreadLocalCache annotation = method.getAnnotation(ThreadLocalCache.class);

        assertNull(annotation, "普通方法不应该有ThreadLocalCache注解");
    }

    /**
     * 测试用的类
     */
    static class TestClass {

        @ThreadLocalCache
        public void annotatedMethod() {
            // 带注解的方法
        }

        public void normalMethod() {
            // 普通方法
        }
    }
}
