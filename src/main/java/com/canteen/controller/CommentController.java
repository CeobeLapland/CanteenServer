package com.canteen.controller;

import com.canteen.model.dto.Dtos.CommentDto;
import com.canteen.model.request.Requests.CreateCommentRequest;
import com.canteen.model.response.ApiResponse;
import com.canteen.model.response.PageResponse;
import com.canteen.service.Services.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 评论相关接口
 *
 * <p>评论属于帖子的子资源，路径设计为嵌套风格：
 *
 * <table border="1">
 *   <tr><th>方法</th><th>路径</th><th>描述</th></tr>
 *   <tr><td>GET</td><td>/api/v1/posts/{postId}/comments</td><td>获取帖子评论列表</td></tr>
 *   <tr><td>POST</td><td>/api/v1/posts/{postId}/comments</td><td>发表评论</td></tr>
 *   <tr><td>DELETE</td><td>/api/v1/comments/{id}</td><td>删除评论</td></tr>
 * </table>
 *
 * <p>注意：删除接口不依赖 postId，单独挂在 /api/v1/comments 下，更简洁。
 */
@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    /**
     * 获取某帖子下的评论列表（分页，按时间升序）
     *
     * <p>GET /api/v1/posts/{postId}/comments?page=0&size=20
     */
    @GetMapping("/v1/posts/{postId}/comments")
    public ResponseEntity<ApiResponse<PageResponse<CommentDto>>> getCommentsByPost(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        PageResponse<CommentDto> result = commentService.getCommentsByPost(postId, pageable);
        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    /**
     * 在指定帖子下发表评论
     *
     * <p>POST /api/v1/posts/{postId}/comments
     * <p>请求体示例：
     * <pre>
     * {
     *   "content": "确实好吃，下次还来！",
     *   "userId": 2
     * }
     * </pre>
     */
    @PostMapping("/v1/posts/{postId}/comments")
    public ResponseEntity<ApiResponse<CommentDto>> createComment(
            @PathVariable Long postId,
            @Valid @RequestBody CreateCommentRequest request) {
        CommentDto created = commentService.createComment(postId, request);
        return ResponseEntity.status(201).body(ApiResponse.created(created));
    }

    /**
     * 删除评论
     *
     * <p>DELETE /api/v1/comments/{id}
     * <p>TODO: 后续引入认证后，需校验当前用户是否为评论作者或管理员
     */
    @DeleteMapping("/v1/comments/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.ok(ApiResponse.ok());
    }
}
