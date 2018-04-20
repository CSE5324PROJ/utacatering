package comcse5324projutacatering.httpsgithub.utacatering;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class cat_uc1_ViewEventReqQueue extends Activity {

    private SimpleDateFormat dateFormTitle = new SimpleDateFormat("MMMM-yyyy", Locale.US);
    private ArrayList<String[]> eventRequestSummaryStrings;
    private List<DatabaseInterface.eventSummarySet> eventRequestSummaryData;
    private ListView eventRequestListView;
    Date importedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent mIntent = getIntent();
        Bundle extras = mIntent.getExtras();

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.DAY_OF_MONTH, 1);

        setContentView(R.layout.activity_cat_uc1_view_event_req_queue);
        android.app.ActionBar actionBar = getActionBar();
        if(actionBar != null) {
            actionBar.setTitle("Event Request Queue");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        eventRequestListView = (ListView) findViewById(R.id.listView);
    }

    private void populateList(Date dateClicked) {

        Calendar tempCal = Calendar.getInstance();

        eventRequestSummaryStrings = new ArrayList<>();
        eventRequestSummaryData = DatabaseInterface.getInstance(this).getEventRequestSummaries();
        importedDate = dateClicked;

        for (int i = 0; i < eventRequestSummaryData.size(); i++)
        {
            tempCal.clear();
            tempCal.setTimeInMillis((eventRequestSummaryData.get(i).epoch));
            String tempDate=DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(tempCal.getTime());
            /* 0) eventRequestSummaryData.
            * */
            eventRequestSummaryStrings.add(new String[]{eventRequestSummaryData.get(i).hall+" Hall"+System.lineSeparator()+"@ "+tempDate,
                    "For "+eventRequestSummaryData.get(i).dur+" hours"+" at the price of $"+eventRequestSummaryData.get(i).price,
                    String.valueOf(eventRequestSummaryData.get(i).req_user_id),
                    String.valueOf(eventRequestSummaryData.get(i).approval_flag),
                    tempDate,
                    eventRequestSummaryData.get(i).dur+" hours",
                    eventRequestSummaryData.get(i).hall,
                    String.valueOf(eventRequestSummaryData.get(i).attendance),
                    eventRequestSummaryData.get(i).price,
                    eventRequestSummaryData.get(i).mealtype,
                    eventRequestSummaryData.get(i).venue,
                    String.valueOf(eventRequestSummaryData.get(i).alco_flag),
                    String.valueOf(eventRequestSummaryData.get(i).formal_flag),
                    eventRequestSummaryData.get(i).occ_type,
                    eventRequestSummaryData.get(i).ent_items,
                    eventRequestSummaryData.get(i).eventID
            });
        }
        ArrayAdapter<String[]> adapter = new cat_uc1_ViewEventReqQueue.eventRequestSummaryDataAdapter();
        eventRequestListView.setAdapter(adapter);
    }

    private class eventRequestSummaryDataAdapter extends ArrayAdapter<String[]>{

        public eventRequestSummaryDataAdapter() {super(cat_uc1_ViewEventReqQueue.this, R.layout.listview_item_event_request_listing, eventRequestSummaryStrings);}

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if (view == null) {
                view = getLayoutInflater().inflate(R.layout.listview_item_events_listing, parent, false);
            }

            final String[] selectedEventRequestInfo = eventRequestSummaryStrings.get(position);

            //Setup of the list of "items" on the list view
            TextView eventRequestSummaryTitle = (TextView) view.findViewById(R.id.item_text_1);
            if (eventRequestSummaryTitle != null)
            {
                eventRequestSummaryTitle.setText(selectedEventRequestInfo[0]);
            }

            final Date trackSelectedDate = importedDate;
            /*
             * Array position translation:
             * 0) Title String
             * */
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent newIntent = new Intent(cat_uc1_ViewEventReqQueue.this, cat_uc34_ApproveOrRejectEventReq.class);
                    newIntent.putExtra("selectedEventRequestInfo", selectedEventRequestInfo);
                    startActivity(newIntent);
                }
            });

            return view;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent( cat_uc1_ViewEventReqQueue.this, cat_uc0_Home.class);
        startActivity(intent);
        finish();
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
            case R.id.main_menu_sign_out:
                Intent intent0 = new Intent(cat_uc1_ViewEventReqQueue.this, sysuser_uc2_Login.class);
                intent0.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK
                );
                //Makes sure shared preference is reset
                Context mContext = getApplicationContext();
                final SharedPreferences sharedPref = mContext.getSharedPreferences(
                        "MavCat.preferences", Context.MODE_PRIVATE
                );
                final SharedPreferences.Editor editor = sharedPref.edit();
                editor.remove("active_username");
                editor.remove("active_id");
                editor.commit();
                //------
                startActivity(intent0);
                finish();
                return true;
            case R.id.main_menu_go_home:
                Intent intent1 = new Intent(cat_uc1_ViewEventReqQueue.this, cat_uc0_Home.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP

                );
                startActivity(intent1);
                finish();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return false;
        }
    }
}