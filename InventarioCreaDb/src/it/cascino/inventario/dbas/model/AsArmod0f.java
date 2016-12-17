package it.cascino.inventario.dbas.model;

import java.io.Serializable;
import javax.persistence.*;
import it.cascino.inventario.dbas.model.pkey.AsArmod0fPKey;

/**
* The persistent class for the cas_dat/Armod0f database table.
* 
*/
@Entity(name = "Armod0f")
@NamedQueries({
	@NamedQuery(name = "AsArmod0f.findAll", query = "SELECT a FROM Armod0f a"),
	@NamedQuery(name = "AsArmod0f.findByDcoda", query = "SELECT a FROM Armod0f a WHERE a.id.dcoda = :dcoda and a.dflal = ' '"),
	@NamedQuery(name = "AsArmod0f.findByDcode", query = "SELECT a FROM Armod0f a WHERE a.id.dcode = :dcode and a.dflal = ' '"),
	@NamedQuery(name = "AsArmod0f.findByDcodaAndDcode", query = "SELECT a FROM Armod0f a WHERE a.id.dcoda = :dcoda and a.id.dcode = :dcode and a.dflal = ' '")
})
public class AsArmod0f implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private AsArmod0fPKey id;
	private String dflal;
	
	public AsArmod0f(){
	}
	
	public AsArmod0fPKey getId(){
		return id;
	}
	
	public void setId(AsArmod0fPKey id){
		this.id = id;
	}
	
	public String getDflal(){
		return dflal;
	}

	public void setDflal(String dflal){
		this.dflal = dflal;
	}

	@Override
	public int hashCode(){
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dflal == null) ? 0 : dflal.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj){
		if(obj instanceof AsArmod0f) {
			if(this.id == ((AsArmod0f)obj).id) {
				return true;
			}else{
				return false;
			}
		}
		return false;
	}

	@Override
	public String toString(){
		return "AsArmod0f [id=" + id + ", dflal=" + dflal + "]";
	}
}