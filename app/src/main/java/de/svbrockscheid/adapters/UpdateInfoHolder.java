package de.svbrockscheid.adapters;

import android.view.View;
import android.widget.TextView;

import de.svbrockscheid.R;

/**
 * Created by Matthias on 31.12.2014.
 */
public class UpdateInfoHolder extends NachrichtenHolder {

    public View downloadButton;
    public TextView changeLog;

    public UpdateInfoHolder(View itemView) {
        super(itemView);
        downloadButton = itemView.findViewById(R.id.downloadButton);
        changeLog = (TextView) itemView.findViewById(R.id.changeLog);
    }
}
