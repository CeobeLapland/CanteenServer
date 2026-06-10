package com.canteen.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "seasoning")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Seasoning extends BaseEntity
{
    /** 名称（可重复） */
    @Column(name = "name", nullable = false, length = 45)
    private String name;

    /** 价格，用INTEGER存储，单位为分 */
    @Column(name = "price")
    private Integer price;

    /** 关联窗口，多对一，window 持有一对多 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "window_id", nullable = false)
    private Window window;

}