package cn.sjjy.edu.manager.portal.webapi.auth;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import cn.sjjy.edu.account.common.AuthItemType;
import cn.sjjy.edu.account.dto.AccountDto;
import cn.sjjy.edu.account.service.IAccountService;
import cn.sjjy.edu.auth.annotation.SolarAuth;
import cn.sjjy.edu.auth.dto.AuthItemChildDto;
import cn.sjjy.edu.auth.dto.AuthItemDto;
import cn.sjjy.edu.auth.service.IAuthItemService;
import cn.sjjy.edu.common.exception.ServiceException;
import cn.sjjy.edu.manager.portal.common.Meta;
import cn.sjjy.edu.manager.portal.webapi.auth.vo.PointGroupVo;
import cn.sjjy.edu.manager.portal.webapi.auth.vo.PointVo;
import cn.sjjy.edu.manager.portal.webapi.auth.vo.RolePointVo;
import cn.sjjy.edu.manager.portal.webapi.auth.vo.RoleVo;
import cn.sjjy.edu.manager.portal.webapi.auth.vo.UserRoleVo;
import cn.sjjy.edu.web.framework.controller.BaseController;
import cn.sjjy.edu.web.framework.core.Context;
import cn.sjjy.edu.web.framework.core.Response;

/**
 * @author Captain
 * @date 2017年1月22日
 */
@Controller
@RequestMapping("/web_api/v1/auth")
@Api(value = "/auth", description = "权限相关")
public class AuthController extends BaseController {
	@Autowired
	private IAccountService accountService;

	@Autowired
	private IAuthItemService authItemService;

	@SolarAuth(points = {"cpm_view_role"})
	@ApiOperation("角色列表")
	@RequestMapping(value = "/roles", method = RequestMethod.GET)
	public @ResponseBody Response<List<RoleVo>> roleList(Context context) throws ServiceException {
		List<AuthItemDto> dtoList = authItemService.getRoleList();
		List<RoleVo> roleVoList = Collections.emptyList();
		if (!CollectionUtils.isEmpty(dtoList)) {
			List<Integer> itemIds = dtoList.stream().map(AuthItemDto::getId).collect(Collectors.toList());
			Map<Integer, List<AccountDto>> itemUserMap = authItemService.getItemUserMapInItemIds(itemIds);
			roleVoList = dtoList.stream().map(dto -> {
				List<Meta> metas = Collections.emptyList();
				if (itemUserMap.containsKey(dto.getId())) {
					metas = itemUserMap.get(dto.getId()).stream()
							.map(o -> Meta.builder().id(o.getAccountId()).name(o.getUsername()).build())
							.collect(Collectors.toList());
				}
				return RoleVo.builder().id(dto.getId()).name(dto.getName()).description(dto.getDescription())
						.memberList(metas).build();
			}).collect(Collectors.toList());
		}
		return Response.SUCCESS(roleVoList);
	}

	@ApiOperation("角色下拉列表")
	@RequestMapping(value = "/roles/drop_box", method = RequestMethod.GET)
	public @ResponseBody Response<List<Meta>> listRoleDropBox(Context context) throws ServiceException {
		List<AuthItemDto> dtoList = authItemService.getRoleList();
		List<Meta> metaList = Collections.emptyList();
		if (!CollectionUtils.isEmpty(dtoList)) {
			metaList = dtoList.stream().map(dto -> Meta.builder().id(dto.getId()).name(dto.getName()).build())
					.collect(Collectors.toList());
		}
		return Response.SUCCESS(metaList);
	}

	// @SolarAuth(points = {"cpm_modify_account"})
	@ApiOperation("新建角色")
	@RequestMapping(value = "/roles", method = RequestMethod.POST)
	public @ResponseBody Response<Integer> createRole(Context context, @RequestBody RoleVo roleVo)
			throws ServiceException {
		AuthItemDto role = AuthItemDto.builder().name(roleVo.getName()).description(roleVo.getDescription())
				.type(AuthItemType.ROLE.getCode()).data("").build();
		Integer roleId = authItemService.createRole(super.getOperator(context), role);
		return Response.SUCCESS(roleId);
	}

	// @SolarAuth(points = {"cpm_view_account"})
	@ApiOperation("角色详情")
	@RequestMapping(value = "/roles/{roleId}", method = RequestMethod.GET)
	public @ResponseBody Response<RoleVo> roleInfo(@PathVariable("roleId") Integer roleId) throws ServiceException {
		AuthItemDto dto = authItemService.getAuthItemById(roleId);
		return Response.SUCCESS(
				RoleVo.builder().id(dto.getId()).name(dto.getName()).description(dto.getDescription()).build());
	}

