package com.hy.zookeeper.config.systemconfig;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hy.zookeeper.config.util.ZookeeperUtil;

@WebListener
public class MyServletContextListener implements ServletContextListener{

	private static final Logger logger = LoggerFactory.getLogger(MyServletContextListener.class);
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		logger.info("关闭zookeeper连接。。。");
		try{
			if(ZookeeperUtil.getCuratorClient() != null){
				ZookeeperUtil.getCuratorClient().close();
			}
		}catch(Exception e){
			logger.error("关闭zookeeper连接异常", e);
		}
		
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		logger.info("项目启动。。。");
	}

}
