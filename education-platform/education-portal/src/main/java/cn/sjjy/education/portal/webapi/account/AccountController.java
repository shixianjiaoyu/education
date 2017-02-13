package cn.sjjy.education.portal.webapi.account;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import cn.sjjy.edu.account.dto.AccountDto;
import cn.sjjy.edu.account.dto.NewAccountDto;
import cn.sjjy.edu.account.service.IAccountService;
import cn.sjjy.edu.common.exception.ServiceException;
import cn.sjjy.edu.web.framework.controller.BaseController;
import cn.sjjy.edu.web.framework.core.Context;
import cn.sjjy.edu.web.framework.core.Response;
import cn.sjjy.education.portal.webapi.account.vo.AccountVo;
import cn.sjjy.education.portal.webapi.account.vo.NewAccountVo;


@Controller
@RequestMapping("/web_api/v1/accounts")
@Api(value = "/account", description = "账户相关")
public class AccountController extends BaseController {
	@Autowired
	private IAccountService accountService;

	@ApiOperation("注册账号")
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public @ResponseBody Response<String> register(Context context, @RequestBody NewAccountVo newAccountVo)
			throws ServiceException {
		accountService.createAccount(newAccountVo2Dto(newAccountVo), super.getOperator(context));
		return Response.SUCCESS(null);
	}

	private NewAccountDto newAccountVo2Dto(NewAccountVo vo) {
		NewAccountDto dto = NewAccountDto.builder().build();
		BeanUtils.copyProperties(vo, dto);
		return dto;
	}

	// @SolarAuth(points = {"cpm_view_account"})
	@ApiOperation("账户详情")
	@RequestMapping(value = "/accounts/{account_id}", method = RequestMethod.GET)
	public @ResponseBody Response<AccountVo> listWallets(@PathVariable Integer account_id) throws ServiceException {
		AccountDto dto = accountService.getAccount(account_id);
		if (null == dto) {
			return Response.SUCCESS(null);
		}
		return Response.SUCCESS(this.convertDtoToVo(dto));
	}

	@ApiOperation("编辑账户名")
	@RequestMapping(value = "/accounts/username", method = RequestMethod.POST)
	public @ResponseBody Response<String> updateAccountUsername(Context context,
			@RequestParam("account_id") Integer accountId, @RequestParam("username") String username)
			throws ServiceException {
		accountService.updateUsername(accountId, username, super.getOperator(context));
		return Response.SUCCESS(null);
	}

	private AccountVo convertDtoToVo(AccountDto dto) {
		AccountVo vo = AccountVo.builder().build();
		BeanUtils.copyProperties(dto, vo);
		return vo;
	}

}
