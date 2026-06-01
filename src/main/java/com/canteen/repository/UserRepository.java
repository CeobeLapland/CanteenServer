package com.canteen.repository;

import com.canteen.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /** 根据用户名查找（后续登录功能用） */
    Optional<User> findByName(String name);

    /** 检查用户名是否已存在 */
    boolean existsByName(String name);

}