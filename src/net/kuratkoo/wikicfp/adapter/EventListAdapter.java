package net.kuratkoo.wikicfp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import net.kuratkoo.wikicfp.R;
import net.kuratkoo.wikicfp.model.Category;
import net.kuratkoo.wikicfp.model.Event;
import net.kuratkoo.wikicfp.model.EventSet;

/**
 * EventListAdapter
 * @author Radim -kuratkoo- Vaculik <kuratkoo@gmail.com>
 */
public class EventListAdapter extends BaseAdapter {

    private EventSet mEventSet;
    private static final String TAG = "WikiCFP|EventListAdapter";
    private LayoutInflater mInflater;
    
    public EventListAdapter(Context context, EventSet eventSet) {
        mEventSet = eventSet;
        mInflater = LayoutInflater.from(context);
    }
    
    public int getCount() {
        return mEventSet.size();
    }

    public Event getItem(int position) {
        return mEventSet.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final EventViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.listitem_event, parent, false);

            holder = new EventViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.subtitle = (TextView) convertView.findViewById(R.id.subtitle);
            holder.location = (TextView) convertView.findViewById(R.id.location);
            holder.when = (TextView) convertView.findViewById(R.id.when);
            holder.deadline = (TextView) convertView.findViewById(R.id.deadline);
            
            convertView.setTag(holder);
        } else {
            holder = (EventViewHolder) convertView.getTag();
        }

        holder.title.setText(this.getItem(position).getTitle());
        holder.subtitle.setText(this.getItem(position).getSubtitle());
        holder.location.setText(this.getItem(position).getLocation());
        holder.when.setText(this.getItem(position).getWhen());
        holder.deadline.setText(this.getItem(position).getDeadline());
        
        return convertView;
    }
}
class EventViewHolder {
    TextView title;
    TextView subtitle;
    TextView location;
    TextView when;
    TextView deadline;
}
