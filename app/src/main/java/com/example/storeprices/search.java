package com.example.storeprices;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.textfield.TextInputEditText;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class search extends AppCompatActivity {
    private static final String TAG = "search";
    private final  String FILENAME = "data.txt";
    private static List<Item> items;

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        recyclerView = findViewById(R.id.recyclerView);

        items = ReadFromFile();

        setUpRecyclerView(items);
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

    private void setUpRecyclerView(List<Item> itemList) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MyAdapter(getApplicationContext(), itemList));
    }

    public void onSearchButtonClick(View view) {
        SearchItem();
    }

    public void SearchItem() {
        TextInputEditText inputField = findViewById(R.id.search_input);
        String searchText = inputField.getText().toString();

        List<Item> searchResults = new ArrayList<>();
        for (Item item : items) {
            // Convert item name to lowercase for case-insensitive search
            String itemName = item.getName().toLowerCase();

            // Check if the item name contains the search text
            if (itemName.contains(searchText)) {
                searchResults.add(item);
            }
        }

        // Update RecyclerView with search results
        setUpRecyclerView(searchResults);
    }


}