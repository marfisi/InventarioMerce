package it.cascino.inventario;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import it.cascino.inventario.dbas.dao.AsAncab0fDao;
import it.cascino.inventario.dbas.dao.AsAnmag0fDao;
import it.cascino.inventario.dbas.dao.AsArdep0fDao;
import it.cascino.inventario.dbas.dao.AsArmod0fDao;
import it.cascino.inventario.dbas.dao.AsNativeQueryDao;
import it.cascino.inventario.dbas.managmentbean.AsAncab0fDaoMng;
import it.cascino.inventario.dbas.managmentbean.AsAnmag0fDaoMng;
import it.cascino.inventario.dbas.managmentbean.AsArdep0fDaoMng;
import it.cascino.inventario.dbas.managmentbean.AsArmod0fDaoMng;
import it.cascino.inventario.dbas.managmentbean.AsNativeQueryDaoMng;
import it.cascino.inventario.dbas.model.AsAncab0f;
import it.cascino.inventario.dbas.model.AsAnmag0f;
import it.cascino.inventario.dbas.model.AsArdep0f;
import it.cascino.inventario.dbas.model.AsArmod0f;
import it.cascino.inventario.dbas.model.pkey.AsArmod0fPKey;
import it.cascino.inventario.dbsqlite.dao.SqliteArticoliDao;
import it.cascino.inventario.dbsqlite.dao.SqliteBarcodeDao;
import it.cascino.inventario.dbsqlite.dao.SqliteInfogenericheDao;
import it.cascino.inventario.dbsqlite.dao.SqliteQtyOriginaliDao;
import it.cascino.inventario.dbsqlite.dao.SqliteRelArticoliBarcodeDao;
import it.cascino.inventario.dbsqlite.managmentbean.SqliteArticoliDaoMng;
import it.cascino.inventario.dbsqlite.managmentbean.SqliteBarcodeDaoMng;
import it.cascino.inventario.dbsqlite.managmentbean.SqliteInfogenericheDaoMng;
import it.cascino.inventario.dbsqlite.managmentbean.SqliteQtyOriginaliDaoMng;
import it.cascino.inventario.dbsqlite.managmentbean.SqliteRelArticoliBarcodeDaoMng;
import it.cascino.inventario.dbsqlite.model.SqliteArticoli;
import it.cascino.inventario.dbsqlite.model.SqliteBarcode;
import it.cascino.inventario.dbsqlite.model.SqliteInfogeneriche;
import it.cascino.inventario.dbsqlite.model.SqliteQtyOriginali;
import it.cascino.inventario.dbsqlite.model.SqliteRelArticoliBarcode;
import it.cascino.inventario.dbsqlite.model.pkey.SqliteRelArticoliBarcodePKey;

public class InventarioCreaDb{
	
	private Logger log = Logger.getLogger(InventarioCreaDb.class);
	
	StringBuilder stringBuilder = new StringBuilder();
	
	private AsAnmag0fDao asAnmag0fDao = new AsAnmag0fDaoMng();
	private List<AsAnmag0f> asAnmag0fLs;
	
	private AsAncab0fDao asAncab0fDao = new AsAncab0fDaoMng();
	private List<AsAncab0f> asAncab0fLs;
	
	private AsArdep0fDao asArdep0fDao = new AsArdep0fDaoMng();
	private List<AsArdep0f> asArdep0fLs;
	
	private AsArmod0fDao asArmod0fDao = new AsArmod0fDaoMng();
	private List<AsArmod0f> asArmod0fLs;
	private List<String> asArmod0fDcodaLs;

	private AsNativeQueryDao asNativeQueryDao = new AsNativeQueryDaoMng();
	
	private SqliteArticoliDao sqliteArticoliDao = new SqliteArticoliDaoMng();
	private SqliteBarcodeDao sqliteBarcodeDao = new SqliteBarcodeDaoMng();
	private SqliteRelArticoliBarcodeDao sqliteRelArticoliBarcodeDao = new SqliteRelArticoliBarcodeDaoMng();
	private SqliteQtyOriginaliDao sqliteQtyOriginaliDao = new SqliteQtyOriginaliDaoMng();
	private SqliteInfogenericheDao sqliteInfogenericheDao = new SqliteInfogenericheDaoMng();
	
