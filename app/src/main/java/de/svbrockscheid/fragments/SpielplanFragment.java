package de.svbrockscheid.fragments;

import android.database.ContentObserver;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.svbrockscheid.APIClient;
import de.svbrockscheid.R;
import de.svbrockscheid.model.LigaSpiel;
import se.emilsjolander.sprinkles.CursorList;
import se.emilsjolander.sprinkles.Query;
import se.emilsjolander.sprinkles.SprinklesContentObserver;

/**
 * A simple {@link Fragment} subclass.
 */
public class SpielplanFragment extends Fragment {

    private SprinklesContentObserver observer = new SprinklesContentObserver(new ContentObserver(new Handler(Looper.getMainLooper())) {
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            loadAll();
        }
    });

    public SpielplanFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_spielplan, container, false);
        if (view != null) {
            final SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshLayoutSpielplan);
            if (refreshLayout != null) {
                refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
//                        if(!refreshLayout.isRefreshing()) {
                        reloadAll();
//                        }
                    }
                });
            }
        }
        return view;
    }

    private void reloadData(CursorList<LigaSpiel> ligaSpiele, @IdRes int zielId) {
        if (ligaSpiele != null) {
            View view1 = getView();
            if (view1 != null) {
                LinearLayout table = (LinearLayout) view1.findViewById(zielId);
                if (table != null) {
                    table.removeAllViews();
                    setupLine(ligaSpiele, table);
                }
            }
            ligaSpiele.close();
        }
    }

    private void setupLine(CursorList<LigaSpiel> ligaSpiele, LinearLayout table) {
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
                table.addView(newLine);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //menü richten
        MenuFragment menuFragment = (MenuFragment) getFragmentManager().findFragmentById(R.id.navigation_drawer);
        if (menuFragment != null) {
            menuFragment.justCheckItem(MenuFragment.SPIELPLAN_POSITION);
        }
        loadAll();
        //Änderungen anzeigen
        observer.register(LigaSpiel.class, false);
    }

    @Override
    public void onPause() {
        super.onPause();
        observer.unregister();
    }

    private void reloadAll() {
        new AsyncTask<Void, Void, LigaSpiel[]>() {
            @Override
            protected LigaSpiel[] doInBackground(Void... params) {
                APIClient.getLigaSpiele(APIClient.KREISLIGA1_JSON);
                APIClient.getLigaSpiele(APIClient.KREISLIGA2_JSON);
                APIClient.getLigaSpiele(APIClient.KREISPOKAL_JSON);
                return null;
            }

            @Override
            protected void onPostExecute(LigaSpiel[] ligaSpiele) {
                super.onPostExecute(ligaSpiele);
                loadAll();
                View view = getView();
                if (view != null) {
                    SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshLayoutSpielplan);
                    if (refreshLayout != null) {
                        refreshLayout.setRefreshing(false);
                    }
                }
            }
        }.execute();
    }

    private void loadAll() {
        reloadData(Query.many(LigaSpiel.class, "SELECT * from LigaSpiel where typ = ?", APIClient.KREISPOKAL_JSON).get(), R.id.kreispokal);
        reloadData(Query.many(LigaSpiel.class, "SELECT * from LigaSpiel where typ = ?", APIClient.KREISLIGA1_JSON).get(), R.id.kreisliga);
        reloadData(Query.many(LigaSpiel.class, "SELECT * from LigaSpiel where typ = ?", APIClient.KREISLIGA2_JSON).get(), R.id.kreisliga2);
    }
}
