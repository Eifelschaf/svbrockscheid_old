package de.svbrockscheid;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.gcm.GoogleCloudMessaging;

/**
 * Created by Matthias on 01.10.2014.
 */
public class PushMessageIntentService extends IntentService {

    private static final String NEUE_NACHRICHTEN = "neueNachrichten";
    private static final String TYPE_PROPERTY = "type";
    private static final String TAG = "PushMessageIntentService";

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
        //get new messages
        AccountManager manager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
        if (manager != null) {
            Account[] accountsByType = manager.getAccountsByType(APIClient.ACCOUNT_TYPE);
            for (Account account : accountsByType) {
                ContentResolver.requestSync(account, APIClient.AUTHORITY, new Bundle());
            }
        }
    }

}
