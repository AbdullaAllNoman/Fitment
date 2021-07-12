package com.example.fitment.DatabaseConnection;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitment.ui.ArCameraActivity;
import com.example.fitment.ui.CartActivity;
import com.example.fitment.ui.HomeActivity;
import com.example.fitment.ui.LoginActivity;
import com.example.fitment.Model.Cart;
import com.example.fitment.Model.Products;
import com.example.fitment.Model.Users;
import com.example.fitment.Prevalent.Prevalent;
import com.example.fitment.ui.ProductDetailsActivity;
import com.example.fitment.R;
import com.example.fitment.ui.RegisterActivity;
import com.example.fitment.ViewHolder.CartViewHolder;
import com.example.fitment.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import java.util.HashMap;

public class DatabaseConnection
{
    private static String parentDbName = "Users";
    static String state = "Normal";

    /**
     * By using this method user can login into account
     * @param RootRef is the reference of the database
     * @param context of the that activity
     * @param email will take the email for validation
     */

    public static void AllowAccessToAccount(final DatabaseReference RootRef, final String email, final Context context)
    {
        String userName = FirebaseAuth.getInstance().getCurrentUser().getUid();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.child(parentDbName).child(userName).exists())
                {
                    Users userData = dataSnapshot.child(parentDbName).child(userName).getValue(Users.class);

                    if(userData.getEmail().equals(email))
                    {
                        LoginActivity.loadingBar.dismiss();
                        if(parentDbName.equals("Users"))
                        {
                            Toast.makeText(context, "Succesfully logged in!!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context, HomeActivity.class);
                            Prevalent.currentonlineUser = userData;
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            context.startActivity(intent);
                        }
                    }
                }
                else
                {
                    Toast.makeText(context, "Account with email:  " + email + " doesn't exists!!!", Toast.LENGTH_SHORT).show();
                    LoginActivity.loadingBar.dismiss();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                Toast.makeText(context, "Error! connecting with database", Toast.LENGTH_SHORT).show();
                LoginActivity.loadingBar.dismiss();
            }
        });
    }

    /**
     * By using this method user can login into account using google
     * @param RootRef is the reference of the database
     * @param context of the that activity where the method is called
     * @param userName will take the unique username produced by database authentication
     */
    public static void googleSignin(final DatabaseReference RootRef, final String userName, final Context context)
    {
        RootRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.child(parentDbName).child(userName).exists())
                {
                    Users userData = dataSnapshot.child(parentDbName).child(userName).getValue(Users.class);

                    if(userData.getUserName().equals(userName))
                    {

                        if(parentDbName.equals("Users"))
                        {

                            LoginActivity.loadingBar.dismiss();
                            Intent intent = new Intent(context, HomeActivity.class);
                            Prevalent.currentonlineUser = userData;
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            context.startActivity(intent);
                            Toast.makeText(context, "Signed in Successfully", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else
                {
                    Toast.makeText(context, "Account with this email  doesn't exists!!!", Toast.LENGTH_SHORT).show();
                    LoginActivity.loadingBar.dismiss();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                Toast.makeText(context, "Error! connecting with database", Toast.LENGTH_SHORT).show();
                LoginActivity.loadingBar.dismiss();
            }
        });
    }

    /**
     * By using this method new user will be created
     * @param RootRef is the reference of the database
     * @param context of the that activity
     * @param name,email,phone,password will be used for user info
     */
    public static void createNewUser(final DatabaseReference RootRef,final String name, final String phone, final String email, final String password, final Context context)
    {
        HashMap<String, Object> userdataMap = new HashMap<>();
        userdataMap.put("name", name);
        userdataMap.put("Phone", phone);
        userdataMap.put("email", email);
        userdataMap.put("password", password);
        RootRef.child("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .updateChildren(userdataMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {

                        if(task.isSuccessful())
                        {
                            Toast.makeText(context, "Registered successfully", Toast.LENGTH_SHORT).show();
                            RegisterActivity.loadingBar.dismiss();
                            Intent intent = new Intent(context, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            context.startActivity(intent);

                        }
                        else
                        {
                            Toast.makeText(context, "Error to create new User!!", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    /**
     * By using this method product will be shown into ui
     * @param ProductsRef is the reference of the database
     * @param context of the that activity
     * @param recyclerView will be used to show the products
     */
    public static void showProducts(final DatabaseReference ProductsRef, final Context context, final RecyclerView recyclerView)
    {
        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(ProductsRef, Products.class)
                        .build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model)
                    {
                        holder.txtproductName.setText(model.getPname2());
                        holder.txtProductPrice.setText("Price: " + model.getPrice() + " Taka");
                        Picasso.get().load(model.getImage()).into(holder.imageView);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(context, ProductDetailsActivity.class);
                                intent.putExtra("pid", model.getPid());
                                context.startActivity(intent);
                            }
                        });
                    }
                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.products_items_layout, parent, false);
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    /**
     * By using this method , user can search specific products
     * @param reference is the reference of the database
     * @param context of the that activity
     * @param recyclerView will be used to show the products
     */
    public static void searchByName(final DatabaseReference reference, final Context context, final RecyclerView recyclerView, final String searchInput)
    {
        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(reference.orderByChild("pname2").startAt(searchInput).endAt(searchInput + "\uf8ff"), Products.class)
                        .build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int i, @NonNull final Products model)
                    {
                        holder.txtproductName.setText(model.getPname2());
                       // holder.txtProductDescription.setText(model.getDescription());
                        holder.txtProductPrice.setText("Price: " + model.getPrice() + " Taka");
                        Picasso.get().load(model.getImage()).into(holder.imageView);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(context, ProductDetailsActivity.class);
                                intent.putExtra("pid", model.getPid());
                                context.startActivity(intent);
                            }
                        });
                    }
                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.products_items_layout, parent, false);
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    /**
     * By using this method user can confirm an order
     * @param ordersRef is the reference of the database
     * @param context of the that activity
     * @param orderMap will be used to save data into database
     */

    public static void orderConfirmation(final DatabaseReference ordersRef, final Context context, final HashMap<String, Object> orderMap, final Activity activity)
    {
        ordersRef.updateChildren(orderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                FirebaseDatabase.getInstance().getReference().child("Cart List")
                        .child("User View")
                        .child(Prevalent.currentonlineUser.getUserName())
                        .removeValue()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task)
                            {
                                if(task.isSuccessful())
                                {
                                    Toast.makeText(context, "Your final order has been placed.", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(context, HomeActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    context.startActivity(intent);
                                    activity.finish();
                                }
                            }
                        });
            }
        });
    }


    /**
     * By using this method user can search product by category
     * @param reference is the reference of the database
     * @param context of the that activity
     * @param recyclerView will be used to show the products
     * @param categoryPassing will be used to pass the category to show category specific products
     */
    public static void categoryWiseShowProduct(final DatabaseReference reference, final String categoryPassing,final Context context, final RecyclerView recyclerView)
    {
        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(reference.orderByChild("category").startAt(categoryPassing).endAt(categoryPassing), Products.class)
                        .build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int i, @NonNull final Products model)
                    {
                        holder.txtproductName.setText(model.getPname2());
                       // holder.txtProductDescription.setText(model.getDescription());
                        holder.txtProductPrice.setText("Price: " + model.getPrice() + " Taka");
                        Picasso.get().load(model.getImage()).into(holder.imageView);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(context, ProductDetailsActivity.class);
                                intent.putExtra("pid", model.getPid());
                                context.startActivity(intent);
                            }
                        });
                    }
                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.products_items_layout, parent, false);
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    /**
     * By using this method product will be shown into ui
     * @param ProductRef is the reference of the database
     * @param productId is the unique id of the product
     * @param productName is the name of the product
     * @param productPrice is the price of that product
     */
    public static void getProductsDetails(final DatabaseReference ProductRef , final String productId , final TextView productName, final TextView productPrice, final TextView productDescription, final ImageView productImage)
    {
        ProductRef.child(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    Products products = dataSnapshot.getValue(Products.class);
                    productName.setText(products.getPname2());
                    productPrice.setText(products.getPrice());
                    productPrice.setVisibility(View.INVISIBLE);
                    productDescription.setText(products.getDescription());
                    Picasso.get().load(products.getImage()).into(productImage);
                    String model3d = products.getModel3dUrl();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    /**
     * By using this method the state of ta products will be tested
     * @param ordersRef is the reference of the database
     * @param context of the that activity
     * @param txtTotalAmount is the total amount ordered by user
     * @param txtMsg1 is the state of the product, wheather it is shipped or not
     * @param nextPocessButton is the confirmation button
     */
    public static  void CheckOrderState(final DatabaseReference ordersRef, final Context context, final RecyclerView recyclerView, final TextView txtTotalAmount, final TextView txtMsg1, final Button nextPocessButton)
    {
        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    String shippingState = dataSnapshot.child("state").getValue().toString();
                    String userName = dataSnapshot.child("name").getValue().toString();

                    if(shippingState.equals("shipped"))
                    {
                        txtTotalAmount.setText("Dear " + userName + "\n Your order is shipped successfully");
                        recyclerView.setVisibility(View.GONE);

                        txtMsg1.setVisibility(View.VISIBLE);
                        txtMsg1.setText("Congratulations, Your final order has been shipped.");
                        nextPocessButton.setVisibility(View.GONE);

                        Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
                    }
                    else if(shippingState.equals("not shipped"))
                    {
                        txtTotalAmount.setText("Products Not Shipped Yet");
                        recyclerView.setVisibility(View.GONE);

                        txtMsg1.setVisibility(View.VISIBLE);
                        nextPocessButton.setVisibility(View.GONE);

                        Toast.makeText(context, "You can purchase more products after validation", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    /**
     * By using this method user can update his/her cart
     * @param cartListRef is the reference of the database
     * @param context of the that activity
     * @param recyclerView will be used to show the products of the cart
     * @param txtTotalAmount will be used to calculate total amount of the products in the cart
     */
    public static void updateCart(final DatabaseReference cartListRef, final Context context, final RecyclerView recyclerView, TextView txtTotalAmount)
    {
        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                        .setQuery(cartListRef.child("User View")
                                .child(Prevalent.currentonlineUser.getUserName())
                                .child("Products"), Cart.class)
                        .build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter
                = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {

            int totalPrice = 0;
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int i, @NonNull final Cart model)
            {
                holder.txtProductQuantity.setText("Quantity: " + model.getQuantity());
                holder.txtProductPrice.setText("Price: " + model.getPrice() + " Taka");
                holder.txtProductName.setText(model.getPname());

                int oneTypeProductTPrice = ((Integer.valueOf(model.getPrice()))) * Integer.valueOf(model.getQuantity());
                totalPrice+= oneTypeProductTPrice;
                txtTotalAmount.setText("Total Price: " + String.valueOf(totalPrice) + " Taka");
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        CharSequence options[] = new CharSequence[]
                                {
                                        "Edit",
                                        "Remove"
                                };
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Cart Options");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                if(i==0)
                                {
                                    Intent intent = new Intent(context, ProductDetailsActivity.class);
                                    intent.putExtra("pid", model.getPid());
                                    context.startActivity(intent);
                                }
                                if (i==1)//remove the product from the cart and also from the database
                                {
                                    cartListRef.child("User View")
                                            .child(Prevalent.currentonlineUser.getUserName())
                                            .child("Products")
                                            .child(model.getPid())
                                            .removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task)
                                                {
                                                    if(task.isSuccessful())
                                                    {
                                                        Toast.makeText(context, "Item removed!!", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(context, CartActivity.class);
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                        context.startActivity(intent);
                                                    }

                                                }
                                            });
                                }

                            }
                        });
                        builder.show();
                    }
                });
            }
            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout, parent, false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }


    /**
     * By using this method user can add products to the cart
     * @param cartListRef is the reference of the database
     * @param context of the that activity
     * @param cartMap will be used to fetch the details of the products and add it to database
     * @param productId is the unique id of the products which will be used to detect the product
     */
    public static void addProductToCart(final DatabaseReference cartListRef, final HashMap<String, Object> cartMap, final String productId, final Context context) {
        cartListRef.child("User View").child(Prevalent.currentonlineUser.getUserName())
                .child("Products").child(productId)
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            cartListRef.child("Admin View").child(Prevalent.currentonlineUser.getUserName())
                                    .child("Products").child(productId)
                                    .updateChildren(cartMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(context, "Product Added to Cart", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(context, HomeActivity.class);
                                                context.startActivity(intent);
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    /**
     * By using this method the details of the user will be displayed into ui
     * @param UserRef is the reference of the database
     * @param profileImageView is the image of the user
     * @param fullNameEditText is the name of the user if he/she wants to change name
     * @param userPhoneEditText is the phone number of the userif he/she wants to change phone number
     * @param addressEditText is the address to ship the products
     */
    public static void userInfoDisplay(final DatabaseReference UserRef, final ImageView profileImageView, final EditText fullNameEditText, final EditText userPhoneEditText, final EditText addressEditText)
    {
        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists())
                {
                    if(dataSnapshot.child("image").exists())
                    {
                        String image = dataSnapshot.child("image").getValue().toString();
                        String name = dataSnapshot.child("name").getValue().toString();
                        String phone = dataSnapshot.child("Phone").getValue().toString();
                        String address = dataSnapshot.child("address").getValue().toString();

                        Picasso.get().load(image).into(profileImageView);
                        fullNameEditText.setText(name);
                        userPhoneEditText.setText(phone);
                        addressEditText.setText(address);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    /**
     * By using this method user can upload image
     * @param fileRef is the reference of the firebaseStorage
     * @param context of the that activity
     * @param imageUri will be used to fetch the image saved into storage
     */
    public static void uploadImageToDatabase(final StorageReference fileRef,final Context context, final Uri imageUri, final Activity activity, final EditText fullNameEditText, final EditText addressEditText, final EditText userPhoneEditText)
    {
        final ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setTitle("Update Profile");
        progressDialog.setMessage("Please Wait, while we are updating your account information");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if(imageUri != null)
        {
            StorageTask uploadTask = fileRef.putFile(imageUri);

            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception
                {
                    if(!task.isSuccessful())
                    {
                        throw task.getException();
                    }
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task)
                {
                    if(task.isSuccessful())
                    {
                        Uri downloadUrl = task.getResult();
                        String myUrl = downloadUrl.toString();
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");
                        HashMap<String, Object> userMap = new HashMap<>();
                        userMap.put("name", fullNameEditText.getText().toString());
                        userMap.put("address", addressEditText.getText().toString());
                        userMap.put("Phone", userPhoneEditText.getText().toString());
                        userMap.put("image", myUrl);
                        ref.child(Prevalent.currentonlineUser.getUserName()).updateChildren(userMap);
                        progressDialog.dismiss();
                        context.startActivity(new Intent(context, HomeActivity.class));
                        Toast.makeText(context, "Profile info updated successfully!", Toast.LENGTH_SHORT).show();
                        activity.finish();
                    }
                    else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else {
            Toast.makeText(context, "image is not selected!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * By using this method user can update or change his/her info
     * @param reference is the reference of the database
     * @param context of the that activity
     * @param activity is the activity which used to update user info
     */
    public static  void updateUserInfo(final DatabaseReference reference, final Context context, final Activity activity, final EditText fullNameEditText, final EditText addressEditText, final EditText userPhoneEditText)
    {
        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("name", fullNameEditText.getText().toString());
        userMap.put("address", addressEditText.getText().toString());
        userMap.put("Phone", userPhoneEditText.getText().toString());
        reference.child(Prevalent.currentonlineUser.getUserName()).updateChildren(userMap);

        context.startActivity(new Intent(context, HomeActivity.class));
        Toast.makeText(context, "Profile info updated successfully!", Toast.LENGTH_SHORT).show();
        activity.finish();
    }
    public static String checkOrderState(final DatabaseReference ordersRef)
    {
        ordersRef.addValueEventListener(new ValueEventListener() {
            String localState = "Normal";
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    String shippingState = dataSnapshot.child("state").getValue().toString();

                    if(shippingState.equals("shipped"))
                    {
                        localState = "Order Shipped";
                    }
                    else if(shippingState.equals("not shipped"))
                    {
                        localState = "Order Placed";
                    }
                }
               state = localState;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
        return state;
    }

    /**
     * By using this method user can see the products through augmented reality
     * @param ProductRef is the reference of the database
     * @param context of the that activity
     * @param productId is the unique id of the products which will be used to detect the product
     */
    public static void goToARCameraActivityClass(final DatabaseReference ProductRef, final String productId, final Context context)
    {
        ProductRef.child(productId).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    if(dataSnapshot.child("model3dUrl").exists())
                    {
                        String model = dataSnapshot.child("model3dUrl").getValue().toString();

                        Intent intent = new Intent(context, ArCameraActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("Model", model);

                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    /**
     * By using this method the shipment details wii be saved
     * @param UserRef is the reference of the database
     * @param nameEditText is the name of the user saved into database
     * @param phoneEditText is the phone number of the user or he/she also can change the number
     * @param addressEditText is the address of the user or he/she can also change the address
     */
    public static void userInfoDisplayForShipment(final DatabaseReference UserRef ,final EditText nameEditText, final EditText phoneEditText, final EditText addressEditText)
    {
        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    String name = dataSnapshot.child("name").getValue().toString();
                    nameEditText.setText(name);
                    if(!dataSnapshot.child("address").exists())
                    {
                        String address = "Enter your address";
                        addressEditText.setText(address);
                    }
                    else if(dataSnapshot.child("address").exists())
                    {
                        String address = dataSnapshot.child("address").getValue().toString();
                        addressEditText.setText(address);
                    }
                    if(!dataSnapshot.child("Phone").exists())
                    {
                        String phone = "Enter your Phone Number";
                        phoneEditText.setText(phone);
                    }
                    else if(dataSnapshot.child("address").exists())
                    {
                        String phone = dataSnapshot.child("Phone").getValue().toString();
                        phoneEditText.setText(phone);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }
}


