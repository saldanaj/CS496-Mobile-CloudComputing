package com.example.jq.androidhttpoauth;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;

import net.openid.appauth.AuthState;
import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.AuthorizationServiceConfiguration;
import net.openid.appauth.ResponseTypeValues;

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

public class viewPosts extends AppCompatActivity
{

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private AuthorizationService mAuthorizationService;
    private AuthState mAuthState;
    private OkHttpClient mOkHttpClient;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_posts);


        // SharedPreference that has the stored access token
        SharedPreferences authPreference = getSharedPreferences("auth", MODE_PRIVATE);
        mAuthorizationService = new AuthorizationService(this);


        ((Button)findViewById(R.id.getPostsButton)).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v)
            {

                try
                {
                    mAuthState.performActionWithFreshTokens(mAuthorizationService, new AuthState.AuthStateAction(){


                        @Override
                        public void execute(@Nullable String accessToken, @Nullable String idToken, @Nullable AuthorizationException e)
                        {
                            if(e == null)
                            {
                                mOkHttpClient = new OkHttpClient();

                                HttpUrl reqUrl = HttpUrl.parse("https://www.googleapis.com/plusDomains/v1/people/me/activities/user");

                               //  reqUrl = reqUrl.newBuilder().addQueryParameter("key", "AIzaSyDsx70aHdYtjvCMIDHtlK-Ni3Qf--fwURg").build();

                                Request request = new Request.Builder()
                                        .url(reqUrl)
                                        .addHeader("Authorization", "Bearer " + accessToken)
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

                                        try
                                        {

                                            JSONObject j = new JSONObject(r);

                                            JSONArray items = j.getJSONArray("items");

                                            List<Map<String,String>> posts = new ArrayList<Map<String,String>>();


                                            // for(int i = 0; i < items.length(); i++)
                                            for(int i = 0; i < items.length(); i++)
                                            {
                                                HashMap<String, String> m = new HashMap<String, String>();
                                                m.put("published", items.getJSONObject(i).getString("published"));
                                                m.put("title",items.getJSONObject(i).getString("title"));
                                                posts.add(m);
                                            }



                                            final SimpleAdapter postAdapter = new SimpleAdapter(
                                                    viewPosts.this,
                                                    posts,
                                                    R.layout.google_plus_item,
                                                    new String[]{"published", "title"},
                                                    new int[]{R.id.google_Column1, R.id.google_Column2});


                                            runOnUiThread(new Runnable()
                                            {


                                                @Override
                                                public void run()
                                                {
                                                    ((ListView)findViewById(R.id.gPlus_listView)).setAdapter(postAdapter);
                                                }



                                            }); // end of the Runnable callback


                                        } // end of inner try statement


                                        catch (JSONException e1)
                                        {
                                            e1.printStackTrace();
                                        }



                                    }

                                }); // end of the newCall.enqueue callback



                            } // end of the if statement


                        } // end of the execute overriden function


                    }); // end of the AuthState callback


                } // end of the outter try bracket
                catch(Exception e)
                {

                    e.printStackTrace();
                }


            } // end of the OnClick method override


        }); // end of the View.OnClickListener() function callback




    } // end of onCreate() method






    // ====================================================================================

    /*
    Either get or create a new access token
     */
    @Override
    protected void onStart()
    {
        mAuthState = getOrCreateAuthState();
        super.onStart();

    } // end of onStart() function


    /*
    Function is checking to see if we have an access token or if we need to create one.  If we
    need to create one then it calls the updateAuthState() function
     */

    AuthState getOrCreateAuthState()
    {
        AuthState auth = null;

        SharedPreferences authPreference = getSharedPreferences("auth", MODE_PRIVATE);

        String stateJson = authPreference.getString("stateJson", null);

        if(stateJson != null)
        {
            try
            {
                auth = AuthState.jsonDeserialize(stateJson);
            }
            catch (JSONException e)
            {
                e.printStackTrace();

                return null;
            }
        }

        if( auth != null && auth.getAccessToken() != null)
        {
            return auth;
        }
        else
        {
            updateAuthState();
            return null;
        }

    } // end of getOrCreateAuthState() function


    /*
    This function constructs the URL's needed to send our request for the accessToken.  Once
    everything is configured the information is passed authComplete intent, which then calls/
    runs the AuthCompleteActivity class
     */

    void updateAuthState()
    {

        // creating the Google API URL's necessary to request our access token
        Uri authEndpoint = new Uri.Builder().scheme("https").authority("accounts.google.com").path("/o/oauth2/v2/auth").build();
        Uri tokenEndpoint = new Uri.Builder().scheme("https").authority("www.googleapis.com").path("/oauth2/v4/token").build();
        Uri redirect = new Uri.Builder().scheme("com.example.jq.androidhttpoauth").path("foo").build();


        // configure the URL's so we can begin constructing our OAuth request
        AuthorizationServiceConfiguration config = new AuthorizationServiceConfiguration(authEndpoint, tokenEndpoint, null);

        AuthorizationRequest req = new AuthorizationRequest.Builder(config, "306731382932-5tb0a6tke770557nnu50539ignhmasvh.apps.googleusercontent.com", ResponseTypeValues.CODE, redirect)
                .setScopes("https://www.googleapis.com/auth/plus.me", "https://www.googleapis.com/auth/plus.stream.write", "https://www.googleapis.com/auth/plus.stream.read")
                .build();

        Intent authComplete = new Intent(this, completeAuthActivity.class);

        mAuthorizationService.performAuthorizationRequest(req, PendingIntent.getActivity(this, req.hashCode(), authComplete, 0));

    } // end of the updateAuthState() function


} // end of viewPosts class
