package de.svbrockscheid.fragments;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.svbrockscheid.R;

public class AboutFragment extends Fragment {
    public AboutFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        if (view != null) {
            TextView versionsInfo = (TextView) view.findViewById(R.id.version);
            if (versionsInfo != null) {
                try {
                    PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
                    versionsInfo.setText(getString(R.string.versionsInfo, pInfo.versionName));
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
    }
}
