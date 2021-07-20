package com.hy.zookeeper.config.base;

import org.hibernate.SQLQuery;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.hy.zookeeper.config.TestBase;
import com.hy.zookeeper.config.common.PageInfo;
import com.hy.zookeeper.config.common.RelationMockData;
import com.hy.zookeeper.config.dao.ServerInfoRepsotory;
import com.hy.zookeeper.config.entity.ServerInfo;
import com.hy.zookeeper.config.util.UUIDTOOL;

public class BaseDaoCustomRepositoryTest extends TestBase{

	@Autowired
	private ServerInfoRepsotory serverInfoRepsotory;
	private PageInfo pageInfo = null;
	private String sql = "SELECT * FROM BASISDATA1.platform_server_info";
	
	@Before
	public void init(){
		pageInfo = new PageInfo();
		pageInfo.setPage(1);
		pageInfo.setPageSize(10);
	}
	
	@Test
	public void updateByHQL(){
		String hql = String.format("UPDATE ServerInfo SET serverName='%s' WHERE id='%s'"
				, "MySql"+UUIDTOOL.getuuid(4), RelationMockData.mysqlServerId);
		Assert.assertTrue(serverInfoRepsotory.updateByHQL(hql) > 0);
	}
	
	@Test
	public void singleByHQL(){
		String hql = String.format("FROM ServerInfo WHERE id='%s'"
				, RelationMockData.mysqlServerId);
		Assert.assertNotNull(serverInfoRepsotory.singleByHQL(hql));
	}
	
	@Test
	public void singleByCriteria(){
		DetachedCriteria criteria = DetachedCriteria.forClass(ServerInfo.class);
		criteria.add(Restrictions.eq("id", RelationMockData.mysqlServerId));
		Assert.assertNotNull(serverInfoRepsotory.singleByCriteria(criteria));
	}
	
	@Test
	public void findPageBySQLQuery(){
		pageInfo.setAllSize(-1);
		SQLQuery sqlQuery = serverInfoRepsotory.findPageBySQLQuery(sql, pageInfo);
		Assert.assertTrue(pageInfo.getAllSize() > -1);
		Assert.assertFalse(sqlQuery.list().isEmpty());
	}
	
	@Test
	public void findPageByCriteria(){
		pageInfo.setAllSize(-1);
		DetachedCriteria criteria = DetachedCriteria.forClass(ServerInfo.class);
		String orderBy = " serverName, serverType asc, serverIp desc ";
		Assert.assertFalse(serverInfoRepsotory.findPageByCriteria(criteria, pageInfo, orderBy).isEmpty());
	}
	
	@Test
	public void findMapPageBySQL(){
		pageInfo.setAllSize(-1);
		Assert.assertFalse(serverInfoRepsotory.findMapPageBySQL(sql, pageInfo).isEmpty());
	}
	
	@Test
	public void findBySQL(){
		pageInfo.setAllSize(-1);
		Assert.assertFalse(serverInfoRepsotory.findBySQL(sql).isEmpty());
	}
	
	@Test
	public void findByCriteria(){
		DetachedCriteria criteria = DetachedCriteria.forClass(ServerInfo.class);
		Assert.assertFalse(serverInfoRepsotory.findByCriteria(criteria).isEmpty());
	}
	
	@Test
	public void countByCriteria(){
		DetachedCriteria criteria = DetachedCriteria.forClass(ServerInfo.class);
		Assert.assertTrue(serverInfoRepsotory.countByCriteria(criteria) > 0);
	}
}
