package net.gongmingqm10.proficiency.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.gongmingqm10.proficiency.R;
import net.gongmingqm10.proficiency.imageloader.ImageLoader;
import net.gongmingqm10.proficiency.model.Item;

import java.util.HashMap;
import java.util.Map;

public class FactsAdapter extends BaseAdapter {

    private final Context context;
    private Item[] items;

    private Map<String, Bitmap> cachedBitmaps;

    public FactsAdapter(Context context) {
        this.context = context;
        cachedBitmaps = new HashMap<String, Bitmap>();
    }

    public void setItems(Item[] items) {
        this.items = items;
        notifyDataSetChanged();
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
        final Item item = items[position];
        if (item.getTitle() != null) holder.title.setText(item.getTitle());
        if (item.getTitle() != null) holder.description.setText(item.getDescription());
        ImageLoader.getInstance().load(item.getImageHref(), holder.image);

        return convertView;
    }

    private class ViewHolder{
        TextView title;
        TextView description;
        ImageView image;
    }
}
