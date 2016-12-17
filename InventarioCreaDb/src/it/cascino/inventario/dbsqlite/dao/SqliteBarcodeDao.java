package it.cascino.inventario.dbsqlite.dao;

import java.util.List;
import it.cascino.inventario.dbsqlite.model.SqliteBarcode;

public interface SqliteBarcodeDao{
	List<SqliteBarcode> getAll();
	
	Integer salva(SqliteBarcode a);
	
	void aggiorna(SqliteBarcode a);
	
	void elimina(SqliteBarcode a);

	SqliteBarcode getBarcodeDaId(Integer id);
	
	SqliteBarcode getBarcodeDaCodice(String codice);
	
	void close();
}
