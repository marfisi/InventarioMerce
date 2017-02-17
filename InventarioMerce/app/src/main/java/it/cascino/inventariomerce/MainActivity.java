package it.cascino.inventariomerce;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.commons.lang3.StringUtils;
import org.greenrobot.greendao.query.QueryBuilder;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import it.cascino.dbsqlite.Articoli;
import it.cascino.dbsqlite.ArticoliDao;
import it.cascino.dbsqlite.BarcodeDao;
import it.cascino.dbsqlite.CascinoOpenHandler;
import it.cascino.dbsqlite.DaoMaster;
import it.cascino.dbsqlite.DaoSession;
import it.cascino.dbsqlite.Depositi;
import it.cascino.dbsqlite.DepositiDao;
import it.cascino.dbsqlite.Infogeneriche;
import it.cascino.dbsqlite.InfogenericheDao;
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
import it.cascino.inventariomerce.utils.DatiStatici;
import it.cascino.inventariomerce.utils.ScambioDatiIntent;
import it.cascino.inventariomerce.utils.TipoStato;

public class MainActivity extends Activity{
	private List<Articolo> articoliLs = new ArrayList<Articolo>();
	private ArticoloAdapter adapterArticoliLs;

	//private Integer numeroInvetariare = 0;
	Timer timer = new Timer();

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
	private InfogenericheDao infogenericheDao;

	private Button saveButton;

	//private View loadingPanel;

	private Inventario inventario;

	private Boolean keyboardVisible;
	// private InputMethodManager inputMethodManager;

	private EditText myFilter;

	private String stringaDaCercare = null;

	private void setStringaDaCercare(String stringaDaCercare){
		String strCercare = StringUtils.upperCase(StringUtils.trimToEmpty(stringaDaCercare));
		strCercare = StringUtils.stripStart(strCercare, "0");	// per quei barcode che iniziano con 0 ma che in as400 non sono presenti
		strCercare = StringUtils.removeStart(strCercare, "%");   // gestisco se inizia con %
		this.stringaDaCercare = strCercare;
	}

	private Integer numeroRisultatoFiltro = -1;

	private TextView inventNomeUtenteDestinatario;
	private TextView inventNomeUtenteCreatore;
	private TextView inventNumeroDeposito;
	private TextView inventNomeDeposito;
	private TextView inventDatacreazioneDb;
	private TextView inventDatacreazioneInventario;
	private TextView inventIdInventario;
	private TextView inventNomeSorgente;
	private TextView inventCommento;

	private MediaPlayer mp;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		helper.onUpgrade(db, 1, 2);
		db = helper.getWritableDatabase();
		daoMaster = new DaoMaster(db);
		daoSession = daoMaster.newSession();

		inventNomeUtenteDestinatario = (TextView)findViewById(R.id.nomeUtente);
		inventNomeUtenteDestinatario.setText("n.d.");
		inventNomeUtenteCreatore = (TextView)findViewById(R.id.utenteCreatore);
		inventNomeUtenteCreatore.setText("n.d.");
		inventNumeroDeposito = (TextView)findViewById(R.id.numDeposito);
		inventNumeroDeposito.setText("n.d.");
		inventNomeDeposito = (TextView)findViewById(R.id.nomeDeposito);
		inventNomeDeposito.setText("n.d.");
		inventDatacreazioneDb = (TextView)findViewById(R.id.dataCreazioneDatabase);
		inventDatacreazioneDb.setText("n.d.");
		inventDatacreazioneInventario = (TextView)findViewById(R.id.dataCreazioneInventario);
		inventDatacreazioneInventario.setText("n.d.");
		inventIdInventario = (TextView)findViewById(R.id.idInventario);
		inventIdInventario.setText("n.d.");
		inventNomeSorgente = (TextView)findViewById(R.id.nomeSorgente);
		inventNomeSorgente.setText("n.d.");
		inventCommento = (TextView)findViewById(R.id.commentoInventario);
		inventCommento.setText("n.d.");

