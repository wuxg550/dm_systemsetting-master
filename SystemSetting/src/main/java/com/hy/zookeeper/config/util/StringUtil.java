package com.hy.zookeeper.config.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class StringUtil extends StringUtils{

	/**
	 * 去掉空格、回车、制表符等
	 * @param str
	 * @return
	 */
	public static String replaceBlank(String str) {
		String dest = "";
		if (str!=null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}
}
