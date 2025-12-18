package cn.talins.mybatis.max.test;

import cn.talins.mybatis.max.api.enums.Connect;
import cn.talins.mybatis.max.api.enums.Operator;
import cn.talins.mybatis.max.api.enums.Order;
import cn.talins.mybatis.max.api.pojo.Condition;
import cn.talins.mybatis.max.api.pojo.Query;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Query和Condition构建器测试
 * 测试查询条件的构建功能
 *
 * @author talins
 */
@DisplayName("查询条件构建器测试")
public class QueryConditionTest {

    @Test
    @DisplayName("测试Query基本构建")
    void testQueryBasicBuild() {
        Query query = Query.newInstance()
                .addColumn("id")
                .addColumn("username")
                .addColumn("nickname");

        assertNotNull(query);
        assertEquals(3, query.getColumnList().size());
        assertTrue(query.getColumnList().contains("id"));
        assertTrue(query.getColumnList().contains("username"));
        assertTrue(query.getColumnList().contains("nickname"));
    }

    @Test
    @DisplayName("测试Query添加排序")
    void testQueryAddOrder() {
        Query query = Query.newInstance()
                .addOrderBy("id", Order.DESC)
                .addOrderBy("createTime", Order.ASC);

        assertNotNull(query.getOrderMap());
        assertEquals(2, query.getOrderMap().size());
        assertEquals(Order.DESC, query.getOrderMap().get("id"));
        assertEquals(Order.ASC, query.getOrderMap().get("createTime"));
    }

    @Test
    @DisplayName("测试Query添加简单条件")
    void testQueryAddSimpleCondition() {
        Query query = Query.newInstance()
                .addCondition("username", "admin")
                .addCondition("status", 1);

        assertNotNull(query.getConditionList());
        assertEquals(2, query.getConditionList().size());

        Condition firstCondition = query.getConditionList().get(0);
        assertEquals("username", firstCondition.getColumn());
        assertEquals("admin", firstCondition.getParamList().get(0));
    }

    @Test
    @DisplayName("测试Query添加复杂条件")
    void testQueryAddComplexCondition() {
        Condition condition = Condition.newInstance()
                .setColumn("age")
                .setOperator(Operator.GREAT_EQUAL)
                .addParam(18);

        Query query = Query.newInstance().addCondition(condition);

        assertEquals(1, query.getConditionList().size());
        assertEquals(Operator.GREAT_EQUAL, query.getConditionList().get(0).getOperator());
    }

    @Test
    @DisplayName("测试Condition基本构建")
    void testConditionBasicBuild() {
        Condition condition = Condition.newInstance()
                .setColumn("name")
                .setOperator(Operator.EQUAL)
                .addParam("test");

        assertNotNull(condition);
        assertEquals("name", condition.getColumn());
        assertEquals(Operator.EQUAL, condition.getOperator());
        assertEquals(1, condition.getParamList().size());
        assertEquals("test", condition.getParamList().get(0));
    }

    @Test
    @DisplayName("测试Condition设置连接符")
    void testConditionSetConnect() {
        Condition condition = Condition.newInstance()
                .setConnect(Connect.OR)
                .setColumn("status")
                .addParam(1);

        assertEquals(Connect.OR, condition.getConnect());
    }

    @Test
    @DisplayName("测试Condition默认连接符为AND")
    void testConditionDefaultConnect() {
        Condition condition = Condition.newInstance();

        assertEquals(Connect.AND, condition.getConnect());
    }

    @Test
    @DisplayName("测试Condition IN操作符")
    void testConditionInOperator() {
        Condition condition = Condition.newInstance()
                .setColumn("id")
                .setOperator(Operator.IN)
                .addParams(Arrays.asList(1L, 2L, 3L));

        assertEquals(Operator.IN, condition.getOperator());
        assertEquals(3, condition.getParamList().size());
    }

    @Test
    @DisplayName("测试Condition BETWEEN操作符")
    void testConditionBetweenOperator() {
        Condition condition = Condition.newInstance()
                .setColumn("createTime")
                .setOperator(Operator.BETWEEN)
                .addParam("2024-01-01")
                .addParam("2024-12-31");

        assertEquals(Operator.BETWEEN, condition.getOperator());
        assertEquals(2, condition.getParamList().size());
    }

    @Test
    @DisplayName("测试Condition LIKE操作符")
    void testConditionLikeOperator() {
        Condition condition = Condition.newInstance()
                .setColumn("username")
                .setOperator(Operator.LIKE)
                .addParam("%admin%");

        assertEquals(Operator.LIKE, condition.getOperator());
        assertEquals("%admin%", condition.getParamList().get(0));
    }

    @Test
    @DisplayName("测试Condition IS_NULL操作符")
    void testConditionIsNullOperator() {
        Condition condition = Condition.newInstance()
                .setColumn("deletedAt")
                .setOperator(Operator.IS_NULL);

        assertEquals(Operator.IS_NULL, condition.getOperator());
        assertTrue(condition.getParamList().isEmpty());
    }

    @Test
    @DisplayName("测试Condition IS_NOT_NULL操作符")
    void testConditionIsNotNullOperator() {
        Condition condition = Condition.newInstance()
                .setColumn("email")
                .setOperator(Operator.IS_NOT_NULL);

        assertEquals(Operator.IS_NOT_NULL, condition.getOperator());
    }

    @Test
    @DisplayName("测试完整的Query构建")
    void testCompleteQueryBuild() {
        Query query = Query.newInstance()
                .addColumn("id")
                .addColumn("username")
                .addColumn("email")
                .addCondition(Condition.newInstance()
                        .setColumn("status")
                        .setOperator(Operator.EQUAL)
                        .addParam(1))
                .addCondition(Condition.newInstance()
                        .setConnect(Connect.AND)
                        .setColumn("age")
                        .setOperator(Operator.GREAT_EQUAL)
                        .addParam(18))
                .addOrderBy("id", Order.DESC);

        assertEquals(3, query.getColumnList().size());
        assertEquals(2, query.getConditionList().size());
        assertEquals(1, query.getOrderMap().size());
    }
}
