package com;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;


/**
 * SpringBoot方式启动类
 *
 * @author lcn
 * @Date 2018/1/10 10:06
 */
@EnableAutoConfiguration
@Configuration
@ComponentScan("cn.hy.authori")
@ComponentScan("com.hy.zookeeper.config")//指定扫描包
@SpringBootApplication
@EnableScheduling
@ServletComponentScan
public class Application extends SpringBootServletInitializer {
    protected static final  Logger log = LoggerFactory.getLogger(Application.class);

    /**
	 * 上下文对象
	 */
	private static ApplicationContext ac;
 
    public static ApplicationContext getApplicationContext() {
		return ac;
	}
    
    
    
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
    	return builder.sources(Application.class);
    }
    
    public static void main(String[] args){
    	ApplicationContext ctx = SpringApplication.run(Application.class, args);
        ac = ctx;
        log.info("Application is success!");
    }   
   
}
