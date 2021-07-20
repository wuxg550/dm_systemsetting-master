package cn.hy.config.zkclient.platform.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.hy.config.zkclient.util.ObjectCompareUtil;
import cn.hy.config.zkclient.zknode.RelationConfig;

/**
 * 流向内存缓存类
 * @author Administrator
 *
 */
public class RelationCache {
	
	private static Logger logger = LoggerFactory.getLogger(RelationCache.class);
	/**
	 * Map内存缓存
	 */
	private static Map<String, List<RelationConfig>> relationMap = new ConcurrentHashMap<>();

	public static Map<String, List<AbstractRelationObserver>> observerMap = new ConcurrentHashMap<>();
	
	/**
	 * 根据源服务消费功能码获取流向
	 * @param fc
	 * @return
	 */
	public static List<RelationConfig> getRelationList(String fc){
		if(relationMap!=null&&relationMap.get(fc)!=null){
			return relationMap.get(fc);
		}
		return null;
	}
	
	/**
	 * 清理流向
	 */
	public static void clearRelationList(){
		if(relationMap!=null){
			relationMap.clear();
		}
	}
	
	public static void resetRelationList(List<RelationConfig> myRelationList) {
		try {
			Map<String, List<RelationConfig>> relations = new ConcurrentHashMap<>();
			if(myRelationList!=null){
				for (RelationConfig myRelation : myRelationList) {
					List<RelationConfig> relationlist = relations.get(myRelation.getSrcConsumerFc());
					if(relationlist==null){
						relationlist = new ArrayList<>();
						relations.put(myRelation.getSrcConsumerFc(), relationlist);
					}
					relationlist.add(myRelation);
					compareAndHandlRelation(myRelation.getSrcConsumerFc(), myRelation);
				}
			}
			//清理流向
			clearRelationList();
			//重设
			relationMap.putAll(relations);
		}catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	/**
	 * 判断并处理流向
	 * 存在相同流向返回true
	 * @param srcFc
	 * @param relation
	 * @return
	 */
	private static boolean compareAndHandlRelation(String srcFc, RelationConfig relation){
		List<RelationConfig> relations = relationMap.get(srcFc);
		if(relations != null && !relations.isEmpty()){
			for(RelationConfig r : relations){
				// 若存在相同的流向，直接返回true
				if(ObjectCompareUtil.compareFields(r, relation)){
					return true;
				}
			}
		}
		
		// 调用已订阅此流向的处理方法
		List<AbstractRelationObserver> observers = observerMap.get(srcFc);
		if(observers != null && !observers.isEmpty()){
			for(AbstractRelationObserver o : observers){
				o.agentHandle(relation);
			}
		}
		return false;
	}
	
	/**
	 * 新增一个流向变更处理类，相同的srcConsumerFc可存在多个处理类
	 * @param observer
	 */
	public static void addObserver(AbstractRelationObserver observer){
		List<AbstractRelationObserver> observers = observerMap.get(observer.getSrcConsumerFc());
		if(observers == null){
			observers = new ArrayList<>();
			observerMap.put(observer.getSrcConsumerFc(), observers);
		}
		observers.add(observer);
	}
	
	/**
	 * 移除流向变更处理类
	 * @param srcConsumerFc
	 */
	public static void removeObserver(String srcConsumerFc){
		observerMap.remove(srcConsumerFc);
	}
}
