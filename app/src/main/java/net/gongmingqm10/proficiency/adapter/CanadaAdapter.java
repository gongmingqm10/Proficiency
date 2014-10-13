package net.gongmingqm10.proficiency.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import net.gongmingqm10.proficiency.model.Item;

public class CanadaAdapter extends BaseAdapter {

    private Item[] items;

    public CanadaAdapter(Item[] items) {
        this.items = items;
    }

    @Override
    public int getCount() {
        return (items == null) ? 0 : items.length;
    }

    @Override
    public Object getItem(int position) {
        return (items == null) ? null : items[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return null;
    }
}
