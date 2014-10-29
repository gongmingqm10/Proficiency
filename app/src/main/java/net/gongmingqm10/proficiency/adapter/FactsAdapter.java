package net.gongmingqm10.proficiency.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.gongmingqm10.proficiency.R;
import net.gongmingqm10.proficiency.model.Item;

import java.util.ArrayList;
import java.util.List;

public class FactsAdapter extends BaseAdapter {

    private final Context context;
    private List<Item> itemList;

    public FactsAdapter(Context context) {
        this.context = context;
        this.itemList = new ArrayList<Item>();
    }

    public void setItems(Item[] items) {
        for (Item item : items) {
            if (item.isValid()) {
                this.itemList.add(item);
            }
        }
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return (itemList == null) ? 0 : itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return (itemList == null) ? null : itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.fact_item, null);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.description = (TextView) convertView.findViewById(R.id.description);
            holder.image = (ImageView) convertView.findViewById(R.id.image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Item item = (Item) getItem(position);
        holder.title.setText(item.getTitle());
        holder.description.setText(item.getDescription());
        Picasso.with(context).load(item.getImageHref()).placeholder(R.drawable.placeholder).into(holder.image);

        return convertView;
    }

    private class ViewHolder {
        TextView title;
        TextView description;
        ImageView image;
    }
}
