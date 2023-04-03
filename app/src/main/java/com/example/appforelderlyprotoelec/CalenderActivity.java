package com.example.appforelderlyprotoelec;


import android.content.Intent;
import android.content.pm.PackageManager;
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
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.appforelderlyprotoelec.HomeActivity;
import com.example.appforelderlyprotoelec.R;

import java.nio.channels.DatagramChannel;
import java.util.Calendar;


public class CalenderActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    String act;
    String hourSet;
    String minSet;
    ImageButton back;
    Button schedule;
    Spinner actSpin;
    Spinner minSpin;
    Spinner hourSpin;
    String[] activities = {"Choose activity to schedule..", "Walk", "Squats", "Sit Ups"};
    String[] hours = new String[24];
    String[] minutes = new String[61];

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
                Toast.makeText(this, getAct() + " is scheduled for " + getHour() + ":" + getMin(), Toast.LENGTH_SHORT).show();
//                String day = dayTextArray[index].getText().toString();
                int hour = Integer.parseInt(getHour());
                int minute = Integer.parseInt(getMin());
                String activity = getAct();

                Schedule schedule_time = new Schedule();
//                schedule.setDay(day);
                schedule_time.setTime(hour + ":" + minute);
                schedule_time.setActivity(activity);

                // Set the alarm time to 30 minutes before the scheduled time
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                //calendar.set(Calendar.DAY_OF_WEEK, index + 1);
                calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(getHour()));
                calendar.set(Calendar.MINUTE, Integer.parseInt(getMin()) - 30);
                calendar.add(Calendar.MINUTE, -30);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        long timeDiff = calendar.getTimeInMillis() - System.currentTimeMillis();
                        try {
                            Thread.sleep(30000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        NotificationCompat.Builder builder = new NotificationCompat.Builder(CalenderActivity.this, "My notification");
                        builder.setContentTitle("Silver Strength");
                        builder.setContentText("You have " + activity + " scheduled in 30 minutes! Get ready!");
                        builder.setSmallIcon(R.drawable.fire);
                        builder.setAutoCancel(true);

                        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(CalenderActivity.this);
                        if (ActivityCompat.checkSelfPermission(CalenderActivity.this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        managerCompat.notify(1, builder.build());

                        dbHelper.addActivity(schedule_time);
                    }
                }).start();
            }else{
                Toast.makeText(this, "Please choose an activity.", Toast.LENGTH_SHORT).show();
            }
        });



        for (int i = 0; i < 24; i++) {
            hours[i] = String.format("%02d", i+1);
        }
        for (int i = 0; i < 61; i++) {
            minutes[i] = String.format("%02d", i);
        }

        //Spinner Code
        actSpin = findViewById(R.id.activityspinner);
        hourSpin = findViewById(R.id.hourspinner);
        minSpin = findViewById(R.id.minspinner);

        actSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
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
                String hourChoose = hours[position];
                setHour(hourChoose);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        minSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String minChoose = minutes[position];
                setMin(minChoose);
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

    public void setHour(String hourSet){
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