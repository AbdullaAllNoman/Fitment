package com.example.fitment.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.fitment.DatabaseConnection.DatabaseConnection;
import com.example.fitment.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SearchProductActivity extends AppCompatActivity {

    private Button searchBtn; // user need to click this button for searching products
    private EditText inputText; //the products name given by the user for searching
    private RecyclerView searchList; // the result od the search will be shown on recycler view
    private String searchInput; // the name given by input text will be saved in this varible

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product);


        searchBtn = findViewById(R.id.search_btn);
        inputText = findViewById(R.id.search_product_name);
        searchList = findViewById(R.id.search_list);
        searchList.setLayoutManager(new LinearLayoutManager(SearchProductActivity.this));

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                // the click listener will listen the button pressed
                searchInput = inputText.getText().toString();

                onStart();
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Products");

        DatabaseConnection.searchByName(reference, SearchProductActivity.this, searchList, searchInput);
    }
}

