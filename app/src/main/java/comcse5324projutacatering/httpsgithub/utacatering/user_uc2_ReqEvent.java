package comcse5324projutacatering.httpsgithub.utacatering;

import android.widget.Spinner;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.view.View;
import android.widget.TextView;
import android.widget.EditText;

public class user_uc2_ReqEvent extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    String[] durations={"2 hr","3 hr","4 hr","5 hr","6 hr"};
    Integer[] durationsInt={2,3,4,5,6};
    Integer selectedDuration=2;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_uc2_req_event);
        //Spinner & Listener Sets
        //Duration Spinner
        Spinner spin1 = (Spinner) findViewById(R.id.spinner);
        spin1.setOnItemSelectedListener(this);
        //Meal Type Spinner
        Spinner spin2 = (Spinner) findViewById(R.id.spinnerMtype);
        spin2.setOnItemSelectedListener(this);
        //Formality Spinner
        Spinner spin3 = (Spinner) findViewById(R.id.spinnerFormality);
        spin3.setOnItemSelectedListener(this);
        //Drink Spinner
        Spinner spin4 = (Spinner) findViewById(R.id.spinnerDrink);
        spin4.setOnItemSelectedListener(this);
        //Attendance Spinner
        Spinner spin5 = (Spinner) findViewById(R.id.spinnerAttn);
        spin5.setOnItemSelectedListener(this);

        //Creating Adapters
        //Duration Adapter
        ArrayAdapter<String> durationsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, durations);
        durationsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin1.setAdapter(durationsAdapter);
        //Meal Type Spinner
        ArrayAdapter<String> mtypAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mealTypes);
        mtypAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin2.setAdapter(mtypAdapter);
        //Formality Spinner
        ArrayAdapter<String> formalAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, formality);
        formalAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin3.setAdapter(formalAdapter);
        //Drink Spinner
        ArrayAdapter<String> drinkAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, drinkTypes);
        drinkAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin4.setAdapter(drinkAdapter);
        //Attendance Spinner w/ Capacity calculation
        final EditText hall =  (EditText) findViewById(R.id.editText3);
        if (hall.getText().toString().equals("Arlington")){
            capacity=50;
            attendance=new Integer[capacity];
            for (int i=1; i<=capacity; i++) {
                attendance[i-1] = i;
            }
        }

        ArrayAdapter<Integer> atdncAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, attendance);
        atdncAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin5.setAdapter(atdncAdapter);

    }

    //Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position,long id) {
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
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
    // nothing

    }
    //View layout=(View)findViewById(R.id.reqLayout).requestFocusFromTouch();

}
