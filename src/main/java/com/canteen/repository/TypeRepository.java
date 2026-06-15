package com.canteen.repository;

import com.canteen.model.entity.Food;
import com.canteen.model.entity.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

// TypeRepository
@Repository
public interface TypeRepository extends JpaRepository<Type, Long> {
    /** 根据LocalDateTime筛选增量更新的列表 */
    @Query("SELECT t FROM Type t WHERE t.updatedAt > :since")
    java.util.List<Type> findUpdatedSince(@Param("since") java.time.LocalDateTime since);
}
