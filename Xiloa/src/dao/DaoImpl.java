package dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository("daoImpl")	
public class DaoImpl<T extends Object> implements IDao<T>{

	private static final Log log = LogFactory.getFactory().getInstance(DaoImpl.class);
		
	@PersistenceContext
	protected EntityManager em;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
		
	@Override
	public T save(T pojo) {
		log.debug("merge instance " + pojo.getClass().getName());
		return (T)em.merge(pojo);
	}

	public void remove(Class<T> classe, int id) {
		log.debug("removing instance " + classe.getName() + " with id: " + id);
		T pojo = findById(classe, id);
		if(pojo != null) {
			em.remove(pojo);
		}		
	}

	public void remove(T pojo) {
		log.debug("removing instance " + pojo.getClass().getName());
		if(pojo != null)
			em.remove(pojo);
	}

	@Override
	public T findById(Class<T> classe, int id) {
		log.debug("findById instance " + classe.getName() + " with id: " + id);
		return (T) em.find(classe, id);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<T> findAll(Class<T> classe) {
		StringBuffer sb = new StringBuffer(20);
		sb.append("from ").append(classe.getName());
		List<T> result = em.createQuery(sb.toString()).getResultList();
		log.debug("finding all successful, result size : " + result.size());
		return result;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<T> findAllByQuery(String jpql) {
		List<T> result = em.createQuery(jpql).getResultList();
		log.debug("finding by query successful, result size : " + result.size());
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T findOneByQuery(String jpql) {
		
		if(em.createQuery(jpql).getResultList().isEmpty()){
			return null;
		}
		T result = (T) em.createQuery(jpql).getResultList().get(0);
		log.debug("finding by query successful, result size : " + 1);
		return result;
	}	

	@Override
	public T findByUsername(Class<T> classe, String username) {
		return em.find(classe, username);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public T findOneByNamedQueryParam(String namedQuery, Object [] params){			
		Query q;
		int i;
		q = em.createNamedQuery(namedQuery);
		i = 1;
		for (Object obj :  params) {
			q.setParameter(i++, obj);
		}
		
		try{			
			return (T) q.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
			
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<T> findAllByNamedQueryParam(String named, Object [] params){
		List<T> result;
		Query q = em.createNamedQuery(named);
		int i = 1;
		for (Object obj :  params) {
			q.setParameter(i++, obj);
		}
		
		try {
			result = q.getResultList();			
		} catch (NoResultException e) {
			result = null;
		}		
		return result;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<T> findAllByNamedQuery(String named){
		List<T> result;
		Query q = em.createNamedQuery(named);
		
		try {
			result = q.getResultList();			
		} catch (NoResultException e) {
			result = null;
		}		
		return result;
	}
	
	@Override
	public Connection getSqlConexion() throws SQLException {			
		DataSource ds = null;
		Connection c = null;
		System.out.println("Entra a DaoImp getSqlConexion");
		try{			
			ds = jdbcTemplate.getDataSource(); //(DataSource)em.getProperties().get("dataSource");			
			System.out.println("DataSource " + ds);
			c = ds.getConnection();
			System.out.println("Conexion " + c);
		}catch (Exception e){
			e.printStackTrace();
			c = null;
		}
	
		return c;
	}
}