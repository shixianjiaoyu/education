package cn.sjjy.edu.web.framework.core;


import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;

import cn.sjjy.edu.web.framework.core.Context;

import javax.servlet.http.HttpServletRequest;

/** 
 * @author Captain
 * @date 2017年1月23日
 */
public class ContextWebArgumentResolver implements WebArgumentResolver {

    @Override
    public Object resolveArgument(MethodParameter methodParameter, NativeWebRequest webRequest) throws Exception {
        if (methodParameter.getParameterType().equals(Context.class)){
            Object request = webRequest.getNativeRequest();
            if (request instanceof HttpServletRequest) {
                HttpServletRequest httpRequest = (HttpServletRequest) request;
                return httpRequest.getAttribute("context");
            }
        }
        return UNRESOLVED;
    }

}
