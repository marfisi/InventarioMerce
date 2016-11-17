package it.cascino.inventariomerce;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.commons.lang3.StringUtils;
import org.greenrobot.greendao.query.QueryBuilder;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import it.cascino.dbsqlite.Articoli;
import it.cascino.dbsqlite.ArticoliDao;
import it.cascino.dbsqlite.BarcodeDao;
import it.cascino.dbsqlite.CascinoOpenHandler;
import it.cascino.dbsqlite.DaoMaster;
import it.cascino.dbsqlite.DaoSession;
import it.cascino.dbsqlite.Depositi;
import it.cascino.dbsqlite.DepositiDao;
import it.cascino.dbsqlite.Inventari_dettagli;
import it.cascino.dbsqlite.Inventari_dettagliDao;
import it.cascino.dbsqlite.Inventari_testate;
import it.cascino.dbsqlite.Inventari_testateDao;
import it.cascino.dbsqlite.Qty_originali;
import it.cascino.dbsqlite.Qty_originaliDao;
import it.cascino.dbsqlite.Rel_articoli_barcode;
import it.cascino.dbsqlite.Rel_articoli_barcodeDao;
import it.cascino.inventariomerce.activity.AggiungiArticoloDaBarcodeActivity;
import it.cascino.inventariomerce.activity.SyncActivity;
import it.cascino.inventariomerce.adapter.ArticoloAdapter;
import it.cascino.inventariomerce.model.Articolo;
import it.cascino.inventariomerce.model.Barcode;
import it.cascino.inventariomerce.model.Inventario;
import it.cascino.inventariomerce.utils.TipoStato;

public class MainActivity extends Activity{
	private List<Articolo> articoliLs = new ArrayList<Articolo>();
	private ArticoloAdapter adapterArticoliLs;

	//private Integer numeroInvetariare = 0;

	private String SHARED_PREF = "shared_pref_inventario";

	private static final int SYNC_REQUEST = 1;
	private static final int ART_BCODE_REQUEST = 2;
	private static final int ART_MODIF_REQUEST = 3;
	private static final int SAVE_REQUEST = 4;

	private TextView testoNumeroInventariati;
	private TextView testoNumeroInvetariare;

	final private String MYDATABASE_NAME = "cascinoinventario.db";
	private CascinoOpenHandler helper = new CascinoOpenHandler(MainActivity.this, MYDATABASE_NAME, null);
	private SQLiteDatabase db;
	private DaoMaster daoMaster;
	private DaoSession daoSession;
	private ArticoliDao articoliDao;
	private BarcodeDao barcodeDao;
	private DepositiDao depositiDao;
	private Inventari_testateDao inventariTestateDao;
	private Inventari_dettagliDao inventariDettagliDao;
	private Qty_originaliDao qtyOriginaliDao;
	private Rel_articoli_barcodeDao relArticoliBarcodeDao;

	private Button saveButton;

	private Inventario inventario;

	private Boolean keyboardVisible;
	// private InputMethodManager inputMethodManager;

	private EditText myFilter;

	private String stringaDaCercare = null;

	private void setStringaDaCercare(String stringaDaCercare){
		this.stringaDaCercare = StringUtils.trimToEmpty(stringaDaCercare);
	}

	private Integer numeroRisultatoFiltro = -1;

