package it.cascino.smarcamentomerce.model;

import android.util.Log;

import org.apache.commons.lang3.StringUtils;

import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Locale;

import it.cascino.smarcamentomerce.utils.Support;
import it.cascino.smarcamentomerce.utils.TipoStato;

public class Articolo{
	private String codice;
	private Barcode[] barcodeOriginale;
	private Barcode[] barcodeRilevata;
	private String descOriginale;
	private String descRilevata;
	private Float prezzoOriginale;
	private Float prezzoRilevata;
	private String um;
	private Float qtyOriginale;
	private Float qtyRilevata;
	private Float qtyEsposteOriginale;
	private Float qtyEsposteRilevata;
	private Float qtyMagazOriginale;
	private Float qtyMagazRilevata;
	private Float qtyDifettOriginale;
	private Float qtyDifettRilevata;
	private Float qtyPerConfezOriginale;
	private Float qtyPerConfezRilevata;
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
	private Integer ordinamento;
	private Boolean inModifica;	// flag che in true se e' in editing ed e' stato modificato almeno un campo

	public Articolo(){
		this.codice = null;
		this.barcodeOriginale = null;
		this.barcodeRilevata = null;
		this.descOriginale = null;
		this.descRilevata = null;
		this.prezzoOriginale = null;
		this.prezzoRilevata = null;
		this.um = null;
		this.qtyOriginale = null;
		this.qtyRilevata = null;
		this.qtyEsposteOriginale = null;
		this.qtyEsposteRilevata = null;
		this.qtyMagazOriginale = null;
		this.qtyMagazRilevata = null;
		this.qtyDifettOriginale = null;
		this.qtyDifettRilevata = null;
		this.qtyPerConfezOriginale = null;
		this.qtyPerConfezRilevata = null;
		this.dataCarico = null;
		this.dataScarico = null;
		this.dataUltimoInventario = null;
		this.scortaMinOriginale = null;
		this.scortaMinRilevata = null;
		this.scortaMaxOriginale = null;
		this.scortaMaxRilevata = null;
		this.commento = null;
		this.stato = null;
		this.timestamp = null;
		this.ordinamento = null;
		this.inModifica = false;
	}
/*	public Articolo(){
		this.codice = "";
		this.barcodeOriginale = null;
		this.barcodeRilevata = null;
		this.descOriginale = "";
		this.descRilevata = "";
		this.prezzoOriginale = -999.99f;
		this.prezzoRilevata = -999.99f;
		this.um = "";
		this.qtyOriginale = -999.99f;
		this.qtyRilevata = -999.99f;
		this.qtyEsposteOriginale = -999.99f;
		this.qtyEsposteRilevata = -999.99f;
		this.qtyMagazOriginale = -999.99f;
		this.qtyMagazRilevata = -999.99f;
		this.qtyDifettOriginale = -999.99f;
		this.qtyDifettRilevata = -999.99f;
		this.qtyPerConfezOriginale = -999.99f;
		this.qtyPerConfezRilevata = -999.99f;
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
		this.ordinamento = -999;
		this.inModifica = false;
	}*/

