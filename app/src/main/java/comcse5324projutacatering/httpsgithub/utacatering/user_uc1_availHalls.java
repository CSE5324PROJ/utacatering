package comcse5324projutacatering.httpsgithub.utacatering;
//TODO test case with a time range where there are no available halls at all.
//TODO translated spinner options to more user friendly options (month translated to 1-12, days of the week (Mon-Sunday) to be done, change from military time.
//TODO buffer time between events? Ask Robb.
//TODO last step incrementing of date/time if it is hour 24, check sqlite docs if its even necessary
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

//Work in progress
public class user_uc1_availHalls extends Activity{

    List<Integer> validYear = new ArrayList<>();
    List<Integer> validMonth = new ArrayList<>();
    List<Integer> validDay = new ArrayList<>();
    Set<Integer> uniqueYear = new LinkedHashSet<>();
    Set<Integer> uniqueMonth = new LinkedHashSet<>();
    Set<Integer> uniqueDay = new LinkedHashSet<>();
    String uniqueYearArray[];
    int selectedYear;
    int selectedMonth;
    int selectedDay;
    int selectedHour;
    int selectedMin;
    int selectedDur;
    String selectedHall;
    Spinner spinYear;
    Spinner spinMonth;
    Spinner spinDay;
    Spinner spinHour;
    Spinner spinMin;
    Spinner spinDur;
    Spinner spinHalls;
    Set<Integer> uniqueMonthValidSet = new LinkedHashSet<>();
    String uniqueMonthArray[];
    Set<Integer> uniqueDayValidSet = new LinkedHashSet<>();
    String uniqueDayArray[];
    String halls[]={"Maverick", "KC", "Arlington", "Shard", "Liberty"};
    String availHalls[];
    List<String> conflictingHalls = new ArrayList<>();
    int i=0;
    int hours_mil[];
    int minutes[]=new int[12];
    String hours_milStr[];
    String minutesStr[]= new String[12];
    String durations[]={"2 hr","3 hr","4 hr","5 hr","6 hr"};
    Integer durationsInt[]={2,3,4,5,6};
    public String EVENT_HALL_COL;
    private Context mContext;
    private boolean userIsInteracting;
    Calendar caltemp;
    int iteratingDayofWeek;

