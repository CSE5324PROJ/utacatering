package comcse5324projutacatering.httpsgithub.utacatering;
//implementation 'com.github.sundeepk:compact-calendar-view:2.0.2.3' needed in build.gradle (app)
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.provider.BaseColumns;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.text.DateFormat;

public class cat_uc5_ViewEventCal extends Activity {
    private String active_username;
    private String active_id;
    private Context mContext;

    //CalendarView calendar;
    CompactCalendarView compactCalendar;
    private SimpleDateFormat dateFormTitle = new SimpleDateFormat("MMMM-yyyy", Locale.US);
    private SimpleDateFormat ymd = new SimpleDateFormat("yyyyMMdd", Locale.US);
    String username;
    List<Long> epochs = new ArrayList<>();
    ListView eventDataListView;
    List<DatabaseInterface.eventSummarySet> eventData;
    List<String[]> eventDataStrings;

    Date importedDate;

    public int customRed;
    public int customGreen;
    public int customBlue;
    public int viewFlag=0;
    private Date test1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat_uc5_view_event_cal);
        mContext = getApplicationContext();

        customRed = getResources().getColor(R.color.customRed);
        customGreen = getResources().getColor(R.color.customGreen);
        customBlue = getResources().getColor(R.color.customBlue);

        Intent mIntent;
        Bundle extras;
        mIntent = getIntent();
        extras=mIntent.getExtras();
        if(extras!=null){
            username = (String) extras.get("username");
            importedDate=(Date) extras.get("trackSelectedDate");
        }
        else{
            //finish(); //activity not properly accessed
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.DAY_OF_MONTH, 1);
        final ActionBar actionbar = getActionBar();
        if(actionbar!=null){
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setTitle("MavCat  "+dateFormTitle.format(cal.getTime()));
        }
        cal.setTime(new Date());

        compactCalendar = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
        compactCalendar.setUseThreeLetterAbbreviation(true);
        compactCalendar.setCurrentDate(new Date());

        //Shared pref stuff
        final SharedPreferences sharedPref = mContext.getSharedPreferences(
                "MavCat.preferences", Context.MODE_PRIVATE
        );
        try{
            active_username = sharedPref.getString("active_username"," ");
            active_id = sharedPref.getString("active_id"," ");
            if(active_username.equals(" ") || active_id.equals(" ")){
                throw new Exception("No valid username/id in shared preferences", null);
            }
        }
        catch(Exception e) {
            if(e.getMessage().equals("No valid username/id in shared preferences")) {
                finish();
            }
        }

        username = active_username;
        refreshEvents();





        eventDataListView = (ListView)findViewById(R.id.listViewEvents);
        if(importedDate==null){
            populateList(cal.getTime()); //initialize for the day of.
        }else{
            populateList(importedDate);
        }


        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                importedDate=dateClicked;
                populateList(dateClicked);
                //Left incase a toast is desired
                //Context context = getApplicationContext();
                /*if(ymd.format(test1).equals(ymd.format(dateClicked))){
                    Toast.makeText(context,"TESTING", Toast.LENGTH_LONG).show();
                }*/
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                if(actionbar!=null){
                    actionbar.setTitle("MavCat  "+dateFormTitle.format(firstDayOfNewMonth));
                }
            }
        });


    }
    @Override
    protected void onResume(){
        super.onResume();
        refreshEvents();
        populateList(importedDate);

    }

    private void refreshEvents(){
        compactCalendar.removeAllEvents();
        epochs=DatabaseInterface.getInstance(cat_uc5_ViewEventCal.this).getEpochs(username);
        for(int i=0;i<epochs.size();i++){
            compactCalendar.addEvent(new Event(R.color.customCalEventDot,epochs.get(i), "no data"));
        }
    }


    private void populateList(Date dateClicked) {

        Calendar tempCal= Calendar.getInstance();

        eventDataStrings = new ArrayList<>();
        eventData = new ArrayList<>();
        eventData =  DatabaseInterface.getInstance(this).getEventSummary(username,dateClicked);
        importedDate=dateClicked;
        for(int i=0;i<eventData.size();i++){
            tempCal.clear();
            tempCal.setTimeInMillis(eventData.get(i).epoch);
            String tempDate=DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(tempCal.getTime());

            eventDataStrings.add(new String[]{eventData.get(i).hall+" Hall"+System.lineSeparator()+"@ "+tempDate,
                    "For "+eventData.get(i).dur+" hours"+" at the price of $"+eventData.get(i).price,
                    String.valueOf(eventData.get(i).req_user_id),
                    String.valueOf(eventData.get(i).approval_flag),
                    tempDate,
                    eventData.get(i).dur+" hours",
                    eventData.get(i).hall,
                    String.valueOf(eventData.get(i).attendance),
                    eventData.get(i).price,
                    eventData.get(i).mealtype,
                    eventData.get(i).venue,
                    String.valueOf(eventData.get(i).alco_flag),
                    String.valueOf(eventData.get(i).formal_flag),
                    eventData.get(i).occ_type,
                    eventData.get(i).ent_items,
                    eventData.get(i).eventID
            });
        }
        ArrayAdapter<String[]> adapter = new cat_uc5_ViewEventCal.eventDataListAdapter();
        eventDataListView.setAdapter(adapter);
    }

    private class eventDataListAdapter extends ArrayAdapter<String[]> {

        public eventDataListAdapter() {super(cat_uc5_ViewEventCal.this, R.layout.listview_item_events_listing, eventDataStrings);}

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if(view == null){
                view = getLayoutInflater().inflate(R.layout.listview_item_events_listing,parent,false);
            }

            final String[] out = eventDataStrings.get(position);
            final Date trackSelectedDate = importedDate;
            final String trackUsername =username;
            /*
             * Array position translation:
             * 0)Title String
             * 1)Subtitle String (duration & price)
             * 2)req_user_id
             * 3)approval_flag
             * 4)User friendly date string
             * 5)User friendly hours string
             * 6)hall
             * 7)Attendance
             * 8)price
             * 9)meal type
             * 10)venue
             * 11)alco_flag
             * 12)formal_flag
             * 13)occ_type string
             * 14)ent_items string
             * 15)eventID
             * */
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(cat_uc5_ViewEventCal.this, cat_uc6_78_ViewEventDetails.class);
                    intent.putExtra("event_data_string_array", out);
                    intent.putExtra("trackSelectedDate", trackSelectedDate);
                    intent.putExtra("username", trackUsername);
                    startActivity(intent);
                }
            });

            TextView summaryTitle = (TextView)view.findViewById(R.id.item_text_1);
            TextView summaryText = (TextView)view.findViewById(R.id.item_text_2);
            TextView warnText = (TextView)view.findViewById(R.id.item_text_3);
            if(summaryTitle != null){
                summaryTitle.setText(out[0]);
                if(out[3].equals("0")){//check approval
                    summaryTitle.setTextColor(customRed);
                }
                else{
                    summaryTitle.setTextColor(Color.parseColor("#40446d"));
                }
            }


            if(summaryText != null){

                if(out[3].equals("0")){//check approval
                    if(viewFlag==0){
                        viewFlag=1;
                    }
                    summaryText.setText(out[1]);
                    summaryText.setTextColor(customRed);
                }
                else{
                    summaryText.setText(out[1]);
                    summaryText.setTextColor(Color.parseColor("#40446d"));
                }
            }
            if(warnText!=null){
                if(out[3].equals("0")) {
                    warnText.setText("(event pending approval)");
                    warnText.setTextColor(customRed);
                }

            }

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
            case R.id.main_menu_sign_out:
                Intent intent0 = new Intent(cat_uc5_ViewEventCal.this, sysuser_uc2_Login.class);
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
                Intent intent = new Intent(cat_uc5_ViewEventCal.this, cat_uc0_Home.class);
                intent.putExtra("username",username);
                startActivity(intent);
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
