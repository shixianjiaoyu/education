package cn.sjjy.edu.account.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.nimbusds.jose.JOSEException;

import cn.sjjy.edu.account.component.PasswordComponent;
import cn.sjjy.edu.account.dao.AccountDao;
import cn.sjjy.edu.account.dto.AccountDropBoxDto;
import cn.sjjy.edu.account.dto.AccountDto;
import cn.sjjy.edu.account.dto.LoginInfoDto;
import cn.sjjy.edu.account.dto.NewAccountDto;
import cn.sjjy.edu.account.dto.UpdateAccountDto;
import cn.sjjy.edu.account.dto.UpdatePasswordDto;
import cn.sjjy.edu.account.exception.AccountExceptionCode;
import cn.sjjy.edu.account.po.AccountPo;
import cn.sjjy.edu.common.AccountStatus;
import cn.sjjy.edu.common.PasswordStrength;
import cn.sjjy.edu.common.bean.Operator;
import cn.sjjy.edu.common.bean.PageResult;
import cn.sjjy.edu.common.exception.ServiceException;
import cn.sjjy.edu.common.util.Page;
import cn.sjjy.edu.common.util.TokenUtil;
import cn.sjjy.edu.log.dto.OperationType;
import cn.sjjy.edu.log.service.ILogService;

/**
 * @author Captain
 * @date 2017年1月21日
 */
@Service
public class AccountService implements IAccountService {
	private final static Logger LOGGER = LoggerFactory.getLogger(AccountService.class);
	@Autowired
	private AccountDao accountDao;
	@Autowired
	private PasswordComponent passwordComponent;
	@Autowired
	private ILogService logService;

	@Transactional(value = "accountTransactionManager", rollbackFor = Exception.class)
	@Override
	public Integer createAccount(NewAccountDto newAccount, Operator operator) throws ServiceException {
		this.paramValidateForCreate(newAccount);

		if (null != accountDao.getByUsername(newAccount.getUsername())) {
			LOGGER.info("exist username {}", newAccount.toString());
			throw new ServiceException(AccountExceptionCode.EXIST_USERNAME);
		}

		passwordComponent.confirmPassword(newAccount.getPassword(), newAccount.getConfirmPassword());

		passwordComponent.validPasswordSafe(newAccount.getPassword());

		String salt = passwordComponent.getSalt();

		String saltPassword = passwordComponent.createSaltPassword(newAccount.getPassword(), salt);

		AccountPo po = AccountPo.builder().username(newAccount.getUsername()).mobile("0")
				.passwordStrength(PasswordStrength.getByPassword(newAccount.getPassword()).getCode()).salt(salt)
				.saltPassword(saltPassword).status(AccountStatus.ON.getCode()).build();

		LOGGER.debug("create account po {}", po);
		accountDao.insert(po);

		logService.insertLog("acc_account", OperationType.INSERT, operator, po);
		return po.getAccountId();
	}

	private void paramValidateForCreate(NewAccountDto newAccount) throws ServiceException {
		if (null == newAccount) {
			LOGGER.info("AccountService.createAccount newAccount is null");
			throw new ServiceException(AccountExceptionCode.REQUIRED_PARAM);
		}

		LOGGER.info("createAccount.NewAccountDto{}", newAccount);

		if (StringUtils.isEmpty(newAccount.getUsername()) || StringUtils.isEmpty(newAccount.getPassword())
				|| StringUtils.isEmpty(newAccount.getConfirmPassword())) {
			LOGGER.info("AccountService.createAccount param is null");
			throw new ServiceException(AccountExceptionCode.REQUIRED_PARAM);
		}
	}

	@Override
	public AccountDto getAccount(Integer accountId) throws ServiceException {
		if (null == accountId) {
			LOGGER.info("getAccount accountId is null");
			throw new ServiceException(AccountExceptionCode.REQUIRED_PARAM);
		}

		LOGGER.info("getAccount accountID{}", accountId);

		AccountPo accountPo = this.getAccountPoById(accountId);

		return this.buildAccountDto(accountPo);
	}

