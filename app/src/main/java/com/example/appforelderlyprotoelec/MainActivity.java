package com.example.appforelderlyprotoelec;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.NestedScrollingChild;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.core.content.ContextCompat;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity {
    View popupView;

    String[] weather = {"cloudy", "rain", "sunny"};
    String curWeather, curLocation, curTemp;
    Button cloudyButton, sunnyButton, rainyButton;
    ImageButton but1, but2, but3, but4, but5, but6, but7, but8, daybut1, daybut2, daybut3, daybut4, daybut5, daybut6, daybut7, daybut8, backButton;
    ImageView currentWeather;
    TextView location, temperature, hour1, hour2, hour3, hour4, hour5, hour6, hour7, hour8, day1, day2, day3, day4, day5;
    HorizontalScrollView hs;
    NestedScrollView sv;

    TextView[] textViewArray, dayTextArray;
    ImageButton[] imageButtonArrayForecast, imageButtonArrayDaysForecast;
    private final String url = "https://api.openweathermap.org/data/2.5/weather";
    private final String urlForecast = "https://pro.openweathermap.org/data/2.5/forecast/hourly?lat=44.34&lon=10.99&appid=b5649972911c2d2ed660bcbb9fe073de";
    private final String appid = "b5649972911c2d2ed660bcbb9fe073de";
    DecimalFormat df = new DecimalFormat("#");
    private HashMap<String, List<DayTime>> scheduleMap;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);  // Inflate the layout
        ConstraintLayout cl = findViewById(R.id.home);
        RelativeLayout rl = (RelativeLayout) cl.findViewById(R.id.relativeLayout);
        RelativeLayout rl2 = (RelativeLayout) cl.findViewById(R.id.relativeLayout2);
        dbHelper = new DatabaseHelper(this);
        sv = (NestedScrollView) rl2.findViewById(R.id.scrollView);
        hs = (HorizontalScrollView) rl.findViewById(R.id.horizontalScrollView);
        cloudyButton = findViewById(R.id.cloudyButton);
        sunnyButton = findViewById(R.id.sunnyButton);
        rainyButton = findViewById(R.id.rainyButton);
        backButton = findViewById(R.id.backButton);
        but1 = findViewById(R.id.button);
        but2 = findViewById(R.id.button1);
        but3 = findViewById(R.id.button2);
        but4 = findViewById(R.id.button3);
        but5 = findViewById(R.id.button4);
        but6 = findViewById(R.id.button5);
        but7 = findViewById(R.id.button6);
        but8 = findViewById(R.id.button7);
        daybut1 = findViewById(R.id.dayButton);
        daybut2 = findViewById(R.id.dayButton1);
        daybut3 = findViewById(R.id.dayButton2);
        daybut4 = findViewById(R.id.dayButton3);
        daybut5 = findViewById(R.id.dayButton4);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("My notification","my notification",NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager=getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        currentWeather = (ImageView) findViewById(R.id.currentWeatherView);
        location = findViewById(R.id.location);
        temperature = findViewById(R.id.currentTemp);
        curLocation = getMyCurrentLocation();
        location.setText(curLocation);
        getWeatherDetails();
        getHourlyForecast();
        get5DayForecast();
        hour1 = (TextView) hs.findViewById(R.id.hour1);
        hour2 = (TextView) hs.findViewById(R.id.hour2);
        hour3 = (TextView) hs.findViewById(R.id.hour3);
        hour4 = (TextView) hs.findViewById(R.id.hour4);
        hour5 = (TextView) hs.findViewById(R.id.hour5);
        hour6 = (TextView) hs.findViewById(R.id.hour6);
        hour7 = (TextView) hs.findViewById(R.id.hour7);
        hour8 = (TextView) hs.findViewById(R.id.hour8);

        day1 = (TextView) sv.findViewById(R.id.day1text);
        day2 = (TextView) sv.findViewById(R.id.day2text);
        day3 = (TextView) sv.findViewById(R.id.day3text);
        day4 = (TextView) sv.findViewById(R.id.day4text);
        day5 = (TextView) sv.findViewById(R.id.day5text);


        dayTextArray = new TextView[]{day1, day2, day3, day4, day5};
        textViewArray = new TextView[]{hour1, hour2, hour3, hour4, hour5, hour6, hour7, hour8};
        imageButtonArrayForecast = new ImageButton[]{but1, but2, but3, but4, but5, but6, but7, but8};
        imageButtonArrayDaysForecast = new ImageButton[]{daybut1, daybut2, daybut3, daybut4, daybut5};
        setClickListener5DayForecast(imageButtonArrayDaysForecast);
        setClickListenerHoursForecast(imageButtonArrayForecast);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDateTime lt = LocalDateTime.now();
            ZonedDateTime zt = ZonedDateTime.now();
            System.out.println("LOCAL" + zt);
            hour1.setText("Now");
            makeDays(lt, dayTextArray);
            makeHours(lt, textViewArray);
        }
        //LocalDateTime lt = LocalDateTime.now();

        //replaceFragment(new cloudyFragment());
        rainyButton.setOnClickListener(view -> replaceFragment(new RainyFragment()));
        cloudyButton.setOnClickListener(view -> replaceFragment(new cloudyFragment()));
        sunnyButton.setOnClickListener(view -> replaceFragment(new sunnyFragment()));

        backButton.setOnClickListener(v -> {
            Intent intentHome = new Intent(MainActivity.this, HomeActivity.class);

            startActivity(intentHome);
        });
    }

    private void setClickListener5DayForecast(ImageButton[] imageButtonArrayDaysForecast) {
        HashMap<String, List<DayTime>> scheduleMap = new HashMap<>();
        for (int i = 0; i < imageButtonArrayDaysForecast.length; i++) {
            int index = i;
            this.imageButtonArrayDaysForecast[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LayoutInflater inflater = (LayoutInflater)
                            getSystemService(LAYOUT_INFLATER_SERVICE);


                    View popupView = inflater.inflate(R.layout.popup_schedule_activity_days, null);
                    findViewById(R.id.home).setAlpha((float) 0.7);
                    //rb = (RatingBar) popupView.findViewById(R.id.ratingBarDifficulty);
                    TimePicker tm = (TimePicker) popupView.findViewById(R.id.simpleTimePicker);
                    Button save = (Button) popupView.findViewById(R.id.saveButton);
                    TextInputEditText activityText = popupView.findViewById(R.id.activity_input);

                    // create the popup window

                    int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                    int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                    boolean focusable = true; // lets taps outside the popup also dismiss it
                    final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
                    save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            save.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.light_green));
                            save.setText("Scheduled");
                            save.invalidate();

                            String day = dayTextArray[index].getText().toString();
                            int hour = tm.getHour();
                            int minute = tm.getMinute();
                            String activity = activityText.getText().toString();

                            Schedule schedule = new Schedule();
                            schedule.setDay(day);
                            schedule.setTime(hour + ":" + minute);
                            schedule.setActivity(activity);

                            // Set the alarm time to 30 minutes before the scheduled time
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTimeInMillis(System.currentTimeMillis());
                            //calendar.set(Calendar.DAY_OF_WEEK, index + 1);
                            calendar.set(Calendar.HOUR_OF_DAY, tm.getHour());
                            calendar.set(Calendar.MINUTE, tm.getMinute() - 30);
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

                                    NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, "My notification");
                                    builder.setContentTitle("Silver Strength");
                                    builder.setContentText("You have " + activity + " scheduled in 30 minutes! Get ready!");
                                    builder.setSmallIcon(R.drawable.fire);
                                    builder.setAutoCancel(true);

                                    NotificationManagerCompat managerCompat = NotificationManagerCompat.from(MainActivity.this);
                                    if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
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

                                    dbHelper.addActivity(schedule);
                                    printActivitiesFromDatabase(); // Call the method to print data to the console
                                }
                            }).start();

                            popupWindow.dismiss();
                        }

                    });

                    popupWindow.setElevation(50);
                    popupWindow.setAnimationStyle(-1);
                    popupWindow.setHeight(1500);
                    popupWindow.setWidth(1010);
                    popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
                    popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            findViewById(R.id.home).setAlpha((float) 1.0);

                        }

                    });
                }
            });
        }
    }



    private void setClickListenerHoursForecast(ImageButton[] imageButtonArrayForecast) {
        HashMap<String, List<DayTime>> scheduleMap = new HashMap<>();
        for(int i =0;i<imageButtonArrayForecast.length;i++){
            int index =i;
            this.imageButtonArrayForecast[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LayoutInflater inflater = (LayoutInflater)
                            getSystemService(LAYOUT_INFLATER_SERVICE);
                    View popupView = inflater.inflate(R.layout.popup_schedule_activity_hours, null);
                    findViewById(R.id.home).setAlpha((float) 0.7);
                    //rb = (RatingBar) popupView.findViewById(R.id.ratingBarDifficulty);
                    Button save = (Button)popupView.findViewById(R.id.saveButton);
                    TextInputEditText activityText= popupView.findViewById(R.id.activity_input);

                    // create the popup window

                    int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                    int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                    boolean focusable = true; // lets taps outside the popup also dismiss it
                    final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
                    save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            save.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.light_green));
                            save.setText("Scheduled");
                            save.invalidate();

                            String day = dayTextArray[0].getText().toString();
                            String hourString = String.valueOf(textViewArray[index].getText());
                            String temp[]=hourString.split(":");
                            int hour= Integer.parseInt(temp[0]);
                            int minute= Integer.parseInt(temp[1]);
                            String activity =activityText.toString();
                            LocalTime time = null;
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                time = LocalTime.of(hour, minute);
                            }
                            DateTimeFormatter formatter = null;
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                formatter = DateTimeFormatter.ofPattern("HH:mm");
                            }
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

                                DayTime dayTime= new DayTime(day, time,activity);
                                if (scheduleMap.containsKey(day)) {
                                    // If it does, add the new DayTime object to the list for that day
                                    List<DayTime> scheduleList = scheduleMap.get(day);
                                    scheduleList.add(dayTime);
                                } else {
                                    // If it doesn't, create a new list with the DayTime object and put it in the map
                                    List<DayTime> scheduleList = new ArrayList<>();
                                    scheduleList.add(dayTime);
                                    scheduleMap.put(day, scheduleList);
                                }

                                // Print the contents of the map for verification
                                System.out.println(scheduleMap);

                                //System.out.println(day+" "+formattedTime);
                            }

                            //popupWindow.dismiss();

                        }
                    });
                    popupWindow.setElevation(50);
                    popupWindow.setAnimationStyle(-1);
                    popupWindow.setHeight(1500);
                    popupWindow.setWidth(1010);
                    popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
                    popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            findViewById(R.id.home).setAlpha((float) 1.0);

                        }

                    });
                }
            });
        }
    }


    private void makeDays(LocalDateTime lt, TextView[] dayTextArray) {
        for(int i = 0; i< this.dayTextArray.length; i++){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                this.dayTextArray[i].setText(formatDay(lt.plusDays(i)));
            }
        }

    }

    private String  formatDay(LocalDateTime plusDays) {
        String strDate="";
        DateTimeFormatter formatter = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("E");
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String formatDateTime = plusDays.format(formatter);
            strDate=formatDateTime;
        }

        return strDate;
    }


    private String getMyCurrentLocation() {
        curLocation="Galway,Ireland";
        return curLocation;
    }


    private void setCurrentWeatherImage(ImageView currentWeather,String iconCode) throws IOException {
        String url = "http://openweathermap.org/img/w/" + iconCode + ".png";
        DownloadImageTask task = new DownloadImageTask(currentWeather);
        task.execute(url);


    }

    private void setForecastWeatherImage(ImageButton currentWeather,String iconCode) throws IOException {
        String url = "http://openweathermap.org/img/w/" + iconCode + ".png";
        DownloadImageTask task = new DownloadImageTask(currentWeather);
        task.execute(url);


    }

    private void set5DayForecastWeatherImage(ImageButton currentWeather,String iconCode) throws IOException {
        String url = "http://openweathermap.org/img/w/" + iconCode + ".png";
        DownloadImageTask task = new DownloadImageTask(currentWeather);
        task.execute(url);


    }


    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager= getSupportFragmentManager();
        FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();

    }

    public void makeHours(LocalDateTime lt, TextView[] textViewArray){
        for(int i = 1; i< this.textViewArray.length; i++){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                this.textViewArray[i].setText(formatDate(lt.plusHours(i)));
                System.out.println("HOURS" +formatDate(lt.plusHours(i)));
            }
        }
    }

    public String formatDate(LocalDateTime date){
        String strDate="";
        DateTimeFormatter formatter = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("HH");
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String formatDateTime = date.format(formatter);
            strDate=formatDateTime+":00";
        }
        return strDate;
    }

    private void get5DayForecast(){
        String tempurl = "";
        String cityCountry = getMyCurrentLocation();
        String[] cityCountryArray= cityCountry.split(",");
        String city =cityCountryArray[0];
        String country = cityCountryArray[1];
        tempurl = "https://api.openweathermap.org/data/2.5/forecast?q=" + city + "," + country + "&units=metric&cnt=5&appid=" + appid; // update the URL to retrieve 5-day forecast
        StringRequest stringRequest = new StringRequest(Request.Method.GET, tempurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Log.d("response",response);
                String output="";
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jsonArray = jsonResponse.getJSONArray("list");

                    // Get the forecast for the next 5 days
                    for (int i = 0; i < jsonArray.length(); i++) { // update the loop to retrieve all the forecast data for 5 days
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        JSONObject jsonObjectMain = jsonObject.getJSONObject("main");
                        double temp = jsonObjectMain.getDouble("temp");
                        JSONArray jsonArrayWeather = jsonObject.getJSONArray("weather");
                        JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);
                        String description = jsonObjectWeather.getString("description");
                        String icon = jsonObjectWeather.getString("icon");
                        if (icon.charAt(icon.length() - 1) == 'n') {
                            // Replace the last character with "d" to get the daytime version of the icon
                            icon = icon.substring(0, icon.length() - 1) + "d";
                        }
                        set5DayForecastWeatherImage(imageButtonArrayDaysForecast[i],icon);

                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString().trim(),Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }
    private void getHourlyForecast(){

        String tempurl = "";
        String cityCountry = getMyCurrentLocation();
        String[] cityCountryArray= cityCountry.split(",");
        String city =cityCountryArray[0];
        String country = cityCountryArray[1];
        tempurl = "https://api.openweathermap.org/data/2.5/forecast?q=" + city + "," + country + "&units=metric&appid=" + appid;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, tempurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Log.d("response",response);
                String output="";
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jsonArray = jsonResponse.getJSONArray("list");

                    // Get the hourly forecast for the next 4 hours
                    for (int i = 0; i < imageButtonArrayForecast.length; i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        JSONObject jsonObjectMain = jsonObject.getJSONObject("main");
                        double temp = jsonObjectMain.getDouble("temp");
                        JSONArray jsonArrayWeather = jsonObject.getJSONArray("weather");
                        JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);
                        String description = jsonObjectWeather.getString("description");
                        String icon = jsonObjectWeather.getString("icon");
                        if (icon.charAt(icon.length() - 1) == 'n') {
                            // Replace the last character with "d" to get the daytime version of the icon
                            icon = icon.substring(0, icon.length() - 1) + "d";
                        }
                        setForecastWeatherImage(imageButtonArrayForecast[i],icon);

                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString().trim(),Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
    public void getWeatherDetails(){
        String tempurl = "";
        String cityCountry = getMyCurrentLocation();
        String[] cityCountryArray= cityCountry.split(",");
        String city =cityCountryArray[0];
        String country = cityCountryArray[1];
        tempurl = url+"?q="+city+ ","+country+"&appid="+appid;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, tempurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Log.d("response",response);
                String output="";
                try {
                    JSONObject jsonResponse=new JSONObject(response);
                    JSONArray jsonArray= jsonResponse.getJSONArray("weather");
                    JSONObject jsonObjectWeather=jsonArray.getJSONObject(0);
                    String description=jsonObjectWeather.getString("description");
                    String condition = jsonObjectWeather.getString("main");
                    String icon = jsonObjectWeather.getString("icon");
                    if (icon.charAt(icon.length() - 1) == 'n') {
                        // Replace the last character with "d" to get the daytime version of the icon
                        icon = icon.substring(0, icon.length() - 1) + "d";
                    }
                    JSONObject jsonObjectMain= jsonResponse.getJSONObject("main");
                    double temp = jsonObjectMain.getDouble("temp")-273.15;
                    JSONObject jsonObjectSys = jsonResponse.getJSONObject("sys");
                    //String countryName = jsonObjectSys.getString("country");
                    String cityName=  jsonResponse.getString("name");
                    output =df.format(temp) +"°C";
                    temperature.setText(output);

                    setCurrentWeatherImage(currentWeather,icon);


                     if(condition.equalsIgnoreCase("clouds")||condition.equalsIgnoreCase("fog")){
                         replaceFragment(new cloudyFragment());
                     }

                     else if(condition.equalsIgnoreCase("rain")||condition.equalsIgnoreCase("snow")||condition.equalsIgnoreCase("drizzle")||condition.equalsIgnoreCase("thunderstorm")||condition.equalsIgnoreCase("mist")){
                         replaceFragment(new RainyFragment());
                     }

                     else if(condition.equalsIgnoreCase("clear")){
                         replaceFragment(new sunnyFragment());

                     }

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString().trim(),Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        private View view;

        public DownloadImageTask(View view) {
            this.view = view;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            OkHttpClient client = new OkHttpClient();
            okhttp3.Request request = new okhttp3.Request.Builder()
                    .url(urls[0])
                    .build();
            okhttp3.Response response;
            try {
                response = client.newCall(request).execute();
                InputStream inputStream = response.body().byteStream();
                return BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (view instanceof ImageView) {
                ((ImageView) view).setImageBitmap(result);
            } else if (view instanceof ImageButton) {
                ((ImageButton) view).setImageBitmap(result);
            }
        }
    }
    private void printActivitiesFromDatabase() {
        List<String> activities = dbHelper.getAllActivities();
        for (String activity : activities) {
            System.out.println(activity);
        }

    }

}

