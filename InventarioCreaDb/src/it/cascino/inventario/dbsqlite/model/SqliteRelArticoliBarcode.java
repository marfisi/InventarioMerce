package it.cascino.inventario.dbsqlite.model;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import it.cascino.inventario.dbsqlite.model.pkey.SqliteRelArticoliBarcodePKey;

/**
* The persistent class for the rel_articoli_barcode database table.
* 
*/
@Entity(name="rel_articoli_barcode")
@NamedQueries({
	@NamedQuery(name = "SqliteRelArticoliBarcode.findAll", query = "SELECT a FROM rel_articoli_barcode a"),
	@NamedQuery(name = "SqliteRelArticoliBarcode.findByIdArt", query = "SELECT a FROM rel_articoli_barcode a WHERE a.id.idArt = :idArt"),
	@NamedQuery(name = "SqliteRelArticoliBarcode.findByIdBc", query = "SELECT a FROM rel_articoli_barcode a WHERE a.id.idBc = :idBc")
})
public class SqliteRelArticoliBarcode implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private SqliteRelArticoliBarcodePKey id;
	
	public SqliteRelArticoliBarcode(){
	}

	public SqliteRelArticoliBarcode(SqliteRelArticoliBarcodePKey id){
		super();
		this.id = id;
	}

	public SqliteRelArticoliBarcodePKey getId(){
		return id;
	}

	public void setId(SqliteRelArticoliBarcodePKey id){
		this.id = id;
	}

	@Override
	public int hashCode(){
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj){
		if(obj instanceof SqliteRelArticoliBarcode){
			if(this.id == ((SqliteRelArticoliBarcode)obj).id){
				return true;
			}else{
				return false;
			}
		}
		return false;
	}

	@Override
	public String toString(){
		return "SqliteRelArticoliBarcode [id=" + id + "]";
	}	
}
