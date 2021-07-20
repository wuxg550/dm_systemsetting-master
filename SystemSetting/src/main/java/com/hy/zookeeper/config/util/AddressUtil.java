package com.hy.zookeeper.config.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddressUtil {

	protected static final Logger logger = LoggerFactory.getLogger(AddressUtil.class);
	
	private AddressUtil(){}
	
	public static final String LOCAL_ADDRESS = "127.0"+".0.1";
	
	public static String getLocalAddress(){
		Enumeration<?> enumeration;
		try {
			enumeration = NetworkInterface.getNetworkInterfaces();
			InetAddress ip=null;
	        while(enumeration.hasMoreElements()){
	            NetworkInterface netInterface = (NetworkInterface) enumeration.nextElement();
	            Enumeration<?> addresses = netInterface.getInetAddresses();
	            while (addresses.hasMoreElements()) {
	                ip = (InetAddress) addresses.nextElement();
	                if (ip != null && ip instanceof Inet4Address 
	                		&& !LOCAL_ADDRESS.equals(ip.getHostAddress()) && !"localhost".equals(ip.getHostAddress())){
	                    return ip.getHostAddress();
	                } 
	            }
	        }
		} catch (SocketException e) {
			logger.error("获取ip异常, 使用默认ip地址："+LOCAL_ADDRESS, e);
		}
        
        return LOCAL_ADDRESS;
	}
}
