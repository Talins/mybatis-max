package cn.talins.mybatis.max.starter;

import cn.hutool.core.util.StrUtil;
import cn.talins.mybatis.max.api.IIdGenerator;
import cn.talins.mybatis.max.api.IDataPermissionHandler;
import cn.talins.mybatis.max.api.IRepositoryHandler;
import cn.talins.mybatis.max.sdk.DynamicDataSource;
import cn.talins.mybatis.max.sdk.repository.BaseRepository;
import cn.talins.mybatis.max.sdk.repository.DataPermissionRepository;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.github.yitter.contract.IdGeneratorOptions;
import com.github.yitter.idgen.YitIdHelper;
import com.zaxxer.hikari.HikariDataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.*;
import org.springframework.web.context.support.StandardServletEnvironment;

import javax.sql.DataSource;
import java.util.*;

/**
 * MyBatis-Max自动配置类 - Spring Boot Starter的核心配置
 * <p>
 * 该类通过Spring Boot的自动配置机制，在应用启动时自动注册以下Bean：
 * <ul>
 *     <li>DataSource: 数据源（支持单数据源和多数据源）</li>
 *     <li>SqlSessionFactory: MyBatis会话工厂</li>
 *     <li>MybatisPlusInterceptor: MyBatis-Plus拦截器（分页等）</li>
 *     <li>BaseRepository: 基础数据仓库</li>
 *     <li>DataPermissionRepository: 数据权限仓库</li>
 *     <li>IRepositoryHandler: 仓库处理器</li>
 *     <li>IIdGenerator: ID生成器</li>
 *     <li>IDataPermissionHandler: 数据权限处理器</li>
 *     <li>DynamicMapperBeanFactoryPostProcessor: 动态Mapper注册处理器</li>
 * </ul>
 * </p>
 * 
 * <p>
 * 所有Bean都使用{@code @ConditionalOnMissingBean}注解，
 * 允许用户自定义实现覆盖默认配置。
 * </p>
 * 
 * <p>
 * 数据源配置支持两种模式：
 * <ul>
 *     <li>单数据源：spring.datasource.url/username/password</li>
 *     <li>多数据源：spring.datasource.{name}.url/username/password</li>
 * </ul>
 * </p>
 * 
 * @author talins
 * @see DynamicMapperBeanFactoryPostProcessor 动态Mapper注册
 * @see DefaultRepositoryHandler 默认仓库处理器
 */
@Configuration
@Import({MybatisMaxProperties.class})
public class BeanConfiguration {

    /**
     * 注册动态Mapper处理器
     * <p>
     * 该处理器在Spring容器初始化时执行，负责：
     * <ol>
     *     <li>读取数据库表结构</li>
     *     <li>动态生成Entity类和Mapper接口</li>
     *     <li>将Mapper注册为Spring Bean</li>
     * </ol>
     * </p>
     * 
     * @param dataSource 数据源
     * @return 动态Mapper处理器实例
     */
    @Bean
    @ConditionalOnMissingBean
    public DynamicMapperBeanFactoryPostProcessor dynamicsMapperBeanFactoryPostProcessor(DataSource dataSource) {
        return new DynamicMapperBeanFactoryPostProcessor(dataSource);
    }

