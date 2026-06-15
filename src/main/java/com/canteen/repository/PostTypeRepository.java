package com.canteen.repository;

import com.canteen.model.entity.Food;
import com.canteen.model.entity.mid.PostType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

// PostTypeRepository
@Repository
public interface PostTypeRepository extends JpaRepository<PostType, Long> {
    /** 根据LocalDateTime筛选增量更新的列表 */
    @Query("SELECT pt FROM PostType pt WHERE pt.updatedAt > :since")
    java.util.List<PostType> findUpdatedSince(@Param("since") LocalDateTime since);
}
