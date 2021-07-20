package com.hy.zookeeper.config.util;


import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

/***
 *  json工具类，简单封装，方便后续的扩展
 *  统一使用该工具类，方便后面更改底层序列化的组件
 * @author fuxinrong
 * @date 2019年5月8日
 */
public class HYJsonUtil {

    private HYJsonUtil() {
        throw new IllegalStateException("Utility class");
      }

    public static String createJson(Object object){
        
        return JSONObject.toJSONString(object,SerializerFeature.WriteDateUseDateFormat);
    }
    
    public static <T> T parseObject(String json,Class<T> objClass){
        
        return JSONObject.parseObject(json, objClass);
    }

	public static <T> List<T> parseArray(String json, Class<T> objClass) {
		return JSONObject.parseArray(json, objClass);
	}

	public static Object parse(String json) {
		return JSONObject.parse(json);
	}
}
