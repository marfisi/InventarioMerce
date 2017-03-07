package it.cascino.inventariomerce.utils;

public abstract class TipoStato{
	public static final int INVENTARIATO_OK = 0;
	public static final int DA_INVENTARIARE = 9;
	public static final int INVENTARIATO_DIFFER = 1;
	public static final int INVENTARIATO_DIFFER_MOLTO = 2;

	public static String getStatoDescrizione(int s){
		String r = "";
		switch(s){
			case INVENTARIATO_OK:
				r = "Inventariato Quadrato";
				break;
			case DA_INVENTARIARE:
				r = "Da Inventariare";
				break;
			case INVENTARIATO_DIFFER:
				r = "Inventariato NON Quadrato";
				break;
			case INVENTARIATO_DIFFER_MOLTO:
				r = "Inventariato NON MOLTO Quadrato";
				break;
			default:
				r = "Stato non consentito";
				break;
		}
		return r;
	}
}