package de.svbrockscheid.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import de.svbrockscheid.APIClient;
import de.svbrockscheid.R;
import de.svbrockscheid.model.InfoNachricht;
import se.emilsjolander.sprinkles.CursorList;
import se.emilsjolander.sprinkles.Query;

/**
 * A fragment representing a list of Items.
 */
public class InfoFragment extends ListFragment {

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public InfoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nachrichten, container, false);
        if (view != null) {
            final SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshLayout);
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

    @Override
    public void onResume() {
        super.onResume();
        //menü richten
        ((MenuFragment) getFragmentManager().findFragmentById(R.id.navigation_drawer)).justCheckItem(MenuFragment.INFO_POSITION);
        //lokale Nachrichten laden
        CursorList<InfoNachricht> infoNachrichten = Query.all(InfoNachricht.class).get();
        if (infoNachrichten != null) {
            setListAdapter(new ArrayAdapter<>(getActivity(),
                    R.layout.list_item_nachrichten, android.R.id.text1, infoNachrichten.asList()));
            infoNachrichten.close();
        }
        reloadAll();
    }

    private void reloadAll() {
        new AsyncTask<Void, Void, InfoNachricht[]>() {
            @Override
            protected InfoNachricht[] doInBackground(Void... params) {
                return APIClient.getNachrichten();
            }

            @Override
            protected void onPostExecute(InfoNachricht[] results) {
                super.onPostExecute(results);
                //neu laden
                CursorList<InfoNachricht> infoNachrichten = Query.all(InfoNachricht.class).get();
                if (infoNachrichten != null) {
                    setListAdapter(new ArrayAdapter<>(getActivity(),
                            R.layout.list_item_nachrichten, android.R.id.text1, infoNachrichten.asList()));
                    infoNachrichten.close();
                }
                View view = getView();
                if (view != null) {
                    SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshLayout);
                    if (refreshLayout != null) {
                        refreshLayout.setRefreshing(false);
                    }
                }
            }
        }.execute();
    }
}
