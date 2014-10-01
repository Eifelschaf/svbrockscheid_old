package de.svbrockscheid.fragments;

import android.app.ListFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import de.svbrockscheid.APIClient;
import de.svbrockscheid.R;
import de.svbrockscheid.model.InfoNachricht;

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
        new AsyncTask<Void, Void, InfoNachricht[]>() {
            @Override
            protected InfoNachricht[] doInBackground(Void... params) {
                return APIClient.getNachrichten();
            }

            @Override
            protected void onPostExecute(InfoNachricht[] results) {
                super.onPostExecute(results);
                setListAdapter(new ArrayAdapter<InfoNachricht>(getActivity(),
                        R.layout.list_item_nachrichten, android.R.id.text1, results));
            }
        }.execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_nachrichten, container, false);
    }
}
