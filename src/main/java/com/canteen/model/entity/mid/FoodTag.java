package com.canteen.model.entity.mid;

import com.canteen.model.entity.BaseEntity;
import com.canteen.model.entity.Food;
import com.canteen.model.entity.Tag;
import jakarta.persistence.*;
import lombok.*;

/** Food和Tag的关联实体 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
        name = "food_tag",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"food_id", "tag_id"})
        }
)
public class FoodTag extends BaseEntity
{
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_id", nullable = false)
    private Food food;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id", nullable = false)
    private Tag tag;
}