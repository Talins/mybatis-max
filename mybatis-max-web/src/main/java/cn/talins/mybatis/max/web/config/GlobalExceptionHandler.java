package cn.talins.mybatis.max.web.config;

import cn.hutool.log.StaticLog;
import cn.talins.mybatis.max.api.pojo.Result;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

/**
 * 全局异常处理器 - 统一处理Controller层抛出的异常
 * <p>
 * 该类使用Spring的{@code @RestControllerAdvice}注解，
 * 拦截所有Controller抛出的异常，并返回统一格式的错误响应。
 * </p>
 * 
 * <p>
 * 错误响应格式：
 * <pre>
 * {
 *     "code": 500,
 *     "message": "错误信息",
 *     "data": null
 * }
 * </pre>
 * </p>
 * 
 * <p>
 * 支持的异常类型：
 * <ul>
 *     <li>MethodArgumentNotValidException: @RequestBody参数校验失败</li>
 *     <li>ConstraintViolationException: @PathVariable/@RequestParam参数校验失败</li>
 *     <li>BindException: 表单参数绑定校验失败</li>
 *     <li>Exception: 其他未捕获的异常</li>
 * </ul>
 * </p>
 * 
 * @author talins
 * @see Result 统一响应格式
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理 @RequestBody 参数校验失败异常
     * <p>
     * 当使用 @Validated 或 @Valid 注解校验 @RequestBody 参数失败时抛出。
     * </p>
     * 
     * @param e 异常对象
     * @return 错误响应
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));
        StaticLog.warn("参数校验失败: {}", message);
        return Result.error(message);
    }

    /**
     * 处理 @PathVariable 和 @RequestParam 参数校验失败异常
     * <p>
     * 当使用 @Validated 注解校验路径变量或请求参数失败时抛出。
     * </p>
     * 
     * @param e 异常对象
     * @return 错误响应
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> handleConstraintViolationException(ConstraintViolationException e) {
        String message = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining("; "));
        StaticLog.warn("参数校验失败: {}", message);
        return Result.error(message);
    }

    /**
     * 处理表单参数绑定校验失败异常
     * <p>
     * 当使用 @Validated 注解校验表单参数失败时抛出。
     * </p>
     * 
     * @param e 异常对象
     * @return 错误响应
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> handleBindException(BindException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));
        StaticLog.warn("参数绑定失败: {}", message);
        return Result.error(message);
    }

    /**
     * 处理所有未捕获的异常
     * <p>
     * 记录异常日志并返回统一的错误响应。
     * </p>
     * 
     * @param e 异常对象
     * @return 错误响应
     */
    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e) {
        // 记录异常日志
        StaticLog.error(e);
        // 返回错误响应
        return Result.error(e.getMessage());
    }
}
