package com.canteen.model.entity;

import com.canteen.model.entity.mid.FoodPost;
import com.canteen.model.entity.mid.PostType;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "post")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post extends BaseEntity {

    /** 帖子标题 */
    @Column(name = "title", nullable = false, length = 200)
    private String title;

    /** 帖子正文内容 */
    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "like_count")
    private Integer likeCount = 0;   // 点赞数

    @Column(name = "view_count")
    private Integer viewCount = 0;   // 浏览数

    /** 软删除标记，默认为 false */
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;





    // 关联关系

    /**
     * 帖子作者（多对一）
     * 数据库存储 user_id 外键列
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User author;

    /**
     * 帖子涉及的菜品，已添加FoodPost中间实体
     */
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<FoodPost> foodPosts = new ArrayList<>();

    /**
     * 帖子下的评论列表（一对多）
     * orphanRemoval=true：删除帖子时同步删除所有评论
     */
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    /**
     * 帖子的类型，和Type多对多关系，已添加PostType中间实体
     */
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PostType> postTypes = new ArrayList<>();




    // 辅助方法维护双向关系

    /** 添加菜品并同步反向关系 */
    public void addFood(Food food) {
        FoodPost foodPost = new FoodPost(food, this);
        foodPosts.add(foodPost);
        food.getFoodPosts().add(foodPost);
    }

    /** 移除菜品并同步反向关系 */
    public void removeFood(Food food) {
        foodPosts.removeIf(fp -> fp.getPost().equals(this) && fp.getFood().equals(food));
        food.getFoodPosts().removeIf(fp -> fp.getPost().equals(this) && fp.getFood().equals(food));
    }

    /** 添加类型并同步反向关系 */
    public void addType(Type type) {
        PostType postType = new PostType(this, type);
        postTypes.add(postType);
        type.getPostTypes().add(postType);
    }

    /** 移除类型并同步反向关系 */
    public void removeType(Type type) {
        postTypes.removeIf(pt -> pt.getPost().equals(this) && pt.getType().equals(type));
        type.getPostTypes().removeIf(pt -> pt.getPost().equals(this) && pt.getType().equals(type));
    }

    // 像和comment这种一对多关系是不是也要添加辅助方法？感觉不太必要，因为comment持有post_id外键，删除post时会自动删除关联的comment，不需要手动维护双向关系。
    // 那要是想给post添加一个comment呢？感觉也不太麻烦，直接new一个comment对象，设置好author和post字段，然后保存comment就行了，不需要在post里维护一个addComment方法。
    // 我明白了，对于一对多关系，如果多的一方（comment）持有外键，那么在一的一方（post）里就不需要维护一个addComment方法，因为comment对象本身就知道它属于哪个post了。只要在创建comment时正确设置post字段，并保存comment，就能自动建立关联关系，不需要在post里手动维护双向关系。
}
