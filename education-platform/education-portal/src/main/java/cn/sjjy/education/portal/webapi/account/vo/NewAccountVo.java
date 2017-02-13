package cn.sjjy.education.portal.webapi.account.vo;

import com.wordnik.swagger.annotations.ApiModelProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 
 * @author Captain
 * @date 2017年1月21日
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewAccountVo {
	@ApiModelProperty(notes = "账户名")
    private String username;
	@ApiModelProperty(notes = "密码")
    private String password;
	@ApiModelProperty(notes = "确认密码")
    private String confirmPassword;
	@ApiModelProperty(notes = "验证码")
	private String verifyCode;
}
