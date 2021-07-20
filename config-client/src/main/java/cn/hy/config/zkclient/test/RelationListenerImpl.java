package cn.hy.config.zkclient.test;

import cn.hy.config.zkclient.zknode.interfaces.IRelationListener;

public class RelationListenerImpl implements IRelationListener{

	public void handleDataChange(String data) throws Exception {
		// TODO Auto-generated method stub
		System.out.println(data);
	}

}
