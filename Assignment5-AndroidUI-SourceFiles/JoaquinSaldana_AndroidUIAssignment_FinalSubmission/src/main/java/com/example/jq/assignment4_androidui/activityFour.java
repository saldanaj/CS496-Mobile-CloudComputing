package com.example.jq.assignment4_androidui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class activityFour extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_four);

        Button btnClose3 = (Button) findViewById(R.id.btnClose2);

        TextView textView1 = (TextView) findViewById(R.id.textView1);
        TextView textView2 = (TextView) findViewById(R.id.textView2);
        TextView textView3 = (TextView) findViewById(R.id.textView3);
        TextView textView4 = (TextView) findViewById(R.id.textView4);
        TextView textView5 = (TextView) findViewById(R.id.textView5);


        btnClose3.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View arg0)
            {
                // close activity1
                finish();
            }

        }); // end of setOnClickListener





    }


}
