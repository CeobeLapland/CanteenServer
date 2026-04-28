package com.canteen.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 自定义异常体系
 *
 * <p>统一异常层次结构，配合 {@link GlobalExceptionHandler} 处理并返回规范的错误响应。
 */
public class Exceptions {

    // ======================================================
    //  基础业务异常
    // ======================================================

    /**
     * 业务异常基类
     * 所有自定义业务异常均继承此类
     */
    @Getter
    public static class BusinessException extends RuntimeException {

        private final int code;
        private final HttpStatus httpStatus;

        public BusinessException(int code, HttpStatus httpStatus, String message) {
            super(message);
            this.code = code;
            this.httpStatus = httpStatus;
        }
    }

    // ======================================================
    //  具体业务异常
    // ======================================================

    /**
     * 资源不存在异常（HTTP 404）
     * 示例：根据 ID 查找食物但不存在时抛出
     */
    public static class ResourceNotFoundException extends BusinessException {

        public ResourceNotFoundException(String resourceName, Long id) {
            super(404, HttpStatus.NOT_FOUND,
                    String.format("%s (id=%d) 不存在", resourceName, id));
        }

        public ResourceNotFoundException(String message) {
            super(404, HttpStatus.NOT_FOUND, message);
        }
    }

    /**
     * 请求参数非法异常（HTTP 400）
     * 示例：传入了不合法的参数组合
     */
    public static class BadRequestException extends BusinessException {

        public BadRequestException(String message) {
            super(400, HttpStatus.BAD_REQUEST, message);
        }
    }

    /**
     * 资源已存在异常（HTTP 409）
     * 示例：注册时用户名已被占用
     */
    public static class ResourceAlreadyExistsException extends BusinessException {

        public ResourceAlreadyExistsException(String message) {
            super(409, HttpStatus.CONFLICT, message);
        }
    }

    /**
     * 权限不足异常（HTTP 403）
     * 示例：用户尝试删除他人帖子
     * TODO: 待引入认证模块后完善
     */
    public static class ForbiddenException extends BusinessException {

        public ForbiddenException(String message) {
            super(403, HttpStatus.FORBIDDEN, message);
        }
    }
}
