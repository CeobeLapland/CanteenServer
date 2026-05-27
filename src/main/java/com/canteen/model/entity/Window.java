package com.canteen.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "food")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Window extends BaseEntity {

    /** 窗口编号或名称 */
    @Column (name = "name", nullable = false, length = 45)
    private String name;

    /** 校区 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campus_id", nullable = false)
    private String campus;

    /** 食堂名称 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "canteen_id", nullable = false)
    private String canteen;

    /** 楼层 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "floor_id", nullable = false)
    private String floor;

}