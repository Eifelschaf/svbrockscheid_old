package de.svbrockscheid.model;

/**
 * Created by Matthias on 01.10.2014.
 */
public class LigaSpiel {
    private String datum;
    private String zeit;
    private String ort;
    private String paar1;
    private String paar2;
    private String erg;
    private int bericht;

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public String getZeit() {
        return zeit;
    }

    public void setZeit(String zeit) {
        this.zeit = zeit;
    }

    public String getOrt() {
        return ort;
    }

    public void setOrt(String ort) {
        this.ort = ort;
    }

    public String getPaar1() {
        return paar1;
    }

    public void setPaar1(String paar1) {
        this.paar1 = paar1;
    }

    public String getPaar2() {
        return paar2;
    }

    public void setPaar2(String paar2) {
        this.paar2 = paar2;
    }

    public String getErg() {
        return erg;
    }

    public void setErg(String erg) {
        this.erg = erg;
    }

    public int getBericht() {
        return bericht;
    }

    public void setBericht(int bericht) {
        this.bericht = bericht;
    }
}
