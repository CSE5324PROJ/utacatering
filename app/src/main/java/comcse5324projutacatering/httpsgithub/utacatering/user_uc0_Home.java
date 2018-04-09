package comcse5324projutacatering.httpsgithub.utacatering;

import android.app.Activity;
import android.content.Intent;
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.main_menu_sign_out:
                finish();
                return true;
            case R.id.main_menu_go_home:
                Intent intent = new Intent(user_uc0_Home.this, user_uc0_Home.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return false;
        }
    }

    private void setupButtons() {
        final Button user_uc1_availHalls_btn = findViewById(R.id.button_user_uc1);
        Button user_uc3_availHalls_btn = findViewById(R.id.button_user_uc3);
        user_uc1_availHalls_btn.setBackgroundColor(customBlue);
        user_uc1_availHalls_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v)  {
                user_uc1_availHalls_btn.setBackgroundColor(customGreen);
                Intent intent =  new Intent(user_uc0_Home.this, user_uc1_availHalls.class);
                intent.putExtra("username",username);
                startActivity(intent);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        user_uc1_availHalls_btn.setBackgroundColor(customBlue);
                    }
                }, 500);

                //finish();
            }
        });
        user_uc3_availHalls_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v)  {
                /*Intent intent =  new Intent(user_uc0_Home.this, user_uc2_ReqEvent.class); //TODO temporary!
                startActivity(intent);
                finish();*/
            }
        });

    }
}
