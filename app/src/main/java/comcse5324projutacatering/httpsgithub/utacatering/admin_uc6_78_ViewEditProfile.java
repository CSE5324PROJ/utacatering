package comcse5324projutacatering.httpsgithub.utacatering;
//TODO implement top menu signout/home page  (implemented differently than on home page!)
//TODO Does Robb want a user's events cancelled if that user is deleted? what if the caterer is deleted? or staff removed from assignment?
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class admin_uc6_78_ViewEditProfile extends Activity {

    private String   workingProfileID;
    private Cursor   profCursor;

    private EditText editUsername;
    private String usernameInitial;
    private EditText editPassword;
    private Spinner  editRole;
    private EditText editStudentID;
    private EditText editContactDetails;
    private EditText editPersonalDetails;
    private Button   saveButton;
    private Button   deleteButton;

    private String selectedRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_uc6_78_view_edit_profile);
        String profile_id = getIntent().getStringExtra("profile_id");
        profCursor = DatabaseInterface.getInstance(this).getProfileByID(profile_id);

        android.app.ActionBar actionBar = getActionBar();
        if(actionBar != null) {
            actionBar.setTitle("View Profile");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        editUsername        = (EditText) findViewById(R.id.edit_username);
        editPassword        = (EditText) findViewById(R.id.edit_password);
        editRole            = (Spinner)  findViewById(R.id.edit_role);
        editStudentID       = (EditText) findViewById(R.id.edit_student_id);
        editContactDetails  = (EditText) findViewById(R.id.edit_contact_details);
        editPersonalDetails = (EditText) findViewById(R.id.edit_personal_details);

        saveButton   = (Button) findViewById(R.id.button_save_profile);
        deleteButton = (Button) findViewById(R.id.button_delete_profile);

        addListeners();
        populateFields();
        usernameInitial = editUsername.getText().toString();
    }

    private void addListeners() {
        editRole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedRole = String.valueOf(editRole.getSelectedItem());
            }
            @Override public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = editUsername.getText().toString();
                String password = editPassword.getText().toString();
                long    stu_id   = Long.parseLong(editStudentID.getText().toString());
                String contact  = editContactDetails.getText().toString();
                String personal = editPersonalDetails.getText().toString();

                if(usernameInitial.equals(username)){
                    executeSaveChanges(username, password, stu_id, contact, personal);
                }
                else{
                    //Verify the new username doesnt conflict
                    if(username.length()==0){
                        Toast.makeText(admin_uc6_78_ViewEditProfile.this, "Cannot change to a blank username.", Toast.LENGTH_LONG).show();
                    }
                    else if(username.contains(" ")){
                        Toast.makeText(admin_uc6_78_ViewEditProfile.this, "Username cannot contain spaces.", Toast.LENGTH_LONG).show();
                    }
                    else if(DatabaseInterface.getInstance(admin_uc6_78_ViewEditProfile.this).searchUsernameRegistrationConflictCheck(username)){
                        //means duplicate is found
                        Toast.makeText(admin_uc6_78_ViewEditProfile.this, "Edit profile failed, there is a user or pending user request with this username already.", Toast.LENGTH_LONG).show();
                    }
                    else{
                        executeSaveChanges(username, password, stu_id, contact, personal);
                    }
                }
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Check if non-admin before removing
                DatabaseInterface.getInstance(admin_uc6_78_ViewEditProfile.this).deleteProfile(workingProfileID);
                Toast.makeText(admin_uc6_78_ViewEditProfile.this, "System user removed.", Toast.LENGTH_LONG).show();

                Intent intent1 = new Intent(admin_uc6_78_ViewEditProfile.this, admin_uc5_SearchSystemUser .class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP

                );
                startActivity(intent1);
                finish();
            }
        });
    }

    private void executeSaveChanges(String username, String  password, long stu_id, String  contact, String  personal){
        DatabaseInterface.getInstance(admin_uc6_78_ViewEditProfile.this).updateProfile(
                workingProfileID, username, password, selectedRole, stu_id, contact, personal
        );

        Toast.makeText(admin_uc6_78_ViewEditProfile.this, "Profile updated.", Toast.LENGTH_LONG).show();
        //No transition to another page according to UCID.
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

        String[] roles = getResources().getStringArray(R.array.roles);
        for(int i = 0; i < roles.length; i++) {
            if ( role.equals(roles[i]) )
                editRole.setSelection(i);
        }
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
