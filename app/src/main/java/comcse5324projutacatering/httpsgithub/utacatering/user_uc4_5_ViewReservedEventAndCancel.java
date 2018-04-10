package comcse5324projutacatering.httpsgithub.utacatering;
//TODO... should be able to rip layout from user_uc2 more or less.

//TODO make sure pass username on return to home
//TODO make sure to pass username and trackSelectedDate if going back to calendar
//TODO actually tracking selected day may not be needed.. calendar remembers position going backwards.
import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.Date;


public class user_uc4_5_ViewReservedEventAndCancel extends Activity {
    String username;
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
