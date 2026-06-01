package com.canteen.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "floor")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Floor extends BaseEntity {

    /** 楼层名称，不允许为空 */
    @Column(name = "name", nullable = false, length = 45)
    private String name;

     /** 校区 */
     @ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
     @JoinColumn(name = "campus_id", nullable = false)
    private Campus campus;

    /** 食堂名称 */
    @ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @JoinColumn(name = "canteen_id", nullable = false)
    private Canteen canteen;
}