# MyBatis-Max

<p align="center">
  <strong>🚀 基于 MyBatis-Plus 的零代码动态 ORM 框架</strong>
</p>

<p align="center">
  <a href="./README.md">English</a> |
  <a href="#快速开始">快速开始</a> |
  <a href="#核心特性">核心特性</a> |
  <a href="#架构设计">架构设计</a>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-11+-blue.svg" alt="Java 11+">
  <img src="https://img.shields.io/badge/Spring%20Boot-2.6.x-green.svg" alt="Spring Boot 2.6.x">
  <img src="https://img.shields.io/badge/MyBatis--Plus-3.5.x-orange.svg" alt="MyBatis-Plus 3.5.x">
  <img src="https://img.shields.io/badge/License-Apache%202.0-blue.svg" alt="License">
</p>

---

## 📖 项目简介

**MyBatis-Max** 是一个革命性的 ORM 框架，它彻底消除了编写 Entity 实体类和 Mapper 接口的需要。框架在应用启动时自动读取数据库表结构，使用 Groovy 动态生成所有必要的代码，并通过统一的 Repository 接口提供完整的 CRUD 操作。

### 为什么选择 MyBatis-Max？

| 传统开发方式 | MyBatis-Max |
|-------------|-------------|
| 为每个表创建 Entity 类 | ✅ 运行时自动生成 |
| 为每个表创建 Mapper 接口 | ✅ 运行时自动生成 |
| 编写 XML 或注解 SQL | ✅ 内置通用操作 |
| 表结构变更需修改代码 | ✅ 重启自动适配 |
| 重复的 CRUD 样板代码 | ✅ 一个接口操作所有表 |

## ✨ 核心特性

- **🔄 动态代码生成** - 根据数据库表结构自动生成 Entity 和 Mapper 类
- **📦 零配置启动** - 只需添加依赖并配置数据源即可使用
- **🎯 统一仓库接口** - 单一接口完成所有表的数据操作
- **📄 内置分页支持** - 原生支持分页查询
- **🔐 数据权限控制** - 支持行级和列级权限控制
- **💾 二级缓存** - 集成 J2Cache 实现高性能缓存
- **🔀 多数据源** - 动态数据源切换，自动路由
- **📡 事件系统** - 操作前后事件，便于业务扩展
- **🌐 REST API** - 开箱即用的 RESTful 接口
- **❄️ 雪花 ID** - 分布式唯一 ID 生成

## 🏗️ 架构设计

### 项目结构

```
mybatis-max
├── mybatis-max-api          # 核心接口和 POJO 定义
├── mybatis-max-sdk          # 核心实现（Repository、缓存、事件）
├── mybatis-max-spring-boot-starter  # Spring Boot 自动配置
├── mybatis-max-web          # REST API 控制器
└── mybatis-max-test         # 测试模块和示例
```

### 核心组件

```
┌─────────────────────────────────────────────────────────────────┐
│                          应用层                                  │
├─────────────────────────────────────────────────────────────────┤
│  BaseRest (REST API)  │  IRepositoryService (业务服务层)        │
├─────────────────────────────────────────────────────────────────┤
│              IRepository / DataPermissionRepository              │
├─────────────────────────────────────────────────────────────────┤
│  DynamicMapperUtil    │  IRepositoryHandler  │  CacheUtil       │
├─────────────────────────────────────────────────────────────────┤
│              MyBatis-Plus BaseMapper (自动生成)                  │
├─────────────────────────────────────────────────────────────────┤
│                    DataSource (单数据源/动态数据源)               │
└─────────────────────────────────────────────────────────────────┘
```

### 运行流程

```
应用启动
    │
    ▼
┌─────────────────────────────────────┐
│ DynamicMapperBeanFactoryPostProcessor│
│   1. 连接数据库                      │
│   2. 读取所有表的元数据               │
│   3. 使用 Groovy 动态生成 Entity 类   │
│   4. 使用 Groovy 动态生成 Mapper 接口 │
│   5. 注册 Mapper 为 Spring Bean      │
└─────────────────────────────────────┘
    │
    ▼
┌─────────────────────────────────────┐
│         BeanConfiguration           │
│   注册 Repository、Handler 等 Bean   │
└─────────────────────────────────────┘
    │
    ▼
应用就绪，可通过 Repository 或 REST API 操作任意表
```

