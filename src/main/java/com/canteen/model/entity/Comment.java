package com.canteen.model.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * 评论实体
 * <p>关系说明：
 * <ul>
 *   <li>Comment → Post : 多对一，存储 post_id 外键</li>
 *   <li>Comment → User : 多对一，存储 user_id 外键（评论者）</li>
 * </ul>
 * <p>后续可扩展：
 * <ul>
 *   <li>parentComment — 支持评论回复（树形结构）</li>
 *   <li>likeCount     — 评论点赞数</li>
 *   <li>isDeleted     — 软删除标记</li>
 * </ul>
 */
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

    // ========== 占位符字段 ==========
    // @Column(name = "like_count")
    // private Integer likeCount = 0;

    // 支持楼中楼回复（自关联）
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "parent_id")
    // private Comment parentComment;

    // ========== 关联关系 ==========

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
