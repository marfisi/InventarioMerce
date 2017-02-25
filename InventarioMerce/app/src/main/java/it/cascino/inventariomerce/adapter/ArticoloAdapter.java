package it.cascino.inventariomerce.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;

import org.apache.commons.lang3.StringUtils;

import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;


import it.cascino.inventariomerce.R;
import it.cascino.inventariomerce.activity.AggiungiArticoloDaBarcodeActivity;
import it.cascino.inventariomerce.activity.ModificaArticoloActivity;
import it.cascino.inventariomerce.model.Articolo;
import it.cascino.inventariomerce.model.Inventario;
import it.cascino.inventariomerce.utils.Support;
import it.cascino.inventariomerce.utils.TipoStato;

public class ArticoloAdapter extends BaseAdapter implements Filterable{
	private List<Articolo> articoliLs;
	private Context context;
	private ArticoloFiltro articoloFiltro;
	private Inventario inventario;

	private Integer numeroRisultatoFiltro = 0;

	public ArticoloAdapter(Context context, List<Articolo> articoliLs){
		this.context = context;
		this.articoliLs = articoliLs;
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

	public View getViewOptimize(final int position, View v, final ViewGroup vg){
		String valAtt;
		String valRil;
		ViewHolder viewHolder = null;
		if(v == null){
			v = LayoutInflater.from(context).inflate(R.layout.rowarticolo, null);
			viewHolder = new ViewHolder();
			viewHolder.art_codart = (TextView)v.findViewById(R.id.art_codart);
			viewHolder.art_desc = (TextView)v.findViewById(R.id.art_desc);
			viewHolder.art_descImg = (ImageView)v.findViewById(R.id.art_descImg);
			viewHolder.art_prezzo = (TextView)v.findViewById(R.id.art_prez);
			viewHolder.art_prezzoImg = (ImageView)v.findViewById(R.id.art_prezImg);
			viewHolder.art_dataCar = (TextView)v.findViewById(R.id.art_dataCar);
			viewHolder.art_dataScar = (TextView)v.findViewById(R.id.art_dataScar);
			viewHolder.art_dataUltimoInvent = (TextView)v.findViewById(R.id.art_dataUltimoInvent);
			viewHolder.art_bcod = (TextView)v.findViewById(R.id.art_bcod);
			viewHolder.art_bcodImg = (ImageView)v.findViewById(R.id.art_bcodImg);
			viewHolder.art_commento = (TextView)v.findViewById(R.id.art_commento);
			viewHolder.art_commentoImg = (ImageView)v.findViewById(R.id.art_commentoImg);
			viewHolder.art_um_qty = (TextView)v.findViewById(R.id.art_um_qty);
			viewHolder.qtyAttese = (TextView)v.findViewById(R.id.qtyAttese);
			viewHolder.qtyRilevate = (TextView)v.findViewById(R.id.qtyRilevate);
			viewHolder.qtyImg = (ImageView)v.findViewById(R.id.qtyImg);
			//viewHolder.qtyEsposteAttese = (TextView)v.findViewById(R.id.qtyEsposteAttese);
			viewHolder.qtyEsposteRilevate = (TextView)v.findViewById(R.id.qtyEsposteRilevate);
			//viewHolder.qtyMagazzinoAttese = (TextView)v.findViewById(R.id.qtyMagazzinoAttese);
			viewHolder.qtyMagazzinoRilevate = (TextView)v.findViewById(R.id.qtyMagazzinoRilevate);
			viewHolder.qtyDifettoseAttese = (TextView)v.findViewById(R.id.qtyDifettoseAttese);
			//viewHolder.qtyDifettoseRilevate = (TextView)v.findViewById(R.id.qtyDifettoseRilevate);
			viewHolder.qtyInTrasferimentoArrivoAttese = (TextView)v.findViewById(R.id.qtyInTrasferimentoArrivoAttese);
			viewHolder.qtyInTrasferimentoPartenzaAttese = (TextView)v.findViewById(R.id.qtyInTrasferimentoPartenzaAttese);
			viewHolder.qtyScortaMinAttese = (TextView)v.findViewById(R.id.qtyScortaMinAttese);
			viewHolder.qtyScortaMinRilevate = (TextView)v.findViewById(R.id.qtyScortaMinRilevate);
			viewHolder.qtyScortaMinImg = (ImageView)v.findViewById(R.id.qtyScortaMinImg);
			viewHolder.qtyScortaMaxAttese = (TextView)v.findViewById(R.id.qtyScortaMaxAttese);
			viewHolder.qtyScortaMaxRilevate = (TextView)v.findViewById(R.id.qtyScortaMaxRilevate);
			viewHolder.qtyScortaMaxImg = (ImageView)v.findViewById(R.id.qtyScortaMaxImg);
			viewHolder.qtyPerConfAttese = (TextView)v.findViewById(R.id.qtyPerConfAttese);
			viewHolder.qtyPerConfRilevate = (TextView)v.findViewById(R.id.qtyPerConfRilevate);
			viewHolder.qtyPerConfImg = (ImageView)v.findViewById(R.id.qtyPerConfImg);
			viewHolder.layAccettaSoloEsposte = (View)v.findViewById(R.id.layAccettaSoloEsposte);
			viewHolder.txtQtyAccettaSoloEsposte = (TextView)v.findViewById(R.id.txtQtyAccettaSoloEsposte);
			viewHolder.btnAccettaSoloEsposte = (ImageButton)v.findViewById(R.id.btnAccettaSoloEsposte);
			viewHolder.btnModif = (ImageButton)v.findViewById(R.id.btnModif);
			v.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)v.getTag();
		}
		final Articolo a = (Articolo)getItem(position);
		viewHolder.art_codart.setText(a.getCodice());

		valAtt = a.getDescOriginale() != null ? a.getDescOriginale() : "n.d.";
		valRil = a.getDescRilevata() != null ? a.getDescRilevata() : "n.r.";
		viewHolder.art_descImg.setBackgroundResource(Support.definisciImg(valAtt, valRil));
		valRil = a.getDescRilevata() != null ? a.getDescRilevata() : a.getDescOriginale();
		viewHolder.art_desc.setText(valRil);

		valAtt = a.getPrezzoOriginale() != null ? Support.floatToString(a.getPrezzoOriginale()) : "n.d.";
		valRil = a.getPrezzoRilevata() != null ? Support.floatToString(a.getPrezzoRilevata()) : "n.r.";
		viewHolder.art_prezzoImg.setBackgroundResource(Support.definisciImg(valAtt, valRil));
		valRil = a.getPrezzoRilevata() != null ? Support.floatToString(a.getPrezzoRilevata()) : Support.floatToString(a.getPrezzoOriginale());
		viewHolder.art_prezzo.setText(valRil);

		viewHolder.art_dataCar.setText(a.getDataCarico());
		viewHolder.art_dataScar.setText(a.getDataScarico());
		viewHolder.art_dataUltimoInvent.setText(a.getDataUltimoInventario());

		String bcod = "";
		if(a.getBarcodeRilevata() != null){
			for(int i = 0, n = a.getBarcodeRilevata().length; i < n; i++){
				bcod = bcod + "-" + a.getBarcodeRilevata()[i].getCodice();
			}
		}
		viewHolder.art_bcod.setText(StringUtils.removeStart(bcod, "-"));
		valAtt = a.getBarcodeOriginale() != null ? Integer.toString(a.getBarcodeOriginale().length) : "0";
		valRil = a.getBarcodeRilevata() != null ? Integer.toString(a.getBarcodeRilevata().length) : "0";
		viewHolder.art_bcodImg.setBackgroundResource(Support.definisciImg(valAtt, valRil));
		if(valAtt.length() == valRil.length()){
			viewHolder.art_bcodImg.setBackgroundResource(Support.definisciImg(valAtt, "n.r."));
		}

		valAtt = "n.r.";
		valRil = a.getCommento() != null ? a.getCommento() : "n.r.";
		viewHolder.art_commentoImg.setBackgroundResource(Support.definisciImg(valAtt, valRil));
		valRil = a.getCommento() != null ? a.getCommento() : "";
		viewHolder.art_commento.setText(valRil);

		viewHolder.art_um_qty.setText(a.getUm() + ")");

		valAtt = a.getQtyOriginale() != null ? Support.floatToString(a.getQtyOriginale()) : "n.d.";
		viewHolder.qtyAttese.setText(valAtt);
		valRil = a.getQtyRilevata() != null ? Support.floatToString(a.getQtyRilevata()) : "n.r.";
		viewHolder.qtyRilevate.setText(valRil);
		viewHolder.qtyImg.setBackgroundResource(Support.definisciImg(valAtt, valRil));

		//viewHolder.qtyEsposteAttese.setText(a.getQtyEsposteOriginale() != null ? Support.floatToString(a.getQtyEsposteOriginale()) : "n.d.");
		viewHolder.qtyEsposteRilevate.setText(a.getQtyEsposteRilevata() != null ? Support.floatToString(a.getQtyEsposteRilevata()) : "n.r.");

		//viewHolder.qtyMagazzinoAttese.setText(a.getQtyMagazOriginale() != null ? Support.floatToString(a.getQtyMagazOriginale()) : "n.d.");
		viewHolder.qtyMagazzinoRilevate.setText(a.getQtyMagazRilevata() != null ? Support.floatToString(a.getQtyMagazRilevata()) : "n.r.");

		viewHolder.qtyDifettoseAttese.setText(a.getQtyDifettOriginale() != null ? Support.floatToString(a.getQtyDifettOriginale()) : "n.d.");
		//viewHolder.qtyDifettoseRilevate.setText(a.getQtyDifettRilevata() != null ? Support.floatToString(a.getQtyDifettRilevata()) : "n.r.");

		viewHolder.qtyInTrasferimentoArrivoAttese.setText("A: +" + (a.getQtyInTrasferimentoArrivoOriginale() != null ? Support.floatToString(a.getQtyInTrasferimentoArrivoOriginale()) : "n.d."));

		viewHolder.qtyInTrasferimentoPartenzaAttese.setText("P: -" + (a.getQtyInTrasferimentoPartenzaOriginale() != null ? Support.floatToString(a.getQtyInTrasferimentoPartenzaOriginale()) : "n.d."));

		valAtt = a.getScortaMinOriginale() != null ? Support.floatToString(a.getScortaMinOriginale()) : "n.d.";
		viewHolder.qtyScortaMinAttese.setText(valAtt);
		valRil = a.getScortaMinRilevata() != null ? Support.floatToString(a.getScortaMinRilevata()) : "n.r.";
		viewHolder.qtyScortaMinRilevate.setText(valRil);
		viewHolder.qtyScortaMinImg.setBackgroundResource(Support.definisciImg(valAtt, valRil));

		valAtt = a.getScortaMaxOriginale() != null ? Support.floatToString(a.getScortaMaxOriginale()) : "n.d.";
		viewHolder.qtyScortaMaxAttese.setText(valAtt);
		valRil = a.getScortaMaxRilevata() != null ? Support.floatToString(a.getScortaMaxRilevata()) : "n.r.";
		viewHolder.qtyScortaMaxRilevate.setText(valRil);
		viewHolder.qtyScortaMaxImg.setBackgroundResource(Support.definisciImg(valAtt, valRil));

		valAtt = a.getQtyPerConfezOriginale() != null ? Support.floatToString(a.getQtyPerConfezOriginale()) : "n.d.";
		viewHolder.qtyPerConfAttese.setText(valAtt);
		valRil = a.getQtyPerConfezRilevata() != null ? Support.floatToString(a.getQtyPerConfezRilevata()) : "n.r.";
		viewHolder.qtyPerConfRilevate.setText(valRil);
		viewHolder.qtyPerConfImg.setBackgroundResource(Support.definisciImg(valAtt, valRil));

		if(a.getStato() == TipoStato.DA_INVENTARIARE){
			viewHolder.layAccettaSoloEsposte.setVisibility(View.VISIBLE);
			Float qtyAccettaSoloEsposte = 0.0f;
			if((a.getQtyOriginale() != null) && (a.getQtyDifettOriginale() != null)){
				qtyAccettaSoloEsposte = a.getQtyOriginale() - a.getQtyDifettOriginale();
				valAtt = Support.floatToString(qtyAccettaSoloEsposte);
			}
			viewHolder.txtQtyAccettaSoloEsposte.setText(valAtt);

			final Float finalQtyAccettaSoloEsposte = qtyAccettaSoloEsposte;
			viewHolder.btnAccettaSoloEsposte.setOnLongClickListener(new View.OnLongClickListener(){
				@Override
				public boolean onLongClick(View v){
					a.setTimestamp(new Timestamp((new Date().getTime())));
					a.setQtyEsposteRilevata(finalQtyAccettaSoloEsposte);
					a.setQtyRilevata(finalQtyAccettaSoloEsposte + a.getQtyDifettOriginale());
					a.setStato();
					if(accettaSoloEsposte != null){
						accettaSoloEsposte.accettaSoloEsposte(a.getOrdinamento() - 1);	// il numero ordine e' parte da 1 e non da 0
					}
					return true;
				}
			});
		}else{
			viewHolder.layAccettaSoloEsposte.setVisibility(View.INVISIBLE);
		}
		viewHolder.btnModif.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				Gson gSon = new Gson();
				Articolo articolo = articoliLs.get(position);
				articolo.setInModifica(true);
				String gSonString = gSon.toJson(articolo);
				Intent intentModifActivity = new Intent(v.getContext(), ModificaArticoloActivity.class);
				intentModifActivity.putExtra("articolo", gSonString);
				gSonString = gSon.toJson(inventario.getNumeroArticoliInventariati());
				intentModifActivity.putExtra("numeroArticoliInventariati", gSonString);
				((Activity)vg.getContext()).startActivityForResult(intentModifActivity, 3);    // 3=MainActivity.ART_MODIF_REQUEST
			}
		});

		manageBckgRow(a, v);//, viewHolder.btnCheck);
		return v;
	}

	private void manageBckgRow(Articolo a, View v){
		switch(a.getStato()){
			case TipoStato.INVENTARIATO_OK:
				v.setBackgroundColor(ContextCompat.getColor(this.context, R.color.colBckgInventarioOK));
				break;
			case TipoStato.INVENTARIATO_DIFFER:
				v.setBackgroundColor(ContextCompat.getColor(this.context, R.color.colBckgInventarioDiffer));
				break;
			case TipoStato.DA_INVENTARIARE:
				v.setBackgroundColor(Color.TRANSPARENT);
				break;
			default:
				v.setBackgroundColor(Color.TRANSPARENT);
				break;
		}
	}

	private class ViewHolder{
		public TextView art_codart;
		public TextView art_desc;
		public ImageView art_descImg;
		public TextView art_prezzo;
		public ImageView art_prezzoImg;
		public TextView art_dataCar;
		public TextView art_dataScar;
		public TextView art_dataUltimoInvent;
		public TextView art_bcod;
		public ImageView art_bcodImg;
		public TextView art_commento;
		public ImageView art_commentoImg;
		public TextView art_um_qty;
		public TextView qtyAttese;
		public TextView qtyRilevate;
		public ImageView qtyImg;
		//public TextView qtyEsposteAttese;
		public TextView qtyEsposteRilevate;
		//public TextView qtyMagazzinoAttese;
		public TextView qtyMagazzinoRilevate;
		public TextView qtyDifettoseAttese;
		//public TextView qtyDifettoseRilevate;
		public TextView qtyInTrasferimentoArrivoAttese;
		public TextView qtyInTrasferimentoPartenzaAttese;
		public TextView qtyScortaMinAttese;
		public TextView qtyScortaMinRilevate;
		public ImageView qtyScortaMinImg;
		public TextView qtyScortaMaxAttese;
		public TextView qtyScortaMaxRilevate;
		public ImageView qtyScortaMaxImg;
		public TextView qtyPerConfAttese;
		public TextView qtyPerConfRilevate;
		public ImageView qtyPerConfImg;
		public View layAccettaSoloEsposte;
		public TextView txtQtyAccettaSoloEsposte;
		public ImageButton btnAccettaSoloEsposte;
		public ImageButton btnModif;
	}

	@Override
	public void notifyDataSetChanged(){
		super.notifyDataSetChanged();
	}

	public void updateArticoliLs(List<Articolo> newArticoliLs){
		articoliLs.clear();
		articoliLs.addAll(newArticoliLs);
		notifyDataSetChanged();
	}

	private class ArticoloFiltro extends Filter{
		@Override
		protected FilterResults performFiltering(CharSequence constraint){
			FilterResults result = new FilterResults();
			if((constraint != null) && (constraint.toString().length() > 0)){
				ArrayList<Articolo> filteredItems = new ArrayList<Articolo>();

				constraint = StringUtils.upperCase(constraint.toString());
				Log.i("ricerca", constraint.toString());
				for(int i = 0; i < articoliLs.size(); i++){
					Articolo articolo = articoliLs.get(i);
					if(StringUtils.contains(articolo.toStringPerFilter(), constraint)){
						filteredItems.add(articolo);
					}
				}
				result.values = filteredItems;
				result.count = filteredItems.size();
				numeroRisultatoFiltro = result.count;
			}else{
				synchronized(this){
					result.values = articoliLs;
					result.count = articoliLs.size();
					numeroRisultatoFiltro = result.count;
				}
			}
			if(modifcaNumeroRisultatoFiltro != null){
				modifcaNumeroRisultatoFiltro.modifcaNumeroRisultatoFiltro(numeroRisultatoFiltro);
			}
			return result;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint, FilterResults results){
			articoliLs = (ArrayList)results.values;
			notifyDataSetChanged();
		}
	}

	// gestione del callback
	public interface ModifcaNumeroRisultatoFiltro{
		public void modifcaNumeroRisultatoFiltro(Integer qty);
	}

	ModifcaNumeroRisultatoFiltro modifcaNumeroRisultatoFiltro;

	public void setOnModifcaNumeroRisultatoFiltroListener(ModifcaNumeroRisultatoFiltro onDataChangeListener){
		modifcaNumeroRisultatoFiltro = onDataChangeListener;
	}

	public interface GestioneBtnAccettaSoloEsposte{
		public void accettaSoloEsposte(Integer position);
	}

	GestioneBtnAccettaSoloEsposte accettaSoloEsposte;

	public void setOnAccettaSoloEsposteListener(GestioneBtnAccettaSoloEsposte onDataChangeListener){
		accettaSoloEsposte = onDataChangeListener;
	}


	public void setInventario(Inventario inventario){
		this.inventario = inventario;
	}
}

