package com.example.jq.app_code;

import android.os.Build;
import android.support.annotation.RequiresApi;
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

public class humanInformation extends AppCompatActivity
{
    private String human_id;
    private String favorite_park;
    private String age;
    private String name;

    private OkHttpClient mOkHttpClient;
    private ListView simpleListView;

    List<Map<String,String>> posts = new ArrayList<Map<String,String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        Bundle bundle = getIntent().getExtras();

        human_id = bundle.getString("human_id");
        favorite_park = bundle.getString("favorite_park");
        age = bundle.getString("age");
        name = bundle.getString("name");


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_human_information);


        TextView nameTxtView = (TextView)findViewById(R.id.humanNameTxtViewBox);
        nameTxtView.setText(name);

        TextView ageTxtView = (TextView)findViewById(R.id.humanAgeTxtView);
        ageTxtView.setText(age);

        TextView favoriteParkTxtView = (TextView)findViewById(R.id.humanFavParkTxtView);
        favoriteParkTxtView.setText(favorite_park);

        simpleListView = (ListView)findViewById(R.id.dogListHumanInfo);

        updateList();

        Button deleteButton = (Button)findViewById(R.id.deleteButton);
        Button modifyButton = (Button)findViewById(R.id.modifyHumanButton);


        deleteButton.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View view)
            {
                deleteHuman();
                finish();
            }

        }); // end of the closeButton callback

        modifyButton.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view)
            {
                modifyHuman();
                finish();
            }

        }); // end of the modifyButton callback








    } // end of the onCreate function



    // ========================================================================================
    // HELPER FUNCTION
    // ========================================================================================


    private void updateList()
    {
        mOkHttpClient = new OkHttpClient();

        HttpUrl reqURL = HttpUrl.parse("https://final-project-saldanaj.appspot.com/humans/" + human_id);

        Request request = new Request.Builder()
                .url(reqURL)
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
                // set the response to the r variable
                String r = response.body().string();

                try
                {
                    JSONObject j = new JSONObject(r);
                    System.out.println("First Response");
                    System.out.println(j);

                    JSONArray dogsOwned = j.getJSONArray("dogsOwned");

                    // final List<Map<String,String>> posts = new ArrayList<Map<String,String>>();

                    // checking to make sure the list is not empty
                    if(dogsOwned.length() > 0)
                    {

                        for(int i = 0; i < dogsOwned.length(); i++)
                        {

                            // make a GET request to get the info on the single dog first
                            HttpUrl reqURL2 = HttpUrl.parse("https://final-project-saldanaj.appspot.com/dogs/" + dogsOwned.getJSONObject(i).getString("dog_id"));

                            Request request2 = new Request.Builder()
                                    .url(reqURL2)
                                    .build();

                            mOkHttpClient.newCall(request2).enqueue(new Callback(){

                                @Override
                                public void onFailure(Call call, IOException e)
                                {
                                    e.printStackTrace();
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException
                                {
                                    String r2 = response.body().string();

                                    try
                                    {
                                        JSONObject j2 = new JSONObject(r2);
                                        System.out.println("Second Response");
                                        System.out.println(j2);


                                        HashMap<String, String> m = new HashMap<String, String>();

                                        m.put("Name", j2.getString("name"));
                                        m.put("Age", j2.getString("age"));
                                        m.put("Breed", j2.getString("breed"));

                                        if(j2.getString("alive") == "true")
                                        {
                                            m.put("Alive", "Alive");
                                        }
                                        else
                                        {
                                            m.put("Alive", "Passed Away");
                                        }

                                        posts.add(m);

                                        final SimpleAdapter postAdapter = new SimpleAdapter(
                                                humanInformation.this,
                                                posts,
                                                R.layout.dog_entity_layout,
                                                new String[]{"Name", "Age", "Breed", "Alive"},
                                                new int[]{R.id.dog_name, R.id.dog_age, R.id.dog_breed, R.id.dog_alive});


                                        runOnUiThread(new Runnable()
                                        {
                                            @Override
                                            public void run()
                                            {
                                                // ((ListView)findViewById(R.id.dogsListView)).setAdapter(postAdapter);
                                                simpleListView.setAdapter(postAdapter);
                                            }

                                        }); // end of the Runnable callback


                                    } // end of inner try statement

                                    catch (JSONException e1)
                                    {
                                        e1.printStackTrace();
                                    }

                                } // end of onResponse function

                            }); // end of the newCall.enqueue callback function

                        } // end of inner for loop

                    }// end of if statement

                } // end of inner try statement

                catch (JSONException e1)
                {
                    e1.printStackTrace();
                }

            } // end of the onResponse function that was overriden

        }); // end of the newCall.enqueue function callback

    } // end of updateList() function

    /*
    Function called when the user hits modify information button
     */

    private void modifyHuman()
    {

        Intent intent = new Intent(humanInformation.this, modifyHumanInfo.class);


        intent.putExtra("human_id", human_id);
        intent.putExtra("name", name);
        intent.putExtra("age", age);
        intent.putExtra("favorite_park", favorite_park);

        startActivity(intent);


    } // end of modifyHuman() function

    /*
    Function called when the user hits delete human button
     */


    private void deleteHuman()
    {

        mOkHttpClient = new OkHttpClient();

        HttpUrl reqURL = HttpUrl.parse("https://final-project-saldanaj.appspot.com/humans/" + human_id);

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

    } // end of deleteHuman() function

} // end of the humanInformation class