	private boolean elaboraSoloModificati;
	
	public InventarioCreaDb(String args[]){
		log.info("[" + "InventarioCreaDb");
		
		BigDecimal bdModelVal = null;
		BigDecimal bdDisplayVal = null;
		
		elaboraSoloModificati = false;
		
		for(int numArg = 0; numArg < args.length; numArg++){
			if(args[numArg].compareTo("-soloModificati") == 0){
//				numArg++;
				elaboraSoloModificati = true;
//			}else if(args[numArg].compareTo("-fI") == 0){
//				numArg++;
//				basePathInput = args[numArg];
			}else{ // se c'e' almeno un parametro e non e' tra quelli previsti stampo il messaggio d'aiuto
			}
		}
		
		AsAnmag0f asAnmag0f = new AsAnmag0f();
		//asAnmag0f = asAnmag0fDao.getArticoloDaMcoda("movma.getVcoda()");
		
		asAnmag0fLs = new ArrayList<AsAnmag0f>();
		if(!(elaboraSoloModificati)){
			asAnmag0fLs = asAnmag0fDao.getAll();
//			asAnmag0fLs = asAnmag0fDao.getAll().subList(100, 200);
		}else{
			asArmod0fDcodaLs =  asArmod0fDao.getDaElaborare();
			Iterator<String> iter_asArmod0fDcodaLs = asArmod0fDcodaLs.iterator();
			iter_asArmod0fDcodaLs = asArmod0fDcodaLs.iterator();
			while(iter_asArmod0fDcodaLs.hasNext()){
				String asArmod0fDcoda = iter_asArmod0fDcodaLs.next();
				
				asAnmag0f = asAnmag0fDao.getArticoloDaMcoda(StringUtils.trim(asArmod0fDcoda));
				
				if(asAnmag0f != null){
					asAnmag0fLs.add(asAnmag0f);
				}
			}
		}
		
		Iterator<AsAnmag0f> iter_asAnmag0fLs = asAnmag0fLs.iterator();
		iter_asAnmag0fLs = asAnmag0fLs.iterator();
		while(iter_asAnmag0fLs.hasNext()){
			asAnmag0f = iter_asAnmag0fLs.next();
			// log.info(asAnmag0f.toString());
			
			if(StringUtils.startsWith(asAnmag0f.getMcoda(), "/")){
				continue;
			}
			
			SqliteArticoli sqliteArticoli = new SqliteArticoli();
			Integer idArticoloElaborato = -1;
			
			sqliteArticoli.setCodart(StringUtils.trim(asAnmag0f.getMcoda()));
			sqliteArticoli.setDesc(StringUtils.trim(asAnmag0f.getMdesc()));
			sqliteArticoli.setUm(StringUtils.trim(asAnmag0f.getMumis()));
			bdModelVal = new BigDecimal(asAnmag0f.getMpeu2());
			bdDisplayVal = bdModelVal.setScale(2,  BigDecimal.ROUND_HALF_UP);
			sqliteArticoli.setPrezzo(bdDisplayVal);
			bdModelVal = new BigDecimal(asAnmag0f.getMconf());
			bdDisplayVal = bdModelVal.setScale(4,  BigDecimal.ROUND_HALF_UP);
			sqliteArticoli.setQty_per_confez(bdDisplayVal);
				
			if(!(elaboraSoloModificati)){
				idArticoloElaborato = sqliteArticoliDao.salva(sqliteArticoli);
				// log.info("Articolo inserito " + idArticoloInserito);
			}else{
				SqliteArticoli sqliteArticoliTmp = sqliteArticoliDao.getArticoloDaCodice(StringUtils.trim(asAnmag0f.getMcoda()));
				if(sqliteArticoliTmp == null){	// nuovo codice, quindi insert
					idArticoloElaborato = sqliteArticoliDao.salva(sqliteArticoli);
				}else{	// gia' presente, quindi update
					sqliteArticoli.set_id(sqliteArticoliTmp.get_id());
					idArticoloElaborato = sqliteArticoli.get_id();
					sqliteArticoliDao.aggiorna(sqliteArticoli);
				}
			}
			
			if(!(elaboraSoloModificati)){
				AsAncab0f asAncab0f = new AsAncab0f();
				asAncab0fLs = asAncab0fDao.getDaCcoda(sqliteArticoli.getCodart());
				Iterator<AsAncab0f> iter_asAncab0fLs = asAncab0fLs.iterator();
				iter_asAncab0fLs = asAncab0fLs.iterator();
				while(iter_asAncab0fLs.hasNext()){
					asAncab0f = iter_asAncab0fLs.next();
					
					SqliteBarcode sqliteBarcode = new SqliteBarcode();
					sqliteBarcode.setCodice(StringUtils.trim(asAncab0f.getCcodb()));
					sqliteBarcode.setTipo("EAN");
					
					Integer idBardoceElaborato = sqliteBarcodeDao.salva(sqliteBarcode);
					
					SqliteRelArticoliBarcode sqliteRelArticoliBarcode = new SqliteRelArticoliBarcode();
					SqliteRelArticoliBarcodePKey sqliteRelArticoliBarcodePKey = new SqliteRelArticoliBarcodePKey();
					sqliteRelArticoliBarcodePKey.setIdArt(idArticoloElaborato);
					sqliteRelArticoliBarcodePKey.setIdBc(idBardoceElaborato);
					sqliteRelArticoliBarcode.setId(sqliteRelArticoliBarcodePKey);
					sqliteRelArticoliBarcodeDao.salva(sqliteRelArticoliBarcode);
				}
			}else{
				// non gestisco la variazione dei barcode, rinvio all'allineamento completo
			}
				
			AsArdep0f asArdep0f = new AsArdep0f();
			asArdep0fLs = asArdep0fDao.getDaDcoda(asAnmag0f.getMcoda());
			Iterator<AsArdep0f> iter_asArdep0fLs = asArdep0fLs.iterator();
			iter_asArdep0fLs = asArdep0fLs.iterator();
			AsArmod0f asArmod0f = new AsArmod0f();
			while(iter_asArdep0fLs.hasNext()){
				asArdep0f = iter_asArdep0fLs.next();
				
				if(!(elaboraSoloModificati)){
				}else{
					asArmod0f =  asArmod0fDao.getDaDcodaAndDcode(sqliteArticoli.getCodart(), asArdep0f.getId().getDcode());
					if(asArmod0f == null){
						continue;
					}
				}
				
				SqliteQtyOriginali sqliteQtyOriginali = new SqliteQtyOriginali();
				Integer idQtyOriginaliElaborato = -1;
				
				sqliteQtyOriginali.setIdart(idArticoloElaborato);	// asArdep0f.getId().getDcoda()
				sqliteQtyOriginali.setIddep(asArdep0f.getId().getDcode());
				bdModelVal = new BigDecimal(asArdep0f.getDgiac());
				bdDisplayVal = bdModelVal.setScale(4,  BigDecimal.ROUND_HALF_UP);
				sqliteQtyOriginali.setQty(bdDisplayVal);
				bdModelVal = new BigDecimal(asArdep0f.getDgdif());
				bdDisplayVal = bdModelVal.setScale(4,  BigDecimal.ROUND_HALF_UP);
				sqliteQtyOriginali.setQty_difettosi(bdDisplayVal);
				sqliteQtyOriginali.setData_carico(StringUtils.trim(asArdep0f.getDatuc()));
				sqliteQtyOriginali.setData_scarico(StringUtils.trim(asArdep0f.getDatus()));
				sqliteQtyOriginali.setData_inventario(StringUtils.trim(asArdep0f.getDatin()));
				bdModelVal = new BigDecimal(asArdep0f.getDscmi());
				bdDisplayVal = bdModelVal.setScale(4,  BigDecimal.ROUND_HALF_UP);
				sqliteQtyOriginali.setQty_scorta_min(bdDisplayVal);
				bdModelVal = new BigDecimal(asArdep0f.getDscma());
				bdDisplayVal = bdModelVal.setScale(4,  BigDecimal.ROUND_HALF_UP);
				sqliteQtyOriginali.setQty_scorta_max(bdDisplayVal);
				BigDecimal mtqua =  asNativeQueryDao.getDaMovtr0f_MtquaDaMtcodAndMtdpa(sqliteArticoli.getCodart(), asArdep0f.getId().getDcode());
				if(mtqua != null){
					bdModelVal = mtqua; //new BigDecimal(mtqua);
				}else{
					bdModelVal = new BigDecimal(0);
				}
				bdDisplayVal = bdModelVal.setScale(2,  BigDecimal.ROUND_HALF_UP);
				sqliteQtyOriginali.setQty_trasf_arrivo(bdDisplayVal);
				
				mtqua =  asNativeQueryDao.getDaMovtr0f_MtquaDaMtcodAndMtdpp(sqliteArticoli.getCodart(), asArdep0f.getId().getDcode());
				if(mtqua != null){
					bdModelVal = mtqua;
				}else{
					bdModelVal = new BigDecimal(0);
				}
				bdDisplayVal = bdModelVal.setScale(2,  BigDecimal.ROUND_HALF_UP);
				sqliteQtyOriginali.setQty_trasf_partenza(bdDisplayVal);
				
				if(!(elaboraSoloModificati)){
					idQtyOriginaliElaborato = sqliteQtyOriginaliDao.salva(sqliteQtyOriginali);
					// log.info("QtyOriginali inserito " + idQtyOriginaliElaborato);
				}else{
					SqliteQtyOriginali sqliteQtyOriginaliTmp = sqliteQtyOriginaliDao.getQtyOriginaliDaIdArtAndIdDep(idArticoloElaborato, asArdep0f.getId().getDcode());
					if(sqliteQtyOriginaliTmp == null){	// nuova giacenza, quindi insert
						idQtyOriginaliElaborato = sqliteQtyOriginaliDao.salva(sqliteQtyOriginali);
					}else{	// gia' presente, quindi update
						sqliteQtyOriginali.set_id(sqliteQtyOriginaliTmp.get_id());
						idQtyOriginaliElaborato = sqliteQtyOriginali.get_id();
						sqliteQtyOriginaliDao.aggiorna(sqliteQtyOriginali);
					}
					asArmod0f.setDflal("L");
					asArmod0fDao.aggiorna(asArmod0f);
				}
			}
		}
		
		SqliteInfogeneriche sqliteInfogeneriche = new SqliteInfogeneriche();
		sqliteInfogeneriche = sqliteInfogenericheDao.getInfoDaNomeInfo("data_creazione");
		DateTimeFormatter formatter = null;
		String strTtimestampAs400 =  asNativeQueryDao.getDaSysdummy1_TimestampAs400().toString();
		// e' in formato "yyyy-MM-dd HH:mm:ss.SSSSSS"
		strTtimestampAs400 = StringUtils.substringBefore(strTtimestampAs400, ".");
		formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");	
		LocalDateTime timestampAs400 = LocalDateTime.parse(strTtimestampAs400, formatter);
		formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss");	
		sqliteInfogeneriche.setValore(timestampAs400.format(formatter));
		sqliteInfogenericheDao.aggiorna(sqliteInfogeneriche);
		
		// se e' creazione totale svuoto tutta armod0f
		if(!(elaboraSoloModificati)){
			asArmod0fDao.svuotaTabella();
		}
		
		asAnmag0fDao.close();
		asAncab0fDao.close();
		asArdep0fDao.close();
		asArmod0fDao.close();
		asNativeQueryDao.close();
		
		sqliteArticoliDao.close();
		sqliteBarcodeDao.close();
		sqliteRelArticoliBarcodeDao.close();
		sqliteQtyOriginaliDao.close();
		sqliteInfogenericheDao.close();	// in questo close c'è "pragma wal_checkpoint" per svuotare il wal file
		
		log.info("]" + "InventarioCreaDb");
	}
}