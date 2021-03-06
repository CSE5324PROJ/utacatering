package comcse5324projutacatering.httpsgithub.utacatering;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;

public class admin_uc5_SearchSystemUser extends Activity {

    private ArrayList<String[]> resultsList;
    private ListView searchResultsView;
    private SearchView searchView;
    private String lastSearch;

    private DatabaseInterface db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = DatabaseInterface.getInstance(this);
        setContentView(R.layout.activity_admin_uc5_search_sys_user);
        android.app.ActionBar actionBar = getActionBar();
        if(actionBar != null) {
            actionBar.setTitle("Search for System User");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        searchView        = (SearchView) findViewById(R.id.searchView);
        searchResultsView = (ListView)   findViewById(R.id.listView);
        searchView.setIconified(false);
        searchView.requestFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                submitSearch(s);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    private void submitSearch(String s) {
        lastSearch = s;
        searchView.clearFocus();
        resultsList = new ArrayList<>();
        Cursor resultCursor = db.searchUsername(s);
        if(resultCursor != null && resultCursor.getCount()>0) {
            while (resultCursor.moveToNext()) {
                String username = resultCursor.getString(resultCursor.getColumnIndexOrThrow(DatabaseInterface.COLUMN_NAME_USERNAME));
                String role = resultCursor.getString(resultCursor.getColumnIndexOrThrow(DatabaseInterface.COLUMN_NAME_ROLE));
                String profileID = resultCursor.getString(resultCursor.getColumnIndexOrThrow(BaseColumns._ID));
                resultsList.add(new String[]{username, role, profileID});
            }
        } else {
            resultsList.add(new String[] {"No results","",""});
        }
        populateList();
    }

    private void populateList() {
        ArrayAdapter<String[]> adapter = new searchResultsListAdapter();
        searchResultsView.setAdapter(adapter);
    }

    private class searchResultsListAdapter extends ArrayAdapter<String[]> {

        public searchResultsListAdapter() {super(admin_uc5_SearchSystemUser.this, R.layout.listview_item_profiles, resultsList);}

        @Override
        public View getView(int position,View view,ViewGroup parent) {
            if(view == null)
                view = getLayoutInflater().inflate(R.layout.listview_item_profiles,parent,false);

            String[] result = resultsList.get(position);
            final String profile_id = result[2];
            if(! profile_id.equals(""))
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(admin_uc5_SearchSystemUser.this, admin_uc6_78_ViewEditProfile.class);
                        intent.putExtra("profile_id", profile_id);
                        startActivity(intent);
                    }
                });

            TextView summaryTitle = (TextView)view.findViewById(R.id.item_text_1);
            TextView summaryText  = (TextView)view.findViewById(R.id.item_text_2);
            if(summaryTitle != null)
                summaryTitle.setText(result[0]);

            if(summaryText != null)
                summaryText.setText(result[1]);
            return view;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.main_menu_go_home:
                finish();
                return true;
            case R.id.main_menu_sign_out:
                startActivity(db.logout(this));
                finish();
                return true;
            default:
                return false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (lastSearch != null)
            submitSearch(lastSearch);
    }
}
