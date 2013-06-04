package com.hk.impl.dao;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.engine.EntityKey;
import org.hibernate.engine.PersistenceContext;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.persister.entity.EntityPersister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.akube.framework.dao.Page;
import com.hk.pact.dao.BaseDao;

/**
 * @author vaibhav.adlakha
 */
@Repository
@Primary
@SuppressWarnings("unchecked")
public class BaseDaoImpl extends HibernateDaoSupport implements BaseDao {

    @Autowired
    @Qualifier("sessionFactory")
    public void init(SessionFactory factory) {
        setSessionFactory(factory);
    }

    @SuppressWarnings("unchecked")
    public boolean containsKey(Class entityClass, Serializable key) {
        return containsKey(entityClass.getName(), key);
    }

    public boolean containsKey(final String entityName, final Serializable key) {
        Boolean contains = (Boolean) getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                SessionImplementor sessionImpl = (SessionImplementor) session;
                return containsKey(entityName, key, sessionImpl);
            }
        });
        return contains;
    }

    public void delete(Object entity) {
        prepareHibernateForWrite();
        getHibernateTemplate().delete(entity);
        resetHibernateAfterWrite();
    }

    @SuppressWarnings("unchecked")
    public void deleteAll(Collection entities) {
        prepareHibernateForWrite();
        getHibernateTemplate().deleteAll(entities);
        resetHibernateAfterWrite();
    }

    private boolean containsKey(String entityName, Serializable key, SessionImplementor sessionImpl) {
        // if key is null then there is no object possible
        if (key == null) {
            return false;
        }
        // get the entity persisted for this entity type
        EntityPersister persister = sessionImpl.getFactory().getEntityPersister(entityName);
        // create the possible entity key
        EntityKey keyToLoad = new EntityKey(key, persister, sessionImpl.getEntityMode());
        // then search in the persistence context
        PersistenceContext persistenceContext = sessionImpl.getPersistenceContext();
        return persistenceContext.containsEntity(keyToLoad);
    }

    @SuppressWarnings("unchecked")
    public List findByQuery(String queryString) {
        return getHibernateTemplate().find(queryString);
    }

    @SuppressWarnings("unchecked")
    public List findByQuery(String queryString, Object... bindings) {
        return getHibernateTemplate().find(queryString, bindings);
    }

    // @Override
    @SuppressWarnings("unchecked")
    public Object findUnique(String queryString) {
        List<Object> results = getHibernateTemplate().find(queryString);

        if (results.size() == 1) {
            return results.get(0);
        }
        return null;
    }

    /*
     * @SuppressWarnings("unchecked") @Override
     */
    /*
     * public List findByNamedParams(String namedQuery, Map<String, Object> namedParams) { // create the query Query
     * query = getSession().createQuery(namedQuery); // set the bindings if (namedParams != null && namedParams.size() >
     * 0) { for (Map.Entry<String, Object> namedParamEntry : namedParams.entrySet()) {
     * query.setParameter(namedParamEntry.getKey(), namedParamEntry.getValue()); } } return query.list(); }
     */

    @SuppressWarnings("unchecked")
    public List find(String queryString, Object[] bindings, int startRow, int maxRows) {
        // create the query
        Query query = getSession().createQuery(queryString);
        // set the bindings
        if (bindings != null) {
            for (int i = 0; i < bindings.length; i++) {
                query.setParameter(i, bindings[i]);
            }
        }
        // and the boundaries
        query.setFirstResult(startRow);
        if (maxRows != 0) {
            query.setMaxResults(maxRows);
        }
        return query.list();

    }

    public List findByNamedQuery(String namedQuery) {
        return getHibernateTemplate().findByNamedQuery(namedQuery);
    }

    @Override
    public List findByNamedQueryAndNamedParam(String queryString, String[] paramNames, Object[] paramValues) {
        return getHibernateTemplate().findByNamedQueryAndNamedParam(queryString, paramNames, paramValues);
    }

    @SuppressWarnings("unchecked")
    public List findByCriteria(DetachedCriteria criteria) throws DataAccessException {
        return getHibernateTemplate().findByCriteria(criteria);
    }

    @SuppressWarnings("unchecked")
    public List findByCriteria(DetachedCriteria criteria, int firstResult, int maxResults) throws DataAccessException {
        return getHibernateTemplate().findByCriteria(criteria, firstResult, maxResults);
    }

    @SuppressWarnings( { "unchecked", "hiding" })
    public <T> List<T> findByExample(T object) {
        return getHibernateTemplate().findByExample(object);
    }

    @SuppressWarnings("unchecked")
    public List findByNamedParams(String queryString, String[] params, Object[] values) {
        return getHibernateTemplate().findByNamedParam(queryString, params, values);
    }

    public Object findUniqueByNamedParams(String queryString, String[] params, Object[] values) {
        List<Object> results = getHibernateTemplate().findByNamedParam(queryString, params, values);

        if (results.size() == 1) {
            return results.get(0);
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    public Object findUnique(String queryString, Object[] binds) {

        List<Object> results = getHibernateTemplate().find(queryString, binds);

        if (results.size() == 1) {
            return results.get(0);
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    public Object findUniqueByNamedQueryAndNamedParam(String queryString, String[] paramNames, Object[] paramValues) {
        List<Object> results = getHibernateTemplate().findByNamedQueryAndNamedParam(queryString, paramNames, paramValues);

        if (results.size() == 1) {
            return results.get(0);
        }

        return null;
    }

    @SuppressWarnings( { "unchecked", "hiding" })
    public <T> T get(Class<T> c, Serializable key) {
        return (T) getHibernateTemplate().get(c, key);
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getAll(Class<T> c) {
        return (List<T>) getHibernateTemplate().find("from " + c.getName());
    }

    public <T> List<T> getAll(Class<T> c, List<Long> ids, String idPropertyName) {
        if (ids == null || ids.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        DetachedCriteria criteria = DetachedCriteria.forClass(c).add(Restrictions.in(idPropertyName, ids));

        return findByCriteria(criteria);
    }

    @SuppressWarnings("unchecked")
    public <T> T load(Class<T> c, Serializable key) {
        return (T) getHibernateTemplate().load(c, key);
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> loadAll(Class<T> c) {
        return (List<T>) getHibernateTemplate().loadAll(c);
    }

    public Object save(Object entity) {
        prepareHibernateForWrite();
        Object updatedEntity = getHibernateTemplate().merge(entity);
        resetHibernateAfterWrite();
        return updatedEntity;
    }

    private void prepareHibernateForWrite() {
        getHibernateTemplate().getSessionFactory().getCurrentSession().setFlushMode(FlushMode.AUTO);
        getHibernateTemplate().setCheckWriteOperations(false);
    }

    private void resetHibernateAfterWrite() {
        getHibernateTemplate().flush();
        getHibernateTemplate().getSessionFactory().getCurrentSession().setFlushMode(FlushMode.MANUAL);
        getHibernateTemplate().setCheckWriteOperations(true);
    }

    /**
     * WARNING: Make sure that you should not access any lazy initialization in this session after calling this method
     */
    public void clearSession() {
        getHibernateTemplate().clear();
    }

    public void saveOrUpdate(Object entity) {
        prepareHibernateForWrite();
        getHibernateTemplate().saveOrUpdate(entity);
        resetHibernateAfterWrite();
    }

    @SuppressWarnings("unchecked")
    public void saveOrUpdate(Collection entities) throws DataAccessException {
        prepareHibernateForWrite();
        getHibernateTemplate().saveOrUpdateAll(entities);
        resetHibernateAfterWrite();
    }

    public void update(Object entity) {
        prepareHibernateForWrite();
        getHibernateTemplate().update(entity);
        resetHibernateAfterWrite();
    }

    public <T> T findDataObject(Class<T> dataObjectClass, String[] propertyNames, Object[] values) {
        StringBuilder sb = new StringBuilder();
        sb.append("from ").append(dataObjectClass.getName()).append(" where ");
        int i = 0;
        for (String propertyName : propertyNames) {
            i++;
            sb.append(propertyName).append(" = ?");
            if (i < propertyNames.length) {
                sb.append(" and ");
            }
        }

        return (T) getByQuery(sb.toString(), values);
    }

    public <T> T getByQuery(String query, Object... parameters) {
        List<T> results = findByQuery(query, parameters);
        return getFirst(results);
    }

    private <T> T getFirst(List<T> results) {
        if (results.size() != 1) {
            return null;
        }
        return results.get(0);
    }

    public <T> List<T> findDataObjects(Class<T> dataObjectClass, String[] propertyNames, Object[] values) {
        StringBuilder sb = new StringBuilder();
        sb.append("from ").append(dataObjectClass.getName()).append(" where ");
        int i = 0;
        for (String propertyName : propertyNames) {
            i++;
            sb.append(propertyName).append(" = ?");
            if (i < propertyNames.length) {
                sb.append(" and ");
            }
        }

        return (List<T>) findByQuery(sb.toString(), values);
    }

    public int executeNativeSql(final String sql, final Object... params) {
        return ((Integer) getHibernateTemplate().execute(new HibernateCallback() {

            // @Override
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                SQLQuery sqlQuery = session.createSQLQuery(sql);

                int i = 0;
                for (Object param : params) {
                    sqlQuery.setParameter(i, param);
                    i++;
                }

                return sqlQuery.executeUpdate();
            }

        })).intValue();
    }

    public List<Object[]> findByNativeSql(final String sql, final int start, final int rows, final Object... params) {
        return (List<Object[]>) getHibernateTemplate().execute(new HibernateCallback() {

            // @Override
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                SQLQuery sqlQuery = session.createSQLQuery("select * from (" + sql + ") t");

                int i = 0;
                for (Object param : params) {
                    sqlQuery.setParameter(i, param);
                    i++;
                }

                if (rows != 0) {
                    sqlQuery.setMaxResults(rows);
                }
                sqlQuery.setFirstResult(start);
                return sqlQuery.list();
            }

        });
    }

    public List findByNativeSqlNoType(final String sql, final int start, final int rows, final Object... params) {
        return findByNativeSql(sql, start, rows, params);
    }

    protected String getSortType(String sortHow) {
        if (StringUtils.isBlank(sortHow)) {
            return "ASC";
        }

        if (StringUtils.equalsIgnoreCase("ASC", sortHow)) {
            return "ASC";
        }
        return "DESC";
    }

    // @Override
    public int countByNativeSql(String sql, Object... params) {
        List result = findByNativeSql(sql, 0, 0, params);
        if (result.isEmpty() || result.get(0) == null) {
            return 0;
        }
        return ((Number) result.get(0)).intValue();
    }

    @SuppressWarnings("unchecked")
    // @Override
    public long count(String sql, Object... params) {
        List result = findByQuery(sql, params);
        return ((Number) result.get(0)).longValue();
    }

    @SuppressWarnings("unchecked")
    // @Override
    public long countByNamedParams(String queryString, String[] params, Object[] values) {
        List result = findByNamedParams(queryString, params, values);
        return ((Number) result.get(0)).longValue();
    }

    // @Override
    public void flush() {
        getHibernateTemplate().flush();
    }
    
    @Override
    public SQLQuery createSqlQuery(String queryString) {
      return getSession().createSQLQuery(queryString);
    }


    @SuppressWarnings("unchecked")
    public int count(DetachedCriteria criteria, boolean hasDistinctRootEntity) {
        int totalResults = 0;
        //DetachedCriteria countCriteria = copy(criteria);
        if (hasDistinctRootEntity) {
            criteria.setProjection(Projections.countDistinct("id"));
        } else {
            criteria.setProjection(Projections.rowCount());
        }
        List totalResultsList = getHibernateTemplate().findByCriteria(criteria);
        totalResults = (Integer) totalResultsList.get(0);

        return totalResults;
    }

    /*private DetachedCriteria copy(DetachedCriteria criteria) {
        try {
            ByteArrayOutputStream baostream = new ByteArrayOutputStream();
            ObjectOutputStream oostream = new ObjectOutputStream(baostream);
            oostream.writeObject(criteria);
            oostream.flush();
            oostream.close();
            ByteArrayInputStream baistream = new ByteArrayInputStream(baostream.toByteArray());
            ObjectInputStream oistream = new ObjectInputStream(baistream);
            DetachedCriteria copy = (DetachedCriteria) oistream.readObject();
            oistream.close();
            return copy;
        } catch (Throwable t) {
            throw new HibernateException(t);
        }
    }*/

    public Page list(DetachedCriteria criteria, int totalResults, int pageNo, int perPage) {
        int firstResult = (pageNo - 1) * perPage;
        List resultList = findByCriteria(criteria, firstResult, perPage);
        return new Page(resultList, perPage, pageNo, totalResults);
    }

    public Page list(DetachedCriteria criteria, boolean hasDistinctRootEntity, int pageNo, int perPage) {

        int totalResults = count(criteria, hasDistinctRootEntity);
        criteria.setProjection(null);
        if (hasDistinctRootEntity) {
            criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        } else {
            criteria.setResultTransformer(Criteria.ROOT_ENTITY);
        }
        int firstResult = (pageNo - 1) * perPage;
        List resultList = findByCriteria(criteria, firstResult, perPage);
        return new Page(resultList, perPage, pageNo, totalResults);
    }

    // @Override
    public Page list(DetachedCriteria criteria, int pageNo, int perPage) {
        return list(criteria, false, pageNo, perPage);
    }

    // @Override
    public void refresh(Object entity) {
        getHibernateTemplate().refresh(entity);
    }

	private Query createQuery(String queryString) {
		return getSession().createQuery(queryString);
	}

	public Page list(String hql, Map<String, Object> params, int pageNo, int perPage) {
		Query query = createQuery(hql);
		applyQueryParams(query, params);
		int totalResults = query.list().size();

		int firstResult = (pageNo - 1) * perPage;
		query.setFirstResult(firstResult);
		query.setMaxResults(perPage);
		List resultList = query.list();
		return new Page(resultList, perPage, pageNo, totalResults);
	}

	private void applyQueryParams(Query query, Map<String, Object> params) {
		Iterator<Map.Entry<String, Object>> paramIterator = params.entrySet().iterator();

		while (paramIterator.hasNext()) {
			Map.Entry<String, Object> entry = paramIterator.next();
			Object value = entry.getValue();
			if (value instanceof Collection) {
				query.setParameterList(entry.getKey(), (Collection) entry.getValue());
			} else {
				query.setParameter(entry.getKey(), entry.getValue());
			}

		}
	}



}
