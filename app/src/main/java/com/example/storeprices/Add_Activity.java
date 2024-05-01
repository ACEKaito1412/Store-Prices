package com.example.storeprices;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.google.android.material.textfield.TextInputEditText;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Add_Activity extends AppCompatActivity {
    private static final String TAG = "Add_Activity";

    private String FILENAME = "data.txt";
    TextInputEditText inputItemName, inputItemPrice, inputItemCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        inputItemCode = findViewById(R.id.inputCode);
        inputItemName = findViewById(R.id.inputItemName);
        inputItemPrice = findViewById(R.id.inputPrice);

    }

    public void SaveClick(View view){
        Calendar cal = Calendar.getInstance();

        String itemName = inputItemName.getText().toString();
        String itemCode = inputItemCode.getText().toString();
        String itemPrice = inputItemPrice.getText().toString();
        String itemDate = cal.get(Calendar.DATE) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR);

        if(!itemName.equals("")){
            String data = itemName + ", " + itemPrice + ", " + itemCode + ", " + itemDate;
            Log.d(TAG, "SaveClick: " + data);
            WriteToFile(data);
        }else{
            showMessage(this, "Error", "Some fields doesn't have values.");
        }
    }

    private void WriteToFile(String data){

        File getDirectory = getExternalFilesDir(null);

        // file name;
        String filename = "data.txt";

        File file = new File(getDirectory, FILENAME);

        try {
            FileOutputStream fos = new FileOutputStream(file, true);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fos);
            BufferedWriter writer = new BufferedWriter(outputStreamWriter);
            writer.write(data);
            writer.newLine();
            writer.close();
            finish();
        }catch (Exception e){
            Log.d(TAG, "WriteToFile: Error Writing To File" + getDirectory, e);
        }
    }

    public static void showMessage(Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Positive button clicked, do something if needed
                        dialog.dismiss(); // Close the dialog
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}