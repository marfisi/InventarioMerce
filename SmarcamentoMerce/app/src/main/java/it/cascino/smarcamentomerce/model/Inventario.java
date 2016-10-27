package it.cascino.smarcamentomerce.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Inventario{
	private boolean sorgenteFile;
	private String nomeFile;
	private String deposito;
	private String utenteCreatore;
	private String utenteDestinatario;
	private Integer progressivo;
	private Integer numeroArticoliTotale;
	private Integer numeroArticoliInventariati;
	private List<Articolo> articoliLs;
	private Timestamp timeCreazione;
	private Timestamp timeRicezione;
	private Timestamp timeModifica;
	private Timestamp timeInvio;
	private String stato;
	private String commento;

	private String campiSep = "|";
	private String insideCampSep = ",";
	private String decimalSep = ",";

	public Inventario(){
		this.sorgenteFile = true;
		this.nomeFile = null;
		this.deposito = null;
		utenteCreatore = null;
		utenteDestinatario = null;
		this.progressivo = null;
		this.numeroArticoliTotale = null;
		this.numeroArticoliInventariati = null;
		this.articoliLs = null; //new ArrayList<Articolo>();
		this.timeCreazione = null;
		this.timeRicezione = null;
		this.timeModifica = null;
		this.timeInvio = null;
		stato = null;
		commento = null;
	}

	public Inventario(boolean sorgenteFile, String nomeFile, String deposito, String utenteCreatore, String utenteDestinatario, Integer progressivo, Integer numeroArticoliTotale, Integer numeroArticoliInvetariati, List<Articolo> articoliLs, Timestamp timeCreazione, Timestamp timeRicezione, Timestamp timeModifica, Timestamp timeInvio, String stato, String commento){
		this.sorgenteFile = sorgenteFile;
		this.nomeFile = nomeFile;
		this.deposito = deposito;
		this.utenteCreatore = utenteCreatore;
		this.utenteDestinatario = utenteDestinatario;
		this.progressivo = progressivo;
		this.numeroArticoliTotale = numeroArticoliTotale;
		this.numeroArticoliInventariati = numeroArticoliInventariati;
		this.articoliLs = articoliLs;
		this.timeCreazione = timeCreazione;
		this.timeRicezione = timeRicezione;
		this.timeModifica = timeModifica;
		this.timeInvio = timeInvio;
		this.stato = stato;
		this.commento = commento;
	}

	public boolean getSorgenteFile(){
		return sorgenteFile;
	}

	public void setSorgenteFile(boolean sorgenteFile){
		this.sorgenteFile = sorgenteFile;
	}

	public String getNomeFile(){
		return nomeFile;
	}

	public void setNomeFile(String nomeFile){
		this.nomeFile = nomeFile;
	}

	public String getDeposito(){
		return deposito;
	}

	public void setDeposito(String deposito){
		this.deposito = deposito;
	}

	public String getUtenteCreatore(){
		return utenteCreatore;
	}

	public void setUtenteCreatore(String utenteCreatore){
		this.utenteCreatore = utenteCreatore;
	}

	public String getUtenteDestinatario(){
		return utenteDestinatario;
	}

	public void setUtenteDestinatario(String utenteDestinatario){
		this.utenteDestinatario = utenteDestinatario;
	}

	public Integer getProgressivo(){
		return progressivo;
	}

	public void setProgressivo(Integer progressivo){
		this.progressivo = progressivo;
	}

	public Integer getNumeroArticoliTotale(){
		return numeroArticoliTotale;
	}

	public void setNumeroArticoliTotale(Integer numeroArticoliTotale){
		this.numeroArticoliTotale = numeroArticoliTotale;
	}

	public Integer getNumeroArticoliInventariati(){
		return numeroArticoliInventariati;
	}

	public void setNumeroArticoliInventariati(Integer numeroArticoliInventariati){
		this.numeroArticoliInventariati = numeroArticoliInventariati;
	}

	public List<Articolo> getArticoliLs(){
		return articoliLs;
	}

	public void setArticoliLs(List<Articolo> articoliLs){
		this.articoliLs = articoliLs;
	}

	public Timestamp getTimeCreazione(){
		return timeCreazione;
	}

	public void setTimeCreazione(Timestamp timeCreazione){
		this.timeCreazione = timeCreazione;
	}

	public Timestamp getTimeRicezione(){
		return timeRicezione;
	}

	public void setTimeRicezione(Timestamp timeRicezione){
		this.timeRicezione = timeRicezione;
	}

	public Timestamp getTimeModifica(){
		return timeModifica;
	}

	public void setTimeModifica(Timestamp timeModifica){
		this.timeModifica = timeModifica;
	}

	public Timestamp getTimeInvio(){
		return timeInvio;
	}

	public void setTimeInvio(Timestamp timeInvio){
		this.timeInvio = timeInvio;
	}

	public String getStato(){
		return stato;
	}

	public void setStato(String stato){
		/*
		vuoto/null - da controllare (come 2)
		0 - controllato ok
		1 - controllato non quadrato
		2 - da controllare
		 */
		this.stato = stato;
	}

	public String getCommento(){
		return commento;
	}

	public void setCommento(String commento){
		this.commento = commento;
	}

	public String getCampiSep(){
		return campiSep;
	}

	public void setCampiSep(String campiSep){
		this.campiSep = campiSep;
	}

	public String getInsideCampSep(){
		return insideCampSep;
	}

	public void setInsideCampSep(String insideCampSep){
		this.insideCampSep = insideCampSep;
	}

	public String getDecimalSep(){
		return decimalSep;
	}

	public void setDecimalSep(String decimalSep){
		this.decimalSep = decimalSep;
	}

}
