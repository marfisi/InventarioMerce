package it.cascino.inventario;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class RunInventarioCreaDb{
	private static Logger log;
	
	public static void main(String[] args){
		PropertyConfigurator.configure("logdir/logInventarioCreaDb.properties");
		log = Logger.getLogger(RunInventarioCreaDb.class);
		log.info("START");
		
		@SuppressWarnings("unused")
		InventarioCreaDb inventarioCreaDb = new InventarioCreaDb(args);
		
		log.info("STOP");
		System.exit(0);
	}
}
