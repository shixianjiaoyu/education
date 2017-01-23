package cn.sjjy.edu.manager.portal.webapi.auth.vo;

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
public class PointVo {
	@ApiModelProperty(notes = "权限点ID")
	private Integer id;
	@ApiModelProperty(notes = "权限点名称")
	private String name;
	@ApiModelProperty(notes = "是否被选中")
	private Boolean isSelected;
}
