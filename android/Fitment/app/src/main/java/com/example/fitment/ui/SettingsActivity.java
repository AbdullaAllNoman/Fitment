package com.example.fitment.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fitment.DatabaseConnection.DatabaseConnection;
import com.example.fitment.Prevalent.Prevalent;
import com.example.fitment.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.theartofdev.edmodo.cropper.CropImage;

public class SettingsActivity extends AppCompatActivity {


    private ImageView profileImageView;  // the variable is for image
    private EditText fullNameEditText, userPhoneEditText, addressEditText; // this edit text are for name ,phone and address
    private TextView profileChangeTextBtn, closeTextBtn, saveTextBtn; // some text view to update profile

    private Uri imageUri; // to fetch image url from storage
    private String myUrl = "";
    private StorageTask uploadTask;
    private StorageReference storageProfilePictureRef; // storage reference
    private String checker = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        storageProfilePictureRef = FirebaseStorage.getInstance().getReference().child("Profile Pictures");

        profileImageView = (ImageView) findViewById(R.id.settings_profile_image);
        fullNameEditText = (EditText) findViewById(R.id.settings_full_name);
        userPhoneEditText = (EditText) findViewById(R.id.settings_phone_number);
        addressEditText = (EditText) findViewById(R.id.settings_address);
        profileChangeTextBtn = (TextView) findViewById(R.id.profile_image_change_btn);
        closeTextBtn = (TextView) findViewById(R.id.close_settings_btn);
        saveTextBtn = (TextView) findViewById(R.id.update_account_setting_btn);

        DatabaseReference UserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentonlineUser.getUserName());

        DatabaseConnection.userInfoDisplay(UserRef, profileImageView, fullNameEditText, userPhoneEditText, addressEditText);

        closeTextBtn.setOnClickListener(new View.OnClickListener(){

            // this will land the user from setting activity to home activity
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });

        saveTextBtn.setOnClickListener(new View.OnClickListener(){

            // update user info
            @Override
            public void onClick(View view)
            {
                if(checker.equals("clicked"))
                {
                    userInfoSaved();
                }
                else
                {
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");
                    DatabaseConnection.updateUserInfo(ref, SettingsActivity.this, SettingsActivity.this, fullNameEditText, addressEditText, userPhoneEditText);
                }
            }
        });

        //user want to change the profile picture or not!
       profileChangeTextBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view)
           {
               checker = "clicked";

               CropImage.activity(imageUri)
                       .setAspectRatio(1,1)
                       .start(SettingsActivity.this);
           }
       });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE &&
                resultCode == RESULT_OK && data != null)
        {
            // crop image and upload it to storage
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();

            profileImageView.setImageURI(imageUri);
        }
        else
        {
            //give a message to user and refresh activity
            Toast.makeText(this, "Error! Try Again.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SettingsActivity.this, SettingsActivity.class));
            finish();
        }
    }

    private void userInfoSaved()
    {
        // user info save or update into database
        if(TextUtils.isEmpty(fullNameEditText.getText().toString()))
        {
            Toast.makeText(this, "Name is mandatory", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(addressEditText.getText().toString()))
        {
            Toast.makeText(this, "address is mandatory", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(userPhoneEditText.getText().toString()))
        {
            Toast.makeText(this, "phone number is mandatory", Toast.LENGTH_SHORT).show();
        }
        else if(checker.equals("clicked"))
        {
            final StorageReference fileRef = storageProfilePictureRef.child(Prevalent.currentonlineUser.getUserName() + ".jpg");
            DatabaseConnection.uploadImageToDatabase(fileRef,SettingsActivity.this, imageUri, SettingsActivity.this, fullNameEditText, addressEditText, userPhoneEditText );
        }
    }
}
