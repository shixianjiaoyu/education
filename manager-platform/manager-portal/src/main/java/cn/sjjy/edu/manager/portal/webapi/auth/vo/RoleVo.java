package cn.sjjy.edu.manager.portal.webapi.auth.vo;

import java.util.List;

import com.wordnik.swagger.annotations.ApiModelProperty;

import cn.sjjy.edu.manager.portal.common.Meta;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 
 * @author Captain
 * @date 2017年1月22日
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleVo {
	@ApiModelProperty(notes = "角色ID")
	private Integer id;
	@ApiModelProperty(notes = "角色名称")
	private String name;
	@ApiModelProperty(notes = "描述")
	private String description;
	@ApiModelProperty(required = false, notes = "成员")
	private List<Meta> memberList;
}
