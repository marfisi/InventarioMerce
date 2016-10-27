package it.cascino.smarcamentomerce.adapter;

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


import it.cascino.smarcamentomerce.R;
import it.cascino.smarcamentomerce.activity.AggiungiArticoloDaBarcodeActivity;
import it.cascino.smarcamentomerce.activity.ModificaArticoloActivity;
import it.cascino.smarcamentomerce.model.Articolo;
import it.cascino.smarcamentomerce.utils.Support;
import it.cascino.smarcamentomerce.utils.TipoStato;

public class ArticoloAdapter extends BaseAdapter implements Filterable{
	private List<Articolo> articoliLs;
	private List<Articolo> articoliOriginaleLs;
	private Context context;
	private ArticoloFiltro articoloFiltro;

	private Integer numeroInvetariare = 0;

	private Integer numeroRisultatoFiltro = 0;

	public ArticoloAdapter(Context context, List<Articolo> articoliLs){
		this.context = context;
		this.articoliLs = articoliLs;
		this.articoliOriginaleLs = articoliLs; //articoliOriginaleLs;
	}

	@Override
	public Filter getFilter(){
		// Log.i("getFilter", "articoloFiltro: " + articoloFiltro);
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
			viewHolder.art_prezzo  = (TextView)v.findViewById(R.id.art_prez);
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
			viewHolder.qtyScortaMinAttese = (TextView)v.findViewById(R.id.qtyScortaMinAttese);
			viewHolder.qtyScortaMinRilevate = (TextView)v.findViewById(R.id.qtyScortaMinRilevate);
			viewHolder.qtyScortaMinImg = (ImageView)v.findViewById(R.id.qtyScortaMinImg);
			viewHolder.qtyScortaMaxAttese = (TextView)v.findViewById(R.id.qtyScortaMaxAttese);
			viewHolder.qtyScortaMaxRilevate = (TextView)v.findViewById(R.id.qtyScortaMaxRilevate);
			viewHolder.qtyScortaMaxImg = (ImageView)v.findViewById(R.id.qtyScortaMaxImg);
			viewHolder.qtyPerConfAttese = (TextView)v.findViewById(R.id.qtyPerConfAttese);
			viewHolder.qtyPerConfRilevate = (TextView)v.findViewById(R.id.qtyPerConfRilevate);
			viewHolder.qtyPerConfImg = (ImageView)v.findViewById(R.id.qtyPerConfImg);
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

		viewHolder.btnModif.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				Gson gSon = new Gson();
				Articolo articolo = articoliLs.get(position);
				articolo.setInModifica(true);
				String gSonString = gSon.toJson(articolo);
				Intent intentLoginFileActivity = new Intent(v.getContext(), ModificaArticoloActivity.class);
				intentLoginFileActivity.putExtra("articolo", gSonString);
				((Activity)vg.getContext()).startActivityForResult(intentLoginFileActivity, 3);	// 3=MainActivity.ART_MODIF_REQUEST
			}
		});
		/*viewHolder.btnModif.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				if(a.getStato() == 2){
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
					if(!articoliOriginaleLs.isEmpty()){
						Iterator<Articolo> iter_articoliLs = articoliOriginaleLs.iterator();
						Articolo art = null;
						while(iter_articoliLs.hasNext()){
							art = iter_articoliLs.next();
							if(StringUtils.equals(art.getCodice(), a.getCodice())){
								iter_articoliLs.remove();    // articoliLs.remove(ordinamento);

								Iterator<Articolo> iter_articoliDestinazioneLs = articoliOriginaleLs.iterator();    // .listIterator(ordinamento);
								Articolo artDestinazione = null;
								Articolo artDestinazionePrecedente = new Articolo();
								artDestinazionePrecedente.setStato(999);
								while(iter_articoliDestinazioneLs.hasNext()){
									artDestinazione = iter_articoliDestinazioneLs.next();

									if((artDestinazione.getStato() == 1) && (!(qtyRilUgualeOrig))){
										break;
									}
									if((artDestinazione.getStato() == 0) && (qtyRilUgualeOrig)){
										break;
									}
									// gestione del rosso sempre sopra il verde
									if((artDestinazione.getStato() == 0) && (artDestinazionePrecedente.getStato() == 2) && (!(qtyRilUgualeOrig))){
										//ordinamentoDestinazione = ordinamentoDestinazione - 2;
										break;
									}
									ordinamentoDestinazione++;
									artDestinazionePrecedente = artDestinazione;
								}
								articoliOriginaleLs.add(ordinamentoDestinazione, a);
								break;
							}
							ordinamento++;
						}
					}

					Animation animation = new ScaleAnimation(1, 1, 1, 0);
					animation.setDuration(400);
					//v.getParent().getParent().startAnimation(animation);
					View daMod = (View)v.getParent().getParent().getParent();
					daMod.startAnimation(animation);

					notifyDataSetChanged();

					numeroInvetariare++;
					if(modifcaQuantitaInventariati != null){
						modifcaQuantitaInventariati.modifcaQtyInventariati(numeroInvetariare);
					}
				}
			}
		});*/
		/*viewHolder.btnReset.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				if(a.getStato() != 2){
					a.setStato(2);
					a.setQtyRilevata(a.getQtyOriginale());
					a.setQtyDifettRilevata(a.getQtyDifettOriginale());
					a.setScortaMinRilevata(a.getScortaMinOriginale());
					a.setScortaMaxRilevata(a.getScortaMaxOriginale());
					a.setCommento("");

					Integer ordinamento = 0;
					if(!articoliOriginaleLs.isEmpty()){
						Iterator<Articolo> iter_articoliLs = articoliOriginaleLs.iterator();
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
					articoliOriginaleLs.add(0, a);

					notifyDataSetChanged();

					numeroInvetariare--;
					if(modifcaQuantitaInventariati != null){
						modifcaQuantitaInventariati.modifcaQtyInventariati(numeroInvetariare);
					}
				}
			}
		});*/
		manageBckgRow(a, v);//, viewHolder.btnCheck);
		return v;
	}

	public Integer getNumeroInvetariare(){
		return numeroInvetariare;
	}

	public void setNumeroInvetariare(Integer numeroInvetariare){
		this.numeroInvetariare = numeroInvetariare;
	}

	/*
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
	*/

	/*private void manageBckgRow(Articolo a, View v){ // private void manageBckgRow(ViewHolder vh, Articolo a, View v){
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
	}*/

	private void manageBckgRow(Articolo a, View v){
		switch(a.getStato()){
			case TipoStato.INVENTARIATO_OK:
				v.setBackgroundColor(ContextCompat.getColor(this.context, R.color.bckgChkOK));
				break;
			case TipoStato.INVENTARIATO_DIFFER:
				v.setBackgroundColor(ContextCompat.getColor(this.context, R.color.bckgChkDiffer));
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
		public TextView qtyScortaMinAttese;
		public TextView qtyScortaMinRilevate;
		public ImageView qtyScortaMinImg;
		public TextView qtyScortaMaxAttese;
		public TextView qtyScortaMaxRilevate;
		public ImageView qtyScortaMaxImg;
		public TextView qtyPerConfAttese;
		public TextView qtyPerConfRilevate;
		public ImageView qtyPerConfImg;
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

				constraint = constraint.toString().toLowerCase();
				Log.i("ricerca", constraint.toString());
				for(int i = 0; i < articoliOriginaleLs.size(); i++){
					Articolo articolo = articoliOriginaleLs.get(i);
					if(articolo.toString().toLowerCase().contains(constraint)){
						filteredItems.add(articolo);
						//Log.i("trovato", articolo.toString());
					}
				}
				result.values = filteredItems;
				result.count = filteredItems.size();
				numeroRisultatoFiltro = result.count;
			}else{
				synchronized(this){
					result.values = articoliOriginaleLs;
					result.count = articoliOriginaleLs.size();
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

	// gestione del callback per la modifica della textview nella main activity gui
	public interface ModifcaQuantitaInventariati{
		public void modifcaQtyInventariati(Integer qty);
	}
	ModifcaQuantitaInventariati modifcaQuantitaInventariati;
	public void setOnModifcaQuantitaListener(ModifcaQuantitaInventariati onDataChangeListener){
		modifcaQuantitaInventariati = onDataChangeListener;
	}

	// gestione del callback
	public interface ModifcaNumeroRisultatoFiltro{
		public void modifcaNumeroRisultatoFiltro(Integer qty);
	}
	ModifcaNumeroRisultatoFiltro modifcaNumeroRisultatoFiltro;
	public void setOnModifcaNumeroRisultatoFiltroListener(ModifcaNumeroRisultatoFiltro onDataChangeListener){
		modifcaNumeroRisultatoFiltro = onDataChangeListener;
	}
}

