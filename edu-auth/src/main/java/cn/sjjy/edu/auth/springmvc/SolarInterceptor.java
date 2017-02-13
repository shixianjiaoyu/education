package cn.sjjy.edu.auth.springmvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cn.sjjy.edu.auth.annotation.SolarAlterAuth;
import cn.sjjy.edu.auth.annotation.SolarAuth;

/**
 * 对{@link SolarAuth}作用的web接口进行权限校验
 * 从request中读取当前用户
 * 忽略{@link SolarUserId}和{@link SolarUserName}
 */
public class SolarInterceptor extends HandlerInterceptorAdapter {

    private final static char pointsSpliter = '&';

    private int cacheTimeout = 0;
    private SolarAuthFailHandler authFailHandler;
    private List<String> basePoints = null;
    private Map<String, List<String>> covers;
    private Map<String, Set<String>> plateCovers;

    public void init() {
        if (authFailHandler == null) {
            authFailHandler = StdSolarAuthFailHandler.INSTANCE;
        }

        plateCovers = new HashMap<>();
        if (covers != null) {
            for (String key : covers.keySet()) {
                Set<String> recCover = doCover(plateCovers, covers, key);
                if (!CollectionUtils.isEmpty(recCover)) {
                    plateCovers.put(key, recCover);
                }
            }
        }
    }

    private Set<String> doCover(Map<String, Set<String>> plateCovers, Map<String, List<String>> covers, String key) {
        List<String> cover = covers.get(key);
        if (CollectionUtils.isEmpty(cover)) {
            return null;
        }

        Set<String> thisCover = plateCovers.get(key);
        if (thisCover == null) {
            thisCover = new HashSet<>(cover);
            plateCovers.put(key, thisCover);
        } else {
            return thisCover;
        }

        for (String covered : cover) {
            Set<String> recCover = doCover(plateCovers, covers, covered);
            if (!CollectionUtils.isEmpty(recCover)) {
                thisCover.addAll(recCover);
            }
        }

        return thisCover;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        System.out.println(request.getCookies());
        ExpectAlters expectAlters = getExpectedAlters(handlerMethod);
        if (expectAlters.directPass) {
            return true;
        }
        List<Set<String>> alters = expectAlters.alters;

        SolarInfo solarInfo = SolarUtils.getSolarInfo(request);
        if (solarInfo == null) {
            // 未登录
            return authFailHandler.handle(request, response, null, alters);
        }

        Set<String> points = getExtendedPoints(solarInfo.getPoints());
        request.setAttribute(SolarUtils.LOGGEDIN_USER, solarInfo);
        SolarUtils.buildRootCookie(response, solarInfo);
        //response.addCookie(buildRootCookie(SolarConstants.CACHE_POINTS_COOKIE_NAME, String.valueOf(Joiner.on(pointsSpliter).join(points))));
        return authAlters(alters, points) || authFailHandler.handle(request, response, solarInfo, alters);
    }

    private Set<String> getExtendedPoints(Set<String> points) {
        if (CollectionUtils.isEmpty(plateCovers) || CollectionUtils.isEmpty(points)) {
            return points;
        }

        Set<String> extendedPoints = new HashSet<>(points);
        for (String point : points) {
            Set<String> extend = plateCovers.get(point);
            if (extend != null) {
                extendedPoints.addAll(extend);
            }
        }
        return extendedPoints;
    }

    private Map<HandlerMethod, ExpectAlters> cachedAlters = new ConcurrentHashMap<>();
    private ExpectAlters getExpectedAlters(HandlerMethod handlerMethod) {
        ExpectAlters expectAlters = cachedAlters.get(handlerMethod);
        if (expectAlters == null) {
            expectAlters = parseExpectedAlters(handlerMethod);
            cachedAlters.put(handlerMethod, expectAlters);
        }
        return expectAlters;
    }

    private ExpectAlters parseExpectedAlters(HandlerMethod handlerMethod) {

        List<Set<String>> alterAuthList = new ArrayList<>();

        Set<String> expectedPoints = new HashSet<>();
        if (basePoints != null) {
            expectedPoints.addAll(basePoints);
        }

        SolarAuth solarAuth = handlerMethod.getMethodAnnotation(SolarAuth.class);
        if (solarAuth != null) {
            Collections.addAll(expectedPoints, solarAuth.points());
        }
        SolarAuth classSolarAuth = handlerMethod.getBeanType().getAnnotation(SolarAuth.class);
        if (classSolarAuth != null) {
            Collections.addAll(expectedPoints, classSolarAuth.points());
        }
        SolarAlterAuth solarAlterAuth = handlerMethod.getMethodAnnotation(SolarAlterAuth.class);
        if (solarAlterAuth == null) {
            if (!expectedPoints.isEmpty()) {
                alterAuthList.add(expectedPoints);
            }
        } else {
            for (SolarAuth alter : solarAlterAuth.alters()) {
                Set<String> alterPoints = new HashSet<>(expectedPoints);
                Collections.addAll(alterPoints, alter.points());
                alterAuthList.add(alterPoints);
            }
        }

        if (alterAuthList.isEmpty() && solarAuth == null && classSolarAuth == null && solarAlterAuth == null) {
            return new ExpectAlters(true, alterAuthList);
        }

        return new ExpectAlters(false, alterAuthList);
    }

    private boolean authAlters(List<Set<String>> alters, Set<String> real) {
        if (CollectionUtils.isEmpty(alters)) {
            return true;
        }
        for (Set<String> expected : alters) {
            if (authPoints(expected, real)) {
                return true;
            }
        }
        return false;
    }

    private boolean authPoints(Set<String> expected, Set<String> real) {
        return real.containsAll(expected);
    }

    public int getCacheTimeout() {
        return cacheTimeout;
    }

    public void setCacheTimeout(int cacheTimeout) {
        this.cacheTimeout = cacheTimeout;
    }

    public SolarAuthFailHandler getAuthFailHandler() {
        return authFailHandler;
    }

    public void setAuthFailHandler(SolarAuthFailHandler authFailHandler) {
        this.authFailHandler = authFailHandler;
    }

    public List<String> getBasePoints() {
        return basePoints;
    }

    public void setBasePoints(List<String> basePoints) {
        this.basePoints = basePoints;
    }

    public Map<String, List<String>> getCovers() {
        return covers;
    }

    public void setCovers(Map<String, List<String>> covers) {
        this.covers = covers;
    }

    private static class ExpectAlters {
        boolean directPass;

        List<Set<String>> alters;

        public ExpectAlters(boolean directPass, List<Set<String>> alters) {
            this.directPass = directPass;
            this.alters = alters;
        }
    }
    
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
    		throws Exception {
    	// TODO Auto-generated method stub
//    	super.afterCompletion(request, response, handler, ex);
    	response.setStatus(HttpStatus.FORBIDDEN.value());
    	throw new Exception("Forbidden");
    }
}
