package cn.sjjy.edu.manager.portal.webapi.exception;

import cn.sjjy.edu.common.exception.IExceptionCode;

/** 
 * @author Captain
 * @date 2017年1月22日
 */
public enum ApiExceptionCode implements IExceptionCode {
    UNAUTHORIZED(401, "令牌错误"),
    BAD_REQUEST(406, "请求格式错误"),
    PARAM_NOT_NULL(407, "参数非空"),
    REQUEST_TIME_OUT(408, "请求过期"),
    SYSTEM_ERROR(500, "系统异常");
    private String message;
    private Integer code;

    ApiExceptionCode(Integer code, String message) {
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
