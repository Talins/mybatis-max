package cn.talins.mybatis.max.api;

/**
 * ID生成器接口 - 定义全局唯一ID的生成策略
 * <p>
 * 该接口用于生成数据库主键ID，框架默认使用雪花算法（Snowflake）实现，
 * 用户也可以自定义实现以满足特殊需求。
 * </p>
 * 
 * <p>
 * 默认实现使用Yitter IdGenerator（雪花算法变种），具有以下特点：
 * <ul>
 *     <li>高性能：单机每秒可生成数百万个ID</li>
 *     <li>趋势递增：生成的ID大致按时间递增，有利于数据库索引</li>
 *     <li>分布式友好：通过workerId区分不同节点，避免ID冲突</li>
 *     <li>ID较短：相比标准雪花算法，生成的ID位数更短</li>
 * </ul>
 * </p>
 * 
 * <p>
 * 自定义实现示例：
 * <pre>
 * &#64;Bean
 * public IIdGenerator customIdGenerator() {
 *     return () -> {
 *         // 使用UUID
 *         return UUID.randomUUID().getMostSignificantBits() &amp; Long.MAX_VALUE;
 *     };
 * }
 * </pre>
 * </p>
 * 
 * @author talins
 * @see cn.talins.mybatis.max.starter.BeanConfiguration#idGenerator 默认ID生成器配置
 */
public interface IIdGenerator {

    /**
     * 生成下一个全局唯一ID
     * <p>
     * 该方法必须保证线程安全，且在分布式环境下不会产生重复ID。
     * </p>
     * 
     * @return 全局唯一的Long类型ID
     */
    Long nextId();
}
