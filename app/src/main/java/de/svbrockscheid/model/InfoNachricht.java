package de.svbrockscheid.model;

import java.util.Date;

import se.emilsjolander.sprinkles.Model;
import se.emilsjolander.sprinkles.annotations.Column;
import se.emilsjolander.sprinkles.annotations.Key;
import se.emilsjolander.sprinkles.annotations.Table;

/**
 * Created by Matthias on 01.10.2014.
 */
@Table("InfoNachricht")
public class InfoNachricht extends Model {

    @Key
    @Column("_id")
    private long id;

    @Column("nachricht")
    private String nachricht = "DUMMY";

    @Column("zeit")
    private Date zeit = new Date();

    private boolean del = false;

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

    public boolean isDelete() {
        return del;
    }
}
