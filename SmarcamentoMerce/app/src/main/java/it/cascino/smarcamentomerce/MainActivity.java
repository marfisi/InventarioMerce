package it.cascino.smarcamentomerce;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import it.cascino.smarcamentomerce.adapter.ArticoloAdapter;
import it.cascino.smarcamentomerce.it.cascino.smarcamentomerce.model.Barcode;

import it.cascino.smarcamentomerce.it.cascino.smarcamentomerce.model.Articolo;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView numDep = (TextView) findViewById(R.id.numDep);
        numDep.setText("02");
        TextView dataSync = (TextView) findViewById(R.id.dataSyncd);
        dataSync.setText("13:15 - 22/01/2016");

        final List<Articolo> articoliLs = new ArrayList<Articolo>();
        Barcode bcode[] = new Barcode[1];
        bcode[0] = new Barcode("12454", "ean13");
        articoliLs.add(new Articolo("cod 1", bcode, "desc 1", "PZ", 3.25f, 0.0f, "00/00/00", "01/08/15", 2, null));
        articoliLs.add(new Articolo("cod 2", bcode, "desc 2", "PZ", 0.25f, 0.0f, "00/00/00", "01/08/15", 2, null));
        articoliLs.add(new Articolo("cod 3", bcode, "desc 3", "PZ", 10f, 0.0f, "00/00/00", "01/08/15", 2, null));



        // ArrayAdapter<String> adapterArticoliLs = new ArrayAdapter<String>(this, R.layout.rowarticolo, articoliLs);
        ListView listViewArticoliLs = (ListView) findViewById(R.id.articoliList);
        listViewArticoliLs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "Sel: " + articoliLs.get(position).getCodice(), Toast.LENGTH_LONG).show();
            }
        });
        ArticoloAdapter adapterArticoliLs = new ArticoloAdapter(getApplicationContext(), articoliLs);
        listViewArticoliLs.setAdapter(adapterArticoliLs);

    }

}