## 🚀 快速开始

### 环境要求

- Java 11+
- Spring Boot 2.6.x
- MySQL 5.7+ / 其他 JDBC 兼容数据库

### Maven 依赖

```xml
<dependency>
    <groupId>cn.talins</groupId>
    <artifactId>mybatis-max-spring-boot-starter</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>

<!-- 可选：REST API 支持 -->
<dependency>
    <groupId>cn.talins</groupId>
    <artifactId>mybatis-max-web</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

### 配置文件

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/your_database?useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC
    username: root
    password: your_password
    driver-class-name: com.mysql.cj.jdbc.Driver

# 可选：分布式部署时配置雪花算法工作节点 ID
mybatis:
  max:
    worker-id: 1
```

### 数据库表设计规范

为了充分利用框架功能，建议表包含以下基础字段：

```sql
CREATE TABLE your_table (
    id BIGINT(19) PRIMARY KEY COMMENT '主键ID（自动生成雪花ID）',
    normal TINYINT(3) DEFAULT 1 COMMENT '逻辑删除标识：1-正常，0-已删除',
    version BIGINT(19) COMMENT '乐观锁版本号',
    update_time DATETIME COMMENT '最后更新时间',
    extra TEXT COMMENT '扩展字段（JSON格式）',
    -- 其他业务字段...
);
```

## 📚 使用指南

### 方式一：使用 REST API（零代码）

应用启动后，所有表自动暴露 REST 接口：

```bash
# 插入记录
POST /mybatis-max/insert/user
{
    "param": {"name": "张三", "age": 25, "email": "zhangsan@example.com"}
}

# 根据 ID 查询
POST /mybatis-max/selectById/user
{
    "param": 1
}

# 条件查询
POST /mybatis-max/selectList/user
{
    "param": {
        "conditionList": [
            {"column": "age", "operator": "GREAT_EQUAL", "paramList": [18]}
        ],
        "orderMap": {"createTime": "DESC"}
    }
}

# 分页查询
POST /mybatis-max/selectPage/user
{
    "pageNum": 1,
    "pageSize": 10,
    "param": {
        "conditionList": [
            {"column": "status", "operator": "EQUAL", "paramList": [1]}
        ]
    }
}

# 根据 ID 更新
POST /mybatis-max/updateById/user
{
    "param": {"id": 1, "name": "李四", "age": 26}
}

# 根据 ID 删除
POST /mybatis-max/deleteById/user
{
    "param": 1
}

# 批量删除
POST /mybatis-max/deleteBatchIds/user
{
    "param": [1, 2, 3]
}

# 判断是否存在
POST /mybatis-max/exists/user
{
    "param": {
        "conditionList": [
            {"column": "email", "operator": "EQUAL", "paramList": ["test@example.com"]}
        ]
    }
}

# 统计数量
POST /mybatis-max/selectCount/user
{
    "param": {
        "conditionList": [
            {"column": "status", "operator": "EQUAL", "paramList": [1]}
        ]
    }
}
```

### 方式二：在代码中使用 Repository

