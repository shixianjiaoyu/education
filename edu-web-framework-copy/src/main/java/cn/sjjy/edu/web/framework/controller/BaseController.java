package cn.sjjy.edu.web.framework.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.sjjy.edu.common.bean.Operator;
import cn.sjjy.edu.common.enums.OperatorType;
import cn.sjjy.edu.common.exception.ServiceException;
import cn.sjjy.edu.web.framework.core.Context;
import cn.sjjy.edu.web.framework.core.Response;

/**
 * @author user
 *
 */
@Controller
public class BaseController {
    private final static Logger LOGGER = LoggerFactory.getLogger(BaseController.class);

    @Autowired
    private OperatorType operatorType;

    @ExceptionHandler(Exception.class)
    public
    @ResponseBody
    Response<String> handleException(HttpServletRequest req, Exception exception) {
        LOGGER.error("handleException.error", exception);
        return Response.FAIL(500, exception.getMessage());
    }

    @ExceptionHandler(ServiceException.class)
    public
    @ResponseBody
    Response<String> handleUserException(HttpServletRequest req, ServiceException exception) {
        LOGGER.error("handleUserException.error", exception);
        return Response.FAIL(exception.getCode(), exception.getMessage());
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public
    @ResponseBody
    Response<String> handleIllegalArgumentException(HttpServletRequest req, IllegalArgumentException exception) {
        LOGGER.error("handleIllegalArgumentException.error", exception);
        return Response.FAIL(500, exception.getMessage());
    }

    @ExceptionHandler({RuntimeException.class})
    public
    @ResponseBody
    Response<String> handleRuntimeException(HttpServletRequest req, RuntimeException exception) {
        LOGGER.error("handleRuntimeException.error", exception);
        return Response.FAIL(500, exception.getMessage());
    }

    @ExceptionHandler({BindException.class})
    public
    @ResponseBody
    Response<String> handleBindException(HttpServletRequest req, BindException exception) {
        LOGGER.error("handleBindException.error", exception);
        if (exception.getFieldErrorCount() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            for (FieldError fieldError : exception.getFieldErrors()) {
                stringBuilder.append(fieldError.getField() + " " + fieldError.getDefaultMessage() + " ");
            }
            return Response.FAIL(400, stringBuilder.toString());
        }
        return Response.FAIL(400, exception.getMessage());
    }

    @ExceptionHandler({MissingServletRequestParameterException.class})
    public
    @ResponseBody
    Response<String> handleMissingServletRequestParameterException(HttpServletRequest req, MissingServletRequestParameterException exception) {
        LOGGER.error("handleMissingServletRequestParameterException.error", exception);
        return Response.FAIL(400, exception.getMessage());
    }

    public Operator getOperator(Context context) {
        return Operator.builder().operatorName(context.getUsername()).operatorType(operatorType).build();
    }
}
