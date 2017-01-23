package cn.sjjy.edu.common.enums;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** 
 * @author Captain
 * @date 2017年1月23日
 */
public enum Status {

    VALID(1, "有效"),
    INVALID(2, "无效");

    private int code;

    private String name;

    public static final List<Integer> VALID_STATUS_LIST;

    static {
        List<Integer> integerList = new ArrayList<>(1);
        integerList.add(VALID.getCode());
        VALID_STATUS_LIST = Collections.unmodifiableList(integerList);
    }

    private Status(int code, String name) {

        this.code = code;
        this.name = name;
    }

    public static Status getByCode(int code) {

        for (Status status : values()) {
            if (status.getCode() == code) {
                return status;
            }
        }

        throw new IllegalArgumentException("unknown status code.");
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
