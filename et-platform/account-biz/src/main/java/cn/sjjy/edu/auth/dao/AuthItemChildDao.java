package cn.sjjy.edu.auth.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.sjjy.edu.auth.po.AuthItemChildPo;

/** 
 * @author Captain
 * @date 2017年1月23日
 */
public interface AuthItemChildDao {

	List<AuthItemChildPo> getInParentIds(@Param("parentIds") List<Integer> parentIds);

	void deleteByParentId(@Param("parentId") Integer parentId);

	void batchInsert(@Param("parentId") Integer parentId, @Param("pointIds") List<Integer> pointIds);
}
