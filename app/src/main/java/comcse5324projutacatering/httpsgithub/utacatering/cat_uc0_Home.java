package comcse5324projutacatering.httpsgithub.utacatering;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class cat_uc0_Home extends Activity {

    private Button viewEventReqQueue;
    private Button viewEventCal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat_uc0_home);

        setupButtons();
    }
    private void setupButtons() {
        viewEventReqQueue = (Button)findViewById(R.id.button_view_event_req_queue);
        viewEventCal = (Button)findViewById(R.id.button_view_event_cal);

        viewEventReqQueue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v)  {
                Intent intent =  new Intent(cat_uc0_Home.this, cat_uc1_ViewEventReqQueue.class);
                startActivity(intent);
            }
        });

        viewEventCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(cat_uc0_Home.this, cat_uc5_ViewEventCal.class);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        DatabaseInterface.getInstance(this).close();
        super.onDestroy();
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
                Intent intent0 = new Intent(cat_uc0_Home.this, sysuser_uc2_Login .class);
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
                Intent intent = new Intent(cat_uc0_Home.this, cat_uc0_Home.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return false;
        }
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(cat_uc0_Home.this, sysuser_uc2_Login .class);
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
