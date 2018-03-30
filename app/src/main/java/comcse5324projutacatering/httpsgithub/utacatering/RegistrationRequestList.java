package comcse5324projutacatering.httpsgithub.utacatering;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class RegistrationRequestList extends Activity {

    private ArrayList<String[]> registrationRequests;
    private ListView regRequestListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_request_list);
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

        Cursor resultCursor = DatabaseInterface.getInstance(this).getRegistrationRequests();

        if(resultCursor != null) {
            while (resultCursor.moveToNext()) {
                String username = resultCursor.getString(resultCursor.getColumnIndexOrThrow(DatabaseInterface.COLUMN_NAME_USERNAME));
                String req_id   = resultCursor.getString(resultCursor.getColumnIndexOrThrow(BaseColumns._ID));
                registrationRequests.add(new String[]{username, "Date/Time", req_id});
            }
        }

        ArrayAdapter<String[]> adapter = new registrationRequestListAdapter();
        regRequestListView.setAdapter(adapter);
    }

    private class registrationRequestListAdapter extends ArrayAdapter<String[]> {

        public registrationRequestListAdapter() {super(RegistrationRequestList.this, R.layout.listview_item, registrationRequests);}

        @Override
        public View getView(int position,View view,ViewGroup parent) {
            if(view == null)
                view = getLayoutInflater().inflate(R.layout.listview_item,parent,false);

            String[] req = registrationRequests.get(position);
            final String req_id = req[2];
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(RegistrationRequestList.this, ViewRegistrationRequest.class);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateList();
    }
}