    private Button user_uc2_ReqEvent_btn;
    ArrayAdapter<String> dayAdapter;
    ArrayAdapter<String> monthAdapter;
    ArrayAdapter<String> yearAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
        setContentView(R.layout.activity_user_uc1_avail_halls);

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date()); // today's date
        cal.add(Calendar.DATE, 7); // Adds 7 days
        cal.add(Calendar.HOUR, 1); // Adds 1 hour
        int year = cal.get(Calendar.YEAR);
        selectedYear = year; //default 1st selection is the current year
        int month = cal.get(Calendar.MONTH);
        selectedMonth = month;
        int day_of_month = cal.get(Calendar.DAY_OF_MONTH);
        selectedDay=day_of_month;
        int hour_of_day = cal.get(Calendar.HOUR_OF_DAY); //Military time used
        cal.set(year,month,day_of_month,hour_of_day,0,0);
        Calendar cal2 = (Calendar)cal.clone();
        cal2.add(Calendar.MONTH, 12); // Adds 12 months to 2nd calendar date (last day an event can be scheduled)

        while(cal.before(cal2)){
            validYear.add(cal.get(Calendar.YEAR));
            validMonth.add(cal.get(Calendar.MONTH));
            validDay.add(cal.get(Calendar.DAY_OF_MONTH));
            cal.add(Calendar.DATE, 1);
        }
        uniqueYear.addAll(validYear);
        uniqueMonth.addAll(validMonth);
        uniqueDay.addAll(validDay);
        String uniqueYearString = Arrays.toString(uniqueYear.toArray(new Integer[uniqueYear.size()]));
        uniqueYearArray=uniqueYearString.substring(1,uniqueYearString.length()-1).split(", ");

        spinYear = (Spinner) findViewById(R.id.spinner_year);
        yearAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, uniqueYearArray);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinYear.setAdapter(yearAdapter);


        EVENT_HALL_COL = mContext.getString(R.string.EVENT_HALL_COL);
        genMonthsArray();
        genDaysArray();
        genOtherArray();
        caltemp = Calendar.getInstance();
        getHoursArray();
        searchAvailHalls(selectedYear,selectedMonth,selectedDay,selectedHour,selectedMin,selectedDur);
        addListeners();
        setupButtons();
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        userIsInteracting = true;
    }

    private void searchAvailHalls(int year, int month, int day, int hour, int minute, int dur){
        conflictingHalls.clear();
        Calendar calTemp = Calendar.getInstance();
        calTemp.set(year,month,day,hour,minute);
        long start = calTemp.getTimeInMillis();
        start = start / (long)1000;
        String startString = String.valueOf(start);
        long end = start + ((long)dur * (long)3600);
        String endString = String.valueOf(end);
        Cursor resultCursor = DatabaseInterface.getInstance(this).searchAvailHalls(startString.substring(0,10), endString.substring(0,10));
        if(resultCursor != null) {
            while (resultCursor.moveToNext()) {
                String resultHall = resultCursor.getString(resultCursor.getColumnIndexOrThrow(EVENT_HALL_COL));
                conflictingHalls.add(resultHall);
            }
        }
        availHalls =  new  String[5-conflictingHalls.size()]; //At most 5 conflicts (thus no halls available)
        int j;
        for(i=0,j=0;i<availHalls.length;i++,j++){
            if(!conflictingHalls.contains(halls[j])){
                availHalls[i]=halls[j];
            }
            else{
                i--;
            }
        }
        spinHalls = (Spinner) findViewById(R.id.spinner_availHalls);
        if(availHalls.length>0){
            ArrayAdapter<String> hallsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, availHalls);
            hallsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinHalls.setAdapter(hallsAdapter);
        }
        else{
            spinHalls.setAdapter(null);
            selectedHall="";
            user_uc2_ReqEvent_btn.setBackgroundColor(Color.RED);
        }

    }

    private void addListeners() {


        spinYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                //selectedRole = String.valueOf(editRole.getSelectedItem());
                Spinner spinner = (Spinner) adapterView;
                if(spinner.getId() == R.id.spinner_year) {
                    selectedYear=Integer.parseInt(uniqueYearArray[position]);
                    genMonthsArray();
                    genDaysArray();
                    /*if(userIsInteracting){
                        searchAvailHalls(selectedYear,selectedMonth,selectedDay,selectedHour,selectedMin,selectedDur);
                        userIsInteracting=false;
                    }*/
                }
            }
            @Override public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        spinMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                //selectedRole = String.valueOf(editRole.getSelectedItem());
                Spinner spinner = (Spinner) adapterView;
                if(spinner.getId() == R.id.spinner_month) {
                    selectedMonth=Integer.parseInt(uniqueMonthArray[position]);
                    genDaysArray();
                    if(userIsInteracting){
                        searchAvailHalls(selectedYear,selectedMonth,selectedDay,selectedHour,selectedMin,selectedDur);
                        userIsInteracting=false;
                    }
                }
            }
            @Override public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        spinDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                //selectedRole = String.valueOf(editRole.getSelectedItem());
                Spinner spinner = (Spinner) adapterView;
                if(spinner.getId() == R.id.spinner_day) {
                    selectedDay=Integer.parseInt(uniqueDayArray[position]);
                    //searchAvailHalls(selectedYear,selectedMonth,selectedDay,selectedHour,selectedMin,selectedDur);
                    getHoursArray();
                    if(userIsInteracting){
                        searchAvailHalls(selectedYear,selectedMonth,selectedDay,selectedHour,selectedMin,selectedDur);
                        userIsInteracting=false;
                    }
                }
            }
            @Override public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        spinHour.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                //selectedRole = String.valueOf(editRole.getSelectedItem());
                Spinner spinner = (Spinner) adapterView;
                if(spinner.getId() == R.id.spinner_hour) {
                    selectedHour=(hours_mil[position]);
                    if(selectedHour!=0){
                        if (!spinMin.isEnabled()){//if hour spinner is reset without user selecting the hour, safe to re-enable minute spinner.
                            spinMin.setEnabled(true);
                        }
                    }
                    else{
                        spinMin.setSelection(0);
                        spinMin.setEnabled(false);
                    }
                    //searchAvailHalls(selectedYear,selectedMonth,selectedDay,selectedHour,selectedMin,selectedDur);
                    if(userIsInteracting){
                        if((hours_mil.length-1)==position && selectedHour!=24){//means last possible hour was selected, so minute must be hard set to 00.
                            spinMin.setSelection(0);
                            spinMin.setEnabled(false);
                        }
                        else if(selectedHour!=0 && selectedHour!=24){
                            spinMin.setEnabled(true);
                        }
                        else if(selectedHour==24){
                            String tempDaySearch = String.valueOf(selectedDay);
                            int dayPos=dayAdapter.getPosition(tempDaySearch);
                            if((dayAdapter.getCount()-1)!=dayPos){//if not last day on spinner, increment it
                                dayPos=dayPos+1;
                                spinDay.setSelection(dayPos);
                                spinHour.setSelection(0);
                                spinMin.setSelection(0);
                                spinMin.setEnabled(false);
                            }
                            else{
                                String tempMonthSearch = String.valueOf(selectedMonth);
                                int monthPos=monthAdapter.getPosition(tempMonthSearch);
                                if((monthAdapter.getCount()-1)!=monthPos){//if it is last day on spinner, and it isnt last month on spinner, increment both
                                    monthPos=monthPos+1;
                                    spinMonth.setSelection(monthPos);
                                    spinDay.setSelection(0);
                                    spinHour.setSelection(0);
                                    spinMin.setSelection(0);
                                    spinMin.setEnabled(false);
                                }
                                else{
                                    String tempYearSearch = String.valueOf(selectedYear);
                                    int yearPos=yearAdapter.getPosition(tempYearSearch);
                                    if((yearAdapter.getCount()-1)!=yearPos){//if it is last day on spinner, and it is last month on spinner, and it isnt last year on spinner, increment all 3.
                                        yearPos=yearPos+1;
                                        spinYear.setSelection(yearPos);
                                        spinMonth.setSelection(0);
                                        spinDay.setSelection(0);
                                        spinHour.setSelection(0);
                                        spinMin.setSelection(0);
                                        spinMin.setEnabled(false);
                                    }
                                    else{
                                        spinMin.setSelection(0);
                                        spinMin.setEnabled(false);
                                    }
                                }
                            }




                        }
                        searchAvailHalls(selectedYear,selectedMonth,selectedDay,selectedHour,selectedMin,selectedDur);
                        userIsInteracting=false;
                    }
                }
            }
            @Override public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        spinMin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                //selectedRole = String.valueOf(editRole.getSelectedItem());
                Spinner spinner = (Spinner) adapterView;
                if(spinner.getId() == R.id.spinner_minute) {
                    selectedMin=(minutes[position]);
                    //searchAvailHalls(selectedYear,selectedMonth,selectedDay,selectedHour,selectedMin,selectedDur);
                    if(userIsInteracting){
                        searchAvailHalls(selectedYear,selectedMonth,selectedDay,selectedHour,selectedMin,selectedDur);
                        userIsInteracting=false;
                    }
                }
            }
            @Override public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        spinDur.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                //selectedRole = String.valueOf(editRole.getSelectedItem());
                Spinner spinner = (Spinner) adapterView;
                if(spinner.getId() == R.id.spinner_duration) {
                    selectedDur=(durationsInt[position]);
                    //searchAvailHalls(selectedYear,selectedMonth,selectedDay,selectedHour,selectedMin,selectedDur);
                    if(userIsInteracting){
                        getHoursArray(); //reset hour selection when changing duration to prevent conflict
                        //if (!spinMin.isEnabled()){hour is reset, so make sure minutes are available again
                            //spinMin.setEnabled(true);
                        //}
                        searchAvailHalls(selectedYear,selectedMonth,selectedDay,selectedHour,selectedMin,selectedDur);
                        userIsInteracting=false;
                    }
                }
            }
            @Override public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        spinHalls.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                //selectedRole = String.valueOf(editRole.getSelectedItem());
                Spinner spinner = (Spinner) adapterView;
                if(spinner.getId() == R.id.spinner_availHalls) {
                    if(availHalls.length>0){
                        selectedHall=(availHalls[position]);
                        user_uc2_ReqEvent_btn.setBackgroundColor(Color.BLUE);
                        //if size =0 selectedHall = "", as updated by searchAvailHalls function when another spinner is accessed.
                    }
                    //searchAvailHalls(selectedYear,selectedMonth,selectedDay,selectedHour,selectedMin,selectedDur);
                    if(userIsInteracting){
                        //searchAvailHalls(selectedYear,selectedMonth,selectedDay,selectedHour,selectedMin,selectedDur);
                        userIsInteracting=false;
                    }
                }
            }
            @Override public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }

    private void genMonthsArray() {
        uniqueMonthValidSet.clear();
        int sizing = validYear.size();
        int i=0;
        for(i=0;i<sizing;i++){
            if(validYear.get(i)==selectedYear){
                uniqueMonthValidSet.add(validMonth.get(i));
            }
        }
        String uniqueMonthString = Arrays.toString(uniqueMonthValidSet.toArray(new Integer[uniqueMonthValidSet.size()]));
        uniqueMonthArray= new String[uniqueMonthValidSet.size()];
        String[] uniqueMonthArrayFriendly= new String[uniqueMonthValidSet.size()];
        String uniqueMonthArrayTemp[]=uniqueMonthString.substring(1,uniqueMonthString.length()-1).split(", ");
        for(i=0;i<uniqueMonthValidSet.size();i++){
            uniqueMonthArray[i]=uniqueMonthArrayTemp[i];
            uniqueMonthArrayFriendly[i]=String.valueOf(Integer.parseInt(uniqueMonthArrayTemp[i])+1);
        }
        monthAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, uniqueMonthArray); //used for indexing
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<String> monthAdapterF = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, uniqueMonthArrayFriendly); //user visible version
        monthAdapterF.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinMonth = (Spinner) findViewById(R.id.spinner_month);
        spinMonth.setAdapter(monthAdapterF);

    }

    private ArrayAdapter<String> genDaysArray() {
        uniqueDayValidSet.clear();
        int sizing = validYear.size();
        int i=0;
        for(i=0;i<sizing;i++){
            if(validYear.get(i)==selectedYear && validMonth.get(i)==selectedMonth){
                uniqueDayValidSet.add(validDay.get(i));
            }
        }
        String uniqueDayString = Arrays.toString(uniqueDayValidSet.toArray(new Integer[uniqueDayValidSet.size()]));
        uniqueDayArray= new String[uniqueDayValidSet.size()];
        String uniqueDayArrayTemp[]=uniqueDayString.substring(1,uniqueDayString.length()-1).split(", ");
        for(i=0;i<uniqueDayValidSet.size();i++){
            uniqueDayArray[i]=uniqueDayArrayTemp[i];
        }
        dayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, uniqueDayArray);
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinDay = (Spinner) findViewById(R.id.spinner_day);
        spinDay.setAdapter(dayAdapter);


        return dayAdapter;
    }

    private void getHoursArray(){
        caltemp.set(selectedYear,selectedMonth,selectedDay,12,0,0);
        //DAY_OF_WEEK = Monday, Tuesday etc. SUNDAY IS = integer 1.
        iteratingDayofWeek=caltemp.get(Calendar.DAY_OF_WEEK);
        int startHour;
        int endHour;
        if(iteratingDayofWeek==1){//Sundays open different time
            startHour=12;
        }
        else {
            startHour=7;
        }
        if(iteratingDayofWeek==1 || iteratingDayofWeek==7){//Sunday and Saturday open until 2AM.
            endHour=24;
        }
        else{
            endHour=21;
        }
        endHour=(endHour-(selectedDur-2))+1; //prior end hour assumed 2 hr duration
        //below 1st loop misses that one can schedule a 2hr event at midnight day of sunday and monday.
        //Loops allow i=endHour because when endHour is selected, minute cannot pass 00.
        if(selectedDur==2){
            if(iteratingDayofWeek!=1 && iteratingDayofWeek!=2){
                hours_mil=new int[(endHour-startHour)];//
                hours_milStr=new String[(endHour-startHour)];
                for(i=startHour;i<endHour;i++){
                    hours_mil[i-startHour]=i;
                    hours_milStr[i-startHour]=String.valueOf(i);
                }
            }
            else{
                hours_mil=new int[(endHour-startHour)+1];
                hours_milStr=new String[(endHour-startHour)+1];
                hours_mil[0]=0; //adding midnight possibility
                hours_milStr[0]="0";
                for(i=startHour;i<endHour;i++){
                    hours_mil[i-startHour+1]=i;
                    hours_milStr[i-startHour+1]=String.valueOf(i);
                }
            }
        }
        else{
            hours_mil=new int[endHour-startHour];
            hours_milStr=new String[endHour-startHour];
            for(i=startHour;i<endHour;i++){
                hours_mil[i-startHour]=i;
                hours_milStr[i-startHour]=String.valueOf(i);
            }
        }





        ArrayAdapter<String> hourAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, hours_milStr);
        hourAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinHour = (Spinner) findViewById(R.id.spinner_hour);
        spinHour.setAdapter(hourAdapter);
        selectedHour=Integer.valueOf(hours_milStr[0]);
    }

    private void genOtherArray() {

        int j=0;
        for(i=0;i<60;i=i+5,j++){
            minutes[j]=i;
            minutesStr[j]=String.valueOf(i);
        }
        minutesStr[0]="00";
        minutesStr[1]="05";

        ArrayAdapter<String> minAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, minutesStr);
        minAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinMin = (Spinner) findViewById(R.id.spinner_minute);
        spinMin.setAdapter(minAdapter);
        selectedMin=0;

        ArrayAdapter<String> durAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, durations);
        minAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinDur = (Spinner) findViewById(R.id.spinner_duration);
        spinDur.setAdapter(durAdapter);
        selectedDur=2;

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
                finish();
                return true;
            case R.id.main_menu_go_home:
                Intent intent = new Intent(user_uc1_availHalls.this, user_uc0_Home.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return false;
        }
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(user_uc1_availHalls.this, user_uc0_Home.class);
        startActivity(intent);
        finish();
    }
    private void setupButtons() {
        user_uc2_ReqEvent_btn = (Button)findViewById(R.id.button_user_uc2);
        user_uc2_ReqEvent_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v)  {
                Intent intent =  new Intent(user_uc1_availHalls.this, user_uc2_ReqEvent.class);
                if( selectedHall.length() > 0){
                    user_uc2_ReqEvent_btn.setBackgroundColor(Color.GREEN);
                    intent.putExtra("selectedYear",selectedYear); //all int's except selectedHall is a String
                    intent.putExtra("selectedMonth",selectedMonth);
                    intent.putExtra("selectedDay",selectedDay);
                    intent.putExtra("selectedHour",selectedHour);
                    intent.putExtra("selectedMin",selectedMin);
                    intent.putExtra("selectedDur",selectedDur);
                    intent.putExtra("selectedHall",selectedHall);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}

