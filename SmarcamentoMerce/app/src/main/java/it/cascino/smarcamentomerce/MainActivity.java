package it.cascino.smarcamentomerce;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
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
import java.util.concurrent.ExecutionException;

import it.cascino.smarcamentomerce.activity.LoginFileActivity;
import it.cascino.smarcamentomerce.adapter.ArticoloAdapter;
import it.cascino.smarcamentomerce.model.Barcode;

import it.cascino.smarcamentomerce.model.Articolo;
import it.cascino.smarcamentomerce.model.FileDaAs;

public class MainActivity extends Activity{
	private List<Articolo> articoliLs = new ArrayList<Articolo>();
	private ArticoloAdapter adapterArticoliLs;

	//private Integer posStatoOk = -1;
	//private Integer posStatoRettificato = -1;

	//private TextView numeroInvetariare;

	private Integer numeroInvetariare = 0;

	private String SHARED_PREF = "shared_pref_inventario";

	static final int PICK_CONTACT_REQUEST = 1;

	private TextView testoNumeroInvetariare;

	private Button saveButton;

	private FileDaAs fileDaAs;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

		/*SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);

		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString("operatore", "AGORIN");
		editor.putString("deposito", "2");
		editor.apply();

		String deposito = "";
		String operatore = "";
		if (sharedPreferences != null) {
			operatore = sharedPreferences.getString("operatore", "nd");
			deposito = sharedPreferences.getString("deposito", "0");
		}
		TextView numDep = (TextView)findViewById(R.id.numDep);
		numDep.setText(deposito);
		*/
		TextView dataSync = (TextView)findViewById(R.id.dataSync);
		DateFormat formatter = new SimpleDateFormat("HH:mm.ss - dd/MM/yyyy");
		String dataSyncStr = formatter.format(new Date());
		dataSync.setText(dataSyncStr);

		final TextView testoNumeroInvetariati = (TextView)findViewById(R.id.numeroInvetariati);
		testoNumeroInvetariati.setText("0");
		testoNumeroInvetariare = (TextView)findViewById(R.id.numeroInvetariare);
		testoNumeroInvetariare.setText("0");
		//final TextView testoNumeroInvetariatiRimanenti = (TextView)findViewById(R.id.numeroInvetariatiRimanenti);
		//testoNumeroInvetariatiRimanenti.setText("0");

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
				testoNumeroInvetariati.setText("0");
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
				testoNumeroInvetariati.setText(qty.toString());
			}
		});

		// abilito il filtro
		listViewArticoliLs.setTextFilterEnabled(true);

		final EditText myFilter = (EditText)findViewById(R.id.myFilter);
		myFilter.addTextChangedListener(new TextWatcher(){
			public void afterTextChanged(Editable s){
				adapterArticoliLs.getFilter().filter(s.toString());
				hideSoftKeyboard(MainActivity.this);
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
				hideSoftKeyboard(MainActivity.this);
			}
		});
		//myFilter.setFocusableInTouchMode(false);
		myFilter.setOnTouchListener(new View.OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event){
				hideSoftKeyboard(MainActivity.this);
				return false;
			}
		});
	}

	public static void hideSoftKeyboard(Activity activity){
		//InputMethodManager inputMethodManager = (InputMethodManager)activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
		//inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
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
						//numeroInvetariare = data.getIntExtra("numeroArticoli", -999);
						//FileDaAs fileDaAs = (FileDaAs)data.getSerializableExtra("fileDaAsSel");
						String gSonString = data.getStringExtra("fileDaAsSel");
						Gson gSon = new Gson();
						fileDaAs = gSon.fromJson(gSonString, FileDaAs.class);
						articoliLs = fileDaAs.getArticoliLs();
						numeroInvetariare = articoliLs.size();
						testoNumeroInvetariare.setText(String.valueOf(numeroInvetariare));
						adapterArticoliLs.updateArticoliLs(articoliLs);

						/*adapterArticoliLs.setArticoliOriginaleLs(articoliLs);
						adapterArticoliLs.notifyDataSetChanged();
						adapterArticoliLs.*/

						saveButton.setVisibility(View.VISIBLE);
					}
					break;
			}
		}

	}
}
