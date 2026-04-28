package com.canteen.controller;

import com.canteen.model.dto.Dtos.UserDto;
import com.canteen.model.request.Requests.CreateUserRequest;
import com.canteen.model.response.ApiResponse;
import com.canteen.service.Services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 用户相关接口
 *
 * <p>基础路径：/api/v1/users
 *
 * <table border="1">
 *   <tr><th>方法</th><th>路径</th><th>描述</th></tr>
 *   <tr><td>GET</td><td>/api/v1/users/{id}</td><td>获取用户信息</td></tr>
 *   <tr><td>POST</td><td>/api/v1/users</td><td>注册新用户</td></tr>
 * </table>
 */
@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 获取用户信息
     *
     * <p>GET /api/v1/users/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDto>> getUserById(@PathVariable Long id) {
        UserDto user = userService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.ok(user));
    }

    /**
     * 注册新用户
     *
     * <p>POST /api/v1/users
     * <p>请求体示例：
     * <pre>
     * { "name": "小明" }
     * </pre>
     */
    @PostMapping
    public ResponseEntity<ApiResponse<UserDto>> createUser(
            @Valid @RequestBody CreateUserRequest request) {
        UserDto created = userService.createUser(request);
        return ResponseEntity.status(201).body(ApiResponse.created(created));
    }

    // TODO: PUT /api/v1/users/{id}  更新用户信息
    // TODO: DELETE /api/v1/users/{id} 注销用户
}
