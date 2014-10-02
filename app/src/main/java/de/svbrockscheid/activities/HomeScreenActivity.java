package de.svbrockscheid.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;

import de.svbrockscheid.APIClient;
import de.svbrockscheid.PushMessageIntentService;
import de.svbrockscheid.R;
import de.svbrockscheid.fragments.InfoFragment;
import de.svbrockscheid.fragments.MenuFragment;
import de.svbrockscheid.fragments.SpielplanFragment;
import de.svbrockscheid.fragments.UebersichtsFragment;

public class HomeScreenActivity extends Activity
        implements MenuFragment.NavigationDrawerCallbacks {

    public static final String NEUE_NACHRICHTEN = "de.svbrockscheid.NEUE_NACHRICHTEN";
    private static final String UEBERSICHT = "übersicht";
    private static final String SPIELPLAN = "spielplan";
    private static final String INFO = "info";

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
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (NEUE_NACHRICHTEN.equals(intent.getAction())) {
            //die Nachrichten auswählen
            ((MenuFragment) getFragmentManager().findFragmentById(R.id.navigation_drawer)).selectItem(MenuFragment.INFO_POSITION);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                //notification entfernen
                notificationManager.cancel(PushMessageIntentService.NOTIFICATION_ID_NEUE_NACHRICHTEN);
            }
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        Fragment selectedFragment;
        String backstackName;
        switch (position) {
            case MenuFragment.INFO_POSITION:
                selectedFragment = new InfoFragment();
                backstackName = INFO;
                break;
            case MenuFragment.UEBERSICHT_POSITION:
                selectedFragment = new UebersichtsFragment();
                backstackName = UEBERSICHT;
                break;
            case MenuFragment.SPIELPLAN_POSITION:
                selectedFragment = new SpielplanFragment();
                backstackName = SPIELPLAN;
                break;
            default:
                selectedFragment = new UebersichtsFragment();
                backstackName = UEBERSICHT;
        }
        FragmentTransaction transaction = fragmentManager.beginTransaction()
                .replace(R.id.container, selectedFragment);
        Fragment fragmentById = fragmentManager.findFragmentById(R.id.container);
        if (fragmentById != null && fragmentById.getClass() != selectedFragment.getClass()) {
            //nur in den backstack wenn es schon was gibt
            transaction.addToBackStack(backstackName);
        }
        transaction.commit();
    }
}
