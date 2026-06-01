package com.canteen.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 食物实体
 * 关系：Food ←→ Post : 多对多，由 Post.foods 中的 @JoinTable 维护关联表 food_post
 * 关系：Food ←→ Tag : 多对多，由 Food.tags 中的 @JoinTable 维护关联表 food_tag
 */
@Entity
@Table(name = "food")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Food extends BaseEntity {

    /** 菜品名称，不允许为空 */
    @Column(name = "name", nullable = false, length = 45)
    private String name;

    /** 菜品描述 */
    @Column(name = "description", length = 45)
    private String description;

    /** 价格，精确到分 */
    @Column(name = "price")
    private Integer price;

    /** 菜品图片 URL（占位符） */
    @Column(name = "image_url", length = 500)
    private String imageUrl;



    /** 校区 */
    @Column(name = "campus", length = 45)
    private String campus;

    /** 食堂名称 */
    @Column(name = "canteen", length = 45)
    private String canteen;

    /** 楼层 */
    @Column(name = "floor", length = 45)
    private String floor;

    /** 窗口编号或名称 */
    @Column(name = "window", length = 45)
    private String window;

    /** 售卖时间，简单以字符串保存（比如："07:00-09:30,11:00-13:00"） */
    @Column(name = "sell_time", length = 200)
    private String sellTime;

    /** 全局综合评分（0.0 ~ 5.0） */
    @Column(name = "average_rating")//, precision = 3, scale = 2)
    private float averageRating = 0f;

    /** 参与评分的总人数 */
    @Column(name = "rating_count")
    private Integer ratingCount = 0;

    // ========== 关联关系 ==========

    /**
     * 与 Post 的多对多关系（被动方）
     * 由 Post.foods 中的 @JoinTable 维护，这里只需 mappedBy
     */
    @ManyToMany(mappedBy = "foods")
    @Builder.Default
    private Set<Post> posts = new HashSet<>();



    /**
     * 与 Tag 的多对多关系（主动方）
     * 关联表 food_tag 存储 food_id 和 tag_id 外键列
     */
    @ManyToMany
    @JoinTable(
            name = "food_tag",
            joinColumns = @JoinColumn(name = "food_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @Builder.Default
    private List<Tag> tags = new ArrayList<>();

    // ========== 辅助方法（维护双向关系） ==========
    /** 添加标签并同步反向关系 */
    public void addTag(Tag tag) {
        this.tags.add(tag);
        tag.getFoods().add(this);
    }

    /** 移除标签并同步反向关系 */
    public void removeTag(Tag tag) {
        this.tags.remove(tag);
        tag.getFoods().remove(this);
    }
}
