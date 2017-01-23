package cn.sjjy.edu.auth.exception;

import cn.sjjy.edu.common.exception.IExceptionCode;

/** 
 * @author Captain
 * @date 2017年1月22日
 */
public enum AuthExceptionCode implements IExceptionCode {
    SYSTEM_ERROR(20000, "系统异常"),
    REQUIRED_PARAM(20001, "参数非空"),
    ILLEGAL_PARAM(20002, "参数不合法"),
    
    NOT_EXIST(20100, "角色或权限不存在");

    private String message;
    private Integer code;

    AuthExceptionCode(Integer code, String message) {
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
