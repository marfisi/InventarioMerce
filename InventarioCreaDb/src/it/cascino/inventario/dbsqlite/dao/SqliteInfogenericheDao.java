package it.cascino.inventario.dbsqlite.dao;

import java.util.List;
import it.cascino.inventario.dbsqlite.model.SqliteInfogeneriche;

public interface SqliteInfogenericheDao{
	List<SqliteInfogeneriche> getAll();
	
	Integer salva(SqliteInfogeneriche a);
	
	void aggiorna(SqliteInfogeneriche a);
	
	void elimina(SqliteInfogeneriche a);

	SqliteInfogeneriche getInfoDaId(Integer id);
	
	SqliteInfogeneriche getInfoDaNomeInfo(String nomeInfo);
	
	void close();
}
