package it.cascino.inventariomerce.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
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
import it.cascino.inventariomerce.model.Inventario;
import it.cascino.inventariomerce.utils.Support;
import it.cascino.inventariomerce.utils.TipoQty;
import it.cascino.inventariomerce.utils.TipoStato;

public class ModificaArticoloActivity extends Activity{
	private Articolo a = null;
	private TextView qtyAttese;
	private TextView qtyRilevate;
	private ImageView qtyImg;
	// private TextView qtyEsposteAttese;
	private TextView qtyEsposteRilevate;
	// private TextView qtyMagazzinoAttese;
	private TextView qtyMagazzinoRilevate;
	private TextView qtyDifettoseAttese;
	// private TextView qtyDifettoseRilevate;
	private TextView qtyScortaMinAttese;
	private TextView qtyScortaMinRilevate;
	private ImageView qtyScortaMinImg;
	private TextView qtyScortaMaxAttese;
	private TextView qtyScortaMaxRilevate;
	private ImageView qtyScortaMaxImg;
	private TextView qtyPerConfAttese;
	private TextView qtyPerConfRilevate;
	private ImageView qtyPerConfImg;
	private EditText qty;
	private QtyTextWatcher qtyTextWatcher;

	private Handler repeatUpdateHandler = new Handler();
	private boolean mAutoIncrement = false;
	private boolean mAutoDecrement = false;
	private static Integer REP_DELAY = 50;

	int tipoQtySelezionata = -999;

