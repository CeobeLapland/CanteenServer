package com.canteen.exception;

import com.canteen.model.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * 全局异常处理器
 *
 * <p>统一捕获 Controller 层抛出的异常，转换为标准 {@link ApiResponse} 格式返回，
 * 避免在每个 Controller 中重复编写 try-catch。
 *
 * <p>{@code @RestControllerAdvice} = {@code @ControllerAdvice} + {@code @ResponseBody}
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ==================== 自定义业务异常 ====================

    /**
     * 处理所有继承自 BusinessException 的自定义异常
     * 例如：ResourceNotFoundException、BadRequestException 等
     */
    @ExceptionHandler(Exceptions.BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(Exceptions.BusinessException ex) {
        log.warn("业务异常: [{}] {}", ex.getCode(), ex.getMessage());
        ApiResponse<Void> response = ApiResponse.fail(ex.getCode(), ex.getMessage());
        return ResponseEntity.status(ex.getHttpStatus()).body(response);
    }

    // ==================== 参数校验异常 ====================

    /**
     * 处理 @Valid 校验失败异常
     * 将所有字段错误信息拼接后返回
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));
        log.warn("参数校验失败: {}", message);
        return ResponseEntity.badRequest()
                .body(ApiResponse.badRequest(message));
    }

    // ==================== 系统级异常 ====================

    /**
     * 兜底处理：捕获所有未被处理的异常
     * 生产环境不应将内部错误信息暴露给客户端
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGenericException(Exception ex) {
        log.error("服务器内部错误", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.serverError("服务器内部错误，请稍后重试"));
    }
}