	// @SolarAuth(points = {"cpm_modify_sales"})
	@ApiOperation("编辑角色")
	@RequestMapping(value = "/roles", method = RequestMethod.PUT)
	public @ResponseBody Response<Object> updateRole(Context context, @RequestBody RoleVo roleVo)
			throws ServiceException {
		AuthItemDto role = AuthItemDto.builder().id(roleVo.getId()).name(roleVo.getName())
				.description(roleVo.getDescription()).type(AuthItemType.ROLE.getCode()).data("").build();
		authItemService.updateRole(super.getOperator(context), role);
		return Response.SUCCESS(null);
	}

	@ApiOperation("角色所拥有的权限列表")
	@RequestMapping(value = "/roles/{roleId}/points", method = RequestMethod.GET)
	public @ResponseBody Response<List<PointGroupVo>> rolePointList(Context context,
			@PathVariable("roleId") Integer roleId) throws ServiceException {
		List<AuthItemDto> pointList = authItemService.getPointList();
		List<PointGroupVo> pointGroupVoList = Collections.emptyList();
		if (!CollectionUtils.isEmpty(pointList)) {
			Map<Integer, String> itemMap = Maps.newHashMap();
			List<Integer> itemIds = Lists.newArrayList(roleId);
			for (AuthItemDto dto : pointList) {
				itemMap.put(dto.getId(), dto.getName());
				if (AuthItemType.PERMISSION_GROUP.equals(AuthItemType.getByCode(dto.getType()))) {
					itemIds.add(dto.getId());
				}
			}
			List<AuthItemChildDto> itemChildList = authItemService.getItemChildListInParentIds(itemIds);
			if (!CollectionUtils.isEmpty(itemChildList)) {
				Map<Integer, List<AuthItemChildDto>> itemChildMap = itemChildList.stream()
						.collect(Collectors.groupingBy(AuthItemChildDto::getParent));
				List<AuthItemChildDto> roleChildList = itemChildMap.get(roleId);
				List<Integer> roleChildIds = roleChildList.stream().map(AuthItemChildDto::getChild)
						.collect(Collectors.toList());

				itemChildMap.remove(roleId);
				List<Integer> groupIds = itemChildMap.keySet().stream().sorted().collect(Collectors.toList());

				pointGroupVoList = Lists.newArrayListWithCapacity(groupIds.size());
				List<AuthItemChildDto> authItemChildDtos = Collections.emptyList();
				String groupName = null;
				for (Integer itemId : groupIds) {
					authItemChildDtos = itemChildMap.get(itemId);
					groupName = itemMap.get(itemId);
					pointGroupVoList.add(PointGroupVo.builder()
							.id(itemId)
							.name(groupName)
							.pointList(itemChild2PointVo(authItemChildDtos, itemMap, roleChildIds))
							.build());
				}

			}
		}

		return Response.SUCCESS(pointGroupVoList);
	}
	
	private List<PointVo> itemChild2PointVo(List<AuthItemChildDto> authItemChildDtos, Map<Integer, String> itemMap, List<Integer> roleChildIds){
		return authItemChildDtos.stream().map(dto -> PointVo.builder()
				.id(dto.getChild())
				.name(itemMap.get(dto.getChild()))
				.isSelected(roleChildIds.contains(dto.getChild())).build()).collect(Collectors.toList());
	}
	
	@ApiOperation("编辑角色关联的权限")
	@RequestMapping(value = "/roles/points", method = RequestMethod.PUT)
	public @ResponseBody Response<Object> updateRolePoints(Context context, @RequestBody RolePointVo rolePointVo)
			throws ServiceException {
		authItemService.updateRolePoint(super.getOperator(context), rolePointVo.getRoleId(), rolePointVo.getPointIds());
		return Response.SUCCESS(null);
	}
	
	@ApiOperation("编辑用户的角色")
	@RequestMapping(value = "/users/roles", method = RequestMethod.PUT)
	public @ResponseBody Response<Object> updateUserRole(Context context, @RequestBody UserRoleVo userRoleVo)
			throws ServiceException {
		authItemService.updateUserRole(super.getOperator(context), userRoleVo.getUserId(), userRoleVo.getRoleIds());
		return Response.SUCCESS(null);
	}

}
