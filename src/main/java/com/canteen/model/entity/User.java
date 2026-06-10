package com.canteen.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    /** 用户权限 */
    @Column(nullable = false, length = 20)
    private String permission = "USER";

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