	@Override
	public LoginInfoDto login(String username, String md5Password) throws ServiceException {
		if (StringUtils.isEmpty(username) || StringUtils.isEmpty(md5Password)) {
			LOGGER.info("AccountService.login param is null");
			throw new ServiceException(AccountExceptionCode.REQUIRED_PARAM);
		}

		LOGGER.info("AccountService.login username{}", username);

		AccountPo accountPo = accountDao.getByUsername(username);

		if (null == accountPo) {
			LOGGER.info("illegal mobile{}", username);
			throw new ServiceException(AccountExceptionCode.NOT_EXIST_ACCOUNT);
		}

		String saltPassword = passwordComponent.encodeSaltMd5Password(md5Password, accountPo.getSalt());
		if (!accountPo.getSaltPassword().equals(saltPassword)) {
			LOGGER.info("incrorrect newPassword username{}", username);
			throw new ServiceException(AccountExceptionCode.INCORRECT_PASSWORD);
		}

		if (AccountStatus.OFF.getCode().equals(accountPo.getStatus())) {
			LOGGER.info("user is disable username{}", username);
			throw new ServiceException(AccountExceptionCode.USER_IS_DISABLE);
		}

		String token;
		try {
			token = TokenUtil.createToken(username, accountPo.getAccountId());
		} catch (JOSEException e) {
			LOGGER.error("TokenUtil.createToken.error", e);
			throw new ServiceException(AccountExceptionCode.SYSTEM_ERROR);
		}

		return LoginInfoDto.builder().accountId(accountPo.getAccountId()).accessToken(token).build();
	}

	@Override
	public void updatePassword(UpdatePasswordDto passwordDto) throws ServiceException {
		if (null == passwordDto) {
			LOGGER.info("updatePassword passwordDto is null");
			throw new ServiceException(AccountExceptionCode.REQUIRED_PARAM);
		}

		if (null == passwordDto.getAccountId() || StringUtils.isEmpty(passwordDto.getOldMd5Password())
				|| StringUtils.isEmpty(passwordDto.getNewPassword())
				|| StringUtils.isEmpty(passwordDto.getConfirmPassword())) {
			LOGGER.info("updatePassword passwordDto value is null");
			throw new ServiceException(AccountExceptionCode.REQUIRED_PARAM);
		}
		AccountPo accountPo = this.getAccountPoById(passwordDto.getAccountId());

		String saltOldPassword = passwordComponent.encodeSaltMd5Password(passwordDto.getOldMd5Password(),
				accountPo.getSalt());

		if (!accountPo.getSaltPassword().equals(saltOldPassword)) {
			LOGGER.info("incrorrect Password account id{}", passwordDto.getAccountId());
			throw new ServiceException(AccountExceptionCode.INCORRECT_PASSWORD);
		}

		passwordComponent.confirmPassword(passwordDto.getNewPassword(), passwordDto.getConfirmPassword());

		passwordComponent.validPasswordSafe(passwordDto.getNewPassword());

		String salt = passwordComponent.getSalt();

		String saltPassword = passwordComponent.createSaltPassword(passwordDto.getNewPassword(), salt);

		accountDao.update(AccountPo.builder().accountId(accountPo.getAccountId()).salt(salt)
				.passwordStrength(PasswordStrength.getByPassword(passwordDto.getNewPassword()).getCode())
				.saltPassword(saltPassword).build());
	}

	@Override
	public Map<Integer, AccountDto> getAccountDtoMapInAccountIds(List<Integer> accountIds) throws ServiceException {
		if (CollectionUtils.isEmpty(accountIds)) {
			return Collections.emptyMap();
		}
		List<AccountDto> accountDtos = this.getAccountDtosInAccountIds(accountIds);
		if (CollectionUtils.isEmpty(accountDtos)) {
			return Collections.emptyMap();
		}
		Map<Integer, AccountDto> resultMap = new HashMap<>(accountDtos.size());
		for (AccountDto dto : accountDtos) {
			resultMap.put(dto.getAccountId(), dto);
		}
		return resultMap;
	}

