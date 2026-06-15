package com.canteen.repository;

import com.canteen.model.entity.Food;
import com.canteen.model.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// TagRepository
@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findByName(String tagName);

    /** 根据LocalDateTime筛选增量更新的列表 */
    @Query("SELECT t FROM Tag t WHERE t.updatedAt > :since")
    java.util.List<Tag> findUpdatedSince(@Param("since") java.time.LocalDateTime since);
}