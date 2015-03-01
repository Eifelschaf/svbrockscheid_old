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
    private String nachricht = "";

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InfoNachricht that = (InfoNachricht) o;

        if (id != that.id) return false;
        if (nachricht != null ? !nachricht.equals(that.nachricht) : that.nachricht != null)
            return false;
        if (zeit != null ? !zeit.equals(that.zeit) : that.zeit != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (nachricht != null ? nachricht.hashCode() : 0);
        result = 31 * result + (zeit != null ? zeit.hashCode() : 0);
        return result;
    }
}
