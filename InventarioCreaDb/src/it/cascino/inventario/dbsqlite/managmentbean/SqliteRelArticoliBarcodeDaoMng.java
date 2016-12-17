package it.cascino.inventario.dbsqlite.managmentbean;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import org.apache.log4j.Logger;
import it.cascino.inventario.dbsqlite.dao.SqliteRelArticoliBarcodeDao;
import it.cascino.inventario.dbsqlite.model.SqliteRelArticoliBarcode;
import it.cascino.inventario.utils.Resources;

public class SqliteRelArticoliBarcodeDaoMng implements SqliteRelArticoliBarcodeDao, Serializable{
	private static final long serialVersionUID = 1L;
	private Resources res = new Resources();
	private EntityManager em = res.getEmSqlite();
	private EntityTransaction utx = res.getUtxSqlite();	
	
	Logger log = Logger.getLogger(SqliteRelArticoliBarcodeDaoMng.class);
	
	@SuppressWarnings("unchecked")
	public List<SqliteRelArticoliBarcode> getAll(){
		List<SqliteRelArticoliBarcode> o = null;
		try{
			try{
				utx.begin();
				Query query = em.createNamedQuery("SqliteRelArticoliRelArticoliBarcode.findAll");
				o = (List<SqliteRelArticoliBarcode>)query.getResultList();
			}catch(NoResultException e){
				o = null;
			}
			utx.commit();
		}catch(Exception e){
			log.fatal(e.toString());
		}
		return o;
	}
	
	public void salva(SqliteRelArticoliBarcode a){
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
	}
	
	public void aggiorna(SqliteRelArticoliBarcode a){
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
	
	public void elimina(SqliteRelArticoliBarcode aElimina){
		// log.info("tmpDEBUGtmp: " + "> " + "elimina(" + produttoreElimina + ")");
//		try{
//			try{
//				utx.begin();
//				SqliteRelArticoliBarcode a = em.find(SqliteRelArticoliBarcode.class, aElimina.get_id());
//				log.info("elimina: " + a.toString());
//				em.remove(a);
//			}finally{
//				utx.commit();
//			}
//		}catch(Exception e){
//			log.fatal(e.toString());
//		}
	}
	
	public SqliteRelArticoliBarcode getRelArticoliBarcodeDaIdArt(Integer idArt){
		SqliteRelArticoliBarcode o = null;
		try{
			try{
				utx.begin();
				Query query = em.createNamedQuery("SqliteRelArticoliBarcode.findByIdArt");
				query.setParameter("idArt", idArt);
				o = (SqliteRelArticoliBarcode)query.getSingleResult();
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
	
	public SqliteRelArticoliBarcode getRelArticoliBarcodeDaIdBc(Integer idBc){
		SqliteRelArticoliBarcode o = null;
		try{
			try{
				utx.begin();
				Query query = em.createNamedQuery("SqliteRelArticoliBarcode.findByIdBc");
				query.setParameter("idBc", idBc);
				o = (SqliteRelArticoliBarcode)query.getSingleResult();
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
