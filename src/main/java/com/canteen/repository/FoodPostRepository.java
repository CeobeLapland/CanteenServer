package com.canteen.repository;

import com.canteen.model.entity.mid.FoodPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

// FoodPostRepository
@Repository
public interface FoodPostRepository extends JpaRepository<FoodPost, Long> {

    /** 根据LocalDateTime筛选增量更新的列表 */
    @Query("SELECT fp FROM FoodPost fp WHERE fp.updatedAt > :since")
    List<FoodPost> findUpdatedSince(@Param("since") LocalDateTime since);
}
