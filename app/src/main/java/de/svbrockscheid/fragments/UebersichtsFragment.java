package de.svbrockscheid.fragments;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Map;

import de.svbrockscheid.APIClient;
import de.svbrockscheid.R;


public class UebersichtsFragment extends Fragment {

    //brockscheider infos
    private static final String POSITION = "posbro";
    private static final String PUNKTE = "pktbro";
    //Vorplatzierter
    private static final String POSITION_VOR = "posvor";
    private static final String MANNSCHAFT_VOR = "mschvor";
    private static final String PUNKTE_VOR = "pktvor";
    //Nachplatzierter
    private static final String POSITION_NACH = "posnach";
    private static final String MANNSCHAFT_NACH = "mschnach";
    private static final String PUNKTE_NACH = "pktnach";
    //nächstes Spiel
    private static final String NAECHSTE_MANNSCHAFT = "mschnext";
    private static final String NAECHSTES_DATUM = "dat";
    private static final String NAECHSTER_ORT = "ort1";
    //Letztes Spiel
    private static final String LETZTE_MANNSCHAFT = "mannschaft";
    private static final String LETZTES_ERGEBNIS = "erg";
    private static final String LETZTER_ORT = "ort2";

    public UebersichtsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new AsyncTask<String, Void, Map<String, String>>() {

            @Override
            protected Map<String, String> doInBackground(String... params) {
                return APIClient.getUebersicht();
            }

            @Override
            protected void onPostExecute(Map<String, String> values) {
                super.onPostExecute(values);
                //werte einfügen
                setupView(values, getView());
            }
        }.execute();
    }

    /**
     * Die View mit Werten befüllen
     *
     * @param values Alle Variablenwerte in einer Map
     * @param view   Die View, die mit Daten befüllt werden soll.
     */
    private void setupView(Map<String, String> values, View view) {
        if (view != null) {
            if (values.containsKey(POSITION)) {
                TextView sv = (TextView) view.findViewById(R.id.sv);
                if (sv != null) {
                    sv.setText(getString(R.string.position, values.get(POSITION), getString(R.string.sv_brockscheid)));
                }
            }
            if (values.containsKey(PUNKTE)) {
                TextView punkteBrockscheid = (TextView) view.findViewById(R.id.punkteBrockscheid);
                if (punkteBrockscheid != null) {
                    punkteBrockscheid.setText(values.get(PUNKTE));
                }
            }
            TextView posVor = (TextView) view.findViewById(R.id.posVor);
            TextView punkteVor = (TextView) view.findViewById(R.id.punkteVor);
            if (values.containsKey(POSITION_VOR) && values.containsKey(MANNSCHAFT_VOR)) {
                if (posVor != null) {
                    posVor.setVisibility(View.VISIBLE);
                    posVor.setText(getString(R.string.position, values.get(POSITION_VOR), values.get(MANNSCHAFT_VOR)));
                }
                if (punkteVor != null) {
                    punkteVor.setVisibility(View.VISIBLE);
                    if (values.containsKey(PUNKTE_VOR)) {
                        punkteVor.setText(values.get(PUNKTE_VOR));
                    }
                }
            } else {
                //hurra, wir sind erster!
                if (posVor != null) {
                    posVor.setVisibility(View.GONE);
                }
                if (punkteVor != null) {
                    punkteVor.setVisibility(View.GONE);
                }
            }
            TextView posNach = (TextView) view.findViewById(R.id.posNach);
            TextView punkteNach = (TextView) view.findViewById(R.id.punkteNach);
            if (values.containsKey(POSITION_NACH) && values.containsKey(MANNSCHAFT_NACH)) {
                if (posNach != null) {
                    posNach.setVisibility(View.VISIBLE);
                    posNach.setText(getString(R.string.position, values.get(POSITION_NACH), values.get(MANNSCHAFT_NACH)));
                }
                if (punkteNach != null) {
                    punkteNach.setVisibility(View.VISIBLE);
                    if (values.containsKey(PUNKTE_NACH)) {
                        punkteNach.setText(values.get(PUNKTE_NACH));
                    }
                }
            } else {
                //traurig, aber wir sind doch tatsächlich letzter...
                if (posNach != null) {
                    posNach.setVisibility(View.GONE);
                }
                if (punkteNach != null) {
                    punkteNach.setVisibility(View.GONE);
                }
            }
            if (values.containsKey(NAECHSTE_MANNSCHAFT)) {
                TextView naechsteMannschaft = (TextView) view.findViewById(R.id.naechsteMannschaft);
                if (naechsteMannschaft != null) {
                    naechsteMannschaft.setText(values.get(NAECHSTE_MANNSCHAFT));
                }
            }
            if (values.containsKey(NAECHSTER_ORT) && values.containsKey(NAECHSTES_DATUM)) {
                TextView naechsteInfos = (TextView) view.findViewById(R.id.naechsteInfos);
                if (naechsteInfos != null) {
                    naechsteInfos.setText(values.get(NAECHSTES_DATUM) + "\n" + values.get(NAECHSTER_ORT));
                }
            }
            if (values.containsKey(LETZTE_MANNSCHAFT)) {
                TextView letzteMannschaft = (TextView) view.findViewById(R.id.letzteMannschaft);
                if (letzteMannschaft != null) {
                    letzteMannschaft.setText(values.get(LETZTE_MANNSCHAFT));
                }
            }
            if (values.containsKey(LETZTER_ORT) && values.containsKey(LETZTES_ERGEBNIS)) {
                TextView letzteInfos = (TextView) view.findViewById(R.id.letzteInfos);
                if (letzteInfos != null) {
                    letzteInfos.setText(values.get(LETZTES_ERGEBNIS) + "\n" + values.get(LETZTER_ORT));
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_uebersicht, container, false);
    }
}
