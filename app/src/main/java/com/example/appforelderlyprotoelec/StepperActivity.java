package com.example.appforelderlyprotoelec;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class StepperActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor stepSensor;
    ImageButton back;
    TextView tv_stepsTaken;
    TextView calSpent;
    TextView distTravelled;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.step_screen);
        tv_stepsTaken = findViewById(R.id.currentsteps);
        calSpent = findViewById(R.id.calories);
        distTravelled = findViewById(R.id.distance);
        back = findViewById(R.id.backButton);

//            loadData();

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

        back.setOnClickListener(v -> {
            Intent intentHome = new Intent(StepperActivity.this, HomeActivity.class);

            startActivity(intentHome);
        });
    }
    @Override
    protected void onResume() {
        super.onResume();

        sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL);

        if (stepSensor == null) {
            Toast.makeText(this, "No sensor detected on this device", Toast.LENGTH_SHORT).show();
        } else {
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    protected void onStop(){
        super.onStop();
        sensorManager.unregisterListener(this, stepSensor);
    }

    private long steps;
    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        float[] values = event.values;
        int value = -1;

        if(values.length > 0){
            value = (int) values[0];
        }

        if (sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            steps++;
            double calories = calcCalories(steps);
            double dist = calcDistance(steps);
                Toast.makeText(this ,"Stepper is Running", Toast.LENGTH_SHORT).show();
                tv_stepsTaken.setText(steps + " " + getString(R.string.ideal_steps));
                calSpent.setText(calories + " " + getString(R.string.calories));
                distTravelled.setText(dist + " " + getString(R.string.metres));
        }
    }

//    private void saveData() {
//        Context context = getApplicationContext();
//        SharedPreferences sharedPreferences = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putFloat("key1", previousTotalSteps);
//        editor.apply();
//    }


//    private void loadData() {
//        Context context = getApplicationContext();
//        SharedPreferences sharedPreferences = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
//        float savedNumber = sharedPreferences.getFloat("key1", 0f);
//        Log.d("StepperActivity", String.valueOf(savedNumber));
//        previousTotalSteps = savedNumber;
//    }

    public double calcCalories(long steps){
        double cal = steps * 0.063;
        return cal;
    }

    public double calcDistance(long steps){
        double distance = steps * 2;
        return distance;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // We do not have to write anything in this function for this app
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}