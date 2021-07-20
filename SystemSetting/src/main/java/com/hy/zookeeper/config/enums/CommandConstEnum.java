package com.hy.zookeeper.config.enums;


/**      
 *	四字命令常量枚举  
 */
public enum CommandConstEnum {

	/**
	 * 输出相关服务配置的详细信息。
	 */
	CONF("conf","conf"),
    /**
     * 列出所有连接到服务器的客户端的完全的连接 / 会话的详细信息。包括“接受 / 发送”的包数量、会话 id 、操作延迟、最后的操作执行等等信息。
     */
	CONS("cons","cons"),
	
	CRST("crst","crst"),
	/**
	 * 列出未经处理的会话和临时节点。
	 */
	DUMP("dump","dump"),
	/**
	 * 输出关于服务环境的详细信息（区别于 conf 命令）。
	 */
	ENVI("envi","envi"),
	//列出未经处理的请求
	//REQS("reqs","reqs"),
	/**
	 * 测试服务是否处于正确状态。如果确实如此，那么服务返回“imok ”，否则不做任何相应。
	 */
	RUOK("ruok","ruok"),
	/**
	 * 
	 */
	SRST("srst","srst"),
	
	SRVR("srvr","srvr"),
	/**
	 * 输出关于性能和连接的客户端的列表。
	 */
	STAT("stat","stat"),
	/**
	 * 列出服务器 watch 的详细信息。
	 */
	WCHS("wchs","wchs"),
	/**
	 * 通过 session 列出服务器 watch 的详细信息，它的输出是一个与watch 相关的会话的列表。
	 */
	WCHC("wchc","wchc"),
	/**
	 * 通过路径列出服务器 watch 的详细信息。它输出一个与 session相关的路径。
	 */
	WCHP("wchp","wchp"),
	
	MNTR("mntr","mntr"),
    
    ;
    /**
     * 值
     */
    private String val;
    /**
     * 描述
     */
    private String msg;

    private CommandConstEnum(String val, String msg) {
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
    public static CommandConstEnum getInstance(String val) {
        for (CommandConstEnum buss : CommandConstEnum.values()) {
            if (buss.getVal().equals(val)) {
                return buss;
            }
        }
        return null;
    }
}
