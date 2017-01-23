package cn.sjjy.edu.common;

/** 
 * @author Captain
 * @date 2017年1月23日
 */
public enum DbTable {

	ACC_ACCOUNT("acc_account"),
	ACC_ACCOUNT_BILI_USER("acc_account_bili_user"),
	ACC_ACCOUNT_WALLET("acc_account_wallet"),
	LAU_CAMPAIGN("lau_campaign"),
	LAU_UNIT("lau_unit")
	;

    private String name;
    
    private DbTable(String name) {
    	this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
