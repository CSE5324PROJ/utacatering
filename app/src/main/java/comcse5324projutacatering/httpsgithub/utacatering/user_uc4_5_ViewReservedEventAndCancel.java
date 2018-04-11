package comcse5324projutacatering.httpsgithub.utacatering;
//TODO make sure to pass username and trackSelectedDate if going back to calendar
import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.Date;


public class user_uc4_5_ViewReservedEventAndCancel extends Activity {
    String username;
    Button cancel_btn;
    Button back_btn;

    public int customRed;
    public int customGreen;
    public int customBlue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_uc4_5_view_reserved_event_and_cancel);
        Intent mIntent;
        Bundle extras;
        mIntent = getIntent();
        extras=mIntent.getExtras();
        if(extras!=null){
            username = (String) extras.get("username");
            //importedDate=(Date) extras.get("trackSelectedDate");
        }
        else{
            finish(); //activity not properly accessed
        }

        setupButtons();
    }

    private void setupButtons() {
        cancel_btn = findViewById(R.id.button_CancelEvent);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v)  {
                Intent intent =  new Intent(user_uc4_5_ViewReservedEventAndCancel .this, user_uc3_ViewReservedEventsCalendar .class);
                    cancel_btn.setBackgroundColor(customGreen);
                    intent.putExtra("username",username);
                    startActivity(intent);
                    finish();
            }
        });
        back_btn= findViewById(R.id.button_goback);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v)  {
                Intent intent =  new Intent(user_uc4_5_ViewReservedEventAndCancel.this, user_uc3_ViewReservedEventsCalendar .class);
                back_btn.setBackgroundColor(customGreen);
                intent.putExtra("username",username);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(user_uc4_5_ViewReservedEventAndCancel .this, user_uc3_ViewReservedEventsCalendar .class);
        intent.putExtra("username",username);
        startActivity(intent);
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
                startActivity(intent0);
                finish();
                return true;
            case R.id.main_menu_go_home:
                Intent intent = new Intent(user_uc4_5_ViewReservedEventAndCancel.this, user_uc0_Home.class);
                intent.putExtra("username",username);
                startActivity(intent);
                finish();
                return true;
            default:
                return false;
        }
    }
}
