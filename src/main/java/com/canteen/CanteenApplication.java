package com.canteen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Canteen 校园食堂评价系统 —— 服务端启动类
 *
 * <p>模块划分说明：
 * <ul>
 *   <li>controller  — 接收 HTTP 请求，参数校验，调用 Service</li>
 *   <li>service     — 业务逻辑层</li>
 *   <li>repository  — 数据访问层（Spring Data JPA）</li>
 *   <li>model       — 实体、DTO、请求/响应对象</li>
 *   <li>mapper      — MapStruct Entity ↔ DTO 转换</li>
 *   <li>exception   — 统一异常处理</li>
 *   <li>config      — 全局配置（跨域、Web 等）</li>
 *   <li>util        — 通用工具类</li>
 * </ul>
 */
@SpringBootApplication
public class CanteenApplication {

    public static void main(String[] args) {
        SpringApplication.run(CanteenApplication.class, args);
    }
}
