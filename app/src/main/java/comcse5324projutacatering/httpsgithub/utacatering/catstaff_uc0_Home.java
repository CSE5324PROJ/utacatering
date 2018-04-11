package comcse5324projutacatering.httpsgithub.utacatering;
//TODO viewEventList

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class catstaff_uc0_Home extends Activity {

    private Button viewEventList;
    private Button updateProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catstaff_uc0_home);
    }

    private void setupButtons(){
        viewEventList = (Button)findViewById(R.id.button_catstaff_view_event_list);
        updateProfile = (Button)findViewById(R.id.button_catstaff_update_profile);

        viewEventList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v)  {
                //Intent intent =  new Intent(catstaff_uc0_Home.this, .class);
                //startActivity(intent);
            }
        });

        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v)  {
                Intent intent =  new Intent(catstaff_uc0_Home.this, sysuser_uc4_nonadmin_UpdateProfile.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.main_menu_sign_out:
                Intent intent0 = new Intent(catstaff_uc0_Home .this, sysuser_uc2_Login .class);
                intent0.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK
                );
                startActivity(intent0);
                finish();
                return true;
            case R.id.main_menu_go_home:
                Intent intent = new Intent(catstaff_uc0_Home.this, catstaff_uc0_Home.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return false;
        }
    }
}
