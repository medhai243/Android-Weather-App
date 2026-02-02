package com.example.medhaweatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;


public class MainActivity extends AppCompatActivity {

    TextView date;
    SeekBar seekbar;
    TextView location;
    TextView temp;
    TextView feelsLike;
    TextView description;
    TextView windSpeed;
    ImageView image;
    EditText zipCodeText;
    Button zipButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        zipCodeText = findViewById(R.id.zipCode_text);
        zipButton = findViewById(R.id.zipCode_button);

        zipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String zip = zipCodeText.getText().toString();
                AsyncThread myThread = new AsyncThread();
                myThread.execute(zip);
            }
        });

    }

    public class AsyncThread extends AsyncTask<String, Void, Void>
    {
        @Override
        protected Void doInBackground(String... strings)  {
            //setting up JSON object
            URL url;
            URLConnection urlConnection;
            InputStream inputStream;
            String zip = strings[0];
            try {
                url = new URL("https://api.openweathermap.org/data/2.5/forecast?zip="+zip+",US&appid=c7331ed7bc9eb7f88fd11d1b9786037f");
                urlConnection = url.openConnection();
                inputStream = urlConnection.getInputStream();
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String inputString;
            JSONObject weatherData;
            try {
                inputString = bufferedReader.readLine();
                weatherData = new JSONObject(inputString);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            //updating screen
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //widgets
                    date = findViewById(R.id.date_text);
                    seekbar = findViewById(R.id.seekBar);
                    location = findViewById(R.id.location_text);
                    temp = findViewById(R.id.temp_text);
                    feelsLike = findViewById(R.id.feelslike_text);
                    description = findViewById(R.id.description_text);
                    windSpeed = findViewById(R.id.wind_text);
                    image = findViewById(R.id.image);

                    //time conversion
                    LocalTime timeOfDay = null;
                    String formattedTime = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        try {
                            timeOfDay = Instant.ofEpochSecond(weatherData.getJSONArray("list").getJSONObject(0).getInt("dt"))
                                    .atOffset(ZoneOffset.ofTotalSeconds(weatherData.getJSONObject("city").getInt("timezone")))
                                    .toLocalTime();
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a");
                            formattedTime = timeOfDay.format(formatter);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    //default values
                    try {
                        date.setText(formattedTime+"");
                        location.setText(weatherData.getJSONObject("city").getString("name")+", US");

                        double temperatureK = weatherData.getJSONArray("list").getJSONObject(0).getJSONObject("main").getDouble("temp");
                        temp.setText((Math.round((temperatureK-273)*9/5.0+32))+"°F");

                        double feelsLikeK = weatherData.getJSONArray("list").getJSONObject(0).getJSONObject("main").getInt("feels_like");

                        String weatherType = weatherData.getJSONArray("list").getJSONObject(0).getJSONArray("weather").getJSONObject(0).getString("description");
                        description.setText(weatherType);

                        feelsLike.setText("feels like "+(Math.round((feelsLikeK-273)*9/5.0+32))+"°F");

                        double windMS = weatherData.getJSONArray("list").getJSONObject(0).getJSONObject("wind").getDouble("speed");
                        BigDecimal round = new BigDecimal(Double.toString(windMS*2.237));
                        round = round.setScale(1, RoundingMode.HALF_UP);
                        windSpeed.setText("wind speed is "+(round.doubleValue())+" mph");

                        switch(weatherType)
                        {
                            case "clear sky":
                                image.setImageResource(R.drawable.sun);
                                break;
                            case "rain":
                                image.setImageResource(R.drawable.rain);
                                break;
                            case "shower rain":
                                image.setImageResource(R.drawable.rain);
                                break;
                            case "light rain":
                                image.setImageResource(R.drawable.rain);
                                break;
                            case "thunderstorm":
                                image.setImageResource(R.drawable.thunder);
                                break;
                            case "snow":
                                image.setImageResource(R.drawable.snow);
                                break;
                            case "heavy snow":
                                image.setImageResource(R.drawable.snow);
                                break;
                            case "mist":
                                image.setImageResource(R.drawable.mist);
                                break;
                            default:
                                image.setImageResource(R.drawable.clouds);

                        }

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                    //changing weather events based on seekbar
                    seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                            //time conversion
                            LocalTime timeOfDay = null;
                            String formattedTime = null;
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                try {
                                    timeOfDay = Instant.ofEpochSecond(weatherData.getJSONArray("list").getJSONObject(progress).getInt("dt"))
                                            .atOffset(ZoneOffset.ofTotalSeconds(weatherData.getJSONObject("city").getInt("timezone")))
                                            .toLocalTime();
                                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a");
                                    formattedTime = timeOfDay.format(formatter);
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }

                            //updating screen
                            try {
                                date.setText(formattedTime+"");
                                location.setText(weatherData.getJSONObject("city").getString("name")+", US");

                                double temperatureK = weatherData.getJSONArray("list").getJSONObject(progress).getJSONObject("main").getDouble("temp");
                                temp.setText((Math.round((temperatureK-273)*9/5.0+32))+"°F");

                                double feelsLikeK = weatherData.getJSONArray("list").getJSONObject(progress).getJSONObject("main").getInt("feels_like");
                                feelsLike.setText("feels like "+(Math.round((feelsLikeK-273)*9/5.0+32))+"°F");

                                String weatherType = weatherData.getJSONArray("list").getJSONObject(progress).getJSONArray("weather").getJSONObject(0).getString("description");
                                description.setText(weatherType);

                                double windMS = weatherData.getJSONArray("list").getJSONObject(progress).getJSONObject("wind").getDouble("speed");
                                BigDecimal round = new BigDecimal(Double.toString(windMS*2.237));
                                round = round.setScale(1, RoundingMode.HALF_UP);
                                windSpeed.setText("wind speed is "+(round.doubleValue())+" mph");

                                //image
                                switch(weatherType)
                                {
                                    case "clear sky":
                                        image.setImageResource(R.drawable.sun);
                                        break;
                                    case "rain":
                                        image.setImageResource(R.drawable.rain);
                                        break;
                                    case "shower rain":
                                        image.setImageResource(R.drawable.rain);
                                        break;
                                    case "thunderstorm":
                                        image.setImageResource(R.drawable.thunder);
                                        break;
                                    case "snow":
                                        image.setImageResource(R.drawable.snow);
                                        break;
                                    case "mist":
                                        image.setImageResource(R.drawable.mist);
                                        break;
                                    case "heavy snow":
                                        image.setImageResource(R.drawable.snow);
                                        break;
                                    case "light rain":
                                        image.setImageResource(R.drawable.rain);
                                        break;
                                    default:
                                        image.setImageResource(R.drawable.clouds);

                                }



                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {

                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {

                        }
                    });

                }
            });

            return null;
        }

    }

}