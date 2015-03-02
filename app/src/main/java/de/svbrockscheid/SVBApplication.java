package de.svbrockscheid;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.crashlytics.android.Crashlytics;

import de.svbrockscheid.model.FileModification;
import io.fabric.sdk.android.Fabric;
import se.emilsjolander.sprinkles.Migration;
import se.emilsjolander.sprinkles.Sprinkles;

/**
 * Created by Matthias on 30.12.2014.
 */
public class SVBApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());

        Sprinkles sprinkles = Sprinkles.init(getApplicationContext());

        sprinkles.addMigration(new Migration() {

            @Override
            protected void doMigration(SQLiteDatabase db) {
                db.execSQL(
                        "CREATE TABLE InfoNachricht (" +
                                "_id INTEGER PRIMARY KEY," +
                                "nachricht TEXT," +
                                "zeit INTEGER" +
                                ")"
                );
                db.execSQL(
                        "CREATE TABLE LigaSpiel (" +
                                "_id INTEGER PRIMARY KEY," +
                                "datum TEXT, " +
                                "zeit TEXT, " +
                                "ort TEXT, " +
                                "paar1 TEXT, " +
                                "paar2 TEXT, " +
                                "erg TEXT, " +
                                "typ TEXT" +
                                ")"
                );
            }
        });
        sprinkles.addMigration(new Migration() {
            @Override
            protected void doMigration(SQLiteDatabase sqLiteDatabase) {
                sqLiteDatabase.execSQL(FileModification.CREATE_TABLE);
            }
        });
    }

}
