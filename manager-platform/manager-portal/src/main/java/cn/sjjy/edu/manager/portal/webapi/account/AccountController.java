package cn.sjjy.edu.manager.portal.webapi.account;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

import cn.sjjy.edu.account.dto.AccountDropBoxDto;
import cn.sjjy.edu.account.dto.AccountDto;
import cn.sjjy.edu.account.dto.NewAccountDto;
import cn.sjjy.edu.account.service.IAccountService;
import cn.sjjy.edu.common.bean.PageResult;
import cn.sjjy.edu.common.exception.ServiceException;
import cn.sjjy.edu.common.util.Page;
import cn.sjjy.edu.manager.portal.webapi.account.vo.AccountDropBoxVo;
import cn.sjjy.edu.manager.portal.webapi.account.vo.AccountVo;
import cn.sjjy.edu.manager.portal.webapi.account.vo.NewAccountVo;
import cn.sjjy.edu.web.framework.controller.BaseController;
import cn.sjjy.edu.web.framework.core.Context;
import cn.sjjy.edu.web.framework.core.Pagination;
import cn.sjjy.edu.web.framework.core.Response;

/**
 * @author Captain
 * @date 2017年1月22日
 */
@Controller
@RequestMapping("/web_api/v1/accounts")
@Api(value = "/account", description = "账户相关")
public class AccountController extends BaseController {
	@Autowired
	private IAccountService accountService;

	@ApiOperation("账户下拉列表")
	@RequestMapping(value = "/drop_box", method = RequestMethod.GET)
	public @ResponseBody Response<List<AccountDropBoxVo>> listDropBox(Context context) throws ServiceException {
		List<AccountDropBoxDto> accountDropBoxDtos = accountService.getAccountDropBox();
		List<AccountDropBoxVo> result = new ArrayList<>(accountDropBoxDtos.size());
		result.addAll(accountDropBoxDtos.stream().map(
				dto -> AccountDropBoxVo.builder().account_id(dto.getAccountId()).username(dto.getUsername()).build())
				.collect(Collectors.toList()));
		return Response.SUCCESS(result);
	}

	// @SolarAuth(points = {"cpm_view_account"})
	@ApiOperation("账户列表")
	@RequestMapping(value = "", method = RequestMethod.GET)
	public @ResponseBody Response<Pagination<List<AccountVo>>> list(Context context,
			@RequestParam(value = "page", required = false, defaultValue = "1") @ApiParam("页码") int currentPage,
			@RequestParam(value = "size", required = false, defaultValue = "10") @ApiParam("页长") int pageSize)
			throws ServiceException {
		PageResult<AccountDto> accountPageResult = accountService
				.getAccountsByPage(Page.valueOf(currentPage, pageSize));
		List<AccountDto> accountDtoList = accountPageResult.getRecords();
		List<AccountVo> accountVoList = Collections.emptyList();
		if (!CollectionUtils.isEmpty(accountDtoList)) {
			accountVoList = accountDtoList.stream().map(dto -> this.convertDtoToVo(dto)).collect(Collectors.toList());
		}
		return Response.SUCCESS(new Pagination<>(currentPage, accountPageResult.getTotal(), accountVoList));
	}

	// @SolarAuth(points = {"cpm_create_account"})
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

	// @SolarAuth(points = {"cpm_modify_account"})
	@ApiOperation("编辑账户名")
	@RequestMapping(value = "/accounts/username", method = RequestMethod.POST)
	public @ResponseBody Response<String> updateAccountUsername(Context context,
			@RequestParam("account_id") Integer accountId, @RequestParam("username") String username)
			throws ServiceException {
		accountService.updateUsername(accountId, username, super.getOperator(context));
		return Response.SUCCESS(null);
	}

	// @SolarAuth(points = {"cpm_modify_sales"})
	@ApiOperation("编辑销售")
	@RequestMapping(value = "/accounts/sales", method = RequestMethod.POST)
	public @ResponseBody Response<String> updateAccountSales(Context context,
			@RequestParam("account_id") Integer accountId, @RequestParam("sales") List<String> sales)
			throws ServiceException {
		accountService.updateSales(accountId, sales, super.getOperator(context));
		return Response.SUCCESS(null);
	}

	// @SolarAuth(points = {"cpm_enable_disable_account"})
	@ApiOperation("启用账户")
	@RequestMapping(value = "/accounts/enable", method = RequestMethod.POST)
	public @ResponseBody Response<String> enableAccount(Context context, @RequestParam("account_id") Integer accountId)
			throws ServiceException {
		accountService.enableAccount(accountId, super.getOperator(context));
		return Response.SUCCESS(null);
	}

	// @SolarAuth(points = {"cpm_enable_disable_account"})
	@ApiOperation("停用账户")
	@RequestMapping(value = "/accounts/disable", method = RequestMethod.POST)
	public @ResponseBody Response<String> disableAccount(Context context, @RequestParam("account_id") Integer accountId)
			throws ServiceException {
		accountService.disableAccount(accountId, super.getOperator(context));
		return Response.SUCCESS(null);
	}

	private AccountVo convertDtoToVo(AccountDto dto) {
		AccountVo vo = AccountVo.builder().build();
		BeanUtils.copyProperties(dto, vo);
		return vo;
	}

}
