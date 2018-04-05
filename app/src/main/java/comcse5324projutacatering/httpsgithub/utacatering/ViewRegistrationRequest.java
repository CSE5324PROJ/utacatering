package comcse5324projutacatering.httpsgithub.utacatering;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class ViewRegistrationRequest extends Activity {

    private Cursor   regCursor;
    private String   request_id;

    private TextView viewUsername;
    private TextView viewPassword;
    private TextView viewRole;
    private TextView viewStudentID;
    private TextView viewContactDetails;
    private TextView viewPersonalDetails;
    private Button   approveButton;
    private Button   rejectButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_registration_request);
        request_id = getIntent().getStringExtra("request_id");
        regCursor = DatabaseInterface.getInstance(this).getRegistrationRequest(request_id);

        android.app.ActionBar actionBar = getActionBar();
        if(actionBar != null) {
            actionBar.setTitle("View Registration Request");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        viewUsername        = (TextView) findViewById(R.id.view_username);
        viewPassword        = (TextView) findViewById(R.id.view_password);
        viewRole            = (TextView) findViewById(R.id.view_role);
        viewStudentID       = (TextView) findViewById(R.id.view_student_id);
        viewContactDetails  = (TextView) findViewById(R.id.view_contact_details);
        viewPersonalDetails = (TextView) findViewById(R.id.view_personal_details);

        approveButton = (Button) findViewById(R.id.button_approve_request);
        rejectButton  = (Button) findViewById(R.id.button_reject_request);

        addListeners();
        populateFields();
    }


    private void addListeners() {
        approveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseInterface.getInstance(ViewRegistrationRequest.this).convertRequestToProfile(request_id);
                Toast.makeText(ViewRegistrationRequest.this, "Request approved.", Toast.LENGTH_LONG).show();
                finish();
            }
        });
        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseInterface.getInstance(ViewRegistrationRequest.this).deleteRegistrationRequest(request_id);
                Toast.makeText(ViewRegistrationRequest.this, "Request rejected.", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    private void populateFields() {
        
        regCursor.moveToFirst();

        String username = regCursor.getString(regCursor.getColumnIndexOrThrow(DatabaseInterface.COLUMN_NAME_USERNAME));
        String password = regCursor.getString(regCursor.getColumnIndexOrThrow(DatabaseInterface.COLUMN_NAME_PASSWORD));
        String role     = regCursor.getString(regCursor.getColumnIndexOrThrow(DatabaseInterface.COLUMN_NAME_ROLE));
        String uta_id   = regCursor.getString(regCursor.getColumnIndexOrThrow(DatabaseInterface.COLUMN_NAME_UTAID));
        String contact  = regCursor.getString(regCursor.getColumnIndexOrThrow(DatabaseInterface.COLUMN_NAME_CONTACT));
        String personal = regCursor.getString(regCursor.getColumnIndexOrThrow(DatabaseInterface.COLUMN_NAME_PERSONAL));
        
        // TODO: Get from database to fill in fields.
        viewUsername.setText(username);
        viewPassword.setText(password);
        viewRole.setText(role);
        viewStudentID.setText(uta_id);
        viewContactDetails.setText(contact);
        viewPersonalDetails.setText(personal);
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