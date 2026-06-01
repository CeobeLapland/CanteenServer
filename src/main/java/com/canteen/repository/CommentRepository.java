package com.canteen.repository;

import com.canteen.model.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

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