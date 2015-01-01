package de.svbrockscheid.adapters;

import android.view.View;

import de.svbrockscheid.R;

/**
 * Created by Matthias on 31.12.2014.
 */
public class UpdateInfoHolder extends NachrichtenHolder {

    public View downloadButton;

    public UpdateInfoHolder(View itemView) {
        super(itemView);
        downloadButton = itemView.findViewById(R.id.downloadButton);
    }
}
