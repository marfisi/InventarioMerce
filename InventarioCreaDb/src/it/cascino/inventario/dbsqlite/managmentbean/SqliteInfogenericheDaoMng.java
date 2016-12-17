package it.cascino.inventario.dbsqlite.managmentbean;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import org.apache.log4j.Logger;
import it.cascino.inventario.dbsqlite.dao.SqliteInfogenericheDao;
import it.cascino.inventario.dbsqlite.model.SqliteInfogeneriche;
import it.cascino.inventario.utils.Resources;

public class SqliteInfogenericheDaoMng implements SqliteInfogenericheDao, Serializable{
	private static final long serialVersionUID = 1L;
	private Resources res = new Resources();
	private EntityManager em = res.getEmSqlite();
	private EntityTransaction utx = res.getUtxSqlite();	
	
	Logger log = Logger.getLogger(SqliteInfogenericheDaoMng.class);
	
	@SuppressWarnings("unchecked")
	public List<SqliteInfogeneriche> getAll(){
		List<SqliteInfogeneriche> o = null;
		try{
			try{
				utx.begin();
				Query query = em.createNamedQuery("SqliteInfogeneriche.findAll");
				o = (List<SqliteInfogeneriche>)query.getResultList();
			}catch(NoResultException e){
				o = null;
			}
			utx.commit();
		}catch(Exception e){
			log.fatal(e.toString());
		}
		return o;
	}
	
	public Integer salva(SqliteInfogeneriche a){
		try{
			try{
				utx.begin();
//				a.setId(null);
				log.info("salva: " + a.toString());
				em.persist(a);
			}finally{
				utx.commit();
			}
		}catch(Exception e){
			log.fatal(e.toString());
		}
		return a.get_id();
	}
	
	public void aggiorna(SqliteInfogeneriche a){
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
	
	public void elimina(SqliteInfogeneriche aElimina){
		// log.info("tmpDEBUGtmp: " + "> " + "elimina(" + produttoreElimina + ")");
		try{
			try{
				utx.begin();
				SqliteInfogeneriche a = em.find(SqliteInfogeneriche.class, aElimina.get_id());
				log.info("elimina: " + a.toString());
				em.remove(a);
			}finally{
				utx.commit();
			}
		}catch(Exception e){
			log.fatal(e.toString());
		}
	}
	
	public SqliteInfogeneriche getInfoDaId(Integer id){
		SqliteInfogeneriche o = null;
		try{
			try{
				utx.begin();
				Query query = em.createNamedQuery("SqliteInfogeneriche.findById");
				query.setParameter("id", id);
				o = (SqliteInfogeneriche)query.getSingleResult();
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
	
	public SqliteInfogeneriche getInfoDaNomeInfo(String nomeInfo){
		SqliteInfogeneriche o = null;
		try{
			try{
				utx.begin();
				Query query = em.createNamedQuery("SqliteInfogeneriche.findByNome");
				query.setParameter("info", nomeInfo);
				o = (SqliteInfogeneriche)query.getSingleResult();
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
		try{
			utx.begin();
			Query query = em.createNativeQuery("pragma wal_checkpoint");
			//query.executeUpdate();
			query.getResultList();
			utx.commit();
		}catch(Exception e){
			log.fatal(e.toString());
			utx.commit();
		}
		res.close();
		log.info("chiuso");
	}
}
