package it.cascino.smarcamentomerce.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class FileDaAs{
	private String nomeFile;
	private String utente;
	private String deposito;
	private Integer progressivo;
	private Integer numeroArticoli;
	private List<Articolo> articoliLs;
	private Timestamp timeRicezione;
	private Timestamp timeModifica;
	private Timestamp timeInvio;

	private String campiSep = "|";
	private String insideCampSep = ",";
	private String decimalSep = ",";

	public FileDaAs(){
		this.nomeFile = "";
		this.utente = "";
		this.deposito = "";
		this.progressivo = 0;
		this.numeroArticoli = -999;
		this.articoliLs = new ArrayList<Articolo>();
		this.timeRicezione = null;
		this.timeModifica = null;
		this.timeInvio = null;
	}

	public FileDaAs(String nomeFile, String utente, String deposito, Integer progressivo, Integer numeroArticoli, List<Articolo> articoliLs, Timestamp timeRicezione, Timestamp timeModifica, Timestamp timeInvio){
		this.nomeFile = nomeFile;
		this.utente = utente;
		this.deposito = deposito;
		this.progressivo = progressivo;
		this.numeroArticoli = numeroArticoli;
		this.articoliLs = articoliLs;
		this.timeRicezione = timeRicezione;
		this.timeModifica = timeModifica;
		this.timeInvio = timeInvio;
	}

	public String getNomeFile(){
		return nomeFile;
	}

	public void setNomeFile(String nomeFile){
		this.nomeFile = nomeFile;
	}

	public String getDecimalSep(){
		return decimalSep;
	}

	public void setDecimalSep(String decimalSep){
		this.decimalSep = decimalSep;
	}

	public String getUtente(){
		return utente;
	}

	public void setUtente(String utente){
		this.utente = utente;
	}

	public String getDeposito(){
		return deposito;
	}

	public void setDeposito(String deposito){
		this.deposito = deposito;
	}

	public Integer getProgressivo(){
		return progressivo;
	}

	public void setProgressivo(Integer progressivo){
		this.progressivo = progressivo;
	}

	public Integer getNumeroArticoli(){
		return numeroArticoli;
	}

	public void setNumeroArticoli(Integer numeroArticoli){
		this.numeroArticoli = numeroArticoli;
	}

	public List<Articolo> getArticoliLs(){
		return articoliLs;
	}

	public void setArticoliLs(List<Articolo> articoliLs){
		this.articoliLs = articoliLs;
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
}
