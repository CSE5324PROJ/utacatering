package comcse5324projutacatering.httpsgithub.utacatering;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class sysuser_uc1_Register extends Activity {

    private EditText editUsername;
    private EditText editPassword;
    private Spinner  editRole;
    private EditText editStudentID;
    private EditText editContactDetails;
    private EditText editPersonalDetails;
    private Button   submitButton;

    private String selectedRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sysuser_uc1_register);

        android.app.ActionBar actionBar = getActionBar();
        if(actionBar != null) {
            actionBar.setTitle("MavCat Register");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        editUsername        = (EditText) findViewById(R.id.edit_username);
        editPassword        = (EditText) findViewById(R.id.edit_password);
        editRole            = (Spinner)  findViewById(R.id.edit_role);
        editStudentID       = (EditText) findViewById(R.id.edit_student_id);
        editContactDetails  = (EditText) findViewById(R.id.edit_contact_details);
        editPersonalDetails = (EditText) findViewById(R.id.edit_personal_details);

        submitButton = (Button) findViewById(R.id.button_submit);

        addListeners();
        editRole.setSelection(0);
    }

    private void addListeners() {
        editRole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedRole = String.valueOf(editRole.getSelectedItem());
            }
            @Override public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean stopRegisterFlag = false;
                EditText[] checkTexts = { editUsername, editPassword, editStudentID, editContactDetails, editPersonalDetails };
                for(EditText et : checkTexts) {
                    if (et.getText().toString().isEmpty()) {
                        et.setError("Please supply a value.");
                        stopRegisterFlag = true;
                    }
                }
                if(stopRegisterFlag) return;

                String username = editUsername.getText().toString();
                String password = editPassword.getText().toString();
                long    stu_id   = Long.parseLong(editStudentID.getText().toString());
                String contact  = editContactDetails.getText().toString();
                String personal = editPersonalDetails.getText().toString();

                if(username.length()==0){
                    Toast.makeText(sysuser_uc1_Register.this, "Registration failed, username field is empty.", Toast.LENGTH_LONG).show();
                }
                else if(username.contains(" ")){
                    Toast.makeText(sysuser_uc1_Register.this, "Registration failed, Username cannot contain spaces.", Toast.LENGTH_LONG).show();
                }
                else if(DatabaseInterface.getInstance(sysuser_uc1_Register.this).searchUsernameRegistrationConflictCheck(username)){
                    //means duplicate is found
                    Toast.makeText(sysuser_uc1_Register.this, "Registration failed, there is a user or pending user request with this username already.", Toast.LENGTH_LONG).show();
                }
                else{
                    DatabaseInterface.getInstance(sysuser_uc1_Register.this).createRegistrationRequest(
                            username, password, selectedRole, stu_id, contact, personal
                    );

                    Toast.makeText(sysuser_uc1_Register.this, "Registration submitted.", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
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