	public Articolo(String codice, Barcode[] barcodeOriginale, Barcode[] barcodeRilevata, String descOriginale, String descRilevata, Float prezzoOriginale, Float prezzoRilevata, String um, Float qtyOriginale, Float qtyRilevata, Float qtyEsposteOriginale, Float qtyEsposteRilevata, Float qtyMagazOriginale, Float qtyMagazRilevata, Float qtyDifettOriginale, Float qtyDifettRilevata, Float qtyPerConfezOriginale, Float qtyPerConfezRilevata, String dataCarico, String dataScarico, String dataUltimoInventario, Float scortaMinOriginale, Float scortaMinRilevata, Float scortaMaxOriginale, Float scortaMaxRilevata, String commento, Integer stato, Timestamp timestamp, Integer ordinamento, Boolean inModifica){
		this.codice = codice;
		this.barcodeOriginale = barcodeOriginale;
		this.barcodeRilevata = barcodeRilevata;
		this.descOriginale = descOriginale;
		this.descRilevata = descRilevata;
		this.prezzoOriginale = prezzoOriginale;
		this.prezzoRilevata = prezzoRilevata;
		this.um = um;
		this.qtyOriginale = qtyOriginale;
		this.qtyRilevata = qtyRilevata;
		this.qtyEsposteOriginale = qtyEsposteOriginale;
		this.qtyEsposteRilevata = qtyEsposteRilevata;
		this.qtyMagazOriginale = qtyMagazOriginale;
		this.qtyMagazRilevata = qtyMagazRilevata;
		this.qtyDifettOriginale = qtyDifettOriginale;
		this.qtyDifettRilevata = qtyDifettRilevata;
		this.qtyPerConfezOriginale = qtyPerConfezOriginale;
		this.qtyPerConfezRilevata = qtyPerConfezRilevata;
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
		this.ordinamento = ordinamento;
		this.inModifica = inModifica;
	}

	public String getCodice(){
		return codice;
	}

	public void setCodice(String codice){
		this.codice = codice;
	}

	public Barcode[] getBarcodeOriginale(){
		return barcodeOriginale;
	}

	public void setBarcodeOriginale(Barcode[] barcodeOriginale){
		this.barcodeOriginale = barcodeOriginale;
	}

	public Barcode[] getBarcodeRilevata(){
		return barcodeRilevata;
	}

	public void setBarcodeRilevata(Barcode[] barcodeRilevata){
		this.barcodeRilevata = barcodeRilevata;
	}

	public String getDescOriginale(){
		return descOriginale;
	}

	public void setDescOriginale(String descOriginale){
		this.descOriginale = descOriginale;
	}

	public String getDescRilevata(){
		return descRilevata;
	}

	public void setDescRilevata(String descRilevata){
		this.descRilevata = descRilevata;
	}

	public Float getPrezzoOriginale(){
		return prezzoOriginale;
	}

	public void setPrezzoOriginale(Float prezzoOriginale){
		this.prezzoOriginale = prezzoOriginale;
	}

	public Float getPrezzoRilevata(){
		return prezzoRilevata;
	}

	public void setPrezzoRilevata(Float prezzoRilevata){
		this.prezzoRilevata = prezzoRilevata;
	}

	public Float getQtyEsposteOriginale(){
		return qtyEsposteOriginale;
	}

	public void setQtyEsposteOriginale(Float qtyEsposteOriginale){
		this.qtyEsposteOriginale = qtyEsposteOriginale;
	}

	public Float getQtyEsposteRilevata(){
		return qtyEsposteRilevata;
	}

	public void setQtyEsposteRilevata(Float qtyEsposteRilevata){
		this.qtyEsposteRilevata = qtyEsposteRilevata;
	}

	public Float getQtyMagazOriginale(){
		return qtyMagazOriginale;
	}

	public void setQtyMagazOriginale(Float qtyMagazOriginale){
		this.qtyMagazOriginale = qtyMagazOriginale;
	}

	public Float getQtyMagazRilevata(){
		return qtyMagazRilevata;
	}

	public void setQtyMagazRilevata(Float qtyMagazRilevata){
		this.qtyMagazRilevata = qtyMagazRilevata;
	}

	public Float getQtyPerConfezOriginale(){
		return qtyPerConfezOriginale;
	}

	public void setQtyPerConfezOriginale(Float qtyPerConfezOriginale){
		this.qtyPerConfezOriginale = qtyPerConfezOriginale;
	}

	public Float getQtyPerConfezRilevata(){
		return qtyPerConfezRilevata;
	}

