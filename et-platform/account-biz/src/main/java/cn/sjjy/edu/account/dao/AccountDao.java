package cn.sjjy.edu.account.dao;

import org.apache.ibatis.annotations.Param;

import cn.sjjy.edu.account.po.AccountPo;
import cn.sjjy.edu.common.util.Page;

import java.util.List;

/** 
 * @author Captain
 * @date 2017年1月21日
 */
public interface AccountDao {
    public AccountPo get(@Param("account_id") Integer accountId);

    public AccountPo getByMobile(@Param("mobile") String mobile);

    public AccountPo getByUsername(@Param("username") String username);

    public Integer insert(AccountPo accountModel);

    public Integer update(AccountPo accountModel);

    public Integer delete(@Param("account_id") Integer accountId);

    public List<AccountPo> getList();

    public List<AccountPo> getInIds(@Param("account_ids") List<Integer> accountIds);

	public Integer getCount();

	public List<AccountPo> getByPage(@Param("page") Page page);
}
