package comcse5324projutacatering.httpsgithub.utacatering;
//TODO take data and insert into table
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.widget.Button;
import android.widget.Spinner;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.view.View;
import android.widget.TextView;
import android.widget.EditText;
import java.text.DateFormatSymbols;
import java.util.Locale;


public class user_uc2_ReqEvent extends Activity {
//implements AdapterView.OnItemSelectedListener

    //String[] durations={"2 hr","3 hr","4 hr","5 hr","6 hr"};
    String[] venues={"American", "Chinese", "French", "Greek", "Indian", "Italian", "Japanese", "Mexican", "Pizza"};
    String selectedVenue=venues[0];
    //Integer[] durationsInt={2,3,4,5,6};
    //Integer selectedDuration=2;
    String[] mealTypes={"Breakfast","Lunch","Dinner"};
    Integer[] mealPrices={8,12,18}; //per attending
    Integer selectedMealPrice=8;
    String[] formality={"Informal","Formal"};
    Double[] formSetMulti={1.0,1.5};
    Double selectedFormMulti=1.0;
    String[] drinkTypes={"Non-Alcoholic","Alcoholic"};
    Integer[] drinkSetInt={0,1};
    Integer selectedDrinkInt=0;
    Integer capacity;
    Integer[] attendance;
    Integer selectedAtdnc=1;
    Double calcdPrice=0.0;
    private Context mContext;
    private Button submitEvReq_btn;

    private Intent mIntent;
    private Bundle extras;
    int selectedYear;
    int selectedMonth;
    int selectedDay;
    int selectedHour;
    int selectedMin;
    int selectedDuration;
    String selectedHall;

    String sqlFormattedDateTime; //YYYY-MM-DD HH:MM:SS.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
        setContentView(R.layout.activity_user_uc2_req_event);
        mIntent = getIntent();
        extras=mIntent.getExtras();
        selectedYear=(int) extras.get("selectedYear");
        selectedMonth=(int) extras.get("selectedMonth");
        selectedDay=(int) extras.get("selectedDay");
        selectedHour=(int) extras.get("selectedHour");
        selectedMin=(int) extras.get("selectedMin");
        selectedDuration=(int) extras.get("selectedDur");
        selectedHall = (String) extras.get("selectedHall");

        setImportedStrings();

        //Spinner & Listener Sets
        //Venue Spinner
        //Spinner spin1 = (Spinner) findViewById(R.id.spinner);
        //spin1.setOnItemSelectedListener(this);

        //Meal Type Spinner
        //Spinner spin2 = (Spinner) findViewById(R.id.spinnermTypes);
        //spin2.setOnItemSelectedListener(this);
        //Formality Spinner
        //Spinner spin3 = (Spinner) findViewById(R.id.spinnerFormality);
        //spin3.setOnItemSelectedListener(this);
        //Drink Spinner
        //Spinner spin4 = (Spinner) findViewById(R.id.spinnerDrink);
        //spin4.setOnItemSelectedListener(this);
        //Attendance Spinner
        //Spinner spin5 = (Spinner) findViewById(R.id.spinnerAttn);
        //spin5.setOnItemSelectedListener(this);

        //Creating Adapters
        //Duration Adapter
        //ArrayAdapter<String> durationsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, venues);
        //durationsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spin1.setAdapter(durationsAdapter);
        //Meal Type Spinner
        //ArrayAdapter<String> mTypeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mealTypes);
        //mTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spin2.setAdapter(mTypeAdapter);
        //Formality Spinner
        //ArrayAdapter<String> formalAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, formality);
        //formalAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spin3.setAdapter(formalAdapter);
        //Drink Spinner
        //ArrayAdapter<String> drinkAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, drinkTypes);
        //drinkAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spin4.setAdapter(drinkAdapter);
        //Attendance Spinner w/ Capacity calculation

        //ArrayAdapter<Integer> atdncAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, attendance);
        //atdncAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spin5.setAdapter(atdncAdapter);