	@Override
	public List<AccountDropBoxDto> getAccountDropBox() throws ServiceException {
		List<AccountPo> accountPos = accountDao.getList();
		if (CollectionUtils.isEmpty(accountPos)) {
			return Collections.emptyList();
		}
		List<AccountDropBoxDto> result = new ArrayList<>(accountPos.size());
		result.addAll(accountPos.stream().filter(accountPo -> !AccountStatus.OFF.equals(accountPo.getStatus()))
				.map(accountPo -> AccountDropBoxDto.builder().accountId(accountPo.getAccountId())
						.username(accountPo.getUsername()).build())
				.collect(Collectors.toList()));
		return result;
	}

	@Override
	public void validateAccountId(Integer accountId) throws ServiceException {
		AccountPo accountPo = this.getAccountPoById(accountId);
		if (AccountStatus.OFF.getCode().equals(accountPo.getStatus())) {
			throw new ServiceException(AccountExceptionCode.USER_IS_DISABLE);
		}
	}

	@Override
	public void enableAccount(Integer accountId, Operator operator) throws ServiceException {
		AccountPo accountPo = this.getAccountPoById(accountId);
		if (AccountStatus.ON.getCode().equals(accountPo.getStatus())) {
			return;
		}
		if (!AccountStatus.OFF.getCode().equals(accountPo.getStatus())) {
			throw new ServiceException(AccountExceptionCode.USER_CAN_NOT_ENABLE);
		}
		this.changeAccountStatus(accountId, operator, AccountStatus.ON);
	}

	@Override
	public void disableAccount(Integer accountId, Operator operator) throws ServiceException {
		AccountPo accountPo = this.getAccountPoById(accountId);
		if (AccountStatus.OFF.getCode().equals(accountPo.getStatus())) {
			return;
		}
		if (!AccountStatus.ON.getCode().equals(accountPo.getStatus())) {
			throw new ServiceException(AccountExceptionCode.USER_CAN_NOT_DISABLE);
		}
		this.changeAccountStatus(accountId, operator, AccountStatus.OFF);
	}

	@Override
	public void arrearageAccount(Integer accountId) throws ServiceException {
		AccountPo accountPo = this.getAccountPoById(accountId);
		if (AccountStatus.ARREARAGE.getCode().equals(accountPo.getStatus())) {
			return;
		}
		this.changeAccountStatus(accountId, Operator.SYSTEM, AccountStatus.ARREARAGE);
	}

	@Override
	public void rechargeEnableAccount(Integer accountId, Operator operator) throws ServiceException {
		AccountPo accountPo = this.getAccountPoById(accountId);
		if (AccountStatus.ON.getCode().equals(accountPo.getStatus())) {
			return;
		}
		this.changeAccountStatus(accountId, operator, AccountStatus.ON);
	}

	@Override
	public void updateUsername(Integer accountId, String username, Operator operator) throws ServiceException {
		if (null == accountId || StringUtils.isEmpty(username) || Operator.validateParamIsNull(operator)) {
			LOGGER.info("updateUsername param is null");
			throw new ServiceException(AccountExceptionCode.REQUIRED_PARAM);
		}

		LOGGER.info("updateUsername accountId{}, username{} operator{}", accountId, username, operator);

		this.getAccountPoById(accountId);

		if (null != accountDao.getByUsername(username)) {
			LOGGER.info("exist username {}", username);
			throw new ServiceException(AccountExceptionCode.EXIST_USERNAME);
		}

		AccountPo po = AccountPo.builder().accountId(accountId).username(username).build();
		try {
			accountDao.update(po);
		} catch (DuplicateKeyException e) {
			LOGGER.error("updateUsername", e);
			LOGGER.info("exist username {}", username);
			throw new ServiceException(AccountExceptionCode.EXIST_USERNAME);
		}

		// logService.insertLog("acc_account", OperationType.UPDATE, operator,
		// po);
	}

