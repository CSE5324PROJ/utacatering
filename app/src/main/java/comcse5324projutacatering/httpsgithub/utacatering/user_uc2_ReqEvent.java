package comcse5324projutacatering.httpsgithub.utacatering;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
//import android.database.Cursor;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
//import android.graphics.Color;
//import android.provider.BaseColumns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Spinner;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.view.View;
import android.widget.TextView;
import android.widget.EditText;
import java.text.DateFormatSymbols;
//import java.util.ArrayList;
//import java.util.List;
import java.util.Locale;


public class user_uc2_ReqEvent extends Activity {
//implements AdapterView.OnItemSelectedListener

    //String[] durations={"2 hr","3 hr","4 hr","5 hr","6 hr"};
    String[] venues={"American","Chinese","French","Greek","Indian","Italian","Japanese","Mexican","Pizza"};
    String selectedVenue=venues[0];
    //Integer[] durationsInt={2,3,4,5,6};
    //Integer selectedDuration=2;
    String[] mealTypes={"Breakfast","Lunch","Dinner"};
    String[] selectedMealType;
    Integer[] mealPrices={8,12,18}; //per attending
    Integer selectedMealPrice=8;
    String[] formality={"Informal","Formal"};
    Double[] formSetMulti={1.0,1.5};
    int[] formalityFlag={0,1};
    int selectedFormality;
    Double selectedFormMulti=1.0;
    String[] drinkTypes={"Non-Alcoholic","Alcoholic"};
    Integer[] drinkSetInt={0,1};
    Integer selectedDrinkInt=0;
    Integer capacity;
    Integer[] attendance;
    Integer selectedAtdnc=1;
    Double calcdPrice=0.0;

    private Button submitEvReq_btn;



    int selectedYear;
    int selectedMonth;
    int selectedDay;
    int selectedHour;
    int selectedMin;
    int selectedDuration;
    String selectedHall;

    String sqlFormattedDateTime; //YYYY-MM-DD HH:MM:SS.
    String username;

    public int customRed;
    public int customGreen;
    public int customBlue;

