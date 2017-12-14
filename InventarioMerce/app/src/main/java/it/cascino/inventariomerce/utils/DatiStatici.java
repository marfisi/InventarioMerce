package it.cascino.inventariomerce.utils;

public class DatiStatici{
	private static DatiStatici instance;

	private Integer tipoPreselezione = null;
	
	private Boolean selezionataCheckBoxAggiungiArtNoConferma;

	protected DatiStatici(){
	}

	public static DatiStatici getInstance(){
		if (instance == null) {
			instance = new DatiStatici();
		}
		return instance;
	}

	public Integer getTipoPreselezione(){
		return tipoPreselezione;
	}

	public void setTipoPreselezione(Integer tipoPreselezione){
		this.tipoPreselezione = tipoPreselezione;
	}
	
	public Boolean getSelezionataCheckBoxAggiungiArtNoConferma(){
		return selezionataCheckBoxAggiungiArtNoConferma;
	}
	
	public void setSelezionataCheckBoxAggiungiArtNoConferma(Boolean selezionataCheckBoxAggiungiArtNoConferma){
		this.selezionataCheckBoxAggiungiArtNoConferma = selezionataCheckBoxAggiungiArtNoConferma;
	}
}
