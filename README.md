# MyBatis-Max

<p align="center">
  <strong>🚀 A Zero-Code Dynamic ORM Framework Based on MyBatis-Plus</strong>
</p>

<p align="center">
  <a href="./README_CN.md">中文文档</a> |
  <a href="#quick-start">Quick Start</a> |
  <a href="#features">Features</a> |
  <a href="#architecture">Architecture</a>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-11+-blue.svg" alt="Java 11+">
  <img src="https://img.shields.io/badge/Spring%20Boot-2.6.x-green.svg" alt="Spring Boot 2.6.x">
  <img src="https://img.shields.io/badge/MyBatis--Plus-3.5.x-orange.svg" alt="MyBatis-Plus 3.5.x">
  <img src="https://img.shields.io/badge/License-Apache%202.0-blue.svg" alt="License">
</p>

---

## 📖 Introduction

**MyBatis-Max** is a revolutionary ORM framework that eliminates the need to write Entity classes and Mapper interfaces. It automatically reads database table structures at application startup and dynamically generates all necessary code using Groovy, providing a complete set of CRUD operations through a unified Repository interface.

### Why MyBatis-Max?

| Traditional Approach | MyBatis-Max |
|---------------------|-------------|
| Create Entity class for each table | ✅ Auto-generated at runtime |
| Create Mapper interface for each table | ✅ Auto-generated at runtime |
| Write XML or annotations for SQL | ✅ Built-in generic operations |
| Modify code when table structure changes | ✅ Auto-adapts on restart |
| Repetitive CRUD boilerplate | ✅ One interface for all tables |

## ✨ Features

- **🔄 Dynamic Code Generation** - Automatically generates Entity and Mapper classes from database schema
- **📦 Zero Configuration** - Just add the dependency and configure datasource
- **🎯 Unified Repository** - Single interface for all table operations
- **📄 Built-in Pagination** - Native support for paginated queries
- **🔐 Data Permission** - Row-level and column-level permission control
- **💾 Two-Level Cache** - J2Cache integration for high-performance caching
- **🔀 Multi-DataSource** - Dynamic datasource switching with automatic routing
- **📡 Event System** - Pre/post operation events for business extensions
- **🌐 REST API** - Out-of-the-box RESTful endpoints for all tables
- **❄️ Snowflake ID** - Distributed unique ID generation

## 🏗️ Architecture

```
mybatis-max
├── mybatis-max-api          # Core interfaces and POJOs
├── mybatis-max-sdk          # Core implementation (Repository, Cache, Events)
├── mybatis-max-spring-boot-starter  # Spring Boot auto-configuration
├── mybatis-max-web          # REST API controllers
└── mybatis-max-test         # Test module and examples
```

### Core Components

```
┌─────────────────────────────────────────────────────────────────┐
│                        Application                               │
├─────────────────────────────────────────────────────────────────┤
│  BaseRest (REST API)  │  IRepositoryService (Business Layer)   │
├─────────────────────────────────────────────────────────────────┤
│              IRepository / DataPermissionRepository              │
├─────────────────────────────────────────────────────────────────┤
│  DynamicMapperUtil    │  IRepositoryHandler  │  CacheUtil       │
├─────────────────────────────────────────────────────────────────┤
│              MyBatis-Plus BaseMapper (Auto-generated)            │
├─────────────────────────────────────────────────────────────────┤
│                    DataSource (Single/Dynamic)                   │
└─────────────────────────────────────────────────────────────────┘
```

## 🚀 Quick Start

### Requirements

- Java 11+
- Spring Boot 2.6.x
- MySQL 5.7+ / Other JDBC-compatible databases

### Maven Dependency

```xml
<dependency>
    <groupId>cn.talins</groupId>
    <artifactId>mybatis-max-spring-boot-starter</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>

<!-- Optional: REST API support -->
<dependency>
    <groupId>cn.talins</groupId>
    <artifactId>mybatis-max-web</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

### Configuration

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/your_database?useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC
    username: root
    password: your_password
    driver-class-name: com.mysql.cj.jdbc.Driver

# Optional: Snowflake worker ID for distributed deployment
mybatis:
  max:
    worker-id: 1
```

### Database Table Design

Tables should include these base fields for full framework support:

```sql
CREATE TABLE your_table (
    id BIGINT(19) PRIMARY KEY COMMENT 'Primary key (auto-generated snowflake ID)',
    normal TINYINT(3) DEFAULT 1 COMMENT 'Logical delete flag: 1=active, 0=deleted',
    version BIGINT(19) COMMENT 'Optimistic lock version',
    update_time DATETIME COMMENT 'Last update time',
    extra TEXT COMMENT 'Extension field (JSON)',
    -- Your business fields...
);
```

## 📚 Usage Guide

### Method 1: Using REST API (Zero Code)

After starting the application, all tables are automatically exposed via REST endpoints:

```bash
# Insert a record
POST /mybatis-max/insert/user
{
    "param": {"name": "John", "age": 25, "email": "john@example.com"}
}

# Query by ID
POST /mybatis-max/selectById/user
{
    "param": 1
}

# Query with conditions
POST /mybatis-max/selectList/user
{
    "param": {
        "conditionList": [
            {"column": "age", "operator": "GREAT_EQUAL", "paramList": [18]}
        ],
        "orderMap": {"createTime": "DESC"}
    }
}

# Paginated query
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
```

### Method 2: Using Repository in Code

