package com.example.jq.app_code;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class createHuman extends AppCompatActivity
{
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private String favorite_park;
    private String age;
    private String name;

    private OkHttpClient mOkHttpClient;

    EditText humanName;
    EditText humanAge;
    EditText humanFavoritePark;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_human);

        Button sendHumanInfo = (Button)findViewById(R.id.submitHummanInfo);

        humanName = (EditText)findViewById(R.id.humanNameInput);

        humanAge = (EditText)findViewById(R.id.humanAgeInput);

        humanFavoritePark = (EditText)findViewById(R.id.humanFavoriteParkInput);

        sendHumanInfo.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View view)
            {
                createHumanInfo();
                finish();
            }

        }); // end of the closeButton callback


    } // end of the onCreate() function

    private void createHumanInfo()
    {
        name = humanName.getText().toString();
        age = humanAge.getText().toString();
        favorite_park = humanFavoritePark.getText().toString();


        String jsonBody = "{ ";

        if(name != "")
        {
            jsonBody = jsonBody + " \"name\": \"" + name + "\",";
        }
        if(age != "")
        {
            jsonBody = jsonBody + " \"age\": " + Integer.parseInt(age) + ", ";
        }
        if(favorite_park != "")
        {
            jsonBody = jsonBody + " \"favorite_park\":  \"" + favorite_park + "\" ";
        }

        jsonBody = jsonBody + " }";

        System.out.println(jsonBody);

        mOkHttpClient = new OkHttpClient();

        HttpUrl reqURL = HttpUrl.parse("https://final-project-saldanaj.appspot.com/humans");

        RequestBody body = RequestBody.create(JSON, jsonBody);

        Request request = new Request.Builder()
                .url(reqURL)
                .post(body)
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


    } // end of createHumanInfo() function


} // end of the createHuman class
