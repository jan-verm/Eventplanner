package com.ugent.eventplanner.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ugent.eventplanner.R;
import com.ugent.eventplanner.models.Event;

public class EventListAdapter extends ArrayAdapter<Event> {

    private ViewHolder viewHolder;

    private static class ViewHolder {
        private TextView itemView;
    }

    public EventListAdapter(Context context, int resource, int textViewResourceId, List<Event> items) {
        super(context, textViewResourceId, textViewResourceId, items);
        viewHolder = new ViewHolder();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext())
                    .inflate(R.layout.title_view, parent, false);
            holder = new ViewHolder();
            holder.itemView = (TextView) convertView.findViewById(R.id.title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Event e = getItem(position);
        holder.itemView.setText(e.getTitle());

        return convertView;
    }
}