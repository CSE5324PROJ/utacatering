package comcse5324projutacatering.httpsgithub.utacatering;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends Activity {

    private static int MAX_ATTEMPTS = 10;
    private EditText username;
    private EditText password;
    private TextView attempts;
    private Button login_button;
    private TextView register_text;
    private static int attempt_counter = MAX_ATTEMPTS;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = (EditText)findViewById(R.id.editText_user);
        password = (EditText)findViewById(R.id.editText_password);
        attempts = (TextView)findViewById(R.id.textView_attempts);
        login_button = (Button)findViewById(R.id.button_login);
        register_text = (TextView)findViewById(R.id.text_register);
        attempts.setText(Integer.toString(attempt_counter));
        attempts.setVisibility(View.GONE);

        //DatabaseInterface.getInstance(this).createProfile("k","k","Admin",1000555555,"555-555-5555","no allergies");
        setupListeners();
    }

    public void setupListeners() {

        register_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });
        login_button.setOnClickListener(
                new View.OnClickListener()  {
                    @Override
                    public void onClick(View v)  {
                        String user = username.getText().toString();
                        String pass = password.getText().toString();
                        String role = DatabaseInterface.getInstance(Login.this).login(user,pass);

                        if(role != null) {
                            attempt_counter = MAX_ATTEMPTS;
                            Intent intent = null;
                            switch (role) {
                                case "Admin":
                                    intent = new Intent(Login.this, AdminHome.class);
                                    break;
                                case "User":
                                    intent = new Intent(Login.this, user_uc0_Home.class);
                                    break;
                                case "Caterer":
                                    break;
                                case "Caterer Staff":
                                    break;

                            }
                            Toast.makeText(Login.this,"Welcome, "+ user + " {"+role+"}",
                                    Toast.LENGTH_SHORT).show();

                            if (intent != null)
                                startActivity(intent);

                        }else {
                            Toast.makeText(Login.this,"Username and Password are not correct",
                                    Toast.LENGTH_SHORT).show();
                            attempt_counter--;
                            attempts.setVisibility(View.VISIBLE);
                            ((ViewGroup.MarginLayoutParams)login_button.getLayoutParams()).topMargin = 0;
                            attempts.setText("Attempts remaining: " + Integer.toString(attempt_counter));
                            if(attempt_counter == 0) {

                                Toast.makeText(Login.this,"You have exceeded maximum number of attempts allowed for login",
                                        Toast.LENGTH_LONG).show();
                                login_button.setEnabled(false);
                            }
                        }
                }

                }
        );
    }
}
