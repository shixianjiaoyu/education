package cn.sjjy.edu.common;

/** 
 * @author Captain
 * @date 2017年1月23日
 */
public enum AccountStatus {
    NULL(-1, "未开通"),
    ON(0, "启用"),
    OFF(1, "冻结"),
    ARREARAGE(2, "余额不足");

    private String desc;
    private Integer code;

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    AccountStatus(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }


}
