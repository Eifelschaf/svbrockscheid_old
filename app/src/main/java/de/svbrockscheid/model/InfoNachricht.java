package de.svbrockscheid.model;

import java.util.Date;

/**
 * Created by Matthias on 01.10.2014.
 */
public class InfoNachricht {

    private String nachricht = "DUMMY";
    private Date zeit = new Date();

    public Date getZeit() {
        return zeit;
    }

    public String getNachricht() {
        return nachricht;
    }

    @Override
    public String toString() {
        return nachricht;
    }
}
