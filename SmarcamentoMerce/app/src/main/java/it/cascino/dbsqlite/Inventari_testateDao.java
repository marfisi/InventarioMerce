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
 * DAO for table "INVENTARI_TESTATE".
*/
public class Inventari_testateDao extends AbstractDao<Inventari_testate, Long> {

    public static final String TABLENAME = "INVENTARI_TESTATE";

    /**
     * Properties of entity Inventari_testate.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Utente_creatore = new Property(1, String.class, "utente_creatore", false, "UTENTE_CREATORE");
        public final static Property Utente_destinatario = new Property(2, String.class, "utente_destinatario", false, "UTENTE_DESTINATARIO");
        public final static Property Data_creazione = new Property(3, String.class, "data_creazione", false, "DATA_CREAZIONE");
        public final static Property Data_modifica = new Property(4, String.class, "data_modifica", false, "DATA_MODIFICA");
        public final static Property Data_conferma = new Property(5, String.class, "data_conferma", false, "DATA_CONFERMA");
        public final static Property Stato = new Property(6, String.class, "stato", false, "STATO");
        public final static Property Commento = new Property(7, String.class, "commento", false, "COMMENTO");
        public final static Property Nome_file = new Property(8, String.class, "nome_file", false, "NOME_FILE");
        public final static Property Iddep = new Property(9, long.class, "iddep", false, "IDDEP");
    }

    private DaoSession daoSession;


    public Inventari_testateDao(DaoConfig config) {
        super(config);
    }
    
    public Inventari_testateDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"INVENTARI_TESTATE\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"UTENTE_CREATORE\" TEXT NOT NULL ," + // 1: utente_creatore
                "\"UTENTE_DESTINATARIO\" TEXT NOT NULL ," + // 2: utente_destinatario
                "\"DATA_CREAZIONE\" TEXT NOT NULL ," + // 3: data_creazione
                "\"DATA_MODIFICA\" TEXT," + // 4: data_modifica
                "\"DATA_CONFERMA\" TEXT," + // 5: data_conferma
                "\"STATO\" TEXT NOT NULL ," + // 6: stato
                "\"COMMENTO\" TEXT," + // 7: commento
                "\"NOME_FILE\" TEXT," + // 8: nome_file
                "\"IDDEP\" INTEGER NOT NULL );"); // 9: iddep
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"INVENTARI_TESTATE\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Inventari_testate entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getUtente_creatore());
        stmt.bindString(3, entity.getUtente_destinatario());
        stmt.bindString(4, entity.getData_creazione());
 
        String data_modifica = entity.getData_modifica();
        if (data_modifica != null) {
            stmt.bindString(5, data_modifica);
        }
 
        String data_conferma = entity.getData_conferma();
        if (data_conferma != null) {
            stmt.bindString(6, data_conferma);
        }
        stmt.bindString(7, entity.getStato());
 
        String commento = entity.getCommento();
        if (commento != null) {
            stmt.bindString(8, commento);
        }
 
        String nome_file = entity.getNome_file();
        if (nome_file != null) {
            stmt.bindString(9, nome_file);
        }
        stmt.bindLong(10, entity.getIddep());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Inventari_testate entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getUtente_creatore());
        stmt.bindString(3, entity.getUtente_destinatario());
        stmt.bindString(4, entity.getData_creazione());
 
        String data_modifica = entity.getData_modifica();
        if (data_modifica != null) {
            stmt.bindString(5, data_modifica);
        }
 
        String data_conferma = entity.getData_conferma();
        if (data_conferma != null) {
            stmt.bindString(6, data_conferma);
        }
        stmt.bindString(7, entity.getStato());
 
        String commento = entity.getCommento();
        if (commento != null) {
            stmt.bindString(8, commento);
        }
 
        String nome_file = entity.getNome_file();
        if (nome_file != null) {
            stmt.bindString(9, nome_file);
        }
        stmt.bindLong(10, entity.getIddep());
    }

    @Override
    protected final void attachEntity(Inventari_testate entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Inventari_testate readEntity(Cursor cursor, int offset) {
        Inventari_testate entity = new Inventari_testate( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // utente_creatore
            cursor.getString(offset + 2), // utente_destinatario
            cursor.getString(offset + 3), // data_creazione
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // data_modifica
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // data_conferma
            cursor.getString(offset + 6), // stato
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // commento
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // nome_file
            cursor.getLong(offset + 9) // iddep
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Inventari_testate entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setUtente_creatore(cursor.getString(offset + 1));
        entity.setUtente_destinatario(cursor.getString(offset + 2));
        entity.setData_creazione(cursor.getString(offset + 3));
        entity.setData_modifica(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setData_conferma(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setStato(cursor.getString(offset + 6));
        entity.setCommento(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setNome_file(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setIddep(cursor.getLong(offset + 9));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Inventari_testate entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Inventari_testate entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Inventari_testate entity) {
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
            SqlUtils.appendColumns(builder, "T0", daoSession.getDepositiDao().getAllColumns());
            builder.append(" FROM INVENTARI_TESTATE T");
            builder.append(" LEFT JOIN DEPOSITI T0 ON T.\"IDDEP\"=T0.\"_id\"");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected Inventari_testate loadCurrentDeep(Cursor cursor, boolean lock) {
        Inventari_testate entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        Depositi depositi = loadCurrentOther(daoSession.getDepositiDao(), cursor, offset);
         if(depositi != null) {
            entity.setDepositi(depositi);
        }

        return entity;    
    }

    public Inventari_testate loadDeep(Long key) {
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
    public List<Inventari_testate> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<Inventari_testate> list = new ArrayList<Inventari_testate>(count);
        
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
    
    protected List<Inventari_testate> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<Inventari_testate> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}