# Contributing to MyBatis-Max

First off, thank you for considering contributing to MyBatis-Max! It's people like you that make MyBatis-Max such a great tool.

## Code of Conduct

This project and everyone participating in it is governed by the [MyBatis-Max Code of Conduct](CODE_OF_CONDUCT.md). By participating, you are expected to uphold this code.

## How Can I Contribute?

### Reporting Bugs

Before creating bug reports, please check the existing issues as you might find out that you don't need to create one. When you are creating a bug report, please include as many details as possible:

- **Use a clear and descriptive title** for the issue to identify the problem.
- **Describe the exact steps which reproduce the problem** in as many details as possible.
- **Provide specific examples to demonstrate the steps**.
- **Describe the behavior you observed after following the steps** and point out what exactly is the problem with that behavior.
- **Explain which behavior you expected to see instead and why.**
- **Include logs and stack traces** if applicable.

### Suggesting Enhancements

Enhancement suggestions are tracked as GitHub issues. When creating an enhancement suggestion, please include:

- **Use a clear and descriptive title** for the issue to identify the suggestion.
- **Provide a step-by-step description of the suggested enhancement** in as many details as possible.
- **Provide specific examples to demonstrate the steps**.
- **Describe the current behavior** and **explain which behavior you expected to see instead** and why.
- **Explain why this enhancement would be useful** to most MyBatis-Max users.

### Pull Requests

1. Fork the repo and create your branch from `main`.
2. If you've added code that should be tested, add tests.
3. If you've changed APIs, update the documentation.
4. Ensure the test suite passes.
5. Make sure your code follows the existing code style.
6. Issue that pull request!

## Development Setup

### Prerequisites

- Java 11 or higher
- Maven 3.6 or higher
- MySQL 5.7+ or PostgreSQL 10+ (for running tests)

### Building the Project

```bash
# Clone your fork
git clone https://github.com/your-username/mybatis-max.git
cd mybatis-max

# Add upstream remote
git remote add upstream https://github.com/talins/mybatis-max.git

# Install dependencies and build
mvn clean install -DskipTests
```

### Running Tests

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=SpringBootValidationTest

# Run tests with coverage
mvn test jacoco:report
```

### Code Style

We follow standard Java coding conventions with some additional rules:

- Use 4 spaces for indentation (no tabs)
- Maximum line length is 120 characters
- Use meaningful variable and method names
- Add Javadoc comments for public APIs
- Keep methods short and focused

### Commit Messages

- Use the present tense ("Add feature" not "Added feature")
- Use the imperative mood ("Move cursor to..." not "Moves cursor to...")
- Limit the first line to 72 characters or less
- Reference issues and pull requests liberally after the first line

Example:
```
Add data permission handler for column-level security

- Implement IDataPermissionHandler interface
- Add unit tests for permission filtering
- Update documentation

Fixes #123
```

## Project Structure

```
mybatis-max/
├── mybatis-max-api/          # Core API interfaces and POJOs
├── mybatis-max-sdk/          # SDK implementation
├── mybatis-max-spring-boot-starter/  # Spring Boot auto-configuration
├── mybatis-max-web/          # RESTful API controllers
├── mybatis-max-test/         # Test cases and examples
├── pom.xml                   # Parent POM
└── README.md
```

## Review Process

1. All submissions require review before being merged.
2. We use GitHub pull requests for this purpose.
3. Reviewers may request changes or ask questions.
4. Once approved, a maintainer will merge your PR.

## Community

- GitHub Issues: For bug reports and feature requests
- GitHub Discussions: For questions and general discussion

## License

By contributing to MyBatis-Max, you agree that your contributions will be licensed under its AGPL-3.0 License.

Thank you for contributing! 🎉
