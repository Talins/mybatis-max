# Security Policy

## Supported Versions

| Version | Supported          |
| ------- | ------------------ |
| 1.0.x   | :white_check_mark: |
| < 1.0   | :x:                |

## Reporting a Vulnerability

We take the security of MyBatis-Max seriously. If you believe you have found a security vulnerability, please report it to us as described below.

### How to Report

**Please do not report security vulnerabilities through public GitHub issues.**

Instead, please report them via email to [security@example.com] (replace with actual email).

You should receive a response within 48 hours. If for some reason you do not, please follow up via email to ensure we received your original message.

Please include the following information in your report:

- Type of issue (e.g., SQL injection, authentication bypass, etc.)
- Full paths of source file(s) related to the manifestation of the issue
- The location of the affected source code (tag/branch/commit or direct URL)
- Any special configuration required to reproduce the issue
- Step-by-step instructions to reproduce the issue
- Proof-of-concept or exploit code (if possible)
- Impact of the issue, including how an attacker might exploit it

### What to Expect

- We will acknowledge receipt of your vulnerability report within 48 hours
- We will provide a more detailed response within 7 days
- We will work with you to understand and resolve the issue quickly
- We will keep you informed about our progress
- We will credit you in the security advisory (unless you prefer to remain anonymous)

### Safe Harbor

We support safe harbor for security researchers who:

- Make a good faith effort to avoid privacy violations, destruction of data, and interruption or degradation of our services
- Only interact with accounts you own or with explicit permission of the account holder
- Do not exploit a security issue for purposes other than verification
- Report vulnerabilities promptly

## Security Best Practices

When using MyBatis-Max, please follow these security best practices:

1. **Keep Dependencies Updated**: Regularly update MyBatis-Max and its dependencies
2. **Use Parameterized Queries**: Always use the provided Query/Condition objects instead of raw SQL
3. **Validate Input**: Use the built-in validation annotations
4. **Implement Data Permissions**: Use IDataPermissionHandler for row/column level security
5. **Secure Database Credentials**: Never commit database credentials to version control
6. **Use HTTPS**: Always use HTTPS in production environments
7. **Limit API Exposure**: Only expose necessary endpoints

## Known Security Features

MyBatis-Max includes several security features:

- **SQL Injection Prevention**: Column names are validated against a whitelist pattern
- **Parameter Validation**: JSR-380 validation annotations prevent malicious input
- **Data Permission Control**: Row and column level access control
- **Input Sanitization**: All user inputs are properly escaped

Thank you for helping keep MyBatis-Max and its users safe!