    String occasion;
    String ent_items;
    EditText editOccasion;
    EditText editEntItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_uc2_req_event);
        //Context mContext;
        //mContext = getApplicationContext();

        editOccasion = findViewById(R.id.editTextOccasion);
        editEntItems = findViewById(R.id.editTextEntItems);

        Intent mIntent;
        Bundle extras;
        mIntent = getIntent();
        extras=mIntent.getExtras();
        if(extras!=null){
            selectedYear=(int) extras.get("selectedYear");
            selectedMonth=(int) extras.get("selectedMonth");
            selectedDay=(int) extras.get("selectedDay");
            selectedHour=(int) extras.get("selectedHour");
            selectedMin=(int) extras.get("selectedMin");
            selectedDuration=(int) extras.get("selectedDur");
            selectedHall = (String) extras.get("selectedHall");
            username = (String) extras.get("username");
        }
        else{
            finish(); //activity not properly accessed
        }
        setImportedStrings();
        addListeners();
        setupButtons();
        customRed = getResources().getColor(R.color.customRed);
        customGreen = getResources().getColor(R.color.customGreen);
        customBlue = getResources().getColor(R.color.customBlue);
        submitEvReq_btn.setBackgroundColor(customBlue);


    }



    private void addListeners(){
        //Durations spinner
        Spinner spin1 = findViewById(R.id.spinner);
        ArrayAdapter<String> venuesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, venues);
        venuesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin1.setAdapter(venuesAdapter);
        spin1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                Spinner spinner = (Spinner) adapterView;
                if(spinner.getId() == R.id.spinner) {
                    selectedVenue=venues[position];

                }
            }
            @Override public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        //Meal-type spinner (breakfast, lunch etc)
        Spinner spin2 = findViewById(R.id.spinnerMtype);
        ArrayAdapter<String> mTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mealTypes);
        mTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin2.setAdapter(mTypeAdapter);
        spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                Spinner spinner = (Spinner) adapterView;
                if(spinner.getId() == R.id.spinnerMtype) {
                    selectedMealPrice=mealPrices[position];
                    selectedMealType=new String[1];
                    selectedMealType[0]=mealTypes[position];
                    recalcPrice();
                }
            }
            @Override public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        //Formality Spinner
        Spinner spin3 = findViewById(R.id.spinnerFormality);
        ArrayAdapter<String> formalAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, formality);
        formalAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin3.setAdapter(formalAdapter);
        spin3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                Spinner spinner = (Spinner) adapterView;
                if(spinner.getId() == R.id.spinnerFormality) {
                    selectedFormMulti=formSetMulti[position];
                    selectedFormality=formalityFlag[position];
                    recalcPrice();
                }
            }
            @Override public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        //Drinks Spinner
        Spinner spin4 = findViewById(R.id.spinnerDrink);
        ArrayAdapter<String> drinkAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, drinkTypes);
        drinkAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin4.setAdapter(drinkAdapter);
        spin4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                Spinner spinner = (Spinner) adapterView;
                if(spinner.getId() == R.id.spinnerDrink) {
                    selectedDrinkInt=drinkSetInt[position];
                    recalcPrice();
                }
            }
            @Override public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        //Hall & Capacity
        if (selectedHall.equals("Maverick")){
            capacity=100;
            attendance=new Integer[capacity];
            for (int i=1; i<=capacity; i++) {
                attendance[i-1] = i;
            }
        }
        else if (selectedHall.equals("KC")){
            capacity=25;
            attendance=new Integer[capacity];
            for (int i=1; i<=capacity; i++) {
                attendance[i-1] = i;
            }
        }
        else if (selectedHall.equals("Arlington")){
            capacity=50;
            attendance=new Integer[capacity];
            for (int i=1; i<=capacity; i++) {
                attendance[i-1] = i;
            }
        }
        else if (selectedHall.equals("Shard")){
            capacity=25;
            attendance=new Integer[capacity];
            for (int i=1; i<=capacity; i++) {
                attendance[i-1] = i;
            }
        }
        else if (selectedHall.equals("Liberty")){
            capacity=75;
            attendance=new Integer[capacity];
            for (int i=1; i<=capacity; i++) {
                attendance[i-1] = i;
            }
        }
        else{
            capacity=0;
            //expect error, this case should not happen.
        }
        Spinner spin5 = findViewById(R.id.spinnerAttn);
        ArrayAdapter<Integer> atdncAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, attendance);
        atdncAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin5.setAdapter(atdncAdapter);
        spin5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                Spinner spinner = (Spinner) adapterView;
                if(spinner.getId() == R.id.spinnerAttn) {
                    selectedAtdnc=attendance[position];
                    recalcPrice();
                }
            }
            @Override public void onNothingSelected(AdapterView<?> adapterView) {}
        });


    }

    private void recalcPrice(){
        calcdPrice=(1.0*2)*(1.0*capacity)*(1.0*selectedDuration);
        calcdPrice=calcdPrice+((1.0*selectedMealPrice)*(1.0*selectedAtdnc)*(selectedFormMulti));
        calcdPrice=calcdPrice+((1.0*15)*(1.0*selectedAtdnc)*(1.0*selectedDrinkInt)); //Wont change if non-alcoholic, since selectedDrinkInt will = 0.
        //((TextView)findViewById(R.id.editTextPrice)).setText(Double.toString(calcdPrice));
        String tempText="$"+String.format(Locale.US,"%.2f", calcdPrice)+" (before tax)";
        ((TextView)findViewById(R.id.editTextPrice)).setText(tempText);
    }


    public String getMonthFir3(int month) {
        String fullName = new DateFormatSymbols().getMonths()[month];
        return fullName.substring(0,3);
    }

    public void setImportedStrings(){
        EditText editText_Time = findViewById(R.id.editText);
        EditText editText_Date = findViewById(R.id.editText2);
        EditText editText_Hall = findViewById(R.id.editText3);
        EditText editText_Dur = findViewById(R.id.editText4);


        int friendlyHour;
        if(selectedHour==0){
            friendlyHour=12;
        }
        else if (selectedHour>12){
            friendlyHour=selectedHour-12;
        }
        else{
            friendlyHour=selectedHour;
        }

        String timeFriendly=String.format(Locale.US,"%02d", friendlyHour)+':'+String.format(Locale.US,"%02d", selectedMin);
        if(selectedHour>11){
            timeFriendly=timeFriendly+" PM";
        }
        else{
            timeFriendly=timeFriendly+" AM";
        }
        String dateFriendly=getMonthFir3(selectedMonth)+"-"+String.format(Locale.US,"%02d", selectedDay)+"-"+String.valueOf(selectedYear);

        editText_Time.setText(timeFriendly, TextView.BufferType.EDITABLE);
        editText_Date.setText(dateFriendly, TextView.BufferType.EDITABLE);
        sqlFormattedDateTime=String.valueOf(selectedYear)+'-'+String.format(Locale.US,"%02d", selectedMonth+1)+'-'+String.format(Locale.US,"%02d", selectedDay)
                +' '+String.format(Locale.US,"%02d", selectedHour)+':'+String.format(Locale.US,"%02d", selectedMin)+':'+"00";
        //+1 to month here to correct for Java Calendar using 0-11 for month ans sqlite using 1-12.
        editText_Hall.setText(selectedHall, TextView.BufferType.EDITABLE);
        String friendlyHours=String.valueOf(selectedDuration)+" hours";
        editText_Dur.setText(friendlyHours, TextView.BufferType.EDITABLE);
    }



    private void setupButtons() {
        submitEvReq_btn = findViewById(R.id.button_submitEvReq);
        submitEvReq_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v)  {
                Intent intent =  new Intent(user_uc2_ReqEvent.this, user_uc3_ViewReservedEventsCalendar .class);
                if( selectedHall.length() > 0){
                    submitEvReq_btn.setBackgroundColor(customGreen);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                            | Intent.FLAG_ACTIVITY_SINGLE_TOP

                    );
                    intent.putExtra("username",username);
                    startActivity(intent);
                    processSubmission();
                    finish();
                }
            }
        });
    }

    private void processSubmission(){
        //editOccasion      = findViewById(R.id.editTextOccasion);
        //editEntItems        = findViewById(R.id.editTextEntItems);
        occasion = editOccasion.getText().toString();
        ent_items = editEntItems .getText().toString();
        SQLiteDatabase db = DatabaseInterface.getInstance(user_uc2_ReqEvent.this).getWritableDatabase();
        //if req_user_id is null, it will search for the ID itself based on the username.
        DatabaseInterface.getInstance(user_uc2_ReqEvent.this).createBaseEvent(db, username,null,sqlFormattedDateTime,String.valueOf(selectedDuration),
                selectedHall, selectedVenue,null,0,selectedAtdnc,selectedDrinkInt,
                selectedFormality,selectedMealType[0],calcdPrice,occasion,ent_items);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(user_uc2_ReqEvent.this, user_uc0_Home.class);
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
                Intent intent0 = new Intent(user_uc2_ReqEvent.this, sysuser_uc2_Login.class);
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
                //------
                startActivity(intent0);
                finish();
                return true;
            case R.id.main_menu_go_home:
                Intent intent1 = new Intent(user_uc2_ReqEvent.this, user_uc0_Home.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP

                );
                startActivity(intent1);
                finish();
                return true;
            default:
                return false;
        }
    }

    public void hideKeyboard(View view){
        if(view!=null){
            InputMethodManager inMethMan = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if(inMethMan!=null){
                inMethMan.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }
}
