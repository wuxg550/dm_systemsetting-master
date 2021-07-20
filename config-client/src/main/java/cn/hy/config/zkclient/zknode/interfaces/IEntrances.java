package cn.hy.config.zkclient.zknode.interfaces;

import java.util.List;

/**
 * 入口集合接口
 * @author jianweng
 *
 */
public interface IEntrances extends IServerKeyPoint {
	public <T extends IEntrance> List<T> getEntrances();
}
