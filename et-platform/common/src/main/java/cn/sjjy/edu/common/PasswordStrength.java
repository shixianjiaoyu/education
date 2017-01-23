package cn.sjjy.edu.common;

/** 
 * @author Captain
 * @date 2017年1月23日
 */
public enum PasswordStrength {
    LOW(0),
    MIDDLE(1),
    HIGH(2);

    private Integer code;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    PasswordStrength(Integer code) {
        this.code = code;
    }

    public final static PasswordStrength getByCode(Integer code) {
        for (PasswordStrength passwordStrength : PasswordStrength.values()) {
            if (passwordStrength.getCode().equals(code)) {
                return passwordStrength;
            }
        }
        throw new IllegalArgumentException("unknown code.");
    }

    public final static PasswordStrength getByPassword(String password) {
        if (password.matches(regexZST)) {
            return HIGH;
        }
        if (password.matches(regexZS) || password.matches(regexZT) || password.matches(regexST)) {
            return MIDDLE;
        }
        return LOW;
    }

    private final static String regexZT = "\\D*";
    private final static String regexST = "[\\d\\W]*";
    private final static String regexZS = "\\w*";
    private final static String regexZST = "[\\w\\W]*";
}
