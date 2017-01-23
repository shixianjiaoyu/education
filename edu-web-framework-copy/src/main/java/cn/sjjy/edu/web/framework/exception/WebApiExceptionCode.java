package cn.sjjy.edu.web.framework.exception;

import cn.sjjy.edu.common.exception.IExceptionCode;

/** 
 * @author Captain
 * @date 2017年1月21日
 */
public enum  WebApiExceptionCode implements IExceptionCode {

    BAD_REQUEST(400, "请求格式错误"),
    UNAUTHORIZED(401, "TOKEN非法"),
    TOKEN_EXPIRE(402, "TOKEN已过期"),
    NO_PERMISSION(403, "没有权限访问"),
    NOT_FOUND(404, "数据为空"),
    SYSTEM_ERROR(500, "系统异常");
    private String message;
    private Integer code;

    WebApiExceptionCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
