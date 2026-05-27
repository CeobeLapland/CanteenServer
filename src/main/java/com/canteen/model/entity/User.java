package com.canteen.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户实体
 * <p>当前版本仅包含基础字段（id、name），后续可按需扩展
 */
@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

    /** 用户昵称，不允许为空 */
    @Column(nullable = false, length = 50)
    private String name;

    /** 用户权限角色（如 ROLE_USER、ROLE_ADMIN），后续权限控制用 */
    @Column(nullable = false, length = 20)
    private String role = "ROLE_USER";

    // ========== 关联关系 ==========

    /**
     * 用户发布的帖子（一对多）
     * mappedBy 指向 Post 中维护外键的字段名
     */
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Post> posts = new ArrayList<>();

    /**
     * 用户发布的评论（一对多）
     */
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();
}
