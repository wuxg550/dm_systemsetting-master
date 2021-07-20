package cn.hy.config.zkclient.zknode;

/**
 * 流向传输类
 * @author jianweng
 *
 */
public class RelationNode{

	//源服务消费功能码 
	private String srcConsumerFc;
	//目标服务id
	private String destServerId;
	//流向名
	private String relationName;
	//源服务类型 
	private String srcServerType;
	//源服务id 
	private String srcServerId;
	//目标服务类型 
	private String destServerType;
	//目标提供功能码 
	private String destProviderFc;
	//目标服务入口id 
	private String destEntranceId;
	//目标服务ip地址
	private String destIp;
	//目标服务端口
	private String destPort;
	//目标服务入口协议 
	private String destProtocol;
	//目标服务URL
	private String destUrl;
	
	private String userName;
	
	private String password;
	
	/**
	 *  目标服务在离线标识  0在线，1离线
	 */
	private Integer destOnlineStatus;
	
	public String getRelationName() {
		return relationName;
	}
	public void setRelationName(String relationName) {
		this.relationName = (relationName != null)? relationName.trim() : null;
	}
	public String getSrcServerType() {
		return srcServerType;
	}
	public void setSrcServerType(String srcServerType) {
		this.srcServerType = (srcServerType != null)? srcServerType.trim() : null;
	}
	public String getSrcServerId() {
		return srcServerId;
	}
	public void setSrcServerId(String srcServerId) {
		this.srcServerId = (srcServerId != null)? srcServerId.trim() : null;
	}
	public String getSrcConsumerFc() {
		return srcConsumerFc;
	}
	public void setSrcConsumerFc(String srcConsumerFc) {
		this.srcConsumerFc = (srcConsumerFc != null)? srcConsumerFc.trim() : null;
	}
	/**
	 * 
	 * @return 目标服务服务类型
	 */
	public String getDestServerType() {
		return destServerType;
	}
	public void setDestServerType(String destServerType) {
		this.destServerType = (destServerType != null)? destServerType.trim() : null;
	}
	/**
	 * @return 目标服务id
	 */
	public String getDestServerId() {
		return destServerId;
	}
	public void setDestServerId(String destServerId) {
		this.destServerId = (destServerId != null)? destServerId.trim() : null;
	}
	/**
	 * 目标服务功能码
	 */
	public String getDestProviderFc() {
		return destProviderFc;
	}
	public void setDestProviderFc(String destProviderFc) {
		this.destProviderFc = (destProviderFc != null)? destProviderFc.trim() : null;
	}
	public String getDestEntranceId() {
		return destEntranceId;
	}
	public void setDestEntranceId(String destEntranceId) {
		this.destEntranceId = (destEntranceId != null)? destEntranceId.trim() : null;
	}
	/**
	 * 目标服务ip
	 */
	public String getDestIp() {
		return destIp;
	}
	public void setDestIp(String destIp) {
		this.destIp = (destIp != null)? destIp.trim() : null;
	}
	/**
	 * 目标服务端口
	 */
	public String getDestPort() {
		return destPort;
	}
	public void setDestPort(String destPort) {
		this.destPort = (destPort != null)? destPort.trim() : null;
	}
	/**
	 * 目标服务协议
	 */
	public String getDestProtocol() {
		return destProtocol;
	}
	public void setDestProtocol(String destProtocol) {
		this.destProtocol = (destProtocol != null)? destProtocol.trim() : null;
	}
	/**
	 * 目标服务url
	 */
	public String getDestUrl() {
		return destUrl;
	}
	public void setDestUrl(String destUrl) {
		this.destUrl = (destUrl != null)? destUrl.trim() : null;
	}
	/**
	 * 目标服务用户名
	 */
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = (userName != null)? userName.trim() : null;
	}
	/**
	 * 目标服务密码
	 */
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = (password != null)? password.trim() : null;
	}
	
	/**
	 * 目标服务在离线标识  0在线，1离线
	 * @return destOnlineStatus
	 */
	public Integer getDestOnlineStatus() {
		return destOnlineStatus;
	}
	
	/**
	 * 目标服务在离线标识 0在线，1离线
	 */
	public void setDestOnlineStatus(Integer destOnlineStatus) {
		this.destOnlineStatus = destOnlineStatus;
	}
}
