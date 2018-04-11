package comcse5324projutacatering.httpsgithub.utacatering;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    }
}
