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




public class getDogs extends AppCompatActivity
{

    private OkHttpClient mOkHttpClient;
    private ListView simpleListView;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_dogs);

        simpleListView = (ListView)findViewById(R.id.dogsListView);

        updateList();

        ((Button)findViewById(R.id.getPostsButton)).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v)
            {
                updateList();

            } // end of the onClick overridden function

        }); // end of OnClickListener callback function

    } // end of the onCreate() overridden function


    private void updateList()
    {

        mOkHttpClient = new OkHttpClient();

        HttpUrl reqURL = HttpUrl.parse("https://final-project-saldanaj.appspot.com/dogs");

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
                System.out.println(r);

                try
                {
                    JSONArray j = new JSONArray(r);
                    System.out.println(j);


                    final List<Map<String,String>> posts = new ArrayList<Map<String,String>>();

                    for(int i = 0; i < j.length(); i++)
                    {

                        HashMap<String, String> m = new HashMap<String, String>();
                        m.put("Name", j.getJSONObject(i).getString("name"));
                        m.put("Age", j.getJSONObject(i).getString("age"));
                        m.put("Breed", j.getJSONObject(i).getString("breed"));
                        m.put("Dog ID", j.getJSONObject(i).getString("dog_id"));
                        m.put("Owner", j.getJSONObject(i).getString("owner"));

                        if(j.getJSONObject(i).getString("alive") == "true")
                        {
                            m.put("Alive", "Alive");
                        }
                        else
                        {
                            m.put("Alive", "Passed Away");
                        }

                        posts.add(m);

                    }

                    final SimpleAdapter postAdapter = new SimpleAdapter(
                            getDogs.this,
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


                            simpleListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
                                {

                                    Intent intent = new Intent(getDogs.this, dogInformation.class);

                                    String dogID = posts.get(position).get("Dog ID");

                                    intent.putExtra("dog_id", dogID);
                                    intent.putExtra("name", posts.get(position).get("Name"));
                                    intent.putExtra("age", posts.get(position).get("Age"));
                                    intent.putExtra("alive", posts.get(position).get("Alive"));
                                    intent.putExtra("breed", posts.get(position).get("Breed"));
                                    intent.putExtra("owner", posts.get(position).get("Owner"));

                                    startActivity(intent);


                                } // end of the onItemClick overridden function


                            }); // end of the setOnItemClickListener() callback function


                        }

                    }); // end of the Runnable callback

                } // end of inner try statement

                catch (JSONException e1)
                {
                    e1.printStackTrace();
                }


            } // end of the onResponse function that was overriden

        }); // end of the newCall.enqueue callback

    } // end of the updateList() function




} // end of the getDogs class
