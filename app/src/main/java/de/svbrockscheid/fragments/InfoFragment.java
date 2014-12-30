package de.svbrockscheid.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_nachrichten, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        //menü richten
        ((MenuFragment) getFragmentManager().findFragmentById(R.id.navigation_drawer)).justCheckItem(MenuFragment.INFO_POSITION);
        //lokale Nachrichten laden
        CursorList<InfoNachricht> infoNachrichten = Query.all(InfoNachricht.class).get();
        setListAdapter(new ArrayAdapter<>(getActivity(),
                R.layout.list_item_nachrichten, android.R.id.text1, infoNachrichten.asList()));
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
                setListAdapter(new ArrayAdapter<>(getActivity(),
                        R.layout.list_item_nachrichten, android.R.id.text1, infoNachrichten.asList()));
            }
        }.execute();
    }
}
