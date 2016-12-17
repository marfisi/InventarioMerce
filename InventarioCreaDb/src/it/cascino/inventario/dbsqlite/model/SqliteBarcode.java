package it.cascino.inventario.dbsqlite.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
* The persistent class for the barcode database table.
* 
*/
@Entity(name="barcode")
@NamedQueries({
	@NamedQuery(name = "SqliteBarcode.findAll", query = "SELECT a FROM barcode a"),
	@NamedQuery(name = "SqliteBarcode.findById", query = "SELECT a FROM barcode a WHERE a._id = :id"),
	@NamedQuery(name = "SqliteBarcode.findByCodice", query = "SELECT a FROM barcode a WHERE a.codice = :codice")
})
public class SqliteBarcode implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Integer _id;
	private String codice;
	private String tipo;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer get_id(){
		return _id;
	}

	public void set_id(Integer _id){
		this._id = _id;
	}
	
	public String getCodice(){
		return codice;
	}

	public void setCodice(String codice){
		this.codice = codice;
	}

	public String getTipo(){
		return tipo;
	}

	public void setTipo(String tipo){
		this.tipo = tipo;
	}

	@Override
	public int hashCode(){
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_id == null) ? 0 : _id.hashCode());
		result = prime * result + ((codice == null) ? 0 : codice.hashCode());
		result = prime * result + ((tipo == null) ? 0 : tipo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj){
		if(obj instanceof SqliteBarcode){
			if(this._id == ((SqliteBarcode)obj)._id){
				return true;
			}else{
				return false;
			}
		}
		return false;
	}

	@Override
	public String toString(){
		return "SqliteBarcode [_id=" + _id + ", codice=" + codice + ", tipo=" + tipo + "]";
	}	
}
