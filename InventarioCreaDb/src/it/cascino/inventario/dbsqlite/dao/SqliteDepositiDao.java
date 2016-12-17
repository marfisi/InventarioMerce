package it.cascino.inventario.dbsqlite.dao;

import java.util.List;
import it.cascino.inventario.dbsqlite.model.SqliteDepositi;

public interface SqliteDepositiDao{
	List<SqliteDepositi> getAll();
	
	Integer salva(SqliteDepositi a);
	
	void aggiorna(SqliteDepositi a);
	
	void elimina(SqliteDepositi a);

	SqliteDepositi getDepositoDaId(Integer id);
	
	SqliteDepositi getDepositoDaIdDep(String idDep);
	
	void close();
}
