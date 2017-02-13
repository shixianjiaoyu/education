package cn.sjjy.edu.manager.portal.webapi.session;

import cn.sjjy.edu.account.dto.LoginInfoDto;
import cn.sjjy.edu.account.service.IAccountService;
import cn.sjjy.edu.auth.springmvc.SolarInfo;
import cn.sjjy.edu.auth.springmvc.SolarUtils;
import cn.sjjy.edu.web.framework.controller.BaseController;
import cn.sjjy.edu.web.framework.core.Response;
import cn.sjjy.edu.manager.portal.webapi.session.vo.LoginInfo;
import cn.sjjy.edu.common.exception.ServiceException;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/** 
 * @author Captain
 * @date 2017年1月22日
 */
@Controller
@RequestMapping("/web_api/v1/session")
@Api(value = "/session", description = "登录相关")
public class SessionController extends BaseController {
	public static final int SESSION_MAX_INACTIVE_TIME = 60 * 60 * 2; // 120 分钟
    @Autowired
    private IAccountService accountService;

    @ApiOperation(value = "登录")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public
    @ResponseBody
    Response<LoginInfo> login(HttpServletRequest request, HttpServletResponse response, @RequestParam("username") String username, @RequestParam("md5_password") String md5Password) throws ServiceException {
        LoginInfoDto loginInfoDto = accountService.login(username, md5Password);
        SolarInfo solarInfo = SolarInfo.builder().userId(loginInfoDto.getAccountId()).userName(loginInfoDto.getUserName()).points(loginInfoDto.getPoints()).build();
        request.getSession().setAttribute(SolarUtils.LOGGEDIN_USER, solarInfo);
        request.getSession().setMaxInactiveInterval(SESSION_MAX_INACTIVE_TIME);
        SolarUtils.buildRootCookie(response, solarInfo);
        return Response.SUCCESS(LoginInfo.builder().access_token(loginInfoDto.getAccessToken()).build());
    }
}
