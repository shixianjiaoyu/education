package cn.sjjy.edu.common.exception;

/** 
 * @author Captain
 * @date 2017年1月23日
 */
public class SystemException extends Exception {
    private Integer code;

    public SystemException(String message) {
        super(message);
    }

    public SystemException(IExceptionCode exceptionCode) {
        this(exceptionCode.getCode(), exceptionCode.getMessage());
    }

    public SystemException(Integer code, String message) {
        this(message);
        setCode(code);
    }

    public SystemException(Integer code, String message, Throwable cause) {
        super(message, cause);
        setCode(code);
    }

    public SystemException(Integer code, Throwable cause) {
        super(cause);
        setCode(code);
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
