package it.cascino.inventario.dbsqlite.dao;

import java.util.List;
import it.cascino.inventario.dbsqlite.model.SqliteQtyOriginali;

public interface SqliteQtyOriginaliDao{
	List<SqliteQtyOriginali> getAll();
	
	Integer salva(SqliteQtyOriginali a);
	
	void aggiorna(SqliteQtyOriginali a);
	
	void elimina(SqliteQtyOriginali a);

	SqliteQtyOriginali getQtyOriginaliDaId(Integer id);
	
	List<SqliteQtyOriginali> getQtyOriginaliDaIdArt(Integer idArt);

	List<SqliteQtyOriginali> getQtyOriginaliDaIdDep(Integer idDep);

	SqliteQtyOriginali getQtyOriginaliDaIdArtAndIdDep(Integer idArt, Integer idDep);

	void close();
}
