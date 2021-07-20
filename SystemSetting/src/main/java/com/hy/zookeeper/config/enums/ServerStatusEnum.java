package com.hy.zookeeper.config.enums;


/**      
 * 服务状态Enum
 * @version    
 */
public enum ServerStatusEnum {

	/**
	 * 在线
	 */
	ONLINE("ONLINE","在线"),
    /**
     * 离线
     */
	OFFLINE("OFFLINE","离线"),
	/**
	 * 异常
	 */
	EXCEPTIOM("EXCEPTIOM","异常"),
    
    ;
    /**
     * 值
     */
    private String val;
    /**
     * 描述
     */
    private String msg;

    private ServerStatusEnum(String val, String msg) {
        this.val = val;
        this.msg = msg;
    }
   
    public String getVal() {
        return val;
    }
    
    public String getMsg() {
        return msg;
    }
   
    public String getString(){
        return this.val;
    }
    public static ServerStatusEnum getInstance(String val) {
        for (ServerStatusEnum buss : ServerStatusEnum.values()) {
            if (buss.getVal().equals(val)) {
                return buss;
            }
        }
        return null;
    }
}
