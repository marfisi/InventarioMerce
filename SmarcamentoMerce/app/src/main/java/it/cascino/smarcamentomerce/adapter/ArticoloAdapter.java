package it.cascino.smarcamentomerce.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import it.cascino.smarcamentomerce.R;
import it.cascino.smarcamentomerce.it.cascino.smarcamentomerce.model.Articolo;

public class ArticoloAdapter extends BaseAdapter implements Filterable{
	private List<Articolo> articoliLs;
	private List<Articolo> articoliOriginaleLs;
	private Context context;
	private ArticoloFiltro articoloFiltro;

	private Handler repeatUpdateHandler = new Handler();
	private boolean mAutoIncrement = false;
	private boolean mAutoDecrement = false;
	private static Integer REP_DELAY = 50;

	public ArticoloAdapter(Context context, List<Articolo> articoliLs){
		this.context = context;
		this.articoliLs = articoliLs;
		this.articoliOriginaleLs = articoliLs; //articoliOriginaleLs;
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
			viewHolder.art_qty = (EditText)v.findViewById(R.id.art_qty);
			viewHolder.art_difet_qty = (TextView)v.findViewById(R.id.art_difet_qty);
			viewHolder.art_dataCarScar = (TextView)v.findViewById(R.id.art_dataCarScar);
			viewHolder.art_dataUltimoInvent = (TextView)v.findViewById(R.id.art_dataUltimoInvent);
			viewHolder.btnIncrementa = (Button)v.findViewById(R.id.btnIncrementa);
			viewHolder.btnDecrementa = (Button)v.findViewById(R.id.btnDecrementa);
			viewHolder.btnCheck = (ImageButton)v.findViewById(R.id.btnCheck);
			viewHolder.btnReset = (ImageButton)v.findViewById(R.id.btnReset);
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
		viewHolder.art_qty.addTextChangedListener(new TextWatcher(){
			public void onTextChanged(CharSequence c, int start, int before, int count){
				Float fLetto = Float.parseFloat(StringUtils.replace(c.toString(), ",", "."));
				a.setQtyRilevata(fLetto);
			}

			public void beforeTextChanged(CharSequence c, int start, int count, int after){
			}

			public void afterTextChanged(Editable c){
			}
		});
		InputMethodManager inputMethodManager = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.showSoftInput(viewHolder.art_qty, InputMethodManager.SHOW_FORCED);
		manageQtyTextColor(viewHolder, a);
		viewHolder.art_difet_qty.setText(floatToString(a.getQtyDifettOriginale()));
		viewHolder.art_dataCarScar.setText("data car: " + a.getDataCarico() + " - scar: " + a.getDataScarico());
		viewHolder.art_dataUltimoInvent.setText("data ultimo invent: " + a.getDataUltimoInventario());
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
				//manageClickIncDec(finalViewHolder, a, true, 5.0f);
				//return true;
				mAutoIncrement = true;
				repeatUpdateHandler.post(new RptUpdater(finalViewHolder, a));
				return false;
			}
		});
		viewHolder.btnIncrementa.setOnTouchListener(new View.OnTouchListener(){
			public boolean onTouch(View v, MotionEvent event){
				if((event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) && mAutoIncrement){
					mAutoIncrement = false;
				}
				return false;
			}
		});
		viewHolder.btnDecrementa.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				manageClickIncDec(finalViewHolder, a, false, 1.0f);
			}
		});
		viewHolder.btnDecrementa.setOnLongClickListener(new View.OnLongClickListener(){
			@Override
			public boolean onLongClick(View v){
				//manageClickIncDec(finalViewHolder, a, true, 5.0f);
				//return true;
				mAutoDecrement = true;
				repeatUpdateHandler.post(new RptUpdater(finalViewHolder, a));
				return false;
			}
		});
		viewHolder.btnDecrementa.setOnTouchListener(new View.OnTouchListener(){
			public boolean onTouch(View v, MotionEvent event){
				if((event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) && mAutoDecrement){
					mAutoDecrement = false;
				}
				return false;
			}
		});
		viewHolder.btnCheck.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				Boolean qtyRilUgualeOrig = false;
				if(Float.compare(a.getQtyRilevata(), a.getQtyOriginale()) == 0){
					a.setStato(0);
					qtyRilUgualeOrig = true;
				}else{
					a.setStato(1);
					qtyRilUgualeOrig = false;
				}
				a.setTimestamp(new Timestamp((new Date().getTime())));
				//manageBckgRow(finalViewHolder, a, v);

				Integer ordinamento = 0;
				Integer ordinamentoDestinazione = 0;
				if(!articoliLs.isEmpty()){
					Iterator<Articolo> iter_articoliLs = articoliLs.iterator();
					Articolo art = null;
					while(iter_articoliLs.hasNext()){
						art = iter_articoliLs.next();
						if(StringUtils.equals(art.getCodice(), a.getCodice())){
							iter_articoliLs.remove();    // articoliLs.remove(ordinamento);

							Iterator<Articolo> iter_articoliDestinazioneLs = articoliLs.iterator();
							Articolo artDestinazione = null;
							while(iter_articoliDestinazioneLs.hasNext()){
								artDestinazione = iter_articoliDestinazioneLs.next();
								if((artDestinazione.getStato() == 1) && (!(qtyRilUgualeOrig))){
									break;
								}
								if((artDestinazione.getStato() == 0) && (qtyRilUgualeOrig)){
									break;
								}
								ordinamentoDestinazione++;
							}
							articoliLs.add(ordinamentoDestinazione, a);
							break;
						}
						ordinamento++;
					}
				}

				Animation animation = new ScaleAnimation(1, 1, 1, 0);
				animation.setDuration(200);
				//v.getParent().getParent().startAnimation(animation);
				View daMod = (View)v.getParent().getParent().getParent();
				daMod.startAnimation(animation);

				notifyDataSetChanged();
			}
		});
		viewHolder.btnReset.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				a.setStato(2);
				a.setQtyRilevata(a.getQtyOriginale());

				Integer ordinamento = 0;
				if(!articoliLs.isEmpty()){
					Iterator<Articolo> iter_articoliLs = articoliLs.iterator();
					Articolo art = null;
					while(iter_articoliLs.hasNext()){
						art = iter_articoliLs.next();
						if(StringUtils.equals(art.getCodice(), a.getCodice())){
							iter_articoliLs.remove();    // articoliLs.remove(ordinamento);
							break;
						}
						ordinamento++;
					}
				}
				articoliLs.add(0, a);

				notifyDataSetChanged();
			}
		});
		manageBckgRow(a, v);//, viewHolder.btnCheck);
		return v;
	}

	private void manageClickIncDec(ViewHolder vh, Articolo a, boolean incrementa, float step){
		// devo poter modificcare solo se non e' stato confermato in precedenza
		if(a.getStato() != 2){
			return;
		}
		if(incrementa){
			a.setQtyRilevata(a.getQtyRilevata() + step);
		}else{
			a.setQtyRilevata(a.getQtyRilevata() - step);
		}
		vh.art_qty.setText(floatToString(a.getQtyRilevata()));

		manageQtyTextColor(vh, a);
	}

	private void manageQtyTextColor(ViewHolder vh, Articolo a){
		if(a.getQtyRilevata() < 0.0f){
			vh.art_qty.setTextColor(ContextCompat.getColor(this.context, R.color.qtyTextColorNeg));
		}else{
			vh.art_qty.setTextColor(ContextCompat.getColor(this.context, R.color.qtyTextColor));
		}
	}

	private void manageBckgRow(Articolo a, View v){ // private void manageBckgRow(ViewHolder vh, Articolo a, View v){
		//View daMod = (View)v.getParent().getParent().getParent();
		if(!(a.getStato().equals(2))){
			if(Float.compare(a.getQtyRilevata(), a.getQtyOriginale()) == 0){
				v.setBackgroundColor(ContextCompat.getColor(this.context, R.color.bckgChkOK));
			}else{
				v.setBackgroundColor(ContextCompat.getColor(this.context, R.color.bckgChkDiffer));
			}
		}else{
			v.setBackgroundColor(Color.TRANSPARENT);
		}
	}

	private class ViewHolder{
		public TextView art_codart;
		public TextView art_bcod;
		public TextView art_desc;
		public TextView art_um_qty;
		public EditText art_qty;
		public TextView art_difet_qty;
		public TextView art_dataCarScar;
		public TextView art_dataUltimoInvent;

		public Button btnIncrementa;
		public Button btnDecrementa;
		public ImageButton btnCheck;
		public ImageButton btnReset;
	}

	class RptUpdater implements Runnable{
		ViewHolder vh;
		Articolo a;

		public RptUpdater(ViewHolder vh, Articolo a){
			this.vh = vh;
			this.a = a;
		}

		public void run(){
			if(mAutoIncrement){
				manageClickIncDec(vh, a, true, 1.0f);
				repeatUpdateHandler.postDelayed(new RptUpdater(vh, a), REP_DELAY);
			}else if(mAutoDecrement){
				manageClickIncDec(vh, a, false, 1.0f);
				repeatUpdateHandler.postDelayed(new RptUpdater(vh, a), REP_DELAY);
			}
		}
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

