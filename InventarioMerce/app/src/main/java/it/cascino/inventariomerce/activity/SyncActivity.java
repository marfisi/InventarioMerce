package it.cascino.inventariomerce.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.MalformedURLException;
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
import java.util.concurrent.ExecutionException;

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
import it.cascino.dbsqlite.Qty_originaliDao;
import it.cascino.dbsqlite.Rel_articoli_barcode;
import it.cascino.dbsqlite.Rel_articoli_barcodeDao;
import it.cascino.inventariomerce.R;

import it.cascino.inventariomerce.adapter.SyncAdapter;
import it.cascino.inventariomerce.model.Articolo;
import it.cascino.inventariomerce.model.Barcode;
import it.cascino.inventariomerce.model.Inventario;
import it.cascino.inventariomerce.utils.ScambioDatiIntent;
import it.cascino.inventariomerce.utils.TipoStato;

public class SyncActivity extends Activity{
	private List<Inventario> inventariLs = new ArrayList<Inventario>();
	private SyncAdapter syncAdapter;

	private Inventario inventarioSel;

	private String SHARED_PREF = "shared_pref_inventario";
	private SharedPreferences sharedPreferences;
	private EditText depositoEditText;
	private EditText operatoreEditText;
	private View loadingPanel;
	private Timer timer = new Timer();

	private String deposito = "";
	private String operatore = "";

	private final String serverFtp = "ftp1.cascino.it";
	private final String userFtp = "androidftp";
	private final String passwordFtp = "androidftp";
	private final String directoryFtp = "/";
	private FTPClient ftpClient = new FTPClient();
	private InetAddress ia;

	final private String MYDATABASE_NAME = "cascinoinventario.db";
	private CascinoOpenHandler helper = new CascinoOpenHandler(SyncActivity.this, MYDATABASE_NAME, null);
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

