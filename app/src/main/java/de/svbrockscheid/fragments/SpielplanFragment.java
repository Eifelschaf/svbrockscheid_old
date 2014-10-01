package de.svbrockscheid.fragments;


import android.app.Fragment;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import de.svbrockscheid.APIClient;
import de.svbrockscheid.R;
import de.svbrockscheid.model.LigaSpiel;

/**
 * A simple {@link Fragment} subclass.
 */
public class SpielplanFragment extends Fragment {

    public SpielplanFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_spielplan, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new AsyncTask<Void, Void, LigaSpiel[]>() {
            @Override
            protected LigaSpiel[] doInBackground(Void... params) {
                return APIClient.getLigaSpiele("kreispokal.json");
            }

            @Override
            protected void onPostExecute(LigaSpiel[] ligaSpiele) {
                super.onPostExecute(ligaSpiele);
                View view1 = getView();
                if (view1 != null) {
                    LinearLayout table = (LinearLayout) view1.findViewById(R.id.kreispokal);
                    if (table != null) {
                        table.removeAllViews();
                        setupLine(ligaSpiele, table);
                    }
                }
            }
        }.execute();
        new AsyncTask<Void, Void, LigaSpiel[]>() {
            @Override
            protected LigaSpiel[] doInBackground(Void... params) {
                return APIClient.getLigaSpiele("kreisliga1.json");
            }

            @Override
            protected void onPostExecute(LigaSpiel[] ligaSpiele) {
                super.onPostExecute(ligaSpiele);
                View view1 = getView();
                if (view1 != null) {
                    LinearLayout table = (LinearLayout) view1.findViewById(R.id.kreisliga);
                    if (table != null) {
                        table.removeAllViews();
                        setupLine(ligaSpiele, table);
                    }
                }
            }
        }.execute();
        new AsyncTask<Void, Void, LigaSpiel[]>() {
            @Override
            protected LigaSpiel[] doInBackground(Void... params) {
                return APIClient.getLigaSpiele("kreisliga2.json");
            }

            @Override
            protected void onPostExecute(LigaSpiel[] ligaSpiele) {
                super.onPostExecute(ligaSpiele);
                View view1 = getView();
                if (view1 != null) {
                    LinearLayout table = (LinearLayout) view1.findViewById(R.id.kreisliga2);
                    if (table != null) {
                        table.removeAllViews();
                        setupLine(ligaSpiele, table);
                    }
                }
            }
        }.execute();
    }

    private void setupLine(LigaSpiel[] ligaSpiele, LinearLayout table) {
        //neue Tabellenzeile
        for (LigaSpiel spiel : ligaSpiele) {
            View newLine = LayoutInflater.from(table.getContext()).inflate(R.layout.list_item_spiel, table, false);
            if (newLine != null) {
                TextView datum = (TextView) newLine.findViewById(R.id.datum);
                if (datum != null) {
                    datum.setText(spiel.getDatum() + " " + spiel.getZeit());
                }
                TextView paar1 = (TextView) newLine.findViewById(R.id.paar1);
                if (paar1 != null) {
                    paar1.setText(spiel.getPaar1());
                }
                TextView paar2 = (TextView) newLine.findViewById(R.id.paar2);
                if (paar2 != null) {
                    paar2.setText(spiel.getPaar2());
                }
                TextView erg = (TextView) newLine.findViewById(R.id.ergebnis);
                if (erg != null) {
                    erg.setText(spiel.getErg());
                }
                TextView ort = (TextView) newLine.findViewById(R.id.ort);
                if (ort != null) {
                    ort.setText(spiel.getOrt());
                }
            }
            table.addView(newLine);
        }
    }

    private void setupRow(TableLayout table, TypedArray textStyles, LigaSpiel spiel) {
        TableRow newRow = new TableRow(table.getContext());
        TextView dateView = new TextView(newRow.getContext());
        dateView.setTextAppearance(dateView.getContext(), textStyles.getResourceId(2, -1));
        dateView.setText(spiel.getDatum());
        newRow.addView(dateView);
        TextView timeView = new TextView(newRow.getContext());
        timeView.setTextAppearance(timeView.getContext(), textStyles.getResourceId(2, -1));
        timeView.setText(spiel.getZeit());
        newRow.addView(timeView);
        TextView locationView = new TextView(newRow.getContext());
        locationView.setTextAppearance(locationView.getContext(), textStyles.getResourceId(2, -1));
        locationView.setText(spiel.getOrt());
        newRow.addView(locationView);
        TextView pairingView = new TextView(newRow.getContext());
        pairingView.setTextAppearance(pairingView.getContext(), textStyles.getResourceId(2, -1));
        pairingView.setText(spiel.getPaar1() + " - " + spiel.getPaar2());
        newRow.addView(pairingView);
        TextView ergView = new TextView(newRow.getContext());
        ergView.setTextAppearance(ergView.getContext(), textStyles.getResourceId(2, -1));
        ergView.setText(spiel.getErg());
        newRow.addView(ergView);
        table.addView(newRow);
    }


}
