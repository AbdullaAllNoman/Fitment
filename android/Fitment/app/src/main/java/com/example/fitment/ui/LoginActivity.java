package com.example.fitment.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fitment.DatabaseConnection.DatabaseConnection;
import com.example.fitment.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText InputUserName, InputPassword; //user input
    private Button LoginButton; //button for login
    public static ProgressDialog loadingBar; //
    final DatabaseReference RootRef = FirebaseDatabase.getInstance().getReference(); // database reference
    String parentDbName = "Users";
    private SignInButton googleBtn; // button to login with google
    FirebaseAuth mAuth; // firebase authentication
    private FirebaseAuth.AuthStateListener firebaseAuthListener; //authentication listener
    GoogleSignInClient mGoogleSignInClient; // google sign in object for authentication
    private int RC_SIGN_IN = 1; //random initalization for authentication
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LoginButton = (Button)findViewById(R.id.login_btn);
        InputUserName = (EditText)findViewById(R.id.login_username_input);
        InputPassword = (EditText)findViewById(R.id.login_password_input);
        loadingBar = new ProgressDialog(this);
        googleBtn = (SignInButton)findViewById(R.id.google_sign_in_button);

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                // this method will validate the user info for login
                LoginUser();
            }
        });
        mAuth = FirebaseAuth.getInstance();


        // google sign object
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        googleBtn.setOnClickListener(new View.OnClickListener() {

            // this click listener will land the user to home activity after validation
            @Override
            public void onClick(View v) {
                loadingBar.setTitle("Login Account");
                loadingBar.setMessage("Please wait, while we are checking credentials.");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN)
        {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try
            {
                // if sign in result is ok then sign in
                GoogleSignInAccount acc = task.getResult(ApiException.class);
                //Toast.makeText(this, "Signed in Successfully", Toast.LENGTH_SHORT).show();
                firebaseGoogleAuth(acc);
            }

            catch (ApiException e)
            {
                //else show an error message
                Toast.makeText(this, "Signed in failed", Toast.LENGTH_SHORT).show();
                firebaseGoogleAuth(null);
            }
        }
    }


    private void firebaseGoogleAuth(GoogleSignInAccount acct) {

        // this method will call to sign in with
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            String userName = getIDforgoogleLogin();
                            DatabaseConnection.googleSignin(RootRef, userName, LoginActivity.this);
                        }
                        else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }
    private void updateUI(FirebaseUser user)
    {
        // this method will call to set user interface

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if(account != null)
        {
                String uid = user.getUid();
                String name = user.getDisplayName();
                Map<String,Object> updates = new HashMap<String,Object>();
                updates.put("userName", uid);
                updates.put("name", name);

                RootRef.child("Users").child(uid).updateChildren(updates);
        }


    }
    private String getIDforgoogleLogin()
    {
        // this method will return unique id of a user
        FirebaseUser user = mAuth.getCurrentUser();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
            String userName = user.getUid();
            return userName;
    }


    private void LoginUser()
    {
        //this method will call to validate user info for normal user(not google login)
        final DatabaseReference RootRef = FirebaseDatabase.getInstance().getReference();
        String email = InputUserName.getText().toString();
        String password = InputPassword.getText().toString();
        if(TextUtils.isEmpty(email)){
        Toast.makeText(this, "Please enter your email...", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password)){
        Toast.makeText(this, "Please enter your password...", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("Please wait, while we are checking credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            mAuth.signInWithEmailAndPassword(email , password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    if(task.isSuccessful())
                    {
                        DatabaseConnection.AllowAccessToAccount(RootRef, email, LoginActivity.this);
                    }
                    else
                    {
                        Toast.makeText(LoginActivity.this, "wrong email or password!!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
