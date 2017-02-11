package it.cascino.dbsqlite;

import java.util.List;
import java.util.ArrayList;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.SqlUtils;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "INVENTARI_DETTAGLI".
*/
public class Inventari_dettagliDao extends AbstractDao<Inventari_dettagli, Long> {

    public static final String TABLENAME = "INVENTARI_DETTAGLI";

    /**
     * Properties of entity Inventari_dettagli.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Qty_esposta = new Property(1, Float.class, "qty_esposta", false, "QTY_ESPOSTA");
        public final static Property Qty_magaz = new Property(2, Float.class, "qty_magaz", false, "QTY_MAGAZ");
        public final static Property Qty_difettosi = new Property(3, Float.class, "qty_difettosi", false, "QTY_DIFETTOSI");
        public final static Property Qty_scorta_min = new Property(4, Float.class, "qty_scorta_min", false, "QTY_SCORTA_MIN");
        public final static Property Qty_scorta_max = new Property(5, Float.class, "qty_scorta_max", false, "QTY_SCORTA_MAX");
        public final static Property Prezzo = new Property(6, Float.class, "prezzo", false, "PREZZO");
        public final static Property Commento = new Property(7, String.class, "commento", false, "COMMENTO");
        public final static Property Stato = new Property(8, String.class, "stato", false, "STATO");
        public final static Property Timestamp = new Property(9, String.class, "timestamp", false, "TIMESTAMP");
        public final static Property Desc = new Property(10, String.class, "desc", false, "DESC");
        public final static Property Posizioneinlista = new Property(11, int.class, "posizioneinlista", false, "POSIZIONEINLISTA");
        public final static Property Qty_per_confez = new Property(12, Float.class, "qty_per_confez", false, "QTY_PER_CONFEZ");
        public final static Property Barcode = new Property(13, String.class, "barcode", false, "BARCODE");
        public final static Property Idtestata = new Property(14, long.class, "idtestata", false, "IDTESTATA");
        public final static Property Idart = new Property(15, long.class, "idart", false, "IDART");
    }

    private DaoSession daoSession;


    public Inventari_dettagliDao(DaoConfig config) {
        super(config);
    }
    
    public Inventari_dettagliDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"INVENTARI_DETTAGLI\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"QTY_ESPOSTA\" REAL," + // 1: qty_esposta
                "\"QTY_MAGAZ\" REAL," + // 2: qty_magaz
                "\"QTY_DIFETTOSI\" REAL," + // 3: qty_difettosi
                "\"QTY_SCORTA_MIN\" REAL," + // 4: qty_scorta_min
                "\"QTY_SCORTA_MAX\" REAL," + // 5: qty_scorta_max
                "\"PREZZO\" REAL," + // 6: prezzo
                "\"COMMENTO\" TEXT," + // 7: commento
                "\"STATO\" TEXT NOT NULL ," + // 8: stato
                "\"TIMESTAMP\" TEXT NOT NULL ," + // 9: timestamp
                "\"DESC\" TEXT," + // 10: desc
                "\"POSIZIONEINLISTA\" INTEGER NOT NULL ," + // 11: posizioneinlista
                "\"QTY_PER_CONFEZ\" REAL," + // 12: qty_per_confez
                "\"BARCODE\" TEXT," + // 13: barcode
                "\"IDTESTATA\" INTEGER NOT NULL ," + // 14: idtestata
                "\"IDART\" INTEGER NOT NULL );"); // 15: idart
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"INVENTARI_DETTAGLI\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Inventari_dettagli entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Float qty_esposta = entity.getQty_esposta();
        if (qty_esposta != null) {
            stmt.bindDouble(2, qty_esposta);
        }
 
        Float qty_magaz = entity.getQty_magaz();
        if (qty_magaz != null) {
            stmt.bindDouble(3, qty_magaz);
        }
 
        Float qty_difettosi = entity.getQty_difettosi();
        if (qty_difettosi != null) {
            stmt.bindDouble(4, qty_difettosi);
        }
 
        Float qty_scorta_min = entity.getQty_scorta_min();
        if (qty_scorta_min != null) {
            stmt.bindDouble(5, qty_scorta_min);
        }
 
        Float qty_scorta_max = entity.getQty_scorta_max();
        if (qty_scorta_max != null) {
            stmt.bindDouble(6, qty_scorta_max);
        }
 
        Float prezzo = entity.getPrezzo();
        if (prezzo != null) {
            stmt.bindDouble(7, prezzo);
        }
 
        String commento = entity.getCommento();
        if (commento != null) {
            stmt.bindString(8, commento);
        }
        stmt.bindString(9, entity.getStato());
        stmt.bindString(10, entity.getTimestamp());
 
        String desc = entity.getDesc();
        if (desc != null) {
            stmt.bindString(11, desc);
        }
        stmt.bindLong(12, entity.getPosizioneinlista());
 
        Float qty_per_confez = entity.getQty_per_confez();
        if (qty_per_confez != null) {
            stmt.bindDouble(13, qty_per_confez);
        }
 
        String barcode = entity.getBarcode();
        if (barcode != null) {
            stmt.bindString(14, barcode);
        }
        stmt.bindLong(15, entity.getIdtestata());
        stmt.bindLong(16, entity.getIdart());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Inventari_dettagli entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Float qty_esposta = entity.getQty_esposta();
        if (qty_esposta != null) {
            stmt.bindDouble(2, qty_esposta);
        }
 
        Float qty_magaz = entity.getQty_magaz();
        if (qty_magaz != null) {
            stmt.bindDouble(3, qty_magaz);
        }
 
        Float qty_difettosi = entity.getQty_difettosi();
        if (qty_difettosi != null) {
            stmt.bindDouble(4, qty_difettosi);
        }
 
        Float qty_scorta_min = entity.getQty_scorta_min();
        if (qty_scorta_min != null) {
            stmt.bindDouble(5, qty_scorta_min);
        }
 
        Float qty_scorta_max = entity.getQty_scorta_max();
        if (qty_scorta_max != null) {
            stmt.bindDouble(6, qty_scorta_max);
        }
 
        Float prezzo = entity.getPrezzo();
        if (prezzo != null) {
            stmt.bindDouble(7, prezzo);
        }
 
        String commento = entity.getCommento();
        if (commento != null) {
            stmt.bindString(8, commento);
        }
        stmt.bindString(9, entity.getStato());
        stmt.bindString(10, entity.getTimestamp());
 
        String desc = entity.getDesc();
        if (desc != null) {
            stmt.bindString(11, desc);
        }
        stmt.bindLong(12, entity.getPosizioneinlista());
 
        Float qty_per_confez = entity.getQty_per_confez();
        if (qty_per_confez != null) {
            stmt.bindDouble(13, qty_per_confez);
        }
 
        String barcode = entity.getBarcode();
        if (barcode != null) {
            stmt.bindString(14, barcode);
        }
        stmt.bindLong(15, entity.getIdtestata());
        stmt.bindLong(16, entity.getIdart());
    }

    @Override
    protected final void attachEntity(Inventari_dettagli entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Inventari_dettagli readEntity(Cursor cursor, int offset) {
        Inventari_dettagli entity = new Inventari_dettagli( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getFloat(offset + 1), // qty_esposta
            cursor.isNull(offset + 2) ? null : cursor.getFloat(offset + 2), // qty_magaz
            cursor.isNull(offset + 3) ? null : cursor.getFloat(offset + 3), // qty_difettosi
            cursor.isNull(offset + 4) ? null : cursor.getFloat(offset + 4), // qty_scorta_min
            cursor.isNull(offset + 5) ? null : cursor.getFloat(offset + 5), // qty_scorta_max
            cursor.isNull(offset + 6) ? null : cursor.getFloat(offset + 6), // prezzo
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // commento
            cursor.getString(offset + 8), // stato
            cursor.getString(offset + 9), // timestamp
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // desc
            cursor.getInt(offset + 11), // posizioneinlista
            cursor.isNull(offset + 12) ? null : cursor.getFloat(offset + 12), // qty_per_confez
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // barcode
            cursor.getLong(offset + 14), // idtestata
            cursor.getLong(offset + 15) // idart
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Inventari_dettagli entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setQty_esposta(cursor.isNull(offset + 1) ? null : cursor.getFloat(offset + 1));
        entity.setQty_magaz(cursor.isNull(offset + 2) ? null : cursor.getFloat(offset + 2));
        entity.setQty_difettosi(cursor.isNull(offset + 3) ? null : cursor.getFloat(offset + 3));
        entity.setQty_scorta_min(cursor.isNull(offset + 4) ? null : cursor.getFloat(offset + 4));
        entity.setQty_scorta_max(cursor.isNull(offset + 5) ? null : cursor.getFloat(offset + 5));
        entity.setPrezzo(cursor.isNull(offset + 6) ? null : cursor.getFloat(offset + 6));
        entity.setCommento(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setStato(cursor.getString(offset + 8));
        entity.setTimestamp(cursor.getString(offset + 9));
        entity.setDesc(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setPosizioneinlista(cursor.getInt(offset + 11));
        entity.setQty_per_confez(cursor.isNull(offset + 12) ? null : cursor.getFloat(offset + 12));
        entity.setBarcode(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setIdtestata(cursor.getLong(offset + 14));
        entity.setIdart(cursor.getLong(offset + 15));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Inventari_dettagli entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Inventari_dettagli entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Inventari_dettagli entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getInventari_testateDao().getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T1", daoSession.getArticoliDao().getAllColumns());
            builder.append(" FROM INVENTARI_DETTAGLI T");
            builder.append(" LEFT JOIN INVENTARI_TESTATE T0 ON T.\"IDTESTATA\"=T0.\"_id\"");
            builder.append(" LEFT JOIN ARTICOLI T1 ON T.\"IDART\"=T1.\"_id\"");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected Inventari_dettagli loadCurrentDeep(Cursor cursor, boolean lock) {
        Inventari_dettagli entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        Inventari_testate inventari_testate = loadCurrentOther(daoSession.getInventari_testateDao(), cursor, offset);
         if(inventari_testate != null) {
            entity.setInventari_testate(inventari_testate);
        }
        offset += daoSession.getInventari_testateDao().getAllColumns().length;

        Articoli articoli = loadCurrentOther(daoSession.getArticoliDao(), cursor, offset);
         if(articoli != null) {
            entity.setArticoli(articoli);
        }

        return entity;    
    }

    public Inventari_dettagli loadDeep(Long key) {
        assertSinglePk();
        if (key == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder(getSelectDeep());
        builder.append("WHERE ");
        SqlUtils.appendColumnsEqValue(builder, "T", getPkColumns());
        String sql = builder.toString();
        
        String[] keyArray = new String[] { key.toString() };
        Cursor cursor = db.rawQuery(sql, keyArray);
        
        try {
            boolean available = cursor.moveToFirst();
            if (!available) {
                return null;
            } else if (!cursor.isLast()) {
                throw new IllegalStateException("Expected unique result, but count was " + cursor.getCount());
            }
            return loadCurrentDeep(cursor, true);
        } finally {
            cursor.close();
        }
    }
    
    /** Reads all available rows from the given cursor and returns a list of new ImageTO objects. */
    public List<Inventari_dettagli> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<Inventari_dettagli> list = new ArrayList<Inventari_dettagli>(count);
        
        if (cursor.moveToFirst()) {
            if (identityScope != null) {
                identityScope.lock();
                identityScope.reserveRoom(count);
            }
            try {
                do {
                    list.add(loadCurrentDeep(cursor, false));
                } while (cursor.moveToNext());
            } finally {
                if (identityScope != null) {
                    identityScope.unlock();
                }
            }
        }
        return list;
    }
    
    protected List<Inventari_dettagli> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<Inventari_dettagli> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
