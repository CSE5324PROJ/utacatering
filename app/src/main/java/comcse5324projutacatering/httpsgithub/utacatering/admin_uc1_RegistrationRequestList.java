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
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class admin_uc1_RegistrationRequestList extends Activity {

    private ArrayList<String[]> registrationRequests;
    private ListView regRequestListView;
    private DatabaseInterface db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = DatabaseInterface.getInstance(this);
        setContentView(R.layout.activity_admin_uc1_registration_request_list);
        android.app.ActionBar actionBar = getActionBar();
        if(actionBar != null) {
            actionBar.setTitle("Registration Requests");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        regRequestListView = (ListView)findViewById(R.id.listView);

        populateList();
    }

    private void populateList() {

        registrationRequests = new ArrayList<>();

        Cursor resultCursor = db.getRegistrationRequests();

        if(resultCursor != null) {
            while (resultCursor.moveToNext()) {
                String username = resultCursor.getString(resultCursor.getColumnIndexOrThrow(DatabaseInterface.COLUMN_NAME_USERNAME));
                String req_id   = resultCursor.getString(resultCursor.getColumnIndexOrThrow(BaseColumns._ID));
                long datetime = resultCursor.getLong(resultCursor.getColumnIndexOrThrow(DatabaseInterface.COLUMN_NAME_DATETIME));
                Date date = new Date(datetime);
                SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy @ hh:mm");
                registrationRequests.add(new String[]{username, sdf.format(date), req_id});
            }
        }

        ArrayAdapter<String[]> adapter = new registrationRequestListAdapter();
        regRequestListView.setAdapter(adapter);
    }

    private class registrationRequestListAdapter extends ArrayAdapter<String[]> {

        public registrationRequestListAdapter() {super(admin_uc1_RegistrationRequestList.this, R.layout.listview_item_profiles, registrationRequests);}

        @Override
        public View getView(int position,View view,ViewGroup parent) {
            if(view == null)
                view = getLayoutInflater().inflate(R.layout.listview_item_profiles,parent,false);

            String[] req = registrationRequests.get(position);
            final String req_id = req[2];
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(admin_uc1_RegistrationRequestList.this, admin_uc2_34_ViewRegistrationRequest.class);
                    intent.putExtra("request_id", req_id);
                    startActivity(intent);
                }
            });

            TextView summaryTitle = (TextView)view.findViewById(R.id.item_text_1);
            TextView summaryText = (TextView)view.findViewById(R.id.item_text_2);
            if(summaryTitle != null)
                summaryTitle.setText(req[0]);

            if(summaryText != null)
                summaryText.setText(req[1]);
            return view;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
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
}
