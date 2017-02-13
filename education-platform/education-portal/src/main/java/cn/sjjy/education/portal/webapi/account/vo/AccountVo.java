package cn.sjjy.education.portal.webapi.account.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
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
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class AccountVo {
    @ApiModelProperty(notes = "账户ID")
    private Integer account_id;
    @ApiModelProperty(notes = "登陆用户名")
    private String username;
    @ApiModelProperty(notes = "客户名")
    private String customer_name;
}
