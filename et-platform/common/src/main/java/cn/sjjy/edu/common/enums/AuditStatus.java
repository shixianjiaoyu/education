package cn.sjjy.edu.common.enums;

/** 
 * @author Captain
 * @date 2017年1月23日
 */
public enum AuditStatus{
    INIT(1, "待审核"),
    ACCEPT(2, "审核通过"),
    REJECT(3, "审核不通过");

    private int code;
    private String name;

    private AuditStatus(int code, String name) {

        this.code = code;
        this.name = name;
    }

    public static AuditStatus getByCode(int code) {

        for (AuditStatus auditStatus: values()) {
            if (auditStatus.code == code) {
                return auditStatus;
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
