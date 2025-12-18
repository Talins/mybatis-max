package cn.talins.mybatis.max.sdk;

import lombok.Getter;
import org.springframework.core.NamedThreadLocal;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 动态数据源 - 实现多数据源的动态切换
 * <p>
 * 该类继承自Spring的{@link AbstractRoutingDataSource}，通过ThreadLocal实现
 * 线程级别的数据源切换。支持嵌套切换（使用栈结构存储数据源名称）。
 * </p>
 * 
 * <p>
 * 工作原理：
 * <ol>
 *     <li>在配置文件中定义多个数据源</li>
 *     <li>框架启动时自动创建DynamicDataSource并注册所有数据源</li>
 *     <li>执行SQL前通过{@link #push(String)}切换到目标数据源</li>
 *     <li>执行SQL后通过{@link #poll()}恢复到上一个数据源</li>
 * </ol>
 * </p>
 * 
 * <p>
 * 配置示例（application.yml）：
 * <pre>
 * spring:
 *   datasource:
 *     master:
 *       url: jdbc:mysql://localhost:3306/master
 *       username: root
 *       password: root
 *     slave:
 *       url: jdbc:mysql://localhost:3306/slave
 *       username: root
 *       password: root
 * </pre>
 * </p>
 * 
 * <p>
 * 使用示例：
 * <pre>
 * try {
 *     DynamicDataSource.push("slave");
 *     // 执行查询操作，使用slave数据源
 *     List&lt;User&gt; users = repository.selectList("user", wrapper);
 * } finally {
 *     DynamicDataSource.poll();
 * }
 * </pre>
 * </p>
 * 
 * @author talins
 * @see cn.talins.mybatis.max.starter.BeanConfiguration#dataSource 数据源配置
 */
@Getter
public class DynamicDataSource extends AbstractRoutingDataSource {

    /**
     * 数据源名称到DataSource实例的映射
     */
    private final Map<String, DataSource> dataSourceMap = new ConcurrentHashMap<>();

    /**
     * 线程本地变量，使用栈结构存储数据源名称，支持嵌套切换
     */
    private static final ThreadLocal<Deque<String>> CONTEXT_HOLDER = new NamedThreadLocal<Deque<String>>("dynamic-datasource") {
        @Override
        protected Deque<String> initialValue() {
            return new ArrayDeque<>();
        }
    };

    /**
     * 获取当前线程使用的数据源名称
     * <p>
     * 查看栈顶的数据源名称，不会移除。
     * </p>
     *
     * @return 当前数据源名称，如果栈为空返回null（使用默认数据源）
     */
    public static String peek() {
        return CONTEXT_HOLDER.get().peek();
    }

    /**
     * 切换到指定数据源
     * <p>
     * 将数据源名称压入栈中，后续的数据库操作将使用该数据源。
     * 支持嵌套调用，每次push都需要对应的poll来恢复。
     * </p>
     * 
     * <p>
     * 注意：调用此方法后必须在finally块中调用{@link #poll()}，
     * 否则可能导致后续操作使用错误的数据源。
     * </p>
     *
     * @param ds 数据源名称
     * @return 数据源名称
     */
    public static String push(String ds) {
        String dataSourceStr = ds == null ? "" : ds;
        CONTEXT_HOLDER.get().push(dataSourceStr);
        return dataSourceStr;
    }

    /**
     * 恢复到上一个数据源
     * <p>
     * 从栈中弹出当前数据源名称，恢复到上一个数据源。
     * 如果栈为空，则清除ThreadLocal以防止内存泄漏。
     * </p>
     */
    public static void poll() {
        Deque<String> deque = CONTEXT_HOLDER.get();
        deque.poll();
        if (deque.isEmpty()) {
            clear();
        }
    }

    /**
     * 强制清空当前线程的数据源设置
     * <p>
     * 清除ThreadLocal中的所有数据源设置，防止内存泄漏。
     * 通常在请求结束时调用，或在异常处理中确保清理。
     * </p>
     */
    public static void clear() {
        CONTEXT_HOLDER.remove();
    }

    /**
     * 确定当前使用的数据源
     * <p>
     * Spring框架在获取数据库连接时调用此方法，
     * 返回值用于从targetDataSources中查找对应的DataSource。
     * </p>
     * 
     * @return 当前数据源的查找键（数据源名称）
     */
    @Override
    protected Object determineCurrentLookupKey() {
        return peek();
    }

    /**
     * 添加数据源到映射表
     * <p>
     * 在初始化时调用，将数据源名称和实例添加到内部映射表中。
     * </p>
     * 
     * @param name 数据源名称
     * @param dataSource 数据源实例
     */
    public void putDataSourceMap(String name, DataSource dataSource) {
        dataSourceMap.put(name, dataSource);
    }
}
