package com.example.jq.assignment4_androidui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class activityThree extends AppCompatActivity
{

    // final List<String> stringList = new ArrayList<String>();
    private ArrayList<Integer> numbers = new ArrayList<Integer>();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_three);


        // Casting of the XML entities/properties created for this class
        Button btnClose3 = (Button) findViewById(R.id.btnClose3);
        Button numAdd = (Button) findViewById(R.id.addNumber);

        final GridView numGrid = (GridView) findViewById(R.id.gridList);
        numGrid.setAdapter(new ArrayAdapter<Integer>(this, R.layout.simple_list_item_1, numbers));


        /*
        final EditText edit = (EditText) findViewById(R.id.inputs);
        String[] array = getResources().getStringArray(R.array.integer_array);
        final List<String> stringList = new ArrayList<String>(Arrays.asList(array));
        */

        /*
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, stringList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        */


        btnClose3.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View arg0)
            {
                // close activity1
                finish();
            }

        }); // end of setOnClickListener



        numAdd.setOnClickListener(new Button.OnClickListener(){

            public void onClick(View v)
            {
                numbers.add(numbers.size());
                ((ArrayAdapter)numGrid.getAdapter()).notifyDataSetChanged();

            }

        });



        /*
        numAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String result = edit.getText().toString();
                stringList.add(stringList.size(), result);
                adapter.notifyDataSetChanged();
                // ((ArrayAdapter)spinner.getAdapter()).notifyDataChanged();

            }
        });
        */


    }
}
