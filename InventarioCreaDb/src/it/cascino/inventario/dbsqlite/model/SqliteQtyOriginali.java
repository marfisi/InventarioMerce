package it.cascino.inventario.dbsqlite.model;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
* The persistent class for the articoli database table.
* 
*/
@Entity(name="qty_originali")
@NamedQueries({
	@NamedQuery(name = "SqliteQtyOriginali.findAll", query = "SELECT a FROM qty_originali a"),
	@NamedQuery(name = "SqliteQtyOriginali.findById", query = "SELECT a FROM qty_originali a WHERE a._id = :id"),
	@NamedQuery(name = "SqliteQtyOriginali.findByIdArt", query = "SELECT a FROM qty_originali a WHERE a.idart = :idArt"),
	@NamedQuery(name = "SqliteQtyOriginali.findByIdDep", query = "SELECT a FROM qty_originali a WHERE a.iddep = :idDep"),
	@NamedQuery(name = "SqliteQtyOriginali.findByIdArtAndIdDep", query = "SELECT a FROM qty_originali a WHERE a.idart = :idArt and a.iddep = :idDep")
})
public class SqliteQtyOriginali implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Integer _id;
	private Integer idart;
	private Integer iddep;
	private BigDecimal  qty;
	private BigDecimal qty_difettosi;
	private String data_carico;
	private String data_scarico;
	private String data_inventario;
    private BigDecimal qty_scorta_min;
    private BigDecimal  qty_scorta_max;
    private BigDecimal  qty_trasf;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer get_id(){
		return _id;
	}

	public void set_id(Integer _id){
		this._id = _id;
	}

	public Integer getIdart(){
		return idart;
	}

	public void setIdart(Integer idart){
		this.idart = idart;
	}

	public Integer getIddep(){
		return iddep;
	}

	public void setIddep(Integer iddep){
		this.iddep = iddep;
	}

	public BigDecimal getQty(){
		return qty;
	}

	public void setQty(BigDecimal qty){
		this.qty = qty;
	}

	public BigDecimal getQty_difettosi(){
		return qty_difettosi;
	}

	public void setQty_difettosi(BigDecimal qty_difettosi){
		this.qty_difettosi = qty_difettosi;
	}

	public String getData_carico(){
		return data_carico;
	}

	public void setData_carico(String data_carico){
		this.data_carico = data_carico;
	}

	public String getData_scarico(){
		return data_scarico;
	}

	public void setData_scarico(String data_scarico){
		this.data_scarico = data_scarico;
	}

	public String getData_inventario(){
		return data_inventario;
	}

	public void setData_inventario(String data_inventario){
		this.data_inventario = data_inventario;
	}

	public BigDecimal getQty_scorta_min(){
		return qty_scorta_min;
	}

	public void setQty_scorta_min(BigDecimal qty_scorta_min){
		this.qty_scorta_min = qty_scorta_min;
	}

	public BigDecimal getQty_scorta_max(){
		return qty_scorta_max;
	}

	public void setQty_scorta_max(BigDecimal qty_scorta_max){
		this.qty_scorta_max = qty_scorta_max;
	}
	
	public BigDecimal getQty_trasf(){
		return qty_trasf;
	}

	public void setQty_trasf(BigDecimal qty_trasf){
		this.qty_trasf = qty_trasf;
	}

	@Override
	public int hashCode(){
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_id == null) ? 0 : _id.hashCode());
		result = prime * result + ((data_carico == null) ? 0 : data_carico.hashCode());
		result = prime * result + ((data_inventario == null) ? 0 : data_inventario.hashCode());
		result = prime * result + ((data_scarico == null) ? 0 : data_scarico.hashCode());
		result = prime * result + ((idart == null) ? 0 : idart.hashCode());
		result = prime * result + ((iddep == null) ? 0 : iddep.hashCode());
		result = prime * result + ((qty == null) ? 0 : qty.hashCode());
		result = prime * result + ((qty_difettosi == null) ? 0 : qty_difettosi.hashCode());
		result = prime * result + ((qty_scorta_max == null) ? 0 : qty_scorta_max.hashCode());
		result = prime * result + ((qty_scorta_min == null) ? 0 : qty_scorta_min.hashCode());
		result = prime * result + ((qty_trasf == null) ? 0 : qty_trasf.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj){
		if(obj instanceof SqliteQtyOriginali){
			if(this._id == ((SqliteQtyOriginali)obj)._id){
				return true;
			}else{
				return false;
			}
		}
		return false;
	}

	@Override
	public String toString(){
		return "SqliteQtyOriginali [_id=" + _id + ", idart=" + idart + ", iddep=" + iddep + ", qty=" + qty + ", qty_difettosi=" + qty_difettosi + ", data_carico=" + data_carico + ", data_scarico=" + data_scarico + ", data_inventario=" + data_inventario + ", qty_scorta_min=" + qty_scorta_min + ", qty_scorta_max=" + qty_scorta_max + ", qty_trasf=" + qty_trasf + "]";
	}
}
