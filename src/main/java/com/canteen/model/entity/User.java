package com.canteen.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户实体
 *
 * <p>当前版本仅包含基础字段（id、name），后续可按需扩展：
 * <ul>
 *   <li>password / 密码（加密存储）</li>
 *   <li>email / 手机号</li>
 *   <li>avatar / 头像 URL</li>
 *   <li>role / 角色（普通用户、管理员）</li>
 * </ul>
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

    /** 用户昵称，不允许为空 */
    @Column(nullable = false, length = 50)
    private String name;

    // ========== 占位符字段（后续扩展） ==========

    // @Column(unique = true, length = 100)
    // private String email;

    // @Column(length = 20)
    // private String phone;

    // @Column(length = 200)
    // private String avatarUrl;

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
