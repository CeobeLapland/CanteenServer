package com.canteen.repository;


import com.canteen.model.entity.Window;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// CampusRepository
@Repository
public interface WindowRepository extends JpaRepository<Window, Long> {

    /** 按窗口名称精确查找 */
    Optional<Window> findByName(String name);

    /* 查找不到就添加
    default Window findByNameOrCreate(String name) {
        return findByName(name).orElseGet(() -> {
            Window newWindow = new Window();
            newWindow.setName(name);
            return save(newWindow);
        });
    }*/

    /** 根据LocalDateTime筛选增量更新的列表 */
    @Query("SELECT w FROM Window w WHERE w.updatedAt > :since")
    java.util.List<Window> findUpdatedSince(@Param("since") java.time.LocalDateTime since);
}