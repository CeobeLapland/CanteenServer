package com.canteen.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 标签实体（目前只有名字）
 */
@Entity
@Table(name = "tag")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tag extends BaseEntity {

    /** 标签名称 */
    @Column(name = "name", nullable = false, length = 45)
    private String name;

    // ========== 关联关系 ==========

    /**
     * 标签关联的帖子（多对多）
     * 由 Food 一侧维护关联表（joinTable 在 Food 中声明）
     */
    @ManyToMany(mappedBy = "tags")
    @Builder.Default
    private List<Food> foods = new ArrayList<>();
}
