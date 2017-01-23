package cn.sjjy.edu.auth.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.sjjy.edu.auth.po.AuthAssignmentPo;

/** 
 * @author Captain
 * @date 2017年1月22日
 */
public interface AuthAssignmentDao {
	List<AuthAssignmentPo> getInItemIds(@Param("itemIds") List<Integer> itemIds);

	List<AuthAssignmentPo> getInUserIds(@Param("userIds") List<Integer> userIds);

	void batchInsert(@Param("userId") Integer userId, @Param("roleIds") List<Integer> roleIds);

	void deleteByUserId(@Param("userId") Integer userId);
}
