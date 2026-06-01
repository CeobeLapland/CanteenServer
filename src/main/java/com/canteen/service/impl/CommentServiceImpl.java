package com.canteen.service.impl;

import com.canteen.exception.Exceptions.*;
import com.canteen.mapper.Mappers.CommentMapper;
import com.canteen.model.dto.Dtos.CommentDto;
import com.canteen.model.entity.Comment;
import com.canteen.model.entity.Post;
import com.canteen.model.entity.User;
import com.canteen.model.request.Requests.CreateCommentRequest;
import com.canteen.model.response.PageResponse;
import com.canteen.repository.CommentRepository;
import com.canteen.repository.PostRepository;
import com.canteen.repository.UserRepository;
import com.canteen.service.Services.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * CommentService 实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;

    @Override
    @Transactional(readOnly = true)
    public PageResponse<CommentDto> getCommentsByPost(Long postId, Pageable pageable) {
        // 确认帖子存在
        if (!postRepository.existsById(postId)) {
            throw new ResourceNotFoundException("帖子", postId);
        }
        Page<CommentDto> page = commentRepository
                .findByPostIdOrderByCreatedAtAsc(postId, pageable)
                .map(commentMapper::toDto);
        return PageResponse.of(page);
    }

    @Override
    @Transactional
    public CommentDto createComment(Long postId, CreateCommentRequest request) {
        // 1. 校验帖子存在
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("帖子", postId));

        // 2. 校验用户存在
        User author = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("用户", request.getUserId()));

        // 3. 构建并保存评论
        Comment comment = Comment.builder()
                .content(request.getContent())
                .post(post)
                .author(author)
                .build();

        Comment saved = commentRepository.save(comment);
        log.info("评论发布成功: id={}, postId={}, userId={}",
                saved.getId(), postId, request.getUserId());
        return commentMapper.toDto(saved);
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("评论", commentId));

        // TODO: 权限校验（仅作者或管理员可删除）
        // if (!comment.getAuthor().getId().equals(currentUserId)) {
        //     throw new ForbiddenException("无权删除他人评论");
        // }

        commentRepository.delete(comment);
        log.info("评论删除成功: id={}", commentId);
    }
}