```java
@Service
public class UserService {
    
    @Autowired
    private BaseRepository baseRepository;
    
    // 插入
    public Long createUser(Map<String, Object> user) {
        return baseRepository.insert("user", user);
    }
    
    // 根据 ID 查询
    public User getUser(Long id) {
        return baseRepository.selectById("user", id, User.class);
    }
    
    // 条件查询
    public List<User> findActiveUsers() {
        QueryWrapper<User> wrapper = Wrappers.query(User.class);
        wrapper.eq("status", 1)
               .orderByDesc("create_time");
        return baseRepository.selectList("user", wrapper);
    }
    
    // 分页查询
    public IPage<User> pageUsers(int pageNum, int pageSize) {
        Page<User> page = new Page<>(pageNum, pageSize);
        QueryWrapper<User> wrapper = Wrappers.query(User.class);
        return baseRepository.selectPage("user", page, wrapper);
    }
    
    // 更新
    public int updateUser(Map<String, Object> user) {
        return baseRepository.updateById("user", user);
    }
    
    // 删除
    public int deleteUser(Long id) {
        return baseRepository.deleteById("user", id);
    }
    
    // 根据 Map 条件查询
    public List<User> findByCondition(Map<String, Object> condition) {
        return baseRepository.selectByMap("user", condition, User.class);
    }
}
```

### 方式三：继承服务抽象类

```java
@Service
public class UserService extends AbstractBaseRepositoryService {
    
    public UserService(BaseRepository baseRepository) {
        super(baseRepository);
    }
    
    @Override
    protected String getTableName() {
        return "user";
    }
    
    // 现在可以直接使用继承的方法，无需指定表名
    // insert(), selectById(), updateById(), deleteById() 等
    
    // 自定义业务方法
    public User findByUsername(String username) {
        return selectOneByMap(Map.of("username", username), User.class);
    }
}
```

## 🔧 高级功能

### 查询操作符

| 操作符 | 说明 | 示例 |
|--------|------|------|
| `EQUAL` | 等于 (=) | `{"column": "status", "operator": "EQUAL", "paramList": [1]}` |
| `NOT_EQUAL` | 不等于 (!=) | `{"column": "status", "operator": "NOT_EQUAL", "paramList": [0]}` |
| `LIKE` | 模糊匹配 | `{"column": "name", "operator": "LIKE", "paramList": ["%张%"]}` |
| `NOT_LIKE` | 不匹配 | `{"column": "name", "operator": "NOT_LIKE", "paramList": ["%test%"]}` |
| `IN` | 包含 | `{"column": "id", "operator": "IN", "paramList": [1, 2, 3]}` |
| `NOT_IN` | 不包含 | `{"column": "status", "operator": "NOT_IN", "paramList": [0, -1]}` |
| `BETWEEN` | 范围 | `{"column": "age", "operator": "BETWEEN", "paramList": [18, 30]}` |
| `NOT_BETWEEN` | 范围外 | `{"column": "age", "operator": "NOT_BETWEEN", "paramList": [0, 18]}` |
| `GREAT` | 大于 (>) | `{"column": "age", "operator": "GREAT", "paramList": [18]}` |
| `LESS` | 小于 (<) | `{"column": "age", "operator": "LESS", "paramList": [60]}` |
| `GREAT_EQUAL` | 大于等于 (>=) | `{"column": "age", "operator": "GREAT_EQUAL", "paramList": [18]}` |
| `LESS_EQUAL` | 小于等于 (<=) | `{"column": "age", "operator": "LESS_EQUAL", "paramList": [60]}` |
| `IS_NULL` | 为空 | `{"column": "email", "operator": "IS_NULL", "paramList": []}` |
| `IS_NOT_NULL` | 不为空 | `{"column": "email", "operator": "IS_NOT_NULL", "paramList": []}` |

### 条件连接

多个条件默认使用 `AND` 连接，可通过 `connect` 字段指定 `OR` 连接：

```json
{
    "param": {
        "conditionList": [
            {"column": "status", "operator": "EQUAL", "paramList": [1]},
            {"column": "type", "operator": "EQUAL", "paramList": [2], "connect": "OR"}
        ]
    }
}
```

### 多数据源配置

```yaml
spring:
  datasource:
    master:
      url: jdbc:mysql://localhost:3306/master_db
      username: root
      password: password
      driver-class-name: com.mysql.cj.jdbc.Driver
    slave:
      url: jdbc:mysql://localhost:3306/slave_db
      username: root
      password: password
      driver-class-name: com.mysql.cj.jdbc.Driver
```

框架会自动根据表所在的数据源进行路由，无需手动切换。

