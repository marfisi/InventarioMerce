package it.cascino.smarcamentomerce.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import it.cascino.smarcamentomerce.R;

import it.cascino.smarcamentomerce.adapter.FileDaAsAdapter;
import it.cascino.smarcamentomerce.model.Articolo;
import it.cascino.smarcamentomerce.model.Barcode;
import it.cascino.smarcamentomerce.model.FileDaAs;

public class LoginFileActivity extends Activity{
	private List<FileDaAs> fileLs = new ArrayList<FileDaAs>();
	private FileDaAsAdapter fileDaAsAdapter;

	private FileDaAs fileDaAsSel;

	private String SHARED_PREF = "shared_pref_inventario";
	private SharedPreferences sharedPreferences;
	private EditText depositoEditText;
	private EditText operatoreEditText;

	private String deposito = "";
	private String operatore = "";

	private final String serverFtp = "ftp1.cascino.it";
	private final String userFtp = "androidftp";
	private final String passwordFtp = "androidftp";
	private final String directoryFtp = "/";
	private FTPClient ftpClient = new FTPClient();
	private InetAddress ia;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_file);

		sharedPreferences = getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
		updatePreferencesData();

		/*final Intent intentLoginFileActivity = getIntent();
		String nomeParametro = intentLoginFileActivity.getStringExtra("nomeParametro");
		Log.i("parametro intent", nomeParametro);*/

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
				/*depositoEditText.setText(deposito);
				operatoreEditText.setText(operatore);*/
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after){
			}

			public void onTextChanged(CharSequence s, int start, int before, int count){
			}
		});

		final Button caricaButton = (Button)findViewById(R.id.caricaButton);

		Button leggiButton = (Button)findViewById(R.id.leggiButton);
		leggiButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				DownloadThread dt = new DownloadThread();
				try{
					dt.execute("").get();
				}catch(InterruptedException e){
					e.printStackTrace();
				}catch(ExecutionException e){
					e.printStackTrace();
				}
				caricaButton.setEnabled(false);
			}
		});

		ListView listViewFileDaAsLs = (ListView)findViewById(R.id.fileList);
		listViewFileDaAsLs.setOnItemClickListener(new AdapterView.OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id){
				fileDaAsSel = fileLs.get(position);
				Toast.makeText(getApplicationContext(), "Sel: " + fileDaAsSel.getNomeFile(), Toast.LENGTH_LONG).show();
				caricaButton.setEnabled(true);	// e' false da xml
			}
		});
		fileDaAsAdapter = new FileDaAsAdapter(getApplicationContext(), fileLs);
		listViewFileDaAsLs.setAdapter(fileDaAsAdapter);

		caricaButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				Intent resultIntent = new Intent();
				resultIntent.putExtra("nomeFile", fileDaAsSel.getNomeFile());
				//resultIntent.putExtra("numeroArticoli", fileDaAsSel.getNumeroArticoli());
				//resultIntent.putExtra("fileDaAsSel", fileDaAsSel);
				Gson gSon = new Gson();
				String gSonString = gSon.toJson(fileDaAsSel);
				resultIntent.putExtra("fileDaAsSel", gSonString);
				setResult(Activity.RESULT_OK, resultIntent);
				finish();
			}
		});

		Intent intentFtpUpload = getIntent();
		if(intentFtpUpload != null){
			String gSonString = intentFtpUpload.getStringExtra("articoliLs");
			String nomeFile = intentFtpUpload.getStringExtra("nomeFile");
			if(StringUtils.isNotEmpty(gSonString)){
				Gson gSon = new Gson();
				Type listType = new TypeToken<ArrayList<Articolo>>(){
				}.getType();
				List<Articolo> articoliLsDaSalv = gSon.fromJson(gSonString, listType);
				UploadThread ut = new UploadThread(articoliLsDaSalv, nomeFile);
				try{
					ut.execute("").get();
				}catch(InterruptedException e){
					e.printStackTrace();
				}catch(ExecutionException e){
					e.printStackTrace();
				}
				finish();
			}
		}
	}

	private class DownloadThread extends AsyncTask<String, Void, String>{
		@Override
		protected String doInBackground(String... params){
			try{
				fileLs.clear();

				Boolean resultFtpOper = false;
				ia = InetAddress.getByName(serverFtp);
				ftpClient.connect(ia, 21);
				ftpClient.login(userFtp, passwordFtp);
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

					FileDaAs fileDaAs = new FileDaAs();
					fileDaAs.setNomeFile(nomeFile);
					fileDaAs.setUtente(operatore);
					fileDaAs.setDeposito(deposito);
					// determino il progressivo del file, che e' l'ultimo numero a destra
					fileDaAs.setProgressivo(Integer.parseInt(StringUtils.substringBefore(StringUtils.substringAfterLast(nomeFile, "_"), ".")));

					InputStream inputStream = ftpClient.retrieveFileStream(nomeFile);
					if(inputStream == null){
						Log.e("inputStream", "inputStream e' nullo");
						Log.i("getReplyCode", String.valueOf(ftpClient.getReplyCode()));
						Log.e("getReplyString", ftpClient.getReplyString());
					}
					InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF8");
					BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

					Integer numRigoArt = 0;
					String lineRead = "";
					List<Articolo> articoliLs = new ArrayList<Articolo>();
					// salto le prime due righe che sono di intestazione
					lineRead = bufferedReader.readLine();
					lineRead = bufferedReader.readLine();
					while((lineRead = bufferedReader.readLine()) != null){
						numRigoArt++;
						Articolo art = new Articolo();
						art.setOrdinamento(numRigoArt);
						String campiSlit[] = StringUtils.splitByWholeSeparatorPreserveAllTokens(lineRead, fileDaAs.getCampiSep());
						art.setCodice(campiSlit[0]);
						String barcodeLine[] = campiSlit[1].split(fileDaAs.getInsideCampSep());
						Barcode barcode[] = new Barcode[barcodeLine.length];
						for(int b = 0; b < barcodeLine.length; b++){
							barcode[b] = new Barcode(barcodeLine[b], "ean13");
						}
						art.setBarcode(barcode);
						art.setDesc(campiSlit[2]);
						art.setUm(campiSlit[3]);

						DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.ITALY);
						symbols.setDecimalSeparator(fileDaAs.getDecimalSep().charAt(0));
						DecimalFormat decimalFormat = new DecimalFormat("#.##", symbols);
						//decimalFormat.setDecimalFormatSymbols(symbols);
						Float f = 0.0f;
						try{
							f = decimalFormat.parse(campiSlit[4]).floatValue();
						}catch(ParseException e){
							e.printStackTrace();
						}
						art.setQtyOriginale(f); // Float.parseFloat(campiSlit[4]));
						try{
							f = decimalFormat.parse(campiSlit[5]).floatValue();
						}catch(ParseException e){
							e.printStackTrace();
						}
						art.setQtyRilevata(f);
						try{
							f = decimalFormat.parse(campiSlit[6]).floatValue();
						}catch(ParseException e){
							e.printStackTrace();
						}
						art.setQtyDifettOriginale(f);
						try{
							f = decimalFormat.parse(campiSlit[7]).floatValue();
						}catch(ParseException e){
							e.printStackTrace();
						}
						art.setQtyDifettRilevata(f);
						art.setDataCarico(campiSlit[8]);
						art.setDataScarico(campiSlit[9]);
						art.setDataUltimoInventario(campiSlit[10]);
						try{
							f = decimalFormat.parse(campiSlit[11]).floatValue();
						}catch(ParseException e){
							e.printStackTrace();
						}
						art.setScortaMinOriginale(f);
						try{
							f = decimalFormat.parse(campiSlit[12]).floatValue();
						}catch(ParseException e){
							e.printStackTrace();
						}
						art.setScortaMinRilevata(f);
						try{
							f = decimalFormat.parse(campiSlit[13]).floatValue();
						}catch(ParseException e){
							e.printStackTrace();
						}
						art.setScortaMaxOriginale(f);
						try{
							f = decimalFormat.parse(campiSlit[14]).floatValue();
						}catch(ParseException e){
							e.printStackTrace();
						}
						art.setScortaMaxRilevata(f);
						art.setCommento(campiSlit[15]);
						art.setStato(Integer.parseInt(campiSlit[16]));
						DateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
						try{
							Date date = (Date)formatter.parse(campiSlit[17]);
							art.setTimestamp(new Timestamp(date.getTime()));
						}catch(ParseException e){
							e.printStackTrace();
						}
						articoliLs.add(art);
					}
					ftpClient.completePendingCommand();
					inputStream.close();
					inputStreamReader.close();
					bufferedReader.close();

					fileDaAs.setNumeroArticoli(numRigoArt);
					fileDaAs.setArticoliLs(articoliLs);

					fileLs.add(fileDaAs);
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

			return "Thread Download terminato";
		}

		@Override
		protected void onPostExecute(String result){
			super.onPostExecute(result);
			Toast.makeText(LoginFileActivity.this, "post", Toast.LENGTH_LONG).show();
			fileDaAsAdapter.notifyDataSetChanged();
		}

		@Override
		protected void onPreExecute(){
			super.onPreExecute();
			Toast.makeText(LoginFileActivity.this, "pre", Toast.LENGTH_LONG).show();
		}

		@Override
		protected void onProgressUpdate(Void... values){
			Toast.makeText(LoginFileActivity.this, "progress", Toast.LENGTH_LONG).show();
		}
	}

	private class UploadThread extends AsyncTask<String, Void, String>{
		private List<Articolo> articoliLsDaSalv;
		private String nomeFile;

		public UploadThread(List<Articolo> articoliLsDaSalv, String nomeFile){
			this.articoliLsDaSalv = articoliLsDaSalv;
			this.nomeFile = nomeFile;
		}

		@Override
		protected String doInBackground(String... params){
			try{
				Boolean resultFtpOper = false;
				ia = InetAddress.getByName(serverFtp);
				ftpClient.connect(ia, 21);
				ftpClient.login(userFtp, passwordFtp);
				ftpClient.enterLocalPassiveMode();
				resultFtpOper = ftpClient.changeWorkingDirectory(directoryFtp);
				if(!(resultFtpOper)){
					Log.e("ftp cambio direc fallit", Boolean.toString(resultFtpOper));
				}

				DateFormat formatter = new SimpleDateFormat("yyyyMMdd");
				String dataSyncStr = formatter.format(new Date());

				String nomeFileOriginale = StringUtils.join(dataSyncStr, "_orig_", nomeFile);
				String nomeFileInventariato = StringUtils.join(dataSyncStr, "_invent_", nomeFile);

				resultFtpOper = ftpClient.rename(nomeFile, nomeFileOriginale);
				if(!(resultFtpOper)){
					Log.e("ftp rename file", Boolean.toString(resultFtpOper));
				}

				StringBuilder stringBuilder = new StringBuilder();

				Iterator<Articolo> iter_articoliLs = articoliLsDaSalv.iterator();
				Articolo art = null;
				while(iter_articoliLs.hasNext()){
					art = iter_articoliLs.next();
					stringBuilder.append(art.toStringPerFtpFile()).append("\n");
				}

				ftpClient.setFileType(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
				InputStream inputStream = new ByteArrayInputStream(stringBuilder.toString().getBytes());
				ftpClient.storeFile(nomeFileInventariato, inputStream);
				inputStream.close();

				//ftpClient.completePendingCommand();

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
			Toast.makeText(LoginFileActivity.this, "post", Toast.LENGTH_LONG).show();
			fileDaAsAdapter.notifyDataSetChanged();
		}

		@Override
		protected void onPreExecute(){
			super.onPreExecute();
			Toast.makeText(LoginFileActivity.this, "pre", Toast.LENGTH_LONG).show();
		}

		@Override
		protected void onProgressUpdate(Void... values){
			Toast.makeText(LoginFileActivity.this, "progress", Toast.LENGTH_LONG).show();
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
