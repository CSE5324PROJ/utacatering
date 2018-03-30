package comcse5324projutacatering.httpsgithub.utacatering;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ViewEditProfile extends Activity {

    private String   workingProfileID;
    private Cursor   profCursor;

    private EditText editUsername;
    private EditText editPassword;
    private EditText editRole;
    private EditText editStudentID;
    private EditText editContactDetails;
    private EditText editPersonalDetails;
    private Button   saveButton;
    private Button   deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_edit_profile);
        String profile_id = getIntent().getStringExtra("profile_id");
        profCursor = DatabaseInterface.getInstance(this).getProfileByID(profile_id);

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

        saveButton = (Button) findViewById(R.id.button_save_profile);
        deleteButton = (Button) findViewById(R.id.button_delete_profile);

        addListeners();
        populateFields();
    }

    private void addListeners() {
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = editUsername.getText().toString();
                String password = editPassword.getText().toString();
                String role     = editRole.getText().toString();
                int    stu_id   = Integer.parseInt(editStudentID.getText().toString());
                String contact  = editContactDetails.getText().toString();
                String personal = editPersonalDetails.getText().toString();

                DatabaseInterface.getInstance(ViewEditProfile.this).updateProfile(
                        workingProfileID, username, password, role, stu_id, contact, personal
                );

                Toast.makeText(ViewEditProfile.this, "Profile updated.", Toast.LENGTH_LONG).show();
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Check if non-admin before removing
                DatabaseInterface.getInstance(ViewEditProfile.this).deleteProfile(workingProfileID);
                Toast.makeText(ViewEditProfile.this, "System user removed.", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    private void populateFields() {

        profCursor.moveToFirst();

        workingProfileID = profCursor.getString(profCursor.getColumnIndexOrThrow(BaseColumns._ID));

        String username = profCursor.getString(profCursor.getColumnIndexOrThrow(DatabaseInterface.COLUMN_NAME_USERNAME));
        String password = profCursor.getString(profCursor.getColumnIndexOrThrow(DatabaseInterface.COLUMN_NAME_PASSWORD));
        String role     = profCursor.getString(profCursor.getColumnIndexOrThrow(DatabaseInterface.COLUMN_NAME_ROLE));
        String uta_id   = profCursor.getString(profCursor.getColumnIndexOrThrow(DatabaseInterface.COLUMN_NAME_UTAID));
        String contact  = profCursor.getString(profCursor.getColumnIndexOrThrow(DatabaseInterface.COLUMN_NAME_CONTACT));
        String personal = profCursor.getString(profCursor.getColumnIndexOrThrow(DatabaseInterface.COLUMN_NAME_PERSONAL));

        editUsername.setText(username);
        editPassword.setText(password);
        editRole.setText(role);
        editStudentID.setText(uta_id);
        editContactDetails.setText(contact);
        editPersonalDetails.setText(personal);
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
