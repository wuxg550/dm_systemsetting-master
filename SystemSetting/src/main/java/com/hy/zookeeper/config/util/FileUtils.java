package com.hy.zookeeper.config.util;

import java.io.File;

import org.springframework.util.ResourceUtils;

public class FileUtils {
	
	private FileUtils(){}

	/**
	 * 查找配置文件绝对路径
	 * 
	 * <p>
	 * 1. 先直接当成本地绝对路径查找
	 * 2. 到项目目录classpath下找
	 * 3. 到运行根目录找
	 * </p>
	 * 
	 * 此方法用于解决：以jar启动springboot的情况下，部分文件读取要求绝对路径，无法简单使用classpath
	 * 
	 * @param filepath
	 * @return absolute filepath
	 */
	public static String searchConfigFileAbsolutePath(String filepath) {
		String relaPath = null;
		if(!new File(filepath).exists()) {
			try { // 查找项目路径下
				relaPath = ResourceUtils.getFile("classpath:").getPath() + filepath;
			} catch(Exception e) { // 查找项目根路径下
				relaPath = System.getProperty("user.dir") + filepath;
			}
			return relaPath;
		}else{
			return filepath;
		}
	}
	
}
