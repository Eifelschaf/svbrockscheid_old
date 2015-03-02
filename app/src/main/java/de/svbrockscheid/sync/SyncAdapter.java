package de.svbrockscheid.sync;

import android.accounts.Account;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.svbrockscheid.APIClient;
import de.svbrockscheid.R;
import de.svbrockscheid.activities.HomeScreenActivity;
import de.svbrockscheid.model.InfoNachricht;
import se.emilsjolander.sprinkles.CursorList;
import se.emilsjolander.sprinkles.Query;

/**
 * Handle the transfer of data between a server and an
 * app, using the Android sync adapter framework.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {

    public static final int NOTIFICATION_ID_NEUE_NACHRICHTEN = 1;

    // Global variables
    // Define a variable to contain a content resolver instance
    ContentResolver mContentResolver;

    /**
     * Set up the sync adapter
     */
    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        mContentResolver = context.getContentResolver();
    }

    /**
     * Set up the sync adapter. This form of the
     * constructor maintains compatibility with Android 3.0
     * and later platform versions
     */
    public SyncAdapter(
            Context context,
            boolean autoInitialize,
            boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        mContentResolver = context.getContentResolver();
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        CursorList<InfoNachricht> alteNachrichtenListe = Query.all(InfoNachricht.class).get();
        InfoNachricht[] alteNachrichten;
        List<InfoNachricht> alteListe;
        try {
            alteListe = alteNachrichtenListe.asList();
        } finally {
            alteNachrichtenListe.close();
        }
        InfoNachricht[] neueNachrichten = APIClient.getNachrichten();
        if(neueNachrichten.length > 0) {
            //gelöschte nachrichten aus der alten liste entfernen
            List<InfoNachricht> neueListe = Arrays.asList(neueNachrichten);
            for (InfoNachricht nachricht : new ArrayList<>(alteListe)) {
                if (!neueListe.contains(nachricht)) {
                    alteListe.remove(nachricht);
                }
            }
            alteNachrichten = alteListe.toArray(new InfoNachricht[alteNachrichtenListe.size()]);
            if (!Arrays.deepEquals(neueNachrichten, alteNachrichten)) {
                //checken, ob nachrichten nur gelöscht wurden
                showNotificationNews();
            }
        }
//        APIClient.getUebersicht(getContext());
        APIClient.getLigaSpiele(APIClient.KREISPOKAL_JSON);
        APIClient.getLigaSpiele(APIClient.KREISLIGA1_JSON);
        APIClient.getLigaSpiele(APIClient.KREISLIGA2_JSON);
    }

    private void showNotificationNews() {
        NotificationManager mNotificationManager = (NotificationManager)
                getContext().getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(HomeScreenActivity.NEUE_NACHRICHTEN);
        intent.putExtra(HomeScreenActivity.NEUE_NACHRICHTEN, 1);
        PendingIntent contentIntent = PendingIntent.getActivity(getContext(), 0,
                intent, 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getContext())
                        .setSmallIcon(R.drawable.noticon)
                        .setContentTitle(getContext().getString(R.string.neuigkeiten))
                        .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                        .setVibrate(new long[]{300, 300, 300, 300})
                        .setContentText(getContext().getString(R.string.neue_nachrichten));

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID_NEUE_NACHRICHTEN, mBuilder.build());
    }
}