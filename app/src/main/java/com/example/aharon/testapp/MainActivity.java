package com.example.aharon.testapp;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends Activity {

    Button btnPlus;
    Button btnMinus;
    Button btnMulti;
    Button btnDevide;
    EditText edtFirstNum;
    EditText edtsecNum;
    TextView txtRes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnPlus = (Button) findViewById(R.id.btnPlus);
        btnMinus = (Button) findViewById(R.id.btnMinus);
        btnMulti = (Button) findViewById(R.id.btnMulti);
        btnDevide = (Button) findViewById(R.id.btnDevide);
        edtFirstNum = (EditText) findViewById(R.id.edtFirstNum);
        edtsecNum = (EditText) findViewById(R.id.edtSecNum);
        txtRes = (TextView) findViewById(R.id.txtRes);
    }


    public void btnCalculate(View view) {
        btnDevide.setEnabled(false);
        btnPlus.setEnabled(false);
        btnMinus.setEnabled(false);
        btnMulti.setEnabled(false);
        String num1 = edtFirstNum.getText().toString();
        String num2 = edtsecNum.getText().toString();
        String operator = view.getTag().toString();

        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                InputStream inputStream = null;
                OutputStream outputStream = null;
                HttpURLConnection urlConnection = null;
                JSONObject jsonRequest = null;
                String res = null;
                try {
                    jsonRequest = new JSONObject();
                    jsonRequest.put("num1", params[0]);
                    jsonRequest.put("num2", params[1]);
                    jsonRequest.put("operator", params[2]);
                    URL url = new URL("http://10.0.2.2:8080/CalculatorServlet");
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setUseCaches(false);
                    urlConnection.setDoOutput(true);
                    urlConnection.connect();
                    outputStream = urlConnection.getOutputStream();
                    outputStream.write(jsonRequest.toString().getBytes());

                    inputStream = urlConnection.getInputStream();
                    byte[] buffer = new byte[128];
                    int actually;
                    actually = inputStream.read(buffer);
                    if (actually != -1){
                        JSONObject jsonResponse = new JSONObject(new String(buffer,
                                0,actually));
                        res = jsonResponse.getString("res");
                        return res;
                    }
                    urlConnection.disconnect();
                    inputStream.close();
                    outputStream.close();
                    inputStream = null;
                    outputStream = null;



                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return "error";
            }

            @Override
            protected void onPostExecute(String string) {
                btnDevide.setEnabled(true);
                btnPlus.setEnabled(true);
                btnMulti.setEnabled(true);
                btnMinus.setEnabled(true);
                txtRes.setText(string);
                edtFirstNum.setText("");
                edtsecNum.setText("");
            }
        }.execute(num1,num2,operator);

    }

    public void btnGoToSecAct(View view) {
        Intent i = new Intent(this,Main2Activity.class);
        startActivity(i);
    }
}
