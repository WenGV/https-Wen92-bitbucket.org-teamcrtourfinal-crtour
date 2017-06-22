package com.projectandroid.kevinjimezb.appcrtour;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private ImageButton ImgBtnMarket;
    private ImageButton ImgBtnHotel;
    private ImageButton ImgBtnResort;
    private ImageButton ImgBtnShip;
    private ImageButton ImgBtnOther;
    private ImageButton ImgBtnFood;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImgBtnMarket = (ImageButton) findViewById(R.id.imgbtnMarket);
        ImgBtnHotel = (ImageButton) findViewById(R.id.imgbtnHotel);
        ImgBtnResort = (ImageButton) findViewById(R.id.imgbtnResort);
        ImgBtnShip = (ImageButton) findViewById(R.id.imgbtnShip);
        ImgBtnOther = (ImageButton) findViewById(R.id.imgbtnOther);
        ImgBtnFood = (ImageButton) findViewById(R.id.imgbtnFood);

        ImgBtnMarket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent =  new Intent(MainActivity.this, CRMap.class);
                myIntent.putExtra("MarkerType", "3");
                startActivity(myIntent);
            }
        });

        ImgBtnHotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent =  new Intent(MainActivity.this, CRMap.class);
                myIntent.putExtra("MarkerType", "1");
                startActivity(myIntent);
            }
        });

        ImgBtnResort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent =  new Intent(MainActivity.this, CRMap.class);
                myIntent.putExtra("MarkerType", "2");
                startActivity(myIntent);
            }
        });

        ImgBtnShip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent =  new Intent(MainActivity.this, CRMap.class);
                myIntent.putExtra("MarkerType", "5");
                startActivity(myIntent);
            }
        });
        ImgBtnOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent other =  new Intent(MainActivity.this, CRMap.class);
                other.putExtra("MarkerType", "6");
                startActivity(other);
            }
        });

        ImgBtnFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent =  new Intent(MainActivity.this, CRMap.class);
                myIntent.putExtra("MarkerType", "4");
                startActivity(myIntent);
            }
        });
        // Spinner element
        Spinner spinner = (Spinner) findViewById(R.id.spFilterCRTour);

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Guanacaste");
        categories.add("San Jose");
        categories.add("Alajuela");
        categories.add("Cartago");
        categories.add("Heredia");
        categories.add("Puntarenas");
        categories.add("Limon");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Has mas placentero tu viaje con CRTour!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Salir:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> province, View view, int position, long l) {
        // On selecting a spinner item
        String item = province.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(province.getContext(), "Selected province: " + item, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}
