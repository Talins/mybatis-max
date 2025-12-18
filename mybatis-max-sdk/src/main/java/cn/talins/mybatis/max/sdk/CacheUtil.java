package cn.talins.mybatis.max.sdk;

import net.oschina.j2cache.CacheChannel;
import net.oschina.j2cache.CacheObject;
import net.oschina.j2cache.J2Cache;

/**
 * 缓存工具类 - 基于J2Cache的二级缓存操作封装
 * <p>
 * 该类封装了J2Cache的常用操作，提供简洁的静态方法进行缓存的增删改查。
 * J2Cache是一个两级缓存框架，支持一级缓存（如Caffeine、EhCache）和
 * 二级缓存（如Redis）的组合使用。
 * </p>
 * 
 * <p>
 * 在MyBatis-Max框架中，缓存主要用于：
 * <ul>
 *     <li>缓存热点数据，减少数据库访问</li>
 *     <li>按表名分区存储，便于管理和清理</li>
 *     <li>在数据变更时自动更新或清除缓存</li>
 * </ul>
 * </p>
 * 
 * <p>
 * 缓存结构：
 * <ul>
 *     <li>region: 缓存区域，通常对应表名</li>
 *     <li>key: 缓存键，通常为记录ID</li>
 *     <li>value: 缓存值，通常为实体对象的JSON表示</li>
 * </ul>
 * </p>
 * 
 * @author talins
 * @see cn.talins.mybatis.max.sdk.repository.BaseRepository 使用缓存的仓库实现
 */
public class CacheUtil {

    /**
     * 检查缓存区域是否存在
     * 
     * @param region 缓存区域名称（通常为表名）
     * @return 如果区域存在返回true，否则返回false
     */
    public static Boolean exists(String region) {
        CacheChannel cache = J2Cache.getChannel();
        if (cache == null) {
            return false;
        }

        return cache.regions().stream().anyMatch(r -> r.getName().equals(region));
    }

    /**
     * 检查指定区域中的缓存键是否存在
     * 
     * @param region 缓存区域名称
     * @param key 缓存键
     * @return 如果键存在返回true，否则返回false
     */
    public static Boolean exists(String region, String key) {
        CacheChannel cache = J2Cache.getChannel();
        if (cache == null) {
            return false;
        }
        return cache.exists(region, key);
    }

    /**
     * 设置缓存值
     * 
     * @param region 缓存区域名称
     * @param key 缓存键
     * @param value 缓存值
     */
    public static void set(String region, String key, Object value) {
        CacheChannel cache = J2Cache.getChannel();
        if (cache == null) {
            return;
        }
        cache.set(region, key, value);
    }

    /**
     * 获取缓存值
     * 
     * @param region 缓存区域名称
     * @param key 缓存键
     * @return 缓存值，如果不存在返回null
     */
    public static Object get(String region, String key) {
        CacheChannel cache = J2Cache.getChannel();
        if (cache == null) {
            return null;
        }
        CacheObject cacheObject = cache.get(region, key);
        return cacheObject.getValue();
    }

    /**
     * 移除指定的缓存项
     * 
     * @param region 缓存区域名称
     * @param key 要移除的缓存键（可变参数，支持批量移除）
     */
    public static void remove(String region, String... key) {
        CacheChannel cache = J2Cache.getChannel();
        if (cache == null) {
            return;
        }
        cache.evict(region, key);
    }

    /**
     * 清空指定区域的所有缓存
     * 
     * @param region 缓存区域名称
     */
    public static void clear(String region) {
        CacheChannel cache = J2Cache.getChannel();
        if (cache == null) {
            return;
        }
        cache.clear(region);
    }
}
