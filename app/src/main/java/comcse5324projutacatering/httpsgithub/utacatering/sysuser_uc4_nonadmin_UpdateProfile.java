package comcse5324projutacatering.httpsgithub.utacatering;
////TODO No transition to another page according to UCID after updating .... change to transition home?
//TODO 2x for bug cases
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Handler;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import static java.lang.reflect.Array.getInt;

public class sysuser_uc4_nonadmin_UpdateProfile extends Activity {

    private String active_username;
    private String active_id;
    private Context mContext;

    private String   workingProfileID;
    private Cursor profCursor;

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

    private String role;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sysuser_uc4_nonadmin_update_profile);
        mContext = getApplicationContext();
        


        android.app.ActionBar actionBar = getActionBar();
        if(actionBar != null) {
            actionBar.setTitle("Update Profile");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        
        final SharedPreferences sharedPref = mContext.getSharedPreferences(
                "MavCat.preferences", Context.MODE_PRIVATE
        );
        try{
            active_username = sharedPref.getString("active_username"," ");
            active_id = sharedPref.getString("active_id"," ");
            if(active_username.equals(" ") || active_id.equals(" ")){
                throw new Exception("No valid username/id in shared preferences", null);
            }
        }
        catch(Exception e) {
            if(e.getMessage().equals("No valid username/id in shared preferences")) {
                finish();
            }
        }

        String profile_id = active_id;
        profCursor = DatabaseInterface.getInstance(this).getProfileByID(profile_id);

        editUsername        = (EditText) findViewById(R.id.edit_username);
        editPassword        = (EditText) findViewById(R.id.edit_password);
        editRole            = (Spinner)  findViewById(R.id.edit_role);
        editRole.setEnabled(false); //only valid for admin
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
                        Toast.makeText(sysuser_uc4_nonadmin_UpdateProfile.this, "Cannot change to a blank username.", Toast.LENGTH_LONG).show();
                    }
                    else if(username.contains(" ")){
                        Toast.makeText(sysuser_uc4_nonadmin_UpdateProfile.this, "Username cannot contain spaces.", Toast.LENGTH_LONG).show();
                    }
                    else if(DatabaseInterface.getInstance(sysuser_uc4_nonadmin_UpdateProfile.this).searchUsernameRegistrationConflictCheck(username)){
                        //means duplicate is found
                        Toast.makeText(sysuser_uc4_nonadmin_UpdateProfile.this, "Edit profile failed, there is a user or pending user request with this username already.", Toast.LENGTH_LONG).show();
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
                if(DatabaseInterface.getInstance(sysuser_uc4_nonadmin_UpdateProfile.this).getProfileRole(workingProfileID).equals("Admin")){
                    Toast.makeText(sysuser_uc4_nonadmin_UpdateProfile.this, "Cannot remove an admin account.", Toast.LENGTH_LONG).show();
                }
                else{
                    DatabaseInterface.getInstance(sysuser_uc4_nonadmin_UpdateProfile.this).deleteProfile(workingProfileID);

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            Toast.makeText(sysuser_uc4_nonadmin_UpdateProfile.this, "System user removed.", Toast.LENGTH_LONG).show();
                            }
                    }, 250);

                    Intent intent0 = new Intent(sysuser_uc4_nonadmin_UpdateProfile.this, sysuser_uc2_Login.class);
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
                }

            }
        });
    }

    private void executeSaveChanges(String username, String  password, long stu_id, String  contact, String  personal){
        DatabaseInterface.getInstance(sysuser_uc4_nonadmin_UpdateProfile.this).updateProfile(
                workingProfileID, username, password, selectedRole, stu_id, contact, personal
        );
        Context mContext = getApplicationContext();
        final SharedPreferences sharedPref = mContext.getSharedPreferences(
                "MavCat.preferences", Context.MODE_PRIVATE
        );
        final SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove("active_username");
        editor.putString("active_username", username);
        editor.commit();
        active_username=username;
        Toast.makeText(sysuser_uc4_nonadmin_UpdateProfile.this, "Profile updated.", Toast.LENGTH_LONG).show();
        //No transition to another page according to UCID.
    }

    private void populateFields() {

        profCursor.moveToFirst();

        workingProfileID = profCursor.getString(profCursor.getColumnIndexOrThrow(BaseColumns._ID));

        String username = profCursor.getString(profCursor.getColumnIndexOrThrow(DatabaseInterface.COLUMN_NAME_USERNAME));
        String password = profCursor.getString(profCursor.getColumnIndexOrThrow(DatabaseInterface.COLUMN_NAME_PASSWORD));
        role     = profCursor.getString(profCursor.getColumnIndexOrThrow(DatabaseInterface.COLUMN_NAME_ROLE));
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
                Intent intent0 = new Intent(sysuser_uc4_nonadmin_UpdateProfile.this, sysuser_uc2_Login.class);
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
                Intent intent;
                switch (role) {
                    case "Admin":
                        intent = new Intent(sysuser_uc4_nonadmin_UpdateProfile.this, admin_uc0_Home.class);
                        break;
                    case "User":
                        intent = new Intent(sysuser_uc4_nonadmin_UpdateProfile.this, user_uc0_Home.class);
                        break;
                    case "Caterer":
                        intent = new Intent(sysuser_uc4_nonadmin_UpdateProfile.this, cat_uc0_Home.class);
                        break;
                    case "Caterer Staff":
                        intent = new Intent(sysuser_uc4_nonadmin_UpdateProfile.this, catstaff_uc0_Home.class);
                        break;
                    default:
                        intent = new Intent(sysuser_uc4_nonadmin_UpdateProfile.this, sysuser_uc2_Login .class);
                        Toast.makeText(sysuser_uc4_nonadmin_UpdateProfile.this,"Error with role",
                                Toast.LENGTH_SHORT).show();
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                                | Intent.FLAG_ACTIVITY_CLEAR_TASK
                        );
                        //Makes sure shared preference is reset
                        Context mContext0 = getApplicationContext();
                        final SharedPreferences sharedPref0 = mContext0.getSharedPreferences(
                                "MavCat.preferences", Context.MODE_PRIVATE
                        );
                        final SharedPreferences.Editor editor0 = sharedPref0.edit();
                        editor0.remove("active_username");
                        editor0.remove("active_id");
                        editor0.commit();
                        break;
                }
                intent.putExtra("username",active_username);
                startActivity(intent);
                finish();
                return true;
            default:
                return false;
        }
    }
    @Override
    public void onBackPressed() {
        Intent intent;
        switch (role) {
            case "Admin":
                intent = new Intent(sysuser_uc4_nonadmin_UpdateProfile.this, admin_uc0_Home.class);
                break;
            case "User":
                intent = new Intent(sysuser_uc4_nonadmin_UpdateProfile.this, user_uc0_Home.class);
                break;
            case "Caterer":
                intent = new Intent(sysuser_uc4_nonadmin_UpdateProfile.this, cat_uc0_Home.class);
                break;
            case "Caterer Staff":
                intent = new Intent(sysuser_uc4_nonadmin_UpdateProfile.this, catstaff_uc0_Home.class);
                break;
            default:
                intent = new Intent(sysuser_uc4_nonadmin_UpdateProfile.this, sysuser_uc2_Login .class);
                Toast.makeText(sysuser_uc4_nonadmin_UpdateProfile.this, "Error with role",
                        Toast.LENGTH_SHORT).show();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK
                );
                break;
        }
        intent.putExtra("username",active_username);
        startActivity(intent);
        finish();
    }
}
