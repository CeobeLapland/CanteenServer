package com.canteen.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 帖子实体（食物评测/点评）
 *
 * <p>关系说明：
 * <ul>
 *   <li>Post ←→ Food  : 多对多，由本类维护关联表 {@code food_post}</li>
 *   <li>Post  → Comment: 一对多，Comment 中存有 post_id 外键</li>
 *   <li>Post  → User  : 多对一，本类存有 user_id 外键（作者）</li>
 * </ul>
 */
@Entity
@Table(name = "post")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post extends BaseEntity {

    /** 帖子标题 */
    @Column(nullable = false, length = 200)
    private String title;

    /** 帖子正文内容 */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    /**
     * 综合评分（1~5 星）
     * 占位符：后续可细化为多维度评分（口味/分量/性价比）
     */
    @Column
    private Integer rating;

    // ========== 占位符字段 ==========
    // @Column(name = "like_count")
    // private Integer likeCount = 0;   // 点赞数

    // @Column(name = "view_count")
    // private Integer viewCount = 0;   // 浏览数

    // ========== 关联关系 ==========

    /**
     * 帖子作者（多对一）
     * 数据库存储 user_id 外键列
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User author;

    /**
     * 帖子涉及的菜品（多对多，主动方）
     *
     * <p>关联表 food_post 结构：
     * <pre>
     *   food_post (
     *     post_id BIGINT FK → post.id,
     *     food_id BIGINT FK → food.id
     *   )
     * </pre>
     */
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "food_post",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "food_id")
    )
    @Builder.Default
    private Set<Food> foods = new HashSet<>();

    /**
     * 帖子下的评论列表（一对多）
     * orphanRemoval=true：删除帖子时同步删除所有评论
     */
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    // ========== 辅助方法（维护双向关系） ==========

    /** 添加菜品并同步反向关系 */
    public void addFood(Food food) {
        this.foods.add(food);
        food.getPosts().add(this);
    }

    /** 移除菜品并同步反向关系 */
    public void removeFood(Food food) {
        this.foods.remove(food);
        food.getPosts().remove(this);
    }
}
