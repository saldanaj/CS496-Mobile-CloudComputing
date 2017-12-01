package com.example.jq.app_code;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    } // end of the onCreate() function


    public void getHumansActivity(View view)
    {
        Intent intent = new Intent(MainActivity.this, getHumans.class);
        startActivity(intent);
    }


    public void getDogsActivity(View view)
    {
        Intent intent = new Intent(MainActivity.this, getDogs.class);
        startActivity(intent);
    }

    public  void createHumanActivity(View view)
    {
        Intent intent = new Intent(MainActivity.this, createHuman.class);
        startActivity(intent);
    }

    public void createDogActivity(View view)
    {
        Intent intent = new Intent(MainActivity.this, createDog.class);
        startActivity(intent);
    }

    public void linkDowOwner(View view)
    {
        Intent intent = new Intent(MainActivity.this, linkDogToOwner.class);
        startActivity(intent);

    }


} // end of the MainActivity class
