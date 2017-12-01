package com.example.jq.assignment4_androidui;

import android.content.Intent;
import android.database.AbstractCursor;
import android.database.CharArrayBuffer;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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


    public void activity1(View view)
    {
        Intent intent1 = new Intent(MainActivity.this, activityOne.class);
        startActivity(intent1);

    } // end of activity1 function



    public void activity2(View view)
    {
        Intent intent2 = new Intent(MainActivity.this, activityTwo.class);
        startActivity(intent2);

    } // end of activity2 function



    public void activity3(View view)
    {
        Intent intent3 = new Intent(MainActivity.this, activityThree.class);
        startActivity(intent3);

    } // end of activity3 function



    public void activity4(View view)
    {
        Intent intent4 = new Intent(MainActivity.this, activityFour.class);
        startActivity(intent4);
    } // end of activity4 function



    /*

    public void sendMessage(View view)
    {
        Intent intent = new Intent(MainActivity.this, DisplayMessageActivity.class);

        EditText editText = (EditText) findViewById(R.id.edit_message);

        String message = editText.getText().toString();

        intent.putExtra(EXTRA_MESSAGE, message);

        startActivity(intent);
    }

    */

} // end of MainActivity
