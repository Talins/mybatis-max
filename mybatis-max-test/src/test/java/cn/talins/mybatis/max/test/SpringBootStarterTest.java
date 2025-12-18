package cn.talins.mybatis.max.test;

import cn.talins.mybatis.max.App;
import cn.talins.mybatis.max.api.IDataPermissionHandler;
import cn.talins.mybatis.max.api.IIdGenerator;
import cn.talins.mybatis.max.api.IRepositoryHandler;
import cn.talins.mybatis.max.sdk.repository.BaseRepository;
import cn.talins.mybatis.max.sdk.repository.DataPermissionRepository;
import cn.talins.mybatis.max.starter.DefaultRepositoryHandler;
import cn.talins.mybatis.max.starter.MybatisMaxProperties;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Spring Boot Starter 自动配置测试
 * 测试所有Bean是否正确注入
 *
 * @author talins
 */
@SpringBootTest(classes = App.class)
@DisplayName("Spring Boot Starter自动配置测试")
public class SpringBootStarterTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired(required = false)
    private DataSource dataSource;

    @Autowired(required = false)
    private SqlSessionFactory sqlSessionFactory;

    @Autowired(required = false)
    private MybatisPlusInterceptor mybatisPlusInterceptor;

    @Autowired(required = false)
    private BaseRepository baseRepository;

    @Autowired(required = false)
    private DataPermissionRepository dataPermissionRepository;

    @Autowired(required = false)
    private IRepositoryHandler repositoryHandler;

    @Autowired(required = false)
    private IDataPermissionHandler dataPermissionHandler;

    @Autowired(required = false)
    private IIdGenerator idGenerator;

    @Autowired(required = false)
    private MybatisMaxProperties mybatisMaxProperties;

    @Test
    @DisplayName("测试ApplicationContext加载")
    void testApplicationContextLoads() {
        assertNotNull(applicationContext, "ApplicationContext应该被正确加载");
    }

    @Test
    @DisplayName("测试DataSource注入")
    void testDataSourceInjection() {
        assertNotNull(dataSource, "DataSource应该被正确注入");
    }

    @Test
    @DisplayName("测试SqlSessionFactory注入")
    void testSqlSessionFactoryInjection() {
        assertNotNull(sqlSessionFactory, "SqlSessionFactory应该被正确注入");
    }

    @Test
    @DisplayName("测试MybatisPlusInterceptor注入")
    void testMybatisPlusInterceptorInjection() {
        assertNotNull(mybatisPlusInterceptor, "MybatisPlusInterceptor应该被正确注入");
    }

    @Test
    @DisplayName("测试BaseRepository注入")
    void testBaseRepositoryInjection() {
        assertNotNull(baseRepository, "BaseRepository应该被正确注入");
    }

    @Test
    @DisplayName("测试DataPermissionRepository注入")
    void testDataPermissionRepositoryInjection() {
        assertNotNull(dataPermissionRepository, "DataPermissionRepository应该被正确注入");
    }

    @Test
    @DisplayName("测试IRepositoryHandler注入")
    void testRepositoryHandlerInjection() {
        assertNotNull(repositoryHandler, "IRepositoryHandler应该被正确注入");
        assertTrue(repositoryHandler instanceof DefaultRepositoryHandler,
                "IRepositoryHandler应该是DefaultRepositoryHandler的实例");
    }

    @Test
    @DisplayName("测试IDataPermissionHandler注入")
    void testDataPermissionHandlerInjection() {
        assertNotNull(dataPermissionHandler, "IDataPermissionHandler应该被正确注入");
    }

    @Test
    @DisplayName("测试IIdGenerator注入")
    void testIdGeneratorInjection() {
        assertNotNull(idGenerator, "IIdGenerator应该被正确注入");
    }

    @Test
    @DisplayName("测试ID生成器功能")
    void testIdGeneratorFunction() {
        Long id1 = idGenerator.nextId();
        Long id2 = idGenerator.nextId();

        assertNotNull(id1, "生成的ID不应为空");
        assertNotNull(id2, "生成的ID不应为空");
        assertNotEquals(id1, id2, "连续生成的ID应该不同");
        assertTrue(id1 > 0, "ID应该大于0");
        assertTrue(id2 > 0, "ID应该大于0");
    }

    @Test
    @DisplayName("测试MybatisMaxProperties注入")
    void testMybatisMaxPropertiesInjection() {
        assertNotNull(mybatisMaxProperties, "MybatisMaxProperties应该被正确注入");
    }

    @Test
    @DisplayName("测试默认数据权限处理器行为")
    void testDefaultDataPermissionHandlerBehavior() {
        // 测试默认实现不修改数据
        String testEntity = "test";
        String result = dataPermissionHandler.addColumnPermission("test_table", testEntity);
        assertEquals(testEntity, result, "默认实现应该返回原始实体");
    }

    @Test
    @DisplayName("测试Bean依赖关系")
    void testBeanDependencies() {
        // 验证Bean之间的依赖关系正确
        assertNotNull(applicationContext.getBean(BaseRepository.class));
        assertNotNull(applicationContext.getBean(DataPermissionRepository.class));
        assertNotNull(applicationContext.getBean(IRepositoryHandler.class));
        assertNotNull(applicationContext.getBean(IIdGenerator.class));
    }
}
