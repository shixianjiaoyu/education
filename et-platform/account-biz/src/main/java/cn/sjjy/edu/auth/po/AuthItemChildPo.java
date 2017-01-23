package cn.sjjy.edu.auth.po;

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
public class AuthItemChildPo {
	private Integer id;
	private Integer parent;
	private Integer child;
}
