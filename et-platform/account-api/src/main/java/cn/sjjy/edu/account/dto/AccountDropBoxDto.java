package cn.sjjy.edu.account.dto;

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
public class AccountDropBoxDto {
    private Integer accountId;
    private String username;
}
