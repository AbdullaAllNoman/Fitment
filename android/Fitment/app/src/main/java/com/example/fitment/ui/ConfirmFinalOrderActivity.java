package com.example.fitment.ui;


import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fitment.DatabaseConnection.DatabaseConnection;
import com.example.fitment.Prevalent.Prevalent;
import com.example.fitment.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmFinalOrderActivity extends AppCompatActivity {


    private EditText nameEditText,phoneEditText,addressEditText;
    private Button confirmOrderBtn;
    private String totalAmount = "";
    final Activity activity = this;
    final DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentonlineUser.getUserName()); // database reference
    final DatabaseReference UserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentonlineUser.getUserName()); // database reference


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);

        totalAmount = getIntent().getStringExtra("Total Price"); // receive the total amount from previous activity
        Toast.makeText(this, "" + totalAmount, Toast.LENGTH_LONG).show();


        confirmOrderBtn = (Button)findViewById(R.id.confirm_final_order_btn);
        nameEditText = (EditText)findViewById(R.id.shippment_name);
        phoneEditText = (EditText)findViewById(R.id.shippment_phone_number);
        addressEditText = (EditText)findViewById(R.id.shippment_address);

        DatabaseConnection.userInfoDisplayForShipment(UserRef, nameEditText, phoneEditText, addressEditText);


        confirmOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //confirmation of order
                ConfirmOrder();

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    private void ConfirmOrder()
    {
        final String saveCurrentDate,saveCurrentTime;
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());
        HashMap<String, Object> orderMap = new HashMap<>();  // the details needed to confirm order


        orderMap.put("totalAmount", totalAmount);
        orderMap.put("name", nameEditText.getText().toString());
        orderMap.put("phone", phoneEditText.getText().toString());
        orderMap.put("address", addressEditText.getText().toString());
        orderMap.put("date", saveCurrentDate);
        orderMap.put("time", saveCurrentTime);
        orderMap.put("state", "not shipped");



        DatabaseConnection.orderConfirmation(ordersRef, ConfirmFinalOrderActivity.this, orderMap, activity);
    }
}
