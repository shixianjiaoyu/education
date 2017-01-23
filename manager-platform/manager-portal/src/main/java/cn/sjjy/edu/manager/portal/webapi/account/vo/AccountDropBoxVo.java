package cn.sjjy.edu.manager.portal.webapi.account.vo;

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
public class AccountDropBoxVo {
    private Integer account_id;
    private String username;
}
