package it.cascino.smarcamentomerce.model;

import java.io.Serializable;
import java.security.SecureRandom;

public class Barcode{
	private String codice;
	private String tipo;

	public Barcode(){
		codice = "";
		tipo = "";
	}

	public Barcode(String codice, String tipo){
		this.codice = codice;
		this.tipo = tipo;
	}

	public String getCodice(){
		return codice;
	}

	public void setCodice(String codice){
		this.codice = codice;
	}

	public String getTipo(){
		return tipo;
	}

	public void setTipo(String tipo){
		this.tipo = tipo;
	}

	@Override
	public String toString(){
		return "Barcode[codice=" + codice + ", tipo=" + tipo + "]";
	}
}
