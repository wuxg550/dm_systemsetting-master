package com.hy.zookeeper.config.enums;
/**      
 * 节点类型枚举  
 * @version    
 */
public enum NodeModelEnum {
	/**
	 * LEADER
	 */
	PERSISTENT("PERSISTENT","持久节点"),
    /**
     * FLOWER
     */
	EPHEMERAL("EPHEMERAL","临时节点"),
    
    ;
    /**
     * 值
     */
    private String val;
    /**
     * 描述
     */
    private String msg;

    private NodeModelEnum(String val, String msg) {
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
    public static NodeModelEnum getInstance(String val) {
        for (NodeModelEnum buss : NodeModelEnum.values()) {
            if (buss.getVal().equals(val)) {
                return buss;
            }
        }
        return null;
    }
}
