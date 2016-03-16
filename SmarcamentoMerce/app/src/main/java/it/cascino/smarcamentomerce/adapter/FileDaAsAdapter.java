package it.cascino.smarcamentomerce.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import it.cascino.smarcamentomerce.R;
import it.cascino.smarcamentomerce.model.FileDaAs;

public class FileDaAsAdapter extends BaseAdapter{
	private List<FileDaAs> fileDaAsLs;
	private Context context;

	public FileDaAsAdapter(Context context, List<FileDaAs> fileDaAsLs){
		this.context = context;
		this.fileDaAsLs = fileDaAsLs;
	}

	@Override
	public int getCount(){
		return fileDaAsLs.size();
	}

	@Override
	public Object getItem(int position){
		return fileDaAsLs.get(position);
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
			v = LayoutInflater.from(context).inflate(R.layout.row_file, null);
			viewHolder = new ViewHolder();
			viewHolder.nomeFile = (TextView)v.findViewById(R.id.nomeFile);
			viewHolder.numeroArticoli = (TextView)v.findViewById(R.id.numeroArticoli);
			viewHolder.progressivo = (TextView)v.findViewById(R.id.progressivo);
			v.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)v.getTag();
		}
		final FileDaAs f = (FileDaAs)getItem(position);
		viewHolder.nomeFile.setText(f.getNomeFile());
		viewHolder.numeroArticoli.setText(String.valueOf(f.getNumeroArticoli()));
		viewHolder.progressivo.setText(String.valueOf(f.getProgressivo()));
		//manageBckgRow(f, v);
		return v;
	}

	/*
	private void manageBckgRow(FileDaAs f, View v){
		//View daMod = (View)v.getParent().getParent().getParent();
		if(!(f.getStato().equals(2))){
			if(Float.compare(f.getQtyRilevata(), f.getQtyOriginale()) == 0){
				v.setBackgroundColor(ContextCompat.getColor(this.context, R.color.bckgChkOK));
			}else{
				v.setBackgroundColor(ContextCompat.getColor(this.context, R.color.bckgChkDiffer));
			}
		}else{
			v.setBackgroundColor(Color.TRANSPARENT);
		}
	}
	*/

	private class ViewHolder{
		public TextView nomeFile;
		public TextView numeroArticoli;
		public TextView progressivo;
	}

	@Override
	public void notifyDataSetChanged(){
		super.notifyDataSetChanged();
	}


}

