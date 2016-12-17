package it.cascino.inventario.dbsqlite.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
* The persistent class for the depositi database table.
* 
*/
@Entity(name="depositi")
@NamedQueries({
	@NamedQuery(name = "SqliteDepositi.findAll", query = "SELECT a FROM depositi a"),
	@NamedQuery(name = "SqliteDepositi.findById", query = "SELECT a FROM depositi a WHERE a._id = :id"),
	@NamedQuery(name = "SqliteDepositi.findByIdDep", query = "SELECT a FROM depositi a WHERE a.iddep = :iddep")
})
public class SqliteDepositi implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Integer _id;
	private String iddep;
	private String desc;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer get_id(){
		return _id;
	}

	public void set_id(Integer _id){
		this._id = _id;
	}
	
	public String getIddep(){
		return iddep;
	}

	public void setIddep(String iddep){
		this.iddep = iddep;
	}

	public String getDesc(){
		return desc;
	}

	public void setDesc(String desc){
		this.desc = desc;
	}

	@Override
	public int hashCode(){
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_id == null) ? 0 : _id.hashCode());
		result = prime * result + ((iddep == null) ? 0 : iddep.hashCode());
		result = prime * result + ((desc == null) ? 0 : desc.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj){
		if(obj instanceof SqliteDepositi){
			if(this._id == ((SqliteDepositi)obj)._id){
				return true;
			}else{
				return false;
			}
		}
		return false;
	}

	@Override
	public String toString(){
		return "SqliteDepositi [_id=" + _id + ", iddep=" + iddep + ", desc=" + desc + "]";
	}
}
