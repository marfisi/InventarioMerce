package it.cascino.inventario.dbsqlite.model.pkey;

import java.io.Serializable;
import javax.persistence.Embeddable;
import org.apache.commons.lang3.StringUtils;
import it.cascino.inventario.dbsqlite.model.SqliteRelArticoliBarcode;

@Embeddable
public class SqliteRelArticoliBarcodePKey implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Integer idArt;
	private Integer idBc;

	public SqliteRelArticoliBarcodePKey(){
	}

	public SqliteRelArticoliBarcodePKey(Integer idArt, Integer idBc){
		super();
		this.idArt = idArt;
		this.idBc = idBc;
	}
	
	public Integer getIdArt(){
		return idArt;
	}

	public void setIdArt(Integer idArt){
		this.idArt = idArt;
	}

	public Integer getIdBc(){
		return idBc;
	}

	public void setIdBc(Integer idBc){
		this.idBc = idBc;
	}

	@Override
	public int hashCode(){
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idArt == null) ? 0 : idArt.hashCode());
		result = prime * result + ((idBc == null) ? 0 : idBc.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj){
		if(obj instanceof SqliteRelArticoliBarcode){
			if((this.idArt == ((SqliteRelArticoliBarcodePKey)obj).idArt)&&(this.idBc == ((SqliteRelArticoliBarcodePKey)obj).idBc)){
				return true;
			}else{
				return false;
			}
		}
		return false;
	}

	@Override
	public String toString(){
		return "SqliteRelArticoliBarcodePKey [idArt=" + idArt + ", idBc=" + idBc + "]";
	}
}
