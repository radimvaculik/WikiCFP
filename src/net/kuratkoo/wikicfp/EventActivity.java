package net.kuratkoo.wikicfp;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import java.io.IOException;
import net.kuratkoo.wikicfp.model.Category;
import net.kuratkoo.wikicfp.model.Event;

/**
 * EventActivity
 *
 * @author Radim -kuratkoo- Vaculik <kuratkoo@gmail.com>
 */
public class EventActivity extends SherlockActivity {

    private static final String TAG = "WikiCFP|EventActivity";
    private Event mEvent;
    private String category;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_event);
        
        Bundle extras = getIntent().getExtras();

        mEvent = new Event();
        mEvent.setId(extras.getString("eventId"));
        mEvent.setTitle(extras.getString("title"));
        mEvent.setSubtitle(extras.getString("subtitle"));
        mEvent.setLocation(extras.getString("location"));
        mEvent.setWhen(extras.getString("when"));
        mEvent.setDeadline(extras.getString("deadline"));
        
        category = extras.getString("category");
        
        ((TextView) findViewById(R.id.subtitle)).setText(mEvent.getSubtitle());
        ((TextView) findViewById(R.id.location)).setText(mEvent.getLocation());
        ((TextView) findViewById(R.id.when)).setText(mEvent.getWhen());
        ((TextView) findViewById(R.id.deadline)).setText(mEvent.getDeadline());
        
        getSupportActionBar().setTitle(mEvent.getTitle());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        new EventAsyncTask().execute();
    }

    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.menu_event, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.map: {
                String uri = "geo:0,0?q=" + mEvent.getLocation();
                startActivity(new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri)));
                return true;
            }
            case android.R.id.home: {
                Intent intent = new Intent(this, CategoryActivity.class);
                intent.putExtra("category", category);            
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

    private class EventAsyncTask extends AsyncTask<Void, Integer, Exception> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            setSupportProgressBarIndeterminateVisibility(true);
        }

        @Override
        protected Exception doInBackground(Void... params) {
            try {
                mEvent.load();
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
                Toast.makeText(EventActivity.this, ex.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                return;
            }
            ((TextView) findViewById(R.id.finalVersion)).setText(mEvent.getFinalVersion());
            ((TextView) findViewById(R.id.notification)).setText(mEvent.getNotification());
            ((TextView) findViewById(R.id.link)).setText(mEvent.getLink());
            String categories = "";
            for (Category c : mEvent.getCategories()) {
                if (categories.isEmpty()) {
                    categories = categories + c.getName();
                } else {
                    categories = categories + ", " + c.getName();
                }
            }
            ((TextView) findViewById(R.id.categories)).setText(categories);
            ((TextView) findViewById(R.id.description)).setText(mEvent.getDescription());

            ((TextView) findViewById(R.id.relatedTitle1)).setText(mEvent.getRelated().get(0).getTitle());
            ((TextView) findViewById(R.id.relatedSubtitle1)).setText(mEvent.getRelated().get(0).getSubtitle());
             ((TextView) findViewById(R.id.relatedTitle2)).setText(mEvent.getRelated().get(1).getTitle());
            ((TextView) findViewById(R.id.relatedSubtitle2)).setText(mEvent.getRelated().get(1).getSubtitle());
            ((TextView) findViewById(R.id.relatedTitle3)).setText(mEvent.getRelated().get(2).getTitle());
            ((TextView) findViewById(R.id.relatedSubtitle3)).setText(mEvent.getRelated().get(2).getSubtitle());
            ((TextView) findViewById(R.id.relatedTitle4)).setText(mEvent.getRelated().get(3).getTitle());
            ((TextView) findViewById(R.id.relatedSubtitle4)).setText(mEvent.getRelated().get(3).getSubtitle());
            ((TextView) findViewById(R.id.relatedTitle5)).setText(mEvent.getRelated().get(4).getTitle());
            ((TextView) findViewById(R.id.relatedSubtitle5)).setText(mEvent.getRelated().get(4).getSubtitle());
            ((TextView) findViewById(R.id.relatedTitle6)).setText(mEvent.getRelated().get(5).getTitle());
            ((TextView) findViewById(R.id.relatedSubtitle6)).setText(mEvent.getRelated().get(5).getSubtitle());
            
            ((LinearLayout) findViewById(R.id.extend)).setVisibility(View.VISIBLE);
        }
    }
}
