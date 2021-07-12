package com.example.fitment.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.fitment.R;

public class MainActivity extends AppCompatActivity {

    private Button joinNowButton, loginButton; // button for login for old user or signup for new user

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        joinNowButton = (Button)findViewById(R.id.main_join_now_btn);
        loginButton = (Button)findViewById(R.id.main_login_btn);

        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view)
            {
                // old user
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent); // start the next activity
            }

        });

        joinNowButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view)
            {
                //new user
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent); // start the next activity
            }

        });
    }
}
