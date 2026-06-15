package com.canteen.repository;

import com.canteen.model.entity.Food;
import com.canteen.model.entity.Seasoning;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

// SeasoningRepository
@Repository
public interface SeasoningRepository extends JpaRepository<Seasoning, Long> {
    /** 根据LocalDateTime筛选增量更新的列表 */
    @Query("SELECT s FROM Seasoning s WHERE s.updatedAt > :since")
    java.util.List<Seasoning> findUpdatedSince(@Param("since") java.time.LocalDateTime since);
}
