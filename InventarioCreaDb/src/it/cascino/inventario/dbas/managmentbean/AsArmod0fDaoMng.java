package it.cascino.inventario.dbas.managmentbean;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import org.apache.log4j.Logger;
import it.cascino.inventario.dbas.dao.AsArmod0fDao;
import it.cascino.inventario.dbas.model.AsArmod0f;
import it.cascino.inventario.utils.Resources;

public class AsArmod0fDaoMng implements AsArmod0fDao, Serializable{
	private static final long serialVersionUID = 1L;
	private Resources res = new Resources();
	private EntityManager em = res.getEmAs();
	private EntityTransaction utx = res.getUtxAs();	
	
	Logger log = Logger.getLogger(AsArmod0fDaoMng.class);
	
	@SuppressWarnings("unchecked")
	public List<AsArmod0f> getAll(){
		List<AsArmod0f> o = null;
		try{
			try{
				utx.begin();
				Query query = em.createNamedQuery("AsArmod0f.findAll");
				o = (List<AsArmod0f>)query.getResultList();
			}catch(NoResultException e){
				o = null;
			}
			utx.commit();
		}catch(Exception e){
			log.fatal(e.toString());
		}
		return o;
	}
	
	public void aggiorna(AsArmod0f a){
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
	
	@SuppressWarnings("unchecked")
	public List<AsArmod0f> getDaDcoda(String dcoda){
		List<AsArmod0f> o = null;
		try{
			try{
				utx.begin();
				Query query = em.createNamedQuery("AsArmod0f.findByDcoda");
				query.setParameter("dcoda", dcoda);
				o = (List<AsArmod0f>)query.getResultList();
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
	public List<AsArmod0f> getDaDcode(Integer dcode){
		List<AsArmod0f> o = null;
		try{
			try{
				utx.begin();
				Query query = em.createNamedQuery("AsArmod0f.findByDcode");
				query.setParameter("dcode", dcode);
				o = (List<AsArmod0f>)query.getResultList();
			}catch(NoResultException e){
				o = null;
			}
			utx.commit();
		}catch(Exception e){
			log.fatal(e.toString());
		}
		return o;
	}
	
	public AsArmod0f getDaDcodaAndDcode(String dcoda, Integer dcode){
		AsArmod0f o = null;
		try{
			try{
				utx.begin();
				Query query = em.createNamedQuery("AsArmod0f.findByDcodaAndDcode");
				query.setParameter("dcoda", dcoda);
				query.setParameter("dcode", dcode);
				o = (AsArmod0f)query.getSingleResult();
			}catch(NoResultException e){
				o = null;
			}
			utx.commit();
		}catch(Exception e){
			log.fatal(e.toString());
		}
		return o;
	}
	
//	@SuppressWarnings("unchecked")
//	public List<AsArmod0f> getDaElaborare(){
//		List<AsArmod0f> o = null;
//		try{
//			try{
//				utx.begin();
//				Query query = em.createNamedQuery("AsArmod0f.findDaElab");
//				o = (List<AsArmod0f>)query.getResultList();
//			}catch(NoResultException e){
//				o = null;
//			}
//			utx.commit();
//		}catch(Exception e){
//			log.fatal(e.toString());
//		}
//		return o;
//	}
	
	@SuppressWarnings("unchecked")
	public List<String> getDaElaborare(){
		List<String> o = null;
		try{
			try{
				utx.begin();
				String sql = "SELECT distinct(a.dcoda) FROM Armod0f a WHERE a.dflal = ' ' order by a.dcoda";
				Query query = em.createNativeQuery(sql);
				o = (List<String>)query.getResultList();
			}catch(NoResultException e){
				o = null;
			}
			utx.commit();
		}catch(Exception e){
			log.fatal(e.toString());
		}
		return o;
	}
	
	public void close(){
		res.close();
		log.info("chiuso");
	}
}