	private Boolean fileSemaforoPresente;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sync);

		sharedPreferences = getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
		updatePreferencesData();

		helper.onUpgrade(db, 1, 2);
		db = helper.getWritableDatabase();
		daoMaster = new DaoMaster(db);
		daoSession = daoMaster.newSession();

		depositoEditText = (EditText)findViewById(R.id.deposito);
		depositoEditText.setText(deposito);
		depositoEditText.addTextChangedListener(new TextWatcher(){
			public void afterTextChanged(Editable s){
				savePreferencesData();
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after){
			}

			public void onTextChanged(CharSequence s, int start, int before, int count){
			}
		});
		operatoreEditText = (EditText)findViewById(R.id.operatore);
		operatoreEditText.setText(operatore);
		operatoreEditText.addTextChangedListener(new TextWatcher(){
			public void afterTextChanged(Editable s){
				savePreferencesData();
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after){
			}

			public void onTextChanged(CharSequence s, int start, int before, int count){
			}
		});

		final Button caricaBtn = (Button)findViewById(R.id.caricaBtn);

		loadingPanel = findViewById(R.id.loadingPanel);

		Button inviaDBBtn = (Button)findViewById(R.id.inviaDBBtn);
		inviaDBBtn.setOnLongClickListener(new View.OnLongClickListener(){
			@Override
			public boolean onLongClick(View v){
				//inventariLs.clear();
				//	loadingPanel.setVisibility(View.VISIBLE);
				UploadDBThread dt = new UploadDBThread();
				try{
					dt.execute("").get();
				}catch(InterruptedException e){
					e.printStackTrace();
				}catch(ExecutionException e){
					e.printStackTrace();
				}
				//loadingPanel.setVisibility(View.GONE);
				//syncAdapter.notifyDataSetChanged();
				//caricaBtn.setEnabled(false);
				return true;
			}
		});

		final Button eliminaInventBtn = (Button)findViewById(R.id.eliminaInventBtn);

		eliminaInventBtn.setOnLongClickListener(new View.OnLongClickListener(){
			@Override
			public boolean onLongClick(View v){
				if(inventarioSel != null){
					// per simulare la cancellazione scrivo la data di conferma, cosi' non risulta ancora aperto e mi consnete di caricare un nuovo invnetario
					inventariTestateDao = daoSession.getInventari_testateDao();
					Inventari_testate inventari_testate = inventariTestateDao.queryBuilder().where(Inventari_testateDao.Properties.Id.eq(inventarioSel.getProgressivo())).unique();

					DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSS");
					Timestamp timestamp = new Timestamp((new Date().getTime()));
					inventari_testate.setData_conferma(formatter.format(timestamp));
					inventari_testate.update();
					inventariLs.remove(inventarioSel);
					syncAdapter.notifyDataSetChanged();
				}
				return false;
			}
		});

		RadioGroup radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
		radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId){
				//loadingPanel.setVisibility(View.GONE);
				inventariLs.clear();
				caricaBtn.setEnabled(false);
				eliminaInventBtn.setVisibility(View.INVISIBLE);
				syncAdapter.notifyDataSetChanged();
			}

		});
		final RadioButton radioBtnFile = (RadioButton)findViewById(R.id.radioBtnFile);

		Button leggiBtn = (Button)findViewById(R.id.leggiBtn);
		leggiBtn.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				inventariLs.clear();
				if(radioBtnFile.isChecked()){
					DownloadFileThread dt = new DownloadFileThread();
					try{
						dt.execute("").get();
					}catch(InterruptedException e){
						e.printStackTrace();
					}catch(ExecutionException e){
						e.printStackTrace();
					}
				}else{    // check del radio button Database
					leggiInvetariDaDB();
				}
				syncAdapter.notifyDataSetChanged();
				//caricaBtn.setEnabled(true);
			}
		});

		ListView listViewInventarioLs = (ListView)findViewById(R.id.fileList);
		listViewInventarioLs.setOnItemClickListener(new AdapterView.OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id){
				inventarioSel = inventariLs.get(position);
				Toast.makeText(getApplicationContext(), "Sel: " + inventarioSel.getProgressivo() + " " + inventarioSel.getNomeFile(), Toast.LENGTH_SHORT).show();

				if(radioBtnFile.isChecked()){
					// per abilitare il caricamento da file devo controllare che non ci siano inventari aperti
					inventariTestateDao = daoSession.getInventari_testateDao();
					Long numeroInventariAperti = inventariTestateDao.queryBuilder().where(Inventari_testateDao.Properties.Iddep.eq(deposito), Inventari_testateDao.Properties.Utente_destinatario.eq(operatore), Inventari_testateDao.Properties.Data_conferma.isNull()).count();
					if(Long.compare(numeroInventariAperti, 0) == 0 ){
						caricaBtn.setEnabled(true);
					}else{
						Toast.makeText(getApplicationContext(), "Inventari gia' caricati", Toast.LENGTH_LONG).show();
					}
				}else{    // check del radio button Database
					caricaBtn.setEnabled(true);
					eliminaInventBtn.setVisibility(View.VISIBLE);
				}

			}
		});
		syncAdapter = new SyncAdapter(getApplicationContext(), inventariLs);
		listViewInventarioLs.setAdapter(syncAdapter);

		caricaBtn.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				loadingPanel.setVisibility(View.VISIBLE);
				if(inventarioSel.getSorgenteFile()){
					ScriviFileSemaforoFtpThread sst = new ScriviFileSemaforoFtpThread();
					try{
						sst.execute("").get();
					}catch(InterruptedException e){
						e.printStackTrace();
					}catch(ExecutionException e){
						e.printStackTrace();
					}
					fileSemaforoPresente = true;

					while(fileSemaforoPresente){
						LeggiFileSemaforoFtpThread slt = new LeggiFileSemaforoFtpThread();
						try{
							slt.execute("").get();
						}catch(InterruptedException e){
							e.printStackTrace();
						}catch(ExecutionException e){
							e.printStackTrace();
						}
						try{
							Thread.sleep(30000);	// 30 secondi
						}catch(InterruptedException e){
						}
					}

					/*
					timer.cancel();
					timer = new Timer();
					timer.schedule(new TimerTask(){
						@Override
						public void run(){


							if(!(fileSemaforoPresente)){
								timer.cancel();
							}
						}
					}, 10000);*/

					DownloadDBThread dt = new DownloadDBThread();
					try{
						dt.execute("").get();
					}catch(InterruptedException e){
						e.printStackTrace();
					}catch(ExecutionException e){
						e.printStackTrace();
					}

					// ricarico il DB appena scaricato
					helper = new CascinoOpenHandler(SyncActivity.this, MYDATABASE_NAME, null);
					helper.onUpgrade(db, 1, 2);
					db = helper.getWritableDatabase();
					daoMaster = new DaoMaster(db);
					daoSession = daoMaster.newSession();

					// se e' file devo aggiungere nel DB l'inveNtario
					inserisciInvetarioInDB();
					rinominaFileSorgenteFtpThread(inventarioSel.getNomeFile());
				}
				//loadingPanel.setVisibility(View.GONE);

				//popolaArticoliInvetarioSelezionato();

				Intent resultIntent = new Intent();
				Gson gSon = new Gson();
				String gSonString = gSon.toJson(inventarioSel);
				resultIntent.putExtra("inventarioSel", gSonString);
				setResult(Activity.RESULT_OK, resultIntent);
				finish();
			}
		});

		Intent intentFtpUpload = getIntent();
		if(intentFtpUpload != null){
			String gSonString = intentFtpUpload.getStringExtra("inventarioUpload");
			String inventarioUpload = ScambioDatiIntent.getInstance().getData();
			if(StringUtils.isNotEmpty(inventarioUpload)){
				Gson gSon = new Gson();
				gSonString = intentFtpUpload.getStringExtra("inventarioId");
				Integer inventarioId = gSon.fromJson(gSonString, Integer.class);
				//gSonString = intentFtpUpload.getStringExtra("inventarioUpload");
				//String inventarioUpload = gSon.fromJson(gSonString, String.class);
				UploadFileThread ut = new UploadFileThread(inventarioId, inventarioUpload);
				try{
					ut.execute("").get();
				}catch(InterruptedException e){
					e.printStackTrace();
				}catch(ExecutionException e){
					e.printStackTrace();
				}
				Intent resultIntent = new Intent();
				setResult(Activity.RESULT_OK, resultIntent);
				finish();
			}
		}
	}

	private class DownloadFileThread extends AsyncTask<String, Void, String>{
		@Override
		protected String doInBackground(String... params){
			try{
				inventariLs.clear();

				Boolean resultFtpOper = false;
				ia = InetAddress.getByName(serverFtp);
				ftpClient.connect(ia, 21);
				ftpClient.login(userFtp, passwordFtp);
				ftpClient.setBufferSize(1024*1024);
				ftpClient.enterLocalPassiveMode();
				//ftpClient.storeFile("test.txt", new FileInputStream(file));
				resultFtpOper = ftpClient.changeWorkingDirectory(directoryFtp);
				if(!(resultFtpOper)){
					Log.e("ftp cambio dire fallito", Boolean.toString(resultFtpOper));
				}

				FTPFile[] ftpFiles = ftpClient.listFiles();
				for(FTPFile fileCorrente : ftpFiles){
					String nomeFile = StringUtils.trim(fileCorrente.getName());
					Log.i("file name", nomeFile);

					if(!(StringUtils.startsWith(nomeFile, "dep"))){
						continue;
					}
					if(!(StringUtils.contains(nomeFile, StringUtils.join("dep", deposito, "_", operatore)))){
						continue;
					}

					Inventario invent = new Inventario();
					invent.setSorgenteFile(true);
					invent.setNomeFile(nomeFile);
					invent.setUtenteDestinatario(operatore);
					invent.setDeposito(deposito);
					// determino il progressivo del file, che e' l'ultimo numero a destra
					invent.setProgressivo(Integer.parseInt(StringUtils.substringBefore(StringUtils.substringAfterLast(nomeFile, "_"), ".")));

					InputStream inputStream = ftpClient.retrieveFileStream(nomeFile);
					if(inputStream == null){
						Log.e("inputStream", "inputStream e' nullo");
						Log.i("getReplyCode", String.valueOf(ftpClient.getReplyCode()));
						Log.e("getReplyString", ftpClient.getReplyString());
					}
					InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF8");
					BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

					DateFormat formatter = new SimpleDateFormat("yyyyMMdd HH:mm:ss.SSS");

					Integer numRigoArt = 0;
					String lineRead = "";
					List<Articolo> articoliLs = new ArrayList<Articolo>();
					// leggo le righe che sono di intestazione
					lineRead = bufferedReader.readLine(); // deposito
					lineRead = bufferedReader.readLine(); // creatore
					String campiSlit[] = StringUtils.splitByWholeSeparatorPreserveAllTokens(lineRead, invent.getCampiSep());
					invent.setUtenteCreatore(campiSlit[1]);
					lineRead = bufferedReader.readLine(); // operatore
					lineRead = bufferedReader.readLine(); // data creazione
					campiSlit = StringUtils.splitByWholeSeparatorPreserveAllTokens(lineRead, invent.getCampiSep());
					try{
						Date date = (Date)formatter.parse(campiSlit[1]);
						invent.setTimeCreazione(new Timestamp(date.getTime()));
					}catch(ParseException e){
						e.printStackTrace();
					}
					lineRead = bufferedReader.readLine(); // commento
					campiSlit = StringUtils.splitByWholeSeparatorPreserveAllTokens(lineRead, invent.getCampiSep());
					invent.setCommento(campiSlit[1]);
					lineRead = bufferedReader.readLine();    // rigo vuoto
					lineRead = bufferedReader.readLine();    // intestazione
					while((lineRead = bufferedReader.readLine()) != null){
						numRigoArt++;
						Articolo art = new Articolo();
						art.setOrdinamento(numRigoArt);
						campiSlit = StringUtils.splitByWholeSeparatorPreserveAllTokens(lineRead, invent.getCampiSep());
						art.setCodice(campiSlit[0]);

						articoliLs.add(art);
					}
					ftpClient.completePendingCommand();
					inputStream.close();
					inputStreamReader.close();
					bufferedReader.close();

					invent.setNumeroArticoliTotale(numRigoArt);
					invent.setNumeroArticoliInventariati(0);
					invent.setArticoliLs(articoliLs);
					invent.setStato(String.valueOf(TipoStato.DA_INVENTARIARE));

					inventariLs.add(invent);
				}
				//fileDaAs.setArticoliLs(articoliLs);

				ftpClient.logout();
				ftpClient.disconnect();
			}catch(MalformedURLException e){
				e.printStackTrace();
			}catch(IOException e){
				e.printStackTrace();
			}

			//adapterArticoliLs.setArticoliOriginaleLs(articoliLs);

			//numeroInvetariare = articoliLs.size();

			return "Thread Download File terminato";
		}

		@Override
		protected void onPostExecute(String result){
			super.onPostExecute(result);
			//Toast.makeText(LoginFileActivity.this, "post", Toast.LENGTH_LONG).show();
			syncAdapter.notifyDataSetChanged();
		}

		@Override
		protected void onPreExecute(){
			super.onPreExecute();
			//Toast.makeText(LoginFileActivity.this, "pre", Toast.LENGTH_LONG).show();
		}

		@Override
		protected void onProgressUpdate(Void... values){
			//Toast.makeText(LoginFileActivity.this, "progress", Toast.LENGTH_LONG).show();
		}
	}

	private class DownloadDBThread extends AsyncTask<String, Void, String>{
		@Override
		protected String doInBackground(String... params){
			try{
				Boolean resultFtpOper = false;
				ia = InetAddress.getByName(serverFtp);
				ftpClient.connect(ia, 21);
				ftpClient.login(userFtp, passwordFtp);
				ftpClient.setBufferSize(1024 * 1024);
				ftpClient.enterLocalPassiveMode();
				//ftpClient.storeFile("test.txt", new FileInputStream(file));
				resultFtpOper = ftpClient.changeWorkingDirectory(directoryFtp);
				if(!(resultFtpOper)){
					Log.e("ftp cambio dire fallito", Boolean.toString(resultFtpOper));
				}

				FTPFile[] ftpFiles = ftpClient.listFiles();
				for(FTPFile fileCorrente : ftpFiles){
					String nomeFile = StringUtils.trim(fileCorrente.getName());
					if(StringUtils.equals(nomeFile, "cascinoinventario.db")){
						String DB_PATH = "/data/data/it.cascino.inventariomerce/";
						final String MYDATABASE_NAME = "cascinoinventario.db";

						OutputStream output;
						output = new FileOutputStream(DB_PATH + "/" + MYDATABASE_NAME);
						//get the file from the remote system
						ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
						ftpClient.retrieveFile(MYDATABASE_NAME, output);
						//close output stream
						output.close(); //

						File f = new File(DB_PATH + "databases/");
						if(!(f.exists())){
							f.mkdir();
						}
						f = new File(DB_PATH + "databases/" + MYDATABASE_NAME);
						f.delete();
						InputStream assetsDB = new FileInputStream(DB_PATH + MYDATABASE_NAME);
						OutputStream dbOut = new FileOutputStream(DB_PATH + "databases/" + MYDATABASE_NAME);
						byte[] buffer = new byte[1024];
						int length = 0;
						while((length = assetsDB.read(buffer)) != -1){
							dbOut.write(buffer, 0, length);
						}
						dbOut.flush();
						dbOut.close();
						assetsDB.close();
						f = new File(DB_PATH + MYDATABASE_NAME);
						f.delete();
					}
				}
				ftpClient.logout();
				ftpClient.disconnect();
			}catch(MalformedURLException e){
				e.printStackTrace();
			}catch(IOException e){
				e.printStackTrace();
			}
			return "Thread Download DB terminato";
		}

		@Override
		protected void onPostExecute(String result){
			super.onPostExecute(result);
			//loadingPanel.setVisibility(View.GONE);
			//Toast.makeText(LoginFileActivity.this, "post", Toast.LENGTH_LONG).show();
			//fileDaAsAdapter.notifyDataSetChanged();
		}

		@Override
		protected void onPreExecute(){
			super.onPreExecute();
			//loadingPanel.setVisibility(View.VISIBLE);
			//Toast.makeText(LoginFileActivity.this, "pre", Toast.LENGTH_LONG).show();
		}

		@Override
		protected void onProgressUpdate(Void... values){
			//Toast.makeText(LoginFileActivity.this, "progress", Toast.LENGTH_LONG).show();
		}
	}

	private class UploadDBThread extends AsyncTask<String, Void, String>{
		@Override
		protected String doInBackground(String... params){
			try{
				Boolean resultFtpOper = false;
				ia = InetAddress.getByName(serverFtp);
				ftpClient.connect(ia, 21);
				ftpClient.login(userFtp, passwordFtp);
				ftpClient.setBufferSize(1024 * 1024);
				ftpClient.enterLocalPassiveMode();
				resultFtpOper = ftpClient.changeWorkingDirectory(directoryFtp);
				if(!(resultFtpOper)){
					Log.e("ftp cambio dire fallito", Boolean.toString(resultFtpOper));
				}

				DateFormat formatter = new SimpleDateFormat("yyyyMMdd");
				String dataSyncStr = formatter.format(new Date());

				String DB_PATH = "/data/data/it.cascino.inventariomerce/";
				final String MYDATABASE_NAME = "cascinoinventario.db";

				File f = new File(DB_PATH + "databases/");
				if(!(f.exists())){
					f.mkdir();
				}
				f = new File(DB_PATH + "databases/" + MYDATABASE_NAME);

				ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
				InputStream inputStream = new FileInputStream(f.getPath());
				String nomeFileDb = StringUtils.join(dataSyncStr, "_", MYDATABASE_NAME, "_", "dep", deposito, "_", operatore, ".db");
				nomeFileDb = StringUtils.replace(nomeFileDb, ".db_", "_");
				ftpClient.storeFile(nomeFileDb, inputStream);
				inputStream.close();

				ftpClient.logout();
				ftpClient.disconnect();
			}catch(MalformedURLException e){
				e.printStackTrace();
			}catch(IOException e){
				e.printStackTrace();
			}
			return "Thread Upload DB terminato";
		}

		@Override
		protected void onPostExecute(String result){
			super.onPostExecute(result);
			//loadingPanel.setVisibility(View.GONE);
			//Toast.makeText(LoginFileActivity.this, "post", Toast.LENGTH_LONG).show();
			//fileDaAsAdapter.notifyDataSetChanged();
		}

		@Override
		protected void onPreExecute(){
			super.onPreExecute();
			//loadingPanel.setVisibility(View.VISIBLE);
			//Toast.makeText(LoginFileActivity.this, "pre", Toast.LENGTH_LONG).show();
		}

		@Override
		protected void onProgressUpdate(Void... values){
			//Toast.makeText(LoginFileActivity.this, "progress", Toast.LENGTH_LONG).show();
		}
	}

	/*private class LoadingThread extends AsyncTask<String, Void, String>{
		@Override
		protected String doInBackground(String... params){
			if(params[1].toString() == "start"){
				loadingPanel.setVisibility(View.VISIBLE);
			}else{
				loadingPanel.setVisibility(View.GONE);
			}
			return "Thread terminato";
		}

		@Override
		protected void onPostExecute(String result){
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute(){
			super.onPreExecute();
		}

		@Override
		protected void onProgressUpdate(Void... values){
		}
	}*/

	private class UploadFileThread extends AsyncTask<String, Void, String>{
		private Integer inventarioId;
		private String inventarioUpload;

		public UploadFileThread(Integer inventarioId, String inventarioUpload){
			this.inventarioId = inventarioId;
			this.inventarioUpload = inventarioUpload;
		}

		@Override
		protected String doInBackground(String... params){
			try{
				Boolean resultFtpOper = false;
				ia = InetAddress.getByName(serverFtp);
				ftpClient.connect(ia, 21);
				ftpClient.login(userFtp, passwordFtp);
				ftpClient.setBufferSize(1024 * 1024);
				ftpClient.enterLocalPassiveMode();
				resultFtpOper = ftpClient.changeWorkingDirectory(directoryFtp);
				if(!(resultFtpOper)){
					Log.e("ftp cambio dire fallito", Boolean.toString(resultFtpOper));
				}

				DateFormat formatter = new SimpleDateFormat("yyyyMMdd");
				String dataSyncStr = formatter.format(new Date());

				inventariTestateDao = daoSession.getInventari_testateDao();
				Inventari_testate inventari_testate = inventariTestateDao.queryBuilder().where(Inventari_testateDao.Properties.Id.eq(inventarioId)).unique();

				formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSS");
				Timestamp timestamp = new Timestamp((new Date().getTime()));
				inventari_testate.setData_conferma(formatter.format(timestamp));
				inventari_testate.update();

				String nomeFileInventariato = StringUtils.join("invent_", dataSyncStr, "_", inventari_testate.getNome_file(), ".csv");
				nomeFileInventariato = StringUtils.replace(nomeFileInventariato, ".csv.csv", ".csv");

				ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
				InputStream inputStream = new ByteArrayInputStream(inventarioUpload.getBytes());
				ftpClient.storeFile(nomeFileInventariato, inputStream);
				inputStream.close();

				ftpClient.logout();
				ftpClient.disconnect();
			}catch(MalformedURLException e){
				e.printStackTrace();
			}catch(IOException e){
				e.printStackTrace();
			}
			return "Thread Upload terminato";
		}

		@Override
		protected void onPostExecute(String result){
			super.onPostExecute(result);
			//Toast.makeText(LoginFileActivity.this, "post", Toast.LENGTH_LONG).show();
			syncAdapter.notifyDataSetChanged();
		}

		@Override
		protected void onPreExecute(){
			super.onPreExecute();
			//Toast.makeText(LoginFileActivity.this, "pre", Toast.LENGTH_LONG).show();
		}

		@Override
		protected void onProgressUpdate(Void... values){
			//Toast.makeText(LoginFileActivity.this, "progress", Toast.LENGTH_LONG).show();
		}
	}

	private void rinominaFileSorgenteFtpThread(String nomeFile){
		RinominaFileSorgenteFtpThread ut = new RinominaFileSorgenteFtpThread(nomeFile);
		try{
			ut.execute("").get();
		}catch(InterruptedException e){
			e.printStackTrace();
		}catch(ExecutionException e){
			e.printStackTrace();
		}
	}

	private class RinominaFileSorgenteFtpThread extends AsyncTask<String, Void, String>{
		private String nomeFile;

		public RinominaFileSorgenteFtpThread(String nomeFile){
			this.nomeFile = nomeFile;
		}

		@Override
		protected String doInBackground(String... params){
			try{
				Boolean resultFtpOper = false;
				ia = InetAddress.getByName(serverFtp);
				ftpClient.connect(ia, 21);
				ftpClient.login(userFtp, passwordFtp);
				ftpClient.setBufferSize(1024*1024);
				ftpClient.enterLocalPassiveMode();
				//ftpClient.storeFile("test.txt", new FileInputStream(file));
				resultFtpOper = ftpClient.changeWorkingDirectory(directoryFtp);
				if(!(resultFtpOper)){
					Log.e("ftp cambio dire fallito", Boolean.toString(resultFtpOper));
				}

				FTPFile[] ftpFiles = ftpClient.listFiles();
				for(FTPFile fileCorrente : ftpFiles){
					String nomeFileInFtp = StringUtils.trim(fileCorrente.getName());
					if(StringUtils.equals(nomeFileInFtp, nomeFile)){
						ftpClient.rename(nomeFileInFtp, "scaricato_" + nomeFile);
						break;
					}
				}
				ftpClient.logout();
				ftpClient.disconnect();
			}catch(MalformedURLException e){
				e.printStackTrace();
			}catch(IOException e){
				e.printStackTrace();
			}
			return "Thread RinominaFileSorgenteFtpThread";
		}

		@Override
		protected void onPostExecute(String result){
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute(){
			super.onPreExecute();
		}

		@Override
		protected void onProgressUpdate(Void... values){
		}
	}

	private class ScriviFileSemaforoFtpThread extends AsyncTask<String, Void, String>{
		@Override
		protected String doInBackground(String... params){
			try{
				Boolean resultFtpOper = false;
				ia = InetAddress.getByName(serverFtp);
				ftpClient.connect(ia, 21);
				ftpClient.login(userFtp, passwordFtp);
				ftpClient.setBufferSize(1024*1024);
				ftpClient.enterLocalPassiveMode();
				//ftpClient.storeFile("test.txt", new FileInputStream(file));
				resultFtpOper = ftpClient.changeWorkingDirectory(directoryFtp);
				if(!(resultFtpOper)){
					Log.e("ftp cambio dire fallito", Boolean.toString(resultFtpOper));
				}
				ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
				ftpClient.storeFile("semaforo_database", new ByteArrayInputStream(new byte[0]));

				ftpClient.logout();
				ftpClient.disconnect();
			}catch(MalformedURLException e){
				e.printStackTrace();
			}catch(IOException e){
				e.printStackTrace();
			}
			return "Thread ScriviFileSemaforoFtpThread";
		}

		@Override
		protected void onPostExecute(String result){
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute(){
			super.onPreExecute();
		}

		@Override
		protected void onProgressUpdate(Void... values){
		}
	}

	private class LeggiFileSemaforoFtpThread extends AsyncTask<String, Void, String>{
		@Override
		protected String doInBackground(String... params){
			try{
				Boolean resultFtpOper = false;
				ia = InetAddress.getByName(serverFtp);
				ftpClient.connect(ia, 21);
				ftpClient.login(userFtp, passwordFtp);
				ftpClient.setBufferSize(1024*1024);
				ftpClient.enterLocalPassiveMode();
				//ftpClient.storeFile("test.txt", new FileInputStream(file));
				resultFtpOper = ftpClient.changeWorkingDirectory(directoryFtp);
				if(!(resultFtpOper)){
					Log.e("ftp cambio dire fallito", Boolean.toString(resultFtpOper));
				}
				String fileSemaforo = "/data/data/it.cascino.inventariomerce/semaforo_database";
				OutputStream output;
				output = new FileOutputStream(fileSemaforo);
				ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
				File f = new File(fileSemaforo);
				f.delete();
				resultFtpOper = ftpClient.retrieveFile("semaforo_database", output);
				if(resultFtpOper){
					fileSemaforoPresente = true;
				}else{
					fileSemaforoPresente = false;
				}

				ftpClient.logout();
				ftpClient.disconnect();
			}catch(MalformedURLException e){
				e.printStackTrace();
			}catch(IOException e){
				e.printStackTrace();
			}
			return "Thread LeggiFileSemaforoFtpThread";
		}

		@Override
		protected void onPostExecute(String result){
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute(){
			super.onPreExecute();
		}

		@Override
		protected void onProgressUpdate(Void... values){
		}
	}

	private void leggiInvetariDaDB(){
		inventariTestateDao = daoSession.getInventari_testateDao();
		inventariDettagliDao = daoSession.getInventari_dettagliDao();
		List<Inventari_testate> inventari_testateLs = null;
		inventari_testateLs = inventariTestateDao.queryBuilder().where(Inventari_testateDao.Properties.Iddep.eq(deposito), Inventari_testateDao.Properties.Utente_destinatario.eq(operatore), Inventari_testateDao.Properties.Data_conferma.isNull()).list();
		for(int i = 0, s = inventari_testateLs.size(); i < s; i++){
			Inventari_testate inventari_testate = inventari_testateLs.get(i);
			Inventario invent = new Inventario();
			invent.setSorgenteFile(false);
			invent.setNomeFile(inventari_testate.getNome_file());
			invent.setUtenteDestinatario(operatore);
			invent.setDeposito(deposito);
			invent.setProgressivo(inventari_testate.getId().intValue());
			invent.setUtenteCreatore(inventari_testate.getUtente_creatore());

			Long numAr = inventariDettagliDao.queryBuilder().where(Inventari_dettagliDao.Properties.Idtestata.eq(inventari_testate.getId())).count();
			invent.setNumeroArticoliTotale(numAr.intValue());
			numAr = inventariDettagliDao.queryBuilder().where(Inventari_dettagliDao.Properties.Idtestata.eq(inventari_testate.getId()), Inventari_dettagliDao.Properties.Stato.notEq(TipoStato.DA_INVENTARIARE)).count();
			invent.setNumeroArticoliInventariati(numAr.intValue());

			List<Articolo> articoliLs = new ArrayList<Articolo>();

			List<Inventari_dettagli> inventari_dettagliLs = null;
			inventari_dettagliLs = inventariDettagliDao.queryBuilder().where(Inventari_dettagliDao.Properties.Idtestata.eq(inventari_testate.getId())).list();
			for(int j = 0, sj = inventari_dettagliLs.size(); j < sj; j++){
				Inventari_dettagli inventari_dettagli = inventari_dettagliLs.get(j);
				Articoli articoli = inventari_dettagli.getArticoli();

				Articolo art = new Articolo();
				art.setOrdinamento(inventari_dettagli.getPosizioneinlista());

				art.setCodice(articoli.getCodart());

				articoliLs.add(art);
			}

			invent.setArticoliLs(articoliLs);

			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSS");
			Date date = null;
			try{
				date = (Date)formatter.parse(inventari_testate.getData_creazione());
				invent.setTimeCreazione(new Timestamp(date.getTime()));
			}catch(ParseException e){
				invent.setTimeCreazione(null);
			}catch(NullPointerException e){
				invent.setTimeCreazione(null);
			}
			try{
				date = (Date)formatter.parse(inventari_testate.getData_modifica());
				invent.setTimeModifica(new Timestamp(date.getTime()));
			}catch(ParseException e){
				invent.setTimeModifica(null);
			}catch(NullPointerException e){
				invent.setTimeModifica(null);
			}
			invent.setCommento(inventari_testate.getCommento());
			invent.setStato(inventari_testate.getStato());

			inventariLs.add(invent);
		}
	}

	private void inserisciInvetarioInDB(){
		inventariTestateDao = daoSession.getInventari_testateDao();
		Inventari_testate inventariTestate = new Inventari_testate();
		inventariTestate.setUtente_creatore(inventarioSel.getUtenteCreatore());
		inventariTestate.setUtente_destinatario(inventarioSel.getUtenteDestinatario());
		inventariTestate.setData_creazione(inventarioSel.getTimeCreazione().toString());
		inventariTestate.setData_modifica(null);
		inventariTestate.setData_conferma(null);
		inventariTestate.setStato(inventarioSel.getStato());
		inventariTestate.setCommento(inventarioSel.getCommento());
		inventariTestate.setNome_file(inventarioSel.getNomeFile());
		depositiDao = daoSession.getDepositiDao();
		Depositi depositi = depositiDao.queryBuilder().where(DepositiDao.Properties.Iddep.eq(StringUtils.right("00" + inventarioSel.getDeposito(), 2))).unique();
		Long idDeposito = depositi.getId();
		inventariTestate.setDepositi(depositi);
		inventariTestateDao.insert(inventariTestate);
		Long idTestata = inventariTestate.getId();
		Log.i("ins id testata", Long.toString(idTestata));

		relArticoliBarcodeDao = daoSession.getRel_articoli_barcodeDao();

		// aggiorno il progressivo dell'invetario che e' attualmente il progressivo del file, con l'id della testata appena inserita
		inventarioSel.setProgressivo(idTestata.intValue());

		inventariDettagliDao = daoSession.getInventari_dettagliDao();
		Inventari_dettagli inventariDettagli = null;
		Iterator<Articolo> iter_articoliLs = inventarioSel.getArticoliLs().iterator();
		Articolo art = null;
		while(iter_articoliLs.hasNext()){
			art = iter_articoliLs.next();
			articoliDao = daoSession.getArticoliDao();
			Articoli articoli = null;
			try{
				articoli = articoliDao.queryBuilder().where(ArticoliDao.Properties.Codart.eq(art.getCodice())).unique();
			}catch(IndexOutOfBoundsException e){
				Log.e("Art non in anag ", art.getCodice());
				iter_articoliLs.remove();
			}
			if(articoli == null){
				iter_articoliLs.remove();
				continue;
			}
			// Log.i("Articolo in DB", articoli.getCodart() + " " + articoli.getDesc());

			inventariDettagli = new Inventari_dettagli();
			inventariDettagli.setIdtestata(idTestata);
			inventariDettagli.setIdart(articoli.getId());

			inventariDettagli.setQty_esposta(null);
			inventariDettagli.setQty_magaz(null);
			inventariDettagli.setQty_difettosi(null);
			inventariDettagli.setQty_scorta_min(null);
			inventariDettagli.setQty_scorta_max(null);
			inventariDettagli.setPrezzo(null);
			inventariDettagli.setCommento(null);
			inventariDettagli.setStato(String.valueOf(TipoStato.DA_INVENTARIARE));
			inventariDettagli.setTimestamp(inventarioSel.getTimeCreazione().toString());
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
			Log.i("ins id inventDettagli", Long.toString(idInventariDettagli));
		}
	}

	public void savePreferencesData(){
		SharedPreferences.Editor editor = sharedPreferences.edit();
		if(depositoEditText != null){
			editor.putString("deposito", depositoEditText.getText().toString());
		}
		if(operatoreEditText != null){
			editor.putString("operatore", StringUtils.upperCase(operatoreEditText.getText().toString()));
		}
		editor.apply();
		updatePreferencesData();
	}

	private void updatePreferencesData(){
		if(sharedPreferences != null){
			deposito = sharedPreferences.getString("deposito", "0");
			operatore = sharedPreferences.getString("operatore", "nd");
		}
	}
}
