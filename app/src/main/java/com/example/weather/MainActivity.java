package com.example.weather;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.weather.ModalClass.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*  Free API from AccuWeather is used therefore API call is limited to 50.
    Data are obtained and set to show only formatting to required format is done.
    Data might be in-accurate or their might be some delay or error in fetching and displaying the data.

    No Database is used data is fetched in JSON Format and displayed directly to the user, without storing it.
 */

public class MainActivity extends AppCompatActivity {

    Context context;
    Weather weather;
    ListView listView;
    public List<Weather> modalList;                                                         //Data Members declaration
    ListAdaptor listAdaptor;

    ImageView dayNight, searchCity;                                                         //Data Members declaration
    TextView temp, sunRise, sunSet, moonRise, moonSet, humidity, visibility,                //Data Members declaration
            dew, wind, airPress, uv, date_Time, hText, wea_con, realFeel, city, tem;

    String hlText = "";                                                                     //Data Members declaration
    SharedPreferences sharedPref;                                                           //Data Members declaration
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = MainActivity.this;                                                        //Data Members initialization
        city = findViewById(R.id.city);                                                     //Data Members initialization
        searchCity = findViewById(R.id.search_btn);
        realFeel = findViewById(R.id.real_feel);
        wea_con = findViewById(R.id.wea_con);
        dayNight = findViewById(R.id.day_night);
        temp = findViewById(R.id.temperature);
        sunRise = findViewById(R.id.sunrise);
        sunSet = findViewById(R.id.sunset);
        moonRise = findViewById(R.id.moonrise);
        moonSet = findViewById(R.id.moonset);
        humidity = findViewById(R.id.humidity);
        visibility = findViewById(R.id.visibility);
        dew = findViewById(R.id.dew);
        wind = findViewById(R.id.windspeed);
        airPress = findViewById(R.id.air_pressure);
        uv = findViewById(R.id.uv_value);
        date_Time = findViewById(R.id.date_time);
        hText = findViewById(R.id.text);
        tem = findViewById(R.id.tem);

        listView = findViewById(R.id.week_record);
        modalList = new ArrayList<>();                                                              //Data Members initialization

        sharedPref = context.getSharedPreferences("LocationKey", Context.MODE_PRIVATE);       //Storing City Key in Shared Preferences
        editor = sharedPref.edit();
        //Setting Up Data
        editor.putString("CityKey", "206678");
        editor.putString("City", "Lucknow, UP");
        editor.commit();

        city.setText(sharedPref.getString("City", null));

        Dialog dialog = new Dialog(context);

