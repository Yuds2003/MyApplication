package com.example.michael.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.ResponseHandlerInterface;

import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.SortedMap;

import cz.msebera.android.httpclient.Header;

/**
 * Created by michael on 1/14/18.
 */

public class LEDActivity extends AppCompatActivity {
    private Map<String, Action> actionMap = new LinkedHashMap<>();
    private AsyncHttpClient httpClient = new AsyncHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_led);

        actionMap.clear();

        actionMap.put("Get LED Status", new Action("GET" ,"/leds"));
        actionMap.put("Get Red LED Status", new Action("GET", "/led/1"));
        actionMap.put("Get Green LED Status", new Action("GET", "/led/2"));
        actionMap.put("Get Blue LED Status", new Action("GET", "/led/3"));
        actionMap.put("Turn Red LED On", new Action("PUT", "/led/1", "state=on"));
        actionMap.put("Turn Green LED On", new Action("PUT", "/led/2", "state=on"));
        actionMap.put("Turn Blue LED On", new Action("PUT", "/led/3", "state=on"));
        actionMap.put("Turn Red LED Off", new Action("PUT", "/led/1", "state=off"));
        actionMap.put("Turn Green LED Off", new Action("PUT", "/led/2", "state=off"));
        actionMap.put("Turn Blue LED Off", new Action("PUT", "/led/3", "state=off"));

        Spinner spinner = (Spinner)findViewById(R.id.spinner2);
        String[] spinnerEntries = actionMap.keySet().toArray(new String[actionMap.size()]);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerEntries);
        spinner.setAdapter(adapter);
    }

    public void onButtonClick(View view) {
        Spinner spinner = (Spinner)findViewById(R.id.spinner2);
        String currentItem = spinner.getSelectedItem().toString();
        Action currentAction = actionMap.get(currentItem);

        if (currentAction == null) {
            alert("no action found!", this);
            return;
        }

        final EditText txtBaseUri = findViewById(R.id.txtBaseUri);
        final String baseUri = txtBaseUri.getText().toString();
        final TextView tv = (TextView)findViewById(R.id.textView2);
        final String uri = String.format("%s%s", baseUri, currentAction.toString());
        tv.setText(String.format("%s %s", currentAction.getMethod(), uri));

        AsyncHttpResponseHandler responder = new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                tv.append("\n\n");
                tv.append("success!\n");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                tv.append("\n\n");
                tv.append("failure!\n");
                if (responseBody != null) {
                    tv.append(String.valueOf(responseBody));
                }
                tv.append(error.getMessage());
            }
        };

        httpClient.setConnectTimeout(2);
        httpClient.setTimeout(2);
        if (currentAction.getMethod().equals("GET")) {
            httpClient.get(uri, responder);
        } else if (currentAction.getMethod().equals("PUT")) {
            httpClient.put(uri, responder);
        }
    }

    private void alert(String message, Context ctx) {
        AlertDialog alertDialog = new AlertDialog.Builder(ctx).create();
        alertDialog.setTitle("Alert!");
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                });

        alertDialog.show();
    }

    private class Action {
        private String method;
        private String url;
        private String param;

        public Action(String method, String url) {
            this(method, url, null);
        }
        public Action(String method, String url, String param) {
            this.method = method;
            this.url = url;
            this.param = param;
        }

        public String getMethod() {
            return this.method;
        }

        public String getUrl() {
            return this.url;
        }

        public String getParam() {
            return this.param;
        }

        public String toString() {
            StringBuffer sb = new StringBuffer();
            sb.append("/leds/api/v1");
            sb.append(url);
            if (param != null) {
                sb.append("?");
                sb.append(param);
            }

            return sb.toString();
        }
    }
}
