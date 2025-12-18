package cn.talins.mybatis.max.starter;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.talins.mybatis.max.api.pojo.TableMetaData;
import cn.talins.mybatis.max.sdk.DynamicMapperUtil;
import lombok.SneakyThrows;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.lang.NonNull;

import javax.sql.DataSource;
import java.util.Map;

import static cn.talins.mybatis.max.sdk.common.Constant.TABLE_DATASOURCE_MAP;

/**
 * 动态Mapper Bean注册处理器 - 在Spring容器初始化时动态注册Mapper
 * <p>
 * 该类实现了{@link BeanDefinitionRegistryPostProcessor}接口，
 * 在Spring容器初始化的早期阶段执行，负责：
 * <ol>
 *     <li>从数据源读取所有表的元数据</li>
 *     <li>使用{@link DynamicMapperUtil}动态生成Entity类和Mapper接口</li>
 *     <li>将生成的Mapper注册为Spring Bean</li>
 *     <li>记录表与数据源的映射关系（用于多数据源场景）</li>
 * </ol>
 * </p>
 * 
 * <p>
 * 执行时机：
 * 该处理器在Spring容器创建Bean定义之后、实例化Bean之前执行，
 * 确保动态生成的Mapper可以被其他Bean依赖注入。
 * </p>
 * 
 * <p>
 * 生成的Mapper Bean命名规则：
 * <ul>
 *     <li>表名：user_info</li>
 *     <li>Mapper类名：UserInfoMapper</li>
 *     <li>Bean名称：userInfoMapper</li>
 * </ul>
 * </p>
 * 
 * @author talins
 * @see DynamicMapperUtil 动态Mapper生成工具
 * @see BeanConfiguration#dynamicsMapperBeanFactoryPostProcessor 注册位置
 */
public class DynamicMapperBeanFactoryPostProcessor implements BeanDefinitionRegistryPostProcessor {

    /**
     * 数据源，用于读取表结构信息
     */
    private final DataSource dataSource;

    /**
     * 构造函数
     * 
     * @param dataSource 数据源
     */
    public DynamicMapperBeanFactoryPostProcessor(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * 处理Bean定义注册
     * <p>
     * 在此方法中完成动态Mapper的生成和注册：
     * <ol>
     *     <li>获取数据库所有表的元数据</li>
     *     <li>为每个表生成Mapper类</li>
     *     <li>将Mapper注册为Spring Bean</li>
     * </ol>
     * </p>
     * 
     * @param registry Bean定义注册器
     * @throws BeansException Bean处理异常
     */
    @SneakyThrows
    @Override
    public void postProcessBeanDefinitionRegistry(@NonNull BeanDefinitionRegistry registry) throws BeansException {
        // 获取所有表的元数据
        Map<String, TableMetaData> tableMetaDataList = DynamicMapperUtil.getMetaDataMap(dataSource);
        if(CollUtil.isEmpty(tableMetaDataList)) {
            return;
        }
        
        // 为每个表生成并注册Mapper
        for(TableMetaData tableMetaData : tableMetaDataList.values()) {
            // 动态生成Mapper类
            Class<?> mapperClass = DynamicMapperUtil.generateMapperClass(tableMetaData);
            if(mapperClass != null) {
                // Bean名称：类名首字母小写
                String beanName = StrUtil.lowerFirst(mapperClass.getSimpleName());
                
                // 如果已存在同名Bean，跳过（允许用户自定义Mapper覆盖）
                if(registry.containsBeanDefinition(beanName)) {
                    continue;
                }
                
                // 创建MapperFactoryBean的Bean定义
                BeanDefinitionBuilder memberBuilder = BeanDefinitionBuilder.genericBeanDefinition();
                AbstractBeanDefinition memberBeanDefinition = memberBuilder.getRawBeanDefinition();
                memberBeanDefinition.setBeanClass(MapperFactoryBean.class);
                memberBeanDefinition.getConstructorArgumentValues().addGenericArgumentValue(mapperClass);
                memberBeanDefinition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
                
                // 注册Bean定义
                registry.registerBeanDefinition(beanName, memberBeanDefinition);
                
                // 记录表与数据源的映射关系（用于多数据源自动切换）
                TABLE_DATASOURCE_MAP.put(tableMetaData.getTableName(), tableMetaData.getDataSourceName());
            }
        }
    }

    /**
     * 处理Bean工厂
     * <p>
     * 此方法在postProcessBeanDefinitionRegistry之后调用，
     * 当前实现为空，不需要额外处理。
     * </p>
     * 
     * @param beanFactory Bean工厂
     * @throws BeansException Bean处理异常
     */
    @Override
    public void postProcessBeanFactory(@NonNull ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // 不需要额外处理
    }
}

