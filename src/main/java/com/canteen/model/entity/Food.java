package com.canteen.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 食物实体
 *
 * <p>代表食堂中的一道具体菜品。
 * 一道菜可以出现在多篇帖子中；一篇帖子也可以包含多道菜（多对多）。
 * 关联表为 food_post，由 Post 一侧维护（joinTable 在 Post 中声明）。
 *
 * <p>后续可扩展字段：
 * <ul>
 *   <li>category  — 分类（主食、小吃、饮品...）</li>
 *   <li>imageUrl  — 菜品图片</li>
 *   <li>isAvailable — 是否在售</li>
 *   <li>canteenId — 所属食堂（若有多个食堂）</li>
 * </ul>
 */
@Entity
@Table(name = "food")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Food extends BaseEntity {

    /** 菜品名称，例如"红烧肉"，不允许为空 */
    @Column(nullable = false, length = 100)
    private String name;

    /** 菜品描述（占位符，可选填写） */
    @Column(columnDefinition = "TEXT")
    private String description;

    /** 价格，精确到分 */
    @Column(precision = 8, scale = 2)
    private BigDecimal price;

    /** 菜品图片 URL（占位符） */
    @Column(name = "image_url", length = 500)
    private String imageUrl;

    // ========== 新增字段（与客户端字段对应） ==========

    /** 校区，例如“北校区/南校区” */
    @Column(length = 100)
    private String campus;

    /** 食堂名称 */
    @Column(length = 150)
    private String canteen;

    /** 楼层 */
    @Column(length = 50)
    private String floor;

    /** 窗口编号或名称 */
    @Column(name = "window_name", length = 100)
    private String window;

    /** 售卖时间，简单以字符串保存（比如："07:00-09:30,11:00-13:00"） */
    @Column(name = "sell_time", length = 255)
    private String sellTime;

    /** 标签列表（使用单独的集合表存储） */
    @ElementCollection
    @CollectionTable(name = "food_tags", joinColumns = @JoinColumn(name = "food_id"))
    @Column(name = "tag", length = 50)
    @Builder.Default
    private List<String> tags = new ArrayList<>();

    /** 全局综合评分（0.0 ~ 5.0） */
    @Column(name = "average_rating", precision = 3, scale = 2)
    private Float averageRating = 0f;

    /** 参与评分的总人数 */
    @Column(name = "rating_count")
    private Integer ratingCount = 0;

    // ========== 占位符字段 ==========
    // @Column(length = 50)
    // private String category;   // 分类

    // @Column(name = "is_available")
    // private Boolean isAvailable = true;

    // ========== 关联关系 ==========

    /**
     * 与 Post 的多对多关系（被动方）
     * 由 Post.foods 中的 @JoinTable 维护，这里只需 mappedBy
     */
    @ManyToMany(mappedBy = "foods")
    @Builder.Default
    private Set<Post> posts = new HashSet<>();
}
