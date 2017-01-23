package cn.sjjy.edu.web.framework.core;

/**
 * Created by fanwenbin on 16/9/19.
 */
public class Context {
    private Integer accountId;
    private String username;
    private Integer salesType;

    public Context(Integer accountId, String username, Integer salesType) {
        this.accountId = accountId;
        this.username = username;
        this.salesType = salesType;
    }

    public Context(String username) {
        this.username = username;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

	public Integer getSalesType() {
		return salesType;
	}

	public void setSalesType(Integer salesType) {
		this.salesType = salesType;
	}

}
