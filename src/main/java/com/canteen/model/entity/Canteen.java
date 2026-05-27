package com.canteen.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "canteen")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Canteen extends BaseEntity {

    /**
     * 食堂名称
     */
    @Column(name = "name", nullable = false, length = 45)
    private String name;

    /**
     * 所属校区
     */
    @ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @JoinColumn(name = "campus_id", nullable = false)
    private Campus campus;
}