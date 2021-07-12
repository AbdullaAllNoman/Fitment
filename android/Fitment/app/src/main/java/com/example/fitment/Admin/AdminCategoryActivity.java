package com.example.fitment.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.fitment.ui.MainActivity;
import com.example.fitment.R;

public class AdminCategoryActivity extends AppCompatActivity {


    private ImageView chair,table,bed,wardrobe;
    private ImageView sofa,closet,bookShelf,desk;
    private ImageView bench,buffet,interiorMirror,consoleTable;

    private Button logoutBtn,checkOrdersBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        logoutBtn = (Button) findViewById(R.id.admin_logout_btn);
        checkOrdersBtn = (Button) findViewById(R.id.check_order_btn);

        chair = (ImageView)findViewById(R.id.chair);
        table = (ImageView)findViewById(R.id.table);
        bed = (ImageView)findViewById(R.id.bed);
        wardrobe = (ImageView)findViewById(R.id.wardrobe);

        sofa = (ImageView)findViewById(R.id.sofa);
        closet = (ImageView)findViewById(R.id.closet);
        bookShelf = (ImageView)findViewById(R.id.book_shelf);
        desk = (ImageView)findViewById(R.id.desk);

        bench = (ImageView)findViewById(R.id.bench);
        buffet = (ImageView)findViewById(R.id.buffet);
        interiorMirror = (ImageView)findViewById(R.id.interior_mirror);
        consoleTable = (ImageView)findViewById(R.id.console_table);


        chair.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "chair");
                startActivity(intent);
            }
        });


        table.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "table");
                startActivity(intent);
            }
        });

        bed.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "bed");
                startActivity(intent);
            }
        });

        wardrobe.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "wardrobe");
                startActivity(intent);
            }
        });

        sofa.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "sofa");
                startActivity(intent);
            }
        });


        closet.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "closet");
                startActivity(intent);
            }
        });


        bookShelf.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "bookShelf");
                startActivity(intent);
            }
        });


        desk.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "desk");
                startActivity(intent);
            }
        });


        desk.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "desk");
                startActivity(intent);
            }
        });


        bench.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "bench");
                startActivity(intent);
            }
        });


        buffet.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "buffet");
                startActivity(intent);
            }
        });


        interiorMirror.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "interiorMirror");
                startActivity(intent);
            }
        });


        consoleTable.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "consoleTable");
                startActivity(intent);
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        checkOrdersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminNewOrdersActivity.class);
                startActivity(intent);
            }
        });


    }
}
