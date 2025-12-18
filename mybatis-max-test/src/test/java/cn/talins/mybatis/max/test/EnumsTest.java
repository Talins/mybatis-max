package cn.talins.mybatis.max.test;

import cn.talins.mybatis.max.api.enums.Booleans;
import cn.talins.mybatis.max.api.enums.Connect;
import cn.talins.mybatis.max.api.enums.Operator;
import cn.talins.mybatis.max.api.enums.Order;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 枚举类测试
 * 测试所有枚举类的值和映射
 *
 * @author talins
 */
@DisplayName("枚举类测试")
public class EnumsTest {

    @Nested
    @DisplayName("Booleans枚举测试")
    class BooleansTest {

        @Test
        @DisplayName("测试TRUE值")
        void testTrueValue() {
            assertEquals(1, Booleans.TRUE.getValue());
        }

        @Test
        @DisplayName("测试FALSE值")
        void testFalseValue() {
            assertEquals(0, Booleans.FALSE.getValue());
        }

        @Test
        @DisplayName("测试MAP映射")
        void testMapMapping() {
            assertEquals(Booleans.TRUE, Booleans.MAP.get(1));
            assertEquals(Booleans.FALSE, Booleans.MAP.get(0));
        }

        @Test
        @DisplayName("测试NAME_MAP映射")
        void testNameMapMapping() {
            assertEquals(Booleans.TRUE, Booleans.NAME_MAP.get("TRUE"));
            assertEquals(Booleans.FALSE, Booleans.NAME_MAP.get("FALSE"));
        }
    }

    @Nested
    @DisplayName("Connect枚举测试")
    class ConnectTest {

        @Test
        @DisplayName("测试AND值")
        void testAndValue() {
            assertEquals(1, Connect.AND.getValue());
        }

        @Test
        @DisplayName("测试OR值")
        void testOrValue() {
            assertEquals(2, Connect.OR.getValue());
        }

        @Test
        @DisplayName("测试MAP映射")
        void testMapMapping() {
            assertEquals(Connect.AND, Connect.MAP.get(1));
            assertEquals(Connect.OR, Connect.MAP.get(2));
        }

        @Test
        @DisplayName("测试NAME_MAP映射")
        void testNameMapMapping() {
            assertEquals(Connect.AND, Connect.NAME_MAP.get("AND"));
            assertEquals(Connect.OR, Connect.NAME_MAP.get("OR"));
        }
    }

    @Nested
    @DisplayName("Order枚举测试")
    class OrderTest {

        @Test
        @DisplayName("测试ASC值")
        void testAscValue() {
            assertEquals(1, Order.ASC.getValue());
        }

        @Test
        @DisplayName("测试DESC值")
        void testDescValue() {
            assertEquals(2, Order.DESC.getValue());
        }

        @Test
        @DisplayName("测试MAP映射")
        void testMapMapping() {
            assertEquals(Order.ASC, Order.MAP.get(1));
            assertEquals(Order.DESC, Order.MAP.get(2));
        }

        @Test
        @DisplayName("测试NAME_MAP映射")
        void testNameMapMapping() {
            assertEquals(Order.ASC, Order.NAME_MAP.get("ASC"));
            assertEquals(Order.DESC, Order.NAME_MAP.get("DESC"));
        }
    }

    @Nested
    @DisplayName("Operator枚举测试")
    class OperatorTest {

        @Test
        @DisplayName("测试EQUAL操作符")
        void testEqualOperator() {
            assertEquals("=", Operator.EQUAL.getValue());
        }

        @Test
        @DisplayName("测试比较操作符")
        void testComparisonOperators() {
            assertEquals("<", Operator.LESS.getValue());
            assertEquals(">", Operator.GREAT.getValue());
            assertEquals("<=", Operator.LESS_EQUAL.getValue());
            assertEquals(">=", Operator.GREAT_EQUAL.getValue());
            assertEquals("!=", Operator.NOT_EQUAL.getValue());
        }

        @Test
        @DisplayName("测试LIKE操作符")
        void testLikeOperators() {
            assertEquals("LIKE", Operator.LIKE.getValue());
            assertEquals("NOT LIKE", Operator.NOT_LIKE.getValue());
        }

        @Test
        @DisplayName("测试NULL操作符")
        void testNullOperators() {
            assertEquals("IS NULL", Operator.IS_NULL.getValue());
            assertEquals("IS NOT NULL", Operator.IS_NOT_NULL.getValue());
        }

        @Test
        @DisplayName("测试IN操作符")
        void testInOperators() {
            assertEquals("IN", Operator.IN.getValue());
            assertEquals("NOT IN", Operator.NOT_IN.getValue());
        }

        @Test
        @DisplayName("测试BETWEEN操作符")
        void testBetweenOperators() {
            assertEquals("BETWEEN", Operator.BETWEEN.getValue());
            assertEquals("NOT BETWEEN", Operator.NOT_BETWEEN.getValue());
        }

        @Test
        @DisplayName("测试MAP映射")
        void testMapMapping() {
            assertEquals(Operator.EQUAL, Operator.MAP.get("="));
            assertEquals(Operator.LIKE, Operator.MAP.get("LIKE"));
            assertEquals(Operator.IN, Operator.MAP.get("IN"));
        }

        @Test
        @DisplayName("测试NAME_MAP映射")
        void testNameMapMapping() {
            assertEquals(Operator.EQUAL, Operator.NAME_MAP.get("EQUAL"));
            assertEquals(Operator.LESS, Operator.NAME_MAP.get("LESS"));
            assertEquals(Operator.GREAT, Operator.NAME_MAP.get("GREAT"));
            assertEquals(Operator.LIKE, Operator.NAME_MAP.get("LIKE"));
            assertEquals(Operator.IN, Operator.NAME_MAP.get("IN"));
            assertEquals(Operator.BETWEEN, Operator.NAME_MAP.get("BETWEEN"));
        }

        @Test
        @DisplayName("测试所有操作符数量")
        void testAllOperatorsCount() {
            assertEquals(14, Operator.values().length);
        }
    }
}
