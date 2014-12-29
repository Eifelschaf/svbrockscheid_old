package de.svbrockscheid.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
        return inflater.inflate(R.layout.fragment_spielplan, container, false);
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
                if (ligaSpiele != null) {
                    View view1 = getView();
                    if (view1 != null) {
                        LinearLayout table = (LinearLayout) view1.findViewById(R.id.kreispokal);
                        if (table != null) {
                            table.removeAllViews();
                            setupLine(ligaSpiele, table);
                        }
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
                if (ligaSpiele != null) {
                    View view1 = getView();
                    if (view1 != null) {
                        LinearLayout table = (LinearLayout) view1.findViewById(R.id.kreisliga);
                        if (table != null) {
                            table.removeAllViews();
                            setupLine(ligaSpiele, table);
                        }
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
                if (ligaSpiele != null) {
                    View view1 = getView();
                    if (view1 != null) {
                        LinearLayout table = (LinearLayout) view1.findViewById(R.id.kreisliga2);
                        if (table != null) {
                            table.removeAllViews();
                            setupLine(ligaSpiele, table);
                        }
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
}
