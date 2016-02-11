package it.cascino.smarcamentomerce.it.cascino.smarcamentomerce.model;

import android.util.Log;

import java.sql.Timestamp;
import java.util.Arrays;

public class Articolo {
	private String codice;
	private Barcode[] barcode;
	private String desc;
	private String um;
	private Float qtyOriginale;
	private Float qtyRilevata;
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
		this.qtyOriginale = -999.99f;
		this.qtyRilevata =  -999.99f;
		this.difettosi = 0.0f;
		this.dataCarico = "";
		this.dataScarico = "";
		this.stato = -1;
		this.timestamp = null;
	}

	public Articolo(String codice, Barcode[] barcode, String desc, String um, Float qtyOriginale, Float qtyRilevata, Float difettosi, String dataCarico, String dataScarico, Integer stato, Timestamp timestamp) {
		this.codice = codice;
		this.barcode = barcode;
		this.desc = desc;
		this.um = um;
		this.qtyOriginale = qtyOriginale;
		this.qtyRilevata = qtyRilevata;
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

	public Float getQtyOriginale() {
		return qtyOriginale;
	}

	public void setQtyOriginale(Float qtyOriginale) {
		this.qtyOriginale = qtyOriginale;
	}

	public Float getQtyRilevata() {
		return qtyRilevata;
	}

	public void setQtyRilevata(Float qtyRilevata) {
		this.qtyRilevata = qtyRilevata;
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

	public String toString(){
		String strToStr = getCodice() + " - ";
		for(int i=0;i<getBarcode().length;i++){
			strToStr = strToStr + getBarcode()[i].getCodice() + " ";
		}
		Log.i("artToString", strToStr);
		return strToStr;
	}
}
