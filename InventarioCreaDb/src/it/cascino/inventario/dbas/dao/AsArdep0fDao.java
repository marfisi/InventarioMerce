package it.cascino.inventario.dbas.dao;

import java.util.List;
import it.cascino.inventario.dbas.model.AsArdep0f;

public interface AsArdep0fDao{
	List<AsArdep0f> getAll();
	
	List<AsArdep0f> getDaDcoda(String dcoda);

	List<AsArdep0f> getDaDcode(Integer dcode);

	AsArdep0f getDaDcodaAndDcode(String dcoda, Integer dcode);

	void close();
}
