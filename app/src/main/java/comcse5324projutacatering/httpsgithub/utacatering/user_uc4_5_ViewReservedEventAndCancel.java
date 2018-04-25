package comcse5324projutacatering.httpsgithub.utacatering;
//TODO confirmation popup??
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class user_uc4_5_ViewReservedEventAndCancel extends Activity {
    String username;
    Button cancel_btn;
    Button back_btn;

    public int customRed;
    public int customGreen;
    public int customBlue;
    public int customGrey;
    AlertDialog cancel_alert;
    String[] event_data_string_array;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_uc4_5_view_reserved_event_and_cancel);
        customRed = getResources().getColor(R.color.customRed);
        customGreen = getResources().getColor(R.color.customGreen);
        customBlue = getResources().getColor(R.color.customBlue);
        customGrey = getResources().getColor(R.color.customGrey);
        final ActionBar actionbar = getActionBar();
        if(actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setTitle("MavCat - View and cancel reserved event");
        }
        Intent mIntent;
        Bundle extras;
        mIntent = getIntent();
        extras=mIntent.getExtras();

        EditText editTextTimeApproval = (EditText)findViewById(R.id.editTextTimeApproval);
        //EditText editTextTime = (EditText)findViewById(R.id.editTextTime);
        EditText editTextDate = (EditText)findViewById(R.id.editTextDate);
        EditText editTextHall = (EditText)findViewById(R.id.editTextHall);
        EditText editTextDuration = (EditText)findViewById(R.id.editTextDuration);
        EditText editTextVenue = (EditText)findViewById(R.id.editTextVenue);
        EditText editTextMealType = (EditText)findViewById(R.id.editTextMealType);
        EditText editTextFormality = (EditText)findViewById(R.id.editTextFormality);
        EditText editTextDrinks = (EditText)findViewById(R.id.editTextDrinks);
        EditText editTextAtt = (EditText)findViewById(R.id.editTextAtt);
        EditText editTextPrice = (EditText)findViewById(R.id.editTextPrice);
        EditText editTextOccasion = (EditText)findViewById(R.id.editTextOccasion);
        EditText editTextEntItems = (EditText)findViewById(R.id.editTextEntItems);



        if(extras!=null){
            username = (String) extras.get("username");
            /* event_data_string_array
             * Array position translation:
             * 0)Title String
             * 1)Subtitle String (duration & price)
             * 2)req_user_id
             * 3)approval_flag
             * 4)User friendly date string
             * 5)User friendly hours string
             * 6)hall
             * 7)Attendance
             * 8)price
             * 9)meal type
             * 10)venue
             * 11)alco_flag
             * 12)formal_flag
             * 13)occ_type string
             * 14)ent_items string
             * 15) eventID
             * */
            event_data_string_array=(String[])extras.get("event_data_string_array");

            //importedDate=(Date) extras.get("trackSelectedDate");
        }
        else{
            finish(); //activity not properly accessed
        }
        if(event_data_string_array[3].equals("0")){
            editTextTimeApproval.setText("Unapproved");
            editTextTimeApproval.setTextColor(customRed);
        }
        else if(event_data_string_array[3].equals("1")){
            editTextTimeApproval.setText("Approved");
            editTextTimeApproval.setTextColor(customGreen);
        }

        //editTextTime.setText(event_data_string_array[5]);
        editTextDate.setText(event_data_string_array[4]);
        editTextHall.setText(event_data_string_array[6]);
        editTextDuration.setText(event_data_string_array[5]);
        editTextVenue.setText(event_data_string_array[10]);
        editTextMealType.setText(event_data_string_array[9]);
        if(event_data_string_array[12].equals("0")){
            editTextFormality.setText("Informal");
        }
        else if(event_data_string_array[12].equals("1")){
            editTextFormality.setText("Formal");
        }
        if(event_data_string_array[11].equals("0")){
            editTextDrinks.setText("Non-Alcoholic");
        }
        else if(event_data_string_array[11].equals("1")){
            editTextDrinks.setText("Alcoholic");
        }
        String friendlyAtt=event_data_string_array[7]+" people";
        editTextAtt.setText(friendlyAtt);
        String friendlyPrice="$"+String.format("%.2f",Double.parseDouble(event_data_string_array[8]));
        editTextPrice.setText(friendlyPrice);
        editTextOccasion.setText(event_data_string_array[13]);
        editTextEntItems.setText(event_data_string_array[14]);
        editTextOccasion.setEnabled(false);
        editTextEntItems.setEnabled(false);


        setupButtons();
        past_event_check();
        set_alert_dialogs();
    }

    private void set_alert_dialogs(){
        cancel_alert = new AlertDialog.Builder(this)
                .setTitle("Confirm event cancellation")
                .setMessage("Do you really want to cancel this event?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Yes.", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Toast.makeText(user_uc4_5_ViewReservedEventAndCancel.this, "Event cancelled", Toast.LENGTH_SHORT).show();

                        //Intent intent =  new Intent(user_uc4_5_ViewReservedEventAndCancel .this, user_uc3_ViewReservedEventsCalendar .class);
                        //cancel_btn.setBackgroundColor(customGreen);

                        DatabaseInterface.getInstance(user_uc4_5_ViewReservedEventAndCancel.this).deleteEvent(event_data_string_array[15]);

                        /*intent.putExtra("username",username);
                        startActivity(intent);*/
                        cancel_alert.dismiss();
                        finish();
                    }})
                .setNegativeButton("No, go back.", null).show();
        cancel_alert.setIcon(R.drawable.uta_logo_alert);
        cancel_alert.hide();
    }

    private void past_event_check(){
        Calendar cal = Calendar.getInstance();
        long current_time = cal.getTimeInMillis();
        final String selected_date = event_data_string_array[4];
        long millis=0;
        try {
            millis = new SimpleDateFormat("MMM d, yyyy hh:mm aaa").parse(selected_date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(current_time>=millis && millis!=0){
            cancel_btn.setBackgroundColor(customGrey);
            cancel_btn.setEnabled(false);
        }
    }

    private void setupButtons() {
        //TODO "cancel_btn = findViewById(R.id.button_rejectEventRequest);" misplaced by Neelim
        cancel_btn = findViewById(R.id.button_CancelEvent);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v)  {
                cancel_alert.show();
            }
        });
        back_btn= findViewById(R.id.button_goback);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v)  {
                //Intent intent =  new Intent(user_uc4_5_ViewReservedEventAndCancel.this, user_uc3_ViewReservedEventsCalendar .class);
                back_btn.setBackgroundColor(customGreen);
                /*intent.putExtra("username",username);
                startActivity(intent);*/
                cancel_alert.dismiss();
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        //Intent intent = new Intent(user_uc4_5_ViewReservedEventAndCancel .this, user_uc3_ViewReservedEventsCalendar .class);
        /*intent.putExtra("username",username);
        startActivity(intent);*/
        cancel_alert.dismiss();
        finish();
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
                Intent intent0 = new Intent(user_uc4_5_ViewReservedEventAndCancel.this, sysuser_uc2_Login.class);
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
                cancel_alert.dismiss();
                finish();
                return true;
            case R.id.main_menu_go_home:
                Intent intent = new Intent(user_uc4_5_ViewReservedEventAndCancel.this, user_uc0_Home.class);
                intent.putExtra("username",username);
                startActivity(intent);
                cancel_alert.dismiss();
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
