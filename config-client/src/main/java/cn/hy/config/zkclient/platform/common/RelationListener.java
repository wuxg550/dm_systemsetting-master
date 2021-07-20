package cn.hy.config.zkclient.platform.common;

import java.util.List;

import com.alibaba.fastjson.JSON;

import cn.hy.config.zkclient.zknode.RelationConfig;
import cn.hy.config.zkclient.zknode.interfaces.IRelationListener;

/**
 * IRelationListener实现类
 * 监听流向变化并作缓存
 * @author jianweng
 *
 */
public class RelationListener implements IRelationListener{

	@Override
	public void handleDataChange(String data) throws Exception {
		List<RelationConfig> relations = JSON.parseArray(data, RelationConfig.class);
		if(relations!=null){
			RelationCache.resetRelationList(relations);
		}
	}

}