	private Inventario inventario;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modificaarticolo);

		TextView art_codart = (TextView)findViewById(R.id.art_codart);

		TextView art_desc = (TextView)findViewById(R.id.art_desc);
		ImageView art_descImg = (ImageView)findViewById(R.id.art_descImg);
		ImageButton btnResetDesc = (ImageButton)findViewById(R.id.btnResetDesc);
		ImageButton btnModifDesc = (ImageButton)findViewById(R.id.btnModifDesc);

		TextView art_prezzo = (TextView)findViewById(R.id.art_prez);
		ImageView art_prezzoImg = (ImageView)findViewById(R.id.art_prezImg);
		ImageButton btnResetPrezzo = (ImageButton)findViewById(R.id.btnResetPrezzo);
		ImageButton btnModifPrezzo = (ImageButton)findViewById(R.id.btnModifPrezzo);

		TextView art_dataCar = (TextView)findViewById(R.id.art_dataCar);
		TextView art_dataScar = (TextView)findViewById(R.id.art_dataScar);
		TextView art_dataUltimoInvent = (TextView)findViewById(R.id.art_dataUltimoInvent);

		TextView art_bcod = (TextView)findViewById(R.id.art_bcod);
		ImageView art_bcodImg = (ImageView)findViewById(R.id.art_bcodImg);
		ImageButton btnResetBcod = (ImageButton)findViewById(R.id.btnResetBcod);
		ImageButton btnModifBcod = (ImageButton)findViewById(R.id.btnModifBcod);

		TextView art_commento = (TextView)findViewById(R.id.art_commento);
		ImageView art_commentoImg = (ImageView)findViewById(R.id.art_commentoImg);
		ImageButton btnResetCommento = (ImageButton)findViewById(R.id.btnResetCommento);
		ImageButton btnModifCommento = (ImageButton)findViewById(R.id.btnModifCommento);

		TextView art_um_qty = (TextView)findViewById(R.id.art_um_qty);

		qtyAttese = (TextView)findViewById(R.id.qtyAttese);
		qtyRilevate = (TextView)findViewById(R.id.qtyRilevate);
		// qtyRilevate senza listener dato che non posso modificarlo direttamente
		qtyImg = (ImageView)findViewById(R.id.qtyImg);

		// qtyEsposteAttese = (TextView)findViewById(R.id.qtyEsposteAttese);
		qtyEsposteRilevate = (TextView)findViewById(R.id.qtyEsposteRilevate);
		qtyEsposteRilevate.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				tipoQtySelezionata = TipoQty.ESPOSTE;
				gestioneQty(0f);
			}
		});
		// qtyMagazzinoAttese = (TextView)findViewById(R.id.qtyMagazzinoAttese);
		qtyMagazzinoRilevate = (TextView)findViewById(R.id.qtyMagazzinoRilevate);
		qtyMagazzinoRilevate.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				tipoQtySelezionata = TipoQty.MAGAZZINO;
				gestioneQty(0f);
			}
		});
		qtyDifettoseAttese = (TextView)findViewById(R.id.qtyDifettoseAttese);
		/* qtyDifettoseRilevate = (TextView)findViewById(R.id.qtyDifettoseRilevate);
		qtyDifettoseRilevate.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				tipoQtySelezionata = TipoQty.DIFETTOSI;
				gestioneQty(0f);
			}
		}); */
		qtyScortaMinAttese = (TextView)findViewById(R.id.qtyScortaMinAttese);
		qtyScortaMinRilevate = (TextView)findViewById(R.id.qtyScortaMinRilevate);
		qtyScortaMinRilevate.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				tipoQtySelezionata = TipoQty.SCORTE_MIN;
				gestioneQty(0f);
			}
		});
		qtyScortaMinImg = (ImageView)findViewById(R.id.qtyScortaMinImg);

		qtyScortaMaxAttese = (TextView)findViewById(R.id.qtyScortaMaxAttese);
		qtyScortaMaxRilevate = (TextView)findViewById(R.id.qtyScortaMaxRilevate);
		qtyScortaMaxRilevate.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				tipoQtySelezionata = TipoQty.SCORTE_MAX;
				gestioneQty(0f);
			}
		});
		qtyScortaMaxImg = (ImageView)findViewById(R.id.qtyScortaMaxImg);

		qtyPerConfAttese = (TextView)findViewById(R.id.qtyPerConfAttese);
		qtyPerConfRilevate = (TextView)findViewById(R.id.qtyPerConfRilevate);
		qtyPerConfRilevate.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				tipoQtySelezionata = TipoQty.PER_CONFEZIONE;
				gestioneQty(0f);
			}
		});
		qtyPerConfImg = (ImageView)findViewById(R.id.qtyPerConfImg);

		ImageButton btnIncrementaQty = (ImageButton)findViewById(R.id.btnIncrementaQty);
		btnIncrementaQty.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				gestioneQty(1.0f);
			}
		});
		btnIncrementaQty.setOnLongClickListener(new View.OnLongClickListener(){
			@Override
			public boolean onLongClick(View v){
				mAutoIncrement = true;
				repeatUpdateHandler.post(new RptUpdater(1.0f));
				return false;
			}
		});
		btnIncrementaQty.setOnTouchListener(new View.OnTouchListener(){
			public boolean onTouch(View v, MotionEvent event){
				if((event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) && mAutoIncrement){
					mAutoIncrement = false;
				}
				return false;
			}
		});
		ImageButton btnDecrementaQty = (ImageButton)findViewById(R.id.btnDecrementaQty);
		btnDecrementaQty.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				gestioneQty(-1.0f);
			}
		});
		btnDecrementaQty.setOnLongClickListener(new View.OnLongClickListener(){
			@Override
			public boolean onLongClick(View v){
				mAutoDecrement = true;
				repeatUpdateHandler.post(new RptUpdater(-1.0f));
				return false;
			}
		});
		btnDecrementaQty.setOnTouchListener(new View.OnTouchListener(){
			public boolean onTouch(View v, MotionEvent event){
				if((event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) && mAutoDecrement){
					mAutoDecrement = false;
				}
				return false;
			}
		});
		qty = (EditText)findViewById(R.id.qty);
		qtyTextWatcher = new QtyTextWatcher();
		qty.addTextChangedListener(qtyTextWatcher);

		ImageButton btnKeypad = (ImageButton)findViewById(R.id.btnKeypad);
		TextView um_qty = (TextView)findViewById(R.id.um_qty);

		Intent intentLoginFileActivity = getIntent();
		if(intentLoginFileActivity != null){
			String gSonString = intentLoginFileActivity.getStringExtra("articolo");
			Gson gSon = new Gson();
			a = gSon.fromJson(gSonString, Articolo.class);
			gSonString = intentLoginFileActivity.getStringExtra("inventario");
			inventario = gSon.fromJson(gSonString, Inventario.class);
		}

		ImageButton btnCheck = (ImageButton)findViewById(R.id.btnCheck);
		btnCheck.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				if(a.getStato() == TipoStato.DA_INVENTARIARE){
					inventario.setNumeroArticoliInventariati(inventario.getNumeroArticoliInventariati() + 1);
				}
				a.setStato();
				a.setTimestamp(new Timestamp((new Date().getTime())));
				Gson gSon = new Gson();
				Intent resultIntent = new Intent();
				String gSonString = gSon.toJson(a);
				resultIntent.putExtra("articolo", gSonString);
				gSonString = gSon.toJson(inventario);
				resultIntent.putExtra("inventario", gSonString);
				setResult(Activity.RESULT_OK, resultIntent);
				finish();
			}
		});
		ImageButton btnReset = (ImageButton)findViewById(R.id.btnReset);
		btnReset.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				Intent resultIntent = new Intent();
				setResult(Activity.RESULT_CANCELED, resultIntent);
				finish();
			}
		});

		String valAtt;
		String valRil;

		art_codart.setText(a.getCodice());

		valAtt = a.getDescOriginale() != null ? a.getDescOriginale() : "n.d.";
		valRil = a.getDescRilevata() != null ? a.getDescRilevata() : "n.r.";
		art_descImg.setBackgroundResource(Support.definisciImg(valAtt, valRil));
		valRil = a.getDescRilevata() != null ? a.getDescRilevata() : a.getDescOriginale();
		art_desc.setText(valRil);

		valAtt = a.getPrezzoOriginale() != null ? Support.floatToString(a.getPrezzoOriginale()) : "n.d.";
		valRil = a.getPrezzoRilevata() != null ? Support.floatToString(a.getPrezzoRilevata()) : "n.r.";
		art_prezzoImg.setBackgroundResource(Support.definisciImg(valAtt, valRil));
		valRil = a.getPrezzoRilevata() != null ? Support.floatToString(a.getPrezzoRilevata()) : Support.floatToString(a.getPrezzoOriginale());
		art_prezzo.setText(valRil);

		art_dataCar.setText(a.getDataCarico());
		art_dataScar.setText(a.getDataScarico());
		art_dataUltimoInvent.setText(a.getDataUltimoInventario());

		String bcod = "";
		if(a.getBarcodeRilevata() != null){
			for(int i = 0, n = a.getBarcodeRilevata().length; i < n; i++){
				bcod = bcod + "-" + a.getBarcodeRilevata()[i].getCodice();
			}
		}
		art_bcod.setText(StringUtils.removeStart(bcod, "-"));
		valAtt = a.getBarcodeOriginale() != null ? Integer.toString(a.getBarcodeOriginale().length) : "0";
		valRil = a.getBarcodeRilevata() != null ? Integer.toString(a.getBarcodeRilevata().length) : "0";
		art_bcodImg.setBackgroundResource(Support.definisciImg(valAtt, valRil));
		if(valAtt.length() == valRil.length()){
			art_bcodImg.setBackgroundResource(Support.definisciImg(valAtt, "n.r."));
		}

		valAtt = "n.r.";
		valRil = a.getCommento() != null ? a.getCommento() : "n.r.";
		art_commentoImg.setBackgroundResource(Support.definisciImg(valAtt, valRil));
		valRil = a.getCommento() != null ? a.getCommento() : "";
		art_commento.setText(valRil);

		art_um_qty.setText(a.getUm() + ")");

		valAtt = a.getQtyOriginale() != null ? Support.floatToString(a.getQtyOriginale()) : "n.d.";
		qtyAttese.setText(valAtt);
		valRil = a.getQtyRilevata() != null ? Support.floatToString(a.getQtyRilevata()) : "n.r.";
		qtyRilevate.setText(valRil);
		qtyImg.setBackgroundResource(Support.definisciImg(valAtt, valRil));


		qtyEsposteRilevate.setText(a.getQtyEsposteRilevata() != null ? Support.floatToString(a.getQtyEsposteRilevata()) : "n.r.");

		qtyMagazzinoRilevate.setText(a.getQtyMagazRilevata() != null ? Support.floatToString(a.getQtyMagazRilevata()) : "n.r.");

		qtyDifettoseAttese.setText(a.getQtyDifettOriginale() != null ? Support.floatToString(a.getQtyDifettOriginale()) : "n.d.");

		valAtt = a.getScortaMinOriginale() != null ? Support.floatToString(a.getScortaMinOriginale()) : "n.d.";
		qtyScortaMinAttese.setText(valAtt);
		valRil = a.getScortaMinRilevata() != null ? Support.floatToString(a.getScortaMinRilevata()) : "n.r.";
		qtyScortaMinRilevate.setText(valRil);
		qtyScortaMinImg.setBackgroundResource(Support.definisciImg(valAtt, valRil));

		valAtt = a.getScortaMaxOriginale() != null ? Support.floatToString(a.getScortaMaxOriginale()) : "n.d.";
		qtyScortaMaxAttese.setText(valAtt);
		valRil = a.getScortaMaxRilevata() != null ? Support.floatToString(a.getScortaMaxRilevata()) : "n.r.";
		qtyScortaMaxRilevate.setText(valRil);
		qtyScortaMaxImg.setBackgroundResource(Support.definisciImg(valAtt, valRil));

		valAtt = a.getQtyPerConfezOriginale() != null ? Support.floatToString(a.getQtyPerConfezOriginale()) : "n.d.";
		qtyPerConfAttese.setText(valAtt);
		valRil = a.getQtyPerConfezRilevata() != null ? Support.floatToString(a.getQtyPerConfezRilevata()) : "n.r.";
		qtyPerConfRilevate.setText(valRil);
		qtyPerConfImg.setBackgroundResource(Support.definisciImg(valAtt, valRil));

		um_qty.setText(a.getUm());
	}

	private void gestioneQty(Float qtyStep){
		gestioneQty(qtyStep, false);
	}

	private void gestioneQty(Float qtyStep, boolean assoluto){
		String txtElab = "";
		Float qtyElab = -999.9f;
		switch(tipoQtySelezionata){
			case TipoQty.ESP_E_MAG:
				break;
			case TipoQty.ESPOSTE:
				txtElab = qtyEsposteRilevate.getText().toString();
				if(StringUtils.equals(txtElab, "n.r.")){
					txtElab = "0";
				}
				txtElab = StringUtils.replace(txtElab, ",", ".");
				qtyElab = Float.parseFloat(txtElab);
				qtyElab = qtyElab + qtyStep;
				if(assoluto){
					qtyElab = qtyStep;
				}
				qtyEsposteRilevate.setText(Support.floatToString(qtyElab));
				a.setQtyEsposteRilevata(qtyElab);
				// aggiorno pure il totale
				gestioneQtyTotDaEsposteMagazzino();
				break;
			case TipoQty.MAGAZZINO:
				txtElab = qtyMagazzinoRilevate.getText().toString();
				if(StringUtils.equals(txtElab, "n.r.")){
					txtElab = "0";
				}
				txtElab = StringUtils.replace(txtElab, ",", ".");
				qtyElab = Float.parseFloat(txtElab);
				qtyElab = qtyElab + qtyStep;
				if(assoluto){
					qtyElab = qtyStep;
				}
				qtyMagazzinoRilevate.setText(Support.floatToString(qtyElab));
				a.setQtyMagazRilevata(qtyElab);
				// aggiorno pure il totale
				gestioneQtyTotDaEsposteMagazzino();
				break;
			/*case TipoQty.DIFETTOSI:
				txtElab = qtyDifettoseRilevate.getText().toString();
				if(StringUtils.equals(txtElab, "n.r.")){
					txtElab = qtyDifettoseAttese.getText().toString();
					if(StringUtils.equals(txtElab, "n.d.")){
						txtElab = "-99";
					}
				}
				txtElab = StringUtils.replace(txtElab, ",", ".");
				qtyElab = Float.parseFloat(txtElab);
				qtyElab = qtyElab + qtyStep;
				if(assoluto){
					qtyElab = qtyStep;
				}
				qtyDifettoseRilevate.setText(Support.floatToString(qtyElab));
				a.setQtyDifettRilevata(qtyElab);
				break;*/
			case TipoQty.SCORTE_MIN:
				txtElab = qtyScortaMinRilevate.getText().toString();
				if(StringUtils.equals(txtElab, "n.r.")){
					txtElab = qtyScortaMinAttese.getText().toString();
					if(StringUtils.equals(txtElab, "n.d.")){
						txtElab = "-99";
					}
				}
				txtElab = StringUtils.replace(txtElab, ",", ".");
				qtyElab = Float.parseFloat(txtElab);
				qtyElab = qtyElab + qtyStep;
				if(assoluto){
					qtyElab = qtyStep;
				}
				qtyScortaMinRilevate.setText(Support.floatToString(qtyElab));
				a.setScortaMinRilevata(qtyElab);
				qtyScortaMinImg.setBackgroundResource(Support.definisciImg(qtyScortaMinAttese.getText().toString(), qtyScortaMinRilevate.getText().toString()));
				break;
			case TipoQty.SCORTE_MAX:
				txtElab = qtyScortaMaxRilevate.getText().toString();
				if(StringUtils.equals(txtElab, "n.r.")){
					txtElab = qtyScortaMaxAttese.getText().toString();
					if(StringUtils.equals(txtElab, "n.d.")){
						txtElab = "-99";
					}
				}
				txtElab = StringUtils.replace(txtElab, ",", ".");
				qtyElab = Float.parseFloat(txtElab);
				qtyElab = qtyElab + qtyStep;
				if(assoluto){
					qtyElab = qtyStep;
				}
				qtyScortaMaxRilevate.setText(Support.floatToString(qtyElab));
				a.setScortaMaxRilevata(qtyElab);
				qtyScortaMaxImg.setBackgroundResource(Support.definisciImg(qtyScortaMaxAttese.getText().toString(), qtyScortaMaxRilevate.getText().toString()));
				break;
			case TipoQty.PER_CONFEZIONE:
				txtElab = qtyPerConfRilevate.getText().toString();
				if(StringUtils.equals(txtElab, "n.r.")){
					txtElab = qtyPerConfAttese.getText().toString();
					if(StringUtils.equals(txtElab, "n.d.")){
						txtElab = "-99";
					}
				}
				txtElab = StringUtils.replace(txtElab, ",", ".");
				qtyElab = Float.parseFloat(txtElab);
				qtyElab = qtyElab + qtyStep;
				if(assoluto){
					qtyElab = qtyStep;
				}
				qtyPerConfRilevate.setText(Support.floatToString(qtyElab));
				a.setQtyPerConfezRilevata(qtyElab);
				qtyPerConfImg.setBackgroundResource(Support.definisciImg(qtyPerConfAttese.getText().toString(), qtyPerConfRilevate.getText().toString()));
				break;
			default:
				break;
		}
		qtyTextWatcher.setActive(false);
		qty.setText(Support.floatToString(qtyElab));
		qtyTextWatcher.setActive(true);

		gestioneQtyStyle();
	}

	private void gestioneQtyStyle(){
		qtyEsposteRilevate.setBackgroundResource(R.drawable.drawable_qty_selezionabile);
		qtyMagazzinoRilevate.setBackgroundResource(R.drawable.drawable_qty_selezionabile);
		qtyScortaMinRilevate.setBackgroundResource(R.drawable.drawable_qty_selezionabile);
		qtyScortaMaxRilevate.setBackgroundResource(R.drawable.drawable_qty_selezionabile);
		qtyPerConfRilevate.setBackgroundResource(R.drawable.drawable_qty_selezionabile);
		switch(tipoQtySelezionata){
			case TipoQty.ESP_E_MAG:
				break;
			case TipoQty.ESPOSTE:
				qtyEsposteRilevate.setBackgroundResource(R.drawable.drawable_qty_selezionato);
				break;
			case TipoQty.MAGAZZINO:
				qtyMagazzinoRilevate.setBackgroundResource(R.drawable.drawable_qty_selezionato);
				break;
			/*case TipoQty.DIFETTOSI:
				break;*/
			case TipoQty.SCORTE_MIN:
				qtyScortaMinRilevate.setBackgroundResource(R.drawable.drawable_qty_selezionato);
				break;
			case TipoQty.SCORTE_MAX:
				qtyScortaMaxRilevate.setBackgroundResource(R.drawable.drawable_qty_selezionato);
				break;
			case TipoQty.PER_CONFEZIONE:
				qtyPerConfRilevate.setBackgroundResource(R.drawable.drawable_qty_selezionato);
				break;
			default:
				break;
		}
	}

	private void gestioneQtyTotDaEsposteMagazzino(){
		Float qtyElab12;
		String txtElab1 = qtyEsposteRilevate.getText().toString();
		String txtElab2 = qtyMagazzinoRilevate.getText().toString();
		if(StringUtils.equals(txtElab1, "n.r.") && StringUtils.equals(txtElab2, "n.r.")){
			qtyRilevate.setText("n.r.");
		}
		if(StringUtils.equals(txtElab1, "n.r.")){
			txtElab1 = "0";
		}
		if(StringUtils.equals(txtElab2, "n.r.")){
			txtElab2 = "0";
		}
		txtElab1 = StringUtils.replace(txtElab1, ",", ".");
		txtElab2 = StringUtils.replace(txtElab2, ",", ".");
		qtyElab12 = Float.parseFloat(txtElab1) + Float.parseFloat(txtElab2) + a.getQtyDifettOriginale();	// aggiungo pure il difettoso
		qtyRilevate.setText(Support.floatToString(qtyElab12));
		a.setQtyRilevata(qtyElab12);
		qtyImg.setBackgroundResource(Support.definisciImg(qtyAttese.getText().toString(), qtyRilevate.getText().toString()));
	}

	private class RptUpdater implements Runnable{
		Float qtyStep;

		public RptUpdater(Float qtyStep){
			this.qtyStep = qtyStep;
		}

		public void run(){
			if(mAutoIncrement){
				gestioneQty(qtyStep);
				repeatUpdateHandler.postDelayed(new RptUpdater(qtyStep), REP_DELAY);
			}else if(mAutoDecrement){
				gestioneQty(qtyStep);
				repeatUpdateHandler.postDelayed(new RptUpdater(qtyStep), REP_DELAY);
			}
		}
	}

	private class QtyTextWatcher implements TextWatcher{
		private boolean mActive;

		void setActive(boolean active){
			mActive = active;
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after){
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count){
		}

		@Override
		public void afterTextChanged(Editable s){
			if(mActive){
				try{
					Float fLetto = Float.parseFloat(StringUtils.replace(s.toString(), ",", "."));
					gestioneQty(fLetto, true);
				}catch(NumberFormatException e){
					gestioneQty(-999.9f, true);
				}
			}
		}
	}
}
