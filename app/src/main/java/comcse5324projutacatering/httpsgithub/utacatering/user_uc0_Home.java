package comcse5324projutacatering.httpsgithub.utacatering;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class user_uc0_Home extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_uc0__home);
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
        Button user_uc1_availHalls_btn;
        Button user_uc3_availHalls_btn;
        user_uc1_availHalls_btn = findViewById(R.id.button_user_uc1);
        user_uc3_availHalls_btn = findViewById(R.id.button_user_uc3);
        user_uc1_availHalls_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v)  {
                Intent intent =  new Intent(user_uc0_Home.this, user_uc1_availHalls.class);
                startActivity(intent);
                finish();
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
