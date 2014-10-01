package svbrockscheid.de.svbrockscheid.fragments;

import android.app.ListFragment;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import svbrockscheid.de.svbrockscheid.model.InfoNachricht;

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


        // TODO: Change Adapter to display your content
        setListAdapter(new ArrayAdapter<InfoNachricht>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, new InfoNachricht[0]));
    }
}
