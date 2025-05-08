package com.example.mad_project;

import android.content.Context;
import android.view.*;
import android.widget.*;

public class CustomDrawerAdapter extends BaseAdapter {

    Context context;
    String[] titles;
    int[] icons;
    LayoutInflater inflater;

    public CustomDrawerAdapter(Context context, String[] titles, int[] icons) {
        this.context = context;
        this.titles = titles;
        this.icons = icons;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public Object getItem(int position) {
        return titles[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        ImageView icon;
        TextView title;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.drawer_item, parent, false);
            holder = new ViewHolder();
            holder.icon = convertView.findViewById(R.id.item_icon);
            holder.title = convertView.findViewById(R.id.item_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.icon.setImageResource(icons[position]);
        holder.title.setText(titles[position]);

        return convertView;
    }
}
