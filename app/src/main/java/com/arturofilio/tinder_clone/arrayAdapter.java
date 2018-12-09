package com.arturofilio.tinder_clone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class arrayAdapter extends ArrayAdapter<Cards> {

    Context context;

    public arrayAdapter(Context context, int resourceId, List<Cards> items) {
        super(context, resourceId, items);

    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Cards card_item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);

        }

        TextView name = (TextView) convertView.findViewById(R.id.name);
        ImageView image = (ImageView) convertView.findViewById(R.id.img_view);

        name.setText(card_item.getName());
        image.setImageResource(R.drawable.tinder_1);

        return convertView;
    }

}
