package com.example.jq.app_code;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.Nullable;

import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.Nullable;

import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class modifyDogInfo extends AppCompatActivity
{

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private String breed;
    private String age;
    private String name;
    private String dog_id;
    private String alive;

    private OkHttpClient mOkHttpClient;


    EditText dogName;
    EditText dogAge;
    EditText dogBreed;
    CheckBox dogAlive;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        Bundle bundle = getIntent().getExtras();

        breed = bundle.getString("breed");
        name = bundle.getString("name");
        age = bundle.getString("age");
        dog_id = bundle.getString("dog_id");
        alive = bundle.getString("alive");


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_dog_info);

        Button sendDogInfo = (Button)findViewById(R.id.submitDogInfo);

        dogName = (EditText)findViewById(R.id.dogNameInput);
        dogName.setText(name);

        dogAge = (EditText)findViewById(R.id.dogAgeInput);
        dogAge.setText(age);

        dogBreed = (EditText)findViewById(R.id.dogBreedInput);
        dogBreed.setText(breed);

        if(alive == "Alive")
        {
            dogAlive = (CheckBox)findViewById(R.id.aliveCheckBox);
            dogAlive.setChecked(dogAlive.isChecked());
        }

        if(alive == "Passed Away")
        {
            dogAlive = (CheckBox)findViewById(R.id.aliveCheckBox);
            dogAlive.setChecked(!dogAlive.isChecked());
        }


        sendDogInfo.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View view)
            {
                sendDogInfo();
                finish();
            }

        }); // end of the closeButton callback


    } // end of OnCreate() function




    private void sendDogInfo()
    {
        String newName = dogName.getText().toString() ;
        String newAge = dogAge.getText().toString();
        String newBreed = dogBreed.getText().toString();

        String jsonBody = "{ ";

        if(newName != "")
        {
            jsonBody = jsonBody + " \"name\": \"" + newName + "\",";
        }
        if(newAge != "")
        {
            jsonBody = jsonBody + " \"age\": " + Integer.parseInt(newAge) + ", ";
        }
        if(newBreed != "")
        {
            jsonBody = jsonBody + " \"breed\":  \"" + newBreed + "\" ";
        }

        jsonBody = jsonBody + " }";

        System.out.println(jsonBody);

        mOkHttpClient = new OkHttpClient();

        HttpUrl reqURL = HttpUrl.parse("https://final-project-saldanaj.appspot.com/dogs/" + dog_id);

        RequestBody body = RequestBody.create(JSON, jsonBody);

        Request request = new Request.Builder()
                .url(reqURL)
                .patch(body)
                .build();

        mOkHttpClient.newCall(request).enqueue(new Callback(){

            @Override
            public void onFailure(Call call, IOException e)
            {
                e.printStackTrace();
            }


            @Override
            public void onResponse(Call call, Response response) throws IOException
            {
                String r = response.body().string();

                System.out.print(r);
            }

        }); // end of the newCall.enqueue callback
    }

    public void onCheckBoxClicked(View view)
    {
        Boolean isAliveStatus;

        boolean checked = ((CheckBox) view).isChecked();

        String jsonBody = "{ ";

        if(checked)
        {
            isAliveStatus = true;

            jsonBody = jsonBody + "\"alive\": " + isAliveStatus + " }";

        }
        else
        {
            isAliveStatus = false;

            jsonBody = jsonBody + "\"alive\": " + isAliveStatus + " }";

        }

        System.out.println(jsonBody);

        mOkHttpClient = new OkHttpClient();

        HttpUrl reqURL = HttpUrl.parse("https://final-project-saldanaj.appspot.com/dogs/" + dog_id);

        RequestBody body = RequestBody.create(JSON, jsonBody);

        Request request = new Request.Builder()
                .url(reqURL)
                .put(body)
                .build();

        mOkHttpClient.newCall(request).enqueue(new Callback(){

            @Override
            public void onFailure(Call call, IOException e)
            {
                e.printStackTrace();
            }


            @Override
            public void onResponse(Call call, Response response) throws IOException
            {
                String r = response.body().string();

                System.out.print(r);

            }

        }); // end of the newCall.enqueue callback

    }


} // end of the modifyDogInfo class
