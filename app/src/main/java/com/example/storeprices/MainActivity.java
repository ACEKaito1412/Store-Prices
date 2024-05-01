package com.example.storeprices;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    Button searchBtn, updateBtn, deleteBtn, addBtn;
    private String tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tag = "MainActivity";
        

        searchBtn = findViewById(R.id.searchBtn);
        updateBtn = findViewById(R.id.updateBtn);
        addBtn = findViewById(R.id.addBtn);


        createInternalFile();
    }


    public void SearchClick(View view){
        Intent intent = new Intent(MainActivity.this, search.class);
        MainActivity.this.startActivity(intent);
    }

    public void UpdateClick(View view){
        Intent intent = new Intent(this, Update_Activity.class);
        MainActivity.this.startActivity(intent);
    }



    public void AddClick(View view){
        Intent intent = new Intent(this, Add_Activity.class);
        MainActivity.this.startActivity(intent);
    }


    private void createInternalFile(){
        File getDirectory = getExternalFilesDir(null);

        // file name;
        String filename = "data.txt";

        File file = new File(getDirectory, filename);

        try {
            if (!file.exists()) {
                boolean created = file.createNewFile();
                if (created) {
                    Log.d("MainActivity", "File created successfully in internal storage");
                } else {
                    Log.e("MainActivity", "Failed to create file in internal storage");
                }
            } else {
                Log.d("MainActivity", "File already exists in internal storage");
            }
        }catch (Exception e){
            Log.e(tag, "Exception Occured", e);
        }


    }


}