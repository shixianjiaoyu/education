package cn.sjjy.edu.account.service;


import java.util.List;
import java.util.Map;

import cn.sjjy.edu.account.dto.AccountDropBoxDto;
import cn.sjjy.edu.account.dto.AccountDto;
import cn.sjjy.edu.account.dto.LoginInfoDto;
import cn.sjjy.edu.account.dto.NewAccountDto;
import cn.sjjy.edu.account.dto.UpdateAccountDto;
import cn.sjjy.edu.account.dto.UpdatePasswordDto;
import cn.sjjy.edu.common.bean.Operator;
import cn.sjjy.edu.common.bean.PageResult;
import cn.sjjy.edu.common.exception.ServiceException;
import cn.sjjy.edu.common.util.Page;

/** 
 * @author Captain
 * @date 2017年1月21日
 */
public interface IAccountService {
    /**
     * 新建账户
     *
     * @param newAccount
     * @return
     * @throws ServiceException
     */
    public Integer createAccount(NewAccountDto newAccount, Operator operator) throws ServiceException;

    /**
     * 更新账户信息
     *
     * @param accountInfoDto
     * @return
     * @throws ServiceException
     */
    public Integer updateAccountInfo(UpdateAccountDto accountInfoDto) throws ServiceException;

    /**
     * 获取账户
     *
     * @param accountId
     * @return
     */
    public AccountDto getAccount(Integer accountId) throws ServiceException;

    public LoginInfoDto login(String username, String md5Password) throws ServiceException;

    public void updatePassword(UpdatePasswordDto passwordDto) throws ServiceException;

    public void enableAccount(Integer accountId, Operator operator) throws ServiceException;

    public void disableAccount(Integer accountId, Operator operator) throws ServiceException;

    public void arrearageAccount(Integer accountId) throws ServiceException;

    public void rechargeEnableAccount(Integer accountId, Operator operator) throws ServiceException;

    public void updateUsername(Integer accountId, String username, Operator operator) throws ServiceException;

    public void updateSales(Integer accountId, List<String> sales, Operator operator) throws ServiceException;

    public List<AccountDto> getAccountDtosInAccountIds(List<Integer> accountIds) throws ServiceException;

    public Map<Integer, AccountDto> getAccountDtoMapInAccountIds(List<Integer> accountIds) throws ServiceException;

    public List<AccountDropBoxDto> getAccountDropBox() throws ServiceException;

    public void validateAccountId(Integer accountId) throws ServiceException;

    public List<AccountDto> getAllAccountDtos() throws ServiceException;

	public PageResult<AccountDto> getAccountsByPage(Page page) throws ServiceException;
}
