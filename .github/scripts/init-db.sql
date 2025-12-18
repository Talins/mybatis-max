-- Initialize test database for CI/CD

CREATE TABLE IF NOT EXISTS `sys_user` (
    `id` BIGINT(19) NOT NULL COMMENT 'Primary Key ID',
    `username` VARCHAR(64) NOT NULL COMMENT 'Username',
    `nickname` VARCHAR(64) DEFAULT NULL COMMENT 'Nickname',
    `email` VARCHAR(128) DEFAULT NULL COMMENT 'Email',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT 'Phone',
    `status` TINYINT(3) DEFAULT 1 COMMENT 'Status: 1-Active, 0-Inactive',
    `normal` TINYINT(3) DEFAULT 1 COMMENT 'Normal flag: 1-Normal, 0-Deleted',
    `version` BIGINT(19) DEFAULT NULL COMMENT 'Optimistic lock version',
    `update_time` VARCHAR(32) DEFAULT NULL COMMENT 'Last update time',
    `extra` TEXT DEFAULT NULL COMMENT 'Extra data (JSON)',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    KEY `idx_status` (`status`),
    KEY `idx_normal` (`normal`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='System User Table';

-- Insert test data
INSERT INTO `sys_user` (`id`, `username`, `nickname`, `email`, `status`, `normal`, `version`, `update_time`) VALUES
(1, 'admin', 'Administrator', 'admin@example.com', 1, 1, 1, '2024-01-01 00:00:00'),
(2, 'user1', 'Test User 1', 'user1@example.com', 1, 1, 1, '2024-01-01 00:00:00'),
(3, 'user2', 'Test User 2', 'user2@example.com', 1, 1, 1, '2024-01-01 00:00:00');
