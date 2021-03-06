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
import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "REL_ARTICOLI_BARCODE".
*/
public class Rel_articoli_barcodeDao extends AbstractDao<Rel_articoli_barcode, Void> {

    public static final String TABLENAME = "REL_ARTICOLI_BARCODE";

    /**
     * Properties of entity Rel_articoli_barcode.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Idart = new Property(0, long.class, "idart", false, "IDART");
        public final static Property Idbc = new Property(1, long.class, "idbc", false, "IDBC");
    }

    private DaoSession daoSession;

    private Query<Rel_articoli_barcode> articoli_Rel_articoli_barcodeListQuery;

    public Rel_articoli_barcodeDao(DaoConfig config) {
        super(config);
    }
    
    public Rel_articoli_barcodeDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"REL_ARTICOLI_BARCODE\" (" + //
                "\"IDART\" INTEGER NOT NULL ," + // 0: idart
                "\"IDBC\" INTEGER NOT NULL );"); // 1: idbc
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"REL_ARTICOLI_BARCODE\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Rel_articoli_barcode entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getIdart());
        stmt.bindLong(2, entity.getIdbc());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Rel_articoli_barcode entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getIdart());
        stmt.bindLong(2, entity.getIdbc());
    }

    @Override
    protected final void attachEntity(Rel_articoli_barcode entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    @Override
    public Rel_articoli_barcode readEntity(Cursor cursor, int offset) {
        Rel_articoli_barcode entity = new Rel_articoli_barcode( //
            cursor.getLong(offset + 0), // idart
            cursor.getLong(offset + 1) // idbc
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Rel_articoli_barcode entity, int offset) {
        entity.setIdart(cursor.getLong(offset + 0));
        entity.setIdbc(cursor.getLong(offset + 1));
     }
    
    @Override
    protected final Void updateKeyAfterInsert(Rel_articoli_barcode entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    @Override
    public Void getKey(Rel_articoli_barcode entity) {
        return null;
    }

    @Override
    public boolean hasKey(Rel_articoli_barcode entity) {
        return false;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
    /** Internal query to resolve the "rel_articoli_barcodeList" to-many relationship of Articoli. */
    public List<Rel_articoli_barcode> _queryArticoli_Rel_articoli_barcodeList(long idart) {
        synchronized (this) {
            if (articoli_Rel_articoli_barcodeListQuery == null) {
                QueryBuilder<Rel_articoli_barcode> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.Idart.eq(null));
                articoli_Rel_articoli_barcodeListQuery = queryBuilder.build();
            }
        }
        Query<Rel_articoli_barcode> query = articoli_Rel_articoli_barcodeListQuery.forCurrentThread();
        query.setParameter(0, idart);
        return query.list();
    }

    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getBarcodeDao().getAllColumns());
            builder.append(" FROM REL_ARTICOLI_BARCODE T");
            builder.append(" LEFT JOIN BARCODE T0 ON T.\"IDBC\"=T0.\"_id\"");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected Rel_articoli_barcode loadCurrentDeep(Cursor cursor, boolean lock) {
        Rel_articoli_barcode entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        Barcode barcode = loadCurrentOther(daoSession.getBarcodeDao(), cursor, offset);
         if(barcode != null) {
            entity.setBarcode(barcode);
        }

        return entity;    
    }

    public Rel_articoli_barcode loadDeep(Long key) {
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
    public List<Rel_articoli_barcode> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<Rel_articoli_barcode> list = new ArrayList<Rel_articoli_barcode>(count);
        
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
    
    protected List<Rel_articoli_barcode> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<Rel_articoli_barcode> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