	private final int TRIGGER_SEARCH = 1;
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg){
			if(msg.what == TRIGGER_SEARCH){
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
		helper.onUpgrade(db, 1, 2);
		db = helper.getWritableDatabase();
		daoMaster = new DaoMaster(db);
		daoSession = daoMaster.newSession();

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
				Intent intentLoginFileActivity = new Intent(getApplicationContext(), SyncActivity.class);
				startActivityForResult(intentLoginFileActivity, SYNC_REQUEST);
			}
		});

		saveButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				saveButton.setVisibility(View.INVISIBLE);
				Gson gSon = new Gson();
				String gSonString = gSon.toJson(inventario);
				Intent intentLoginFileActivity = new Intent(getApplicationContext(), SyncActivity.class);
				intentLoginFileActivity.putExtra("inventario", gSonString);
				startActivityForResult(intentLoginFileActivity, SAVE_REQUEST);
				Log.i("saveButton", saveButton.toString());
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

		/*adapterArticoliLs.setOnModifcaQuantitaListener(new ArticoloAdapter.ModifcaQuantitaInventariati(){
			@Override
			public void modifcaQtyInventariati(Integer qty){
				//testoNumeroInvetariati.setText(qty.toString());
				//testoNumeroInvetariatiRimanenti.setText(numeroInvetariare);
				testoNumeroInventariati.setText(qty.toString());
			}
		});*/

		adapterArticoliLs.setOnModifcaNumeroRisultatoFiltroListener(new ArticoloAdapter.ModifcaNumeroRisultatoFiltro(){
			@Override
			public void modifcaNumeroRisultatoFiltro(Integer qty){
				numeroRisultatoFiltro = qty;
				Log.i("Filtro", "size numeroRisultatoFiltro " + numeroRisultatoFiltro);
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

				if(StringUtils.length(stringaDaCercare) < 4){
					return;
				}
				handler.removeMessages(TRIGGER_SEARCH);
				handler.sendEmptyMessageDelayed(TRIGGER_SEARCH, 500);
				Log.i("Filtro", "afterTextChanged");
				Log.i("Filtro", "size numeroRisultatoFiltro " + numeroRisultatoFiltro);
				if(numeroRisultatoFiltro == 0 && (StringUtils.length(stringaDaCercare) > 3)){
					Intent intent = new Intent(getApplicationContext(), AggiungiArticoloDaBarcodeActivity.class);
					if(StringUtils.containsOnly(stringaDaCercare, "0123456789")){
						intent.putExtra("barcode", stringaDaCercare);
					}else if(StringUtils.startsWith(stringaDaCercare, "%")){    // gestisco se inizia con %
						intent.putExtra("codArt", StringUtils.removeStart(stringaDaCercare, "%"));
					}
					startActivityForResult(intent, ART_BCODE_REQUEST);
				}
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
				case SYNC_REQUEST:
					if(resultCode == Activity.RESULT_OK){
						String gSonString = data.getStringExtra("inventarioSel");
						Gson gSon = new Gson();
						inventario = gSon.fromJson(gSonString, Inventario.class);
						articoliLs = inventario.getArticoliLs();
						testoNumeroInvetariare.setText(String.valueOf(inventario.getNumeroArticoliTotale()));
						testoNumeroInventariati.setText(String.valueOf(inventario.getNumeroArticoliInventariati()));
						ordinaArticoliLs();
						adapterArticoliLs.updateArticoliLs(articoliLs);
						adapterArticoliLs.setInventario(inventario);
						saveButton.setVisibility(View.VISIBLE);
					}
					break;
				case ART_BCODE_REQUEST:
					if(resultCode == Activity.RESULT_OK){
						String aggiungiCodiceArticolo = data.getStringExtra("aggiungi");
						Log.i("aggiungiCodiceArticolo", aggiungiCodiceArticolo);
						if(StringUtils.equals(aggiungiCodiceArticolo, "Y")){
							String codiceArticolo = data.getStringExtra("codiceArticolo");
							Log.i("codiceArticolo", codiceArticolo);
							//inventario = gSon.fromJson(gSonString, Inventario.class);
							//articoliLs = inventario.getArticoliLs();
							testoNumeroInvetariare.setText(String.valueOf(inventario.getNumeroArticoliTotale()));
							testoNumeroInventariati.setText(String.valueOf(inventario.getNumeroArticoliInventariati()));
						}else{
							Log.i("codiceArticolo", "non aggiungerlo");
						}
					}
					break;
				case ART_MODIF_REQUEST:
					if(resultCode == Activity.RESULT_OK){
						String gSonString = data.getStringExtra("articolo");
						Gson gSon = new Gson();
						Articolo articoloInventariato = gSon.fromJson(gSonString, Articolo.class);
						articoloInventariato.setInModifica(false);
						gSonString = data.getStringExtra("inventario");
						inventario = gSon.fromJson(gSonString, Inventario.class);
						testoNumeroInvetariare.setText(String.valueOf(inventario.getNumeroArticoliTotale()));
						testoNumeroInventariati.setText(String.valueOf(inventario.getNumeroArticoliInventariati()));

						aggiornaArticoloInventariatoInDB(articoloInventariato);

						articoliLs.remove(articoloInventariato.getOrdinamento() - 1);
						articoliLs.add(articoloInventariato.getOrdinamento() - 1, articoloInventariato);
						ordinaArticoliLs();
						adapterArticoliLs.setInventario(inventario);
						adapterArticoliLs.updateArticoliLs(articoliLs);
					}else if(resultCode == Activity.RESULT_CANCELED){
						Log.i("modifica", "indietro da modifica");
					}
					break;
				case SAVE_REQUEST:
					if(resultCode == Activity.RESULT_OK){
						Log.i("salvato", "salvato");

						articoliLs.clear();
						inventario.setArticoliLs(articoliLs);
						testoNumeroInvetariare.setText("0");
						testoNumeroInventariati.setText("0");
						adapterArticoliLs.setInventario(inventario);
						adapterArticoliLs.updateArticoliLs(articoliLs);
					}
					break;
				default:
					break;
			}
		}

	}

	private void ordinaArticoliLs(){
		ArrayList<Articolo> articoliLs0 = new ArrayList<Articolo>();
		ArrayList<Articolo> articoliLs1 = new ArrayList<Articolo>();
		ArrayList<Articolo> articoliLs2 = new ArrayList<Articolo>();

		Iterator<Articolo> iter_articoliLs = articoliLs.iterator();
		Articolo art = null;
		while(iter_articoliLs.hasNext()){
			art = iter_articoliLs.next();
			switch(art.getStato()){
				case TipoStato.INVENTARIATO_OK:
					articoliLs0.add(art);
					break;
				case TipoStato.INVENTARIATO_DIFFER:
					articoliLs1.add(art);
					break;
				case TipoStato.DA_INVENTARIARE:
					articoliLs2.add(art);
					break;
				default:
					break;
			}
		}
		articoliLs.clear();
		articoliLs.addAll(articoliLs2);
		articoliLs.addAll(articoliLs1);
		articoliLs.addAll(articoliLs0);

		iter_articoliLs = articoliLs.iterator();
		int posInLista = 0;
		while(iter_articoliLs.hasNext()){
			posInLista++;
			art = iter_articoliLs.next();
			art.setOrdinamento(posInLista);
		}
	}

	private void aggiornaArticoloInventariatoInDB(Articolo articoloInventariato){
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSS");
		Timestamp timestamp = new Timestamp((new Date().getTime()));
		inventariTestateDao = daoSession.getInventari_testateDao();
		Inventari_testate inventariTestate = inventariTestateDao.queryBuilder().where(Inventari_testateDao.Properties.Id.eq(inventario.getProgressivo())).unique();
		inventariTestate.setData_modifica(formatter.format(timestamp));
		inventariTestate.setStato(String.valueOf(TipoStato.INVENTARIATO_DIFFER));
		inventariTestate.update();

		articoliDao = daoSession.getArticoliDao();
		Articoli articoli = articoliDao.queryBuilder().where(ArticoliDao.Properties.Codart.eq(articoloInventariato.getCodice())).unique();

		inventariDettagliDao = daoSession.getInventari_dettagliDao();
		Inventari_dettagli inventariDettagli = inventariDettagliDao.queryBuilder().where(Inventari_dettagliDao.Properties.Idtestata.eq(inventario.getProgressivo()), Inventari_dettagliDao.Properties.Idart.eq(articoli.getId())).unique();
		inventariDettagli.setQty_esposta(articoloInventariato.getQtyEsposteRilevata());
		inventariDettagli.setQty_magaz(articoloInventariato.getQtyMagazRilevata());
		inventariDettagli.setQty_difettosi(articoloInventariato.getQtyDifettRilevata());
		inventariDettagli.setQty_scorta_min(articoloInventariato.getScortaMinRilevata());
		inventariDettagli.setQty_scorta_max(articoloInventariato.getScortaMaxRilevata());
		inventariDettagli.setPrezzo(articoloInventariato.getPrezzoRilevata());
		inventariDettagli.setCommento(articoloInventariato.getCommento());
		inventariDettagli.setStato(String.valueOf(articoloInventariato.getStato()));
		inventariDettagli.setTimestamp(formatter.format(timestamp));
		inventariDettagli.setDesc(articoloInventariato.getDescRilevata());
		//inventariDettagli.setPosizioneinlista();
		inventariDettagli.setQty_per_confez(articoloInventariato.getQtyPerConfezRilevata());
		inventariDettagli.setBarcode((new Barcode()).arrayToString(articoloInventariato.getBarcodeRilevata()));
		inventariDettagli.update();
	}
}