```java
@Service
public class UserService {
    
    @Autowired
    private BaseRepository baseRepository;
    
    // Insert
    public Long createUser(Map<String, Object> user) {
        return baseRepository.insert("user", user);
    }
    
    // Query by ID
    public User getUser(Long id) {
        return baseRepository.selectById("user", id, User.class);
    }
    
    // Query with conditions
    public List<User> findActiveUsers() {
        QueryWrapper<User> wrapper = Wrappers.query(User.class);
        wrapper.eq("status", 1)
               .orderByDesc("create_time");
        return baseRepository.selectList("user", wrapper);
    }
    
    // Paginated query
    public IPage<User> pageUsers(int pageNum, int pageSize) {
        Page<User> page = new Page<>(pageNum, pageSize);
        QueryWrapper<User> wrapper = Wrappers.query(User.class);
        return baseRepository.selectPage("user", page, wrapper);
    }
    
    // Update
    public int updateUser(Map<String, Object> user) {
        return baseRepository.updateById("user", user);
    }
    
    // Delete
    public int deleteUser(Long id) {
        return baseRepository.deleteById("user", id);
    }
}
```

### Method 3: Using Service Abstraction

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
    
    // Now you can use inherited methods without specifying table name
    // insert(), selectById(), updateById(), deleteById(), etc.
}
```

## 🔧 Advanced Features

### Query Operators

| Operator | Description | Example |
|----------|-------------|---------|
| `EQUAL` | Equals (=) | `{"column": "status", "operator": "EQUAL", "paramList": [1]}` |
| `NOT_EQUAL` | Not equals (!=) | `{"column": "status", "operator": "NOT_EQUAL", "paramList": [0]}` |
| `LIKE` | Pattern match | `{"column": "name", "operator": "LIKE", "paramList": ["%John%"]}` |
| `IN` | In list | `{"column": "id", "operator": "IN", "paramList": [1, 2, 3]}` |
| `BETWEEN` | Range | `{"column": "age", "operator": "BETWEEN", "paramList": [18, 30]}` |
| `GREAT` / `LESS` | Greater/Less than | `{"column": "age", "operator": "GREAT", "paramList": [18]}` |
| `IS_NULL` / `IS_NOT_NULL` | Null check | `{"column": "email", "operator": "IS_NOT_NULL", "paramList": []}` |

### Multi-DataSource Configuration

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

The framework automatically routes queries to the correct datasource based on table location.

### Data Permission Control

Implement `IDataPermissionHandler` to add row/column level permissions:

```java
@Bean
public IDataPermissionHandler dataPermissionHandler() {
    return new IDataPermissionHandler() {
        @Override
        public <T> void addRowPermission(String tableName, QueryWrapper<T> wrapper) {
            // Add tenant isolation
            wrapper.eq("tenant_id", getCurrentTenantId());
        }
        
        @Override
        public <T> T addColumnPermission(String tableName, T entity) {
            // Mask sensitive fields
            if (entity instanceof User) {
                ((User) entity).setPhone(maskPhone(((User) entity).getPhone()));
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

### Event Listeners

Listen to entity operation events for business extensions:

```java
@Component
public class EntityEventListener {
    
    @EventListener
    public void onInsert(EntityInsertEvent event) {
        if (event.getIsBefore() == 1) {
            // Before insert
            log.info("Inserting into {}: {}", event.getTableName(), event.getEntity());
        } else {
            // After insert
            log.info("Inserted into {}: {}", event.getTableName(), event.getEntity());
        }
    }
    
    @EventListener
    public void onUpdate(EntityUpdateByIdEvent event) {
        // Handle update events
    }
    
    @EventListener
    public void onDelete(EntityDeleteEvent event) {
        // Handle delete events
    }
}
```

### Custom ID Generator

```java
@Bean
public IIdGenerator customIdGenerator() {
    return () -> {
        // Your custom ID generation logic
        return UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
    };
}
```

## 🔨 Building from Source

```bash
# Clone the repository
git clone https://github.com/talins/mybatis-max.git
cd mybatis-max

# Build with Maven
mvn clean install -DskipTests

# Run tests
mvn test
```

## 📋 Module Description

| Module | Description |
|--------|-------------|
| `mybatis-max-api` | Core interfaces (`IRepository`, `IRepositoryService`) and POJOs (`BaseEntity`, `Query`, `Condition`) |
| `mybatis-max-sdk` | Core implementations including `BaseRepository`, `DynamicMapperUtil`, cache utilities, and event system |
| `mybatis-max-spring-boot-starter` | Spring Boot auto-configuration, dynamic Mapper registration, and default bean configurations |
| `mybatis-max-web` | REST controller (`BaseRest`) providing HTTP endpoints for all database operations |
| `mybatis-max-test` | Test cases and usage examples |

## 🤝 Contributing

We welcome contributions! Please see our [Contributing Guide](CONTRIBUTING.md) for details.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- [MyBatis-Plus](https://github.com/baomidou/mybatis-plus) - The powerful ORM framework this project is built upon
- [Hutool](https://github.com/dromara/hutool) - A comprehensive Java utility library
- [J2Cache](https://gitee.com/ld/J2Cache) - Two-level cache framework
- [Yitter IdGenerator](https://github.com/yitter/IdGenerator) - Snowflake ID generator

---

<p align="center">
  Made with ❤️ by <a href="https://github.com/talins">Talins</a>
</p>
