package com.canteen.model.entity.mid;

import com.canteen.model.entity.BaseEntity;
import com.canteen.model.entity.Food;
import com.canteen.model.entity.Post;
import jakarta.persistence.*;
import lombok.*;

/** Food和Post的中间实体 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
        name = "food_post",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"food_id", "post_id"})
        }
)
public class FoodPost extends BaseEntity
{
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_id", nullable = false)
    private Food food;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;
}