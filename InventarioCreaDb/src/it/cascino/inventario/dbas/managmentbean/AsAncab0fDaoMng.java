package it.cascino.inventario.dbas.managmentbean;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import org.apache.log4j.Logger;
import it.cascino.inventario.dbas.dao.AsAncab0fDao;
import it.cascino.inventario.dbas.model.AsAncab0f;
import it.cascino.inventario.utils.Resources;

public class AsAncab0fDaoMng implements AsAncab0fDao, Serializable{
	private static final long serialVersionUID = 1L;
	private Resources res = new Resources();
	private EntityManager em = res.getEmAs();
	private EntityTransaction utx = res.getUtxAs();	
	
	Logger log = Logger.getLogger(AsAncab0fDaoMng.class);
	
	@SuppressWarnings("unchecked")
	public List<AsAncab0f> getAll(){
		List<AsAncab0f> o = null;
		try{
			try{
				utx.begin();
				Query query = em.createNamedQuery("AsAncab0f.findAll");
				o = (List<AsAncab0f>)query.getResultList();
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
	public List<AsAncab0f> getDaCcoda(String ccoda){
		List<AsAncab0f> o = null;
		try{
			try{
				utx.begin();
				Query query = em.createNamedQuery("AsAncab0f.findByCcoda");
				query.setParameter("ccoda", ccoda);
				o = (List<AsAncab0f>)query.getResultList();
			}catch(NoResultException e){
				o = null;
			}
			utx.commit();
		}catch(Exception e){
			log.fatal(e.toString());
		}
		return o;
	}
	
	public AsAncab0f getDaCcodb(String ccodb){
		AsAncab0f cod = null;
		try{
			try{
				utx.begin();
				Query query = em.createNamedQuery("AsAncab0f.findByCcodb");
				query.setParameter("ccodb", ccodb);
				cod = (AsAncab0f)query.getSingleResult();
			}catch(NoResultException e){
				cod = null;
			}
			utx.commit();
		}catch(Exception e){
			log.fatal(e.toString());
		}
		return cod;
	}
	
	public void close(){
		res.close();
		log.info("chiuso");
	}
}
