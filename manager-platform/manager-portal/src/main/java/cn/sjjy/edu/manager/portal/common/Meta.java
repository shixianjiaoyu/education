package cn.sjjy.edu.manager.portal.common;

import com.wordnik.swagger.annotations.ApiModelProperty;

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
public class Meta {
	@ApiModelProperty(notes = "ID")
	private Integer id;
	@ApiModelProperty(notes = "名称")
	private String name;
	@ApiModelProperty(notes = "是否被选中")
	private boolean isSelected;
}
