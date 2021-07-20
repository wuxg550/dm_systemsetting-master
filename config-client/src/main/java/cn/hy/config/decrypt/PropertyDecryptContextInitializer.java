package cn.hy.config.decrypt;

import java.util.Objects;
import java.util.Properties;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.util.StringUtils;

import cn.hy.config.util.DESUtil;

public class PropertyDecryptContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext>{

	@Override
	public void initialize(ConfigurableApplicationContext context) {
		ConfigurableEnvironment environment = context.getEnvironment();
		
		String decryptCipher =  environment.getProperty(CommonConsts.DECRYPT_CIPHER_KEYNAME);
		String decryptKeys = environment.getProperty(CommonConsts.DECRYPT_KEYS_KEYNAME);
		
		if(Objects.isNull(decryptCipher) || Objects.isNull(decryptKeys)) {
			return;
		}
		
		String[] decryptKeyArr = decryptKeys.split(",");
		Properties properties = new Properties();
		
		for(String decryptKey : decryptKeyArr) {
			String propertyKey = decryptKey.trim();
			if(StringUtils.isEmpty(propertyKey)) {
				continue;
			}
			// 读取属性
			String oldProperty = environment.getProperty(propertyKey);
			if(StringUtils.isEmpty(oldProperty)) {
				continue;
			}
			
			// 解密
			String newProperty = DESUtil.decryptBybase64(oldProperty, decryptCipher);
			// 设置解密后的密码
			properties.setProperty(propertyKey, newProperty);
		}
		
		PropertiesPropertySource propertiesPropertySource = new PropertiesPropertySource("decrypt.property",properties);
		// 将解密后的配置加到多个属性源头部
        environment.getPropertySources().addFirst(propertiesPropertySource);
	}

}
