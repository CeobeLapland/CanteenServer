package com.canteen.service.impl;

import com.canteen.exception.Exceptions.*;
import com.canteen.mapper.Mappers.PostMapper;
import com.canteen.model.dto.Dtos.PostDetailDto;
import com.canteen.model.dto.Dtos.PostSummaryDto;
import com.canteen.model.entity.Food;
import com.canteen.model.entity.Post;
import com.canteen.model.entity.User;
import com.canteen.model.request.Requests.CreatePostRequest;
import com.canteen.model.request.Requests.UpdatePostRequest;
import com.canteen.model.response.PageResponse;
import com.canteen.repository.Repositories.FoodRepository;
import com.canteen.repository.Repositories.PostRepository;
import com.canteen.repository.Repositories.UserRepository;
import com.canteen.service.Services.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * PostService 实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final FoodRepository foodRepository;
    private final PostMapper postMapper;

    @Override
    @Transactional(readOnly = true)
    public PostDetailDto getPostById(Long id) {
        Post post = findPostOrThrow(id);
        return postMapper.toDetailDto(post);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<PostSummaryDto> getAllPosts(Pageable pageable) {
        Page<PostSummaryDto> page = postRepository
                .findAllByOrderByCreatedAtDesc(pageable)
                .map(this::toSummaryWithCommentCount);
        return PageResponse.of(page);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<PostSummaryDto> getPostsByFood(Long foodId, Pageable pageable) {
        // 先确认菜品存在
        if (!foodRepository.existsById(foodId)) {
            throw new ResourceNotFoundException("菜品", foodId);
        }
        Page<PostSummaryDto> page = postRepository
                .findByFoodId(foodId, pageable)
                .map(this::toSummaryWithCommentCount);
        return PageResponse.of(page);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<PostSummaryDto> getPostsByUser(Long userId, Pageable pageable) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("用户", userId);
        }
        Page<PostSummaryDto> page = postRepository
                .findByAuthorId(userId, pageable)
                .map(this::toSummaryWithCommentCount);
        return PageResponse.of(page);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<PostSummaryDto> searchPosts(String keyword, Pageable pageable) {
        Page<PostSummaryDto> page = postRepository
                .findByTitleContainingIgnoreCase(keyword, pageable)
                .map(this::toSummaryWithCommentCount);
        return PageResponse.of(page);
    }

    @Override
    @Transactional
    public PostDetailDto createPost(CreatePostRequest request) {
        // 1. 校验并加载作者
        User author = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("用户", request.getUserId()));

        // 2. 校验并加载关联菜品
        Set<Food> foods = loadFoodsOrThrow(request.getFoodIds());

        // 3. 构建 Post 实体
        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .viewCount(request.getViewCount())
                .likeCount(request.getLikeCount())
                .author(author)
                .build();

        // 4. 使用辅助方法同步双向关系
        foods.forEach(post::addFood);

        Post saved = postRepository.save(post);
        log.info("帖子发布成功: id={}, title={}", saved.getId(), saved.getTitle());
        return postMapper.toDetailDto(saved);
    }

    @Override
    @Transactional
    public PostDetailDto updatePost(Long id, UpdatePostRequest request) {
        Post post = findPostOrThrow(id);

        // 仅更新请求中不为空的字段
        if (request.getTitle() != null)   post.setTitle(request.getTitle());
        if (request.getContent() != null) post.setContent(request.getContent());
        if (request.getViewCount() != null) post.setViewCount(request.getViewCount());
        if (request.getLikeCount() != null) post.setLikeCount(request.getLikeCount());

        // 更新关联菜品（若请求中包含）
        if (request.getFoodIds() != null && !request.getFoodIds().isEmpty()) {
            // 先清空原有关联，再重新绑定
            new java.util.HashSet<>(post.getFoods()).forEach(post::removeFood);
            Set<Food> newFoods = loadFoodsOrThrow(request.getFoodIds());
            newFoods.forEach(post::addFood);
        }

        Post saved = postRepository.save(post);
        log.info("帖子更新成功: id={}", saved.getId());
        return postMapper.toDetailDto(saved);
    }

    @Override
    @Transactional
    public void deletePost(Long id) {
        Post post = findPostOrThrow(id);
        // 解除与菜品的多对多关联（避免关联表残留）
        new java.util.HashSet<>(post.getFoods()).forEach(post::removeFood);
        postRepository.delete(post);
        log.info("帖子删除成功: id={}", id);
    }

    // ==================== 内部工具方法 ====================

    private Post findPostOrThrow(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("帖子", id));
    }

    /**
     * 批量加载菜品，若任意一个 ID 不存在则抛出异常
     */
    private Set<Food> loadFoodsOrThrow(Set<Long> foodIds) {
        Set<Food> foods = foodIds.stream()
                .map(fid -> foodRepository.findById(fid)
                        .orElseThrow(() -> new ResourceNotFoundException("菜品", fid)))
                .collect(Collectors.toSet());
        return foods;
    }

    /**
     * 将 Post 转为 Summary DTO 并填充评论数
     * 使用 COUNT 查询，不加载评论列表，避免 N+1
     */
    private PostSummaryDto toSummaryWithCommentCount(Post post) {
        PostSummaryDto dto = postMapper.toSummaryDto(post);
        dto.setCommentCount((int) postRepository.countCommentsByPostId(post.getId()));
        return dto;
    }
}
