package cn.sjjy.edu.account.po;

import java.sql.Timestamp;

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
public class AccountPo {
    private Integer accountId;
    private String username;
    private String mobile;
    private Integer passwordStrength;
    private String salt;
    private String saltPassword;
    private Integer status;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Integer isDeleted;
}
