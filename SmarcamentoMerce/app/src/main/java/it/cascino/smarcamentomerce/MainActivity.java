package it.cascino.smarcamentomerce;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import it.cascino.smarcamentomerce.activity.LoginFileActivity;
import it.cascino.smarcamentomerce.adapter.ArticoloAdapter;
import it.cascino.smarcamentomerce.model.Barcode;

import it.cascino.smarcamentomerce.model.Articolo;
import it.cascino.smarcamentomerce.model.FileDaAs;

public class MainActivity extends Activity{
	private List<Articolo> articoliLs = new ArrayList<Articolo>();
	private ArticoloAdapter adapterArticoliLs;

	private Integer numeroInvetariare = 0;

	private String SHARED_PREF = "shared_pref_inventario";

	static final int PICK_CONTACT_REQUEST = 1;

	private TextView testoNumeroInventariati;
	private TextView testoNumeroInvetariare;

	private Button saveButton;

	private FileDaAs fileDaAs;

	private Boolean keyboardVisible;
	// private InputMethodManager inputMethodManager;

	private EditText myFilter;

	private String stringaDaCercare = null;
	private void setStringaDaCercare(String stringaDaCercare){
		this.stringaDaCercare = stringaDaCercare;
	}

	private final int TRIGGER_SEARCH = 1;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == TRIGGER_SEARCH) {
				//triggerSearch();
				adapterArticoliLs.getFilter().filter(stringaDaCercare);
				Log.i("Filtro", "handleMessage");
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

		TextView dataSync = (TextView)findViewById(R.id.dataSync);
		DateFormat formatter = new SimpleDateFormat("HH:mm.ss - dd/MM/yyyy");
		String dataSyncStr = formatter.format(new Date());
		dataSync.setText(dataSyncStr);

		testoNumeroInventariati = (TextView)findViewById(R.id.numeroInvetariati);
		testoNumeroInventariati.setText("0");
		testoNumeroInvetariare = (TextView)findViewById(R.id.numeroInvetariare);
		testoNumeroInvetariare.setText("0");

		saveButton = (Button)findViewById(R.id.saveButton);

		Button syncButton = (Button)findViewById(R.id.syncButton);
		syncButton.setOnClickListener(new View.OnClickListener(){
			/*@Override
			public void onClick(View v){
				DownloadThread dt = new DownloadThread();
				try{
					dt.execute("").get();
				}catch(InterruptedException e){
					e.printStackTrace();
				}catch(ExecutionException e){
					e.printStackTrace();
				}
				/*synchronized(articoliLs){
					numeroInvetariare.setText(articoliLs.size());
				}/
				//dt.cancel(true);

				testoNumeroInvetariare.setText(numeroInvetariare.toString());
				//testoNumeroInvetariatiRimanenti.setText(numeroInvetariare.toString());
			}*/
			@Override
			public void onClick(View v){
				Intent intentLoginFileActivity = new Intent(getApplicationContext(), LoginFileActivity.class);
				intentLoginFileActivity.putExtra("nomeParametro", "nomeParametroContenuto");
				Integer resultCode = 0;
				startActivityForResult(intentLoginFileActivity, PICK_CONTACT_REQUEST); //resultCode);
				Log.i("resultCode", String.valueOf(resultCode));
			}
		});

		saveButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				saveButton.setVisibility(View.INVISIBLE);
				Gson gSon = new Gson();
				String gSonString = gSon.toJson(articoliLs);
				Intent intentLoginFileActivity = new Intent(getApplicationContext(), LoginFileActivity.class);
				intentLoginFileActivity.putExtra("articoliLs", gSonString);
				intentLoginFileActivity.putExtra("nomeFile", fileDaAs.getNomeFile());
				Integer resultCode = 0;
				startActivityForResult(intentLoginFileActivity, PICK_CONTACT_REQUEST); //resultCode);
				Log.i("saveButton", saveButton.toString());
				articoliLs.clear();
				numeroInvetariare = articoliLs.size();
				testoNumeroInvetariare.setText(String.valueOf(numeroInvetariare));
				adapterArticoliLs.updateArticoliLs(articoliLs);
				adapterArticoliLs.setNumeroInvetariare(0);
				testoNumeroInventariati.setText("0");
			}
		});

		// ArrayAdapter<String> adapterArticoliLs = new ArrayAdapter<String>(this, R.layout.rowarticolo, articoliLs);
		ListView listViewArticoliLs = (ListView)findViewById(R.id.articoliList);
		listViewArticoliLs.setOnItemClickListener(new AdapterView.OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id){
				Articolo articoloSel = articoliLs.get(position);
				Toast.makeText(getApplicationContext(), "Sel: " + articoloSel.getCodice(), Toast.LENGTH_LONG).show();
			}
		});
		adapterArticoliLs = new ArticoloAdapter(getApplicationContext(), articoliLs);
		listViewArticoliLs.setAdapter(adapterArticoliLs);

		adapterArticoliLs.setOnModifcaQuantitaListener(new ArticoloAdapter.ModifcaQuantitaInventariati(){
			@Override
			public void modifcaQtyInventariati(Integer qty){
				//testoNumeroInvetariati.setText(qty.toString());
				//testoNumeroInvetariatiRimanenti.setText(numeroInvetariare);
				testoNumeroInventariati.setText(qty.toString());
			}
		});

		// abilito il filtro - non utilizzare dato che lo filtro con un adapter
		//listViewArticoliLs.setTextFilterEnabled(true);

		myFilter = (EditText)findViewById(R.id.myFilter);
		myFilter.addTextChangedListener(new TextWatcher(){
			//private Timer timer = new Timer();
			//private String sringaDaCercare = "";
			//private Boolean daCercare = false;

			public void afterTextChanged(final Editable s){
				//adapterArticoliLs.getFilter().filter(s.toString());
				//keyboardHide(myFilter);
				//sringaDaCercare = s.toString();
				//if(sringaDaCercare.length() > 2){
				/*	timer.cancel();
					timer = new Timer();
					timer.schedule(new TimerTask(){
						@Override
						public void run(){
							sringaDaCercare = s.toString();
							Log.i("Filtro", "run: " + sringaDaCercare);
							//adapterArticoliLs.getFilter().filter(sletto);
							//Toast.makeText(getApplicationContext(), "cerco: " + sletto, Toast.LENGTH_LONG);
							adapterArticoliLs.getFilter().filter(sringaDaCercare);
							daCercare = true;
							Log.i("Filtro", "daCercare 1: " + daCercare);
						}
					}, 500);*/
				//}

				/*synchronized(daCercare){
				Log.i("Filtro", "daCercare 2: " + daCercare);
				if(daCercare){
					daCercare = false;
					adapterArticoliLs.getFilter().filter(sringaDaCercare);
					sringaDaCercare = null;
					adapterArticoliLs.notifyDataSetChanged();
				}}*/
				//Log.i("Filtro", "daCercare 3: " + daCercare);

				setStringaDaCercare(s.toString());
				handler.removeMessages(TRIGGER_SEARCH);
				handler.sendEmptyMessageDelayed(TRIGGER_SEARCH, 500);
				Log.i("Filtro", "afterTextChanged");
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after){
			}

			public void onTextChanged(CharSequence s, int start, int before, int count){
			}
		});
		myFilter.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				myFilter.setText("");
				keyboardHide(myFilter);
				Log.i("Filtro", "onClick");
			}
		});
		//myFilter.setFocusableInTouchMode(false);
		myFilter.setOnTouchListener(new View.OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event){
				//hideSoftKeyboard(MainActivity.this);
				myFilter.setText("");
				keyboardHide(myFilter);
				Log.i("Filtro", "onTouch");
				return false;
			}
		});

		keyboardVisible = false;
		ImageButton keyboardButton = (ImageButton)findViewById(R.id.keyboardButton);
		keyboardButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				keyboardSwitch(v);
			}
		});
	}



	public void keyboardSwitch(View v){
		if(keyboardVisible){
			keyboardHide(v);
			myFilter.clearFocus();
			keyboardVisible = false;
		}else{
	//		inputMethodManager.showSoftInput(myFilter, InputMethodManager.SHOW_FORCED);
			keyboardVisible = true;
		}
		Log.i("keyboard", "visibile " + keyboardVisible);
		Log.i("Filtro", "keyboardSwitch");
	}

	public void keyboardHide(View v){
	//	inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
		myFilter.clearFocus();
		keyboardVisible = false;
		Log.i("Filtro", "keyboardHide");
	}

	public void keyboardShow(View v){
	//	inputMethodManager.showSoftInput(v, InputMethodManager.SHOW_FORCED);
		keyboardVisible = true;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		if(data != null){
			switch(requestCode){
				case 1:
					if(resultCode == Activity.RESULT_OK){
						String nomeFile = data.getStringExtra("nomeFile");
						Log.i("nomeFile", nomeFile);
						String gSonString = data.getStringExtra("fileDaAsSel");
						Gson gSon = new Gson();
						fileDaAs = gSon.fromJson(gSonString, FileDaAs.class);
						articoliLs = fileDaAs.getArticoliLs();
						numeroInvetariare = articoliLs.size();
						testoNumeroInvetariare.setText(String.valueOf(numeroInvetariare));
						adapterArticoliLs.setNumeroInvetariare(0);
						testoNumeroInventariati.setText("0");
						adapterArticoliLs.updateArticoliLs(articoliLs);
						saveButton.setVisibility(View.VISIBLE);
					}
					break;
			}
		}

	}
}
