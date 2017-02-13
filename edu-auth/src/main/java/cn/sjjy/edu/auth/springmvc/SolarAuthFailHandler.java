package cn.sjjy.edu.auth.springmvc;


import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface SolarAuthFailHandler {

    /**
     * 处理Solar权限认证失败
     * @param request       http request
     * @param response      http response
     * @param solarInfo     Solar用户信息, 若未登录或非Solar用户，则为null
     * @param pointsAlters  可选期望权限点
     * @return  是否继续传递请求
     */
    boolean handle(HttpServletRequest request, HttpServletResponse response, SolarInfo solarInfo, List<Set<String>> pointsAlters);
}
