package com.hy.zookeeper.config.common;

import com.hy.zookeeper.config.entity.ServerEntrance;
import com.hy.zookeeper.config.entity.ServerRelation;
import com.hy.zookeeper.config.util.UUIDTOOL;

public class RelationMockData {

	public static final String[] serverTypes = new String[]{"BaseDataManage"};
	public static final String[] serverIds = new String[]{"7083aa1f2cf84e9ca0764d28e136ee6a"};
	
	public static final String mysqlFc = "FC_GET_MYSQL_CONNECTIONSTRING_0001";
	public static final String mysqlServerId = "634f37c1649d4b85b0c61e74f80d096d";
	
	public static final String GISMAP_SERVERID = "b11abee21900459b98f50409f5ce2ade";
	
	public static ServerRelation relation = new ServerRelation();
	
	public static ServerRelation getRelationMockData(ServerEntrance entrance){
		relation.setId(UUIDTOOL.getuuid(32));
		relation.setDestEntranceId(entrance.getId());
		relation.setDestIp("10.7.2.12");
		relation.setDestOnlineStatus(0);
		relation.setDestProviderFc(mysqlFc);
		relation.setSrcConsumerFc(mysqlFc);
		relation.setDestServerId(mysqlServerId);
		relation.setDestServerType(entrance.getServerType());
		relation.setRelationName("hhh");
		relation.setSrcServerId(serverIds[0]);
		relation.setSrcServerType(serverTypes[0]);
		
		return relation;
	}
	
	public static final String split1 = "_HsplitY_"; // 分割标识1，分割导图节点间信息
	public static final String split2 = "_HlinetoY_"; // 分割标识2，分割两个导图节点
	public static final String providerMarker = "_HpY_"; // 提供功能标识
	public static final String consumerMarker = "_HcY_"; // 消费功能标识
	
	public static String getRelationLineId(ServerRelation relation){
		return relation.getSrcServerType() + RelationMockData.split1
				+ relation.getSrcServerId() + RelationMockData.split1 + relation.getSrcConsumerFc()
				+ RelationMockData.split2 + relation.getDestServerType() + RelationMockData.split1
				+ relation.getDestServerId() + RelationMockData.split1 + relation.getDestProviderFc();
	}
}
