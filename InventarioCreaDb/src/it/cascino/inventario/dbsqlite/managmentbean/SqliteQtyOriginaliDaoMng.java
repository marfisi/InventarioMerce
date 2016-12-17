package it.cascino.inventario.dbsqlite.managmentbean;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import org.apache.log4j.Logger;
import it.cascino.inventario.dbsqlite.dao.SqliteQtyOriginaliDao;
import it.cascino.inventario.dbsqlite.model.SqliteQtyOriginali;
import it.cascino.inventario.utils.Resources;

public class SqliteQtyOriginaliDaoMng implements SqliteQtyOriginaliDao, Serializable{
	private static final long serialVersionUID = 1L;
	private Resources res = new Resources();
	private EntityManager em = res.getEmSqlite();
	private EntityTransaction utx = res.getUtxSqlite();	
	
	Logger log = Logger.getLogger(SqliteQtyOriginaliDaoMng.class);
	
	@SuppressWarnings("unchecked")
	public List<SqliteQtyOriginali> getAll(){
		List<SqliteQtyOriginali> o = null;
		try{
			try{
				utx.begin();
				Query query = em.createNamedQuery("SqliteQtyOriginali.findAll");
				o = (List<SqliteQtyOriginali>)query.getResultList();
			}catch(NoResultException e){
				o = null;
			}
			utx.commit();
		}catch(Exception e){
			log.fatal(e.toString());
		}
		return o;
	}
	
	public Integer salva(SqliteQtyOriginali a){
		try{
			try{
				utx.begin();
//				a.setId(null);
//				log.info("salva: " + a.toString());
				em.persist(a);
			}finally{
				utx.commit();
			}
		}catch(Exception e){
			log.fatal(e.toString());
		}
		return a.get_id();
	}
	
	public void aggiorna(SqliteQtyOriginali a){
		try{
			try{
				utx.begin();
				log.info("aggiorna: " + a.toString());
				em.merge(a);
			}finally{
				utx.commit();
			}
		}catch(Exception e){
			log.fatal(e.toString());
		}
	}
	
	public void elimina(SqliteQtyOriginali aElimina){
		// log.info("tmpDEBUGtmp: " + "> " + "elimina(" + produttoreElimina + ")");
		try{
			try{
				utx.begin();
				SqliteQtyOriginali a = em.find(SqliteQtyOriginali.class, aElimina.get_id());
				log.info("elimina: " + a.toString());
				em.remove(a);
			}finally{
				utx.commit();
			}
		}catch(Exception e){
			log.fatal(e.toString());
		}
	}
	
	public SqliteQtyOriginali getQtyOriginaliDaId(Integer id){
		SqliteQtyOriginali o = null;
		try{
			try{
				utx.begin();
				Query query = em.createNamedQuery("SqliteQtyOriginali.findById");
				query.setParameter("_id", id);
				o = (SqliteQtyOriginali)query.getSingleResult();
			}catch(NoResultException e){
				o = null;
			}
			utx.commit();
		}catch(Exception e){
			log.fatal(e.toString());
			utx.commit();
		}
		return o;
	}
	
	@SuppressWarnings("unchecked")
	public List<SqliteQtyOriginali> getQtyOriginaliDaIdArt(Integer idArt){
		List<SqliteQtyOriginali> o = null;
		try{
			try{
				utx.begin();
				Query query = em.createNamedQuery("SqliteQtyOriginali.findAll");
				query.setParameter("idArt", idArt);
				o = (List<SqliteQtyOriginali>)query.getResultList();
			}catch(NoResultException e){
				o = null;
			}
			utx.commit();
		}catch(Exception e){
			log.fatal(e.toString());
		}
		return o;
	}

	@SuppressWarnings("unchecked")
	public List<SqliteQtyOriginali> getQtyOriginaliDaIdDep(Integer idDep){
		List<SqliteQtyOriginali> o = null;
		try{
			try{
				utx.begin();
				Query query = em.createNamedQuery("SqliteQtyOriginali.findAll");
				query.setParameter("idDep", idDep);
				o = (List<SqliteQtyOriginali>)query.getResultList();
			}catch(NoResultException e){
				o = null;
			}
			utx.commit();
		}catch(Exception e){
			log.fatal(e.toString());
		}
		return o;
	}

	public SqliteQtyOriginali getQtyOriginaliDaIdArtAndIdDep(Integer idArt, Integer idDep){
		SqliteQtyOriginali o = null;
		try{
			try{
				utx.begin();
				Query query = em.createNamedQuery("SqliteQtyOriginali.findByIdArtAndIdDep");
				query.setParameter("idArt", idArt);
				query.setParameter("idDep", idDep);
				o = (SqliteQtyOriginali)query.getSingleResult();
			}catch(NoResultException e){
				o = null;
			}
			utx.commit();
		}catch(Exception e){
			log.fatal(e.toString());
			utx.commit();
		}
		return o;
	}

	public void close(){
		res.close();
		log.info("chiuso");
	}
}
