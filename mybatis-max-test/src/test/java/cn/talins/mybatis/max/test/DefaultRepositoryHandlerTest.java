package cn.talins.mybatis.max.test;

import cn.talins.mybatis.max.App;
import cn.talins.mybatis.max.api.enums.Booleans;
import cn.talins.mybatis.max.api.pojo.BaseEntity;
import cn.talins.mybatis.max.starter.DefaultRepositoryHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * DefaultRepositoryHandler 单元测试
 * 测试默认的Repository处理器功能
 *
 * @author talins
 */
@SpringBootTest(classes = App.class)
@DisplayName("DefaultRepositoryHandler测试")
public class DefaultRepositoryHandlerTest {

    @Autowired
    private DefaultRepositoryHandler repositoryHandler;

    private BaseEntity testEntity;

    @BeforeEach
    void setUp() {
        testEntity = new BaseEntity();
    }

    @Test
    @DisplayName("测试fillInsertEntity - 自动生成ID")
    void testFillInsertEntityAutoGenerateId() {
        assertNull(testEntity.getId(), "初始ID应为空");

        repositoryHandler.fillInsertEntity(testEntity);

        assertNotNull(testEntity.getId(), "ID应该被自动生成");
        assertTrue(testEntity.getId() > 0, "ID应该大于0");
    }

    @Test
    @DisplayName("测试fillInsertEntity - 保留已有ID")
    void testFillInsertEntityKeepExistingId() {
        Long existingId = 12345L;
        testEntity.setId(existingId);

        repositoryHandler.fillInsertEntity(testEntity);

        assertEquals(existingId, testEntity.getId(), "已有的ID应该被保留");
    }

    @Test
    @DisplayName("测试fillInsertEntity - 设置normal字段")
    void testFillInsertEntitySetNormal() {
        assertNull(testEntity.getNormal(), "初始normal应为空");

        repositoryHandler.fillInsertEntity(testEntity);

        assertEquals(Booleans.TRUE.getValue(), testEntity.getNormal(), "normal应该被设置为TRUE");
    }

    @Test
    @DisplayName("测试fillInsertEntity - 保留已有normal")
    void testFillInsertEntityKeepExistingNormal() {
        testEntity.setNormal(Booleans.FALSE.getValue());

        repositoryHandler.fillInsertEntity(testEntity);

        assertEquals(Booleans.FALSE.getValue(), testEntity.getNormal(), "已有的normal应该被保留");
    }

    @Test
    @DisplayName("测试fillInsertEntity - 设置version")
    void testFillInsertEntitySetVersion() {
        repositoryHandler.fillInsertEntity(testEntity);

        assertNotNull(testEntity.getVersion(), "version应该被设置");
        assertEquals(testEntity.getId(), testEntity.getVersion(), "version应该等于ID");
    }

    @Test
    @DisplayName("测试fillInsertEntity - 设置updateTime")
    void testFillInsertEntitySetUpdateTime() {
        repositoryHandler.fillInsertEntity(testEntity);

        assertNotNull(testEntity.getUpdateTime(), "updateTime应该被设置");
        assertFalse(testEntity.getUpdateTime() == null, "updateTime不应为空字符串");
    }

    @Test
    @DisplayName("测试fillUpdateEntity - 设置version")
    void testFillUpdateEntitySetVersion() {
        assertNull(testEntity.getVersion(), "初始version应为空");

        repositoryHandler.fillUpdateEntity(testEntity);

        assertNotNull(testEntity.getVersion(), "version应该被设置");
        assertTrue(testEntity.getVersion() > 0, "version应该大于0");
    }

    @Test
    @DisplayName("测试fillUpdateEntity - 保留已有version")
    void testFillUpdateEntityKeepExistingVersion() {
        Long existingVersion = 99999L;
        testEntity.setVersion(existingVersion);

        repositoryHandler.fillUpdateEntity(testEntity);

        assertEquals(existingVersion, testEntity.getVersion(), "已有的version应该被保留");
    }

    @Test
    @DisplayName("测试fillUpdateEntity - 设置updateTime")
    void testFillUpdateEntitySetUpdateTime() {
        repositoryHandler.fillUpdateEntity(testEntity);

        assertNotNull(testEntity.getUpdateTime(), "updateTime应该被设置");
        assertFalse(testEntity.getUpdateTime() == null, "updateTime不应为空字符串");
    }

    @Test
    @DisplayName("测试fillInsertEntity完整流程")
    void testFillInsertEntityComplete() {
        repositoryHandler.fillInsertEntity(testEntity);

        assertNotNull(testEntity.getId());
        assertNotNull(testEntity.getNormal());
        assertNotNull(testEntity.getVersion());
        assertNotNull(testEntity.getUpdateTime());

        assertEquals(Booleans.TRUE.getValue(), testEntity.getNormal());
        assertEquals(testEntity.getId(), testEntity.getVersion());
    }

    @Test
    @DisplayName("测试fillUpdateEntity完整流程")
    void testFillUpdateEntityComplete() {
        testEntity.setId(1L);

        repositoryHandler.fillUpdateEntity(testEntity);

        assertNotNull(testEntity.getVersion());
        assertNotNull(testEntity.getUpdateTime());
    }

    @Test
    @DisplayName("测试多次调用fillInsertEntity生成不同ID")
    void testMultipleFillInsertEntityGenerateDifferentIds() {
        BaseEntity entity1 = new BaseEntity();
        BaseEntity entity2 = new BaseEntity();

        repositoryHandler.fillInsertEntity(entity1);
        repositoryHandler.fillInsertEntity(entity2);

        assertNotEquals(entity1.getId(), entity2.getId(), "不同实体应该生成不同的ID");
    }
}
