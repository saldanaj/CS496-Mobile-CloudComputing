package com.example.jq.assignment4_androidui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


public class DisplayMessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        // String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        // Create the text view to display the message
        TextView textView = new TextView(this);
        textView.setTextSize(40);
        // textView.setText(message);
        setContentView(textView);

        setContentView(R.layout.activity_display_message);
    }
}
