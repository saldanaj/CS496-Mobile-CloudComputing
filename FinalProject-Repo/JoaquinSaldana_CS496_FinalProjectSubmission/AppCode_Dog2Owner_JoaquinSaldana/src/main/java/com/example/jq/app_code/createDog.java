package com.example.jq.app_code;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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



public class createDog extends AppCompatActivity
{

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private String breed;
    private String age;
    private String name;

    private OkHttpClient mOkHttpClient;

    EditText dogName;
    EditText dogAge;
    EditText dogBreed;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_dog);

        Button sendDogInfo = (Button)findViewById(R.id.submitDogInfo);

        dogName = (EditText)findViewById(R.id.dogNameInput);
        dogAge = (EditText)findViewById(R.id.dogAgeInput);
        dogBreed = (EditText)findViewById(R.id.dogBreedInput);

        sendDogInfo.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View view)
            {
                submitNewDogInfo();
                finish();
            }

        }); // end of the closeButton callback


    } // end of onCreate() function

    private void submitNewDogInfo()
    {
        name = dogName.getText().toString() ;
        age = dogAge.getText().toString();
        breed = dogBreed.getText().toString();

        String jsonBody = "{ ";

        if(name != "")
        {
            jsonBody = jsonBody + " \"name\": \"" + name + "\",";
        }
        if(age != "")
        {
            jsonBody = jsonBody + " \"age\": " + Integer.parseInt(age) + ", ";
        }
        if(breed != "")
        {
            jsonBody = jsonBody + " \"breed\":  \"" + breed + "\" ";
        }

        jsonBody = jsonBody + " }";

        System.out.println(jsonBody);

        mOkHttpClient = new OkHttpClient();

        HttpUrl reqURL = HttpUrl.parse("https://final-project-saldanaj.appspot.com/dogs");

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

    } // end of submitNewDogInfo() function

} // end of createDog class
