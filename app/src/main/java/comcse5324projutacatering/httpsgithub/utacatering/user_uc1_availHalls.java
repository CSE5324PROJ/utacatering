package comcse5324projutacatering.httpsgithub.utacatering;

import android.app.Activity;
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
    Set<Integer> uniqueMonthValidSet = new LinkedHashSet<>();
    String uniqueMonthArray[];
    Set<Integer> uniqueDayValidSet = new LinkedHashSet<>();
    String uniqueDayArray[];

    int i=0;
    int hours_mil[]= new int[24];
    int minutes[]=new int[12];
    String hours_milStr[]= new String[24];;
    String minutesStr[]= new String[12];;
    String durations[]={"2 hr","3 hr","4 hr","5 hr","6 hr"};
    Integer durationsInt[]={2,3,4,5,6};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        while(cal.before(cal2)==true){
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



        genMonthsArray();
        genDaysArray();
        genOtherArray();
        addListeners();
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



    /*
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        Spinner spinner = (Spinner) arg0;
        if(spinner.getId() == R.id.spinner) {
            selectedDuration=durationsInt[position];
        }
        else if(spinner.getId() == R.id.spinnerMtype){
            selectedMealPrice=mealPrices[position];
        }
        else if(spinner.getId() == R.id.spinnerFormality){
            selectedFormMulti=formSetMulti[position];
        }
        else if(spinner.getId() == R.id.spinnerDrink){
            selectedDrinkInt=drinkSetInt[position];
        }
        else if(spinner.getId() == R.id.spinnerAttn){
            selectedAtdnc=attendance[position];
        }
        calcdPrice=(1.0*2)*(1.0*capacity)*(1.0*selectedDuration);
        calcdPrice=calcdPrice+((1.0*selectedMealPrice)*(1.0*selectedAtdnc)*(selectedFormMulti));
        calcdPrice=calcdPrice+((1.0*15)*(1.0*selectedAtdnc)*(1.0*selectedDrinkInt)); //Wont change if non-alcoholic, since selectedDrinkInt will = 0.
        //((TextView)findViewById(R.id.editTextPrice)).setText(Double.toString(calcdPrice));
        ((TextView)findViewById(R.id.editTextPrice)).setText("$"+String.format("%.2f", calcdPrice)+" (before tax)");
        //Toast.makeText(getApplicationContext(), durations[position], Toast.LENGTH_LONG).show();
    }

    */
}

