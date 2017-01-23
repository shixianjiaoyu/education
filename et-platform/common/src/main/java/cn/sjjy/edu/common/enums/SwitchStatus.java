package cn.sjjy.edu.common.enums;

/** 
 * @author Captain
 * @date 2017年1月23日
 */
public enum SwitchStatus {

    STARTED(1, "启动"),
    STOPED(2, "暂停"),
	DELETE(3, "删除");

    private int code;

    private String name;

    private SwitchStatus(int code, String name) {

        this.code = code;
        this.name = name;
    }

    public static SwitchStatus getByCode(int code) {
        for (SwitchStatus switchStatus : values()) {
            if (switchStatus.code == code) {
                return switchStatus;
            }
        }
        throw new IllegalArgumentException("unknown code");
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
