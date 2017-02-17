package it.cascino.inventariomerce.utils;

public class ScambioDatiIntent{
	private static ScambioDatiIntent instance;
	private String data = null;

	protected ScambioDatiIntent(){
	}

	public static ScambioDatiIntent getInstance(){
		if (instance == null) {
			instance = new ScambioDatiIntent();
		}
		return instance;
	}

	public String getData(){
		return data;
	}

	public void setData(String data){
		this.data = data;
	}
}
