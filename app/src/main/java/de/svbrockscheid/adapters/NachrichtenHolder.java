package de.svbrockscheid.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import de.svbrockscheid.R;

/**
 * Created by Matthias on 31.12.2014.
 */
public class NachrichtenHolder extends RecyclerView.ViewHolder {

    public TextView nachricht;
    public TextView zeit;

    public NachrichtenHolder(View itemView) {
        super(itemView);
        nachricht = (TextView) itemView.findViewById(R.id.nachricht);
        zeit = (TextView) itemView.findViewById(R.id.zeit);
    }
}
