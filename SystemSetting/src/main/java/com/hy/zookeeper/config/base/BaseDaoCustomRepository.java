package com.hy.zookeeper.config.base;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import com.hy.zookeeper.config.common.PageInfo;
@SuppressWarnings("all")
public class BaseDaoCustomRepository<T, ID extends Serializable>
		extends SimpleJpaRepository<T, ID> implements BaseDaoRepository<T, ID> {

	private final EntityManager entityManager;

	/**
	 * 实体类类型(由构造方法自动赋值)
	 */
	private Class<T> entityClass;

	/**
	 * 构造方法，根据实例类自动获取实体类类型
	 */
	public BaseDaoCustomRepository(Class<T> domainClass, EntityManager em) {
		super(domainClass, em);
		//entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		this.entityClass = domainClass;
		this.entityManager=em;
	}
	  
	/*public BaseDaoCustomRepository(final JpaEntityInformation<T, ?> entityInformation, final EntityManager entityManager) {  
	    super(entityInformation, entityManager);  
	    this.entityManager = entityManager;  
	}  */

	/**
	 * 初始化Log4j的一个实例
	 */
	private final Logger logger =  LoggerFactory.getLogger(this.getClass());
	//private static final Logger logger = Logger.getLogger(BaseDaoCustomRepository.class);


	@Override
	public Session getSession() {
		return entityManager.unwrap(Session.class);
	}

	@Override
	public void flush() {
		getSession().flush();
	}

	@Override
	public void clear() {
		getSession().clear();
	}

	//----------------------------HQL Query----------------------------

	@Override
	public List<T> findByHQL(String hql) {
		return createQuery(hql).list();
	}

	@Override
	public T singleByHQL(String hql) {
		return (T)createQuery(hql).uniqueResult();
	}

	@Override
	public List<T> findPageByHQL(String hql, PageInfo pageInfo) {
		// get count
		if(pageInfo.isNotCount()){
			pageInfo.setAllSize(countByHQL(hql));
			if(pageInfo.getAllSize()==0)return new ArrayList<T>();
		}
		//get data
		Query pagequery = createQuery(hql);
		// set page
		pagequery.setFirstResult(pageInfo.getFirstResult());
		pagequery.setMaxResults(pageInfo.getMaxResults());
		// pagequery.set
		return pagequery.list();
	}

	@Override
	public long countByHQL(String hql) {
		String countQlString = "select count(*) " + removeSelect(removeOrders(hql));
		Query query = createQuery(countQlString);
		List<Object> list = query.list();
		if (list.size() > 0){
			return Long.valueOf(list.get(0).toString());
		}else{
			return 0;
		}
	}

	// -------------------------- SQL Query -----------------

	@Override
	public List<T> findBySQL(String sql) {
		SQLQuery query = createSqlQuery(sql);
		query.addEntity(entityClass);
		return query.list();
	}

	@Override
	public List<T> findPageBySQL(String sql, PageInfo pageInfo) {
		// get count	
		if(pageInfo.isNotCount()){
			pageInfo.setAllSize(countBySQL(sql));
			if(pageInfo.getAllSize()==0)return new ArrayList<T>();
		}
		SQLQuery query = createSqlQuery(sql);
		query.addEntity(entityClass);
		// set page
		query.setFirstResult(pageInfo.getFirstResult());
		query.setMaxResults(pageInfo.getMaxResults());

		return query.list();
	}

	@Override
	public SQLQuery findPageBySQLQuery(String sql, PageInfo pageInfo) {
		// get count	
		if(pageInfo.isNotCount()){
			pageInfo.setAllSize(countBySQL(sql));
			if(pageInfo.getAllSize()==0)return getSession().createSQLQuery(sql);
		}
		SQLQuery query = createSqlQuery(sql);
		query.addEntity(entityClass);
		// set page
		query.setFirstResult(pageInfo.getFirstResult());
		query.setMaxResults(pageInfo.getMaxResults());
		return query;
	}

	@Override
	public long countBySQL(String sql) {
//		String countSqlString ="select count(*) from ("+sql+")AAAA";
		String countSqlString = "select count(*) " + removeSelect(removeOrders(sql));
		Query query = createSqlQuery(countSqlString);
		List<Object> list = query.list();
		if (list.size() > 0){
			return Long.valueOf(list.get(0).toString());
		}else{
			return 0;
		}
	}

	@Override
	public int executeSQL(String sql){
		Query query = getSession().createSQLQuery(sql);
		return query.executeUpdate();
	}

	// ----------------------- Criteria Query----------------------------
	@Override
	public List<T> findByCriteria(DetachedCriteria criteria) {
		Criteria querycriteria = criteria.getExecutableCriteria(getSession());
		return querycriteria.list();
	}

	@Override
	public T singleByCriteria(DetachedCriteria criteria) {
		Criteria querycriteria = criteria.getExecutableCriteria(getSession());
		return (T)querycriteria.uniqueResult();
	}

	@Override
	public List<T> findPageByCriteria(DetachedCriteria criteria,
									  PageInfo pageInfo,String orderBy) {
		// get count
		if(pageInfo.isNotCount()){
			pageInfo.setAllSize(countByCriteria(criteria));
			if(pageInfo.getAllSize()==0)return new ArrayList<T>();
		}
		Criteria querycriteria = criteria.getExecutableCriteria(getSession());
		// set page
		querycriteria.setFirstResult(pageInfo.getFirstResult());
		querycriteria.setMaxResults(pageInfo.getMaxResults());

		// order by
		if (StringUtils.isNotBlank(orderBy)){
			for (String order : StringUtils.split(orderBy, ",")){
				String[] o = StringUtils.split(order, " ");
				if (o.length==1){
					criteria.addOrder(Order.asc(o[0]));
				}else if (o.length==2){
					if ("DESC".equals(o[1].toUpperCase())){
						criteria.addOrder(Order.desc(o[0]));
					}else{
						criteria.addOrder(Order.asc(o[0]));
					}
				}
			}
		}
		return querycriteria.list();
	}

	@Override
	public long countByCriteria(DetachedCriteria criteria) {
		Criteria queryCriteria = criteria.getExecutableCriteria(getSession());
		long totalCount = 0;
		try {
			// Get orders
			Field field = CriteriaImpl.class.getDeclaredField("orderEntries");
			field.setAccessible(true);
			List orderEntrys = (List)field.get(queryCriteria);
			// Remove orders
			field.set(queryCriteria, new ArrayList());
			// Get count
			queryCriteria.setProjection(Projections.rowCount());
			totalCount = Long.valueOf(queryCriteria.uniqueResult().toString());
			// Clean count
			queryCriteria.setProjection(null);
			// Restore orders
			field.set(queryCriteria, orderEntrys);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return totalCount;
	}

	// -------------- Query Tools --------------

	/**
	 * 去除qlString的select子句。
	 * @param qlString
	 * @return
	 */
	private String removeSelect(String qlString){
		int beginPos = qlString.toLowerCase().indexOf("from");
		return qlString.substring(beginPos);
	}

	/**
	 * 去除hql的orderBy子句。
	 * @param qlString
	 * @return
	 */
	private String removeOrders(String qlString) {
		Pattern p = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(qlString);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, "");
		}
		m.appendTail(sb);
		return sb.toString();
	}

	/**
	 * 创建 QL 查询对象
	 * @param qlString
	 * @param parameter
	 * @return
	 */
	private Query createQuery(String qlString){
		Query query = getSession().createQuery(qlString);
		return query;
	}

	/**
	 * 创建 SQL 查询对象
	 * @param sqlString
	 * @param parameter
	 * @return
	 */
	private SQLQuery createSqlQuery(String sqlString){
		SQLQuery query = getSession().createSQLQuery(sqlString);
		return query;
	}

	@Override
	public int updateByHQL(String hql) {
		return createQuery(hql).executeUpdate();
	}

	@Override
	public int updateBySql(String sqlString) {
		return createSqlQuery(sqlString).executeUpdate();
	}

	@Override
	public int executeHQL(String hql){
		Query query = getSession().createQuery(hql);
		return query.executeUpdate();
	}

	@Override
	public List<Map<String, Object>> findMapPageBySQL(String sql,PageInfo pageInfo) {
		if(pageInfo==null){
			SQLQuery query = createSqlQuery(sql);
			query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			return query.list();
		}
		if(pageInfo.isNotCount()){
			pageInfo.setAllSize(countBySQL(sql));
			if(pageInfo.getAllSize()==0)return new ArrayList<Map<String, Object>>();
		}
		SQLQuery query = createSqlQuery(sql);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		// set page
		query.setFirstResult(pageInfo.getFirstResult());
		query.setMaxResults(pageInfo.getMaxResults());
		return query.list();
	}
}
