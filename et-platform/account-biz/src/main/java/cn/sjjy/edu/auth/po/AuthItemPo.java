package cn.sjjy.edu.auth.po;

import java.sql.Timestamp;

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
public class AuthItemPo {
	private Integer id;
	private String name;
	private Integer type;
	private String data;
	private String description;
	private Integer isDeleted;
	private Timestamp createdTime;
	private Timestamp updatedTime;
}
