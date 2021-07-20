package com.hy.zookeeper.config.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hy.zookeeper.config.client.CuratorClient;
import com.hy.zookeeper.config.common.TreeNode;
import com.hy.zookeeper.config.common.ZkNode;
import com.hy.zookeeper.config.entity.Zkserver;
import com.hy.zookeeper.config.service.IZktreeService;
import com.hy.zookeeper.config.util.ZookeeperUtil;

@Service
public class ZktreeServiceImpl implements IZktreeService {

	@Override
	public List<Zkserver> finAllServer() {
		return new ArrayList<>();
	}

	@Override
	public List<TreeNode> getZkTree(String path, String ip, String port){
	    CuratorClient curator = ZookeeperUtil.getCuratorClient();
		List<TreeNode> treeList = new ArrayList<>();
		String pathString = "";
		if(path!=null && "".equals(path)){
			TreeNode tn = new TreeNode();
			tn.setText("zookeeper  ("+ip+":"+port+")");
			tn.setId("/");
			tn.setpId("#");
			path = "/";
			if(curator.haveClidren(path)){
				tn.setChildren(true);
			}else{
				tn.setChildren(false);
			}
			treeList.add(tn);
		}else{
			List<String> childrenList = curator.getClildrenList(path);
			if(!"/".equals(path)){
				pathString = "/";
			}
			for (String string : childrenList) {
				TreeNode tn = new TreeNode();
				tn.setId(path+pathString+string);
				tn.setpId(path);
				tn.setText(string);
				if(curator.haveClidren(path+pathString+string)){
					tn.setChildren(true);
				}else{
					tn.setChildren(false);
				}
				treeList.add(tn);
			}
		}
		return treeList;
	}

	@Override
	public List<ZkNode> getZKTreeData(String path) {

		return new ArrayList<>();
	}



}
