package cn.sjjy.edu.auth.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import cn.sjjy.edu.account.common.AuthItemType;
import cn.sjjy.edu.account.dto.AccountDto;
import cn.sjjy.edu.account.exception.AccountExceptionCode;
import cn.sjjy.edu.account.service.IAccountService;
import cn.sjjy.edu.auth.dao.AuthAssignmentDao;
import cn.sjjy.edu.auth.dao.AuthItemChildDao;
import cn.sjjy.edu.auth.dao.AuthItemDao;
import cn.sjjy.edu.auth.dto.AuthItemChildDto;
import cn.sjjy.edu.auth.dto.AuthItemDto;
import cn.sjjy.edu.auth.exception.AuthExceptionCode;
import cn.sjjy.edu.auth.po.AuthAssignmentPo;
import cn.sjjy.edu.auth.po.AuthItemChildPo;
import cn.sjjy.edu.auth.po.AuthItemPo;
import cn.sjjy.edu.auth.query.AuthItemPoQuery;
import cn.sjjy.edu.common.bean.Operator;
import cn.sjjy.edu.common.exception.ServiceException;
import cn.sjjy.edu.log.service.ILogService;

/** 
 * @author Captain
 * @date 2017年1月22日
 */
@Service
public class AuthItemServiceImpl implements IAuthItemService {
    private final static Logger LOGGER = LoggerFactory.getLogger(AuthItemServiceImpl.class);
    @Autowired
    private AuthItemDao authItemDao;
    @Autowired
    private AuthAssignmentDao authAssignmentDao;
    @Autowired
    private AuthItemChildDao authItemChildDao;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private ILogService logService;
    
	@Override
	public List<AuthItemDto> getRoleList() {
		List<AuthItemPo> authItemPoList = authItemDao.query(AuthItemPoQuery.builder()
				.isDeleted(false).typeList(Lists.newArrayList(AuthItemType.ROLE.getCode())).build());
		List<AuthItemDto> authItemDtoList = Collections.emptyList();
		if (!CollectionUtils.isEmpty(authItemPoList)) {
			authItemDtoList = authItemPoList.stream().map(po -> this.po2dto(po)).collect(Collectors.toList());
		}
		return authItemDtoList;
	}
	
	private AuthItemDto po2dto(AuthItemPo po){
		AuthItemDto dto = AuthItemDto.builder().build();
		BeanUtils.copyProperties(po, dto);
		return dto;
	}

	@Override
	public Map<Integer, List<AccountDto>> getItemUserMapInItemIds(List<Integer> itemIds) throws ServiceException {
		if (CollectionUtils.isEmpty(itemIds)) {
			throw new ServiceException(AuthExceptionCode.REQUIRED_PARAM);
		}
		List<AuthAssignmentPo> assignmentPoList = authAssignmentDao.getInItemIds(itemIds);
		Map<Integer, List<AccountDto>> map = Collections.emptyMap();
		if (!CollectionUtils.isEmpty(assignmentPoList)) {
			Set<Integer> accountIds = assignmentPoList.stream().map(AuthAssignmentPo::getUserId).collect(Collectors.toSet());
			List<AccountDto> accountDtoList = accountService.getAccountDtosInAccountIds(Lists.newArrayList(accountIds));
			if (!CollectionUtils.isEmpty(accountDtoList)) {
				Map<Integer, AccountDto> accountMap = accountDtoList.stream().collect(Collectors.toMap(AccountDto::getAccountId, dto -> dto));
				map = Maps.newHashMap();
				AccountDto accountDto = null;
				Integer itemId = null;
				List<AccountDto> accountDtos = Collections.emptyList();
				for(AuthAssignmentPo assignmentPo: assignmentPoList){
					if (accountMap.containsKey(assignmentPo.getUserId())) {
						itemId = assignmentPo.getItemId();
						accountDto = accountMap.get(assignmentPo.getUserId());
						if (map.containsKey(itemId)) {
							accountDtos = map.get(itemId);
							accountDtos.add(accountDto);
							map.put(itemId, accountDtos);
						}else {
							map.put(itemId, Lists.newArrayList(accountDto));
						}
					}
				}
			}
		}
		return map;
	}

	@Override
	public AuthItemDto getAuthItemById(Integer itemId) throws ServiceException {
		if (itemId == null) {
			throw new ServiceException(AccountExceptionCode.REQUIRED_PARAM);
		}
		AuthItemPo po = authItemDao.getById(itemId);
		if (po == null) {
			throw new ServiceException(AuthExceptionCode.NOT_EXIST);
		}
		return this.po2dto(po);
	}

	@Override
	@Transactional(value = "accountTransactionManager", rollbackFor = Exception.class)
	public Integer createRole(Operator operator, AuthItemDto dto) throws ServiceException {
		validateCreateAuthItem(dto);
		AuthItemPo po = this.dto2po(dto);
		authItemDao.insert(po);
		return po.getId();
	}
	
	private void validateCreateAuthItem(AuthItemDto dto) throws ServiceException {
		if (dto == null) {
			throw new ServiceException(AccountExceptionCode.REQUIRED_PARAM);
		}
		//TODO
	}

	private AuthItemPo dto2po(AuthItemDto dto){
		AuthItemPo po = AuthItemPo.builder().build();
		BeanUtils.copyProperties(dto, po);
		return po;
	}

