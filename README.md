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

### 接口

