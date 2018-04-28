package comcse5324projutacatering.httpsgithub.utacatering;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class user_uc0_Home extends Activity {
    public int customRed;
    public int customGreen;
    public int customBlue;
    public String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_uc0_home);
        /*
        final ActionBar actionbar = getActionBar();
        if(actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setTitle("User Home");
        }
        */


        Intent mIntent;
        Bundle extras;
        mIntent = getIntent();
        extras=mIntent.getExtras();
        if(extras!=null){
            username = (String) extras.get("username");
        }
        else{
            finish(); //activity not properly accessed (must come from login)
        }
        customRed = getResources().getColor(R.color.customRed);
        customGreen = getResources().getColor(R.color.customGreen);
        customBlue = getResources().getColor(R.color.customBlue);
        setupButtons();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu_main, menu);

        //hide go to home page when on the home page
        MenuItem item = menu.findItem(R.id.main_menu_go_home);
        item.setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection

        switch (item.getItemId()) {
            case R.id.main_menu_sign_out:
                Intent intent = new Intent(user_uc0_Home.this, sysuser_uc2_Login .class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
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
                startActivity(intent);
                finish();
                return true;
            case R.id.main_menu_go_home:
                Intent intent0 = new Intent(user_uc0_Home.this, user_uc0_Home.class);
                startActivity(intent0);
                //finish();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return false;
        }
    }

    private void setupButtons() {
        final Button user_uc1_availHalls_btn = findViewById(R.id.button_user_uc1);
        final Button user_uc3_ViewReservedEventsCalendar_btn = findViewById(R.id.button_user_uc3);
        final Button sysuser_uc4_nonadmin_UpdateProfile_btn = findViewById(R.id.button_user_upd_profile);

        //blue buttons
        //user_uc1_availHalls_btn.setBackgroundColor(customBlue);
        //user_uc3_ViewReservedEventsCalendar_btn.setBackgroundColor(customBlue);
        //sysuser_uc4_nonadmin_UpdateProfile_btn.setBackgroundColor(customBlue);

        user_uc1_availHalls_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v)  {
                //user_uc1_availHalls_btn.setBackgroundColor(customGreen);
                Intent intent =  new Intent(user_uc0_Home.this, user_uc1_availHalls.class);
                intent.putExtra("username",username);
                startActivity(intent);
                /*Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        user_uc1_availHalls_btn.setBackgroundColor(customBlue);
                    }
                }, 500);*/

                //finish();
            }
        });
        user_uc3_ViewReservedEventsCalendar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v)  {
                //user_uc3_ViewReservedEventsCalendar_btn.setBackgroundColor(customGreen);
                Intent intent =  new Intent(user_uc0_Home.this, user_uc3_ViewReservedEventsCalendar.class);
                intent.putExtra("username",username);
                startActivity(intent);
                /*Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        user_uc3_ViewReservedEventsCalendar_btn.setBackgroundColor(customBlue);
                    }}, 500);*/
                //finish();
            }
        });
        sysuser_uc4_nonadmin_UpdateProfile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v)  {
                //sysuser_uc4_nonadmin_UpdateProfile_btn.setBackgroundColor(customGreen);
                Intent intent =  new Intent(user_uc0_Home.this, sysuser_uc4_nonadmin_UpdateProfile.class);
                intent.putExtra("username",username);
                startActivity(intent);
                /*Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        sysuser_uc4_nonadmin_UpdateProfile_btn.setBackgroundColor(customBlue);
                    }}, 500);*/
                //finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(user_uc0_Home.this, sysuser_uc2_Login .class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
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
        startActivity(intent);
        finish();
    }
}
