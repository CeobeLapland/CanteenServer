package com.canteen.model.entity;

import com.canteen.model.entity.mid.PostType;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/** 这个是帖子的类型，和Post多对多关系，维护关联表post_type */
@Entity
@Table(name = "type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Type extends BaseEntity
{
    /** 类型名称 */
    @Column(name = "name", nullable = false, length = 45)
    private String name;

    /** 和帖子多对多，已经添加了中间实体PostType */
    @OneToMany(
            mappedBy = "type",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<PostType> postTypes = new ArrayList<>();
}
