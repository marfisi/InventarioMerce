package it.cascino.smarcamentomerce.it.cascino.smarcamentomerce.model;

import android.util.Log;

import java.sql.Timestamp;
import java.util.Arrays;

public class Articolo{
	private Integer ordinamento;
	private String codice;
	private Barcode[] barcode;
	private String desc;
	private String um;
	private Float qtyOriginale;
	private Float qtyRilevata;
	private Float qtyDifettOriginale;
	private Float qtyDifettRilevata;
	private String dataCarico;
	private String dataScarico;
	private String dataUltimoInventario;
	private Float scortaMinOriginale;
	private Float scortaMinRilevata;
	private Float scortaMaxOriginale;
	private Float scortaMaxRilevata;
	private String commento;

	private Integer stato;
	private Timestamp timestamp;

	public Articolo(){
		this.ordinamento = -999;
		this.codice = "";
		this.barcode = new Barcode[1];
		this.desc = "";
		this.um = "";
		this.qtyOriginale = -999.99f;
		this.qtyRilevata = -999.99f;
		this.qtyDifettOriginale = -999.99f;
		this.qtyDifettRilevata = -999.99f;
		this.dataCarico = "";
		this.dataScarico = "";
		this.dataUltimoInventario = "";
		this.scortaMinOriginale = -999.99f;
		this.scortaMinRilevata = -999.99f;
		this.scortaMaxOriginale = -999.99f;
		this.scortaMaxRilevata = -999.99f;
		this.commento = "";
		this.stato = -1;
		this.timestamp = null;
	}

	public Articolo(Integer ordinamento, String codice, Barcode[] barcode, String desc, String um, Float qtyOriginale, Float qtyRilevata, Float qtyDifettOriginale, Float qtyDifettRilevata, String dataCarico, String dataScarico, String dataUltimoInventario, Float scortaMinOriginale, Float scortaMinRilevata, Float scortaMaxOriginale, Float scortaMaxRilevata, String commento, Integer stato, Timestamp timestamp){
		this.ordinamento = ordinamento;
		this.codice = codice;
		this.barcode = barcode;
		this.desc = desc;
		this.um = um;
		this.qtyOriginale = qtyOriginale;
		this.qtyRilevata = qtyRilevata;
		this.qtyDifettOriginale = qtyDifettOriginale;
		this.qtyDifettRilevata = qtyDifettRilevata;
		this.dataCarico = dataCarico;
		this.dataScarico = dataScarico;
		this.dataUltimoInventario = dataUltimoInventario;
		this.scortaMinOriginale = scortaMinOriginale;
		this.scortaMinRilevata = scortaMinRilevata;
		this.scortaMaxOriginale = scortaMaxOriginale;
		this.scortaMaxRilevata = scortaMaxRilevata;
		this.commento = commento;
		this.stato = stato;
		this.timestamp = timestamp;
	}

	public Integer getOrdinamento(){
		return ordinamento;
	}

	public void setOrdinamento(Integer ordinamento){
		this.ordinamento = ordinamento;
	}

	public String getCodice(){
		return codice;
	}

	public void setCodice(String codice){
		this.codice = codice;
	}

	public Barcode[] getBarcode(){
		return barcode;
	}

	public void setBarcode(Barcode[] barcode){
		this.barcode = barcode;
	}

	public String getDesc(){
		return desc;
	}

	public void setDesc(String desc){
		this.desc = desc;
	}

	public String getUm(){
		return um;
	}

	public void setUm(String um){
		this.um = um;
	}

	public Float getQtyOriginale(){
		return qtyOriginale;
	}

	public void setQtyOriginale(Float qtyOriginale){
		this.qtyOriginale = qtyOriginale;
	}

	public Float getQtyRilevata(){
		return qtyRilevata;
	}

	public void setQtyRilevata(Float qtyRilevata){
		this.qtyRilevata = qtyRilevata;
	}

	public Float getQtyDifettOriginale(){
		return qtyDifettOriginale;
	}

	public void setQtyDifettOriginale(Float qtyDifettOriginale){
		this.qtyDifettOriginale = qtyDifettOriginale;
	}

	public Float getQtyDifettRilevata(){
		return qtyDifettRilevata;
	}

	public void setQtyDifettRilevata(Float qtyDifettRilevata){
		this.qtyDifettRilevata = qtyDifettRilevata;
	}

	public String getDataCarico(){
		return dataCarico;
	}

	public void setDataCarico(String dataCarico){
		this.dataCarico = dataCarico;
	}

	public String getDataScarico(){
		return dataScarico;
	}

	public void setDataScarico(String dataScarico){
		this.dataScarico = dataScarico;
	}

	public String getDataUltimoInventario(){
		return dataUltimoInventario;
	}

	public void setDataUltimoInventario(String dataUltimoInventario){
		this.dataUltimoInventario = dataUltimoInventario;
	}

	public Float getScortaMinOriginale(){
		return scortaMinOriginale;
	}

	public void setScortaMinOriginale(Float scortaMinOriginale){
		this.scortaMinOriginale = scortaMinOriginale;
	}

	public Float getScortaMinRilevata(){
		return scortaMinRilevata;
	}

	public void setScortaMinRilevata(Float scortaMinRilevata){
		this.scortaMinRilevata = scortaMinRilevata;
	}

	public Float getScortaMaxOriginale(){
		return scortaMaxOriginale;
	}

	public void setScortaMaxOriginale(Float scortaMaxOriginale){
		this.scortaMaxOriginale = scortaMaxOriginale;
	}

	public Float getScortaMaxRilevata(){
		return scortaMaxRilevata;
	}

	public void setScortaMaxRilevata(Float scortaMaxRilevata){
		this.scortaMaxRilevata = scortaMaxRilevata;
	}

	public String getCommento(){
		return commento;
	}

	public void setCommento(String commento){
		this.commento = commento;
	}

	public Integer getStato(){
		return stato;
	}

	public void setStato(Integer stato){
		/*
		vuoto/null - da controllare (come 2)
		0 - controllato ok
		1 - controllato non quadrato
		2 - da controllare
		 */
		this.stato = stato;
	}

	public Timestamp getTimestamp(){
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp){
		this.timestamp = timestamp;
	}

	public String toString(){
		String strToStr = getCodice() + " - ";
		for(int i = 0; i < getBarcode().length; i++){
			strToStr = strToStr + getBarcode()[i].getCodice() + " ";
		}
		Log.i("artToString", strToStr);
		return strToStr;
	}
}
