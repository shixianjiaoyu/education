package cn.sjjy.edu.auth.service;


import java.util.List;
import java.util.Map;

import cn.sjjy.edu.account.dto.AccountDto;
import cn.sjjy.edu.auth.dto.AuthItemChildDto;
import cn.sjjy.edu.auth.dto.AuthItemDto;
import cn.sjjy.edu.common.bean.Operator;
import cn.sjjy.edu.common.exception.ServiceException;

/** 
 * @author Captain
 * @date 2017年1月22日
 */
public interface IAuthItemService {
	
    List<AuthItemDto> getRoleList();

	Map<Integer, List<AccountDto>> getItemUserMapInItemIds(List<Integer> itemIds) throws ServiceException;

	AuthItemDto getAuthItemById(Integer itemId) throws ServiceException;

	Integer createRole(Operator operator, AuthItemDto dto) throws ServiceException;

	void updateRole(Operator operator, AuthItemDto dto) throws ServiceException;

	List<AuthItemDto> getPointList();

	List<AuthItemChildDto> getItemChildListInParentIds(List<Integer> parentIds);

	void updateRolePoint(Operator operator, Integer roleId, List<Integer> pointIds) throws ServiceException;

	void updateUserRole(Operator operator, Integer userId, List<Integer> roleIds) throws ServiceException;
}
