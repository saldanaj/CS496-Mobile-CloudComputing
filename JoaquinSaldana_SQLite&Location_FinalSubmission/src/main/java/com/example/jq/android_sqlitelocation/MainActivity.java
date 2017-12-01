package com.example.jq.android_sqlitelocation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.view.View;

import android.database.AbstractCursor;
import android.database.CharArrayBuffer;
import android.database.Cursor;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    // activity to call if the user selects to enter a message
    public void enterMsgActivity(View view)
    {
        Intent intent1 = new Intent(MainActivity.this, enterMsg.class);
        startActivity(intent1);

    } // end of function to enter a message


}
