package it.cascino.inventario.dbas.model;

import java.io.Serializable;
import javax.persistence.*;
import org.apache.commons.lang3.StringUtils;

/**
* The persistent class for the cas_dat/anmag0f database table.
* 
*/
@Entity(name="Anmag0f")
@NamedQueries({
	@NamedQuery(name = "AsAnmag0f.findAll", query = "SELECT a FROM Anmag0f a WHERE a.atama != 'A' order by a.mcoda asc"),
	@NamedQuery(name = "AsAnmag0f.findByMcoda", query = "SELECT a FROM Anmag0f a WHERE a.atama != 'A' and a.mcoda = :mcoda ")
})
public class AsAnmag0f implements Serializable{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Logger
	 */
//	@Inject
//	private Logger log;
	
	private String atama;
	private String mcoda;
	private String mdesc;
	private String mumis;
	private Float mpeu2;
	private Float mconf;
	
	public AsAnmag0f(){
	}
	
	public AsAnmag0f(String atama, String mcoda, String mdesc, String mumis, Float mpeu2, Float mconf){
		super();
		this.atama = atama;
		this.mcoda = mcoda;
		this.mdesc = mdesc;
		this.mumis = mumis;
		this.mpeu2 = mpeu2;
		this.mconf = mconf;
	}

	public String getAtama(){
		return atama;
	}

	public void setAtama(String atama){
		this.atama = atama;
	}

	@Id
	public String getMcoda(){
		return mcoda;
	}

	public void setMcoda(String mcoda){
		this.mcoda = mcoda;
	}
	
	public String getMdesc(){
		return mdesc;
	}

	public void setMdesc(String mdesc){
		this.mdesc = mdesc;
	}

	public String getMumis(){
		return mumis;
	}

	public void setMumis(String mumis){
		this.mumis = mumis;
	}

	public Float getMpeu2(){
		return mpeu2;
	}

	public void setMpeu2(Float mpeu2){
		this.mpeu2 = mpeu2;
	}

	public Float getMconf(){
		return mconf;
	}

	public void setMconf(Float mconf){
		this.mconf = mconf;
	}

	@Override
	public boolean equals(Object obj){
		if(obj instanceof AsAnmag0f){
			if(this.mcoda == ((AsAnmag0f)obj).mcoda){
				return true;
			}else{
				return false;
			}
		}
		return false;
	}

	@Override
	public int hashCode(){
		final int prime = 31;
		int result = 1;
		result = prime * result + ((atama == null) ? 0 : atama.hashCode());
		result = prime * result + ((mcoda == null) ? 0 : mcoda.hashCode());
		result = prime * result + ((mdesc == null) ? 0 : mdesc.hashCode());
		result = prime * result + ((mumis == null) ? 0 : mumis.hashCode());
		result = prime * result + ((mpeu2 == null) ? 0 : mpeu2.hashCode());
		result = prime * result + ((mconf == null) ? 0 : mconf.hashCode());
		return result;
	}
	
	@Override
	public String toString(){
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(this.getClass().getName().substring(this.getClass().getName().lastIndexOf(".") + 1));
		stringBuilder.append("[");
		stringBuilder.append("mcoda=" + StringUtils.trim(mcoda)).append(", ");
		stringBuilder.append("atama=" + StringUtils.trim(atama)).append(", ");
		stringBuilder.append("mcoda=" + StringUtils.trim(mcoda)).append(", ");
		stringBuilder.append("mumis=" + StringUtils.trim(mumis)).append(", ");
		stringBuilder.append("mpeu2=" + mpeu2).append(", ");
		stringBuilder.append("mconf=" + mconf);
		stringBuilder.append("]");
		return stringBuilder.toString();
	}
}