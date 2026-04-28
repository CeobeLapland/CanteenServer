package com.canteen.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * JPA 配置类
 *
 * <p>{@code @EnableJpaAuditing} 启用 JPA 审计功能，
 * 使 {@link com.canteen.model.entity.BaseEntity} 中的
 * {@code @CreatedDate} 和 {@code @LastModifiedDate} 注解生效，
 * 自动填充 createdAt / updatedAt 字段。
 *
 * <p>若后续引入用户认证，可配置 AuditorAware Bean 以自动记录操作人：
 * <pre>
 * {@code
 * @Bean
 * public AuditorAware<Long> auditorAware() {
 *     // 从 SecurityContext 中取当前用户 ID
 *     return () -> Optional.ofNullable(SecurityUtils.getCurrentUserId());
 * }
 * }
 * </pre>
 */
@Configuration
@EnableJpaAuditing
public class JpaConfig {
    // 配置体保持空，注解本身已足够启用审计
}
