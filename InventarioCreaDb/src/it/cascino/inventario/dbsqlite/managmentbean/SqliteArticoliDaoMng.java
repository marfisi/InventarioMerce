package it.cascino.inventario.dbsqlite.managmentbean;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import org.apache.log4j.Logger;
import it.cascino.inventario.dbsqlite.dao.SqliteArticoliDao;
import it.cascino.inventario.dbsqlite.model.SqliteArticoli;
import it.cascino.inventario.utils.Resources;

public class SqliteArticoliDaoMng implements SqliteArticoliDao, Serializable{
	private static final long serialVersionUID = 1L;
	private Resources res = new Resources();
	private EntityManager em = res.getEmSqlite();
	private EntityTransaction utx = res.getUtxSqlite();	
	
	Logger log = Logger.getLogger(SqliteArticoliDaoMng.class);
	
	@SuppressWarnings("unchecked")
	public List<SqliteArticoli> getAll(){
		List<SqliteArticoli> o = null;
		try{
			try{
				utx.begin();
				Query query = em.createNamedQuery("SqliteArticoli.findAll");
				o = (List<SqliteArticoli>)query.getResultList();
			}catch(NoResultException e){
				o = null;
			}
			utx.commit();
		}catch(Exception e){
			log.fatal(e.toString());
		}
		return o;
	}
	
	public Integer salva(SqliteArticoli a){
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
	
	public void aggiorna(SqliteArticoli a){
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
	
	public void elimina(SqliteArticoli aElimina){
		// log.info("tmpDEBUGtmp: " + "> " + "elimina(" + produttoreElimina + ")");
		try{
			try{
				utx.begin();
				SqliteArticoli a = em.find(SqliteArticoli.class, aElimina.get_id());
				log.info("elimina: " + a.toString());
				em.remove(a);
			}finally{
				utx.commit();
			}
		}catch(Exception e){
			log.fatal(e.toString());
		}
	}
	
	public SqliteArticoli getArticoloDaId(Integer id){
		SqliteArticoli o = null;
		try{
			try{
				utx.begin();
				Query query = em.createNamedQuery("SqliteArticoli.findById");
				query.setParameter("_id", id);
				o = (SqliteArticoli)query.getSingleResult();
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
	
	public SqliteArticoli getArticoloDaCodice(String codiceArticolo){
		SqliteArticoli o = null;
		try{
			try{
				utx.begin();
				Query query = em.createNamedQuery("SqliteArticoli.findByCodart");
				query.setParameter("codart", codiceArticolo);
				o = (SqliteArticoli)query.getSingleResult();
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
