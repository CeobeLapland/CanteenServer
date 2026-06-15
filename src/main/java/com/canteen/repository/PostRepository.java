package com.canteen.repository;

import com.canteen.model.entity.Food;
import com.canteen.model.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    /** 按作者查询帖子列表（分页） */
    Page<Post> findByAuthorId(Long userId, Pageable pageable);

    /**
     * 按关联菜品查询帖子（分页）
     * 现在已经改了，Post里只有List<FoodPost> foodPosts，没有直接关联Food了，所以需要写JPQL查询
     */
    @Query("SELECT p FROM Post p JOIN p.foodPosts fp WHERE fp.food.id = :foodId")
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


    /** 根据LocalDateTime筛选增量更新的列表 */
    @Query("SELECT p FROM Post p WHERE p.updatedAt > :since")
    List<Post> findUpdatedSince(@Param("since") LocalDateTime since);
}