package it.cascino.inventario.dbas.managmentbean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import org.apache.log4j.Logger;
import it.cascino.inventario.dbas.dao.AsNativeQueryDao;
import it.cascino.inventario.utils.Resources;

public class AsNativeQueryDaoMng implements AsNativeQueryDao, Serializable{
	private static final long serialVersionUID = 1L;
	private Resources res = new Resources();
	private EntityManager em = res.getEmAs();
	private EntityTransaction utx = res.getUtxAs();	
	
	Logger log = Logger.getLogger(AsNativeQueryDaoMng.class);
		
	public BigDecimal getDaMovtr0f_MtquaDaMtcodAndMtdpa(String mtcod, Integer mtdpa){
		BigDecimal o = null;
		try{
			try{
				utx.begin();
				String sql = "SELECT sum(a.mtqua) FROM Movtr0f a WHERE a.mtcod = :mtcod and a.mtsta = ' ' and a.mtdpa = :mtdpa and a.mtdat > :mtdat";
				Query query = em.createNativeQuery(sql);
				query.setParameter("mtcod", mtcod);
				query.setParameter("mtdpa", mtdpa);
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
				LocalDateTime dateTime = LocalDateTime.now();
				dateTime = dateTime.minusMonths(6);
				query.setParameter("mtdat", dateTime.format(formatter));
				o = (BigDecimal)query.getSingleResult();
			}catch(NoResultException e){
				o = null;
			}
			utx.commit();
		}catch(Exception e){
			log.fatal(e.toString());
		}
		return o;
	}
	
	public Timestamp getDaSysdummy1_TimestampAs400(){
		Timestamp o = null;
		try{
			try{
				utx.begin();
				String sql = "select a.ts from (select current timestamp ts from sysibm.sysdummy1) a";
				Query query = em.createNativeQuery(sql);
				o = (Timestamp)query.getSingleResult();
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
