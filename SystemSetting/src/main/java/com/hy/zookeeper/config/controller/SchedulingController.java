package com.hy.zookeeper.config.controller;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.web.bind.annotation.RestController;

import com.hy.zookeeper.config.service.INodeDataService;
import com.hy.zookeeper.config.service.IServerRelationService;

@RestController  
@EnableScheduling 
public class SchedulingController implements SchedulingConfigurer{

	@Value("${validate.expression}")
	private String expression;
	
	@Resource
	private IServerRelationService relationService;
	
	@Autowired
	private INodeDataService nodeDataSerice;
	
	@Override
	public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
		//定时任务要执行的方法      
        Runnable task=()->{  
                nodeDataSerice.syncNodeData();
                relationService.validateRelation();
        };  
        //调度实现的时间控制  
        Trigger trigger=triggerContext->{  
                CronTrigger cronTrigger=new CronTrigger(expression);  
                return cronTrigger.nextExecutionTime(triggerContext);  
        };  
        scheduledTaskRegistrar.addTriggerTask(task, trigger);
	}

}
