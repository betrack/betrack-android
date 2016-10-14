package cranio.betrack.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import cranio.betrack.R;
import cranio.betrack.utils.AppPreferences;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText usernameET;
    private EditText passwordET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppPreferences.instance(getApplication()).getSessionOpen()){
            Intent i = new Intent(LoginActivity.this, NfcActivity.class);
            startActivity(i);
            finish();
        }
        setContentView(R.layout.activity_login);

        Button loginBtn = (Button) findViewById(R.id.ingresarBtn);
        assert loginBtn != null;
        loginBtn.setOnClickListener(this);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/betrackfont.ttf");
        loginBtn.setTypeface(typeface);

        usernameET = (EditText) findViewById(R.id.usernameET);
        passwordET = (EditText) findViewById(R.id.passwordET);


    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()) {
            case R.id.ingresarBtn:
                String username = usernameET.getText().toString();
                String password = passwordET.getText().toString();

                if (username.equals("bar@betrack.co")) {
                    if (password.equals("betrack")) {
                        AppPreferences.instance(getApplication()).saveUserMail("bar@betrack.co");
                        AppPreferences.instance(getApplication()).saveUsername("Bar");
                        i = new Intent(LoginActivity.this, NfcActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(this, "Contraseña incorrecta", Toast.LENGTH_LONG).show();

                    }
                } else if (username.equals("cerveceria@betrack.co")) {
                    if (password.equals("betrack")) {
                        AppPreferences.instance(getApplication()).saveUserMail("cerveceria@betrack.co");
                        AppPreferences.instance(getApplication()).saveUsername("Cerveceria");
                        i = new Intent(LoginActivity.this, NfcActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(this, "Contraseña incorrecta", Toast.LENGTH_LONG).show();

                    }
                } else {
                    Toast.makeText(this, "Usuario Incorrecto", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}
