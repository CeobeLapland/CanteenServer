package com.canteen.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "window")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Window extends BaseEntity {

    /** 窗口编号或名称 */
    @Column (name = "name", nullable = false, length = 45)
    private String name;

    /** 食堂名称 */
    @Column(name = "canteen_name", length = 45)
    private String canteenName;

    /** 校区名称 */
    @Column(name = "campus_name", length = 45)
    private String campusName;

    /** 楼层信息 */
    @Column(name = "floor_name", length = 45)
    private String floorName;



    /** 当前窗口的食物列表，一对多，food持有window_id */
    @OneToMany(mappedBy = "window", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Food> foods = new ArrayList<>();

    /** 窗口处的调味品（可重复一对多），seasoning负责持有window_id外键 */
    // 可重复一对多和不可重复一对多注解的区别在于，前者不需要在Seasoning实体中添加唯一约束，而后者需要在Seasoning实体中添加唯一约束（例如在window_id列上添加unique=true）。这里我们选择可重复一对多，因此不需要添加唯一约束。
    @OneToMany(mappedBy = "window", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Seasoning> seasonings = new ArrayList<>();
}