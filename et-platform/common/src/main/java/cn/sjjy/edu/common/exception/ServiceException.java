package cn.sjjy.edu.common.exception;

/** 
 * @author Captain
 * @date 2017年1月23日
 */
public class ServiceException extends Exception {
    private Integer code;

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(IExceptionCode exceptionCode) {
        this(exceptionCode.getCode(), exceptionCode.getMessage());
    }

    public ServiceException(Integer code, String message) {
        this(message);
        setCode(code);
    }

    public ServiceException(Integer code, String message, Throwable cause) {
        super(message, cause);
        setCode(code);
    }

    public ServiceException(Integer code, Throwable cause) {
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
