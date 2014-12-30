package de.svbrockscheid.model;

import se.emilsjolander.sprinkles.Model;
import se.emilsjolander.sprinkles.annotations.Column;
import se.emilsjolander.sprinkles.annotations.Key;
import se.emilsjolander.sprinkles.annotations.Table;

/**
 * Created by Matthias on 01.10.2014.
 */
@Table("LigaSpiel")
public class LigaSpiel extends Model {

    @Column("datum")
    private String datum;
    @Column("zeit")
    private String zeit;
    @Column("ort")
    private String ort;
    @Column("paar1")
    private String paar1;
    @Column("paar2")
    private String paar2;
    @Column("erg")
    private String erg;
    @Key
    @Column("_id")
    private int bericht;

    @Column("typ")
    private String typ;

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

    public void setTyp(String typ) {
        this.typ = typ;
    }
}
