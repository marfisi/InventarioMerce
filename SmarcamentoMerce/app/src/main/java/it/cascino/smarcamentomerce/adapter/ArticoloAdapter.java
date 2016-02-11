package it.cascino.smarcamentomerce.adapter;

import android.content.Context;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import it.cascino.smarcamentomerce.R;
import it.cascino.smarcamentomerce.it.cascino.smarcamentomerce.model.Articolo;

public class ArticoloAdapter extends BaseAdapter implements Filterable{
	private List<Articolo> articoliLs;
	private List<Articolo> articoliOriginaleLs;
	private Context context;
	private ArticoloFiltro articoloFiltro;

	public ArticoloAdapter(Context context, List<Articolo> articoliLs){
		this.context = context;
		this.articoliLs = articoliLs;
		this.articoliOriginaleLs =  articoliLs; //articoliOriginaleLs;
	}

	@Override
	public Filter getFilter(){
		if(articoloFiltro == null){
			articoloFiltro = new ArticoloFiltro();
		}
		return articoloFiltro;
	}

	@Override
	public int getCount(){
		return articoliLs.size();
	}

	@Override
	public Object getItem(int position){
		return articoliLs.get(position);
	}

	@Override
	public long getItemId(int position){
		return getItem(position).hashCode();
	}

	/* @Override
	public View getView(int position, View v, ViewGroup vg){
		// con " extends ArrayAdapter<Articolo>" avrei potuto usare
		// LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if(v == null){
			v = LayoutInflater.from(context).inflate(R.layout.rowarticolo, null);
		}
		Articolo a = (Articolo)getItem(position);
		TextView txt = (TextView)v.findViewById(R.id.art_codart);
		txt.setText(a.getCodice());
		txt = (TextView)v.findViewById(R.id.art_bcod);
		String bcod = "";
		for(int i = 0; i < a.getBarcode().length; i++){
			bcod = bcod + "," + a.getBarcode()[i].getCodice();
		}
		txt.setText(StringUtils.removeStart(bcod, ","));
		txt = (TextView)v.findViewById(R.id.art_desc);
		txt.setText(a.getDesc());
		txt = (TextView)v.findViewById(R.id.art_um_qty);
		txt.setText(a.getUm());
		txt = (TextView)v.findViewById(R.id.art_qtyTxt);
		txt.setText(Float.toString(a.getQty()));
		txt = (TextView)v.findViewById(R.id.art_qty);
		txt.setText(Float.toString(a.getQty()));
		txt = (TextView)v.findViewById(R.id.art_difet_qty);
		txt.setText(Float.toString(a.getDifettosi()));
		txt = (TextView)v.findViewById(R.id.art_dataCarScar);
		txt.setText("c: " + a.getDataCarico() + " - s: " + a.getDataScarico());
		return v;
	} */

	@Override
	public View getView(int position, View v, ViewGroup vg){
		return getViewOptimize(position, v, vg);
	}

