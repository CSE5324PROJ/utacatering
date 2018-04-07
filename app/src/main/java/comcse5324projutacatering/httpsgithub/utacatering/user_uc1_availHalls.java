package comcse5324projutacatering.httpsgithub.utacatering;
//TODO test case with a time range where there are no available halls at all.
//TODO translated spinner options to more user friendly options
//TODO limiting hours available? buffer time between events?
//TODO button to transition to reqEvent given current selections
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
    int hours_mil[]= new int[24];
    int minutes[]=new int[12];
    String hours_milStr[]= new String[24];;
    String minutesStr[]= new String[12];;
    String durations[]={"2 hr","3 hr","4 hr","5 hr","6 hr"};
    Integer durationsInt[]={2,3,4,5,6};
    public String EVENT_HALL_COL;
    private Context mContext;
    private boolean userIsInteracting;
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
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, uniqueYearArray);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinYear.setAdapter(yearAdapter);


        EVENT_HALL_COL = mContext.getString(R.string.EVENT_HALL_COL);
        genMonthsArray();
        genDaysArray();
        genOtherArray();
        searchAvailHalls(selectedYear,selectedMonth,selectedDay,selectedHour,selectedMin,selectedDur);
        addListeners();
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
                    //searchAvailHalls(selectedYear,selectedMonth,selectedDay,selectedHour,selectedMin,selectedDur);
                    if(userIsInteracting){
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
                        searchAvailHalls(selectedYear,selectedMonth,selectedDay,selectedHour,selectedMin,selectedDur);
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
        String uniqueMonthArrayTemp[]=uniqueMonthString.substring(1,uniqueMonthString.length()-1).split(", ");
        for(i=0;i<uniqueMonthValidSet.size();i++){
            uniqueMonthArray[i]=uniqueMonthArrayTemp[i];
        }
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, uniqueMonthArray);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinMonth = (Spinner) findViewById(R.id.spinner_month);
        spinMonth.setAdapter(monthAdapter);

    }

    private void genDaysArray() {
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
        ArrayAdapter<String> dayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, uniqueDayArray);
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinDay = (Spinner) findViewById(R.id.spinner_day);
        spinDay.setAdapter(dayAdapter);



    }

    private void genOtherArray() {
        for(i=0;i<24;i++){
            hours_mil[i]=i;
            hours_milStr[i]=String.valueOf(i);
        }
        int j=0;
        for(i=0;i<60;i=i+5,j++){
            minutes[j]=i;
            minutesStr[j]=String.valueOf(i);
        }
        minutesStr[0]="00";
        minutesStr[1]="05";

        ArrayAdapter<String> hourAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, hours_milStr);
        hourAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinHour = (Spinner) findViewById(R.id.spinner_hour);
        spinHour.setAdapter(hourAdapter);
        selectedHour=0;

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
}

