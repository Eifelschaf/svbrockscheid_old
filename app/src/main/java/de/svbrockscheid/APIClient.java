package de.svbrockscheid;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.util.Log;
import android.util.TimeUtils;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.svbrockscheid.model.FileModification;
import de.svbrockscheid.model.InfoNachricht;
import de.svbrockscheid.model.LigaSpiel;
import de.svbrockscheid.model.UpdateInfo;
import se.emilsjolander.sprinkles.Query;

/**
 * Created by Matthias on 01.10.2014.
 */
public class APIClient {

    public static final String PROPERTY_REG_ID = "reg_id";
    public static final String KREISPOKAL_JSON = "kreispokal.json";
    public static final String KREISLIGA1_JSON = "kreisliga1.json";
    public static final String KREISLIGA2_JSON = "kreisliga2.json";
    public static final String AUTHORITY = "de.svbrockscheid.provider";
    public static final String ACCOUNT_TYPE = "svbrockscheid.de";
    private static final Gson GSON = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm").create();
    private static final String TAG = "APIClient";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    /**
     * Substitute you own sender ID here. This is the project number you got
     * from the API Console, as described in "Getting Started."
     */
    private static final String SENDER_ID = "552004700799";
    private static GoogleCloudMessaging gcm;

    /**
     * Die Übersicht vom Web lesen und zurück geben
     *
     * @return Alle Variablen, die in der Datei app.php gesetzt werden.
     */
    public static Map<String, String> getUebersicht(Context context) {
        Map<String, String> returnValues = new HashMap<String, String>();
        //falls mal mehr Dateien ausgelesen werden müssen
        for (String param : new String[]{BuildDependentConstants.URL + "/app.php"}) {
            String fileContent = getFileContent(param);
            if (fileContent != null && !fileContent.isEmpty()) {
                //store the file locally
                if (context != null) {
                    FileOutputStream cacheFile;
                    try {
                        cacheFile = context.openFileOutput("app.php", Context.MODE_PRIVATE);
                        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(cacheFile);
                        outputStreamWriter.write(fileContent);
                        outputStreamWriter.flush();
                        outputStreamWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                //try to read the local file
                fileContent = getCachedFileContent(context, "app.php");
            }
            returnValues.putAll(ValueParser.parse(fileContent));
        }
        return returnValues;
    }

    public static LigaSpiel[] getLigaSpiele(String fileName) {
        String fileContent = getFileContent("http://www.svbrockscheid.de/" + fileName);
        if(fileContent != null && !fileContent.isEmpty()) {
            LigaSpiel[] ligaSpiele = GSON.fromJson(fileContent, LigaSpiel[].class);
            if (ligaSpiele != null) {
                //ligaspiele speichern
                for (LigaSpiel spiel : ligaSpiele) {
                    if (spiel != null) {
                        spiel.setTyp(fileName);
                        spiel.save();
                    }
                }
            }
            return ligaSpiele;
        } else {
            return new LigaSpiel[0];
        }
    }

    private static String getFileContent(String fileName) {
        try {
            HttpGet request = new HttpGet(fileName);
            FileModification fileModification = Query.one(FileModification.class, "SELECT * FROM " + FileModification.TABLE_NAME + " WHERE " + FileModification.COLUMN_FILE_NAME + " = ?", fileName).get();
            if(fileModification != null) {
                request.addHeader("If-Modified-Since", fileModification.getModificationDate());
            }
            try {
                request.setURI(new URI(fileName));
                HttpResponse response = new DefaultHttpClient().execute(request);
                if(response.getStatusLine().getStatusCode() == 200) {
                    //forward the content
                    BufferedReader inStream = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                    StringBuilder builder = new StringBuilder();
                    String line;
                    while ((line = inStream.readLine()) != null) {
                        builder.append(line);
                    }
                    inStream.close();
                    //das änderungsdatum speichern
                    Header lastModifiedHeader = response.getFirstHeader("Last-Modified");
                    if(lastModifiedHeader != null) {
                        if (fileModification == null) {
                            fileModification = new FileModification();
                            fileModification.setFileName(fileName);
                        }
                        fileModification.setModificationDate(lastModifiedHeader.getValue());
                        fileModification.save();
                    }
                    //den stringbuilder parsen
                    return builder.toString();
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            Log.e(TAG, "FileNotFoundException", e);
        } catch (IOException e) {
            Log.e(TAG, "IOException", e);
        }
        return "";
    }

    private static String getCachedFileContent(Context context, String fileName) {
        if (context != null) {
            try {
                FileInputStream fileInputStream = context.openFileInput(fileName);
                BufferedReader instream = new BufferedReader(new InputStreamReader(fileInputStream));
                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = instream.readLine()) != null) {
                    builder.append(line);
                }
                instream.close();
                //den stringbuilder parsen
                return builder.toString();
            } catch (FileNotFoundException e) {
                Log.e(TAG, "FileNotFoundException", e);
            } catch (IOException e) {
                Log.e(TAG, "IOException", e);
            }
        }
        return "";
    }

    /**
     * Alle Nachrichten vom Server laden
     *
     * @return Alle Nachrichten, fertig geparsed.
     */
    public static InfoNachricht[] getNachrichten() {
        String fileContent = getFileContent(BuildDependentConstants.URL + "/nachrichten.json");
        if(fileContent != null && !fileContent.isEmpty()) {
            List<InfoNachricht> infoNachrichten = Arrays.asList(GSON.fromJson(fileContent, InfoNachricht[].class));
            for (InfoNachricht nachricht : infoNachrichten) {
                if (nachricht != null && !nachricht.isDelete()) {
                    nachricht.save();
                } else if (nachricht != null) {
                    nachricht.delete();
                }
            }
            return infoNachrichten.toArray(new InfoNachricht[infoNachrichten.size()]);
        } else {
            return new InfoNachricht[0];
        }
    }

    public static boolean registerCGM(Activity activity) {
        // Check device for Play Services APK. If check succeeds, proceed with
        //  GCM registration.
        if (checkPlayServices(activity)) {
            Context applicationContext = activity.getApplicationContext();
            gcm = GoogleCloudMessaging.getInstance(applicationContext);

            if (getRegistrationId(applicationContext).isEmpty()) {
                registerInBackground(applicationContext);
            }
        } else {
            Log.i(TAG, "No valid Google Play Services APK found.");
        }
        return false;
    }

    /**
     * Registers the application with GCM servers asynchronously.
     * <p/>
     * Stores the registration ID and app versionCode in the application's
     * shared preferences.
     */
    private static void registerInBackground(final Context context) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    String regid = gcm.register(SENDER_ID);

                    // You should send the registration ID to your server over HTTP,
                    // so it can use GCM/HTTP or CCS to send messages to your app.
                    // The request to your server should be authenticated if your app
                    // is using accounts.
                    sendRegistrationIdToBackend(regid);

                    // For this demo: we don't need to send it because the device
                    // will send upstream messages to a server that echo back the
                    // message using the 'from' address in the message.

                    // Persist the regID - no need to register again.
                    storeRegistrationId(context, regid);
                } catch (IOException ex) {
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return null;
            }

        }.execute(null, null, null);
    }

    /**
     * Stores the registration ID and app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param context application's context.
     * @param regId   registration ID
     */
    private static void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }

    /**
     * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP
     * or CCS to send messages to your app. Not needed for this demo since the
     * device sends upstream messages to a server that echoes back the message
     * using the 'from' address in the message.
     */
    private static void sendRegistrationIdToBackend(String regid) {
        //key an Server schicken
        URLConnection urlConnection;
        try {
            urlConnection = new URL(BuildDependentConstants.URL + "/register.php?key=" + regid).openConnection();
            urlConnection.connect();
            urlConnection.getContent();
        } catch (IOException e) {
            Log.e(TAG, "IOException", e);
        }
    }

    /**
     * Gets the current registration ID for application on GCM service.
     * <p/>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     * registration ID.
     */
    private static String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    /**
     * @return Application's {@code SharedPreferences}.
     */
    private static SharedPreferences getGCMPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the regID in your app is up to you.
        return context.getSharedPreferences(context.getPackageName(),
                Context.MODE_PRIVATE);
    }

    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private static boolean checkPlayServices(Activity activity) {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity.getApplicationContext());
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, activity,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
            }
            return false;
        }
        return true;
    }

    public static UpdateInfo checkForUpdate(Context context) {
        //auf update prüfen
        try {
            String fileContent = getFileContent(BuildDependentConstants.URL + "/updateCheck.php?version=" + context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode);
            if (fileContent != null && !fileContent.isEmpty()) {
                return GSON.fromJson(fileContent, UpdateInfo.class);
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "NameNotFoundException", e);
        }
        return null;
    }
}
