package it.cascino.inventario.dbsqlite.dao;

import java.util.List;
import it.cascino.inventario.dbsqlite.model.SqliteRelArticoliBarcode;

public interface SqliteRelArticoliBarcodeDao{
	List<SqliteRelArticoliBarcode> getAll();
	
	void salva(SqliteRelArticoliBarcode a);
	
	void aggiorna(SqliteRelArticoliBarcode a);
	
	void elimina(SqliteRelArticoliBarcode a);

	SqliteRelArticoliBarcode getRelArticoliBarcodeDaIdArt(Integer idArt);
	
	SqliteRelArticoliBarcode getRelArticoliBarcodeDaIdBc(Integer idBc);
	
	void close();
}
