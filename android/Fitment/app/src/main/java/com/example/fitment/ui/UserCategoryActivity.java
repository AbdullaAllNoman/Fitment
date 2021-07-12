package com.example.fitment.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.fitment.R;

public class UserCategoryActivity extends AppCompatActivity
{

    private ImageView chair,table,bed,wardrobe;    // image view for category
    private ImageView sofa,closet,bookShelf,desk;  // image view for category
    private ImageView bench,buffet,interiorMirror,consoleTable; // image view for category
    private RecyclerView searchList; // recycler view to show the category wise product
    private String categoryPassing = ""; //pass the category to the next activity
    private LinearLayout layout1,layout2,layout3;
    private TextView allCategory,products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_category);

        chair = (ImageView)findViewById(R.id.u_chair);
        table = (ImageView)findViewById(R.id.u_table);
        bed = (ImageView)findViewById(R.id.u_bed);
        wardrobe = (ImageView)findViewById(R.id.u_wardrobe);

        sofa = (ImageView)findViewById(R.id.u_sofa);
        closet = (ImageView)findViewById(R.id.u_closet);
        bookShelf = (ImageView)findViewById(R.id.u_book_shelf);
        desk = (ImageView)findViewById(R.id.u_desk);

        bench = (ImageView)findViewById(R.id.u_bench);
        buffet = (ImageView)findViewById(R.id.u_buffet);
        interiorMirror = (ImageView)findViewById(R.id.u_interior_mirror);
        consoleTable = (ImageView)findViewById(R.id.u_console_table);



        // all of this click listener are send the user to the
        // next activity where the user can see only
        // the products of that specific category

        chair.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(UserCategoryActivity.this, CategoryShowProductActivity.class);
                intent.putExtra("passId","chair");
                startActivity(intent);
            }
        });
        table.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(UserCategoryActivity.this, CategoryShowProductActivity.class);
                intent.putExtra("passId","table");
                startActivity(intent);
            }
        });

        bed.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(UserCategoryActivity.this, CategoryShowProductActivity.class);
                intent.putExtra("passId","bed");
                startActivity(intent);
            }
        });

        wardrobe.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(UserCategoryActivity.this, CategoryShowProductActivity.class);
                intent.putExtra("passId","wardrobe");
                startActivity(intent);
            }
        });

        sofa.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(UserCategoryActivity.this, CategoryShowProductActivity.class);
                intent.putExtra("passId","sofa");
                startActivity(intent);
            }
        });


        closet.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(UserCategoryActivity.this, CategoryShowProductActivity.class);
                intent.putExtra("passId","closet");
                startActivity(intent);
            }
        });


        bookShelf.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(UserCategoryActivity.this, CategoryShowProductActivity.class);
                intent.putExtra("passId","bookShelf");
                startActivity(intent);
            }
        });


        desk.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(UserCategoryActivity.this, CategoryShowProductActivity.class);
                intent.putExtra("passId","desk");
                startActivity(intent);
            }
        });


        bench.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(UserCategoryActivity.this, CategoryShowProductActivity.class);
                intent.putExtra("passId","bench");
                startActivity(intent);
            }
        });


        buffet.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(UserCategoryActivity.this, CategoryShowProductActivity.class);
                intent.putExtra("passId","buffet");
                startActivity(intent);
            }
        });


        interiorMirror.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(UserCategoryActivity.this, CategoryShowProductActivity.class);
                intent.putExtra("passId","interiorMirror");
                startActivity(intent);
            }
        });


        consoleTable.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(UserCategoryActivity.this, CategoryShowProductActivity.class);
                intent.putExtra("passId","consoleTable");
                startActivity(intent);
            }
        });
    }


}
