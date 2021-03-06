package comcse5324projutacatering.httpsgithub.utacatering;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
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
    String thisEventID;
    String selectedStaff;
    List<String> unassignedStaffList = new ArrayList<>();
    List<String> assignedStaffList = new ArrayList<>();
    String unassignedStaff[];
    String assignedStaff[];
    String unassignedSpinText = "Available staff";
    String assignedSpinText = "Assigned staff";

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
            thisEventID = (String)extras.get("eventID");
        }

        genAssignedArray();
        genUnassignedArray();

        addListeners();
        setupButtons();

        final ActionBar actionbar = getActionBar();
        if(actionbar!=null){
            actionbar.setDisplayHomeAsUpEnabled(true);
            //actionbar.setTitle("Assign staff to event ID: "+thisEventID); // For debugging
            actionbar.setTitle("Assign caterer staff to event");
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
                back_btn.setBackgroundColor(customGreen);

                finish();
            }
        });
    }

    private void addListeners() {

        spinUnassigned.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override public void onItemSelected(AdapterView<?> adapterView, View view, final int position, long id) {
                Spinner spinner = (Spinner) adapterView;

                if(spinner.getId() == R.id.spinner_unassigned_cs) {
                    if(userIsInteracting){
                        spinUnassigned.setEnabled(false);
                        spinAssigned.setEnabled(false);
                        selectedStaff = unassignedStaff[position];
                        if (!selectedStaff.equals(unassignedSpinText)) {
                            DatabaseInterface.getInstance(cat_uc7_AssignCatererStaff.this).assignCatStaff(thisEventID, selectedStaff);
                            Toast.makeText(cat_uc7_AssignCatererStaff.this, "Added " + selectedStaff, Toast.LENGTH_SHORT).show();

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    genAssignedArray();
                                    genUnassignedArray();
                                    userIsInteracting = false;
                                    spinUnassigned.setEnabled(true);
                                    spinAssigned.setEnabled(true);

                                }
                            }, 1500);
                        }
                        else {
                            userIsInteracting = false;
                            spinUnassigned.setEnabled(true);
                            spinAssigned.setEnabled(true);
                        }
                    }

                }
            }
            @Override public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        spinAssigned.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                Spinner spinner = (Spinner) adapterView;

                if(spinner.getId() == R.id.spinner_assigned_cs) {
                    if(userIsInteracting){
                        spinUnassigned.setEnabled(false);
                        spinAssigned.setEnabled(false);
                        selectedStaff = assignedStaff[position];
                        if (!selectedStaff.equals(assignedSpinText)) {
                            DatabaseInterface.getInstance(cat_uc7_AssignCatererStaff.this).removeCatStaff(thisEventID, selectedStaff);
                            Toast.makeText(cat_uc7_AssignCatererStaff.this, "Removed " + selectedStaff, Toast.LENGTH_SHORT).show();

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    genAssignedArray();
                                    genUnassignedArray();
                                    userIsInteracting = false;
                                    spinUnassigned.setEnabled(true);
                                    spinAssigned.setEnabled(true);

                                }
                            }, 1500);
                        }
                        else {
                            userIsInteracting = false;
                            spinUnassigned.setEnabled(true);
                            spinAssigned.setEnabled(true);
                        }
                    }

                }
            }
            @Override public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }

    private void genUnassignedArray() {
        unassignedStaffList = DatabaseInterface.getInstance(cat_uc7_AssignCatererStaff.this).getFreeCS(thisEventID);
        unassignedStaffList.add(0,unassignedSpinText);

        unassignedStaff = new String[unassignedStaffList.size()];
        for (int i = 0; i<unassignedStaff.length; i++){
            unassignedStaff[i] = unassignedStaffList.get(i);
        }

        unassignedAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, unassignedStaff); //Used for indexing
        unassignedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinUnassigned = findViewById(R.id.spinner_unassigned_cs);
        spinUnassigned.setAdapter(unassignedAdapter);
    }

    private void genAssignedArray() {
        assignedStaffList = DatabaseInterface.getInstance(cat_uc7_AssignCatererStaff.this).getCSofEvent(thisEventID);
        assignedStaffList.add(0,assignedSpinText);

        assignedStaff = new String[assignedStaffList.size()];
        for (int i = 0; i<assignedStaff.length; i++){
            assignedStaff[i] = assignedStaffList.get(i);
        }

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