    /**
     * 注册MyBatis SqlSessionFactory
     * <p>
     * 使用MyBatis-Plus的MybatisSqlSessionFactoryBean创建，
     * 自动配置分页等拦截器。
     * </p>
     * 
     * @param dataSource 数据源
     * @return SqlSessionFactory实例
     * @throws Exception 创建异常
     */
    @Bean
    @ConditionalOnMissingBean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        MybatisSqlSessionFactoryBean factoryBean = new MybatisSqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setPlugins(mybatisPlusInterceptor());
        return factoryBean.getObject();
    }

    /**
     * 注册MyBatis-Plus拦截器
     * <p>
     * 默认配置分页拦截器，支持物理分页。
     * </p>
     * 
     * @return MyBatis-Plus拦截器实例
     */
    @Bean
    @ConditionalOnMissingBean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return interceptor;
    }

    /**
     * 注册基础数据仓库
     * 
     * @param mapperHandler 仓库处理器
     * @return 基础仓库实例
     */
    @Bean
    @ConditionalOnMissingBean
    public BaseRepository baseRepository(IRepositoryHandler mapperHandler) {
        return new BaseRepository(mapperHandler);
    }

    /**
     * 注册数据权限仓库
     * 
     * @param mapperHandler 仓库处理器
     * @param permissionHandler 数据权限处理器
     * @return 数据权限仓库实例
     */
    @Bean
    @ConditionalOnMissingBean
    public DataPermissionRepository dataPermissionRepository(IRepositoryHandler mapperHandler,
                                                             IDataPermissionHandler permissionHandler) {
        return new DataPermissionRepository(mapperHandler, permissionHandler);
    }

    /**
     * 注册默认的数据权限处理器
     * <p>
     * 默认实现不进行任何权限控制，用户可以自定义实现覆盖此Bean。
     * </p>
     * 
     * @return 数据权限处理器实例
     */
    @Bean
    @ConditionalOnMissingBean
    public IDataPermissionHandler permissionHandler() {
        return new IDataPermissionHandler() {
            @Override
            public <T> void addRowPermission(String tableName, QueryWrapper<T> queryWrapper) {
                // 默认不添加行级权限
            }

            @Override
            public <T> T addColumnPermission(String tableName, T entity) {
                // 默认不进行列级权限处理
                return entity;
            }

            @Override
            public <T> List<T> addColumnPermission(String tableName, List<T> entityList) {
                // 默认不进行列级权限处理
                return entityList;
            }
        };
    }

    /**
     * 注册默认的仓库处理器
     * 
     * @param idGenerator ID生成器
     * @return 仓库处理器实例
     */
    @Bean
    @ConditionalOnMissingBean
    public DefaultRepositoryHandler mapperHandler(IIdGenerator idGenerator) {
        return new DefaultRepositoryHandler(idGenerator);
    }

    /**
     * 注册ID生成器
     * <p>
     * 使用Yitter IdGenerator（雪花算法变种），支持通过配置指定workerId。
     * </p>
     * 
     * @param properties 配置属性
     * @return ID生成器实例
     */
    @Bean
    @ConditionalOnMissingBean
    public IIdGenerator idGenerator(MybatisMaxProperties properties) {
        YitIdHelper.setIdGenerator(new IdGeneratorOptions(properties.getWorkerId() == null ? 0 : properties.getWorkerId()));
        return YitIdHelper::nextId;
    }

    /**
     * 注册数据源
     * <p>
     * 支持两种配置模式：
     * <ul>
     *     <li>单数据源模式：配置spring.datasource.url时使用</li>
     *     <li>多数据源模式：配置spring.datasource.{name}.url时使用</li>
     * </ul>
     * </p>
     * 
     * <p>
     * 单数据源配置示例：
     * <pre>
     * spring:
     *   datasource:
     *     url: jdbc:mysql://localhost:3306/test
     *     username: root
     *     password: root
     *     driver-class-name: com.mysql.cj.jdbc.Driver
     * </pre>
     * </p>
     * 
     * <p>
     * 多数据源配置示例：
     * <pre>
     * spring:
     *   datasource:
     *     master:
     *       url: jdbc:mysql://localhost:3306/master
     *       username: root
     *       password: root
     *       driver-class-name: com.mysql.cj.jdbc.Driver
     *     slave:
     *       url: jdbc:mysql://localhost:3306/slave
     *       username: root
     *       password: root
     *       driver-class-name: com.mysql.cj.jdbc.Driver
     * </pre>
     * </p>
     * 
     * @param environment Spring环境对象，用于读取配置
     * @return 数据源实例（单数据源返回HikariDataSource，多数据源返回DynamicDataSource）
     */
    @Bean
    @ConditionalOnMissingBean
    public DataSource dataSource(StandardServletEnvironment environment) {
        // 单数据源模式：直接配置spring.datasource.url
        if(StrUtil.isNotBlank(environment.getProperty("spring.datasource.url"))) {
            HikariDataSource dataSource = new HikariDataSource();
            dataSource.setDriverClassName(environment.getProperty("spring.datasource.driver-class-name"));
            dataSource.setUsername(environment.getProperty("spring.datasource.username"));
            dataSource.setPassword(environment.getProperty("spring.datasource.password"));
            dataSource.setJdbcUrl(environment.getProperty("spring.datasource.url"));
            return dataSource;
        }
        
        // 多数据源模式：配置spring.datasource.{name}.url
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        Set<String> dataSourceNameSet = new LinkedHashSet<>();
        
        // 从配置中提取所有数据源名称
        environment.getPropertySources().stream().forEach(propertySource -> {
            if(!(propertySource.getSource() instanceof Map)) {
                return;
            }
            ((Map<?, ?>)propertySource.getSource()).forEach((key, value) -> {
                String keyStr = StrUtil.toString(key);
                if(keyStr.startsWith("spring.datasource")) {
                    // 提取数据源名称（spring.datasource.{name}.xxx中的name）
                    dataSourceNameSet.add(keyStr.split("\\.")[2]);
                }
            });
        });

        if(dataSourceNameSet.isEmpty()) {
            return null;
        }
        
        // 为每个数据源名称创建HikariDataSource
        Map<Object, Object> targetDataSources = new HashMap<>();
        dataSourceNameSet.forEach(dataSourceName -> {
            HikariDataSource dataSource = new HikariDataSource();
            dataSource.setDriverClassName(environment.getProperty("spring.datasource." + dataSourceName + ".driver-class-name"));
            dataSource.setUsername(environment.getProperty("spring.datasource." + dataSourceName + ".username"));
            dataSource.setPassword(environment.getProperty("spring.datasource." + dataSourceName + ".password"));
            dataSource.setJdbcUrl(environment.getProperty("spring.datasource." + dataSourceName + ".url"));
            targetDataSources.put(dataSourceName, dataSource);
            dynamicDataSource.putDataSourceMap(dataSourceName, dataSource);
        });

        // 设置第一个数据源为默认数据源
        dynamicDataSource.setDefaultTargetDataSource(targetDataSources.get(new ArrayList<>(dataSourceNameSet).get(0)));
        dynamicDataSource.setTargetDataSources(targetDataSources);
        return dynamicDataSource;
    }

}
