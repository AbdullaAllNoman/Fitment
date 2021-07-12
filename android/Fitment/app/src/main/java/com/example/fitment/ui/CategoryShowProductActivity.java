package com.example.fitment.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.fitment.DatabaseConnection.DatabaseConnection;
import com.example.fitment.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CategoryShowProductActivity extends AppCompatActivity {
    private RecyclerView searchList;
    private String categoryPassing = "";
    final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Products"); // database reference



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_show_product);

        categoryPassing = getIntent().getStringExtra("passId"); // receive the id from previous activity

        searchList = findViewById(R.id.show_product);
        searchList.setLayoutManager(new LinearLayoutManager(CategoryShowProductActivity.this));



    }
    @Override
    protected void onStart()
    {
        super.onStart();
        DatabaseConnection.categoryWiseShowProduct(reference, categoryPassing, CategoryShowProductActivity.this, searchList );

    }
}
