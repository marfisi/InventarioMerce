package it.cascino.smarcamentomerce.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import java.util.Iterator;
import java.util.List;

import it.cascino.dbsqlite.Articoli;
import it.cascino.dbsqlite.ArticoliDao;
import it.cascino.dbsqlite.Barcode;
import it.cascino.dbsqlite.BarcodeDao;
import it.cascino.dbsqlite.CascinoOpenHandler;
import it.cascino.dbsqlite.DaoMaster;
import it.cascino.dbsqlite.DaoSession;
import it.cascino.dbsqlite.Rel_articoli_barcode;
import it.cascino.dbsqlite.Rel_articoli_barcodeDao;
import it.cascino.smarcamentomerce.R;

public class AggiungiArticoloDaBarcodeActivity extends Activity{
	final private String MYDATABASE_NAME = "cascinoinventario.db";
	private CascinoOpenHandler helper = new CascinoOpenHandler(AggiungiArticoloDaBarcodeActivity.this, MYDATABASE_NAME, null);
	private SQLiteDatabase db;
	private DaoMaster daoMaster;
	private DaoSession daoSession;
	private ArticoliDao articoliDao;
	private BarcodeDao barcodeDao;
	private Rel_articoli_barcodeDao relArticoliBarcodeDao;

	private TextView art_codart;
	private TextView art_desc;
	private ImageButton btnY;
	private ImageButton btnN;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_aggiungiarticolodabarcode);

		helper.onUpgrade(db, 1, 2);
		db = helper.getWritableDatabase();
		daoMaster = new DaoMaster(db);
		daoSession = daoMaster.newSession();

		art_codart = (TextView)findViewById(R.id.art_codart);
		art_desc = (TextView)findViewById(R.id.art_desc);

		Intent intentAggiungiArticoloDaBarcodeActivity = getIntent();
		if(intentAggiungiArticoloDaBarcodeActivity != null){
			String bcode = intentAggiungiArticoloDaBarcodeActivity.getStringExtra("barcode");
			String codArt = intentAggiungiArticoloDaBarcodeActivity.getStringExtra("codArt");
			if(StringUtils.isNotEmpty(bcode)){
				barcodeDao = daoSession.getBarcodeDao();
				Barcode barcode = barcodeDao.queryBuilder().where(BarcodeDao.Properties.Codice.eq(bcode)).unique();
				if(barcode != null){
					relArticoliBarcodeDao = daoSession.getRel_articoli_barcodeDao();
					List rel_articoli_barcodeLs = relArticoliBarcodeDao.queryBuilder().where(Rel_articoli_barcodeDao.Properties.Idbc.eq(barcode.getId())).list();
					Iterator<Rel_articoli_barcode> iter_rel_articoli_barcodeLs = rel_articoli_barcodeLs.iterator();
					Rel_articoli_barcode art = null;
					while(iter_rel_articoli_barcodeLs.hasNext()){
						art = iter_rel_articoli_barcodeLs.next();
						articoliDao = daoSession.getArticoliDao();
						Articoli articoli = articoliDao.queryBuilder().where(ArticoliDao.Properties.Id.eq(art.getIdart())).unique();
						art_codart.setText(articoli.getCodart());
						art_desc.setText(articoli.getDesc());
					}
				}
			}else if(StringUtils.isNotEmpty(codArt)){
				articoliDao = daoSession.getArticoliDao();
				Articoli articoli = articoliDao.queryBuilder().where(ArticoliDao.Properties.Codart.eq(codArt)).unique();
				art_codart.setText(articoli.getCodart());
				art_desc.setText(articoli.getDesc());
			}
		}

		final Intent resultIntent = new Intent();

		btnY = (ImageButton)findViewById(R.id.btnY);
		btnY.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				resultIntent.putExtra("aggiungi", "Y");
				resultIntent.putExtra("codiceArticolo", art_codart.getText());
				setResult(Activity.RESULT_OK, resultIntent);
				finish();
			}
		});

		btnN = (ImageButton)findViewById(R.id.btnN);
		btnN.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				resultIntent.putExtra("aggiungi", "N");
				setResult(Activity.RESULT_OK, resultIntent);
				finish();
			}
		});
	}
}
