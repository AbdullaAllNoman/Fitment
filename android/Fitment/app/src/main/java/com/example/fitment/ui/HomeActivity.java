package com.example.fitment.ui;

import android.content.Intent;
import android.os.Bundle;

import com.example.fitment.DatabaseConnection.DatabaseConnection;
import com.example.fitment.Prevalent.Prevalent;
import com.example.fitment.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
//import androidx.legacy.app.ActionBarDrawerToggle;
import androidx.appcompat.app.ActionBarDrawerToggle;
//import android.icu.util.LocaleData.PaperSize;




import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{

    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;

    private DatabaseReference ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products"); // database reference
    private RecyclerView recyclerView;  // recyclerView to show products
    RecyclerView.LayoutManager layoutManager; // layout for recycler view



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar); // home activity toolbar
        toolbar.setTitle("Home"); //title of the ui
        setSupportActionBar(toolbar);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                startActivity(intent); // start the next activity

            }
        });


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        TextView userNameTextView = headerView.findViewById(R.id.user_profile_name);
        ImageView profileImageView = headerView.findViewById(R.id.user_profile_image);

        userNameTextView.setText(Prevalent.currentonlineUser.getName());
        Picasso.get().load(Prevalent.currentonlineUser.getImage()).placeholder(R.drawable.profile2).into(profileImageView);

        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }


    @Override
    protected void onStart() {
        super.onStart();

        DatabaseConnection.showProducts(ProductsRef, HomeActivity.this, recyclerView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    // the method will call if the user press back button on home activity
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //return true if user select an option from navigation
        if(toggle.onOptionsItemSelected(item))
        {
            return true;
        }

        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    // the method will be called if the user select something on navigation
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.nav_cart)
        {
            // if the user select cart on navigation then it will land him to Cart Activity class
            Intent intent = new Intent(HomeActivity.this, CartActivity.class);
            startActivity(intent);

        }
        else if (id == R.id.nav_search)
        {
            // if the user select search on navigation then it will land him to search product Activity class
            Intent intent = new Intent(HomeActivity.this, SearchProductActivity.class);
            startActivity(intent);

        }
        else if (id == R.id.nav_categories)
        {
            // if the user select category on navigation then it will land him to category Activity class
            Intent intent = new Intent(HomeActivity.this, UserCategoryActivity.class);
            startActivity(intent);

        }
        else if (id == R.id.nav_settings)
        {
            // if the user select setting on navigation then it will land him to setting Activity class
            Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
            startActivity(intent);

        }
        else if (id == R.id.nav_logout)
        {
            // if the user select logout on navigation then it will land him to main Activity class
            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
