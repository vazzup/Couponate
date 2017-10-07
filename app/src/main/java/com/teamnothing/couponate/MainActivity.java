package com.teamnothing.couponate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.teamnothing.couponate.loginasynctask.LoginAsyncTask;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    Button loginButton;
    EditText emailTextBox, passwordTextBox;
    TextView linkSignup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginButton = (Button) findViewById(R.id.btn_login);
        emailTextBox = (EditText) findViewById(R.id.input_email);
        passwordTextBox = (EditText) findViewById(R.id.input_password);
        linkSignup = (TextView) findViewById(R.id.link_signup);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    boolean result = new LoginAsyncTask().execute(emailTextBox.getText().toString(), passwordTextBox.getText().toString()).get();
                    if(!result) {
                        linkSignup.setText("Incorrect Credentials!");
                    } else {
                        linkSignup.setText("Successful Login!");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
