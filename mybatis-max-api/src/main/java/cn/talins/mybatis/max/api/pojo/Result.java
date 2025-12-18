package cn.talins.mybatis.max.api.pojo;

import cn.hutool.http.HttpStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 通用结果包装类 - 用于封装API响应结果
 * <p>
 * 该类是框架统一的API响应格式，所有REST接口都使用此类包装返回结果。
 * </p>
 * 
 * <p>
 * 成功响应示例：
 * <pre>
 * {
 *     "code": 200,
 *     "message": "success",
 *     "data": {"id": 1, "name": "张三"}
 * }
 * </pre>
 * </p>
 * 
 * <p>
 * 失败响应示例：
 * <pre>
 * {
 *     "code": 500,
 *     "message": "系统异常",
 *     "data": null
 * }
 * </pre>
 * </p>
 * 
 * @param <T> 数据类型
 * @author talins
 * @see PageResult 分页结果包装类
 */
@Data
@AllArgsConstructor
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 响应状态码
     * <p>
     * 200表示成功，500表示服务器错误，其他值可自定义。
     * </p>
     */
    private int code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 创建成功响应
     * 
     * @param data 响应数据
     * @param <T> 数据类型
     * @return 成功的Result对象
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(HttpStatus.HTTP_OK, "success", data);
    }

    /**
     * 创建失败响应
     * 
     * @param message 错误消息
     * @param <T> 数据类型
     * @return 失败的Result对象
     */
    public static <T> Result<T> error(String message) {
        return new Result<>(HttpStatus.HTTP_INTERNAL_ERROR, message, null);
    }
}
