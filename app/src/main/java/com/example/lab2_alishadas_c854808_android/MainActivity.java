package com.example.lab2_alishadas_c854808_android;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.example.lab2_alishadas_c854808_android.model.Product;
import com.example.lab2_alishadas_c854808_android.utils.DatabaseHel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {



    DatabaseHel sqLiteDatabase;

    List<Product> productList;
    ListView productsListView;
    List<Product> tempProductList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        productList = new ArrayList<>();
        tempProductList = new ArrayList<>();
        sqLiteDatabase = new DatabaseHel(this);

        productsListView = findViewById(R.id.lv_products);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
                View view2 = layoutInflater.inflate(R.layout.dialog_add_products, null);
                builder.setView(view2);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

                final EditText etName = view2.findViewById(R.id.et_name);
                final EditText etPrice = view2.findViewById(R.id.et_price);
                final EditText etDesc = view2.findViewById(R.id.et_description);

                view2.findViewById(R.id.btn_add_employee).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = etName.getText().toString().trim();
                        String price = etPrice.getText().toString().trim();
                        String desc = etDesc.getText().toString();

                        if (name.isEmpty()) {
                            etName.setError("name field cannot be empty");
                            etName.requestFocus();
                            return;
                        }

                        if (desc.isEmpty()) {
                            etDesc.setError("name field cannot be empty");
                            etDesc.requestFocus();
                            return;
                        }

                        if (price.isEmpty()) {
                            etPrice.setError("Price cannot be empty");
                            etPrice.requestFocus();
                            return;
                        }

                        if (sqLiteDatabase.addProduct(name, desc, Double.parseDouble(price)))
                            loadProducts();
                        alertDialog.dismiss();
                    }
                });
            }
        });

        loadTempProducts();
        loadProducts();
    }

    private void loadTempProducts() {
        tempProductList.add(new Product(1, "Apple Watch","Apple Watch is a wearable smartwatch that allows users to accomplish a variety of tasks, including making phone call.",700.0));
        tempProductList.add(new Product(2, "Iphone","Iphone allows users to accomplish a variety of tasks, such as making phone cal.l",1500.0));
        tempProductList.add(new Product(3, "laptop","Laptop allows users to accomplish a variety of tasks.",2000.0));
        tempProductList.add(new Product(4, "Camera","Helps the user to capture high pxl photos",3000.0));
        tempProductList.add(new Product(5, "Bag","Convienet product that helps to carry heavy things.",100.0));
        tempProductList.add(new Product(6, "Sport Shoes","Best for exercising or recreational Activity.",150.0));
        tempProductList.add(new Product(7, "Hiking pole","Assist walker with their rhythm.",50.0));
        tempProductList.add(new Product(8, "Waterproof Jacket","Garment for the rainy season.",80.0));
        tempProductList.add(new Product(9, "Water bottle","Convinent for carry water and help the person to be hydradeS",20.0));
        tempProductList.add(new Product(10, "First aid Kit","Medical kit for the emergency situation.",160.0));

    }

    private void loadProducts() {
        Cursor cursor = sqLiteDatabase.getAllProducts();
        if (cursor.moveToFirst()) {
            do {
                // create an employee instance
                productList.add(new Product(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getDouble(3)
                ));
            } while (cursor.moveToNext());
            cursor.close();
        }

        if (productList.containsAll(tempProductList)){
            return;
        }else{
            productList.addAll(tempProductList);
        }

        // create an adapter to display the employees
        ProductAdapter productAdapter = new ProductAdapter(this, R.layout.list_layout, productList);
        productsListView.setAdapter(productAdapter);
    }
}