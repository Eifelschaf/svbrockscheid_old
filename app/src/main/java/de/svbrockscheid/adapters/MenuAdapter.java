package de.svbrockscheid.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import de.svbrockscheid.R;

/**
 * Created by eifel_000 on 01/03/2015.
 */
public class MenuAdapter extends BaseAdapter {
    private final int[] icons;
    private final String[] names;

    public MenuAdapter(String[] names, int[] icons) {
        super();
        this.names = names;
        this.icons = icons;
    }

    @Override
    public int getCount() {
        return names.length;
    }

    @Override
    public Object getItem(int position) {
        return names[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_menu, parent, false);
        }
        TextView text = (TextView) view.findViewById(R.id.text);
        if (text != null) {
            text.setText(names[position]);
        }
        ImageView icon = (ImageView) view.findViewById(R.id.icon);
        if (icon != null) {
            icon.setImageResource(icons[position]);
        }
        return view;
    }
}