### 数据权限控制

实现 `IDataPermissionHandler` 接口添加行级/列级权限：

```java
@Bean
public IDataPermissionHandler dataPermissionHandler() {
    return new IDataPermissionHandler() {
        @Override
        public <T> void addRowPermission(String tableName, QueryWrapper<T> wrapper) {
            // 添加租户隔离条件
            wrapper.eq("tenant_id", getCurrentTenantId());
            // 添加部门数据范围
            wrapper.in("dept_id", getCurrentUserDeptIds());
        }
        
        @Override
        public <T> T addColumnPermission(String tableName, T entity) {
            // 敏感字段脱敏
            if (entity instanceof User) {
                User user = (User) entity;
                user.setPhone(maskPhone(user.getPhone()));
                user.setIdCard(maskIdCard(user.getIdCard()));
            }
            return entity;
        }
        
        @Override
        public <T> List<T> addColumnPermission(String tableName, List<T> list) {
            return list.stream()
                .map(e -> addColumnPermission(tableName, e))
                .collect(Collectors.toList());
        }
    };
}
```

### 事件监听

监听实体操作事件进行业务扩展：

```java
@Component
public class EntityEventListener {
    
    @EventListener
    public void onInsert(EntityInsertEvent event) {
        if (event.getIsBefore() == 1) {
            // 插入前
            log.info("准备插入到 {}: {}", event.getTableName(), event.getEntity());
        } else {
            // 插入后
            log.info("已插入到 {}: {}", event.getTableName(), event.getEntity());
            // 可以在这里发送消息、记录日志等
        }
    }
    
    @EventListener
    public void onUpdate(EntityUpdateByIdEvent event) {
        // 处理更新事件
    }
    
    @EventListener
    public void onDelete(EntityDeleteEvent event) {
        // 处理删除事件
    }
    
    @EventListener
    public void onDeleteBatch(EntityDeleteBatchEvent event) {
        // 处理批量删除事件
    }
}
```

### 自定义 ID 生成器

```java
@Bean
public IIdGenerator customIdGenerator() {
    return () -> {
        // 自定义 ID 生成逻辑
        return UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
    };
}
```

### 自定义仓库处理器

```java
@Bean
public IRepositoryHandler customRepositoryHandler(IIdGenerator idGenerator) {
    return new DefaultRepositoryHandler(idGenerator) {
        @Override
        public void fillInsertEntity(BaseEntity entity) {
            super.fillInsertEntity(entity);
            // 添加自定义字段填充逻辑
            // 例如：设置创建人、创建时间等
        }
    };
}
```

### 缓存配置

框架集成了 J2Cache 二级缓存，需要配置 `j2cache.properties`：

```properties
# 一级缓存
j2cache.L1.provider_class=caffeine
caffeine.region.default=1000,30m

# 二级缓存
j2cache.L2.provider_class=redis
j2cache.L2.config_section=redis
redis.hosts=localhost:6379
redis.password=
redis.database=0
```

## 🔨 本地构建

```bash
# 克隆仓库
git clone https://github.com/talins/mybatis-max.git
cd mybatis-max

# Maven 构建
mvn clean install -DskipTests

# 运行测试
mvn test
```

## 📋 模块说明

| 模块 | 说明 |
|------|------|
| `mybatis-max-api` | 核心接口（`IRepository`、`IRepositoryService`）和 POJO（`BaseEntity`、`Query`、`Condition`） |
| `mybatis-max-sdk` | 核心实现，包括 `BaseRepository`、`DynamicMapperUtil`、缓存工具和事件系统 |
| `mybatis-max-spring-boot-starter` | Spring Boot 自动配置、动态 Mapper 注册和默认 Bean 配置 |
| `mybatis-max-web` | REST 控制器（`BaseRest`），提供所有数据库操作的 HTTP 接口 |
| `mybatis-max-test` | 测试用例和使用示例 |

## 🔍 API 参考

### IRepository 接口

