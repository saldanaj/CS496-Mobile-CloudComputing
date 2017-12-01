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
import android.widget.LinearLayout;
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

public class modifyHumanInfo extends AppCompatActivity
{

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private String human_id;
    private String favorite_park;
    private String age;
    private String name;

    private OkHttpClient mOkHttpClient;
    private ListView simpleListView;

    EditText humanName;
    EditText humanAge;
    EditText humanFavoritePark;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Bundle bundle = getIntent().getExtras();

        human_id = bundle.getString("human_id");
        favorite_park = bundle.getString("favorite_park");
        age = bundle.getString("age");
        name = bundle.getString("name");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_human_info);


        Button sendHumanInfo = (Button)findViewById(R.id.submitHummanInfo);

        humanName = (EditText)findViewById(R.id.humanNameInput);
        humanName.setText(name);

        humanAge = (EditText)findViewById(R.id.humanAgeInput);
        humanAge.setText(age);

        humanFavoritePark = (EditText)findViewById(R.id.humanFavoriteParkInput);
        humanFavoritePark.setText(favorite_park);

        sendHumanInfo.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View view)
            {
                sendHumanInfo();
                finish();
            }

        }); // end of the closeButton callback


    } // end of onCreate function


    private void sendHumanInfo()
    {

        String newName = humanName.getText().toString() ;
        String newAge = humanAge.getText().toString();
        String newFavoritePark = humanFavoritePark.getText().toString();

        String jsonBody = "{ ";

        if(newName != "")
        {
            jsonBody = jsonBody + " \"name\": \"" + newName + "\",";
        }
        if(newAge != "")
        {
            jsonBody = jsonBody + " \"age\": " + Integer.parseInt(newAge) + ", ";
        }
        if(newFavoritePark != "")
        {
            jsonBody = jsonBody + " \"favorite_park\":  \"" + newFavoritePark + "\" ";
        }

        jsonBody = jsonBody + " }";

        System.out.println(jsonBody);

        mOkHttpClient = new OkHttpClient();

        HttpUrl reqURL = HttpUrl.parse("https://final-project-saldanaj.appspot.com/humans/" + human_id);

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


    } // end of sendHumanInfo() function


} // end of modifyHumanInfo class
