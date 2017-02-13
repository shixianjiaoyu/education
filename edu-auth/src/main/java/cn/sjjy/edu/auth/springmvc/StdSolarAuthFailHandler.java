package cn.sjjy.edu.auth.springmvc;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;

/**
 * 默认权限认证失败处理， 返回403
 */
public class StdSolarAuthFailHandler implements SolarAuthFailHandler {

    public static final StdSolarAuthFailHandler INSTANCE = new StdSolarAuthFailHandler();

    @Override
    public boolean handle(HttpServletRequest request, HttpServletResponse response, SolarInfo solarInfo, List<Set<String>> alters) {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        return false;
    }
}
