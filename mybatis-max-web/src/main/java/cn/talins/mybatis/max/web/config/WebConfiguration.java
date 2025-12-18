package cn.talins.mybatis.max.web.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Web配置类 - 配置Web层相关的Bean
 * <p>
 * 该类配置了JSON序列化相关的设置，确保API响应的JSON格式符合预期。
 * </p>
 * 
 * @author talins
 */
@Configuration
public class WebConfiguration {

    /**
     * 配置Jackson ObjectMapper
     * <p>
     * 配置内容：
     * <ul>
     *     <li>注册JavaTimeModule：支持Java 8日期时间类型的序列化</li>
     *     <li>设置NON_NULL：序列化时忽略null值字段</li>
     * </ul>
     * </p>
     * 
     * @return 配置好的ObjectMapper实例
     */
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        // 支持Java 8日期时间类型（LocalDateTime等）
        mapper.registerModule(new JavaTimeModule());
        // 序列化时忽略null值字段，减少响应体大小
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper;
    }
}