	public void setQtyPerConfezRilevata(Float qtyPerConfezRilevata){
		this.qtyPerConfezRilevata = qtyPerConfezRilevata;
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

	public Integer getOrdinamento(){
		return ordinamento;
	}

	public void setOrdinamento(Integer ordinamento){
		this.ordinamento = ordinamento;
	}

	public Boolean getInModifica(){
		return inModifica;
	}

	public void setInModifica(Boolean inModifica){
		this.inModifica = inModifica;
	}

	// occhio a non modificarla, e' utilizzata per il filtro della listview
	public String toString(){
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(getCodice());
		stringBuilder.append(" - ");
		for(int i = 0; i < getBarcodeOriginale().length; i++){
			stringBuilder.append(getBarcodeOriginale()[i].getCodice());
			stringBuilder.append(" ");
		}
		Log.i("artToString", stringBuilder.toString());
		return stringBuilder.toString();
	}

	public String toStringPerFtpFile(){
		String campiSep = "|";
		String insideCampSep = ",";

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(getCodice()).append(campiSep);
		if(getBarcodeRilevata().length != getBarcodeOriginale().length){
			for(int i = 0; i < getBarcodeRilevata().length; i++){
				stringBuilder.append(getBarcodeRilevata()[i].getCodice()).append(insideCampSep);
			}
		}
		stringBuilder.append(campiSep);
		if(!(StringUtils.equals(getDescRilevata(), getDescOriginale()))){
			stringBuilder.append(getDescRilevata());
		}
		stringBuilder.append(campiSep);
		if(Float.compare(getPrezzoRilevata(), getPrezzoOriginale()) != 0){
			stringBuilder.append(Support.floatToString(getPrezzoRilevata()));
		}
		stringBuilder.append(campiSep);
		stringBuilder.append(Support.floatToString(getQtyEsposteOriginale())).append(campiSep);
		if(getStato().compareTo(2) != 0){
			stringBuilder.append(Support.floatToString(getQtyEsposteRilevata())).append(campiSep);
		}else{
			stringBuilder.append(Support.floatToString(getQtyEsposteOriginale())).append(campiSep);
		}
		stringBuilder.append(Support.floatToString(getQtyMagazOriginale())).append(campiSep);
		if(getStato().compareTo(2) != 0){
			stringBuilder.append(Support.floatToString(getQtyMagazRilevata())).append(campiSep);
		}else{
			stringBuilder.append(Support.floatToString(getQtyMagazOriginale())).append(campiSep);
		}
		stringBuilder.append(Support.floatToString(getQtyDifettOriginale())).append(campiSep);
		if(getStato().compareTo(2) != 0){
			stringBuilder.append(Support.floatToString(getQtyDifettRilevata())).append(campiSep);
		}else{
			stringBuilder.append(Support.floatToString(getQtyDifettOriginale())).append(campiSep);
		}
		stringBuilder.append(Support.floatToString(getScortaMinOriginale())).append(campiSep);
		if(getStato().compareTo(2) != 0){
			stringBuilder.append(Support.floatToString(getScortaMinRilevata())).append(campiSep);
		}else{
			stringBuilder.append(Support.floatToString(getScortaMinOriginale())).append(campiSep);
		}
		stringBuilder.append(Support.floatToString(getScortaMaxOriginale())).append(campiSep);
		if(getStato().compareTo(2) != 0){
			stringBuilder.append(Support.floatToString(getScortaMaxRilevata())).append(campiSep);
		}else{
			stringBuilder.append(Support.floatToString(getScortaMaxOriginale())).append(campiSep);
		}
		stringBuilder.append(getCommento()).append(campiSep);
		stringBuilder.append(getStato()).append(campiSep);
		stringBuilder.append((new SimpleDateFormat("yyyyMMddHHmmss")).format(getTimestamp()));
		String ret = StringUtils.replace(stringBuilder.toString(), insideCampSep+campiSep, campiSep);
		return ret;
	}

	public String toStringPerFtpFileOLD(){
		String campiSep = "|";
		String insideCampSep = ",";

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(getCodice()).append(campiSep);
		for(int i = 0; i < getBarcodeOriginale().length; i++){
			stringBuilder.append(getBarcodeOriginale()[i].getCodice()).append(insideCampSep);
		}
		stringBuilder.append(campiSep);
		stringBuilder.append(getDescOriginale()).append(campiSep);
		stringBuilder.append(getUm()).append(campiSep);
		stringBuilder.append(Support.floatToString(getQtyOriginale())).append(campiSep);
		if(getStato().compareTo(2) != 0){
			stringBuilder.append(Support.floatToString(getQtyRilevata())).append(campiSep);
		}else{
			stringBuilder.append(Support.floatToString(getQtyOriginale())).append(campiSep);
		}
		stringBuilder.append(Support.floatToString(getQtyDifettOriginale())).append(campiSep);
		if(getStato().compareTo(2) != 0){
			stringBuilder.append(Support.floatToString(getQtyDifettRilevata())).append(campiSep);
		}else{
			stringBuilder.append(Support.floatToString(getQtyDifettOriginale())).append(campiSep);
		}
		stringBuilder.append(getDataCarico()).append(campiSep);
		stringBuilder.append(getDataScarico()).append(campiSep);
		stringBuilder.append(getDataUltimoInventario()).append(campiSep);
		stringBuilder.append(Support.floatToString(getScortaMinOriginale())).append(campiSep);
		if(getStato().compareTo(2) != 0){
			stringBuilder.append(Support.floatToString(getScortaMinRilevata())).append(campiSep);
		}else{
			stringBuilder.append(Support.floatToString(getScortaMinOriginale())).append(campiSep);
		}
		stringBuilder.append(Support.floatToString(getScortaMaxOriginale())).append(campiSep);
		if(getStato().compareTo(2) != 0){
			stringBuilder.append(Support.floatToString(getScortaMaxRilevata())).append(campiSep);
		}else{
			stringBuilder.append(Support.floatToString(getScortaMaxOriginale())).append(campiSep);
		}
		stringBuilder.append(getCommento()).append(campiSep);
		stringBuilder.append(getStato()).append(campiSep);
		stringBuilder.append((new SimpleDateFormat("yyyyMMddHHmmss")).format(getTimestamp()));
		String ret = StringUtils.replace(stringBuilder.toString(), insideCampSep+campiSep, campiSep);
		return ret;
	}

	private boolean statoDesc(){
		return StringUtils.equals(descOriginale, descRilevata);
	}

	private boolean statoPrezzo(){
		return (Float.compare(prezzoOriginale, prezzoRilevata) == 0);
	}

	private boolean statoBarcode(){
		// e' possibile solo aggiungere e non togliere barcode
		return (barcodeOriginale.length == barcodeRilevata.length);
	}

	private boolean statoQtyOriginale(){
		return (Float.compare(qtyOriginale, qtyRilevata) == 0);
	}

	private boolean statoQtyEsposte(){
		return (Float.compare(qtyEsposteOriginale, qtyEsposteRilevata) == 0);
	}

	private boolean statoQtyMagaz(){
		return (Float.compare(qtyMagazOriginale, qtyMagazRilevata) == 0);
	}

	private boolean statoQtyDifett(){
		return (Float.compare(qtyDifettOriginale, qtyDifettRilevata) == 0);
	}

	private boolean statoScortaMin(){
		return (Float.compare(scortaMinOriginale, scortaMinRilevata) == 0);
	}

	private boolean statoScortaMax(){
		return (Float.compare(scortaMaxOriginale, scortaMaxRilevata) == 0);
	}

	private boolean statoCommento(){
		return StringUtils.isEmpty(commento);
	}

	public void setStato(){
		if(statoDesc() && statoPrezzo() && statoBarcode() && statoQtyOriginale() && statoQtyEsposte() && statoQtyMagaz() && statoQtyDifett() && statoScortaMin() && statoScortaMax() && statoCommento()){
			setStato(TipoStato.INVENTARIATO_OK);
		}else{
			setStato(TipoStato.INVENTARIATO_DIFFER);
		}
	}

}
