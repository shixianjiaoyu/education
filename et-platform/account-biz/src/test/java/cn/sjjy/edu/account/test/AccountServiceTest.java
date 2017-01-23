package cn.sjjy.edu.account.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;

import cn.sjjy.edu.account.dto.NewAccountDto;
import cn.sjjy.edu.account.service.IAccountService;
import cn.sjjy.edu.common.bean.Operator;
import cn.sjjy.edu.common.enums.OperatorType;
import cn.sjjy.edu.common.exception.ServiceException;

/** 
 * @author Captain
 * @date 2017年1月21日
 */
public class AccountServiceTest extends BaseTest {
    private final static Logger LOGGER = LoggerFactory.getLogger(AccountServiceTest.class);
    @Autowired
    private IAccountService accountService;

    @Test
    public void getAccountDtoMapInAccountIds() throws Exception {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        LOGGER.info(JSON.toJSONString(accountService.getAccountDtoMapInAccountIds(list)));
    }

    @Test
    public void enableAccount() throws Exception {
        Operator operator = new Operator("cuihaichuan", OperatorType.OPERATING_PERSONNEL);
        accountService.enableAccount(8, operator);
    }

    @Test
    public void disableAccount() throws Exception {
        Operator operator = new Operator("cuihaichuan", OperatorType.OPERATING_PERSONNEL);
        accountService.disableAccount(8, operator);
    }

    @Test
    public void updateUsername() throws Exception {
        Operator operator = new Operator("cuihaichuan", OperatorType.OPERATING_PERSONNEL);
        accountService.updateUsername(8, "doudou", operator);
    }

    @Test
    public void createAccount() throws ServiceException {
        Operator operator = new Operator("cuihaichuan", OperatorType.OPERATING_PERSONNEL);
        List<String> bili = new ArrayList<>();
        bili.add("cuihaichuan");
        accountService.createAccount(NewAccountDto.builder()
                .username("cuihaichuan")
                .password("1qaz@WSX")
                .confirmPassword("1qaz@WSX")
                .build(), operator);
    }

    @Test
    public void getAccount() throws ServiceException {
        LOGGER.info(accountService.getAccount(2).toString());
    }

    @Test
    public void login() throws ServiceException {
        LOGGER.info(accountService.login("hahahahaha", "1qaz@WSX").toString());
    }

    @Test
    public void updateSales() throws ServiceException {
        List<String> bili = new ArrayList<>();
        bili.add("cuihaichuan");
        bili.add("zhongyuan");
        accountService.updateSales(8, bili, new Operator("cuihaichuan", OperatorType.OPERATING_PERSONNEL));
    }

//    @Test
//    public void updatePassword() throws ServiceException {
//        accountService.updatePassword(UpdatePasswordDto.builder().accountId(1)
//        .oldMd5Password(Md5Util.md5Hash("1qaz@WSX")).newPassword("3edc$RFV").confirmPassword("3edc$RFV").build());
//
//        accountService.login("15221111111", Md5Util.md5Hash("3edc$RFV"));
//    }

//    public void updateAccountInfo(UpdateAccountDto accountInfoDto) throws ServiceException {
//
//    }
//
//    public void getAccount(Integer accountId) throws ServiceException {
//
//    }
//
//    public void login(String mobile, String md5Password) throws ServiceException {
//
//    }
//
//    public void updatePassword(UpdatePasswordDto passwordDto) throws ServiceException {
//
//    }
}
