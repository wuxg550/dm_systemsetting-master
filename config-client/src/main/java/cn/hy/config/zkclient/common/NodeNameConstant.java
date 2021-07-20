package cn.hy.config.zkclient.common;

public class NodeNameConstant {
	/**
	 * 节点分隔符
	 */
	public static final String SEPARATOR = "/"; 
	
	/**
	 * 节点名
	 */
	public static final String SERVERS = "/servers"; 
	public static final String RELATIONCONFIG = "/relationConfig"; 
	public static final String FUNELEMENT = "/funElement"; 
	
	public static final String PROVIDER = "/provider";
	public static final String CONSUMER = "/consumer";
	public static final String INSTANCE ="/instance";
	
	public static final String INFO = "/info";
	public static final String IP = "/address";
	public static final String ENTRANCE = "/entrance";
	public static final String REGISTEDFUNTIOANS = "/registedFunctions";
	public static final String RELATION = "/relation";
	public static final String RUNNINGCONFIG = "/runningConfig";
	public static final String ONLINE = "/online";
	
	/**
	 * 服务功能节点
	 */
	public static final String FUNCTION_CODES = "/functionCodes";
	
	/**
	 * 获取/functionCodes下功能码子节点   如：/S4/functionCodes/FC_XXXX_001
	 * @param region 域，如：S4
	 * @param fc 功能码，如：FC_XXXX_001
	 * @return
	 */
	public static String getFunctionCodesFcPath(String region, String fc){
		return SEPARATOR + region + FUNCTION_CODES + SEPARATOR + fc;
	}
}
