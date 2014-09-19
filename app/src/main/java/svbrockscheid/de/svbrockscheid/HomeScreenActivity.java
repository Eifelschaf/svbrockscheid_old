package svbrockscheid.de.svbrockscheid;

import android.app.Activity;
import android.os.Bundle;


public class HomeScreenActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new TestFragment())
                    .commit();
        }
    }


}
