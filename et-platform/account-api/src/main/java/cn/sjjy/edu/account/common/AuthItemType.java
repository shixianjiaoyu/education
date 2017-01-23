package cn.sjjy.edu.account.common;

import cn.sjjy.edu.common.exception.ServiceException;

public enum AuthItemType {

    ROLE(0, "角色"), 
    PERMISSION_GROUP(1, "权限组"),
    PERMISSION_POINT(2, "权限点");

    private Integer code;

    private String desc;

    AuthItemType(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public Integer getCode() {
        return code;
    }

    public final static AuthItemType getByCode(Integer code) throws ServiceException {
        for (AuthItemType type : AuthItemType.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        throw new ServiceException("Unknown AuthItemType code.");
    }

}
