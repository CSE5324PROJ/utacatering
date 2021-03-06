package comcse5324projutacatering.httpsgithub.utacatering;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class admin_uc2_34_ViewRegistrationRequest extends Activity {

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

    private DatabaseInterface db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = DatabaseInterface.getInstance(this);
        setContentView(R.layout.activity_admin_uc2_34_view_registration_request);
        request_id = getIntent().getStringExtra("request_id");
        regCursor = db.getRegistrationRequest(request_id);

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
                db.convertRequestToProfile(request_id);
                Toast.makeText(admin_uc2_34_ViewRegistrationRequest.this, "Request approved.", Toast.LENGTH_LONG).show();
                finish();
            }
        });
        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.deleteRegistrationRequest(request_id);
                Toast.makeText(admin_uc2_34_ViewRegistrationRequest.this, "Request rejected.", Toast.LENGTH_LONG).show();
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
        

        viewUsername.setText(username);
        viewPassword.setText(password);
        viewRole.setText(role);
        viewStudentID.setText(uta_id);
        viewContactDetails.setText(contact);
        viewPersonalDetails.setText(personal);
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
            case android.R.id.home:
                finish();
                return true;
            case R.id.main_menu_go_home:
                Intent intent = new Intent(this, admin_uc0_Home.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK
                );
                startActivity(intent);
                finish();
                return true;
            case R.id.main_menu_sign_out:
                startActivity(db.logout(this));
                finish();
                return true;
            default:
                return false;
        }
    }
}
