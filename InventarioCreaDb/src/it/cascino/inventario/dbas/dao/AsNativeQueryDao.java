package it.cascino.inventario.dbas.dao;

import java.math.BigDecimal;
import java.sql.Timestamp;

public interface AsNativeQueryDao{
	BigDecimal getDaMovtr0f_MtquaDaMtcodAndMtdpa(String mtcod, Integer mtdpa);

	Timestamp getDaSysdummy1_TimestampAs400();

	void close();
}