	private void changeAccountStatus(Integer accountId, Operator operator, AccountStatus accountStatus)
			throws ServiceException {
		if (null == accountId || Operator.validateParamIsNull(operator)) {
			LOGGER.info("changeAccountStatus param is null");
			throw new ServiceException(AccountExceptionCode.REQUIRED_PARAM);
		}
		LOGGER.info("changeAccountStatus accountId{}, operatorname{}, accountStatus{}", accountId,
				operator.getOperatorName(), accountStatus.getCode());

		AccountPo po = AccountPo.builder().accountId(accountId).status(accountStatus.getCode()).build();

		Integer result = accountDao.update(po);
		if (result != 1) {
			throw new ServiceException(AccountExceptionCode.FAIL_OPERATION);
		}
		// logService.insertLog("acc_account", OperationType.UPDATE, operator,
		// po);
	}

	private void mobileNumberValidation(String str) throws ServiceException {
		Pattern p = null;
		Matcher m = null;
		boolean b = false;
		p = Pattern.compile("^1[3|4|5|7|8]\\d{9}$"); // 验证手机号
		m = p.matcher(str);
		b = m.matches();

		if (str.trim().length() != 11 || !b) {
			throw new ServiceException(AccountExceptionCode.ILLEGAL_MOBILE);
		}
	}

	private AccountPo getAccountPoById(Integer accountId) throws ServiceException {
		AccountPo accountPo = accountDao.get(accountId);
		if (null == accountPo) {
			LOGGER.info("account does not exist, account id{}", accountId);
			throw new ServiceException(AccountExceptionCode.NOT_EXIST_ACCOUNT);
		}
		return accountPo;
	}

	private AccountDto buildAccountDto(AccountPo accountPo) {
		return AccountDto.builder().accountId(accountPo.getAccountId())
				.passwordStrength(accountPo.getPasswordStrength()).username(accountPo.getUsername())
				.mobile(accountPo.getMobile()).status(accountPo.getStatus()).build();
	}

	@Override
	public Integer updateAccountInfo(UpdateAccountDto accountInfoDto) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateSales(Integer accountId, List<String> sales, Operator operator) throws ServiceException {
		// TODO Auto-generated method stub

	}

	@Override
	public List<AccountDto> getAccountDtosInAccountIds(List<Integer> accountIds) throws ServiceException {
		if (CollectionUtils.isEmpty(accountIds)) {
			return Collections.emptyList();
		}
		return this.addInfoToAccountDtos(accountDao.getInIds(accountIds));
	}
	
	private List<AccountDto> addInfoToAccountDtos(List<AccountPo> accountPos) throws ServiceException {
        if (CollectionUtils.isEmpty(accountPos)) {
            return Collections.emptyList();
        }
        return accountPos.stream().map(po -> this.po2dto(po)).collect(Collectors.toList());
    }
	
	private AccountDto po2dto(AccountPo po){
		AccountDto dto = AccountDto.builder().build();
		BeanUtils.copyProperties(po, dto);
		return dto;
	}

	@Override
	public List<AccountDto> getAllAccountDtos() throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public PageResult<AccountDto> getAccountsByPage(Page page) throws ServiceException {
		if (null == page) {
            throw new ServiceException(AccountExceptionCode.REQUIRED_PARAM);
        }
        LOGGER.info("AccountService.getAccountsByPage page{}", page);
        Integer count = accountDao.getCount();
        if (null == count || count == 0) {
            return PageResult.EMPTY_PAGE_RESULT;
        }
        List<AccountPo> pos = accountDao.getByPage(page);
        if (CollectionUtils.isEmpty(pos)) {
            return PageResult.<AccountDto>builder().total(count).records(Collections.emptyList()).build();
        }
        return PageResult.<AccountDto>builder().total(count).records(this.posToDtos(pos)).build();
	}

	private List<AccountDto> posToDtos(List<AccountPo> pos) {
		return pos.stream().map(po -> this.po2dto(po)).collect(Collectors.toList());
	}

}