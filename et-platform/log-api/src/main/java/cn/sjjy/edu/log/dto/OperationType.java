package cn.sjjy.edu.log.dto;

/** 
 * @author Captain
 * @date 2017年1月21日
 */
public enum OperationType {
    INSERT(0),
    DELETE(1),
    UPDATE(2),
    SELECT(3),;
    private Integer code;

    OperationType(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}

