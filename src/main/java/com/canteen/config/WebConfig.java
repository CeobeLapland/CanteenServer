package com.canteen.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC 全局配置
 *
 * <p>主要职责：
 * <ul>
 *   <li>配置 CORS 跨域策略（允许 Android 端调用）</li>
 *   <li>后续可添加：拦截器、消息转换器、静态资源映射等</li>
 * </ul>
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * 跨域配置
     *
     * <p>开发阶段允许所有来源，生产环境应替换为具体的服务器域名：
     * <pre>
     *   .allowedOrigins("https://your-domain.com")
     * </pre>
     *
     * <p>Android 原生应用不受浏览器 CORS 限制，但保留此配置
     * 便于后续 Web 端或 Postman 调试使用。
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/v1/**")
                .allowedOriginPatterns("*")           // 开发时允许所有来源
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);                         // 预检请求缓存 1 小时
    }

    // TODO: 后续可在此添加：
    // - JWT 认证拦截器 addInterceptors()
    // - 自定义参数解析器 addArgumentResolvers()
}
