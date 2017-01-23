package cn.sjjy.edu.auth.query;

import java.util.Collection;

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
public class AuthItemPoQuery {

    private Collection<Integer> itemIdList;

    private Collection<Integer> typeList;

    private Boolean isDeleted;

	/*public Collection<Integer> getItemIdList() {
		return itemIdList;
	}

	public AuthItemPoQuery setItemIdList(Collection<Integer> itemIdList) {
		this.itemIdList = itemIdList;
		return this;
	}

	public Collection<Integer> getTypeList() {
		return typeList;
	}

	public AuthItemPoQuery setTypeList(Collection<Integer> typeList) {
		this.typeList = typeList;
		return this;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public AuthItemPoQuery setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
		return this;
	}*/

    
}
