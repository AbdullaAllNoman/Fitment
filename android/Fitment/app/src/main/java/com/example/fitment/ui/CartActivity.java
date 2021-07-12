package com.example.fitment.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fitment.DatabaseConnection.DatabaseConnection;
import com.example.fitment.Prevalent.Prevalent;
import com.example.fitment.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button nextProcessButton;
    private TextView txtTotalAmount, txtMsg1;


    final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List"); // database reference
    final DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentonlineUser.getUserName()); // database reference


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        nextProcessButton = (Button) findViewById(R.id.next_btn);
        txtTotalAmount = (TextView) findViewById(R.id.total_price);
        txtMsg1 = (TextView) findViewById(R.id.msg1);




    }

    @Override
    protected void onStart()
    {
        super.onStart();

        DatabaseConnection.CheckOrderState(ordersRef, CartActivity.this, recyclerView, txtTotalAmount, txtMsg1, nextProcessButton);
        DatabaseConnection.updateCart(cartListRef,CartActivity.this, recyclerView, txtTotalAmount);



            nextProcessButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CartActivity.this, ConfirmFinalOrderActivity.class);
                    intent.putExtra("Total Price", txtTotalAmount.getText()); //send total price to confirm final order activity
                    startActivity(intent); //start next activity
                    finish();
                }
            });


    }
}
