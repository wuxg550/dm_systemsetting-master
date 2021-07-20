package cn.hy.config.zkclient.util;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * 对象比较工具类
 * @author jianweng
 *
 */
public class ObjectCompareUtil {

	/**
	 * 比较相同类型的两个实例属性值是否一样，不同类型直接返回false，
	 * 属性值一样返回true
	 * @param obj1
	 * @param obj2
	 * @param ignoreArr
	 * @return
	 */
	public static boolean compareFields(Object obj1, Object obj2, String... ignoreArr){
		if(obj1 != null && obj2 != null){
			try{
				List<String> ignoreList = null;
				if(ignoreArr != null && ignoreArr.length > 0){
					// array转化为list
					ignoreList = Arrays.asList(ignoreArr);
				}
				if (obj1.getClass() == obj2.getClass()) {
					Class<?> clazz = obj1.getClass();
					// 获取object的属性描述
					PropertyDescriptor[] pds = Introspector.getBeanInfo(clazz,
							Object.class).getPropertyDescriptors();
					for (PropertyDescriptor pd : pds) {
						String name = pd.getName();// 属性名
						if(ignoreList != null && ignoreList.contains(name)){// 如果当前属性选择忽略比较，跳到下一次循环
							continue;
						}
						Method readMethod = pd.getReadMethod();// get方法
						// 在obj1上调用get方法等同于获得obj1的属性值
						Object o1 = readMethod.invoke(obj1);
						// 在obj2上调用get方法等同于获得obj2的属性值
						Object o2 = readMethod.invoke(obj2);
						String s1 = o1 == null ? "" : o1.toString();//避免空指针异常
	                    String s2 = o2 == null ? "" : o2.toString();//避免空指针异常
	                    if (!s1.equals(s2)) {
	                    	return false;
	                    }
					}
					return true;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		return false;
	}
	
}
