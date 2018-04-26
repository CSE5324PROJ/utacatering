package comcse5324projutacatering.httpsgithub.utacatering;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class catstaff_uc1_ViewAssisgnedEvevntsList extends Activity {

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

    private String active_username;
    private String active_id;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catstaff_uc1_view_assisgned_events_list);
        android.app.ActionBar actionBar = getActionBar();
        if(actionBar != null) {
            actionBar.setTitle("View Assigned Events");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

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
            finish(); //activity not properly accessed
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.DAY_OF_MONTH, 1);

        cal.setTime(new Date());

        final ActionBar actionbar = getActionBar();
        if(actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setTitle("MavCat "+dateFormTitle.format(cal.getTime()));
        }
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
        username=active_username;

        compactCalendar = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
        compactCalendar.setUseThreeLetterAbbreviation(true);
        compactCalendar.setCurrentDate(new Date());


        refreshEvents();

        eventDataListView = (ListView)findViewById(R.id.listViewEvents);
        if(importedDate==null){
            populateList(cal.getTime()); //initialize for the day of.
        }else{
            populateList(importedDate);
        }

        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener(){
            @Override
            public void onDayClick(Date dateClicked) {      //not needed as staff only needs to know on which days he has been assigned
                //populateList(dateClicked);
                //importedDate=dateClicked;
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

    private void populateList(Date dateClicked) {

        Calendar tempCal= Calendar.getInstance();

        eventDataStrings = new ArrayList<>();
        eventData = new ArrayList<>();
        eventData =  DatabaseInterface.getInstance(this).getEventSummary(username,dateClicked);
        importedDate=dateClicked;
        for(int i=0;i<eventData.size();i++){
            tempCal.clear();
            tempCal.setTimeInMillis(eventData.get(i).epoch);
            String tempDate= DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(tempCal.getTime());

            eventDataStrings.add(new String[]{eventData.get(i).hall+" Hall"+System.lineSeparator()+"@ "+tempDate,
                    "For "+eventData.get(i).dur+" hours"+String.valueOf(eventData.get(i).req_user_id),
                    //String.valueOf(eventData.get(i).approval_flag),
                    tempDate,
                    eventData.get(i).dur+" hours",
                    eventData.get(i).hall,
                    String.valueOf(eventData.get(i).attendance),
                    //eventData.get(i).price,
                    //eventData.get(i).mealtype,
                    eventData.get(i).venue,
                    //String.valueOf(eventData.get(i).alco_flag),
                    //String.valueOf(eventData.get(i).formal_flag),
                    //eventData.get(i).occ_type,
                    //eventData.get(i).ent_items,
                    eventData.get(i).eventID
            });
        }
        //ArrayAdapter<String[]> adapter = new cat_uc5_ViewEventCal.eventDataListAdapter();
        //eventDataListView.setAdapter(adapter);
    }

    @Override
    protected void onResume(){
        super.onResume();
        refreshEvents();
        populateList(importedDate);

    }

    private void refreshEvents(){
        compactCalendar.removeAllEvents();
        epochs=DatabaseInterface.getInstance(catstaff_uc1_ViewAssisgnedEvevntsList.this).getEpochs(username);
        for(int i=0;i<epochs.size();i++){
            compactCalendar.addEvent(new Event(R.color.customCalEventDot,epochs.get(i), "no data"));
        }
    }


}
