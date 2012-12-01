/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.kuratkoo.wikicfp.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import net.kuratkoo.wikicfp.R;
import net.kuratkoo.wikicfp.model.Category;
import net.kuratkoo.wikicfp.model.CategorySet;


/**
 * CategoryListAdapter
 * @author Radim -kuratkoo- Vaculik <kuratkoo@gmail.com>
 */
public class CategoryListAdapter extends BaseAdapter {

    private CategorySet mCategorySet;
    private static final String TAG = "WikiCFP|CategoryListAdapter";
    private LayoutInflater mInflater;
    private Context mContext;
    
    public CategoryListAdapter(Context context, CategorySet categorySet) {
        mCategorySet = categorySet;
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }
    
    public int getCount() {
        return mCategorySet.size();
    }

    public Category getItem(int position) {
        return mCategorySet.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final CategoryViewHolder holder;
        final SharedPreferences sharedPrefs = mContext.getSharedPreferences("star", 0);        
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.listitem_category, parent, false);

            holder = new CategoryViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.count = (TextView) convertView.findViewById(R.id.count);
            holder.star = (ImageView) convertView.findViewById(R.id.star);
            convertView.setTag(holder);
        } else {
            holder = (CategoryViewHolder) convertView.getTag();
        }

        holder.name.setText(this.getItem(position).getName().substring(0, 1).toUpperCase() + this.getItem(position).getName().substring(1));
        holder.count.setText(this.getItem(position).getCount());

        final String name = CategoryListAdapter.this.getItem(position).getName();
        final Boolean state = sharedPrefs.getBoolean(name, false);
        
        if (state) {
            holder.star.setImageResource(android.R.drawable.btn_star_big_on);
        } else {
            holder.star.setImageResource(android.R.drawable.btn_star_big_off);
        }
        
        holder.star.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Editor e = sharedPrefs.edit();
                if (!state) { // no star
                    e.putBoolean(name, true);
                    holder.star.setImageResource(android.R.drawable.btn_star_big_on);
                } else {
                    e.putBoolean(name, false);
                    holder.star.setImageResource(android.R.drawable.btn_star_big_off);
                }
                mCategorySet.get(position).setStar(!state);
                e.commit();
                mCategorySet.sort();
                notifyDataSetChanged();
            }
        });
        
        return convertView;
    }
}
class CategoryViewHolder {
    ImageView star;
    TextView name;
    TextView count;
}
