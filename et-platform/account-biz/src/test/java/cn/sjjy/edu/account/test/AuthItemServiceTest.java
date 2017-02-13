package cn.sjjy.edu.account.test;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;

import cn.sjjy.edu.account.common.AuthItemType;
import cn.sjjy.edu.auth.dto.AuthItemDto;
import cn.sjjy.edu.auth.service.IAuthItemService;
import cn.sjjy.edu.common.bean.Operator;
import cn.sjjy.edu.common.enums.OperatorType;
import cn.sjjy.edu.common.exception.ServiceException;

/** 
 * @author Captain
 * @date 2017年1月22日
 */
public class AuthItemServiceTest extends BaseTest {
    private final static Logger LOGGER = LoggerFactory.getLogger(AuthItemServiceTest.class);
    @Autowired
    private IAuthItemService authItemService;
    
    private Operator operator;
    
    @Before
    public void init(){
    	operator = Operator.builder().operatorName("cuihaichuan").operatorType(OperatorType.CUSTOMER).build();
    }

    @Test
    public void queryTest() throws Exception {
    	System.out.println(authItemService.getRoleList());
    }
    
    @Test
    public void getItemUserMapInItemIdsTest() throws ServiceException{
    	System.out.println(authItemService.getItemUserMapInItemIds(Lists.newArrayList(1, 116, 117)));
    }
    
    @Test
    public void getAuthItemByIdTest() throws ServiceException{
    	System.out.println(JSON.toJSONString(authItemService.getAuthItemById(1)));
    }
    
    @Test
    public void createRoleTest() throws ServiceException{
    	AuthItemDto dto = AuthItemDto.builder()
		.name("test-role")
		.type(AuthItemType.ROLE.getCode())
		.data("")
		.description("测试权限").build();
    	System.out.println(authItemService.createRole(operator, dto));
    	
    }
    
    @Test
    public void updateRoleTest() throws ServiceException{
    	AuthItemDto dto = AuthItemDto.builder()
		.id(147)
		.name("test-role-tes")
		.type(AuthItemType.ROLE.getCode())
		.data("")
		.description("测试权限-1999").build();
    	authItemService.updateRole(operator, dto);
    }
    
    @Test
    public void updateRolePoint() throws ServiceException{
    	authItemService.updateRolePoint(operator, 147, Lists.newArrayList(138));
    }

}
