package comcse5324projutacatering.httpsgithub.utacatering;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static android.content.ContentValues.TAG;

public class sysuser_uc2_Login extends Activity {

    private EditText username;
    private EditText password;
    private TextView attemptMessage;
    private Button login_button;
    private TextView register_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        android.app.ActionBar actionBar = getActionBar();
        if(actionBar != null) {
            actionBar.setTitle("MavCat Login");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sysuser_uc2_login);
        username = (EditText)findViewById(R.id.editText_user);
        password = (EditText)findViewById(R.id.editText_password);
        attemptMessage = (TextView)findViewById(R.id.textView_attempts);
        attemptMessage.setText("Invalid login.");
        //((ViewGroup.MarginLayoutParams)attemptMessage.getLayoutParams()).topMargin = 5;
        attemptMessage.setVisibility(View.INVISIBLE);
        login_button = (Button)findViewById(R.id.button_login);
        //((ViewGroup.MarginLayoutParams)login_button.getLayoutParams()).topMargin = 10;
        register_text = (TextView)findViewById(R.id.text_register);
        //attemptMessage.setVisibility(View.GONE);

        //DatabaseInterface.getInstance(this).createProfile("k","k","Admin",1000555555,"555-555-5555","no allergies");
        setupListeners();
    }

    public void setupListeners() {

        register_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(sysuser_uc2_Login.this, sysuser_uc1_Register.class);
                startActivity(intent);

            }
        });
        login_button.setOnClickListener(
                new View.OnClickListener()  {
                    @Override
                    public void onClick(View v)  {
                        String user = username.getText().toString();
                        String pass = password.getText().toString();
                        String role = DatabaseInterface.getInstance(sysuser_uc2_Login.this).login(user,pass);
                        Intent intent = null;
                        if(role != null) {
                            switch (role) {
                                case "Admin":
                                    intent = new Intent(sysuser_uc2_Login.this, admin_uc0_Home.class);
                                    break;
                                case "User":
                                    intent = new Intent(sysuser_uc2_Login.this, user_uc0_Home.class);
                                    break;
                                case "Caterer":
                                    intent = new Intent(sysuser_uc2_Login.this, cat_uc0_Home.class);
                                    break;
                                case "Caterer Staff":
                                    intent = new Intent(sysuser_uc2_Login.this, catstaff_uc0_Home.class);
                                    break;

                            }
                            attemptMessage.setVisibility(View.INVISIBLE);
                            Toast.makeText(sysuser_uc2_Login.this,"Welcome, "+ user + " {"+role+"}",
                                    Toast.LENGTH_SHORT).show();

                            if (intent != null) {
                                intent.putExtra("username", user);
                                startActivity(intent);
                            }

                        }else {
                            //Toast.makeText(sysuser_uc2_Login.this,"Username and Password are not correct", Toast.LENGTH_SHORT).show();

                            //((ViewGroup.MarginLayoutParams)login_button.getLayoutParams()).topMargin = 0;


                            attemptMessage.setVisibility(View.VISIBLE);
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    attemptMessage.setVisibility(View.INVISIBLE);
                                }
                            }, 1500);
                        }
                    }

                }
        );
        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))
                        || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    Log.i(TAG,"Enter selected");
                    login_button.performClick();
                    login_button.setPressed(true);
                    login_button.invalidate();
                    login_button.setPressed(false);
                    login_button.invalidate();
                }
                return false;
            }
        });
    }
    public void hideKeyboard(View view){
        if(view!=null){
            InputMethodManager inMethMan = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if(inMethMan!=null){
                inMethMan.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }
}
