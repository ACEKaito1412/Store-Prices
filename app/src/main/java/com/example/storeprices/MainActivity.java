package com.example.storeprices;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

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


    public void openDirectory(View view) {
        // Choose a directory using the system's file picker.
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("text/plain");

        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == 1
                && resultCode == Activity.RESULT_OK) {
            // The result data contains a URI for the document or directory that
            // the user selected.
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                // Perform operations on the document using its URI.
                try {
                    InputStream inputStream = getContentResolver().openInputStream(uri);

                    if (inputStream != null) {
                        Log.d(tag, "onActivityResult: on read file");
                        ReadFromFile(inputStream);
                        inputStream.close();
                    } else {
                        // Handle the case where inputStream is null
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }

    public void AddClick(View view){
        Intent intent = new Intent(this, Add_Activity.class);
        MainActivity.this.startActivity(intent);
    }


    private void createInternalFile(){
        // file name;
        String filename = "data.txt";

        File file = new File(getFilesDir(), filename);

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

        Log.d(tag, "createInternalFile: " + file.getAbsolutePath());
    }

    private void ReadFromFile(InputStream inputStream){
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                Log.d(tag, "ReadFromFile: " + line);
                if(parts.length >= 3) {
                    String name = parts[0].trim();
                    String price = parts[1].trim();
                    String code = parts[2].trim();
                    String date = parts[3].trim();

                    String lines = name + ", " + price + ", " + code + ", " + date;
                    WriteToFile(lines);
                }
            }
            reader.close();
        } catch (Exception e) {
            Log.e("MainActivity", "Error reading from file", e);
        }
    }

    private void WriteToFile(String data){
        File getDirectory = getExternalFilesDir(null);

        // file name;
        String filename = "data.txt";

        File file = new File(getDirectory, filename);
        Log.d(tag, "WriteToFile: " + data);
        try {
            FileOutputStream fos = new FileOutputStream(file, true);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fos);
            BufferedWriter writer = new BufferedWriter(outputStreamWriter);
            writer.write(data);
            writer.newLine();
            writer.close();
            fos.close();
            outputStreamWriter.close();
        }catch (Exception e){
            Log.d(tag, "WriteToFile: Error Writing To File", e);
        }
    }



}