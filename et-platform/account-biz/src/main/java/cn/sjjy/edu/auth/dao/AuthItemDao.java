package cn.sjjy.edu.auth.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.sjjy.edu.auth.po.AuthItemPo;
import cn.sjjy.edu.auth.query.AuthItemPoQuery;

/** 
 * @author Captain
 * @date 2017年1月22日
 */
public interface AuthItemDao {
	List<AuthItemPo> query(@Param("query") AuthItemPoQuery query);

	AuthItemPo getById(@Param("itemId") Integer itemId);

	Integer insert(@Param("po") AuthItemPo po);

	void update(@Param("po") AuthItemPo po);
}
