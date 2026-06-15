package com.canteen.repository;


import com.canteen.model.entity.mid.FoodTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

// FoodTagRepository
@Repository
public interface FoodTagRepository extends JpaRepository<FoodTag, Long> {

    /** 根据LocalDateTime筛选增量更新的列表 */
    @Query("SELECT ft FROM FoodTag ft WHERE ft.updatedAt > :since")
    java.util.List<FoodTag> findUpdatedSince(@Param("since") java.time.LocalDateTime since);
}
