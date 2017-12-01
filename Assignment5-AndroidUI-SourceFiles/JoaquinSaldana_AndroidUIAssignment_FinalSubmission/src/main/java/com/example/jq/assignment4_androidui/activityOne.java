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

/*
In this activity, the view is setup as a linear layout.  When the user hits the
change number button, the number increments and continues to increment until the user
hits the back button or the "close" button.

This is the LINEAR LAYOUT - HORIZONTAL

 */


public class activityOne extends AppCompatActivity
{

    final List<Integer> numberList = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one);

        // Casting of the XML entities/properties created for this class
        Button btnClose = (Button) findViewById(R.id.btnClose);
        Button changeNumWord = (Button) findViewById(R.id.addNumber);
        final TextView textView1 = (TextView) findViewById(R.id.addTextHoriz);


        btnClose.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View arg0)
            {
                // close activity1
                finish();
            }

        }); // end of setOnClickListener


        changeNumWord.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view)
            {
                numberList.add(numberList.size());

                // Integer result = numberList.size();

                // textView1.setText(result.toString());

                for(int i = 0; i < numberList.size(); i++)
                {
                    Integer result = numberList.get(i);
                    textView1.setText(result.toString());
                }

            }

        }); // end of setOnClickListerner


    } // end of the onCreate method


} // end of activityOne class
