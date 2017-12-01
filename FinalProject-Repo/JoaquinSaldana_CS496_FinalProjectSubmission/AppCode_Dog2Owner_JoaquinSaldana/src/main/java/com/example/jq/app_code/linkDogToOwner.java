package com.example.jq.app_code;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class linkDogToOwner extends AppCompatActivity
{
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private String human_id;
    private String human_name;
    private String dog_id;
    private String dog_name;

    private OkHttpClient mOkHttpClient;


    ArrayList<String> ownerNames;
    ArrayList<String> ownerID;
    ArrayList<String> dogNames;
    ArrayList<String> dogID;

    ArrayAdapter<String> adapter;
    ArrayAdapter<String> adapter2;

    Spinner ownersDropDown;
    Spinner dogsDropDown;

    Integer ownerSelected;
    Integer dogSelected;


    /*
    List<Map<String,String>> ownerPosts;
    List<Map<String,String>> dogPosts;
    */

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_dog_to_owner);

        Button submitLinkInfo = (Button)findViewById(R.id.submitLinkInfo);
        Button goBackButton = (Button)findViewById(R.id.goBackButton);

        updateSpinners();


        submitLinkInfo.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View view)
            {
                submit();
                updateSpinners();
                finish();
            }

        }); // end of the submitLinkInfo callback


        goBackButton.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View view)
            {
                finish();
            }

        }); // end of the goBackButton callback


    } // end of onCreate() function




    private void submit()
    {
        ownerSelected = ownersDropDown.getSelectedItemPosition();
        dogSelected = dogsDropDown.getSelectedItemPosition();

        dog_id = dogID.get(dogSelected).toString();
        human_id = ownerID.get(ownerSelected).toString();

        String jsonBody = "{}";

        mOkHttpClient = new OkHttpClient();

        HttpUrl reqURL = HttpUrl.parse("https://final-project-saldanaj.appspot.com/dogs/" + dog_id + "/owner/" + human_id);

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

    } // end of submit() function



    private void updateSpinners()
    {

        ownersDropDown =(Spinner)findViewById(R.id.ownerSpinner);
        dogsDropDown = (Spinner)findViewById(R.id.dogSpinner);

        ownerNames = new ArrayList<String>();
        ownerID = new ArrayList<String>();

        dogNames = new ArrayList<String>();
        dogID = new ArrayList<String>();

        // -----------
        // HUMAN REQUEST
        // -----------

        mOkHttpClient = new OkHttpClient();

        HttpUrl reqURL = HttpUrl.parse("https://final-project-saldanaj.appspot.com/humans");

        Request request = new Request.Builder()
                .url(reqURL)
                .build();

        mOkHttpClient.newCall(request).enqueue(new Callback(){

            @Override
            public void onFailure(Call call, IOException e)
            {
                e.printStackTrace();
            } // end of the onFailure() function

            @Override
            public void onResponse(Call call, Response response) throws IOException
            {
                String r = response.body().string();

                try
                {
                    JSONArray j = new JSONArray(r);

                    for(int i = 0; i < j.length(); i++)
                    {
                        /*
                        HashMap<String, String> ownersHashMap = new HashMap<String, String>();
                        ownersHashMap.put("name", j.getJSONObject(i).getString("name"));
                        ownersHashMap.put("human_id", j.getJSONObject(i).getString("human_id"));
                        ownerPosts.add(ownersHashMap);
                        */

                        ownerNames.add(j.getJSONObject(i).getString("name"));
                        ownerID.add(j.getJSONObject(i).getString("human_id"));
                    }

                    adapter = new ArrayAdapter<String>(linkDogToOwner.this , android.R.layout.simple_spinner_dropdown_item, ownerNames);

                    runOnUiThread(new Runnable(){

                        @Override
                        public void run()
                        {
                            ownersDropDown.setAdapter(adapter);
                        }


                    }); // end of runOnUIThread() callback

                }
                catch (JSONException e1)
                {
                    e1.printStackTrace();
                }
            } // end of the onResponse() function

        }); // end of newCall.enqueue() function callback


        /*

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                ownerNames);
       */


        // -----------
        // DOG REQUEST
        // -----------

        mOkHttpClient = new OkHttpClient();

        HttpUrl reqURL2 = HttpUrl.parse("https://final-project-saldanaj.appspot.com/dogs");

        Request request2 = new Request.Builder()
                .url(reqURL2)
                .build();

        mOkHttpClient.newCall(request2).enqueue(new Callback(){

            @Override
            public void onFailure(Call call, IOException e)
            {
                e.printStackTrace();
            } // end of the onFailure() function

            @Override
            public void onResponse(Call call, Response response) throws IOException
            {
                String r2 = response.body().string();

                try
                {
                    JSONArray j2 = new JSONArray(r2);


                    for(int i = 0; i < j2.length(); i++)
                    {
                        /*
                        HashMap<String, String> dogsHashMap = new HashMap<String, String>();
                        dogsHashMap.put("name", j2.getJSONObject(i).getString("name"));
                        dogsHashMap.put("dog_id", j2.getJSONObject(i).getString("dog_id"));
                        dogPosts.add(dogsHashMap);
                        */

                        dogNames.add(j2.getJSONObject(i).getString("name"));
                        dogID.add(j2.getJSONObject(i).getString("dog_id"));

                    }

                    adapter2 = new ArrayAdapter<String>(linkDogToOwner.this , android.R.layout.simple_spinner_dropdown_item, dogNames);

                    runOnUiThread(new Runnable(){

                        @Override
                        public void run()
                        {
                            dogsDropDown.setAdapter(adapter2);
                        }

                    });

                }
                catch (JSONException e1)
                {
                    e1.printStackTrace();
                }

            } // end of the onResponse() function

        });

    } // end of the updateSpinners() function




} // end of linkDogToOwner class
