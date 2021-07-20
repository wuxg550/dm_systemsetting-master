package com.hy.zookeeper.config.util;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * 对象转MultiValueMap工具类
 * @author jianweng
 *
 */
public class ObjectToValueMap {

	/**
	 * 通过反射获取对象属性值，填充MultiValueMap
	 * @param object
	 * @return
	 */
	public static MultiValueMap<String, String> getObjectValueMap(Object object){
		MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
		if(object != null){
			try{
				Class<?> clazz = object.getClass();
				// 获取object的属性描述
				PropertyDescriptor[] pds = Introspector.getBeanInfo(clazz,
						Object.class).getPropertyDescriptors();
				for (PropertyDescriptor pd : pds) {
					String name = pd.getName();// 属性名
					Method readMethod = pd.getReadMethod();// get方法
					// 通过get方法获取属性值
					Object value = readMethod.invoke(object);
					// 避免空指针异常
					String s = value == null ? "" : value.toString();
					valueMap.add(name, s);
				}
			}catch(Exception e){
				
			}
			
		}
		return valueMap;
	}
	
}
