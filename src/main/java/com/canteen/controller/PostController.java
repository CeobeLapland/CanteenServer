package com.canteen.controller;

import com.canteen.model.dto.Dtos.PostDetailDto;
import com.canteen.model.dto.Dtos.PostSummaryDto;
import com.canteen.model.request.Requests.CreatePostRequest;
import com.canteen.model.request.Requests.UpdatePostRequest;
import com.canteen.model.response.ApiResponse;
import com.canteen.model.response.PageResponse;
import com.canteen.service.Services.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 帖子（点评）相关接口
 * <p>基础路径：/api/v1/posts
 * <table border="1">
 *   <tr><th>方法</th><th>路径</th><th>描述</th></tr>
 *   <tr><td>GET</td><td>/api/v1/posts</td><td>首页 Feed（最新帖子，分页）</td></tr>
 *   <tr><td>GET</td><td>/api/v1/posts/search</td><td>按标题搜索</td></tr>
 *   <tr><td>GET</td><td>/api/v1/posts/{id}</td><td>帖子详情</td></tr>
 *   <tr><td>GET</td><td>/api/v1/posts/food/{foodId}</td><td>某菜品下的帖子</td></tr>
 *   <tr><td>GET</td><td>/api/v1/posts/user/{userId}</td><td>某用户的帖子</td></tr>
 *   <tr><td>POST</td><td>/api/v1/posts</td><td>发布帖子</td></tr>
 *   <tr><td>PUT</td><td>/api/v1/posts/{id}</td><td>更新帖子</td></tr>
 *   <tr><td>DELETE</td><td>/api/v1/posts/{id}</td><td>删除帖子</td></tr>
 * </table>
 */
@RestController
@RequestMapping("/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    /**
     * 获取首页 Feed（最新帖子，按时间倒序，分页）
     * <p>GET /api/v1/posts?page=0&size=10
     */
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<PostSummaryDto>>> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(ApiResponse.ok(postService.getAllPosts(pageable)));
    }

    /**
     * 按标题搜索帖子（分页）
     * <p>GET /api/v1/posts/search?keyword=红烧肉&page=0&size=10
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<PostSummaryDto>>> searchPosts(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(ApiResponse.ok(postService.searchPosts(keyword, pageable)));
    }

    /**
     * 获取帖子详情（含评论列表）
     * <p>GET /api/v1/posts/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PostDetailDto>> getPostById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(postService.getPostById(id)));
    }

    /**
     * 获取某菜品下的所有帖子（分页）
     * <p>GET /api/v1/posts/food/{foodId}?page=0&size=10
     */
    @GetMapping("/food/{foodId}")
    public ResponseEntity<ApiResponse<PageResponse<PostSummaryDto>>> getPostsByFood(
            @PathVariable Long foodId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(ApiResponse.ok(postService.getPostsByFood(foodId, pageable)));
    }

    /**
     * 获取某用户的所有帖子（分页）
     * <p>GET /api/v1/posts/user/{userId}?page=0&size=10
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<PageResponse<PostSummaryDto>>> getPostsByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(ApiResponse.ok(postService.getPostsByUser(userId, pageable)));
    }

    /**
     * 发布帖子
     * <p>POST /api/v1/posts
     * <p>请求体示例：
     * <pre>
     * {
     *   "title": "今天的红烧肉超好吃！",
     *   "content": "肉质软烂，汤汁浓郁，强烈推荐！",
     *   "rating": 5,
     *   "userId": 1,
     *   "foodIds": [1, 2]
     * }
     * </pre>
     */
    @PostMapping
    public ResponseEntity<ApiResponse<PostDetailDto>> createPost(
            @Valid @RequestBody CreatePostRequest request) {
        PostDetailDto created = postService.createPost(request);
        return ResponseEntity.status(201).body(ApiResponse.created(created));
    }

    /**
     * 更新帖子（仅更新提供的字段）
     * <p>PUT /api/v1/posts/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PostDetailDto>> updatePost(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePostRequest request) {
        PostDetailDto updated = postService.updatePost(id, request);
        return ResponseEntity.ok(ApiResponse.ok("更新成功", updated));
    }

    /**
     * 删除帖子
     * <p>DELETE /api/v1/posts/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.ok(ApiResponse.ok());
    }
}
