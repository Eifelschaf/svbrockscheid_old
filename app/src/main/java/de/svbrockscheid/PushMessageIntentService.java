package de.svbrockscheid;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import de.svbrockscheid.activities.HomeScreenActivity;

/**
 * Created by Matthias on 01.10.2014.
 */
public class PushMessageIntentService extends IntentService {
    public static final int NOTIFICATION_ID = 1;
    private static final String NEUE_NACHRICHTEN = "neueNachrichten";
    private static final String TYPE_PROPERTY = "type";
    private static final String TAG = "PushMessageIntentService";
    NotificationCompat.Builder builder;
    private NotificationManager mNotificationManager;

    public PushMessageIntentService() {
        super("PushMessageIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM
             * will be extended in the future with new message types, just ignore
             * any message types you're not interested in, or that you don't
             * recognize.
             */
            if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                // Post notification of received message.
                if (extras.containsKey(TYPE_PROPERTY) && NEUE_NACHRICHTEN.equals(extras.get(TYPE_PROPERTY))) {
                    sendNotificationForNewMessages();
                }
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotificationForNewMessages() {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(this, HomeScreenActivity.class);
        intent.putExtra(HomeScreenActivity.NEUE_NACHRICHTEN, true);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                intent, 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.noticon)
                        .setContentTitle(getString(R.string.neue_nachrichten));

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

}