```java
public interface IRepository {
    // 插入
    <T> Long insert(String tableName, T entity);
    
    // 删除
    int deleteById(String tableName, Long id);
    int deleteByMap(String tableName, Map<String, Object> columnMap);
    <T> int delete(String tableName, QueryWrapper<T> queryWrapper);
    int deleteBatchIds(String tableName, Collection<Long> idList);
    
    // 更新
    <T> int updateById(String tableName, T entity);
    <T> int update(String tableName, T entity, QueryWrapper<T> updateWrapper);
    
    // 查询
    <T> T selectById(String tableName, Long id, Class<T> clazz);
    <T> List<T> selectBatchIds(String tableName, Collection<Long> idList, Class<T> clazz);
    <T> List<T> selectByMap(String tableName, Map<String, Object> columnMap, Class<T> clazz);
    <T> T selectOne(String tableName, QueryWrapper<T> queryWrapper);
    <T> List<T> selectList(String tableName, QueryWrapper<T> queryWrapper);
    <T> List<T> selectList(String tableName, IPage<T> page, QueryWrapper<T> queryWrapper);
    <T, P extends IPage<T>> P selectPage(String tableName, P page, QueryWrapper<T> queryWrapper);
    
    // 统计
    <T> Long selectCount(String tableName, QueryWrapper<T> queryWrapper);
    <T> boolean exists(String tableName, QueryWrapper<T> queryWrapper);
}
```

### REST API 端点

| 端点 | 方法 | 说明 |
|------|------|------|
| `/mybatis-max/insert/{tableName}` | POST | 插入记录 |
| `/mybatis-max/deleteById/{tableName}` | POST | 根据 ID 删除 |
| `/mybatis-max/deleteByMap/{tableName}` | POST | 根据条件删除 |
| `/mybatis-max/delete/{tableName}` | POST | 根据 QueryWrapper 删除 |
| `/mybatis-max/deleteBatchIds/{tableName}` | POST | 批量删除 |
| `/mybatis-max/updateById/{tableName}` | POST | 根据 ID 更新 |
| `/mybatis-max/update/{tableName}` | POST | 根据条件更新 |
| `/mybatis-max/selectById/{tableName}` | POST | 根据 ID 查询 |
| `/mybatis-max/selectBatchIds/{tableName}` | POST | 批量 ID 查询 |
| `/mybatis-max/selectByMap/{tableName}` | POST | 根据 Map 条件查询 |
| `/mybatis-max/selectOneByMap/{tableName}` | POST | 根据 Map 条件查询单条 |
| `/mybatis-max/selectOne/{tableName}` | POST | 查询单条记录 |
| `/mybatis-max/selectList/{tableName}` | POST | 查询列表 |
| `/mybatis-max/selectPage/{tableName}` | POST | 分页查询 |
| `/mybatis-max/selectCount/{tableName}` | POST | 统计数量 |
| `/mybatis-max/selectCountByMap/{tableName}` | POST | 根据 Map 统计数量 |
| `/mybatis-max/exists/{tableName}` | POST | 判断是否存在 |

## 🤝 参与贡献

欢迎贡献代码！请查看 [贡献指南](CONTRIBUTING.md) 了解详情。

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/amazing-feature`)
3. 提交更改 (`git commit -m 'Add amazing feature'`)
4. 推送到分支 (`git push origin feature/amazing-feature`)
5. 提交 Pull Request

## 📄 开源协议

本项目采用 Apache License 2.0 协议 - 详见 [LICENSE](LICENSE) 文件。

## 🙏 致谢

- [MyBatis-Plus](https://github.com/baomidou/mybatis-plus) - 本项目基于的强大 ORM 框架
- [Hutool](https://github.com/dromara/hutool) - 全面的 Java 工具库
- [J2Cache](https://gitee.com/ld/J2Cache) - 二级缓存框架
- [Yitter IdGenerator](https://github.com/yitter/IdGenerator) - 雪花 ID 生成器

---

<p align="center">
  Made with ❤️ by <a href="https://github.com/talins">Talins</a>
</p>
