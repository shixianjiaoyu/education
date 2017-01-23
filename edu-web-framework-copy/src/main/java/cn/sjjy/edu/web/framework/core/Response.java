package cn.sjjy.edu.web.framework.core;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import cn.sjjy.edu.common.exception.IExceptionCode;

/** 
 * @author Captain
 * @date 2017年1月21日
 * @param <E>
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Response<E> {
    public String status;
    public E result;
    public Integer error_code;
    public String error_msg;

    private Response(String status, E result, Integer error_code, String error_msg) {
        this.status = status;
        this.result = result;
        this.error_code = error_code;
        this.error_msg = error_msg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public E getResult() {
        return result;
    }

    public void setResult(E result) {
        this.result = result;
    }

    public Integer getError_code() {
        return error_code;
    }

    public void setError_code(Integer error_code) {
        this.error_code = error_code;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    public final static <E> Response<E> SUCCESS(E result) {
        return new Response("success", result, null, null);
    }

    public final static <E> Response<E> FAIL(Integer error_code, String error_msg) {
        return new Response("fail", null, error_code, error_msg);
    }

    public final static <E> Response<E> FAIL(IExceptionCode exceptionCode) {
        return new Response("fail", null, exceptionCode.getCode(), exceptionCode.getMessage());
    }

    @Override
    public String toString() {
        return "Response{" +
                "status='" + status + '\'' +
                ", result=" + result +
                ", error_code=" + error_code +
                ", error_msg='" + error_msg + '\'' +
                '}';
    }
}


