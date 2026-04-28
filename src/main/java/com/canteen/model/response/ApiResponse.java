package com.canteen.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 统一 API 响应包装类
 *
 * <p>所有接口返回值均包装为此格式，便于 Android 端统一解析：
 * <pre>
 * {
 *   "success": true,
 *   "code": 200,
 *   "message": "操作成功",
 *   "data": { ... },
 *   "timestamp": "2025-01-01 12:00:00"
 * }
 * </pre>
 *
 * @param <T> 业务数据类型
 */
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private final boolean success;
    private final int code;
    private final String message;
    private final T data;
    private final LocalDateTime timestamp;

    private ApiResponse(boolean success, int code, String message, T data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    // ==================== 成功响应工厂方法 ====================

    /** 操作成功，有返回数据 */
    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(true, 200, "操作成功", data);
    }

    /** 操作成功，自定义消息 */
    public static <T> ApiResponse<T> ok(String message, T data) {
        return new ApiResponse<>(true, 200, message, data);
    }

    /** 操作成功，无返回数据（如删除） */
    public static <T> ApiResponse<T> ok() {
        return new ApiResponse<>(true, 200, "操作成功", null);
    }

    /** 创建成功（HTTP 201） */
    public static <T> ApiResponse<T> created(T data) {
        return new ApiResponse<>(true, 201, "创建成功", data);
    }

    // ==================== 失败响应工厂方法 ====================

    /** 通用失败 */
    public static <T> ApiResponse<T> fail(int code, String message) {
        return new ApiResponse<>(false, code, message, null);
    }

    /** 400 参数错误 */
    public static <T> ApiResponse<T> badRequest(String message) {
        return new ApiResponse<>(false, 400, message, null);
    }

    /** 404 资源不存在 */
    public static <T> ApiResponse<T> notFound(String message) {
        return new ApiResponse<>(false, 404, message, null);
    }

    /** 500 服务器错误 */
    public static <T> ApiResponse<T> serverError(String message) {
        return new ApiResponse<>(false, 500, message, null);
    }
}
