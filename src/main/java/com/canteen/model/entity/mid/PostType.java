package com.canteen.model.entity.mid;

import com.canteen.model.entity.BaseEntity;
import com.canteen.model.entity.Post;
import com.canteen.model.entity.Type;
import jakarta.persistence.*;
import lombok.*;

/** Post和Type的中间实体 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
        name = "post_type",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"post_id", "type_id"})
        }
)
public class PostType extends BaseEntity
{
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id", nullable = false)
    private Type type;
}