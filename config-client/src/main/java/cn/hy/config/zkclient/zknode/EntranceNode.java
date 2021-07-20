package cn.hy.config.zkclient.zknode;

import java.util.List;

import cn.hy.config.zkclient.zknode.interfaces.IEntrance;

/**
 * 入口传输类
 * @author jianweng
 *
 */
public class EntranceNode implements IEntrance {

	//端口
	private Integer port;
	//入口协议类型(HTTP_POST,HTTP_GET,TCP,SDK)
	private String protocol;
	//入口地址
	private String url;
	
	// 功能码集合
	private List<String> fcs;
	
	private String userName;
	private String password;
	
	
	
	public EntranceNode(){}
	public EntranceNode(IEntrance entrance) {
		this.port = entrance.getPort();
		this.protocol = entrance.getProtocol();
		this.url = entrance.getUrl();
		this.fcs = entrance.getFcs();
		this.userName = entrance.getUserName();
		this.password = entrance.getPassword();
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<String> getFcs() {
		return fcs;
	}

	public void setFcs(List<String> fcs) {
		this.fcs = fcs;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
