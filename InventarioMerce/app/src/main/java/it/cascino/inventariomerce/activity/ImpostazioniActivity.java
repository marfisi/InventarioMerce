package it.cascino.inventariomerce.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;
import java.util.Date;

import it.cascino.inventariomerce.R;
import it.cascino.inventariomerce.model.Articolo;
import it.cascino.inventariomerce.utils.DatiStatici;
import it.cascino.inventariomerce.utils.Support;
import it.cascino.inventariomerce.utils.TipoQty;
import it.cascino.inventariomerce.utils.TipoStato;

public class ImpostazioniActivity extends Activity{
	private CheckBox checkBoxAggiungiArtNoConferma;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_impostazioni);
		
		checkBoxAggiungiArtNoConferma = (CheckBox)findViewById(R.id.checkBoxAggiungiArtNoConferma);
		checkBoxAggiungiArtNoConferma.setChecked(DatiStatici.getInstance().getSelezionataCheckBoxAggiungiArtNoConferma());
		checkBoxAggiungiArtNoConferma.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,boolean isChecked){
				if (isChecked){
					DatiStatici.getInstance().setSelezionataCheckBoxAggiungiArtNoConferma(true);
				}else{
					DatiStatici.getInstance().setSelezionataCheckBoxAggiungiArtNoConferma(false);
				}
			}
		});
	}
}
