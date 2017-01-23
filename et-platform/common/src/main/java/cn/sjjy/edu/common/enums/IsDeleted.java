package cn.sjjy.edu.common.enums;


/** 
 * @author Captain
 * @date 2017年1月23日
 */
public enum IsDeleted {

    VALID(0, "有效"),
    DELETED(1, "删除");

    private int code;

    private String name;

    private IsDeleted(int code, String name) {

        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
