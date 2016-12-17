package it.cascino.inventario.dbsqlite.managmentbean;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import org.apache.log4j.Logger;
import it.cascino.inventario.dbsqlite.dao.SqliteBarcodeDao;
import it.cascino.inventario.dbsqlite.model.SqliteBarcode;
import it.cascino.inventario.utils.Resources;

public class SqliteBarcodeDaoMng implements SqliteBarcodeDao, Serializable{
	private static final long serialVersionUID = 1L;
	private Resources res = new Resources();
	private EntityManager em = res.getEmSqlite();
	private EntityTransaction utx = res.getUtxSqlite();	
	
	Logger log = Logger.getLogger(SqliteBarcodeDaoMng.class);
	
	@SuppressWarnings("unchecked")
	public List<SqliteBarcode> getAll(){
		List<SqliteBarcode> o = null;
		try{
			try{
				utx.begin();
				Query query = em.createNamedQuery("SqliteBarcode.findAll");
				o = (List<SqliteBarcode>)query.getResultList();
			}catch(NoResultException e){
				o = null;
			}
			utx.commit();
		}catch(Exception e){
			log.fatal(e.toString());
		}
		return o;
	}
	
	public Integer salva(SqliteBarcode a){
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
	
	public void aggiorna(SqliteBarcode a){
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
	
	public void elimina(SqliteBarcode aElimina){
		// log.info("tmpDEBUGtmp: " + "> " + "elimina(" + produttoreElimina + ")");
		try{
			try{
				utx.begin();
				SqliteBarcode a = em.find(SqliteBarcode.class, aElimina.get_id());
				log.info("elimina: " + a.toString());
				em.remove(a);
			}finally{
				utx.commit();
			}
		}catch(Exception e){
			log.fatal(e.toString());
		}
	}
	
	public SqliteBarcode getBarcodeDaId(Integer id){
		SqliteBarcode o = null;
		try{
			try{
				utx.begin();
				Query query = em.createNamedQuery("SqliteBarcode.findById");
				query.setParameter("_id", id);
				o = (SqliteBarcode)query.getSingleResult();
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
	
	public SqliteBarcode getBarcodeDaCodice(String codice){
		SqliteBarcode o = null;
		try{
			try{
				utx.begin();
				Query query = em.createNamedQuery("SqliteBarcode.findByCodice");
				query.setParameter("codice", codice);
				o = (SqliteBarcode)query.getSingleResult();
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
