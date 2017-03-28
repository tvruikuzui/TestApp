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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity {

    EditText edtTheItem;
    ListView lstItems;
    List<String> mItems;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        edtTheItem = (EditText) findViewById(R.id.edtTheItem);
        lstItems = (ListView) findViewById(R.id.lstItems);

        mItems = new ArrayList<>();

        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,mItems);

        lstItems.setAdapter(adapter);

    }

    public void btnAddToList(View view) {
        String item = edtTheItem.getText().toString();
        mItems.add(item);
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
                            mItems.add(json.getString(i));
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
                JSONArray jsonArray = new JSONArray(mItems);
                OutputStream outputStream = null;
                try {
                    outputStream = openFileOutput("liran",MODE_PRIVATE);
                    outputStream.write(jsonArray.toString().getBytes());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
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
