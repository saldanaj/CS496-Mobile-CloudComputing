package com.example.jq.assignment4_androidui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class activityTwo extends AppCompatActivity
{

    final List<String> stringList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);


        // Casting of the XML entities/properties created for this class
        Button btnClose2 = (Button) findViewById(R.id.btnClose2);
        final TextView text = (TextView) findViewById(R.id.addTextVert);
        Button addWord = (Button) findViewById(R.id.addString);
        final EditText edit = (EditText) findViewById(R.id.addStringBox);


        btnClose2.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View arg0)
            {
                // close activity1
                finish();
            }

        }); // end of setOnClickListener


        addWord.setOnClickListener(new View.OnClickListener(){

            public void onClick(View arg0)
            {

                String result = edit.getText().toString();
                stringList.add(stringList.size(), result);
                text.setText(result);

            }
        }); // end if the OnClickListener method for the add word/string button

    } // end of OnCreate method

} // end of activityTwo class
