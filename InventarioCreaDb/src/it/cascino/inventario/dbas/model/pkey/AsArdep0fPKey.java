package it.cascino.inventario.dbas.model.pkey;

import java.io.Serializable;
import javax.persistence.Embeddable;
import org.apache.commons.lang3.StringUtils;
import it.cascino.inventario.dbsqlite.model.SqliteRelArticoliBarcode;

@Embeddable
public class AsArdep0fPKey implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String dcoda;
	private Integer dcode;

	public AsArdep0fPKey(){
	}

	public AsArdep0fPKey(String dcoda, Integer dcode){
		super();
		this.dcoda = dcoda;
		this.dcode = dcode;
	}
	
	public String getDcoda(){
		return dcoda;
	}

	public void setDcoda(String dcoda){
		this.dcoda = dcoda;
	}

	public Integer getDcode(){
		return dcode;
	}

	public void setDcode(Integer dcode){
		this.dcode = dcode;
	}

	@Override
	public int hashCode(){
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dcoda == null) ? 0 : dcoda.hashCode());
		result = prime * result + ((dcode == null) ? 0 : dcode.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj){
		if(obj instanceof SqliteRelArticoliBarcode){
			if((this.dcoda == ((AsArdep0fPKey)obj).dcoda)&&(this.dcode == ((AsArdep0fPKey)obj).dcode)){
				return true;
			}else{
				return false;
			}
		}
		return false;
	}

	@Override
	public String toString(){
		return "AsArdep0fPKey [dcoda=" + dcoda + ", dcode=" + dcode + "]";
	}
}
