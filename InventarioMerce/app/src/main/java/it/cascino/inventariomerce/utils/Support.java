package it.cascino.inventariomerce.utils;

import android.graphics.Color;

import org.apache.commons.lang3.StringUtils;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import it.cascino.inventariomerce.R;

public class Support{
	private static Support support;
	private static String decimalSep;
	private static DecimalFormatSymbols symbols;
	private static DecimalFormat decimalFormat;
	
	public Support(){
		decimalSep = ",";
		symbols = new DecimalFormatSymbols(Locale.ITALY);
		symbols.setDecimalSeparator(decimalSep.charAt(0));
		decimalFormat = new DecimalFormat("0.00", symbols);
		decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
	}
	
	public static String floatToString(Float f){
		if(f == null){
			return "";
		}
		if(support == null){
			support = new Support();
		}

		String fToStr = decimalFormat.format(f);
		return fToStr;
	}

	public static int definisciImg(String valAtt, String valRil){
		if(StringUtils.equals(valRil, "n.r.")){
			return Color.TRANSPARENT;
		}
		if(StringUtils.equals(valAtt, valRil)){
			return R.drawable.check;
		}
		return R.drawable.warn;
	}
}