		testoNumeroInventariati = (TextView)findViewById(R.id.numeroInvetariati);
		testoNumeroInventariati.setText("n.d.");
		testoNumeroInvetariare = (TextView)findViewById(R.id.numeroInvetariare);
		testoNumeroInvetariare.setText("n.d.");

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

		saveButton = (Button)findViewById(R.id.saveButton);
		saveButton.setOnLongClickListener(new View.OnLongClickListener(){
			@Override
			public boolean onLongClick(View v){
				DateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
				Timestamp timestamp = new Timestamp((new Date().getTime()));
				inventario.setTimeInvio(timestamp);

				StringBuilder stringBuilder = new StringBuilder();
				stringBuilder.append("deposito|").append(inventario.getDeposito()).append("\n");
				stringBuilder.append("userCrea|").append(inventario.getUtenteCreatore()).append("\n");
				stringBuilder.append("userInven|").append(inventario.getUtenteDestinatario()).append("\n");
				stringBuilder.append("creazione|").append(formatter.format(inventario.getTimeCreazione())).append("\n");

				DateFormat formatterTesto = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
				infogenericheDao = daoSession.getInfogenericheDao();
				Infogeneriche infogeneriche = infogenericheDao.queryBuilder().where(InfogenericheDao.Properties.Info.eq("data_creazione")).unique();
				Timestamp timestampCreazioneDb = null;
				try{
					Date date = (Date)formatterTesto.parse(infogeneriche.getValore());
					timestampCreazioneDb = new Timestamp(date.getTime());
				}catch(ParseException e){
					e.printStackTrace();
				}
				stringBuilder.append("database|").append(formatter.format(timestampCreazioneDb)).append("\n");
				stringBuilder.append("conferma|").append(formatter.format(inventario.getTimeInvio())).append("\n");
				stringBuilder.append("commento|").append(inventario.getCommento()).append("\n");
				stringBuilder.append("\n");
				stringBuilder.append("codice|barcode|desclunga|prezzo|qtyAttesa|qtyContate|qtyEsposteAttesa|qtyEsposteContate|qtyMagazAttesa|qtyMagazContate|difetAttesa|difetContate|scortaMinAttesa|scortaMinContate|scortaMaxAttesa|scortaMaxContate|perConfAttesa|perConfContate|commento|stato|timestamp").append("\n");

				List<Articolo> articoliLsDaSalv = inventario.getArticoliLs();
				Iterator<Articolo> iter_articoliLs = articoliLsDaSalv.iterator();
				Articolo art = null;
				while(iter_articoliLs.hasNext()){
					art = iter_articoliLs.next();
					stringBuilder.append(art.toStringPerFtpFile()).append("\n");
				}

				Gson gSon = new Gson();
				Intent intentSyncActivity = new Intent(getApplicationContext(), SyncActivity.class);
				String gSonString = gSon.toJson(inventario.getProgressivo());
				intentSyncActivity.putExtra("inventarioId", gSonString);
				//gSonString = gSon.toJson(stringBuilder.toString());
				//intentSyncActivity.putExtra("inventarioUpload", gSonString);
				ScambioDatiIntent.getInstance().setData(stringBuilder.toString());
				startActivityForResult(intentSyncActivity, SAVE_REQUEST);
				saveButton.setVisibility(View.INVISIBLE);
				Log.i("saveButton", saveButton.toString());
				return true;
			}
		});

		Button copyToClipboardButton = (Button)findViewById(R.id.copyToClipboard);
		copyToClipboardButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				StringBuilder stringBuilder = new StringBuilder();
				stringBuilder.append("CODART, DESC, QTY RISULTANTE, QTY RILEVATA").append("\n");
				List<Articolo> articoliLsDaSalv = inventario.getArticoliLs();
				Iterator<Articolo> iter_articoliLs = articoliLsDaSalv.iterator();
				Articolo art = null;
				while(iter_articoliLs.hasNext()){
					art = iter_articoliLs.next();
					if(art.getStato() != TipoStato.DA_INVENTARIARE){
						stringBuilder.append(art.toStringPerClipboard()).append("\n");
					}
				}
				ClipboardManager clipboard = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
				ClipData clip = ClipData.newPlainText("Copied", stringBuilder.toString());
				clipboard.setPrimaryClip(clip);
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



