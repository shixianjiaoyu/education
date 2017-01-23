package cn.sjjy.edu.common.enums;

/** 
 * @author Captain
 * @date 2017年1月23日
 */
public enum OperatorType {

	CUSTOMER(0, "普通用户"),
	OPERATING_PERSONNEL(1, "运营人员"),
    SYSTEM(2,"系统");

    private int code;

    private String name;

    private OperatorType(int code, String name) {

        this.code = code;
        this.name = name;
    }

    public static OperatorType getByCode(int code) {

        for (OperatorType operatorType : values()) {
            if (operatorType.getCode() == code) {
                return operatorType;
            }
        }

        return null;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
