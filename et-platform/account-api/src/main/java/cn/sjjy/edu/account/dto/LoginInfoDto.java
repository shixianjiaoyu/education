package cn.sjjy.edu.account.dto;

import java.util.Set;

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
public class LoginInfoDto {
    private Integer accountId;
    private String userName;
    private String accessToken;
    private Set<String> points;
}
