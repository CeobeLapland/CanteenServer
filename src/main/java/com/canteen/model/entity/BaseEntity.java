package com.canteen.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 所有实体的公共基类
 *
 * <p>提供：
 * <ul>
 *   <li>id          — 自增主键</li>
 *   <li>createdAt   — 创建时间（自动填充）</li>
 *   <li>updatedAt   — 更新时间（自动填充）</li>
 * </ul>
 *
 * <p>子类只需标注 {@link jakarta.persistence.Entity} 即可继承这些字段，
 * 无需重复声明。如需启用自动审计，在配置类上加 {@code @EnableJpaAuditing}。
 */
@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 记录创建时间，由 JPA Auditing 自动填充，不可更新 */
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /** 记录最后更新时间，由 JPA Auditing 自动填充 */
    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
