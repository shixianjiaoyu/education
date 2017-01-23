package cn.sjjy.edu.account.component;

import java.security.SecureRandom;
import java.util.Random;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import cn.sjjy.edu.account.exception.AccountExceptionCode;
import cn.sjjy.edu.common.exception.ServiceException;
import cn.sjjy.edu.common.util.Md5Util;
import sun.misc.BASE64Encoder;

/** 
 * @author Captain
 * @date 2017年1月21日
 */
@Component
public final class PasswordComponent {
    private final static Logger LOGGER = LoggerFactory.getLogger(PasswordComponent.class);
    private static final int MIN_LEN = 8;
    private static final int MAX_LEN = 16;

    public void confirmPassword(String password, String confirmPassword) throws ServiceException {
        if (!(password.equals(confirmPassword))) {
            LOGGER.info("illegal confirm newPassword");
            throw new ServiceException(AccountExceptionCode.ILLEGAL_CONFIRM_PASSWORD);
        }
    }

    public String createSaltPassword(String password, String salt) {
        return Md5Util.md5Hash(Md5Util.md5Hash(password)+salt);
    }

    public String encodeSaltMd5Password(String md5Password, String salt) {
        return Md5Util.md5Hash(Md5Util.md5Hash(md5Password)+salt);
    }

    /**
     * 生成含有随机盐的密码
     */
    public String getSalt() {
        Random RANDOM = new SecureRandom();
        byte[] salt = new byte[8];
        RANDOM.nextBytes(salt);
        return new BASE64Encoder().encode(salt);
    }


    public void validPasswordSafe(String password) throws ServiceException {
        if (validNotNull(password) &&
                validLen(password) &&
                validLetter(password) &&
                validSpecialChar(password)) {
            return;
        }
        throw new ServiceException(AccountExceptionCode.ILLEGAL_PASSWORD);
    }

    private boolean validNotNull(String password) {
        return !StringUtils.isEmpty(password);
    }

    private boolean validLen(String password) {
        return password.length() >= MIN_LEN && password.length() <= MAX_LEN;
    }

    private boolean validLetter(String password) {
        return Pattern.compile("(?i)[a-z]").matcher(password).find();
    }

    private boolean validSpecialChar(String password) {
        for (int i = 0; i < SPECIAL_CHARAS.length; i++)
            if (password.indexOf(SPECIAL_CHARAS[i]) != -1)
                return true;

        return false;
    }

    //~ ` ! @ # $ % ^ & * ( ) _ - + ={ } [ ] \ | : ; " ' < > , . ? / " "
    private static final char[] SPECIAL_CHARAS = new char[]
            {
                    '~', '`', '!', '@', '#', '$', '%', '^',
                    '&', '*', '(', ')', '_', '+', '=', '{',
                    '}', '[', ']', '\\', '|', ':', ';', '"',
                    '\'', '<', '>', ',', '.', '?', '/'
            };
}
