package net.kuratkoo.wikicfp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Window;
import java.io.IOException;
import net.kuratkoo.wikicfp.adapter.EventListAdapter;
import net.kuratkoo.wikicfp.model.Event;
import net.kuratkoo.wikicfp.model.EventSet;

public class CategoryActivity extends SherlockListActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = "WikiCFP|CategoryActivity";
    private ListView mListView;
    private EventSet mEventSet;
    private String category;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_category);
        
        Bundle extras = getIntent().getExtras();
        category = extras.getString("category");

        getSupportActionBar().setTitle(category.substring(0, 1).toUpperCase() + category.substring(1));

        mListView = getListView();
        mListView.setOnItemClickListener(this);
        mEventSet = new EventSet();

        new CategoryAsyncTask().execute();
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Event e = mEventSet.get(position);
        Intent i = new Intent(CategoryActivity.this, EventActivity.class);
        i.putExtra("eventId", e.getId());
        i.putExtra("title", e.getTitle());
        i.putExtra("subtitle", e.getSubtitle());
        i.putExtra("when", e.getWhen());
        i.putExtra("location", e.getLocation());
        i.putExtra("deadline", e.getDeadline());
        startActivity(i);
    }

    private class CategoryAsyncTask extends AsyncTask<Void, Integer, Exception> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            setSupportProgressBarIndeterminateVisibility(true);
        }

        @Override
        protected Exception doInBackground(Void... params) {
            try {
                mEventSet.load(category);
            } catch (IOException ex) {
                return ex;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Exception ex) {
            super.onPostExecute(ex);
            setSupportProgressBarIndeterminateVisibility(false);
            if (ex != null) {
                Log.w(TAG, ex);
                Toast.makeText(CategoryActivity.this, ex.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                return;
            }
            mListView.setAdapter(new EventListAdapter(CategoryActivity.this, mEventSet));
        }
    }
}
