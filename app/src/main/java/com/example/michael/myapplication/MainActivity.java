package com.example.michael.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.michael.myapplication.EXTRA_MESSAGE";

    public ArrayList<String> myList;
    public ArrayAdapter<String> myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ListView lv = (ListView) findViewById(R.id.listView);
        this.myList = new ArrayList<>();
        this.myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, myList);

        lv.setAdapter(myAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                /*
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("Clicked!");
                alertDialog.setMessage(String.format("clicked: position=%d, id=%d",position, id));
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                dialog.dismiss();
                            }
                        });

                alertDialog.show();
                */
                final String message = myList.get(position);
                showDisplayActivity(message);

            }
        });
    }

    public void sendMessage(View view) {
        EditText editText = findViewById(R.id.editText);
        String message = editText.getText().toString();

        CheckBox checkBox = (CheckBox)findViewById(R.id.chkShowDialog);
        if (checkBox.isChecked()) {
            showDisplayActivity(message);
        }

        else {
            myAdapter.add(message);
            myAdapter.notifyDataSetChanged();
        }

        editText.setText("");
    }

    /*
    private void showDisplayActivity(String message) {
        Intent intent = new Intent(this, DisplayMainActivity.class);

        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
    */
    private void showDisplayActivity(final String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Clicked!");
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                        myAdapter.add(message);
                        myAdapter.notifyDataSetChanged();
                    }
                });

        alertDialog.show();
    }

    public void toggleButtonMessage(View view) {
        CheckBox checkBox = (CheckBox)findViewById(R.id.chkShowDialog);
        Button b = (Button)findViewById(R.id.button);

        if (checkBox.isChecked()) {
            b.setText("Show!");
        }

        else {
            b.setText("Add!");
        }

    }

    public void sortList(View view) {
        myAdapter.sort(new Comparator<String>() {
            @Override
            public int compare(String s, String t1) {
                return s.compareTo(t1);
            }
        });

        myAdapter.notifyDataSetChanged();
    }

    public void reverseList(View view) {
        Collections.reverse(myList);
        myAdapter.notifyDataSetChanged();
    }

    public void clearList(View view) {
        myAdapter.clear();
    }
}
