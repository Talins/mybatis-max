package cn.talins.mybatis.max.test;

import cn.talins.mybatis.max.api.enums.Booleans;
import cn.talins.mybatis.max.api.pojo.BaseEntity;
import cn.talins.mybatis.max.sdk.event.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 事件类测试
 * 测试实体操作事件的创建和属性
 *
 * @author talins
 */
@DisplayName("事件类测试")
public class EventTest {

    @Nested
    @DisplayName("EntityInsertEvent测试")
    class EntityInsertEventTest {

        @Test
        @DisplayName("测试创建插入前事件")
        void testCreateBeforeInsertEvent() {
            BaseEntity entity = new BaseEntity();
            entity.setId(1L);

            EntityInsertEvent event = new EntityInsertEvent(
                    "sys_user",
                    entity,
                    Booleans.TRUE.getValue()
            );

            assertEquals("sys_user", event.getTableName());
            assertEquals(entity, event.getEntity());
            assertEquals(Booleans.TRUE.getValue(), event.getIsBefore());
        }

        @Test
        @DisplayName("测试创建插入后事件")
        void testCreateAfterInsertEvent() {
            BaseEntity entity = new BaseEntity();
            entity.setId(1L);

            EntityInsertEvent event = new EntityInsertEvent(
                    "sys_user",
                    entity,
                    Booleans.FALSE.getValue()
            );

            assertEquals(Booleans.FALSE.getValue(), event.getIsBefore());
        }

        @Test
        @DisplayName("测试事件序列化")
        void testEventSerializable() {
            EntityInsertEvent event = new EntityInsertEvent("test", new BaseEntity(), 1);
            assertTrue(event instanceof java.io.Serializable);
        }
    }

    @Nested
    @DisplayName("EntityDeleteEvent测试")
    class EntityDeleteEventTest {

        @Test
        @DisplayName("测试创建删除前事件")
        void testCreateBeforeDeleteEvent() {
            QueryWrapper<Object> queryWrapper = Wrappers.query();
            queryWrapper.eq("id", 1L);

            EntityDeleteEvent<Object> event = new EntityDeleteEvent<>(
                    "sys_user",
                    queryWrapper,
                    Booleans.TRUE.getValue()
            );

            assertEquals("sys_user", event.getTableName());
            assertNotNull(event.getQueryWrapper());
            assertEquals(Booleans.TRUE.getValue(), event.getIsBefore());
        }

        @Test
        @DisplayName("测试创建删除后事件")
        void testCreateAfterDeleteEvent() {
            QueryWrapper<Object> queryWrapper = Wrappers.query();

            EntityDeleteEvent<Object> event = new EntityDeleteEvent<>(
                    "sys_user",
                    queryWrapper,
                    Booleans.FALSE.getValue()
            );

            assertEquals(Booleans.FALSE.getValue(), event.getIsBefore());
        }
    }

    @Nested
    @DisplayName("EntityDeleteBatchEvent测试")
    class EntityDeleteBatchEventTest {

        @Test
        @DisplayName("测试创建批量删除前事件")
        void testCreateBeforeDeleteBatchEvent() {
            Collection<Long> idList = Arrays.asList(1L, 2L, 3L);

            EntityDeleteBatchEvent event = new EntityDeleteBatchEvent(
                    "sys_user",
                    idList,
                    Booleans.TRUE.getValue()
            );

            assertEquals("sys_user", event.getTableName());
            assertEquals(3, event.getIdList().size());
            assertEquals(Booleans.TRUE.getValue(), event.getIsBefore());
        }

        @Test
        @DisplayName("测试创建批量删除后事件")
        void testCreateAfterDeleteBatchEvent() {
            Collection<Long> idList = Arrays.asList(1L, 2L);

            EntityDeleteBatchEvent event = new EntityDeleteBatchEvent(
                    "sys_user",
                    idList,
                    Booleans.FALSE.getValue()
            );

            assertEquals(Booleans.FALSE.getValue(), event.getIsBefore());
        }
    }

    @Nested
    @DisplayName("EntityUpdateByIdEvent测试")
    class EntityUpdateByIdEventTest {

        @Test
        @DisplayName("测试创建ID更新前事件")
        void testCreateBeforeUpdateByIdEvent() {
            BaseEntity entity = new BaseEntity();
            entity.setId(1L);

            EntityUpdateByIdEvent<BaseEntity> event = new EntityUpdateByIdEvent<>(
                    "sys_user",
                    entity,
                    Booleans.TRUE.getValue()
            );

            assertEquals("sys_user", event.getTableName());
            assertEquals(entity, event.getEntity());
            assertEquals(Booleans.TRUE.getValue(), event.getIsBefore());
        }

        @Test
        @DisplayName("测试创建ID更新后事件")
        void testCreateAfterUpdateByIdEvent() {
            BaseEntity entity = new BaseEntity();
            entity.setId(1L);

            EntityUpdateByIdEvent<BaseEntity> event = new EntityUpdateByIdEvent<>(
                    "sys_user",
                    entity,
                    Booleans.FALSE.getValue()
            );

            assertEquals(Booleans.FALSE.getValue(), event.getIsBefore());
        }
    }

    @Nested
    @DisplayName("EntityUpdateEvent测试")
    class EntityUpdateEventTest {

        @Test
        @DisplayName("测试创建条件更新前事件")
        void testCreateBeforeUpdateEvent() {
            BaseEntity entity = new BaseEntity();
            entity.setId(1L);
            QueryWrapper<BaseEntity> updateWrapper = Wrappers.query();
            updateWrapper.eq("id", 1L);

            EntityUpdateEvent<BaseEntity> event = new EntityUpdateEvent<>(
                    "sys_user",
                    entity,
                    updateWrapper,
                    Booleans.TRUE.getValue()
            );

            assertEquals("sys_user", event.getTableName());
            assertEquals(entity, event.getEntity());
            assertNotNull(event.getUpdateWrapper());
            assertEquals(Booleans.TRUE.getValue(), event.getIsBefore());
        }

        @Test
        @DisplayName("测试创建条件更新后事件")
        void testCreateAfterUpdateEvent() {
            BaseEntity entity = new BaseEntity();
            QueryWrapper<BaseEntity> updateWrapper = Wrappers.query();

            EntityUpdateEvent<BaseEntity> event = new EntityUpdateEvent<>(
                    "sys_user",
                    entity,
                    updateWrapper,
                    Booleans.FALSE.getValue()
            );

            assertEquals(Booleans.FALSE.getValue(), event.getIsBefore());
        }

        @Test
        @DisplayName("测试事件序列化")
        void testEventSerializable() {
            EntityUpdateEvent<BaseEntity> event = new EntityUpdateEvent<>(
                    "test",
                    new BaseEntity(),
                    Wrappers.query(),
                    1
            );
            assertTrue(event instanceof java.io.Serializable);
        }
    }
}
