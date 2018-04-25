package comcse5324projutacatering.httpsgithub.utacatering;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class admin_uc0_Home extends Activity {

    private Button viewRegReqButton;
    private Button searchUserButton;
    private TextView textSignOut;

    private DatabaseInterface db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = DatabaseInterface.getInstance(this);
        setContentView(R.layout.activity_admin_uc0_home);

        setupButtons();

        textSignOut = findViewById(R.id.text_signout);
        textSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void setupButtons() {
        searchUserButton = findViewById(R.id.button_search_user);
        viewRegReqButton = findViewById(R.id.button_view_reg_req);

        viewRegReqButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick (View v)  {
                    Intent intent =  new Intent(admin_uc0_Home.this, admin_uc1_RegistrationRequestList.class);
                    startActivity(intent);
                }
        });

        searchUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(admin_uc0_Home.this, admin_uc5_SearchSystemUser.class);
                startActivity(intent);
            }
        });
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
                onBackPressed();
                return true;
            default:
                return false;
        }
    }
    @Override
    public void onBackPressed() {
        startActivity(db.logout(this));
        finish();
    }

}
