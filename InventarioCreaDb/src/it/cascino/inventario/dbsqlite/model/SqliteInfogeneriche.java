package it.cascino.inventario.dbsqlite.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
* The persistent class for the infogeneriche database table.
* 
*/
@Entity(name="infogeneriche")
@NamedQueries({
	@NamedQuery(name = "SqliteInfogeneriche.findAll", query = "SELECT a FROM infogeneriche a"),
	@NamedQuery(name = "SqliteInfogeneriche.findById", query = "SELECT a FROM infogeneriche a WHERE a._id = :id"),
	@NamedQuery(name = "SqliteInfogeneriche.findByNome", query = "SELECT a FROM infogeneriche a WHERE a.info = :info")
})
public class SqliteInfogeneriche implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Integer _id;
	private String info;
	private String valore;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer get_id(){
		return _id;
	}

	public void set_id(Integer _id){
		this._id = _id;
	}

	public String getInfo(){
		return info;
	}

	public void setInfo(String info){
		this.info = info;
	}

	public String getValore(){
		return valore;
	}

	public void setValore(String valore){
		this.valore = valore;
	}

	@Override
	public int hashCode(){
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_id == null) ? 0 : _id.hashCode());
		result = prime * result + ((info == null) ? 0 : info.hashCode());
		result = prime * result + ((valore == null) ? 0 : valore.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj){
		if(obj instanceof SqliteInfogeneriche){
			if(this._id == ((SqliteInfogeneriche)obj)._id){
				return true;
			}else{
				return false;
			}
		}
		return false;
	}

	@Override
	public String toString(){
		return "SqliteInfogeneriche [_id=" + _id + ", info=" + info + ", valore=" + valore + "]";
	}
}
