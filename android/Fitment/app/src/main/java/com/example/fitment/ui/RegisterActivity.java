package com.example.fitment.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
//import android.app.TaskInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fitment.DatabaseConnection.DatabaseConnection;
import com.example.fitment.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RegisterActivity extends AppCompatActivity {

    private Button CreateAccountBatton; // button to create account
    private EditText InputName, InputPhone, InputPassword, InputUserName; //  this edit text will take the name,phone etc.
    public static ProgressDialog loadingBar; // simple progress dialog
    private FirebaseAuth mAuth; // firebase authentication object
    final DatabaseReference RootRef = FirebaseDatabase.getInstance().getReference(); // database reference


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        CreateAccountBatton = (Button)findViewById(R.id.register_btn);
        InputName = (EditText)findViewById(R.id.register_name_input);
        InputPhone = (EditText)findViewById(R.id.register_Phone_input);
        InputPassword = (EditText)findViewById(R.id.register_password_input);
        InputUserName = (EditText)findViewById(R.id.register_username_input);
        loadingBar = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        CreateAccountBatton.setOnClickListener(new View.OnClickListener() {

            // this click listener will listen if an user want to create an account
            @Override
            public void onClick(View view)
            {
                CreateAccount();
            }
        });
    }

    private void CreateAccount()
    {

        // this method will take some validation to create an account

        String name = InputName.getText().toString();
        String phone = InputPhone.getText().toString();
        String email = InputUserName.getText().toString();
        String password = InputPassword.getText().toString();

        if(TextUtils.isEmpty(name)){
            Toast.makeText(this, "Please write your name...", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(phone)){
            Toast.makeText(this, "Please write your phone number...", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please write your username...", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please enter your password...", Toast.LENGTH_SHORT).show();
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
        }
        else if (password.length() < 6){
            Toast.makeText(this, "password length must be more than 6", Toast.LENGTH_SHORT).show();
        }
        else{
            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Please wait, while we are checking credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {

                            if(task.isSuccessful())
                            {
                                DatabaseConnection.createNewUser(RootRef, name, phone, email, password, RegisterActivity.this);
                            }
                            else
                            {
                                loadingBar.dismiss();
                                if(task.getException() instanceof FirebaseAuthUserCollisionException)
                                {
                                    Toast.makeText(RegisterActivity.this, "you are already registered", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(RegisterActivity.this, "error!!", Toast.LENGTH_SHORT).show();

                                }
                            }

                        }
                    });
        }
    }
}
