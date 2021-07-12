package com.example.fitment.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.fitment.DatabaseConnection.DatabaseConnection;
import com.example.fitment.Prevalent.Prevalent;
import com.example.fitment.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity {

    private Button addToCartBtn,arCameraBtn; // button to add product to cart and to see ar view
    private ImageView productImage; // fetch product image from database
    private ElegantNumberButton numberButton; // button to select quantity of a product
    private TextView productPrice, productDescription, productName; //fetch price,description and name of a product
    private String productId= "", state = "Normal"; // product identity unique
    final DatabaseReference productref = FirebaseDatabase.getInstance().getReference().child("Products"); // database reference
    final DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentonlineUser.getUserName()); // database reference


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        productId = getIntent().getStringExtra("pid"); // receive the product id from previous activity

        addToCartBtn = (Button) findViewById(R.id.pd_add_to_cart_button);
        arCameraBtn = (Button) findViewById(R.id.augmented_button);
        productImage = (ImageView) findViewById(R.id.product_image_details);
        numberButton = (ElegantNumberButton) findViewById(R.id.number_btn);
        productPrice = (TextView) findViewById(R.id.product_price_details);
        productDescription = (TextView) findViewById(R.id.product_description_details);
        productName = (TextView) findViewById(R.id.product_name_details);

        DatabaseConnection.getProductsDetails(productref, productId, productName, productPrice, productDescription, productImage);


        arCameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseConnection.goToARCameraActivityClass(productref, productId, ProductDetailsActivity.this);
            }
        });

        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //this button will add a product to cart on click
                if(state.equals("Order Placed") || state.equals("Order Shipped"))
                {
                    Toast.makeText(ProductDetailsActivity.this, "You can add more products once your order is shipped", Toast.LENGTH_LONG).show();
                }
                else
                {
                    final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
                    String saveCurrentTime, saveCurrentDate;

                    Calendar calForDate = Calendar.getInstance();
                    SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
                    saveCurrentDate = currentDate.format(calForDate.getTime());

                    SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
                    saveCurrentTime = currentDate.format(calForDate.getTime());

                    final HashMap<String, Object> cartMap = new HashMap<>(); // details of a product

                    cartMap.put("pid", productId);
                    cartMap.put("pname", productName.getText().toString());
                    cartMap.put("price", productPrice.getText().toString());
                    cartMap.put("date", saveCurrentDate);
                    cartMap.put("time", saveCurrentTime);
                    cartMap.put("quantity", numberButton.getNumber());
                    cartMap.put("discount", "");
                    DatabaseConnection.addProductToCart(cartListRef, cartMap, productId, ProductDetailsActivity.this);
                }
            }
        });
    }
    @Override
    protected void onStart()
    {
        super.onStart();

        state = DatabaseConnection.checkOrderState(ordersRef); // check the state of a product that it is shipped or not
    }
}