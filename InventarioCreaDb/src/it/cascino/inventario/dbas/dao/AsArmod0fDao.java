package it.cascino.inventario.dbas.dao;

import java.util.List;
import it.cascino.inventario.dbas.model.AsArmod0f;

public interface AsArmod0fDao{
	List<AsArmod0f> getAll();
	
	void aggiorna(AsArmod0f a);
	
	List<AsArmod0f> getDaDcoda(String dcoda);

	List<AsArmod0f> getDaDcode(Integer dcode);

	AsArmod0f getDaDcodaAndDcode(String dcoda, Integer dcode);
	
	List<String> getDaElaborare();

	void close();
}
