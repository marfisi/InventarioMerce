package it.cascino.smarcamentomerce.it.cascino.smarcamentomerce.model;

import java.sql.Timestamp;

public class Articolo {
	private String codice;
	private Barcode[] barcode;
	private String desc;
	private String um;
	private Float qty;
	private Float difettosi;
	private String dataCarico;
	private String dataScarico;
	private Integer stato;
	private Timestamp timestamp;

	public Articolo() {
		this.codice = "";
		this.barcode = new Barcode[1];
		this.desc = "";
		this.um = "";
		this.qty = -999.99f;
		this.difettosi = 0.0f;
		this.dataCarico = "";
		this.dataScarico = "";
		this.stato = -1;
		this.timestamp = null;
	}

	public Articolo(String codice, Barcode[] barcode, String desc, String um, Float qty, Float difettosi, String dataCarico, String dataScarico, Integer stato, Timestamp timestamp) {
		this.codice = codice;
		this.barcode = barcode;
		this.desc = desc;
		this.um = um;
		this.qty = qty;
		this.difettosi = difettosi;
		this.dataCarico = dataCarico;
		this.dataScarico = dataScarico;
		this.stato = stato;
		this.timestamp = timestamp;
	}

	public String getCodice() {
		return codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}

	public Barcode[] getBarcode() {
		return barcode;
	}

	public void setBarcode(Barcode[] barcode) {
		this.barcode = barcode;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getUm() {
		return um;
	}

	public void setUm(String um) {
		this.um = um;
	}

	public Float getQty() {
		return qty;
	}

	public void setQty(Float qty) {
		this.qty = qty;
	}

	public Float getDifettosi() {
		return difettosi;
	}

	public void setDifettosi(Float difettosi) {
		this.difettosi = difettosi;
	}

	public String getDataCarico() {
		return dataCarico;
	}

	public void setDataCarico(String dataCarico) {
		this.dataCarico = dataCarico;
	}

	public String getDataScarico() {
		return dataScarico;
	}

	public void setDataScarico(String dataScarico) {
		this.dataScarico = dataScarico;
	}

	public Integer getStato() {
		return stato;
	}

	public void setStato(Integer stato) {
		/*
		vuoto/null - da controllare (come 2)
		0 - controllato ok
		1 - controllato non quadrato
		2 - da controllare
		 */
		this.stato = stato;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
}
