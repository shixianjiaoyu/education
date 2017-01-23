package cn.sjjy.edu.log.po;

import java.sql.Timestamp;

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
public class LogOperationVo {
    private Long id;
    private String tableName;
    private Integer type;
    private String operatorUsername;
    private Integer operatorType;
    private String value;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Integer isDeleted;
}
