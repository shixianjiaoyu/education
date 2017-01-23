package cn.sjjy.edu.manager.portal.webapi.auth.vo;

import java.util.List;

import com.wordnik.swagger.annotations.ApiModelProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 
 * @author Captain
 * @date 2017年1月23日
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RolePointVo {
	@ApiModelProperty(notes = "角色ID")
	private Integer roleId;
	@ApiModelProperty(notes = "权限点ID")
	private List<Integer> pointIds;
}
