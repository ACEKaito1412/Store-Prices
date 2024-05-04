package com.example.storeprices;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Update_Activity extends AppCompatActivity {

    private static final String FILENAME = "data.txt";
    private List<Item> items = new ArrayList<>();
    TextInputEditText itemName, itemPrice, itemCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        Intent intent = getIntent();
        String value = intent.getStringExtra("item");

        itemName = findViewById(R.id.inputItemName);
        itemPrice = findViewById(R.id.inputPrice);
        itemCode = findViewById(R.id.inputCode);

        items = ReadFromFile();

        if(value != null){
            SearchItem(value);
            TextInputEditText inputField = findViewById(R.id.search_input);
            inputField.setText(value);
        }


    }

    private List<Item> ReadFromFile(){
        List<Item> items = new ArrayList<>();
        try {

            File getDirectory = getExternalFilesDir(null);

            // file name;
            String filename = "data.txt";

            File file = new File(getDirectory, filename);

            FileInputStream fis = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fis);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = reader.readLine()) != null) {
                // Split the line based on commas
                String[] parts = line.split(", ");

                // Extract the individual components
                if (parts.length >= 3) { // Ensure that there are at least three components
                    String name = parts[0].trim();
                    String price = parts[1].trim();
                    String code = parts[2].trim();
                    String date = parts[3].trim();

                    // Create a new Item object and add it to the list
                    Item item = new Item(name, code, price, date);
                    items.add(item);
                }
            }
            reader.close();
        } catch (Exception e) {
            Log.e("MainActivity", "Error reading from file", e);
            Item item = new Item("No Item Found","" ,"", "");
            items.add(item);
            return items;
        }
        return items;
    }

    public void onSearchButtonClick(View view) {
        TextInputEditText inputField = findViewById(R.id.search_input);
        SearchItem(inputField.getText().toString());
    }

    public void SearchItem(String searchText) {
        Item searchItem = new Item("Item not found.", "", "", "");
        for (Item item : items) {
            if (searchText.equalsIgnoreCase(item.getName())) {
                searchItem = item;
            }
        }

        itemName.setText(searchItem.getName());
        itemPrice.setText(searchItem.getPrice());
        itemCode.setText(searchItem.getCode());
    }


    public void onUpdateBtnClick(View view){
        Calendar cal = Calendar.getInstance();
        String date = cal.get(Calendar.DATE) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR);
        String name = itemName.getText().toString();
        String price = itemPrice.getText().toString();
        String code = itemCode.getText().toString();

        Item updated_item = new Item(name, code, price, date);

        String data = name + ";" + price + ":" + code;

        Log.d("Btn Click" , data);
        writeToFile(updated_item);
        finish();
    }

    public void writeToFile(Item updated_item){
        List<String> stringList = new ArrayList<>();
        for (Item item: items){
            if(updated_item.getName().equals(item.getName())){
                String data = updated_item.getName() + ", " + updated_item.getPrice() + ", " + updated_item.getCode() + ", " + updated_item.getDate();
                stringList.add(data);
            }else{
                String data = item.getName() + ", " + item.getPrice() + ", " + item.getCode() + ", " + item.getDate();
                stringList.add(data);
            }
        }

        File getDirectory = getExternalFilesDir(null);

        // file name;
        String filename = "data.txt";

        File file = new File(getDirectory, filename);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for(String line: stringList){
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}