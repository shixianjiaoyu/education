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
public class PointGroupVo {
	@ApiModelProperty(notes = "权限组ID")
	private Integer id;
	@ApiModelProperty(notes = "权限组名称")
	private String name;
	@ApiModelProperty(notes = "权限点集合")
	private List<PointVo> pointList;
}
