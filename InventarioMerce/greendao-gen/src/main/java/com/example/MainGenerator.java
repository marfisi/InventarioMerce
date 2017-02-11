package com.example;

import org.greenrobot.greendao.generator.DaoGenerator;
import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Property;
import org.greenrobot.greendao.generator.Schema;

public class MainGenerator{

	private static final String PROJECT_DIR = System.getProperty("user.dir").replace("\\", "/");

	public static void main(String[] args){
		Schema schema = new Schema(1, "it.cascino.dbsqlite");
		schema.enableKeepSectionsByDefault();

		addTables(schema);

		try {
			new DaoGenerator().generateAll(schema, PROJECT_DIR + "/app/src/main/java");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void addTables(final Schema schema) {
		Entity articoli = addArticoli(schema);
		Entity barcode = addBarcode(schema);
		Entity rel_articoli_barcode = addRel_articoli_barcode(schema);
		Entity depositi = addDepositi(schema);
		Entity qty_originali = addQty_originali(schema);
		Entity inventari_testate = addInventari_testate(schema);
		Entity inventari_dettagli = addInventari_dettagli(schema);
		Entity infogeneriche = addInfogeneriche(schema);

		Property idart_bc = rel_articoli_barcode.addLongProperty("idart").notNull().getProperty();
		articoli.addToMany(rel_articoli_barcode, idart_bc);
		Property idbc = rel_articoli_barcode.addLongProperty("idbc").notNull().getProperty();
		rel_articoli_barcode.addToOne(barcode, idbc);

		Property iddep = inventari_testate.addLongProperty("iddep").notNull().getProperty();
		inventari_testate.addToOne(depositi, iddep);

		Property idtestata = inventari_dettagli.addLongProperty("idtestata").notNull().getProperty();
		inventari_dettagli.addToOne(inventari_testate, idtestata);
		Property idart_invent = inventari_dettagli.addLongProperty("idart").notNull().getProperty();
		inventari_dettagli.addToOne(articoli, idart_invent);

		Property idart_qty = qty_originali.addLongProperty("idart").notNull().getProperty();
		qty_originali.addToOne(articoli, idart_qty);
		Property iddep_qty = qty_originali.addLongProperty("iddep").notNull().getProperty();
		qty_originali.addToOne(depositi, iddep_qty);
	}

	private static Entity addArticoli(final Schema schema){
		Entity articoli = schema.addEntity("Articoli");
		articoli.addIdProperty().primaryKey().autoincrement();
		articoli.addStringProperty("codart").notNull();
		articoli.addStringProperty("desc").notNull();
		articoli.addStringProperty("um").notNull();
		articoli.addFloatProperty("prezzo").notNull();
		articoli.addFloatProperty("qty_per_confez");
		return articoli;
	}

	private static Entity addBarcode(final Schema schema){
		Entity barcode = schema.addEntity("Barcode");
		barcode.addIdProperty().primaryKey().autoincrement();
		barcode.addStringProperty("codice").notNull();
		barcode.addFloatProperty("tipo");
		return barcode;
	}

	private static Entity addRel_articoli_barcode(final Schema schema){
		Entity rel_articoli_barcode = schema.addEntity("Rel_articoli_barcode");
		// rel_articoli_barcode.addLongProperty("idart").notNull();
		// rel_articoli_barcode.addLongProperty("idbc").notNull();
		return rel_articoli_barcode;
	}

	private static Entity addDepositi(final Schema schema){
		Entity depositi = schema.addEntity("Depositi");
		depositi.addIdProperty().primaryKey().autoincrement();
		depositi.addStringProperty("iddep").notNull();
		depositi.addStringProperty("desc").notNull();
		return depositi;
	}

	private static Entity addQty_originali(final Schema schema){
		Entity qty_originali = schema.addEntity("Qty_originali");
		qty_originali.addIdProperty().primaryKey().autoincrement();
		// qty_originali.addLongProperty("idart").notNull();
		// qty_originali.addLongProperty("iddep").notNull();
		qty_originali.addFloatProperty("qty").notNull();
		qty_originali.addFloatProperty("qty_difettosi").notNull();
		qty_originali.addStringProperty("data_carico");
		qty_originali.addStringProperty("data_scarico");
		qty_originali.addStringProperty("data_inventario");
		qty_originali.addFloatProperty("qty_scorta_min");
		qty_originali.addFloatProperty("qty_scorta_max");
		qty_originali.addFloatProperty("qty_trasf");
		return qty_originali;
	}

	private static Entity addInventari_testate(final Schema schema){
		Entity inventari_testate = schema.addEntity("Inventari_testate");
		inventari_testate.addIdProperty().primaryKey().autoincrement();
		// inventari_testate.addLongProperty("iddep").notNull();
		inventari_testate.addStringProperty("utente_creatore").notNull();
		inventari_testate.addStringProperty("utente_destinatario").notNull();
		inventari_testate.addStringProperty("data_creazione").notNull();
		inventari_testate.addStringProperty("data_modifica");
		inventari_testate.addStringProperty("data_conferma");
		inventari_testate.addStringProperty("stato").notNull();
		inventari_testate.addStringProperty("commento");
		inventari_testate.addStringProperty("nome_file");
		return inventari_testate;
	}

	private static Entity addInventari_dettagli(final Schema schema){
		Entity inventari_dettagli = schema.addEntity("Inventari_dettagli");
		inventari_dettagli.addIdProperty().primaryKey().autoincrement();
		// inventari_dettagli.addLongProperty("idtestata").notNull();
		// inventari_dettagli.addLongProperty("idart").notNull();
		inventari_dettagli.addFloatProperty("qty_esposta");
		inventari_dettagli.addFloatProperty("qty_magaz");
		inventari_dettagli.addFloatProperty("qty_difettosi");
		inventari_dettagli.addFloatProperty("qty_scorta_min");
		inventari_dettagli.addFloatProperty("qty_scorta_max");
		inventari_dettagli.addFloatProperty("prezzo");
		inventari_dettagli.addStringProperty("commento");
		inventari_dettagli.addStringProperty("stato").notNull();
		inventari_dettagli.addStringProperty("timestamp").notNull();
		inventari_dettagli.addStringProperty("desc");
		inventari_dettagli.addIntProperty("posizioneinlista").notNull();
		inventari_dettagli.addFloatProperty("qty_per_confez");
		inventari_dettagli.addStringProperty("barcode");
		return inventari_dettagli;
	}

	private static Entity addInfogeneriche(final Schema schema){
		Entity infogeneriche = schema.addEntity("Infogeneriche");
		infogeneriche.addIdProperty().primaryKey().autoincrement();
		infogeneriche.addStringProperty("info").notNull();
		infogeneriche.addStringProperty("valore").notNull();
		return infogeneriche;
	}
}