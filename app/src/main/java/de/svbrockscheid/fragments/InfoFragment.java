package de.svbrockscheid.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.svbrockscheid.APIClient;
import de.svbrockscheid.R;
import de.svbrockscheid.adapters.NachrichtenAdapter;
import de.svbrockscheid.model.InfoNachricht;
import de.svbrockscheid.model.UpdateInfo;

/**
 * A fragment representing a list of Items.
 */
public class InfoFragment extends Fragment {

    private NachrichtenAdapter nachrichtenAdapter;

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
            final SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshLayoutNachrichten);
            if (refreshLayout != null) {
                refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        reloadAll();
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
        MenuFragment menuFragment = (MenuFragment) getFragmentManager().findFragmentById(R.id.navigation_drawer);
        if (menuFragment != null) {
            menuFragment.justCheckItem(MenuFragment.INFO_POSITION);
        }
        View view = getView();
        if (view != null) {
            //nachrichten laden
            RecyclerView list = (RecyclerView) view.findViewById(R.id.list);
            list.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
            list.setAdapter(getNachrichtenAdapter());
            reloadAll();
        }
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
                getNachrichtenAdapter().refresh();
                View view = getView();
                if (view != null) {
                    SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshLayoutNachrichten);
                    if (refreshLayout != null) {
                        refreshLayout.setRefreshing(false);
                    }
                }
            }
        }.execute();
        //check for an update
        new AsyncTask<Context, Void, UpdateInfo>() {
            @Override
            protected UpdateInfo doInBackground(Context... params) {
                if (params != null && params.length > 0) {
                    return APIClient.checkForUpdate(params[0]);
                }
                return null;
            }

            @Override
            protected void onPostExecute(UpdateInfo updateInfo) {
                super.onPostExecute(updateInfo);
                //show/hide the update Info window
                getNachrichtenAdapter().setUpdateInfo(updateInfo);
                nachrichtenAdapter.refresh();
            }
        }.execute(getActivity());
    }

    public NachrichtenAdapter getNachrichtenAdapter() {
        if (nachrichtenAdapter == null) {
            nachrichtenAdapter = new NachrichtenAdapter();
        }
        return nachrichtenAdapter;
    }
}
