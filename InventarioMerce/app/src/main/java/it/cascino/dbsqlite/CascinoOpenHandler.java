package it.cascino.dbsqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/*
Dopo la rigenerazione delle classi con GreenDao, devo modificare la classe DaoMaster.OpneHelper.onCreate (rigo #95)

            // createAllTables(db, false);
            createAllTables(db, true);

il true forza il "if not exist" nella creazione delle tabelle
 */

public class CascinoOpenHandler extends DaoMaster.OpenHelper{
	public CascinoOpenHandler(Context context, String name, SQLiteDatabase.CursorFactory factory){
		super(context, name, factory);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
		Log.i("greenDAO", "Upgrading schema from version " + oldVersion + " to " + newVersion);
	}
}
