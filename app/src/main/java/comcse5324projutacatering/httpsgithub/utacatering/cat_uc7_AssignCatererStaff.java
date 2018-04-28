package comcse5324projutacatering.httpsgithub.utacatering;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class cat_uc7_AssignCatererStaff extends Activity {
    String username;
    private Button back_btn;
    private Spinner spinUnassigned;
    private Spinner spinAssigned;
    ArrayAdapter<String> unassignedAdapter;
    ArrayAdapter<String> assignedAdapter;
    int thisEventID;
    String selectedStaff;
    String allCatStaff[];
    //ArrayList<String> unassignedStaffList = new ArrayList<>();
    //ArrayList<String> assignedStaffList = new ArrayList<>();
    String unassignedStaff[];
    String assignedStaff[];

    private boolean userIsInteracting;
    private DatabaseInterface db;

    public int customRed;
    public int customGreen;
    public int customBlue;
    public int customGrey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = DatabaseInterface.getInstance(this);
        setContentView(R.layout.activity_cat_uc7_assign_caterer_staff);
        Context mContext;
        mContext = getApplicationContext();

        customRed = getResources().getColor(R.color.customRed);
        customGreen = getResources().getColor(R.color.customGreen);
        customBlue = getResources().getColor(R.color.customBlue);

        Intent mIntent;
        Bundle extras;
        mIntent = getIntent();
        extras=mIntent.getExtras();

        spinUnassigned = findViewById(R.id.spinner_unassigned_cs);
        spinAssigned = findViewById(R.id.spinner_assigned_cs);

        if(extras!=null){
            thisEventID = Integer.parseInt(extras.getString("eventID"));
        }

        genAllCatStaff();
        genAssignedArray();
        genUnassignedArray();

        addListeners();
        setupButtons();

        final ActionBar actionbar = getActionBar();
        if(actionbar!=null){
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setTitle("Assign staff to event ID: "+thisEventID);
        }
    }




    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        userIsInteracting = true;
    }

    private void setupButtons() {
        back_btn= findViewById(R.id.button_goback);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v)  {
                //Intent intent =  new Intent(cat_uc6_78_ViewEventDetails.this, cat_uc5_ViewEventCal.class);
                back_btn.setBackgroundColor(customGreen);

                /*intent.putExtra("username",username);
                startActivity(intent);*/
                finish();
            }
        });
    }

    private void addListeners() {

        spinUnassigned.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                //selectedRole = String.valueOf(editRole.getSelectedItem());
                Spinner spinner = (Spinner) adapterView;

                if(spinner.getId() == R.id.spinner_unassigned_cs) {
                    if(userIsInteracting){
                        selectedStaff = unassignedStaff[position];
                        Toast.makeText(cat_uc7_AssignCatererStaff.this, "Added "+selectedStaff, Toast.LENGTH_SHORT).show();
                        spinUnassigned.setSelection(0);
                        DatabaseInterface.getInstance(cat_uc7_AssignCatererStaff.this).assignCatStaff(thisEventID, selectedStaff);
                        genUnassignedArray();
                        userIsInteracting=false;
                    }

                }
            }
            @Override public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        spinAssigned.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                //selectedRole = String.valueOf(editRole.getSelectedItem());
                Spinner spinner = (Spinner) adapterView;

                if(spinner.getId() == R.id.spinner_assigned_cs) {
                    if(userIsInteracting){
                        selectedStaff = assignedStaff[position];
                        Toast.makeText(cat_uc7_AssignCatererStaff.this, "Removed "+selectedStaff, Toast.LENGTH_SHORT).show();
                        spinAssigned.setSelection(0);
                        DatabaseInterface.getInstance(cat_uc7_AssignCatererStaff.this).removeCatStaff(thisEventID, selectedStaff);
                        genAssignedArray();
                        userIsInteracting=false;
                    }

                }
            }
            @Override public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }

    private void genAllCatStaff() {
        allCatStaff = DatabaseInterface.getInstance(cat_uc7_AssignCatererStaff.this).getAllCatStaff();
    }

    private void genUnassignedArray() {
        List<String> staff = new ArrayList<>();
        String user, test_user;
        boolean user_assigned = false;
        staff.add("");
        for (int i = 0; i < allCatStaff.length; i++){
            user = allCatStaff[i];
            user_assigned = false;
            for (int j = 0; j < assignedStaff.length; j++){
                test_user = assignedStaff[j];
                if (user.equals(test_user)) {
                    user_assigned = true;
                    break;
                }
            }
            if (user_assigned == false)
               staff.add(user);
        }

        unassignedStaff = new String[staff.size()];
        for (int i = 0; i<unassignedStaff.length; i++){
            unassignedStaff[i] = staff.get(i);
        }

        unassignedAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, unassignedStaff); //Used for indexing
        unassignedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinUnassigned = findViewById(R.id.spinner_unassigned_cs);
        spinUnassigned.setAdapter(unassignedAdapter);
    }

    private void genAssignedArray() {
        assignedStaff = DatabaseInterface.getInstance(cat_uc7_AssignCatererStaff.this).getAssignedCatStaff(thisEventID);

        /*
        assignedStaff = new String[3];
        assignedStaff[0] = " ";
        assignedStaff[1] = "1";
        assignedStaff[2] = "2";
        */
        assignedAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, assignedStaff); //Used for indexing
        assignedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinAssigned = findViewById(R.id.spinner_assigned_cs);
        spinAssigned.setAdapter(assignedAdapter);
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
                Intent intent0 = new Intent(cat_uc7_AssignCatererStaff.this, sysuser_uc2_Login.class);
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
                Intent intent = new Intent(cat_uc7_AssignCatererStaff.this, cat_uc0_Home.class);
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

    public void hideKeyboard(View view){
        if(view!=null){
            InputMethodManager inMethMan = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if(inMethMan!=null){
                inMethMan.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }
}
