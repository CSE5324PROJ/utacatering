package comcse5324projutacatering.httpsgithub.utacatering;
//TODO .. look into android provided calendar layout
//implementation 'com.github.sundeepk:compact-calendar-view:2.0.2.3' needed in build.gradle (app)
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.content.Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT;
import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class user_uc3_ViewReservedEventsCalendar extends Activity {

    //CalendarView calendar;
    CompactCalendarView compactCalendar;
    private SimpleDateFormat dateFormTitle = new SimpleDateFormat("MMMM-yyyy", Locale.US);
    private SimpleDateFormat ymd = new SimpleDateFormat("yyyyMMdd", Locale.US);
    String username;
    List<Long> epochs = new ArrayList<>();

    private Date test1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_uc3_view_reserved_events_calendar);

        Intent mIntent;
        Bundle extras;
        mIntent = getIntent();
        extras=mIntent.getExtras();
        if(extras!=null){
            username = (String) extras.get("username");
        }
        else{
            finish(); //activity not properly accessed
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.DAY_OF_MONTH, 1);
        final ActionBar actionbar = getActionBar();
        if(actionbar!=null){
            actionbar.setDisplayHomeAsUpEnabled(false);
            actionbar.setTitle("MavCat  "+dateFormTitle.format(cal.getTime()));
        }
        cal.setTime(new Date());
        compactCalendar = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
        compactCalendar.setUseThreeLetterAbbreviation(true);


        epochs=DatabaseInterface.getInstance(user_uc3_ViewReservedEventsCalendar .this).getEpochs(username);
        for(int i=0;i<epochs.size();i++){
            compactCalendar.addEvent(new Event(R.color.customCalEventDot,epochs.get(i), "no data"));
        }
        /*
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        long time_at_run=cal.getTimeInMillis();
        cal.add(Calendar.YEAR, 1);
        cal.add(Calendar.DATE, 1);
        long year_and_day_future_epoch=cal.getTimeInMillis();
        cal.setTime(new Date());
        cal.add(Calendar.YEAR, -1);
        cal.add(Calendar.DATE, -1);
        long year_and_day_past_epoch=cal.getTimeInMillis();
        cal.setTime(new Date());
        */



        //Event ev1 = new Event(R.color.customCalEventDot,1524041504000L, "NAME OF EVENT");
        //compactCalendar.addEvent(new Event(R.color.customCalEventDot,1524041504000L, "NAME OF EVENT"));

        //cal.setTimeInMillis(1524041504000L);
        test1 = cal.getTime();
                //compactCalendar.addEvent(ev1);

        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                Context context = getApplicationContext();
                Long test = dateClicked.getTime();
                if(ymd.format(test1).equals(ymd.format(dateClicked))){
                    Toast.makeText(context,"TESTING", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                if(actionbar!=null){
                    actionbar.setTitle("MavCat  "+dateFormTitle.format(firstDayOfNewMonth));
                }
            }
        });








        /*
        calendar = findViewById(R.id.calendar);
        calendar.setDate(time_at_run,true,true);
        calendar.setMaxDate(year_and_day_future_epoch);
        calendar.setMinDate(year_and_day_past_epoch);
        calendar.setShowWeekNumber(false);
        calendar.setSelectedDateVerticalBar(R.drawable.spinner_greenbg);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                //action when different day is selected
            }
        });*/


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
                Intent intent0 = new Intent(user_uc3_ViewReservedEventsCalendar.this, sysuser_uc2_Login.class);
                intent0.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK
                );
                startActivity(intent0);
                finish();
                return true;
            case R.id.main_menu_go_home:
                Intent intent = new Intent(user_uc3_ViewReservedEventsCalendar.this, user_uc0_Home.class);
                intent.putExtra("username",username);
                startActivity(intent);
                finish();
                return true;
            default:
                return false;
        }
    }
}
