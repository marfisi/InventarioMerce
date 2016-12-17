package it.cascino.inventario.utils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class Resources{
	private EntityManagerFactory emfAs = null;
	private EntityManager emAs = null;
	private EntityTransaction utxAs = null;
	
	private EntityManagerFactory emfSqlite = null;
	private EntityManager emSqlite = null;
	private EntityTransaction utxSqlite = null;
	
	public Resources(){
		super();
		if(emfAs == null) {
			initAs();
		}
		if(emfSqlite == null) {
			initSqlite();
		}
	}
	
	private void initAs(){
		emfAs = Persistence.createEntityManagerFactory("DB2AS400");
		emAs = emfAs.createEntityManager();
		utxAs = emAs.getTransaction();
	}
	
	private void initSqlite(){
		emfSqlite = Persistence.createEntityManagerFactory("Sqlite");
		emSqlite = emfSqlite.createEntityManager();
		utxSqlite = emSqlite.getTransaction();
	}
	
	public void close(){
		if(emfAs != null) {
			closeAs();
		}
		if(emfSqlite != null) {
			closeSqlite();
		}
	}

	private void closeAs(){
		emAs.close();
		emfAs.close();
	}
	
	private void closeSqlite(){
		emSqlite.close();
		emfSqlite.close();
	}
	
	public EntityManagerFactory getEmfAs(){
		return emfAs;
	}
	
	public void setEmfAs(EntityManagerFactory emfAs){
		this.emfAs = emfAs;
	}
	
	public EntityManager getEmAs(){
		return emAs;
	}
	
	public void setEmAs(EntityManager emAs){
		this.emAs = emAs;
	}
	
	public EntityTransaction getUtxAs(){
		return utxAs;
	}
	
	public void setUtxAs(EntityTransaction utxAs){
		this.utxAs = utxAs;
	}
	
	public EntityManagerFactory getEmfSqlite(){
		return emfSqlite;
	}
	
	public void setEmfSqlite(EntityManagerFactory emfSqlite){
		this.emfSqlite = emfSqlite;
	}
	
	public EntityManager getEmSqlite(){
		return emSqlite;
	}
	
	public void setEmSqlite(EntityManager emSqlite){
		this.emSqlite = emSqlite;
	}
	
	public EntityTransaction getUtxSqlite(){
		return utxSqlite;
	}
	
	public void setUtxSqlite(EntityTransaction utxSqlite){
		this.utxSqlite = utxSqlite;
	}
}
