package com.canteen.model.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "comment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment extends BaseEntity {

    /** 评论内容，不允许为空 */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    // 支持楼中楼回复（自关联）
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "parent_id")
    // private Comment parentComment;

    /** 软删除标记，默认为 false */
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;


    // 关联关系

    /**
     * 评论所属帖子（多对一）
     * 数据库存储 post_id 外键列
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    /**
     * 评论作者（多对一）
     * 数据库存储 user_id 外键列
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User author;
}
