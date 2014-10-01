package de.svbrockscheid.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;

import de.svbrockscheid.APIClient;
import de.svbrockscheid.R;
import de.svbrockscheid.fragments.InfoFragment;
import de.svbrockscheid.fragments.MenuFragment;
import de.svbrockscheid.fragments.SpielplanFragment;
import de.svbrockscheid.fragments.UebersichtsFragment;

public class HomeScreenActivity extends Activity
        implements MenuFragment.NavigationDrawerCallbacks {

    public static final String NEUE_NACHRICHTEN = "neueNachrichten";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        MenuFragment mMenuFragment = (MenuFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);

        // Set up the drawer.
        mMenuFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        APIClient.registerCGM(this);
        if (getIntent().getExtras() != null && getIntent().getExtras().getBoolean(NEUE_NACHRICHTEN)) {
            //die Nachrichten auswählen
            mMenuFragment.selectItem(MenuFragment.INFO_POSITION);
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        Fragment selectedFragment;
        switch (position) {
            case MenuFragment.INFO_POSITION:
                selectedFragment = new InfoFragment();
                break;
            case MenuFragment.UEBERSICHT_POSITION:
                selectedFragment = new UebersichtsFragment();
                break;
            case MenuFragment.SPIELPLAN_POSITION:
                selectedFragment = new SpielplanFragment();
                break;
            default:
                selectedFragment = new UebersichtsFragment();
        }
        fragmentManager.beginTransaction()
                .replace(R.id.container, selectedFragment)
                .addToBackStack(null)
                .commit();
    }
}
