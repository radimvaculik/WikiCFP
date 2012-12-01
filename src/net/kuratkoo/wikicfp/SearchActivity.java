package net.kuratkoo.wikicfp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import java.io.IOException;
import net.kuratkoo.wikicfp.adapter.EventListAdapter;
import net.kuratkoo.wikicfp.model.Event;
import net.kuratkoo.wikicfp.model.EventSet;

/**
 * SearchActivity
 * 
 * @author Radim -kuratkoo- Vaculik <kuratkoo@gmail.com>
 */
public class SearchActivity extends SherlockListActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = "WikiCFP|SearchActivity";
    private ListView mListView;
    private EventSet mEventSet;
    private String search;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_search);
        
        Bundle extras = getIntent().getExtras();
        search = extras.getString("search");

        getSupportActionBar().setTitle(search);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mListView = getListView();
        mListView.setOnItemClickListener(this);
        
        mEventSet = new EventSet();

        new CategoryAsyncTask().execute();
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Event e = mEventSet.get(position);
        Intent i = new Intent(SearchActivity.this, EventActivity.class);
        i.putExtra("eventId", e.getId());
        i.putExtra("title", e.getTitle());
        i.putExtra("subtitle", e.getSubtitle());
        i.putExtra("when", e.getWhen());
        i.putExtra("location", e.getLocation());
        i.putExtra("deadline", e.getDeadline());
        startActivity(i);
    }
    
        
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                Intent intent = new Intent(this, CategoryActivity.class);            
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
                startActivity(intent);            
                return true;   
            }
            default: {
                super.onOptionsItemSelected(item);
            } break;
        }
        return true;
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
                mEventSet.search(search);
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
                Toast.makeText(SearchActivity.this, ex.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                return;
            }
            mListView.setAdapter(new EventListAdapter(SearchActivity.this, mEventSet));
        }
    }
}
