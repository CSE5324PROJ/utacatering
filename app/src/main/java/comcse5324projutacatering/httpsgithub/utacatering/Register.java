package comcse5324projutacatering.httpsgithub.utacatering;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends Activity {

    private EditText editUsername;
    private EditText editPassword;
    private EditText editRole;
    private EditText editStudentID;
    private EditText editContactDetails;
    private EditText editPersonalDetails;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        android.app.ActionBar actionBar = getActionBar();
        if(actionBar != null) {
            actionBar.setTitle("View Profile");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        editUsername        = (EditText) findViewById(R.id.edit_username);
        editPassword        = (EditText) findViewById(R.id.edit_password);
        editRole            = (EditText) findViewById(R.id.edit_role);
        editStudentID       = (EditText) findViewById(R.id.edit_student_id);
        editContactDetails  = (EditText) findViewById(R.id.edit_contact_details);
        editPersonalDetails = (EditText) findViewById(R.id.edit_personal_details);

        submitButton = (Button) findViewById(R.id.button_submit);

        addListeners();
    }

    private void addListeners() {
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = editUsername.getText().toString();
                String password = editPassword.getText().toString();
                String role     = editRole.getText().toString();
                int    stu_id   = Integer.parseInt(editStudentID.getText().toString());
                String contact  = editContactDetails.getText().toString();
                String personal = editPersonalDetails.getText().toString();

                DatabaseInterface.getInstance(Register.this).createRegistrationRequest(
                        username, password, role, stu_id, contact, personal
                );

                Toast.makeText(Register.this, "Registration submitted.", Toast.LENGTH_LONG).show();
                finish();
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
}
