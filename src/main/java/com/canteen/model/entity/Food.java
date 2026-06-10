package com.canteen.model.entity;

import com.canteen.model.entity.mid.FoodPost;
import com.canteen.model.entity.mid.FoodTag;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "food")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Food extends BaseEntity {

    /** 菜品名称 */
    @Column(name = "name", nullable = false, length = 45)
    private String name;

    /** 菜品描述 */
    @Column(name = "description", length = 45)
    private String description;

    /** 价格，精确到分 */
    @Column(name = "price")
    private Integer price;

    /** 校区 */
    @Column(name = "campus", length = 45)
    private String campusName;

    /** 食堂名称 */
    @Column(name = "canteen", length = 45)
    private String canteenName;

    /** 楼层 */
    @Column(name = "floor", length = 45)
    private String floorName;



    /** 售卖时间，简单以字符串保存（比如："07:00-09:30,11:00-13:00"） */
    @Column(name = "sell_time", length = 200)
    private String sellTime;

    /** 全局综合评分（0.0 ~ 5.0） */
    @Column(name = "average_rating")//, precision = 3, scale = 2)
    private Float averageRating = 0f;

    /** 参与评分的总人数 */
    @Column(name = "rating_count")
    private Integer ratingCount = 0;







    // 关联关系

    /** 窗口编号或名称，多对一关系，food持有window_id外键 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "window_id")
    private Window window;



    /**
     * 与 Post 的多对多关系，已添加 FoodPost 中间实体，维护关系由Post负责
     */
    @OneToMany(
            mappedBy = "food",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<FoodPost> foodPosts = new ArrayList<>();

    /**
     * 与Tag的多对多关系，已添加FoodTag中间实体
     */
    @OneToMany(
            mappedBy = "food",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<FoodTag> foodTags = new ArrayList<>();



    // 辅助方法维护双向关系
    /** 添加标签并同步反向关系 */
    public void addTag(Tag tag) {
        FoodTag foodTag = new FoodTag(this, tag);
        foodTags.add(foodTag);
        tag.getFoodTags().add(foodTag);
    }

    /** 移除标签并同步反向关系 */
    public void removeTag(Tag tag) {
        foodTags.removeIf(ft -> ft.getFood().equals(this) && ft.getTag().equals(tag));
        tag.getFoodTags().removeIf(ft -> ft.getFood().equals(this) && ft.getTag().equals(tag));
    }
}
