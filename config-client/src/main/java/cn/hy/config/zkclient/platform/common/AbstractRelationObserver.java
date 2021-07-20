package cn.hy.config.zkclient.platform.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.hy.config.zkclient.zknode.RelationConfig;

/**
 * 流向数据变更订阅处理类
 * @author jianweng
 *
 */
public abstract class AbstractRelationObserver {

	public static final Logger logger = LoggerFactory.getLogger(AbstractRelationObserver.class);
	
	/**
	 * 源服务消费功能码 
	 */
	private String srcConsumerFc;
	
	public String getSrcConsumerFc() {
		return srcConsumerFc;
	}

	/**
	 * @param srcConsumerFc 源服务消费功能码 
	 */
	public AbstractRelationObserver(String srcConsumerFc) {
		super();
		this.srcConsumerFc = srcConsumerFc;
	}

	/**
	 * 调用处理方法，吞掉异常
	 * @param relation
	 */
	public void agentHandle(RelationConfig relation){
		try{
			handle(relation);
		}catch(Exception e){
			logger.error("处理流向变更异常", e);
		}
	}
	
	/**
	 * srcConsumerFc对应流向变更处理方法，请尽量避免耗时处理，否则影响其他流向的更新
	 * @param relation
	 */
	public abstract void handle(RelationConfig relation);
}
