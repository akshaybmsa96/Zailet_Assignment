package com.example.akki.zailetassignment;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button login=(Button)findViewById(R.id.btnSignIn);
        final EditText un=(EditText)findViewById(R.id.uname);
        final EditText ps=(EditText)findViewById(R.id.upass);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextInputLayout unl = (TextInputLayout) findViewById(R.id.lv2);
                TextInputLayout upl = (TextInputLayout) findViewById(R.id.lv3);

                if(un.getText().toString().equals("") && ps.getText().toString().equals("")) {      //set error if fields are empty
                    if (un.getText().toString().equals("")) {
                        unl.setError("INVALID Username");
                        unl.setErrorEnabled(true);
                    } else {
                        unl.setErrorEnabled(false);
                    }
                    if (ps.getText().toString().equals("")) {
                        upl.setError("INVALID Password");
                        upl.setErrorEnabled(true);
                    } else {
                        upl.setErrorEnabled(false);
                    }
                }

                else if(!(un.getText().toString().equals("") && ps.getText().toString().equals("")) )   // authenticating the username and password
                {
                    DbTaskLogin db = new DbTaskLogin(LoginActivity.this,LoginActivity.this);
                    db.execute(un.getText().toString(),ps.getText().toString());                  //passing parameters i.e username and password
                }

            }
        });
    }
}
