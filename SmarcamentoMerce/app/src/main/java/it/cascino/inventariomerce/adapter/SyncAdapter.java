package it.cascino.inventariomerce.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import it.cascino.inventariomerce.R;
import it.cascino.inventariomerce.model.Inventario;
import it.cascino.inventariomerce.utils.TipoStato;

public class SyncAdapter extends BaseAdapter{
	private List<Inventario> inventariLs;
	private Context context;

	public SyncAdapter(Context context, List<Inventario> inventariLs){
		this.context = context;
		this.inventariLs = inventariLs;
	}

	@Override
	public int getCount(){
		return inventariLs.size();
	}

	@Override
	public Object getItem(int position){
		return inventariLs.get(position);
	}

	@Override
	public long getItemId(int position){
		return getItem(position).hashCode();
	}

	@Override
	public View getView(int position, View v, ViewGroup vg){
		return getViewOptimize(position, v, vg);
	}

	public View getViewOptimize(int position, View v, ViewGroup vg){
		ViewHolder viewHolder = null;
		if(v == null){
			v = LayoutInflater.from(context).inflate(R.layout.rowsorgenteinventario, null);
			viewHolder = new ViewHolder();
			viewHolder.nomeFile = (TextView)v.findViewById(R.id.nomeFile);
			viewHolder.progressivo = (TextView)v.findViewById(R.id.progressivo);
			viewHolder.artInvent = (TextView)v.findViewById(R.id.artInvent);
			viewHolder.statoInvent = (TextView)v.findViewById(R.id.statoInvent);
			viewHolder.creatoreInvent = (TextView)v.findViewById(R.id.creatoreInvent);
			viewHolder.dataCreazioneInvent = (TextView)v.findViewById(R.id.dataCreazioneInvent);
			viewHolder.textDataModifica = (TextView)v.findViewById(R.id.textDataModifica);
			viewHolder.dataModificaInvent = (TextView)v.findViewById(R.id.dataModificaInvent);
			viewHolder.commentoInvent = (TextView)v.findViewById(R.id.commentoInvent);
			viewHolder.nomeFileInDbInvent = (TextView)v.findViewById(R.id.nomeFileInDbInvent);
			viewHolder.textNomeFileSorgente = (TextView)v.findViewById(R.id.textNomeFileSorgente);
			v.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)v.getTag();
		}
		final Inventario f = (Inventario)getItem(position);
		if(f.getSorgenteFile()){
			viewHolder.nomeFile.setText(f.getNomeFile());
		}else{
			viewHolder.nomeFile.setText("Database");
		}
		viewHolder.progressivo.setText(String.valueOf(f.getProgressivo()));
		viewHolder.artInvent.setText(String.valueOf(f.getNumeroArticoliInventariati()) + "/" + String.valueOf(f.getNumeroArticoliTotale()));
		viewHolder.statoInvent.setText(TipoStato.getStatoDescrizione(Integer.parseInt(f.getStato())));
		viewHolder.creatoreInvent.setText(f.getUtenteCreatore());
		viewHolder.dataCreazioneInvent.setText(f.getTimeCreazione().toString());
		if(f.getSorgenteFile()){
			viewHolder.textDataModifica.setVisibility(View.INVISIBLE);
			viewHolder.dataModificaInvent.setVisibility(View.INVISIBLE);
		}else{
			viewHolder.dataModificaInvent.setText((f.getTimeModifica() == null) ? "" : f.getTimeModifica().toString());
		}
		viewHolder.commentoInvent.setText(f.getCommento());
		if(f.getSorgenteFile()){
			viewHolder.nomeFileInDbInvent.setVisibility(View.INVISIBLE);
			viewHolder.textNomeFileSorgente.setVisibility(View.INVISIBLE);
		}else{
			viewHolder.nomeFileInDbInvent.setText(f.getNomeFile());
		}
		return v;
	}

	private class ViewHolder{
		public TextView nomeFile;
		public TextView progressivo;
		public TextView artInvent;
		public TextView statoInvent;
		public TextView creatoreInvent;
		public TextView dataCreazioneInvent;
		public TextView textDataModifica;
		public TextView dataModificaInvent;
		public TextView commentoInvent;
		public TextView nomeFileInDbInvent;
		public TextView textNomeFileSorgente;
	}

	@Override
	public void notifyDataSetChanged(){
		super.notifyDataSetChanged();
	}
}