	@Override
	@Transactional(value = "accountTransactionManager", rollbackFor = Exception.class)
	public void updateRole(Operator operator, AuthItemDto dto) throws ServiceException {
		validateUpdateAuthItem(dto);
		AuthItemPo po = this.dto2po(dto);
		authItemDao.update(po);
	}

	private void validateUpdateAuthItem(AuthItemDto dto) throws ServiceException {
		if (dto == null) {
			throw new ServiceException(AccountExceptionCode.REQUIRED_PARAM);
		}
		//TODO
	}

	@Override
	public List<AuthItemDto> getPointList() {
		List<AuthItemPo> authItemPoList = authItemDao.query(AuthItemPoQuery.builder()
				.isDeleted(false).typeList(Lists.newArrayList(AuthItemType.PERMISSION_GROUP.getCode(), AuthItemType.PERMISSION_POINT.getCode())).build());
		List<AuthItemDto> authItemDtoList = Collections.emptyList();
		if (!CollectionUtils.isEmpty(authItemPoList)) {
			authItemDtoList = authItemPoList.stream().map(po -> this.po2dto(po)).collect(Collectors.toList());
		}
		return authItemDtoList;
	}

	@Override
	public List<AuthItemChildDto> getItemChildListInParentIds(List<Integer> parentIds) {
		List<AuthItemChildPo> poList = authItemChildDao.getInParentIds(parentIds);
		List<AuthItemChildDto> dtoList = Collections.emptyList();
		if (!CollectionUtils.isEmpty(poList)) {
			dtoList = poList.stream().map(po -> this.authItemChildPo2Dto(po)).collect(Collectors.toList());
		}
		return dtoList;
	}
	
	private AuthItemChildDto authItemChildPo2Dto(AuthItemChildPo po){
		AuthItemChildDto dto = AuthItemChildDto.builder().build();
		BeanUtils.copyProperties(po, dto);
		return dto;
	}

	@Override
	@Transactional(value = "accountTransactionManager", rollbackFor = Exception.class)
	public void updateRolePoint(Operator operator, Integer roleId, List<Integer> pointIds) throws ServiceException {
		if (roleId == null) {
			throw new ServiceException(AccountExceptionCode.REQUIRED_PARAM);
		}
		this.getAuthItemById(roleId);
		if (CollectionUtils.isEmpty(pointIds)) {
			authItemChildDao.deleteByParentId(roleId);
		}else {
			List<AuthItemPo> authItemPos = authItemDao.query(AuthItemPoQuery.builder()
					.isDeleted(false)
					.itemIdList(pointIds)
					.typeList(Lists.newArrayList(AuthItemType.PERMISSION_POINT.getCode())).build());
			if (pointIds.size() != authItemPos.size()) {
				throw new ServiceException(AuthExceptionCode.ILLEGAL_PARAM);
			}
			List<AuthItemChildPo> poList = authItemChildDao.getInParentIds(Lists.newArrayList(roleId));
			if (CollectionUtils.isEmpty(poList)) {
				authItemChildDao.batchInsert(roleId, pointIds);
			}else {
				authItemChildDao.deleteByParentId(roleId);
				authItemChildDao.batchInsert(roleId, pointIds);
			}
		}
	}

	@Override
	@Transactional(value = "accountTransactionManager", rollbackFor = Exception.class)
	public void updateUserRole(Operator operator, Integer userId, List<Integer> roleIds) throws ServiceException {
		if (userId == null) {
			throw new ServiceException(AccountExceptionCode.REQUIRED_PARAM);
		}
		accountService.getAccount(userId);
		if (CollectionUtils.isEmpty(roleIds)) {
			authAssignmentDao.deleteByUserId(userId);
		}else {
			List<AuthItemPo> authItemPos = authItemDao.query(AuthItemPoQuery.builder()
					.isDeleted(false)
					.itemIdList(roleIds)
					.typeList(Lists.newArrayList(AuthItemType.ROLE.getCode())).build());
			if (roleIds.size() != authItemPos.size()) {
				throw new ServiceException(AuthExceptionCode.ILLEGAL_PARAM);
			}
			List<AuthAssignmentPo> poList = authAssignmentDao.getInUserIds(Lists.newArrayList(userId));
			if (CollectionUtils.isEmpty(poList)) {
				authAssignmentDao.batchInsert(userId, roleIds);
			}else {
				authAssignmentDao.deleteByUserId(userId);
				authAssignmentDao.batchInsert(userId, roleIds);
			}
		}
	}

	@Override
	public Set<String> getPointsByAccountId(Integer accountId) {
		Set<String> points = Collections.emptySet();
		List<AuthAssignmentPo> authAssignmentPos = authAssignmentDao.getInUserIds(Lists.newArrayList(accountId));
		if (!CollectionUtils.isEmpty(authAssignmentPos)) {
			List<Integer> roleIds = authAssignmentPos.stream().map(po -> po.getItemId()).collect(Collectors.toList());
			List<AuthItemChildPo> authItemChildPos = authItemChildDao.getInParentIds(roleIds);
			if (!CollectionUtils.isEmpty(authItemChildPos)) {
				List<Integer> itemIds = authItemChildPos.stream().map(po -> po.getChild()).collect(Collectors.toList());
				List<AuthItemPo> authItemPos = authItemDao.query(AuthItemPoQuery.builder().isDeleted(false).itemIdList(itemIds).build());
				if (!CollectionUtils.isEmpty(authItemPos)) {
					points = authItemPos.stream().map(po -> po.getData()).collect(Collectors.toSet());
				}
			}
		}
		return points;
	}

}