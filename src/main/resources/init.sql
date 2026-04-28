-- ============================================================
-- Canteen 数据库初始化脚本
-- 仅供参考，JPA 会根据实体自动建表（ddl-auto: update）
-- 生产环境建议使用 Flyway / Liquibase 管理数据库版本
-- ============================================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS canteen_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE canteen_db;

-- ============================================================
-- 用户表
-- ============================================================
CREATE TABLE IF NOT EXISTS users (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(50)  NOT NULL COMMENT '用户昵称',
    created_at DATETIME(6)  NOT NULL COMMENT '创建时间',
    updated_at DATETIME(6)  NOT NULL COMMENT '更新时间'
    -- TODO: email VARCHAR(100) UNIQUE
    -- TODO: avatar_url VARCHAR(500)
) COMMENT = '用户表';

-- ============================================================
-- 菜品表
-- ============================================================
CREATE TABLE IF NOT EXISTS food (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100)   NOT NULL COMMENT '菜品名称',
    description TEXT           COMMENT '菜品描述',
    price       DECIMAL(8, 2)  COMMENT '价格',
    image_url   VARCHAR(500)   COMMENT '菜品图片 URL',
    campus      VARCHAR(100)   COMMENT '校区',
    canteen     VARCHAR(150)   COMMENT '食堂名称',
    floor       VARCHAR(50)    COMMENT '楼层',
    window_name VARCHAR(100)   COMMENT '窗口名称/编号',
    sell_time   VARCHAR(255)   COMMENT '售卖时间',
    average_rating DECIMAL(3,2) DEFAULT 0.00 COMMENT '平均评分',
    rating_count INT DEFAULT 0 COMMENT '评分人数',
    created_at  DATETIME(6)    NOT NULL,
    updated_at  DATETIME(6)    NOT NULL
    -- TODO: category VARCHAR(50) COMMENT '分类'
    -- TODO: is_available TINYINT(1) DEFAULT 1
) COMMENT = '菜品表';

-- 存放标签的集合表
CREATE TABLE IF NOT EXISTS food_tags (
    food_id BIGINT NOT NULL,
    tag VARCHAR(50),
    CONSTRAINT fk_ft_food FOREIGN KEY (food_id) REFERENCES food (id) ON DELETE CASCADE
) COMMENT = '菜品标签表';

-- ============================================================
-- 帖子表
-- ============================================================
CREATE TABLE IF NOT EXISTS post (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    title      VARCHAR(200) NOT NULL COMMENT '帖子标题',
    content    TEXT         NOT NULL COMMENT '帖子内容',
    rating     INT          COMMENT '评分 1~5',
    user_id    BIGINT       NOT NULL COMMENT '作者 ID',
    created_at DATETIME(6)  NOT NULL,
    updated_at DATETIME(6)  NOT NULL,
    CONSTRAINT fk_post_user FOREIGN KEY (user_id) REFERENCES users (id)
    -- TODO: like_count INT DEFAULT 0
    -- TODO: view_count INT DEFAULT 0
) COMMENT = '帖子/点评表';

-- ============================================================
-- 菜品-帖子 关联表（多对多）
-- ============================================================
CREATE TABLE IF NOT EXISTS food_post (
    post_id BIGINT NOT NULL,
    food_id BIGINT NOT NULL,
    PRIMARY KEY (post_id, food_id),
    CONSTRAINT fk_fp_post FOREIGN KEY (post_id) REFERENCES post (id) ON DELETE CASCADE,
    CONSTRAINT fk_fp_food FOREIGN KEY (food_id) REFERENCES food (id) ON DELETE CASCADE
) COMMENT = '菜品与帖子多对多关联表';

-- ============================================================
-- 评论表
-- ============================================================
CREATE TABLE IF NOT EXISTS comment (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    content    TEXT        NOT NULL COMMENT '评论内容',
    post_id    BIGINT      NOT NULL COMMENT '所属帖子 ID',
    user_id    BIGINT      NOT NULL COMMENT '评论者 ID',
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    CONSTRAINT fk_comment_post FOREIGN KEY (post_id) REFERENCES post (id) ON DELETE CASCADE,
    CONSTRAINT fk_comment_user FOREIGN KEY (user_id) REFERENCES users (id)
    -- TODO: parent_id BIGINT COMMENT '父评论 ID（楼中楼）'
) COMMENT = '评论表';

-- ============================================================
-- 示例测试数据（可选，用于本地开发调试）
-- ============================================================
INSERT IGNORE INTO users (id, name, created_at, updated_at)
VALUES (1, '小明', NOW(), NOW()),
       (2, '小红', NOW(), NOW());

INSERT IGNORE INTO food (id, name, description, price, campus, canteen, floor, window_name, sell_time, average_rating, rating_count, created_at, updated_at)
VALUES (1, '红烧肉', '肥而不腻，汤汁浓郁', 12.00, NULL, NULL, NULL, NULL, NULL, 0.00, 0, NOW(), NOW()),
       (2, '番茄炒蛋', '家常下饭菜', 8.00, NULL, NULL, NULL, NULL, NULL, 0.00, 0, NOW(), NOW()),
       (3, '米饭', '东北大米', 1.00, NULL, NULL, NULL, NULL, NULL, 0.00, 0, NOW(), NOW());

-- 给示例食物添加标签
INSERT IGNORE INTO food_tags (food_id, tag)
VALUES (1, '肉类'),
       (1, '经典'),
       (2, '素菜'),
       (2, '家常'),
       (3, '主食');

-- 示例帖子数据
INSERT IGNORE INTO post (id, title, content, rating, user_id, created_at, updated_at)
VALUES (1, '红烧肉太好吃了！', '我觉得食堂的红烧肉味道非常棒，肥而不腻，汤汁浓郁，强烈推荐！', 5, 1, NOW(), NOW()),
       (2, '番茄炒蛋不错', '番茄炒蛋味道不错，家常下饭菜，价格也实惠。', 4, 2, NOW(), NOW());
--INSERT IGNORE INTO food (id, name, description, price, created_at, updated_at)
--VALUES (1, '红烧肉', '肥而不腻，汤汁浓郁', 12.00, NOW(), NOW()),
--       (2, '番茄炒蛋', '家常下饭菜', 8.00, NOW(), NOW()),
--       (3, '米饭', '东北大米', 1.00, NOW(), NOW());