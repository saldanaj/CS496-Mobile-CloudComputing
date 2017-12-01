package com.example.jq.androidhttpoauth;

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
    }

    /*
    enterPostsActivity
    viewPostsActivity
     */


    // function called when the user clicks the Post To G+ button
    public void enterPostsActivity(View view)
    {
        Intent intent = new Intent(MainActivity.this, enterPosts.class);
        startActivity(intent);
    }

    // function called when the user clicks the View G+ Posts button
    public void viewPostsActivity(View view)
    {
        Intent intent = new Intent(MainActivity.this, viewPosts.class);
        startActivity(intent);
    }

}
