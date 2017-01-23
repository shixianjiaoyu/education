package cn.sjjy.edu.account.exception;

import cn.sjjy.edu.common.exception.IExceptionCode;

/** 
 * @author Captain
 * @date 2017年1月21日
 */
public enum AccountExceptionCode implements IExceptionCode {
    SYSTEM_ERROR(10000, "系统异常"),
    REQUIRED_PARAM(10001, "参数非空"),
    ILLEGAL_PARAM(10002, "参数不合法"),
    NOT_EXIST_MOBILE(10003, "手机号不存在"),
    ILLEGAL_PASSWORD(10004, "密码不合法"),
    ILLEGAL_MOBILE(10005, "电话号码格式不对"),
    NOT_EXIST_ACCOUNT(10006, "账户不存在"),
    INCORRECT_PASSWORD(10007, "密码不正确"),
    ILLEGAL_CONFIRM_PASSWORD(10008, "密码与确认密码不同"),
    FAIL_OPERATION(10009, "操作失败"),
    NOT_ENOUGH_MONEY(10010, "金额不足"),
    ILLEGAL_CUSTOMER_ID(10011, "customer id 不合法"),
    EXIST_USERNAME(10012, "用户名已存在"),
    EXIST_MOBILE(10013, "手机号已存在"),
    EXIST_CRM_USER(10014, "该用户已注册"),
    USER_IS_DISABLE(10015, "用户已停用"),
    HAS_CREATIVE_ORDER(10016, "尚有订单未完成"),

    USER_CAN_NOT_ENABLE(10025,"该状态用户不能启用"),
    USER_CAN_NOT_DISABLE(10026,"该状态用户不能启用");

    private String message;
    private Integer code;

    AccountExceptionCode(Integer code, String message) {
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
