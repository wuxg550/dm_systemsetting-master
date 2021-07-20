package cn.hy.config.decrypt;

import java.util.Objects;
import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.util.StringUtils;

import cn.hy.config.util.DESUtil;

public class EncrypPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer{

	@Override
	protected void processProperties(
			ConfigurableListableBeanFactory beanFactoryToProcess,
			Properties props) throws BeansException {
		String decryptCipher =  props.getProperty(CommonConsts.DECRYPT_CIPHER_KEYNAME);
		String decryptKeys = props.getProperty(CommonConsts.DECRYPT_KEYS_KEYNAME);
		
		if(Objects.nonNull(decryptCipher) && Objects.nonNull(decryptKeys)) {
			String[] decryptKeyArr = decryptKeys.split(",");
			
			for(String decryptKey : decryptKeyArr) {
				String propertyKey = decryptKey.trim();
				if(StringUtils.isEmpty(propertyKey)) {
					continue;
				}
				
				// 读取属性
				String oldProperty = props.getProperty(propertyKey);
				if(StringUtils.isEmpty(oldProperty)) {
					continue;
				}
				
				// 解密
				String newProperty = DESUtil.decryptBybase64(oldProperty, decryptCipher);
				props.setProperty(decryptKey, newProperty);
			}
		}
		
		super.processProperties(beanFactoryToProcess, props);
	}

}
