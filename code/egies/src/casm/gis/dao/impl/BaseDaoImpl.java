package casm.gis.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.hibernate4.support.HibernateDaoSupport;

import casm.gis.retrieve.api.FullTextService;
import casm.gis.retrieve.api.ServerFactory;
import casm.gis.dao.BaseDao;

/*
 * Create BaseDao interface internship class
 * 2017-05-15 01:42:53
 */
public class BaseDaoImpl extends HibernateDaoSupport implements BaseDao {
	
	@Override
	public void addObject(Object object) {
		this.getHibernateTemplate().save(object);
	}

	@Override
	public void updateObject(Object object) {
		this.getHibernateTemplate().update(object);
	}

	@Override
	public void deleteObject(Object object) {
		this.getHibernateTemplate().delete(object);
	}

	@Override
	public Object getObjectById(Class clazz, int id) {
		return this.getHibernateTemplate().get(clazz, id);
	}

	@Override
	public List<Object> getAllObject(Class clazz) {
		String className = clazz.getSimpleName();
		String queryString = "from "+className;
		return this.getHibernateTemplate().find(queryString);
	}

	@Override
	public List<Object> getAllObject(Class clazz, String id, boolean isAsc) {
		String className = clazz.getSimpleName();
		String queryString = "";
		if(isAsc){
			queryString = "from "+className+" order by "+id+" asc";
		}else{
			queryString = "from "+className+" order by "+id+" desc";
		}
		return this.getHibernateTemplate().find(queryString);
	}

	@Override
	public List<Object> getAllObject(String className, String id, boolean isAsc) {
		String queryString = "";
		if(isAsc){
			queryString = "from "+className+" order by "+id+" asc";
		}else{
			queryString = "from "+className+" order by "+id+" desc";
		}
		return this.getHibernateTemplate().find(queryString);
	}

	@Override
	public List<Object> pagination(int pageNow, int pageSize, String queryString) {
		return this.currentSession().createQuery(queryString).setFirstResult((pageNow-1)*pageSize).setMaxResults(pageSize).list();
	}

	@Override
	public List<Object> listPagination(int pageNow, int pageSize,
			List<Object> list) {
		List<Object> result = new ArrayList<Object>();
		int temp = (pageNow-1)*pageSize+pageSize;
		if(temp > list.size()){
			temp = list.size();
		}
		for(int i=(pageNow-1)*pageSize;i<temp;i++){
			result.add(list.get(i));
		}
		return result;
	}

	@Override
	public FullTextService getSearchService(String type, String serverName,
			String flag, String className, String indexPath, String url) {
		FullTextService searchService = null;
		Map<String,String> params = new HashMap<String,String>();
		params.put("type", type);
		params.put("serverName", serverName);
		params.put("flag", flag);
		params.put("className", className);
		params.put("indexPath", indexPath);
		params.put("url", url);
		ServerFactory serverFactory = new ServerFactory();
		searchService = serverFactory.beginService(params);
		searchService.setServerName(serverName);
		return searchService;
	}
}