        addListeners();
        setupButtons();
        submitEvReq_btn.setBackgroundColor(Color.BLUE);
    }



    private void addListeners(){
        //Durations spinner
        Spinner spin1 = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> venuesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, venues);
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
        Spinner spin2 = (Spinner) findViewById(R.id.spinnerMtype);
        ArrayAdapter<String> mTypeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mealTypes);
        mTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin2.setAdapter(mTypeAdapter);
        spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                Spinner spinner = (Spinner) adapterView;
                if(spinner.getId() == R.id.spinnerMtype) {
                    selectedMealPrice=mealPrices[position];
                    recalcPrice();
                }
            }
            @Override public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        //Formality Spinner
        Spinner spin3 = (Spinner) findViewById(R.id.spinnerFormality);
        ArrayAdapter<String> formalAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, formality);
        formalAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin3.setAdapter(formalAdapter);
        spin3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                Spinner spinner = (Spinner) adapterView;
                if(spinner.getId() == R.id.spinnerFormality) {
                    selectedFormMulti=formSetMulti[position];
                    recalcPrice();
                }
            }
            @Override public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        //Drinks Spinner
        Spinner spin4 = (Spinner) findViewById(R.id.spinnerDrink);
        ArrayAdapter<String> drinkAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, drinkTypes);
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
        //final EditText hall =  (EditText) findViewById(R.id.editText3);
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
        Spinner spin5 = (Spinner) findViewById(R.id.spinnerAttn);
        ArrayAdapter<Integer> atdncAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, attendance);
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
        ((TextView)findViewById(R.id.editTextPrice)).setText("$"+String.format("%.2f", calcdPrice)+" (before tax)");
    }


    public String getMonthFir3(int month) {
        String fullName = new DateFormatSymbols().getMonths()[month];
        return fullName.substring(0,3);
    }

    public void setImportedStrings(){
        EditText editText_Time = (EditText)findViewById(R.id.editText);
        EditText editText_Date = (EditText)findViewById(R.id.editText2);
        EditText editText_Hall = (EditText)findViewById(R.id.editText3);
        EditText editText_Dur = (EditText)findViewById(R.id.editText4);

        //String selectedHall = mIntent.getStringExtra("selectedHall");

        //check if extras is not null
        //if(extras!=null)


        int friendlyHour=0;
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
        sqlFormattedDateTime=String.valueOf(selectedYear)+'-'+String.format(Locale.US,"%02d", selectedMonth)+'-'+String.format(Locale.US,"%02d", selectedDay)
                +' '+String.format(Locale.US,"%02d", selectedHour)+':'+String.format(Locale.US,"%02d", selectedMin)+':'+"00";

        editText_Hall.setText(selectedHall, TextView.BufferType.EDITABLE);
        String friendlyHours=String.valueOf(selectedDuration)+" hours";
        editText_Dur.setText(friendlyHours, TextView.BufferType.EDITABLE);
    }



    private void setupButtons() {
        submitEvReq_btn = (Button)findViewById(R.id.button_submitEvReq);
        submitEvReq_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v)  {
                Intent intent =  new Intent(user_uc2_ReqEvent.this, user_uc0_Home.class);
                if( selectedHall.length() > 0){
                    submitEvReq_btn.setBackgroundColor(Color.GREEN);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
































    /*

    public void setOnItemSelectedListener(AdapterView<?> arg0, View arg1, int position,long id) {
        Spinner spinner = (Spinner) arg0;
        if(spinner.getId() == R.id.spinner) {
            selectedDuration=durationsInt[position];
        }
        else if(spinner.getId() == R.id.spinnerMtype){

        }
        else if(spinner.getId() == R.id.spinnerFormality){

        }
        else if(spinner.getId() == R.id.spinnerDrink){

        }
        else if(spinner.getId() == R.id.spinnerAttn){

        }
        calcdPrice=(1.0*2)*(1.0*capacity)*(1.0*selectedDuration);
        calcdPrice=calcdPrice+((1.0*selectedMealPrice)*(1.0*selectedAtdnc)*(selectedFormMulti));
        calcdPrice=calcdPrice+((1.0*15)*(1.0*selectedAtdnc)*(1.0*selectedDrinkInt)); //Wont change if non-alcoholic, since selectedDrinkInt will = 0.
        //((TextView)findViewById(R.id.editTextPrice)).setText(Double.toString(calcdPrice));
        ((TextView)findViewById(R.id.editTextPrice)).setText("$"+String.format("%.2f", calcdPrice)+" (before tax)");
        //Toast.makeText(getApplicationContext(), durations[position], Toast.LENGTH_LONG).show();
    }

    public void onNothingSelected(AdapterView<?> arg0) {
    // nothing

    }
    //View layout=(View)findViewById(R.id.reqLayout).requestFocusFromTouch();
    */
}