				/*if(StringUtils.length(stringaDaCercare) < 4){
					return;
				}*/
				//handler.removeMessages(TRIGGER_SEARCH);
				//handler.sendEmptyMessageDelayed(TRIGGER_SEARCH, 1000);
				//Log.i("Filtro", "afterTextChanged");

				setStringaDaCercare(s.toString());
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after){
			}

			public void onTextChanged(CharSequence s, int start, int before, int count){
			}
		});
		/*myFilter.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				setStringaDaCercare("");
				myFilter.setText("");
				keyboardHide(myFilter);
				Log.i("Filtro", "onClick");
			}
		});*/
		//myFilter.setFocusableInTouchMode(false);

		mp = MediaPlayer.create(getApplicationContext(), R.raw.click_filter);


		myFilter.setOnTouchListener(new View.OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event){
				if(event.getAction() == MotionEvent.ACTION_UP){
					//hideSoftKeyboard(MainActivity.this);
					setStringaDaCercare("");
					myFilter.setText("");
					myFilter.setSelectAllOnFocus(true);
					keyboardHide(myFilter);
					Log.i("Filtro", "onTouch");

					try{
						if(mp != null && mp.isPlaying()){
							mp.stop();
							mp.reset(); // release();
							mp = null;
							mp = MediaPlayer.create(getApplicationContext(), R.raw.click_filter);
							mp.start();
						}else{
							mp = MediaPlayer.create(getApplicationContext(), R.raw.click_filter);
							mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener(){
								@Override
								public void onPrepared(MediaPlayer arg0){
									mp.start();
								}
							});
							mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
								@Override
								public void onCompletion(MediaPlayer mp){
									mp.reset(); // release();
								}
							});
						}
					}catch(Exception e){
						e.getMessage();
					}
				}
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

		//loadingPanel = findViewById(R.id.loadingPanel);
		//loadingPanel.setVisibility(View.INVISIBLE);

		ImageButton cercaButton = (ImageButton)findViewById(R.id.cercaButton);
		cercaButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				//loadingPanel.setVisibility(View.VISIBLE);
				adapterArticoliLs.updateArticoliLs(articoliLs);
				adapterArticoliLs.getFilter().filter(stringaDaCercare);
				adapterArticoliLs.notifyDataSetChanged();

				timer.cancel();
				timer = new Timer();
				timer.schedule(new TimerTask(){
					@Override
					public void run(){
						cercaStringaDaCercare();
					}
				}, 500);
				//loadingPanel.setVisibility(View.INVISIBLE);

				/*if(mp != null && mp.isPlaying()){
					mp.stop();
					mp.reset(); // mp.release();
					mp = null;
				}*/
			}
		});

		adapterArticoliLs.setOnAccettaSoloEsposteListener(new ArticoloAdapter.GestioneBtnAccettaSoloEsposte(){
			@Override
			public void accettaSoloEsposte(Integer position){
				Articolo art = articoliLs.get(position);
				gestisciListaDopoModificaArticolo(art, inventario.getNumeroArticoliInventariati() + 1);
				Log.i("accettaSoloEsposte", "articolo " + position);
			}
		});

		RadioGroup radioGroupPreselezione = (RadioGroup)findViewById(R.id.radioGroupPreselezione);
		final RadioButton radioBtnPreselezioneEsposte = (RadioButton)findViewById(R.id.radioBtnPreselezioneEsposte);
		final RadioButton radioBtnPreselezioneMagazzino = (RadioButton)findViewById(R.id.radioBtnPreselezioneMagazzino);
		final RadioButton radioBtnPreselezioneNessuna = (RadioButton)findViewById(R.id.radioBtnPreselezioneNessuna);
		DatiStatici.getInstance().setTipoPreselezione(0);
		radioGroupPreselezione.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId){
				if(radioBtnPreselezioneEsposte.isChecked()){
					DatiStatici.getInstance().setTipoPreselezione(1);
				}else if(radioBtnPreselezioneMagazzino.isChecked()){
					DatiStatici.getInstance().setTipoPreselezione(2);
				}else{	// radioBtnPreselezioneNessuna
					DatiStatici.getInstance().setTipoPreselezione(0);
				}
			}
		});
	}

	private void cercaStringaDaCercare(){
		Log.i("Filtro", "size numeroRisultatoFiltro " + numeroRisultatoFiltro);
		if(numeroRisultatoFiltro == 0 && (StringUtils.length(stringaDaCercare) > 3)){
			Intent intent = new Intent(getApplicationContext(), AggiungiArticoloDaBarcodeActivity.class);
			if(StringUtils.containsOnly(stringaDaCercare, "0123456789")){
				intent.putExtra("barcode", stringaDaCercare);
			//}else if(StringUtils.startsWith(stringaDaCercare, "%")){    // gestisco se inizia con %
			//	intent.putExtra("codArt", StringUtils.removeStart(stringaDaCercare, "%"));
			}else{
				intent.putExtra("codArt", stringaDaCercare);
			}
			startActivityForResult(intent, ART_BCODE_REQUEST);
		}
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

						// ricarico il DB appena scaricato
						helper = new CascinoOpenHandler(MainActivity.this, MYDATABASE_NAME, null);
						helper.onUpgrade(db, 1, 2);
						db = helper.getWritableDatabase();
						daoMaster = new DaoMaster(db);
						daoSession = daoMaster.newSession();

						popolaArticoliInvetarioSelezionato();

						articoliLs = inventario.getArticoliLs();
						testoNumeroInvetariare.setText(String.valueOf(inventario.getNumeroArticoliTotale()));
						testoNumeroInventariati.setText(String.valueOf(inventario.getNumeroArticoliInventariati()));
						ordinaArticoliLs();
						adapterArticoliLs.updateArticoliLs(articoliLs);
						adapterArticoliLs.setInventario(inventario);
						saveButton.setVisibility(View.VISIBLE);

						// setto i campi di info in alto alla schermata
						inventNomeUtenteDestinatario.setText(inventario.getUtenteDestinatario());
						inventNomeUtenteCreatore.setText(inventario.getUtenteCreatore());
						inventNumeroDeposito.setText(inventario.getDeposito());
						depositiDao = daoSession.getDepositiDao();
						Depositi depositi = depositiDao.queryBuilder().where(DepositiDao.Properties.Iddep.eq(StringUtils.right("00" + inventario.getDeposito(), 2))).unique();
						inventNomeDeposito.setText(depositi.getDesc());
						DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
						infogenericheDao = daoSession.getInfogenericheDao();
						Infogeneriche infogeneriche = infogenericheDao.queryBuilder().where(InfogenericheDao.Properties.Info.eq("data_creazione")).unique();
						inventDatacreazioneDb.setText(infogeneriche.getValore());
						inventDatacreazioneInventario.setText(formatter.format(inventario.getTimeCreazione()));
						inventIdInventario.setText(String.valueOf(inventario.getProgressivo()));
						inventNomeSorgente.setText(inventario.getNomeFile());
						inventCommento.setText(inventario.getCommento());
					}
					break;
				case ART_BCODE_REQUEST:
					if(resultCode == Activity.RESULT_OK){
						String aggiungiCodiceArticolo = data.getStringExtra("aggiungi");
						Log.i("aggiungiCodiceArticolo", aggiungiCodiceArticolo);
						if(StringUtils.equals(aggiungiCodiceArticolo, "Y")){
							String codiceArticolo = data.getStringExtra("codiceArticolo");
							Log.i("codiceArticolo", codiceArticolo);

							aggiungiArticoloAdInventario(codiceArticolo);

							testoNumeroInvetariare.setText(String.valueOf(inventario.getNumeroArticoliTotale()));
							ordinaArticoliLs();
							adapterArticoliLs.setInventario(inventario);
							adapterArticoliLs.updateArticoliLs(articoliLs);
						}else{
							Log.i("codiceArticolo", "non aggiunto");
							setStringaDaCercare("");
							myFilter.setText("");
						}
					}
					break;
				case ART_MODIF_REQUEST:
					if(resultCode == Activity.RESULT_OK){
						String gSonString = data.getStringExtra("articolo");
						Gson gSon = new Gson();
						Articolo articoloInventariato = gSon.fromJson(gSonString, Articolo.class);
						gSonString = data.getStringExtra("numeroArticoliInventariati");
						Integer numeroArticoliInventariati = gSon.fromJson(gSonString, Integer.class);
						gestisciListaDopoModificaArticolo(articoloInventariato, numeroArticoliInventariati);
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

	private void gestisciListaDopoModificaArticolo(Articolo articoloInventariato, Integer numeroArticoliInventariati){
		articoloInventariato.setInModifica(false);
		inventario.setNumeroArticoliInventariati(numeroArticoliInventariati);
		testoNumeroInvetariare.setText(String.valueOf(inventario.getNumeroArticoliTotale()));
		testoNumeroInventariati.setText(String.valueOf(inventario.getNumeroArticoliInventariati()));

		aggiornaArticoloInventariatoInDB(articoloInventariato);

		articoliLs.remove(articoloInventariato.getOrdinamento() - 1);
		articoliLs.add(articoloInventariato.getOrdinamento() - 1, articoloInventariato);
		ordinaArticoliLs();
		inventario.setArticoliLs(articoliLs);
		adapterArticoliLs.setInventario(inventario);
		adapterArticoliLs.updateArticoliLs(articoliLs);
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

	private void aggiungiArticoloAdInventario(String codiceArticolo){
		articoliDao = daoSession.getArticoliDao();
		Articoli articoli = articoliDao.queryBuilder().where(ArticoliDao.Properties.Codart.eq(codiceArticolo)).unique();

		inventariDettagliDao = daoSession.getInventari_dettagliDao();
		Inventari_dettagli inventariDettagli = null;
		inventariDettagli = inventariDettagliDao.queryBuilder().where(Inventari_dettagliDao.Properties.Idtestata.eq(inventario.getProgressivo()), Inventari_dettagliDao.Properties.Idart.eq(articoli.getId())).unique();
		if(inventariDettagli != null){
			// significa che gia' c'e' l'articolo nell'invnetario quindi salto e non l'aggiungo
			return;
		}

		relArticoliBarcodeDao = daoSession.getRel_articoli_barcodeDao();
		qtyOriginaliDao = daoSession.getQty_originaliDao();
		depositiDao = daoSession.getDepositiDao();

		Depositi depositi = depositiDao.queryBuilder().where(DepositiDao.Properties.Iddep.eq(StringUtils.right("00" + inventario.getDeposito(), 2))).unique();
		Long idDeposito = depositi.getId();

		Articolo art = new Articolo();
		art.setOrdinamento(inventario.getNumeroArticoliTotale() + 1);
		art.setCodice(articoli.getCodart());
		QueryBuilder qb = qtyOriginaliDao.queryBuilder();
		Qty_originali qtyOriginali = null;
		try{
			qtyOriginali = (Qty_originali)qb.where(qb.and(Qty_originaliDao.Properties.Idart.eq(articoli.getId()), Qty_originaliDao.Properties.Iddep.eq(idDeposito))).unique();
		}catch(IndexOutOfBoundsException e){
			Log.w("Art non ha qty per dep ", String.valueOf(idDeposito));
		}
		if(qtyOriginali != null){
			art.setQtyOriginale(qtyOriginali.getQty());
			art.setQtyEsposteOriginale(qtyOriginali.getQty());
			art.setQtyMagazOriginale(0.0f);
			art.setQtyDifettOriginale(qtyOriginali.getQty_difettosi());
			art.setQtyInTrasferimentoOriginale(qtyOriginali.getQty_trasf());
			art.setScortaMinOriginale(qtyOriginali.getQty_scorta_min());
			art.setScortaMaxOriginale(qtyOriginali.getQty_scorta_max());
			art.setDataCarico(qtyOriginali.getData_carico());
			art.setDataScarico(qtyOriginali.getData_scarico());
			art.setDataUltimoInventario(qtyOriginali.getData_inventario());
		}else{
			art.setQtyOriginale(0.0f);
			art.setQtyEsposteOriginale(0.0f);
			art.setQtyMagazOriginale(0.0f);
			art.setQtyDifettOriginale(0.0f);
			art.setQtyInTrasferimentoOriginale(0.0f);
			art.setScortaMinOriginale(0.0f);
			art.setScortaMaxOriginale(0.0f);
			art.setDataCarico("");
			art.setDataScarico("");
			art.setDataUltimoInventario("");
		}
		inventariDettagli = new Inventari_dettagli();
		inventariDettagli.setIdtestata(inventario.getProgressivo());
		inventariDettagli.setIdart(articoli.getId());

		inventariDettagli.setQty_esposta(null);
		inventariDettagli.setQty_magaz(null);
		inventariDettagli.setQty_difettosi(null);
		inventariDettagli.setQty_scorta_min(null);
		inventariDettagli.setQty_scorta_max(null);
		inventariDettagli.setPrezzo(null);
		inventariDettagli.setCommento(null);
		inventariDettagli.setStato(String.valueOf(TipoStato.DA_INVENTARIARE));
		inventariDettagli.setTimestamp(inventario.getTimeCreazione().toString());
		inventariDettagli.setDesc(null);
		inventariDettagli.setPosizioneinlista(art.getOrdinamento());
		inventariDettagli.setQty_per_confez(null);
		List<Rel_articoli_barcode> relArticoliBarcode = null;
		try{
			relArticoliBarcode = relArticoliBarcodeDao.queryBuilder().where(Rel_articoli_barcodeDao.Properties.Idart.eq(articoli.getId())).list();
		}catch(Exception e){
			Log.w("Art non ha barcode ", Long.toString(articoli.getId()));
		}
		if(relArticoliBarcode != null){
			Barcode bcod[] = new Barcode[relArticoliBarcode.size()];
			for(int j = 0, s = relArticoliBarcode.size(); j < s; j++){
				bcod[j] = new Barcode();
				bcod[j].setCodice(relArticoliBarcode.get(j).getBarcode().getCodice());
			}
			inventariDettagli.setBarcode((new Barcode()).arrayToString(bcod));
		}else{
			inventariDettagli.setBarcode("");
		}

		inventariDettagliDao.insert(inventariDettagli);
		Long idInventariDettagli = inventariDettagli.getId();

		art.setQtyEsposteRilevata(inventariDettagli.getQty_esposta());
		art.setQtyMagazRilevata(inventariDettagli.getQty_magaz());
		art.setQtyDifettRilevata(inventariDettagli.getQty_difettosi());
		art.setScortaMinRilevata(inventariDettagli.getQty_scorta_min());
		art.setScortaMaxRilevata(inventariDettagli.getQty_scorta_max());
		art.setDescOriginale(articoli.getDesc());
		art.setDescRilevata(inventariDettagli.getDesc());
		art.setPrezzoOriginale(articoli.getPrezzo());
		art.setPrezzoRilevata(inventariDettagli.getPrezzo());
		art.setCommento(inventariDettagli.getCommento());
		art.setStato(Integer.parseInt(inventariDettagli.getStato()));
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSS");
		Date date = null;
		try{
			date = (Date)formatter.parse(inventariDettagli.getTimestamp());
			art.setTimestamp(new Timestamp(date.getTime()));
		}catch(Exception e){
			art.setTimestamp(null);
		}
		art.setOrdinamento(inventariDettagli.getPosizioneinlista());
		art.setQtyPerConfezOriginale(articoli.getQty_per_confez());
		art.setQtyPerConfezRilevata(inventariDettagli.getQty_per_confez());
		art.setBarcodeOriginale((new Barcode()).stringToArray(inventariDettagli.getBarcode()));
		art.setBarcodeRilevata((new Barcode()).stringToArray(inventariDettagli.getBarcode()));
		art.setUm(articoli.getUm());
		art.setInModifica(false);

		articoliLs.add(art);
		inventario.setArticoliLs(articoliLs);
		inventario.setNumeroArticoliTotale(articoliLs.size());
	}

	private void popolaArticoliInvetarioSelezionato(){
		int idDeposito = Integer.parseInt(inventario.getDeposito());
		inventariTestateDao = daoSession.getInventari_testateDao();
		Inventari_testate inventariTestate = inventariTestateDao.queryBuilder().where(Inventari_testateDao.Properties.Id.eq(inventario.getProgressivo())).unique();
		inventariDettagliDao = daoSession.getInventari_dettagliDao();
		List<Inventari_dettagli> inventariDettagliLs = null;
		inventariDettagliLs = inventariDettagliDao.queryBuilder().where(Inventari_dettagliDao.Properties.Idtestata.eq(inventariTestate.getId())).list();
		Inventari_dettagli inventariDettagli = null;
		articoliDao = daoSession.getArticoliDao();
		qtyOriginaliDao = daoSession.getQty_originaliDao();
		relArticoliBarcodeDao = daoSession.getRel_articoli_barcodeDao();

		for(int i = 0, n = inventariDettagliLs.size(); i < n; i++){
			inventariDettagli = inventariDettagliLs.get(i);
			Articoli articoli = articoliDao.queryBuilder().where(ArticoliDao.Properties.Id.eq(inventariDettagli.getIdart())).unique();

			Iterator<Articolo> iter_articoliLs = inventario.getArticoliLs().iterator();
			Articolo art = null;
			while(iter_articoliLs.hasNext()){
				art = iter_articoliLs.next();
				//String s1 = art.getCodice();
				//String s2 = articoli.getCodart();
				if(!(StringUtils.equals(art.getCodice(), articoli.getCodart()))){
					//if(!(StringUtils.equals(s1, s2))){
					continue;
				}
				// trovato e quindi lo popolo
				QueryBuilder qb = qtyOriginaliDao.queryBuilder();
				Qty_originali qtyOriginali = null;
				try{
					qtyOriginali = (Qty_originali)qb.where(qb.and(Qty_originaliDao.Properties.Idart.eq(articoli.getId()), Qty_originaliDao.Properties.Iddep.eq(idDeposito))).unique();
					//qtyOriginali = (Qty_originali)qb.where(qb.and(Qty_originaliDao.Properties.Idart.eq(articoli.getId()), Qty_originaliDao.Properties.Iddep.eq(idDeposito))).list().get(0);
				}catch(IndexOutOfBoundsException e){
					qtyOriginali = null;
					Log.w("Art non ha qty per dep ", String.valueOf(idDeposito));
				}
				if(qtyOriginali != null){
					art.setQtyOriginale(qtyOriginali.getQty());
					art.setQtyEsposteOriginale(qtyOriginali.getQty());
					art.setQtyMagazOriginale(0.0f);
					art.setQtyDifettOriginale(qtyOriginali.getQty_difettosi());
					art.setQtyInTrasferimentoOriginale(qtyOriginali.getQty_trasf());
					art.setScortaMinOriginale(qtyOriginali.getQty_scorta_min());
					art.setScortaMaxOriginale(qtyOriginali.getQty_scorta_max());
					art.setDataCarico(qtyOriginali.getData_carico());
					art.setDataScarico(qtyOriginali.getData_scarico());
					art.setDataUltimoInventario(qtyOriginali.getData_inventario());
				}else{
					art.setQtyOriginale(0.0f);
					art.setQtyEsposteOriginale(0.0f);
					art.setQtyMagazOriginale(0.0f);
					art.setQtyDifettOriginale(0.0f);
					art.setQtyInTrasferimentoOriginale(0.0f);
					art.setScortaMinOriginale(0.0f);
					art.setScortaMaxOriginale(0.0f);
					art.setDataCarico("");
					art.setDataScarico("");
					art.setDataUltimoInventario("");
				}
				Float qtyRilevata = null;
				Float qtyEsposteRilevata = inventariDettagli.getQty_esposta();
				Float qtyMagazRilevata = inventariDettagli.getQty_magaz();
				if((qtyEsposteRilevata != null) || (qtyMagazRilevata != null)){
					qtyRilevata = 0.0f;
					if(qtyEsposteRilevata != null){
						qtyRilevata = qtyRilevata + qtyEsposteRilevata;
					}
					if(qtyMagazRilevata != null){
						qtyRilevata = qtyRilevata + qtyMagazRilevata;
					}
					if(art.getQtyDifettOriginale() != null){
						qtyRilevata = qtyRilevata + art.getQtyDifettOriginale();
					}
				}
				art.setQtyRilevata(qtyRilevata);

				art.setQtyEsposteRilevata(qtyEsposteRilevata);
				art.setQtyMagazRilevata(qtyMagazRilevata);
				art.setQtyDifettRilevata(inventariDettagli.getQty_difettosi());
				art.setScortaMinRilevata(inventariDettagli.getQty_scorta_min());
				art.setScortaMaxRilevata(inventariDettagli.getQty_scorta_max());
				art.setDescOriginale(articoli.getDesc());
				art.setDescRilevata(inventariDettagli.getDesc());
				art.setPrezzoOriginale(articoli.getPrezzo());
				art.setPrezzoRilevata(inventariDettagli.getPrezzo());
				art.setCommento(inventariDettagli.getCommento());
				art.setStato(Integer.parseInt(inventariDettagli.getStato()));
				DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSS");
				Date date = null;
				try{
					date = (Date)formatter.parse(inventariDettagli.getTimestamp());
					art.setTimestamp(new Timestamp(date.getTime()));
				}catch(Exception e){
					art.setTimestamp(null);
				}
				art.setOrdinamento(inventariDettagli.getPosizioneinlista());
				art.setQtyPerConfezOriginale(articoli.getQty_per_confez());
				art.setQtyPerConfezRilevata(inventariDettagli.getQty_per_confez());

				List<Rel_articoli_barcode> relArticoliBarcode = null;
				try{
					relArticoliBarcode = relArticoliBarcodeDao.queryBuilder().where(Rel_articoli_barcodeDao.Properties.Idart.eq(articoli.getId())).list();
				}catch(Exception e){
					Log.w("Art non ha barcode ", Long.toString(articoli.getId()));
				}
				if(relArticoliBarcode != null){
					Barcode bcod[] = new Barcode[relArticoliBarcode.size()];
					for(int j = 0, s = relArticoliBarcode.size(); j < s; j++){
						bcod[j] = new Barcode();
						bcod[j].setCodice(relArticoliBarcode.get(j).getBarcode().getCodice());
					}
					art.setBarcodeOriginale(bcod);
				}else{
					art.setBarcodeOriginale(null);
				}
				art.setBarcodeRilevata((new Barcode()).stringToArray(inventariDettagli.getBarcode()));
				art.setUm(articoli.getUm());

				art.setInModifica(false);
			}
		}
	}
}
