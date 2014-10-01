package svbrockscheid.de.svbrockscheid.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;

import svbrockscheid.de.svbrockscheid.R;
import svbrockscheid.de.svbrockscheid.fragments.InfoFragment;
import svbrockscheid.de.svbrockscheid.fragments.MenuFragment;
import svbrockscheid.de.svbrockscheid.fragments.SpielplanFragment;
import svbrockscheid.de.svbrockscheid.fragments.UebersichtsFragment;

public class HomeScreenActivity extends Activity
        implements MenuFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private MenuFragment mMenuFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        mMenuFragment = (MenuFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mMenuFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
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
