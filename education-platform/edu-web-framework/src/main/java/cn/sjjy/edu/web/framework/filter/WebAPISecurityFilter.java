package cn.sjjy.edu.web.framework.filter;

import java.io.IOException;
import java.text.ParseException;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;

import cn.sjjy.edu.common.util.TokenUtil;
import cn.sjjy.edu.common.util.Utils;
import cn.sjjy.edu.web.framework.core.Context;
import cn.sjjy.edu.web.framework.core.Response;
import cn.sjjy.edu.web.framework.exception.WebApiExceptionCode;

/** 
 * @author Captain
 * @date 2017年1月21日
 */
public class WebAPISecurityFilter implements Filter {

    public static final String HTTP_ACCESS_TOKEN = "HTTP-ACCESS-TOKEN";
    public static final String HTTP_SECRET_KEY = "HTTP-SECRET-KEY";
    public static final String LOGIN_URL = "/session/login";
    public static final String SALE_URL = "/sale";
    public static final String API_URL = "api-docs";

    final static Logger logger = LoggerFactory.getLogger(WebAPISecurityFilter.class);
    private static MACVerifier verifier = null;

    static {
        SecretKey secretKeySpec = new SecretKeySpec(TokenUtil.getSecretKey(), "AES");
        try {
            verifier = new MACVerifier(secretKeySpec);
        } catch (JOSEException e) {
            logger.error("new MACVerifier.error", e);
            System.exit(0);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        // 忽略登录验证链接
        if (httpRequest.getRequestURI().indexOf(LOGIN_URL) > 0) {
            chain.doFilter(request, response);
            return;
        }

        if (httpRequest.getRequestURI().indexOf(SALE_URL) > 0) {
            chain.doFilter(request, response);
            return;
        }

        if (httpRequest.getRequestURI().indexOf(API_URL) > 0) {
            chain.doFilter(request, response);
            return;
        }
        Response<String> error = securityCheck(httpRequest, httpResponse);
        if (null != error) {
            httpResponse.setContentType("application/json");
            httpResponse.setCharacterEncoding("UTF-8");
            httpResponse.getWriter().write(JSON.toJSONString(error));
            httpResponse.getWriter().flush();
            return;
        }

        chain.doFilter(request, response);
    }

    private Response<String> securityCheck(HttpServletRequest request, HttpServletResponse
            response) {
        String token = request.getHeader(HTTP_ACCESS_TOKEN);
        if (StringUtils.isEmpty(token)) {
            return Response.FAIL(WebApiExceptionCode.BAD_REQUEST);
        }

        SignedJWT signedJWT = null;
        try {
            signedJWT = SignedJWT.parse(token);
        } catch (ParseException e) {
            logger.error("SignedJWT.parse.error", e);
            return Response.FAIL(WebApiExceptionCode.SYSTEM_ERROR);
        }
        try {
            if (!signedJWT.verify(verifier)) {
                return Response.FAIL(WebApiExceptionCode.UNAUTHORIZED);
            }
        } catch (JOSEException e) {
            logger.error("signedJWT.verify.error", e);
            return Response.FAIL(WebApiExceptionCode.SYSTEM_ERROR);
        }

        try {
            if (Utils.getNow().getTime() > signedJWT.getJWTClaimsSet().getExpirationTime().getTime()) {
                logger.info("token is expire token{}", token);
                return Response.FAIL(WebApiExceptionCode.TOKEN_EXPIRE);
            }
            request.setAttribute("context", new Context(
                    Integer.valueOf(signedJWT.getJWTClaimsSet().getJWTID()),
                    signedJWT.getJWTClaimsSet().getIssuer()));
        } catch (ParseException e) {
            logger.error("signedJWT.getJWTClaimsSet().error", e);
            return Response.FAIL(WebApiExceptionCode.SYSTEM_ERROR);
        }

        return null;
    }

    public void destroy() {

    }

}
