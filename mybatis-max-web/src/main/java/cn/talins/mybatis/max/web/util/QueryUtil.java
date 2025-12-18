package cn.talins.mybatis.max.web.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.talins.mybatis.max.api.enums.Connect;
import cn.talins.mybatis.max.api.enums.Order;
import cn.talins.mybatis.max.api.pojo.Condition;
import cn.talins.mybatis.max.api.pojo.Query;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 查询工具类 - 将Query对象转换为MyBatis-Plus的QueryWrapper
 * <p>
 * 该类负责将前端传入的{@link Query}对象转换为MyBatis-Plus可识别的{@link QueryWrapper}，
 * 实现了查询条件的动态构建。
 * </p>
 * 
 * <p>
 * 转换规则：
 * <ul>
 *     <li>列名：驼峰命名自动转换为下划线命名（userName -> user_name）</li>
 *     <li>条件：根据Operator枚举转换为对应的SQL操作</li>
 *     <li>连接：根据Connect枚举使用AND或OR连接多个条件</li>
 *     <li>排序：根据Order枚举添加ORDER BY子句</li>
 * </ul>
 * </p>
 * 
 * @author talins
 * @see Query 查询对象
 * @see Condition 查询条件
 */
public class QueryUtil {

    /**
     * 将Query对象转换为QueryWrapper
     * <p>
     * 转换过程：
     * <ol>
     *     <li>处理查询列（SELECT子句）</li>
     *     <li>处理排序规则（ORDER BY子句）</li>
     *     <li>处理查询条件（WHERE子句）</li>
     * </ol>
     * </p>
     * 
     * @param query 查询对象
     * @return MyBatis-Plus的QueryWrapper
     */
    public static QueryWrapper<Map<String, Object>> toQueryWrapper(Query query) {
        QueryWrapper<Map<String, Object>> queryWrapper = Wrappers.query(new LinkedHashMap<>());

        if(CollUtil.isNotEmpty(query.getColumnList())) {
            queryWrapper.select(query.getColumnList().stream().map(StrUtil::toUnderlineCase).collect(Collectors.toList()));
        }

        if(CollUtil.isNotEmpty(query.getOrderMap())) {
            query.getOrderMap().forEach((k, v) -> {
                if(v == Order.DESC) {
                    queryWrapper.orderByDesc(StrUtil.toUnderlineCase(k));
                } else {
                    queryWrapper.orderByAsc(StrUtil.toUnderlineCase(k));
                }
            });
        }

        if(CollUtil.isNotEmpty(query.getConditionList())) {
            List<Condition> conditionList = query.getConditionList();
            for(int i = 0, size = conditionList.size(); i < size; i++) {
                Condition condition = conditionList.get(i);
                if(i > 0 && Connect.OR.equals(condition.getConnect())) {
                    queryWrapper.or(wrapper -> applyCondition(wrapper, condition));
                } else {
                    queryWrapper.and(wrapper -> applyCondition(wrapper, condition));
                }
            }
        }

        return queryWrapper;
    }

    /**
     * 将单个Condition应用到QueryWrapper
     * <p>
     * 根据Condition中的操作符类型，调用QueryWrapper的对应方法。
     * </p>
     * 
     * @param wrapper QueryWrapper实例
     * @param condition 查询条件
     */
    public static void applyCondition(QueryWrapper<?> wrapper, Condition condition) {
        // 列名转换：驼峰 -> 下划线
        String column = StrUtil.toUnderlineCase(condition.getColumn());
        
        // 根据操作符类型应用不同的查询方法
        switch (condition.getOperator()) {
            case EQUAL:
                // 等于：column = value
                wrapper.eq(column, condition.getParamList().get(0));
                break;
            case NOT_EQUAL:
                // 不等于：column != value
                wrapper.ne(column, condition.getParamList().get(0));
                break;
            case LIKE:
                // 模糊匹配：column LIKE '%value%'
                wrapper.like(column, condition.getParamList().get(0));
                break;
            case NOT_LIKE:
                // 不匹配：column NOT LIKE '%value%'
                wrapper.notLike(column, condition.getParamList().get(0));
                break;
            case IN:
                // 包含：column IN (value1, value2, ...)
                wrapper.in(column, condition.getParamList());
                break;
            case NOT_IN:
                // 不包含：column NOT IN (value1, value2, ...)
                wrapper.notIn(column, condition.getParamList());
                break;
            case BETWEEN:
                // 范围内：column BETWEEN value1 AND value2
                wrapper.between(column, condition.getParamList().get(0), condition.getParamList().get(1));
                break;
            case NOT_BETWEEN:
                // 范围外：column NOT BETWEEN value1 AND value2
                wrapper.notBetween(column, condition.getParamList().get(0), condition.getParamList().get(1));
                break;
            case GREAT:
                // 大于：column > value
                wrapper.gt(column, condition.getParamList().get(0));
                break;
            case LESS:
                // 小于：column < value
                wrapper.lt(column, condition.getParamList().get(0));
                break;
            case GREAT_EQUAL:
                // 大于等于：column >= value
                wrapper.ge(column, condition.getParamList().get(0));
                break;
            case LESS_EQUAL:
                // 小于等于：column <= value
                wrapper.le(column, condition.getParamList().get(0));
                break;
            case IS_NULL:
                // 为空：column IS NULL
                wrapper.isNull(column);
                break;
            case IS_NOT_NULL:
                // 不为空：column IS NOT NULL
                wrapper.isNotNull(column);
                break;
            default:
                // 未知操作符，忽略
                break;
        }
    }
}
