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


public class getHumans extends AppCompatActivity
{

    private OkHttpClient mOkHttpClient;
    private ListView simpleListView;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_humans);

        simpleListView = (ListView)findViewById(R.id.humansListView);

        updateList();


        ((Button)findViewById(R.id.getPostsButton)).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v)
            {

                updateList();

            } // end of the onClick overridden function

        }); // end of OnClickListener callback function

    } // end of the onCreate() function


    private void updateList()
    {

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
                        m.put("Favorite Park", j.getJSONObject(i).getString("favDogPark"));
                        m.put("Human ID", j.getJSONObject(i).getString("human_id"));
                        posts.add(m);

                    }

                    final SimpleAdapter postAdapter = new SimpleAdapter(
                            getHumans.this,
                            posts,
                            R.layout.human_entity_layout,
                            new String[]{"Name", "Age", "Favorite Park"},
                            new int[]{R.id.human_name, R.id.human_age, R.id.human_favorite_park});

                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            simpleListView.setAdapter(postAdapter);

                            simpleListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
                                {

                                    Intent intent = new Intent(getHumans.this, humanInformation.class);

                                    String humanID = posts.get(position).get("Human ID");

                                    intent.putExtra("human_id", humanID);
                                    intent.putExtra("name", posts.get(position).get("Name"));
                                    intent.putExtra("age", posts.get(position).get("Age"));
                                    intent.putExtra("favorite_park", posts.get(position).get("Favorite Park"));

                                    startActivity(intent);


                                } // end of the onItemClick overridden function


                            }); // end of the setOnItemClickListener() callback function


                        } // end of the run() function

                    }); // end of the Runnable callback

                } // end of inner try statement

                catch (JSONException e1)
                {
                    e1.printStackTrace();
                }


            } // end of the onResponse function that was overriden

        }); // end of the newCall.enqueue callback

    } // end of the updateList() function




} // end of the getHumans class
