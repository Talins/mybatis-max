package cn.talins.mybatis.max.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 线程本地缓存注解 - 用于方法级别的线程本地缓存
 * <p>
 * 标注此注解的方法，其返回结果会被缓存在当前线程的ThreadLocal中。
 * 在同一线程的后续调用中，如果参数相同，将直接返回缓存的结果，
 * 避免重复执行方法逻辑（如数据库查询）。
 * </p>
 * 
 * <p>
 * 适用场景：
 * <ul>
 *     <li>在一次请求中多次查询相同数据</li>
 *     <li>避免N+1查询问题</li>
 *     <li>减少重复的数据库访问</li>
 * </ul>
 * </p>
 * 
 * <p>
 * 注意事项：
 * <ul>
 *     <li>缓存仅在当前线程有效，请求结束后自动清理</li>
 *     <li>不适用于需要实时数据的场景</li>
 *     <li>需要配合AOP切面使用</li>
 * </ul>
 * </p>
 * 
 * <p>
 * 使用示例：
 * <pre>
 * &#64;ThreadLocalCache
 * public User getUserById(Long id) {
 *     return repository.selectById("user", id, User.class);
 * }
 * </pre>
 * </p>
 * 
 * @author talins
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ThreadLocalCache {
}
