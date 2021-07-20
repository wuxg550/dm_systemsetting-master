package cn.hy.config.zkclient.zknode.interfaces;

import java.util.List;

import cn.hy.config.zkclient.zknode.RelationConfig;

/**
 * 功能节点处理类
 * @author jianweng
 *
 */
public interface IFunctionCodesListener {

	/**
	 * 服务上下线等发布功能时触发
	 * @param fc 功能码
	 * @param relations 无数据则返回空列表，流向数据，数据中只有目标服务的信息
	 */
	void handleDataChange(String fc, List<RelationConfig> relations);
}
