package it.cascino.inventario.dbsqlite.managmentbean;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import org.apache.log4j.Logger;
import it.cascino.inventario.dbsqlite.dao.SqliteDepositiDao;
import it.cascino.inventario.dbsqlite.model.SqliteDepositi;
import it.cascino.inventario.utils.Resources;

public class SqliteDepositiDaoMng implements SqliteDepositiDao, Serializable{
	private static final long serialVersionUID = 1L;
	private Resources res = new Resources();
	private EntityManager em = res.getEmSqlite();
	private EntityTransaction utx = res.getUtxSqlite();	
	
	Logger log = Logger.getLogger(SqliteDepositiDaoMng.class);
	
	@SuppressWarnings("unchecked")
	public List<SqliteDepositi> getAll(){
		List<SqliteDepositi> o = null;
		try{
			try{
				utx.begin();
				Query query = em.createNamedQuery("SqliteDepositi.findAll");
				o = (List<SqliteDepositi>)query.getResultList();
			}catch(NoResultException e){
				o = null;
			}
			utx.commit();
		}catch(Exception e){
			log.fatal(e.toString());
		}
		return o;
	}
	
	public Integer salva(SqliteDepositi a){
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
	
	public void aggiorna(SqliteDepositi a){
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
	
	public void elimina(SqliteDepositi aElimina){
		// log.info("tmpDEBUGtmp: " + "> " + "elimina(" + produttoreElimina + ")");
		try{
			try{
				utx.begin();
				SqliteDepositi a = em.find(SqliteDepositi.class, aElimina.get_id());
				log.info("elimina: " + a.toString());
				em.remove(a);
			}finally{
				utx.commit();
			}
		}catch(Exception e){
			log.fatal(e.toString());
		}
	}
	
	public SqliteDepositi getDepositoDaId(Integer id){
		SqliteDepositi o = null;
		try{
			try{
				utx.begin();
				Query query = em.createNamedQuery("SqliteDepositi.findById");
				query.setParameter("id", id);
				o = (SqliteDepositi)query.getSingleResult();
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
	
	public SqliteDepositi getDepositoDaIdDep(String idDep){
		SqliteDepositi o = null;
		try{
			try{
				utx.begin();
				Query query = em.createNamedQuery("SqliteDepositi.findByIdDep");
				query.setParameter("idDep", idDep);
				o = (SqliteDepositi)query.getSingleResult();
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
