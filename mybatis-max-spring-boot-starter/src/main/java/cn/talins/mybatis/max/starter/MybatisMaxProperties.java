package cn.talins.mybatis.max.starter;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * MyBatis-Max配置属性类 - 用于绑定application.yml中的配置
 * <p>
 * 配置前缀：mybatis.max
 * </p>
 * 
 * <p>
 * 配置示例：
 * <pre>
 * mybatis:
 *   max:
 *     worker-id: 1
 * </pre>
 * </p>
 * 
 * <p>
 * 配置说明：
 * <ul>
 *     <li>worker-id: 雪花算法的工作节点ID，用于分布式环境下避免ID冲突。
 *         取值范围：0-63，默认为0。在集群部署时，每个节点应配置不同的值。</li>
 * </ul>
 * </p>
 * 
 * @author talins
 * @see BeanConfiguration#idGenerator ID生成器配置
 */
@Data
@ConfigurationProperties("mybatis.max")
public class MybatisMaxProperties {

    /**
     * 雪花算法工作节点ID
     * <p>
     * 用于分布式环境下区分不同的服务节点，避免生成重复的ID。
     * 取值范围：0-63，默认为0。
     * </p>
     * 
     * <p>
     * 在集群部署时，建议为每个节点配置不同的workerId：
     * <ul>
     *     <li>节点1：mybatis.max.worker-id=1</li>
     *     <li>节点2：mybatis.max.worker-id=2</li>
     *     <li>...</li>
     * </ul>
     * </p>
     */
    private Short workerId;

}
