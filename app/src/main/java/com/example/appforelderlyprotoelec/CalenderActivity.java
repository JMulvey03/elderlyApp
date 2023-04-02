package com.example.appforelderlyprotoelec;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appforelderlyprotoelec.HomeActivity;
import com.example.appforelderlyprotoelec.R;


public class CalenderActivity extends AppCompatActivity {

    String act;
    String hourSet;
    String minSet;
    ImageButton back;
    Button schedule;
    Spinner actSpin;
    Spinner minSpin;
    Spinner hourSpin;
    String[] activities = {"Choose activity to schedule..", "act1", "act2", "act3"};
    String[] hours = new String[24];
    String[] minutes = new String[60];

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calender_screen);

        //Screen Switch Code
        back = findViewById(R.id.backButton);
        back.setOnClickListener(v -> {
            Intent intentHome = new Intent(CalenderActivity.this, HomeActivity.class);

            startActivity(intentHome);
        });
        schedule = findViewById(R.id.confirm_sched);
        schedule.setOnClickListener(v -> {
            if(getAct() != "Choose activity to schedule..") {
                //add details to database.
            }else{
                Toast.makeText(this, "Please choose an activity.", Toast.LENGTH_SHORT).show();
            }
        });



        for (int i = 0; i < 24; i++) {
            hours[i] = String.format("%02d", i+1);
        }
        for (int i = 0; i < 60; i++) {
            minutes[i] = String.format("%02d", i+1);
        }

        //Spinner Code
        actSpin = findViewById(R.id.activityspinner);
        hourSpin = findViewById(R.id.hourspinner);
        minSpin = findViewById(R.id.minspinner);

        actSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), activities[position], Toast.LENGTH_SHORT).show();
                String actChoose = activities[position];
                setAct(actChoose);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        hourSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),hours[position], Toast.LENGTH_SHORT).show();
                String hourChoose = hours[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        minSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),minutes[position],Toast.LENGTH_SHORT).show();
                String minChoose = minutes[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<String> aAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, activities);
        aAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        actSpin.setAdapter(aAdapter);

        ArrayAdapter<String> hourAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,hours);
        hourAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hourSpin.setAdapter(hourAdapter);

        ArrayAdapter<String> minAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,minutes);
        minAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        minSpin.setAdapter(minAdapter);
    }

    public void setAct(String act){
        this.act = act;
    }
    public String getAct(){
        return act;
    }

    public void getHour(String hourSet){
        this.hourSet = hourSet;
    }
    public String getHour(){
        return hourSet;
    }

    public void setMin(String minSet){
        this.minSet = minSet;
    }
    public String getMin(){
        return minSet;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}