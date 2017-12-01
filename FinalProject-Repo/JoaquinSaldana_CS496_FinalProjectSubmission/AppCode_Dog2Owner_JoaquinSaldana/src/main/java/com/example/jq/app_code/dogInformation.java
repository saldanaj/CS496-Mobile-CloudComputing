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


public class dogInformation extends AppCompatActivity
{

    private String owner_id;
    private String dog_id;
    private String breed;
    private String age;
    private String name;
    private String alive;

    TextView nameTxtView;
    TextView ageTxtView;
    TextView breedTxtView;
    TextView aliveTxtView;
    TextView ownerTxtView;

    private OkHttpClient mOkHttpClient;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Bundle bundle = getIntent().getExtras();

        owner_id = bundle.getString("owner");
        dog_id = bundle.getString("dog_id");
        breed = bundle.getString("breed");
        name = bundle.getString("name");
        age = bundle.getString("age");
        alive = bundle.getString("alive");


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dog_information);


        nameTxtView = (TextView)findViewById(R.id.dogNameInputField);
        nameTxtView.setText(name);

        ageTxtView = (TextView)findViewById(R.id.dogAgeInputField);
        ageTxtView.setText(age);

        breedTxtView = (TextView)findViewById(R.id.dogBreedInputField);
        breedTxtView.setText(breed);

        aliveTxtView = (TextView)findViewById(R.id.dogAliveInputField);
        aliveTxtView.setText(alive);

        getOwnerName();

        Button deleteButton = (Button)findViewById(R.id.deleteButton);
        Button modifyButton = (Button)findViewById(R.id.modifyDogButton);

        deleteButton.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View view)
            {
                deleteDog();
                finish();
            }

        }); // end of the closeButton callback

        modifyButton.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view)
            {
                modifyDog();
                finish();
            }

        }); // end of the modifyButton callback



    } // end of the onCreate() function


    private void getOwnerName()
    {
        mOkHttpClient = new OkHttpClient();

        HttpUrl reqURL = HttpUrl.parse("https://final-project-saldanaj.appspot.com/humans/" + owner_id);

        Request request = new Request.Builder()
                .url(reqURL)
                .build();

        mOkHttpClient.newCall(request).enqueue(new Callback()
        {

            @Override
            public void onFailure(Call call, IOException e)
            {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException
            {
                String r = response.body().string();

                try
                {
                    final JSONObject j = new JSONObject(r);


                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            ownerTxtView = (TextView)findViewById(R.id.dogOwnerInputField);

                            try
                            {
                                ownerTxtView.setText(j.getString("name"));
                            }
                            catch (JSONException e)
                            {
                                e.printStackTrace();
                            }
                        }

                    }); // end of the Runnable callback


                } // end of inner try statement

                catch (JSONException e1)
                {
                    e1.printStackTrace();
                }

            } // end of onResponse function


        }); // end of the newCall(request).enqueue function


    } // end of getOwnerName() function




    private void deleteDog()
    {
        mOkHttpClient = new OkHttpClient();

        HttpUrl reqURL = HttpUrl.parse("https://final-project-saldanaj.appspot.com/dogs/" + dog_id);

        Request request = new Request.Builder()
                .url(reqURL)
                .delete()
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
                System.out.println(r);
            }

        });

    } // end of deleteDog() function



    private void modifyDog()
    {

        Intent intent = new Intent(dogInformation.this, modifyDogInfo.class);

        intent.putExtra("dog_id", dog_id);
        intent.putExtra("name", name);
        intent.putExtra("age", age);
        intent.putExtra("breed", breed);
        intent.putExtra("alive", alive);

        startActivity(intent);

    } // end of modifyDog() function


} //end of the dogInformation() function
