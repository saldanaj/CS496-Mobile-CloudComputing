package com.example.jq.android_sqlitelocation;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.content.ContentValues;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class enterMsg extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
{

    // Variables that will be used for the gathering of the location
    // The first set of variables are standard varirables from the libs
    // The next variables are custom variables
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private LocationListener mLocationListener;
    private static final int LOCATION_PERMISSON_RESULT = 17;
    private Location mLastLocation;


    public String latitute;
    public String longitude;



    // Variables that have to do w/ the SQLite DB
    Button mSQLSubmitButton;
    Button mdeleteSQL;
    SQLiteTableAssignment5 msqliteAssignment5DB;
    SQLiteDatabase mSQLDB;
    private static final String TAG = "SQLActivity";

    // Variables used to populate the database
    Cursor mSQLCursor;
    SimpleCursorAdapter mSQLCursorAdapter;



    /*
    In the onCreate function we are going to create the XML properties fot he
    enter_msg.xml file and also create the necessary code to collect the user's
    location information from their device
     */

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_msg);

        if (mGoogleApiClient == null)
        {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }


        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);

        mLocationListener = new LocationListener()
        {
            @Override
            public void onLocationChanged(Location location)
            {
                if(location != null)
                {
                    // right now i'm storing the values of the latitude
                    // and longitude to string however i may need to reconsider
                    // and store them as a double
                    latitute = String.valueOf(location.getLatitude());
                    longitude = String.valueOf(location.getLongitude());
                }
                else
                {
                    latitute = Double.toString(44.5);
                    longitude = Double.toString(-123.2);
                }

            } // end of onLocationChanged function

        };  // end of new LocationListener() function


        /*
        Now creating the variables and function to activate the SQLite database
         */
        msqliteAssignment5DB = new SQLiteTableAssignment5(this);
        mSQLDB = msqliteAssignment5DB.getWritableDatabase();

        mSQLSubmitButton = (Button) findViewById(R.id.submitMsgButton);

        mSQLSubmitButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(mSQLDB != null)
                {
                    updateLocation();

                    ContentValues vals = new ContentValues();
                    vals.put(DBContract.SQLiteTable.COLUMN1_MSG, ((EditText)findViewById(R.id.enterMsgWindow)).getText().toString());
                    vals.put(DBContract.SQLiteTable.COLUMN2_LAT, latitute);
                    vals.put(DBContract.SQLiteTable.COLUMN3_LONG, longitude);

                    mSQLDB.insert(DBContract.SQLiteTable.TABLE_NAME, null, vals);

                    populateTable();

                    displayPopupWindow(v);
                }
                else
                {
                    Log.d(TAG, "Unable to access database for writing");
                }

            } // end of onClick() function


        }); // end of the mSQLSubmitButton.setOnClickListener() function


        populateTable();


        mdeleteSQL = (Button) findViewById(R.id.deleteList);

        mdeleteSQL.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                msqliteAssignment5DB.onUpgrade(mSQLDB, 0, 0);
                populateTable();
            }
        });


    } // end of onCreate() function





    /*
    ==============================================================================================
     */


    /*
    Below is the methods for the Location Services that are necessary
     */

    @Override
    protected void onStart()
    {
        mGoogleApiClient.connect();
        super.onStart();

    } // end of the onStart() method

    @Override
    protected void onStop()
    {
        mGoogleApiClient.disconnect();
        super.onStop();
    } // end of the onStop() function


    /*
    This function is called when the location services immediately connects to the application
     */

    @Override
    public void onConnected(@Nullable Bundle bundle)
    {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSON_RESULT);

            latitute = Double.toString(44.5);
            longitude = Double.toString(-123.2);

            return;
        }

        // need to decide how to handle this function
        updateLocation();

    } // end of the onConnected() function

    @Override
    public void onConnectionSuspended(int i)
    {

    } // end of the onConnectionSuspended() function

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
    {
        Dialog errDialog = GoogleApiAvailability.getInstance().getErrorDialog(this, connectionResult.getErrorCode(), 0);
        errDialog.show();

        latitute = Double.toString(44.5);
        longitude = Double.toString(-123.2);

        return;

    } // end of the onConnectionFailed() function


    /*
    This function is called when the user grants permission during the notification window.
     */

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        if(requestCode == LOCATION_PERMISSON_RESULT)
        {
            if(grantResults.length > 0)
            {
                updateLocation();
            }
            else
            {
                latitute = Double.toString(44.5);
                longitude = Double.toString(-123.2);
            }
        }
    } // end of the onREquestPermissionsResult() function


    /*
    Function that is called to recorded the updated location
     */

    private void updateLocation()
    {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if(mLastLocation != null)
        {
            latitute = String.valueOf(mLastLocation.getLatitude());
            longitude = String.valueOf(mLastLocation.getLongitude());
        }
        else
        {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,mLocationRequest,mLocationListener);
        }

    } // end of the updateLocation() function



    private void displayPopupWindow(View anchorView)
    {
        PopupWindow popup = new PopupWindow(this);

        View layout = getLayoutInflater().inflate(R.layout.popup_content, null);
        popup.setContentView(layout);
        popup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popup.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        popup.setOutsideTouchable(true);
        popup.setFocusable(true);

        popup.setBackgroundDrawable(new BitmapDrawable());
        popup.showAsDropDown(anchorView);

    } // end of the displayPopupWindow() function



    private void populateTable()
    {
        if(mSQLDB != null)
        {

            try
            {
                if(mSQLCursorAdapter != null && mSQLCursorAdapter.getCursor() != null)
                {
                    if(!mSQLCursorAdapter.getCursor().isClosed())
                    {
                        mSQLCursorAdapter.getCursor().close();
                    }
                }

                mSQLCursor = mSQLDB.query(DBContract.SQLiteTable.TABLE_NAME,
                        new String[]{DBContract.SQLiteTable._ID, DBContract.SQLiteTable.COLUMN1_MSG, DBContract.SQLiteTable.COLUMN2_LAT, DBContract.SQLiteTable.COLUMN3_LONG},
                        null,
                        null,
                        null,
                        null,
                        null);

                ListView SQLListView = (ListView) findViewById(R.id.sql_listView);

                mSQLCursorAdapter = new SimpleCursorAdapter(this,
                        R.layout.sql_item, mSQLCursor,
                        new String[]{DBContract.SQLiteTable.COLUMN1_MSG, DBContract.SQLiteTable.COLUMN2_LAT, DBContract.SQLiteTable.COLUMN3_LONG},
                        new int[]{R.id.sql_Column1, R.id.sql_Column2, R.id.sql_Column3},
                        0);

                SQLListView.setAdapter(mSQLCursorAdapter);


            } catch (Exception e)
            {
                Log.d(TAG, "Error Loading Data From Database");
            }

        }

    } //end of populateTable()function





    /*
    ==============================================================================================
     */



    /*
    Subclass that implements the SQLiteHelper class.  This will be used to create the table
    when it has not yet been created.
     */

    class SQLiteTableAssignment5 extends SQLiteOpenHelper
    {
        public SQLiteTableAssignment5(Context context)
        {
            super(context, DBContract.SQLiteTable.DB_NAME, null, DBContract.SQLiteTable.DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            db.execSQL(DBContract.SQLiteTable.SQL_CREATE_TABLE);

            // needed to import the package to use this object
            ContentValues testValues = new ContentValues();

            /*
            // testing to see if i get an error w/ the creation of the table
            testValues.put(DBContract.SQLiteTable.COLUMN1_MSG, "This is test");
            testValues.put(DBContract.SQLiteTable.COLUMN2_LAT, "100.1");
            testValues.put(DBContract.SQLiteTable.COLUMN3_LONG, "20.3");
            db.insert(DBContract.SQLiteTable.TABLE_NAME, null, testValues);
            */


        } // end of onCreate() function


        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            db.execSQL(DBContract.SQLiteTable.SQL_DROP_TABLE);
            onCreate(db);
        } // end of onUpgrade() function


    } // end of the SQLiteTableAssignment5 class


    /*

    This is the DBContract class that will hold static and final strings that will be used
    various times to either create the table or to test if the table is operating
     */

    final class DBContract
    {
        private DBContract(){};

        public final class SQLiteTable implements BaseColumns
        {
            // Variables that are declared as static and final that will hold the
            // name of the table and columns of the sqlite table

            public static final String DB_NAME = "assignment5DBName";
            public static final String TABLE_NAME = "assignment5TableName";
            public static final String COLUMN1_MSG = "stringEntered";
            public static final String COLUMN2_LAT = "latitudeSaved";
            public static final String COLUMN3_LONG = "longitudeSaved";
            public static final int DB_VERSION = 4;


            // SQL command statement that will be used to create the table

            public static final String SQL_CREATE_TABLE = "CREATE TABLE " +
                    SQLiteTable.TABLE_NAME + "(" + SQLiteTable._ID + " INTEGER PRIMARY KEY NOT NULL, " +
                    SQLiteTable.COLUMN1_MSG + " VARCHAR(255)," +
                    SQLiteTable.COLUMN2_LAT + " VARCHAR(255)," +
                    SQLiteTable.COLUMN3_LONG + " VARCHAR(255));";


            // test string that will be used as a SQL command to enter test information
            // in the table created and insert test data

            public static final String SQLITE_TABLE_INSERT = "INSERT INTO " + TABLE_NAME +
                    " (" + COLUMN1_MSG + ", " + COLUMN2_LAT + ", " + COLUMN3_LONG + ") VALUES (test1, test2, test3);";

            // final and static string that will be used when we wish to drop the table

            public static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + SQLiteTable.TABLE_NAME;


        } // end of the sqliteTable class


    } // end of the DBContract class








} // end of the enterMsg.java class
