package it.cascino.smarcamentomerce;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

import it.cascino.smarcamentomerce.adapter.ArticoloAdapter;
import it.cascino.smarcamentomerce.it.cascino.smarcamentomerce.model.Barcode;

import it.cascino.smarcamentomerce.it.cascino.smarcamentomerce.model.Articolo;
import it.cascino.smarcamentomerce.it.cascino.smarcamentomerce.model.FileDaAs;

public class MainActivity extends Activity{
	private List<Articolo> articoliLs = new ArrayList<Articolo>();
	private ArticoloAdapter adapterArticoliLs;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		TextView numDep = (TextView)findViewById(R.id.numDep);
		numDep.setText("02");
		TextView dataSync = (TextView)findViewById(R.id.dataSync);
		DateFormat formatter = new SimpleDateFormat("HH:mm.ss - dd/MM/yyyy");
		String dataSyncStr = formatter.format(new Date());
		dataSync.setText(dataSyncStr);

	   /*
		final List<Articolo> articoliLs = new ArrayList<Articolo>();
		Barcode bcode[] = new Barcode[1];
		bcode[0] = new Barcode("12454", "ean13");
		articoliLs.add(new Articolo("cod 1", bcode, "desc 1", "PZ", 3.25f, 0.0f, "00/00/00", "01/08/15", 2, null));
		articoliLs.add(new Articolo("cod 2", bcode, "desc 2", "PZ", 0.25f, 0.0f, "00/00/00", "01/08/15", 2, null));
		articoliLs.add(new Articolo("cod 3", bcode, "desc 3", "PZ", 10f, 0.0f, "00/00/00", "01/08/15", 2, null));
		*/

		Button syncButton = (Button)findViewById(R.id.syncButton);
		syncButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				new DownloadThread().execute("");
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

		// abilito il filtro
		listViewArticoliLs.setTextFilterEnabled(true);

		final EditText myFilter = (EditText)findViewById(R.id.myFilter);
		myFilter.addTextChangedListener(new TextWatcher(){
			public void afterTextChanged(Editable s){
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after){
			}

			public void onTextChanged(CharSequence s, int start, int before, int count){
				adapterArticoliLs.getFilter().filter(s.toString());
			}
		});
		myFilter.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				myFilter.setText("");
			}
		});

	}

	private class DownloadThread extends AsyncTask<String, Void, String>{
		@Override
		protected String doInBackground(String... params){
			String dep = "02";
			String usr = "AGORIN";
			String userFtp = "androidftp";
			String passwordFtp = "androidftp";
			String directoryFtp = "/";
			String filenameFtp = "inventario_dep" + dep + "_" + usr + ".csv";
			//String filenameFtp = "a.csv";
			try{
				// URL url = new URL("ftp://" + userFtp + ":" + passwordFtp + "@" + "ftp1.cascino.it" + directoryFtp + filenameFtp);
				//URLConnection conn = url.openConnection();
				//InputStream inputStream = conn.getInputStream();

				// controllare se la lista e' gia' modificata, in caso non la devo sync fino a quando non faccio l'upload o annullamento
				articoliLs.clear();

				FTPClient ftpClient = new FTPClient();
				InetAddress ia = InetAddress.getByName("ftp1.cascino.it");
				ftpClient.connect(ia, 21);
				ftpClient.login(userFtp, passwordFtp);
				ftpClient.enterLocalPassiveMode();
				//ftpClient.storeFile("test.txt", new FileInputStream(file));
				ftpClient.changeWorkingDirectory(directoryFtp);
				InputStream inputStream = ftpClient.retrieveFileStream(filenameFtp);
				InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF8");
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

				// Toast.makeText(MainActivity.this, "collegato", Toast.LENGTH_LONG).show();

				String lineRead = "";
				// List<Articolo> articoliLs = null;
				FileDaAs fileDaAs = new FileDaAs();
				// salto le prime due righe che sono di intestazione
				lineRead = bufferedReader.readLine();
				lineRead = bufferedReader.readLine();
				while((lineRead = bufferedReader.readLine()) != null){
					Articolo art = new Articolo();
					//String campiSlit[] = lineRead.split("\\" + fileDaAs.getCampiSep());
					String campiSlit[] = StringUtils.split(lineRead, fileDaAs.getCampiSep());
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
					art.setStato(Integer.parseInt(campiSlit[11]));
					DateFormat formatter = new SimpleDateFormat("ddMMyyyyHHmmss");
					try{
						Date date = (Date)formatter.parse(campiSlit[12]);
						art.setTimestamp(new Timestamp(date.getTime()));
					}catch(ParseException e){
						e.printStackTrace();
					}
					articoliLs.add(art);
				}
				//fileDaAs.setArticoliLs(articoliLs);
				ftpClient.logout();
				ftpClient.disconnect();
			}catch(MalformedURLException e){
				e.printStackTrace();
			}catch(IOException e){
				e.printStackTrace();
			}

			adapterArticoliLs.setArticoliOriginaleLs(articoliLs);
			//adapterArticoliLs.setArticoliLs(articoliLs);

			return "Thread terminato";
		}

		@Override
		protected void onPostExecute(String result){
			super.onPostExecute(result);
			Toast.makeText(MainActivity.this, "post", Toast.LENGTH_LONG).show();
			//findViewById(R.id.articoliList).invalidate();
			adapterArticoliLs.notifyDataSetChanged();
		}

		@Override
		protected void onPreExecute(){
			super.onPreExecute();
			Toast.makeText(MainActivity.this, "pre", Toast.LENGTH_LONG).show();
		}

		@Override
		protected void onProgressUpdate(Void... values){
			Toast.makeText(MainActivity.this, "progress", Toast.LENGTH_LONG).show();
		}
	}
}
