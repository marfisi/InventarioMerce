package it.cascino.smarcamentomerce.model;

import org.apache.commons.lang3.StringUtils;

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

	public Barcode[] stringToArray(String bcode){
		bcode = StringUtils.trimToNull(bcode);
		if(bcode == null){
			return null;
		}
		String bcodeStrAr[] = StringUtils.split(bcode, " ");
		int n = bcodeStrAr.length;
		if(n == 0){
			return null;
		}
		Barcode bcodeAr[] = new Barcode[n];
		for(int i = 0; i < n; i++){
			bcodeAr[i] = new Barcode(bcodeStrAr[i], "");
		}
		return bcodeAr;
	}

	public String arrayToString(Barcode[] bcode){
		if(bcode == null){
			return null;
		}
		String bcodeStr = "";
		int n = bcode.length;
		if(n == 0){
			return null;
		}
		for(int i = 0; i < n; i++){
			bcodeStr = bcodeStr + " " + bcode[i].getCodice();
		}
		return StringUtils.trim(bcodeStr);
	}
}
