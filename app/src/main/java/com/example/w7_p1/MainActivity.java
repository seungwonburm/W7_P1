package com.example.w7_p1;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DatabaseManager theDb;

    TextView result;
    Button delete, add, update, search, clear;
    EditText first, last, email;
    String var0=null, var1, var2, var3 = null;
    Cursor cursor;
    AutoCompleteTextView acc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        theDb = new DatabaseManager(this);
        search = (Button) findViewById(R.id.search);
        result = (TextView) findViewById(R.id.result);
        delete = (Button) findViewById(R.id.delete);
        add = (Button) findViewById(R.id.add);
        update = (Button) findViewById(R.id.update);
        first = (EditText) findViewById(R.id.first);
        last = (EditText) findViewById(R.id.last);
        email = (EditText) findViewById(R.id.email);
        acc = (AutoCompleteTextView) findViewById(R.id.acc);
        clear = (Button) findViewById(R.id.clear);

        Add();
        Search();
        Delete();
        Update();

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                first.getText().clear();
                last.getText().clear();
                email.getText().clear();
            }
        });

    }

    public void Add(){
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean added = false;
                if (!first.getText().toString().equals("") && !last.getText().toString().equals("") && !email.getText().toString().equals("")){
                    int result =theDb.verify(email.getText().toString());
                    if (result == -1){
                        added = false;
                        Toast.makeText(MainActivity.this, "Email Already Exists", Toast.LENGTH_LONG).show();

                    } else if (result == -2){
                        Toast.makeText(MainActivity.this, "Invalid Email Format", Toast.LENGTH_LONG).show();
                    }

                    else {
                        added = theDb.insert(first.getText().toString(), last.getText().toString(), email.getText().toString());
                    }


                    if (added == true){
                        String[] list;
                        list = theDb.autoComplete();
                        ArrayAdapter<String> autoComplete = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, list);
                        acc.setAdapter(autoComplete);
                        first.getText().clear();
                        last.getText().clear();
                        email.getText().clear();
                        Toast.makeText(MainActivity.this, "Data Added", Toast.LENGTH_LONG).show();
                    }
                    else
                        Toast.makeText(MainActivity.this, "Data Not Added", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, "Please Enter Required Information!", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    public void Delete(){
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (var0 != null){
                    theDb.delete(var0);
                    result.setText("Result");

                    String[] list;
                    list = theDb.autoComplete();
                    ArrayAdapter<String> autoComplete = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, list);
                    acc.setAdapter(autoComplete);

                    Toast.makeText(MainActivity.this, "Data Deleted!", Toast.LENGTH_LONG).show();


                }else{
                    Toast.makeText(MainActivity.this, "Deletion Failed!", Toast.LENGTH_LONG).show();
                }


            }
        });
    }

    public void Update(){
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                int verify =theDb.verify(email.getText().toString());
                if (verify ==1 || (verify== -1 && email.getText().toString().equals(var3))){
                    cursor = theDb.update(var0, first.getText().toString(), last.getText().toString(), email.getText().toString());
                    if (var0 != null && cursor.moveToFirst() && cursor != null){

                        do{
                            var0 = cursor.getString(cursor.getColumnIndexOrThrow("id"));
                            var1 = cursor.getString(cursor.getColumnIndexOrThrow("first"));
                            var2 = cursor.getString(cursor.getColumnIndexOrThrow("last"));
                            var3 = cursor.getString(cursor.getColumnIndexOrThrow("email"));
                            String combined = var1 + " " + var2 + "\n" + var3;

                            result.setText(combined + "");
                        } while (cursor.moveToNext());

                        String[] list;
                        list = theDb.autoComplete();
                        ArrayAdapter<String> autoComplete = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, list);
                        acc.setAdapter(autoComplete);
                        Toast.makeText(MainActivity.this, "Data Updated!", Toast.LENGTH_LONG).show();
                        first.getText().clear();
                        last.getText().clear();
                        email.getText().clear();
                    }

                }
                else if (verify == -1){
                    Toast.makeText(MainActivity.this, "Email Already Exists!", Toast.LENGTH_LONG).show();
                }

                else if (verify == -2){
                    Toast.makeText(MainActivity.this, "Invalid Email Format!", Toast.LENGTH_LONG).show();
                } else{
                    Toast.makeText(MainActivity.this, "Update Failed!", Toast.LENGTH_LONG).show();
                }






            }
        });
    }

    public void Search(){
        String[] list;
        list = theDb.autoComplete();
        ArrayAdapter<String> autoComplete = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, list);
        acc.setAdapter(autoComplete);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cursor =theDb.search(acc.getText().toString());
                if (cursor.getCount()<=0){
                    Toast.makeText(MainActivity.this, "NO DATA!!", Toast.LENGTH_LONG).show();
                }
                else if (cursor.moveToFirst() && cursor != null) {


                    do{
                        var0 = cursor.getString(cursor.getColumnIndexOrThrow("id"));
                        var1 = cursor.getString(cursor.getColumnIndexOrThrow("first"));
                        var2 = cursor.getString(cursor.getColumnIndexOrThrow("last"));
                        var3 = cursor.getString(cursor.getColumnIndexOrThrow("email"));
                        String combined = var1 + " " + var2 + "\n" + var3;

                        result.setText(combined + "");
                    } while (cursor.moveToNext());

                    first.setText(var1);
                    last.setText(var2);
                    email.setText(var3);


                } else if (cursor == null){
                    Toast.makeText(MainActivity.this, "NO DATA!!", Toast.LENGTH_LONG).show();
                }
                cursor.close();

            }
        });


    }


//    public void Search2(){
//        sv. setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String s) {
//                cursor =theDb.search(s);
//                if (cursor.getCount()<=0){
//                    Toast.makeText(MainActivity.this, "NO DATA!!", Toast.LENGTH_LONG).show();
//                }
//                else if (cursor.moveToFirst() && cursor != null) {
//
//
//                        do{
//                            var0 = cursor.getString(cursor.getColumnIndexOrThrow("id"));
//                            var1 = cursor.getString(cursor.getColumnIndexOrThrow("first"));
//                            var2 = cursor.getString(cursor.getColumnIndexOrThrow("last"));
//                            var3 = cursor.getString(cursor.getColumnIndexOrThrow("email"));
//                            String combined = var1 + " " + var2 + "\n" + var3;
//
//                            result.setText(combined + "");
//                        } while (cursor.moveToNext());
//
//
//
//
//
//                } else if (cursor == null){
//                   Toast.makeText(MainActivity.this, "NO DATA!!", Toast.LENGTH_LONG).show();
//               }
//               cursor.close();
//
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String s) {
//                return false;
//            }
//        });
//
//    }
}