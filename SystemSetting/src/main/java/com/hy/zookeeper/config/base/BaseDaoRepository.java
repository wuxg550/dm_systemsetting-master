package com.hy.zookeeper.config.base;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.hy.zookeeper.config.common.PageInfo;
@NoRepositoryBean
public interface BaseDaoRepository<T, ID extends Serializable>
		extends JpaRepository<T, ID>{

	public Session getSession();
	/**
	 * 强制与数据库同步
	 */
	public void flush();
	/**
	 * 清楚缓存数据
	 */
	public void clear();

	//----------------------------HQL Query----------------------------
	/**
	 * 返回 hql 查询结果集
	 * @param hql
	 * @return
	 */
	public List<T> findByHQL(String hql);
	/**
	 * 返回 hql 查询的单个对象，多个时抛出异常
	 * @param hql
	 * @return
	 */
	public T singleByHQL(String hql);

	/**
	 * 返回 符合hql查询结果的指定页结果 
	 * @param hql
	 * @param pageInfo 分页信息对象，为了防止对同一条件重复统计记录总数，请保持该对象的状态。
	 * 如果条件有变化，要重新统计总记录数，必须重新设置allSize为-1
	 * @return
	 */
	public List<T> findPageByHQL(String hql,PageInfo pageInfo);
	/**
	 * 统计符合hql记录数
	 * @param hql
	 * @return
	 */
	public long countByHQL(String hql);

	/**
	 * 更新 By HQL
	 * @param hql
	 * @return
	 */
	public int updateByHQL(String hql);

	/**
	 * 执行HQL语句，返回受影响的行数
	 * @param hql
	 * @return
	 */
	public int executeHQL(String hql);

	// -------------------------- SQL Query -----------------
	/**
	 * 返回 sql 查询结果集
	 * @param sql
	 * @return
	 */
	public List<T> findBySQL(String sql);


	/**
	 * 返回 符合sql查询结果的指定页结果
	 * @param sql
	 * @param pageInfo 分页信息对象，为了防止对同一条件重复统计记录总数，请保持该对象的状态。
	 * 如果条件有变化，要重新统计总记录数，必须重新设置allSize为-1
	 * @return
	 */
	public List<T> findPageBySQL(String sql,PageInfo pageInfo);

	/**
	 * 返回SQLQuery 符合sql查询结果的指定页结果
	 * @param sql
	 * @param pageInfo
	 * @return
	 */
	public SQLQuery findPageBySQLQuery(String sql, PageInfo pageInfo);

	/**
	 * 统计符合sql记录数
	 * @param sql
	 * @return
	 */
	public long countBySQL(String sql);
	/**
	 * 执行SQL更新语句
	 * @param sqlString
	 * @return
	 */
	public int updateBySql(String sqlString);

	/**
	 * 执行SQL语句，返回受影响的行数
	 * @param sql
	 * @return
	 */
	public int executeSQL(String sql);
	// ----------------------- Criteria Query----------------------------

	/**
	 * 返回 criteria 查询结果集
	 * @param sql
	 * @return
	 */
	public List<T> findByCriteria(DetachedCriteria criteria);
	/**
	 * 返回 criteria 查询的单个对象，多个时抛出异常
	 * @param criteria
	 * @return
	 */
	public T singleByCriteria(DetachedCriteria criteria);

	/**
	 * 返回 符合 criteria 查询结果的指定页结果
	 * @param criteria
	 * @param pageInfo 分页信息对象，为了防止对同一条件重复统计记录总数，请保持该对象的状态。
	 * 如果条件有变化，要重新统计总记录数，必须重新设置allSize为-1
	 * @param orderBy abc desc,ded asc (多个排序半角逗号分开)
	 * @return
	 */
	public List<T> findPageByCriteria(DetachedCriteria criteria,PageInfo pageInfo,String orderBy);

	/**
	 * 统计符合criteria记录数
	 * @param criteria
	 * @return
	 */
	public long countByCriteria(DetachedCriteria criteria);

	/**
	 * 返回 List<Map<String,Object>> 查询分页结果
	 * @param sql
	 * @param pageInfo
	 * @return
	 */
	public List<Map<String,Object>> findMapPageBySQL(String sql,PageInfo pageInfo);
}
