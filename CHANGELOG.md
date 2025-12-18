# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added
- Initial release of MyBatis-Max framework
- Dynamic entity generation based on database table structure
- Zero-code CRUD operations for all database tables
- RESTful API auto-generation via `mybatis-max-web` module
- Spring Boot starter for easy integration
- Data permission control (row-level and column-level)
- Comprehensive parameter validation with JSR-380 annotations
- Distributed ID generation using snowflake algorithm
- Dynamic data source support
- Pagination support with configurable page size limits
- Query builder with support for multiple conditions and operators
- Event system for entity lifecycle hooks (insert, update, delete)
- Thread-local cache for performance optimization

### Security
- SQL injection prevention through column name pattern validation
- Parameter validation to prevent malicious input

## [1.0.0-SNAPSHOT] - 2024-XX-XX

### Added
- Core API module (`mybatis-max-api`)
  - `IRepository` interface for basic CRUD operations
  - `IRepositoryService` interface for service layer
  - `IDataPermissionHandler` for data permission control
  - `IIdGenerator` for ID generation
  - POJO classes: `BaseRequest`, `PageRequest`, `MapRequest`, `Query`, `Condition`, `BaseEntity`
  - Enums: `Operator`, `Connect`, `Order`, `Booleans`

- SDK module (`mybatis-max-sdk`)
  - `BaseRepository` implementation
  - `DataPermissionRepository` with permission filtering
  - `DynamicMapperUtil` for dynamic mapper generation
  - `DynamicDataSource` for multi-datasource support
  - Event publishing for entity operations

- Spring Boot Starter module (`mybatis-max-spring-boot-starter`)
  - Auto-configuration for all components
  - `MybatisMaxProperties` for configuration
  - `DefaultRepositoryHandler` for entity field auto-filling
  - Dynamic mapper bean registration

- Web module (`mybatis-max-web`)
  - `BaseRest` controller with full CRUD endpoints
  - `GlobalExceptionHandler` for unified error handling
  - `QueryUtil` for converting Query objects to QueryWrapper

- Test module (`mybatis-max-test`)
  - Comprehensive unit tests
  - Integration tests
  - Validation tests

---

## Version History

### Versioning Scheme

We use [Semantic Versioning](https://semver.org/):

- **MAJOR** version for incompatible API changes
- **MINOR** version for backwards-compatible functionality additions
- **PATCH** version for backwards-compatible bug fixes

### Release Cycle

- **SNAPSHOT** releases are published on every commit to the main branch
- **Release** versions are published when features are stable and tested

[Unreleased]: https://github.com/talins/mybatis-max/compare/v1.0.0...HEAD
[1.0.0-SNAPSHOT]: https://github.com/talins/mybatis-max/releases/tag/v1.0.0-SNAPSHOT
