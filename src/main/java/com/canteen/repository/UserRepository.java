package com.canteen.repository;

import com.canteen.model.entity.Food;
import com.canteen.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /** 根据用户名查找（后续登录功能用） */
    Optional<User> findByName(String name);

    /** 检查用户名是否已存在 */
    boolean existsByName(String name);


    /** 根据LocalDateTime筛选增量更新的列表 */
    @Query("SELECT u FROM User u WHERE u.updatedAt > :since")
    java.util.List<User> findUpdatedSince(@Param("since") java.time.LocalDateTime since);
}