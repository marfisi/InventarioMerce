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

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import java.util.List;
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

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_file);

		sharedPreferences = getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
		updatePreferencesData();

		final Intent intentLoginFileActivity = getIntent();
		String nomeParametro = intentLoginFileActivity.getStringExtra("nomeParametro");
		Log.i("parametro intent", nomeParametro);

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
			}
		});

		ListView listViewFileDaAsLs = (ListView)findViewById(R.id.fileList);
		listViewFileDaAsLs.setOnItemClickListener(new AdapterView.OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id){
				fileDaAsSel = fileLs.get(position);
				Toast.makeText(getApplicationContext(), "Sel: " + fileDaAsSel.getNomeFile(), Toast.LENGTH_LONG).show();
			}
		});
		fileDaAsAdapter = new FileDaAsAdapter(getApplicationContext(), fileLs);
		listViewFileDaAsLs.setAdapter(fileDaAsAdapter);

		Button caricaButton = (Button)findViewById(R.id.caricaButton);
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
	}

	private class DownloadThread extends AsyncTask<String, Void, String>{
		@Override
		protected String doInBackground(String... params){
			//String dep = "02";
			//String usr = "AGORIN";
			String userFtp = "androidftp";
			String passwordFtp = "androidftp";
			String directoryFtp = "/";
			//String filenameFtp = "inventario_dep" + dep + "_" + usr + ".csv";
			try{
				// URL url = new URL("ftp://" + userFtp + ":" + passwordFtp + "@" + "ftp1.cascino.it" + directoryFtp + filenameFtp);
				//URLConnection conn = url.openConnection();
				//InputStream inputStream = conn.getInputStream();

				// controllare se la lista e' gia' modificata, in caso non la devo sync fino a quando non faccio l'upload o annullamento
				fileLs.clear();

				FTPClient ftpClient = new FTPClient();
				InetAddress ia = InetAddress.getByName("ftp1.cascino.it");
				ftpClient.connect(ia, 21);
				ftpClient.login(userFtp, passwordFtp);
				ftpClient.enterLocalPassiveMode();
				//ftpClient.storeFile("test.txt", new FileInputStream(file));
				ftpClient.changeWorkingDirectory(directoryFtp);

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

						DecimalFormatSymbols symbols = new DecimalFormatSymbols();
						symbols.setDecimalSeparator(',');
						DecimalFormat format = new DecimalFormat();
						format.setDecimalFormatSymbols(symbols);
						Float f = 0.0f;
						try{
							f = format.parse(campiSlit[4]).floatValue();
						}catch(ParseException e){
							e.printStackTrace();
						}
						art.setQtyOriginale(f); // Float.parseFloat(campiSlit[4]));
						try{
							f = format.parse(campiSlit[5]).floatValue();
						}catch(ParseException e){
							e.printStackTrace();
						}
						art.setQtyRilevata(f);
						try{
							f = format.parse(campiSlit[6]).floatValue();
						}catch(ParseException e){
							e.printStackTrace();
						}
						art.setQtyDifettOriginale(f);
						try{
							f = format.parse(campiSlit[7]).floatValue();
						}catch(ParseException e){
							e.printStackTrace();
						}
						art.setQtyDifettRilevata(f);
						art.setDataCarico(campiSlit[8]);
						art.setDataScarico(campiSlit[9]);
						art.setDataUltimoInventario(campiSlit[10]);
						try{
							f = format.parse(campiSlit[11]).floatValue();
						}catch(ParseException e){
							e.printStackTrace();
						}
						art.setScortaMinOriginale(f);
						try{
							f = format.parse(campiSlit[12]).floatValue();
						}catch(ParseException e){
							e.printStackTrace();
						}
						art.setScortaMinRilevata(f);
						try{
							f = format.parse(campiSlit[13]).floatValue();
						}catch(ParseException e){
							e.printStackTrace();
						}
						art.setScortaMaxOriginale(f);
						try{
							f = format.parse(campiSlit[14]).floatValue();
						}catch(ParseException e){
							e.printStackTrace();
						}
						art.setScortaMaxRilevata(f);
						art.setCommento(campiSlit[15]);
						art.setStato(Integer.parseInt(campiSlit[16]));
						DateFormat formatter = new SimpleDateFormat("ddMMyyyyHHmmss");
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

			return "Thread terminato";
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
