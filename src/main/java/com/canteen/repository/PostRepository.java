package com.canteen.repository;

import com.canteen.model.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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