        searchCity.setOnClickListener(v -> {                                                        //City Selection Operation
            dialog.setContentView(R.layout.city_layout);
            EditText data = dialog.findViewById(R.id.enter_city);
            Button btn = dialog.findViewById(R.id.search_begin);

            try {
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        searchCityCode(data.getText().toString());
                        dialog.dismiss();
                    }
                });
                dialog.show();
            } catch (Exception e) {
                Toast.makeText(context, "Maximum API Called. Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        try {
            fetchWeatherData();                                                                         //Obtaining 5 Day's Weathear Data
            getCurrentCond();
        } catch (Exception e) {
            Toast.makeText(context, "Maximum API Call Recieved. Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }                                                                      //Obtaining Current Weather Information of the Current City

    }

    //Collecting City Key Data using AccuWeather API through Volley
    //City Selection Process i.e Function

    private void searchCityCode(String toString) {
        String url = "https://dataservice.accuweather.com/locations/v1/cities/autocomplete?apikey=e3Tan6Yhiv9NLZ4y8pmmOy7X23ihKTCa&q=" + toString;
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONArray(response).getJSONObject(0);
                    String key = jsonObject.get("Key").toString(),
                            currCity = jsonObject.get("LocalizedName").toString() + ", " + jsonObject.getJSONObject("AdministrativeArea").get("LocalizedName").toString();
                    //Setting Up Data
                    editor.putString("CityKey", key);
                    editor.putString("City", currCity);
                    editor.commit();
                    city.setText(sharedPref.getString("City", null));
                    try {
                        fetchWeatherData();                                                                         //Obtaining 5 Day's Weathear Data
                        getCurrentCond();
                    } catch (Exception e) {
                        Toast.makeText(context, "Maximum API Call Recieved. Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }                                                                      //Obtaining Current Weather Information of the Current City
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();             //Displaying if any Error produced
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();             //Displaying if any Error produced
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());                //Fetching Data through API using Volley Dependency
        requestQueue.add(request);
    }

    //Collecting Current City Weather Data using AccuWeather API through Volley
    private void getCurrentCond() {
        String url = "https://dataservice.accuweather.com/currentconditions/v1/" + sharedPref.getString("CityKey", "206678") + "?apikey=e3Tan6Yhiv9NLZ4y8pmmOy7X23ihKTCa&details=true";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONArray(response).getJSONObject(0);
                    if (jsonObject.get("IsDayTime").toString() == "false")
                        dayNight.setImageResource(R.drawable.moon);
                    String dateTime = jsonObject.get("LocalObservationDateTime").toString(),
                            text = jsonObject.get("WeatherText").toString(),
                            realFe = jsonObject.getJSONObject("RealFeelTemperature").getJSONObject("Metric").get("Value").toString(),
                            tempe = jsonObject.getJSONObject("Temperature").getJSONObject("Metric").get("Value").toString(),
                            humi = jsonObject.get("RelativeHumidity").toString(),
                            visi = jsonObject.getJSONObject("Visibility").getJSONObject("Metric").get("Value").toString(),
                            Dew = jsonObject.getJSONObject("DewPoint").getJSONObject("Metric").get("Value").toString(),
                            Wind = jsonObject.getJSONObject("Wind").getJSONObject("Speed").getJSONObject("Metric").get("Value").toString()
                                    + "km/h/" + jsonObject.getJSONObject("Wind").getJSONObject("Direction").get("Degrees").toString()
                                    + "°" + jsonObject.getJSONObject("Wind").getJSONObject("Direction").get("English").toString(),
                            airPressure = jsonObject.getJSONObject("Pressure").getJSONObject("Metric").get("Value").toString(),
                            uvI = jsonObject.get("UVIndex").toString() + "/" + jsonObject.get("UVIndexText").toString();

                    //Formatting TimeStamp to required format of Date and Time using Built-in functions
                    String dateStr = "";
                    try {
                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date d = dateFormat.parse(dateTime.substring(0, 10));
                        DateFormat formatter = new SimpleDateFormat("HH:mm EEE,MMM d,''yy");
                        dateStr = formatter.format(d);
                    } catch (Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();         //Displaying if any Error produced
                    }
                    //Setting Up Data
                    date_Time.setText("Last Update : " + dateStr);
                    hText.setText(hlText);
                    wea_con.setText(text);
                    temp.setText(tempe + "°");
                    realFeel.setText("Feel " + realFe + "°");
                    humidity.setText(humi + "%");
                    visibility.setText(visi + "km");
                    dew.setText(Dew + "%");
                    wind.setText(Wind);
                    airPress.setText(airPressure + "mb");
                    uv.setText(uvI);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();             //Displaying if any Error produced
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();             //Displaying if any Error produced
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());                //Fetching Data through API using Volley Dependency
        requestQueue.add(request);
    }

    //Collecting 5 Day's Data using AccuWeather API through Volley
    private void fetchWeatherData() {
        modalList.clear();
        String url = "https://dataservice.accuweather.com/forecasts/v1/daily/5day/" + sharedPref.getString("CityKey", "206678") + "?apikey=e3Tan6Yhiv9NLZ4y8pmmOy7X23ihKTCa&details=true&metric=true";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    hlText = jsonObject.getJSONObject("Headline").get("Text").toString();
                    JSONArray jsonArray = jsonObject.getJSONArray("DailyForecasts");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String date = jsonObject1.get("Date").toString(),
                                minTemp = jsonObject1.getJSONObject("Temperature").getJSONObject("Minimum").
                                        get("Value").toString(),
                                maxTemp = jsonObject1.getJSONObject("Temperature").getJSONObject("Maximum").
                                        get("Value").toString(),
                                sunRi = jsonObject1.getJSONObject("Sun").get("Rise").toString(),
                                sunSe = jsonObject1.getJSONObject("Sun").get("Set").toString(),
                                moonRi = jsonObject1.getJSONObject("Moon").get("Rise").toString(),
                                moonSe = jsonObject1.getJSONObject("Moon").get("Set").toString(),
                                wind = jsonObject1.getJSONObject("Day").getJSONObject("Wind").
                                        get("Speed").toString() + "/" + jsonObject1.getJSONObject("Night").getJSONObject("Wind").
                                        get("Speed").toString(),
                                uv = jsonObject1.getJSONObject("Day").getJSONObject("SolarIrradiance").get("Value").toString(),
                                rain = jsonObject1.getJSONObject("Day").get("RainProbability").toString() + "/" + jsonObject1.getJSONObject("Night").get("RainProbability").toString(),
                                day = jsonObject1.getJSONObject("Day").get("IconPhrase").toString(),
                                night = jsonObject1.getJSONObject("Night").get("IconPhrase").toString();

                        //Setting Up Data
                        tem.setText("Max. Temp. :" + maxTemp + "°C / Min. Temp. :" + minTemp + "°C");
                        sunRise.setText(sunRi.substring(11, 16));
                        sunSet.setText(sunSe.substring(11, 16));
                        moonRise.setText(moonSe.substring(11, 16));
                        moonSet.setText(moonRi.substring(11, 16));

                        weather = new Weather(date, minTemp, maxTemp, sunRi, sunSe, moonRi, moonSe, wind, uv, rain, day, night);
                        modalList.add(weather);
                    }
                    listAdaptor = new ListAdaptor(context, modalList);
                    listView.setAdapter(listAdaptor);
                } catch (JSONException e) {
                    e.printStackTrace();
                    //Log.i("JSON Error : ", e.toString());
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();             //Displaying if any Error produced
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.i("VolleyError : ", error.toString());
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();             //Displaying if any Error produced
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());                //Fetching Data through API using Volley Dependency
        requestQueue.add(request);
    }
}