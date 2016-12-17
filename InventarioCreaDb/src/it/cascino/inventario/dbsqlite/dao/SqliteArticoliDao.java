package it.cascino.inventario.dbsqlite.dao;

import java.util.List;
import it.cascino.inventario.dbsqlite.model.SqliteArticoli;

public interface SqliteArticoliDao{
	List<SqliteArticoli> getAll();
	
	Integer salva(SqliteArticoli a);
	
	void aggiorna(SqliteArticoli a);
	
	void elimina(SqliteArticoli a);

	SqliteArticoli getArticoloDaId(Integer id);
	
	SqliteArticoli getArticoloDaCodice(String codiceArticolo);
	
	void close();
}
