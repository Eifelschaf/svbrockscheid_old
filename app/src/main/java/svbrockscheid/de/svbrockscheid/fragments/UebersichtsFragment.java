package svbrockscheid.de.svbrockscheid.fragments;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import svbrockscheid.de.svbrockscheid.R;
import svbrockscheid.de.svbrockscheid.ValueParser;


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
                Map<String, String> returnValues = new HashMap<String, String>();
                for (String param : params) {
                    try {
                        BufferedReader instream = new BufferedReader(new InputStreamReader(new URL("http://svbrockscheid.de/" + param).openConnection().getInputStream()));
                        StringBuilder builder = new StringBuilder();
                        String line;
                        while ((line = instream.readLine()) != null) {
                            builder.append(line);
                        }
                        //den stringbuilder parsen
                        returnValues.putAll(ValueParser.parse(builder.toString()));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return returnValues;
            }

            @Override
            protected void onPostExecute(Map<String, String> values) {
                super.onPostExecute(values);
                //werte einfügen
                View view1 = getView();
                if (view1 != null) {
                    if (values.containsKey(POSITION)) {
                        TextView sv = (TextView) view1.findViewById(R.id.sv);
                        sv.setText(getString(R.string.position, values.get(POSITION), getString(R.string.sv_brockscheid)));
                    }
                    if (values.containsKey(PUNKTE)) {
                        TextView punkteBrockscheid = (TextView) view1.findViewById(R.id.punkteBrockscheid);
                        punkteBrockscheid.setText(values.get(PUNKTE));
                    }
                    if (values.containsKey(POSITION_VOR) && values.containsKey(MANNSCHAFT_VOR)) {
                        view1.findViewById(R.id.posVor).setVisibility(View.VISIBLE);
                        view1.findViewById(R.id.punkteVor).setVisibility(View.VISIBLE);
                        TextView posVor = (TextView) view1.findViewById(R.id.posVor);
                        posVor.setText(getString(R.string.position, values.get(POSITION_VOR), values.get(MANNSCHAFT_VOR)));
                        if (values.containsKey(PUNKTE_VOR)) {
                            TextView punkteVor = (TextView) view1.findViewById(R.id.punkteVor);
                            punkteVor.setText(values.get(PUNKTE_VOR));
                        }
                    } else {
                        //hurra, wir sind erster!
                        view1.findViewById(R.id.posVor).setVisibility(View.GONE);
                        view1.findViewById(R.id.punkteVor).setVisibility(View.GONE);
                    }
                    if (values.containsKey(POSITION_NACH) && values.containsKey(MANNSCHAFT_NACH)) {
                        view1.findViewById(R.id.posNach).setVisibility(View.VISIBLE);
                        view1.findViewById(R.id.punkteNach).setVisibility(View.VISIBLE);
                        TextView posNach = (TextView) view1.findViewById(R.id.posNach);
                        posNach.setText(getString(R.string.position, values.get(POSITION_NACH), values.get(MANNSCHAFT_NACH)));
                        if (values.containsKey(PUNKTE_NACH)) {
                            TextView punkteNach = (TextView) view1.findViewById(R.id.punkteNach);
                            punkteNach.setText(values.get(PUNKTE_NACH));
                        }
                    } else {
                        //traurig, aber wir sind doch tatsächlich letzter...
                        view1.findViewById(R.id.posNach).setVisibility(View.GONE);
                        view1.findViewById(R.id.punkteNach).setVisibility(View.GONE);
                    }
                    if (values.containsKey(NAECHSTE_MANNSCHAFT)) {
                        TextView naechsteMannschaft = (TextView) view1.findViewById(R.id.naechsteMannschaft);
                        naechsteMannschaft.setText(values.get(NAECHSTE_MANNSCHAFT));
                    }
                    if (values.containsKey(NAECHSTER_ORT) && values.containsKey(NAECHSTES_DATUM)) {
                        TextView naechsteInfos = (TextView) view1.findViewById(R.id.naechsteInfos);
                        naechsteInfos.setText(values.get(NAECHSTES_DATUM) + "\n" + values.get(NAECHSTER_ORT));
                    }
                    if (values.containsKey(LETZTE_MANNSCHAFT)) {
                        TextView letzteMannschaft = (TextView) view1.findViewById(R.id.letzteMannschaft);
                        letzteMannschaft.setText(values.get(LETZTE_MANNSCHAFT));
                    }
                    if (values.containsKey(LETZTER_ORT) && values.containsKey(LETZTES_ERGEBNIS)) {
                        TextView letzteInfos = (TextView) view1.findViewById(R.id.letzteInfos);
                        letzteInfos.setText(values.get(LETZTES_ERGEBNIS) + "\n" + values.get(LETZTER_ORT));
                    }
                }
            }
        }.execute("app.php");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_uebersicht, container, false);
    }
}
