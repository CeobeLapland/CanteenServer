package com.canteen.repository;

import com.canteen.model.entity.Comment;
import com.canteen.model.entity.Food;
import com.canteen.model.entity.Post;
import com.canteen.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 数据访问层（Repository）
 * <p>所有 Repository 集中在此文件，后续可拆分为独立接口文件。
 * Spring Data JPA 会自动实现接口方法，无需手写 SQL（基础 CRUD）。
 * 复杂查询使用 {@code @Query} 编写 JPQL 或原生 SQL。
 */
public class Repositories {


    // ======================================================
    //  UserRepository
    // ======================================================
    @Repository
    public interface UserRepository extends JpaRepository<User, Long> {

        /** 根据用户名查找（后续登录功能用） */
        Optional<User> findByName(String name);

        /** 检查用户名是否已存在 */
        boolean existsByName(String name);

    }


    // ======================================================
    //  FoodRepository
    // ======================================================
    @Repository
    public interface FoodRepository extends JpaRepository<Food, Long> {

        /** 按菜名模糊搜索（分页） */
        Page<Food> findByNameContainingIgnoreCase(String keyword, Pageable pageable);

        /** 按菜名精确查找 */
        Optional<Food> findByName(String name);

    }


    // ======================================================
    //  PostRepository
    // ======================================================
    @Repository
    public interface PostRepository extends JpaRepository<Post, Long> {

        /** 按作者查询帖子列表（分页） */
        Page<Post> findByAuthorId(Long userId, Pageable pageable);

        /**
         * 按关联菜品查询帖子（分页）
         * 使用 JPQL：foods 是 Post 中的集合字段名
         */
        @Query("SELECT p FROM Post p JOIN p.foods f WHERE f.id = :foodId")
        Page<Post> findByFoodId(@Param("foodId") Long foodId, Pageable pageable);

        /** 按标题模糊搜索（分页） */
        Page<Post> findByTitleContainingIgnoreCase(String keyword, Pageable pageable);

        /** 查询最新帖子（首页 Feed） */
        Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);

        /**
         * 统计某帖子的评论数
         * 比加载整个评论列表再 size() 更高效
         */
        @Query("SELECT COUNT(c) FROM Comment c WHERE c.post.id = :postId")
        long countCommentsByPostId(@Param("postId") Long postId);
    }


    // ======================================================
    //  CommentRepository
    // ======================================================
    @Repository
    public interface CommentRepository extends JpaRepository<Comment, Long> {

        /** 按帖子查询评论列表（分页，按时间升序） */
        Page<Comment> findByPostIdOrderByCreatedAtAsc(Long postId, Pageable pageable);

        /** 按用户查询其所有评论（分页） */
        Page<Comment> findByAuthorId(Long userId, Pageable pageable);

        /** 统计帖子评论数 */
        long countByPostId(Long postId);

        /** 删除某帖子下所有评论（PostService 删帖时调用） */
        void deleteByPostId(Long postId);
    }
}
