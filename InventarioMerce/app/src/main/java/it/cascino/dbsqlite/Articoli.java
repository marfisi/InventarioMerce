package it.cascino.dbsqlite;

import org.greenrobot.greendao.annotation.*;

import java.util.List;
import it.cascino.dbsqlite.DaoSession;
import org.greenrobot.greendao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table "ARTICOLI".
 */
@Entity(active = true)
public class Articoli {

    @Id(autoincrement = true)
    private Long id;

    @NotNull
    private String codart;

    @NotNull
    private String desc;

    @NotNull
    private String um;
    private float prezzo;
    private Float qty_per_confez;

    /** Used to resolve relations */
    @Generated
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated
    private transient ArticoliDao myDao;

    @ToMany(joinProperties = {
        @JoinProperty(name = "id", referencedName = "idart")
    })
    private List<Rel_articoli_barcode> rel_articoli_barcodeList;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    @Generated
    public Articoli() {
    }

    public Articoli(Long id) {
        this.id = id;
    }

    @Generated
    public Articoli(Long id, String codart, String desc, String um, float prezzo, Float qty_per_confez) {
        this.id = id;
        this.codart = codart;
        this.desc = desc;
        this.um = um;
        this.prezzo = prezzo;
        this.qty_per_confez = qty_per_confez;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getArticoliDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotNull
    public String getCodart() {
        return codart;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setCodart(@NotNull String codart) {
        this.codart = codart;
    }

    @NotNull
    public String getDesc() {
        return desc;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setDesc(@NotNull String desc) {
        this.desc = desc;
    }

    @NotNull
    public String getUm() {
        return um;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setUm(@NotNull String um) {
        this.um = um;
    }

    public float getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(float prezzo) {
        this.prezzo = prezzo;
    }

    public Float getQty_per_confez() {
        return qty_per_confez;
    }

    public void setQty_per_confez(Float qty_per_confez) {
        this.qty_per_confez = qty_per_confez;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    @Generated
    public List<Rel_articoli_barcode> getRel_articoli_barcodeList() {
        if (rel_articoli_barcodeList == null) {
            __throwIfDetached();
            Rel_articoli_barcodeDao targetDao = daoSession.getRel_articoli_barcodeDao();
            List<Rel_articoli_barcode> rel_articoli_barcodeListNew = targetDao._queryArticoli_Rel_articoli_barcodeList(id);
            synchronized (this) {
                if(rel_articoli_barcodeList == null) {
                    rel_articoli_barcodeList = rel_articoli_barcodeListNew;
                }
            }
        }
        return rel_articoli_barcodeList;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated
    public synchronized void resetRel_articoli_barcodeList() {
        rel_articoli_barcodeList = null;
    }

    /**
    * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
    * Entity must attached to an entity context.
    */
    @Generated
    public void delete() {
        __throwIfDetached();
        myDao.delete(this);
    }

    /**
    * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
    * Entity must attached to an entity context.
    */
    @Generated
    public void update() {
        __throwIfDetached();
        myDao.update(this);
    }

    /**
    * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
    * Entity must attached to an entity context.
    */
    @Generated
    public void refresh() {
        __throwIfDetached();
        myDao.refresh(this);
    }

    @Generated
    private void __throwIfDetached() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}
