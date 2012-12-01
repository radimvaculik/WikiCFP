package net.kuratkoo.wikicfp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.actionbarsherlock.widget.SearchView;
import java.io.IOException;
import net.kuratkoo.wikicfp.adapter.CategoryListAdapter;
import net.kuratkoo.wikicfp.model.CategorySet;

/**
 * MainActivity
 * 
 * @author Radim -kuratkoo- Vaculik <kuratkoo@gmail.com>
 */
public class MainActivity extends SherlockListActivity implements AdapterView.OnItemClickListener, SearchView.OnQueryTextListener {

    private static final String TAG = "WikiCFP|MainActivity";
    private ListView mListView;
    private CategorySet mCategorySet;
    private SearchView mSearchView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle(R.string.categories_title);

        mListView = getListView();
        mListView.setOnItemClickListener(this);

        mSearchView = new SearchView(getSupportActionBar().getThemedContext());
        mSearchView.setQueryHint(getText(R.string.search));
        mSearchView.setOnQueryTextListener(this);

        mCategorySet = new CategorySet(this);

        new MainAsyncTask().execute();
        
        SharedPreferences sharedPref = getSharedPreferences("star", 0);

        if (sharedPref.getInt("count", 0) == 5) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View dialog = inflater.inflate(R.layout.dialog_rate, null);

            AlertDialog.Builder d = new AlertDialog.Builder(this);
            d.setTitle(R.string.support_app);
            d.setIcon(R.drawable.ic_launcher);
            d.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface di, int arg1) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getClass().getPackage().getName())));
                }
            });
            d.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface di, int arg1) {
                    di.dismiss();
                }
            });
            d.setView(dialog);
            d.show();
        }
        Editor e = sharedPref.edit();
        e.putInt("count", sharedPref.getInt("count", 0) + 1);
        e.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("Search")
                .setIcon(R.drawable.abs__ic_search)
                .setActionView(mSearchView)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);

        return true;
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivity(new Intent(MainActivity.this, CategoryActivity.class).putExtra("category", mCategorySet.get(position).getName()));
    }

    public boolean onQueryTextSubmit(String string) {
        startActivity(new Intent(MainActivity.this, SearchActivity.class).putExtra("search", string));
        return false;
    }

    public boolean onQueryTextChange(String string) {
        return false;
    }

    private class MainAsyncTask extends AsyncTask<Void, Integer, Exception> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            setSupportProgressBarIndeterminateVisibility(true);
        }

        @Override
        protected Exception doInBackground(Void... params) {
            try {
                mCategorySet.load();
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
                Toast.makeText(MainActivity.this, ex.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                return;
            }
            mListView.setAdapter(new CategoryListAdapter(MainActivity.this, mCategorySet));
        }
    }
}
