package it.cascino.smarcamentomerce.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import it.cascino.smarcamentomerce.R;
import it.cascino.smarcamentomerce.it.cascino.smarcamentomerce.model.Articolo;

public class ArticoloAdapter extends BaseAdapter {
	private List<Articolo> articoliLs;
	private Context context;

	public ArticoloAdapter(Context context, List<Articolo> articoliLs) {
		this.context = context;
		this.articoliLs = articoliLs;
	}

	@Override
	public int getCount() {
		return articoliLs.size();
	}

	@Override
	public Object getItem(int position) {
		return articoliLs.get(position);
	}

	@Override
	public long getItemId(int position) {
		return getItem(position).hashCode();
	}

	@Override
	public View getView(int position, View v, ViewGroup vg) {
		if (v == null) {
			v = LayoutInflater.from(context).inflate(R.layout.rowarticolo, null);
        }
		Articolo a = (Articolo) getItem(position);
		TextView txt = (TextView) v.findViewById(R.id.art_codart);
		txt.setText(a.getCodice());
		txt = (TextView) v.findViewById(R.id.art_bcod);
		txt.setText(a.getBarcode()[0].getCodice());
        txt = (TextView) v.findViewById(R.id.art_desc);
        txt.setText(a.getDesc());
        txt = (TextView) v.findViewById(R.id.art_qty);
        txt.setText(Float.toString(a.getQty()));
        txt = (TextView) v.findViewById(R.id.art_dataCarScar);
        txt.setText("c: " + a.getDataCarico() + " - s: " + a.getDataScarico());
        return v;
	}
}