	public View getViewOptimize(int position, View v, ViewGroup vg){
		ViewHolder viewHolder = null;
		if(v == null){
			v = LayoutInflater.from(context).inflate(R.layout.rowarticolo, null);
			viewHolder = new ViewHolder();
			viewHolder.art_codart = (TextView)v.findViewById(R.id.art_codart);
			viewHolder.art_bcod = (TextView)v.findViewById(R.id.art_bcod);
			viewHolder.art_desc = (TextView)v.findViewById(R.id.art_desc);
			viewHolder.art_um_qty = (TextView)v.findViewById(R.id.art_um_qty);
			viewHolder.art_qty = (TextView)v.findViewById(R.id.art_qty);
			viewHolder.art_difet_qty = (TextView)v.findViewById(R.id.art_difet_qty);
			viewHolder.art_dataCarScar = (TextView)v.findViewById(R.id.art_dataCarScar);
			viewHolder.btnIncrementa = (Button)v.findViewById(R.id.btnIncrementa);
			viewHolder.btnDecrementa = (Button)v.findViewById(R.id.btnDecrementa);
			v.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)v.getTag();
		}
		final Articolo a = (Articolo)getItem(position);
		viewHolder.art_codart.setText(a.getCodice());
		String bcod = "";
		for(int i = 0; i < a.getBarcode().length; i++){
			bcod = bcod + "-" + a.getBarcode()[i].getCodice();
		}
		viewHolder.art_bcod.setText(StringUtils.removeStart(bcod, "-"));
		viewHolder.art_desc.setText(a.getDesc());
		viewHolder.art_um_qty.setText(a.getUm());
		viewHolder.art_qty.setText(floatToString(a.getQtyRilevata()));
		//viewHolder.art_qty.setInputType(InputType.TYPE_CLASS_NUMBER);//.TYPE_NUMBER_FLAG_DECIMAL);
		//viewHolder.art_qty.requestFocus();
		//InputMethodManager inputMethodManager = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
		//inputMethodManager.showSoftInput(viewHolder.art_qty, InputMethodManager.SHOW_FORCED);
		//inputMethodManager.showSoftInput(viewHolder.art_qty, InputMethodManager.SHOW_FORCED);
		//inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
		viewHolder.art_difet_qty.setText(floatToString(a.getDifettosi()));
		viewHolder.art_dataCarScar.setText("c: " + a.getDataCarico() + " - s: " + a.getDataScarico());
		final ViewHolder finalViewHolder = viewHolder;
		viewHolder.btnIncrementa.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				manageClickIncDec(finalViewHolder, a, true, 1.0f);
			}
		});
		viewHolder.btnIncrementa.setOnLongClickListener(new View.OnLongClickListener(){
			@Override
			public boolean onLongClick(View v){
				manageClickIncDec(finalViewHolder, a, true, 5.0f);
				return true;
			}
		});
		/*viewHolder.btnIncrementa.setOnTouchListener(new Button.OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event){
				if(event.getAction() == MotionEvent.ACTION_HOVER_ENTER){
					manageClickIncDec(finalViewHolder, a, true, 0.2f);
				}
				return false;
			}
		});*/
		viewHolder.btnDecrementa.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				manageClickIncDec(finalViewHolder, a, false, 1.0f);
			}
		});
		viewHolder.btnDecrementa.setOnLongClickListener(new View.OnLongClickListener(){
			@Override
			public boolean onLongClick(View v){
				manageClickIncDec(finalViewHolder, a, false, 5.0f);
				return true;
			}
		});
		return v;
	}

	private void manageClickIncDec(ViewHolder vh, Articolo a, boolean incrementa, float step){
		if(incrementa){
			a.setQtyRilevata(a.getQtyRilevata() + step);
		}else{
			a.setQtyRilevata(a.getQtyRilevata() - step);
		}
		vh.art_qty.setText(floatToString(a.getQtyRilevata()));
	}

	private class ViewHolder{
		public TextView art_codart;
		public TextView art_bcod;
		public TextView art_desc;
		public TextView art_um_qty;
		public TextView art_qty;
		public TextView art_difet_qty;
		public TextView art_dataCarScar;

		public Button btnIncrementa;
		public Button btnDecrementa;
	}

	@Override
	public void notifyDataSetChanged(){
		super.notifyDataSetChanged();
	}

	private class ArticoloFiltro extends Filter{
		@Override
		protected FilterResults performFiltering(CharSequence constraint){
			FilterResults result = new FilterResults();
			if((constraint != null) && (constraint.toString().length() > 0)){
				ArrayList<Articolo> filteredItems = new ArrayList<Articolo>();

				constraint = constraint.toString().toLowerCase();
				Log.i("ricerca: ", constraint.toString());
				for(int i = 0; i < articoliOriginaleLs.size(); i++){
					Articolo articolo = articoliOriginaleLs.get(i);
					if(articolo.toString().toLowerCase().contains(constraint)){
						filteredItems.add(articolo);
						Log.i("trovato: ", articolo.toString());
					}
				}
				result.values = filteredItems;
				result.count = filteredItems.size();
			}else{
				synchronized(this){
					result.values = articoliOriginaleLs;
					result.count = articoliOriginaleLs.size();
				}
			}
			return result;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint, FilterResults results){
			/*if(results.count == 0){
				notifyDataSetInvalidated();
			}else{*/
				articoliLs = (ArrayList)results.values;
				notifyDataSetChanged();
			//}

			/* articoliLs = (ArrayList<Articolo>)results.values;
			notifyDataSetChanged();
			articoliLs.clear();
			for(int i = 0; i < articoliLs.size(); i++) {
				articoliLs.add(articoliLs.get(i));
			}
			notifyDataSetInvalidated(); */
		}
	}

	/* public List<Articolo> getArticoliLs(){
		return articoliLs;
	}

	public void setArticoliLs(List<Articolo> articoliLs){
		this.articoliLs = articoliLs;
	} */

	public List<Articolo> getArticoliOriginaleLs(){
		return articoliOriginaleLs;
	}

	public void setArticoliOriginaleLs(List<Articolo> articoliOriginaleLs){
		this.articoliOriginaleLs = articoliOriginaleLs;
	}

	private String floatToString(Float f){
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator(',');
		DecimalFormat format = new DecimalFormat();
		format.setDecimalFormatSymbols(symbols);
		format.applyPattern("0.00");
		return format.format(f);
	}
}
