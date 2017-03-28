package com.example.aharon.testapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity {

    EditText edtTheItem;
    EditText edtDesc;
    EditText edtPrice;
    ListView lstItems;
    //List<String> mItems;
    //ArrayAdapter<String> adapter;
    List<Expence> mItems;
    ExpenceArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        edtTheItem = (EditText) findViewById(R.id.edtTheItem);
        edtDesc = (EditText) findViewById(R.id.edtTheItemDesc);
        edtPrice = (EditText) findViewById(R.id.edtTheItemPrice);
        lstItems = (ListView) findViewById(R.id.lstItems);

        //mItems = new ArrayList<>();
        mItems = new ArrayList<>();

        //adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,mItems);
        adapter = new ExpenceArrayAdapter(this,mItems);

        lstItems.setAdapter(adapter);

    }

    public void btnAddToList(View view) {
        String itemName = edtTheItem.getText().toString();
        String itemDesc = edtDesc.getText().toString();
        double itemPrice = Double.valueOf(edtPrice.getText().toString());
        mItems.add(new Expence(itemName,itemDesc,itemPrice));
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(this, "sec act Started", Toast.LENGTH_SHORT).show();
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                JSONArray json = null;
                InputStream input = null;
                StringBuilder stringBuilder = new StringBuilder();
                try {
                    input = openFileInput("liran");
                    int actually;
                    byte[] buffer = new byte[128];
                    while ((actually = input.read(buffer)) != -1)
                        stringBuilder.append(new String(buffer,0,actually));
                    json = new JSONArray(stringBuilder.toString());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {
                    if (input != null)
                        try {
                            input.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                }
                if (json != null){
                    for (int i = 0; i < json.length(); i++) {
                        try {
                            JSONObject jsonExpnce = json.getJSONObject(i);
                            Expence expense = new Expence(
                                    jsonExpnce.getString("name"),
                                    jsonExpnce.getString("description"),
                                    jsonExpnce.getDouble("price"));
                            mItems.add(expense);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                adapter.notifyDataSetChanged();
            }
        }.execute();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Toast.makeText(this, "sec act Stopped", Toast.LENGTH_SHORT).show();
        Thread save = new Thread(new Runnable() {
            @Override
            public void run() {
                OutputStream outputStream = null;
                JSONArray jsonArray = new JSONArray();
                try {
                    for (Expence expence : mItems) {
                        JSONObject jsonExpense = new JSONObject();
                        jsonExpense.put("name", expence.getName());
                        jsonExpense.put("description", expence.getDesc());
                        jsonExpense.put("price", expence.getPrice());
                        jsonArray.put(jsonExpense);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    outputStream = openFileOutput("liran", MODE_PRIVATE);
                    outputStream.write(jsonArray.toString().getBytes());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (outputStream != null)
                        try {
                            outputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                }
            }
        });
        save.start();
    }
}
