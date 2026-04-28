# 🍜 Canteen Server — 校园食堂评价系统后端

> Android 端大众点评风格的校园食堂点评 App 服务端，基于 Spring Boot 构建。

---
## 关联库，基于spring boot的java后端服务器，主要为Canteen Android应用提供数据服务
---

## 技术栈

| 组件 | 版本 |
|------|------|
| Java | 17 |
| Spring Boot | 3.5.0（待 4.0.5 正式发布后升级） |
| Spring Data JPA | 随 Spring Boot |
| MySQL | 8.0+ |
| MapStruct | 1.6.3 |
| Lombok | 1.18.36 |
| Maven | 3.8+ |

---

## 项目结构

```
canteen/
├── pom.xml
└── src/main/
    ├── java/com/canteen/
    │   ├── CanteenApplication.java          # 启动类
    │   ├── config/
    │   │   ├── JpaConfig.java               # 启用 JPA Auditing
    │   │   └── WebConfig.java               # CORS 等 Web 配置
    │   ├── controller/
    │   │   ├── UserController.java
    │   │   ├── FoodController.java
    │   │   ├── PostController.java
    │   │   └── CommentController.java
    │   ├── service/
    │   │   ├── Services.java                # 所有 Service 接口
    │   │   └── impl/
    │   │       ├── UserServiceImpl.java
    │   │       ├── FoodServiceImpl.java
    │   │       ├── PostServiceImpl.java
    │   │       └── CommentServiceImpl.java
    │   ├── repository/
    │   │   └── Repositories.java            # 所有 Repository 接口
    │   ├── model/
    │   │   ├── entity/
    │   │   │   ├── BaseEntity.java          # 公共字段基类
    │   │   │   ├── User.java
    │   │   │   ├── Food.java
    │   │   │   ├── Post.java
    │   │   │   └── Comment.java
    │   │   ├── dto/
    │   │   │   └── Dtos.java                # 所有 DTO
    │   │   ├── request/
    │   │   │   └── Requests.java            # 所有请求体对象
    │   │   └── response/
    │   │       ├── ApiResponse.java         # 统一响应包装
    │   │       └── PageResponse.java        # 分页响应包装
    │   ├── mapper/
    │   │   └── Mappers.java                 # MapStruct 转换器
    │   └── exception/
    │       ├── Exceptions.java              # 自定义异常
    │       └── GlobalExceptionHandler.java  # 全局异常处理
    └── resources/
        ├── application.yml                  # 主配置文件
        └── init.sql                         # 数据库初始化脚本（参考）
```

---

## 快速启动

### 1. 准备数据库

```bash
mysql -u root -p src/main/resources/init.sql
```

### 2. 修改配置

编辑 `src/main/resources/application.yml`，填写你的数据库密码：

```yaml
spring:
  datasource:
    password: your_password   # ← 改这里
```

### 3. 启动服务

```bash
mvn spring-boot:run
```

服务启动后访问：`http://localhost:8080/api/v1/...`

---

## API 接口一览

> 所有接口前缀：`/api`（由 `server.servlet.context-path` 配置）

### 统一响应格式

```json
{
  "success": true,
  "code": 200,
  "message": "操作成功",
  "data": { },
  "timestamp": "2025-01-01T12:00:00"
}
```

### 分页响应格式（data 字段）

```json
{
  "content": [],
  "page": 0,
  "size": 10,
  "totalElements": 100,
  "totalPages": 10,
  "last": false
}
```

---

### 👤 User 接口

| 方法 | 路径 | 描述 |
|------|------|------|
| GET | `/v1/users/{id}` | 获取用户信息 |
| POST | `/v1/users` | 注册用户 |

### 🍖 Food 接口

| 方法 | 路径 | 描述 |
|------|------|------|
| GET | `/v1/foods` | 菜品列表（分页） |
| GET | `/v1/foods/search?keyword=xxx` | 搜索菜品 |
| GET | `/v1/foods/{id}` | 菜品详情 |
| POST | `/v1/foods` | 新增菜品 |
| PUT | `/v1/foods/{id}` | 更新菜品 |
| DELETE | `/v1/foods/{id}` | 删除菜品 |

### 📝 Post 接口

| 方法 | 路径 | 描述 |
|------|------|------|
| GET | `/v1/posts` | 首页 Feed |
| GET | `/v1/posts/search?keyword=xxx` | 搜索帖子 |
| GET | `/v1/posts/{id}` | 帖子详情 |
| GET | `/v1/posts/food/{foodId}` | 某菜品的帖子 |
| GET | `/v1/posts/user/{userId}` | 某用户的帖子 |
| POST | `/v1/posts` | 发布帖子 |
| PUT | `/v1/posts/{id}` | 更新帖子 |
| DELETE | `/v1/posts/{id}` | 删除帖子 |

### 💬 Comment 接口

| 方法 | 路径 | 描述 |
|------|------|------|
| GET | `/v1/posts/{postId}/comments` | 帖子评论列表 |
| POST | `/v1/posts/{postId}/comments` | 发表评论 |
| DELETE | `/v1/comments/{id}` | 删除评论 |

---

## 数据关系

```
User  ──< Post >──── Food
            │
            └──< Comment >── User
```

- **Food ↔ Post**：多对多，关联表 `food_post`
- **Post → Comment**：一对多
- **User → Post**：一对多（作者）
- **User → Comment**：一对多（评论者）

---

## 后续扩展建议

- [ ] 引入 Spring Security + JWT 认证
- [ ] 菜品分类 / 食堂管理
- [ ] 帖子点赞 / 收藏功能
- [ ] 图片上传（OSS / MinIO）
- [ ] 引入 Redis 缓存热门帖子
- [ ] 使用 Flyway 管理数据库版本迁移
- [ ] 评论楼中楼（树形结构）
- [ ] Swagger/OpenAPI 接口文档

