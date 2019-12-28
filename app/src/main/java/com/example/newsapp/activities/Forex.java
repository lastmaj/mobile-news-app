package com.example.newsapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

        import android.os.AsyncTask;
        import android.view.View;
import android.widget.TextView;

import com.androdocs.httprequest.HttpRequest;
import com.example.newsapp.R;

import org.json.JSONException;
        import org.json.JSONObject;

        import java.text.SimpleDateFormat;
        import java.util.Date;
        import java.util.Locale;


public class Forex extends AppCompatActivity {
    TextView curr1,curr2,curr3,curr4,curr5,curr6,updatedOn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forex);
        curr1 = findViewById(R.id.usd);
        curr2=findViewById(R.id.gbp);
        curr3=findViewById(R.id.yuan);
        curr4=findViewById(R.id.euro);
        curr5=findViewById(R.id.jpy);
        curr6=findViewById(R.id.cny);
        updatedOn=findViewById(R.id.updated_at);
        /*updated_atTxt = findViewById(R.id.updated_at);
        statusTxt = findViewById(R.id.status);
        tempTxt = findViewById(R.id.temp);
        temp_minTxt = findViewById(R.id.temp_min);
        temp_maxTxt = findViewById(R.id.temp_max);
        sunriseTxt = findViewById(R.id.sunrise);
        sunsetTxt = findViewById(R.id.sunset);
        windTxt = findViewById(R.id.wind);
        pressureTxt = findViewById(R.id.pressure);
        humidityTxt = findViewById(R.id.humidity);
        button=findViewById(R.id.button);*/
        new ForexTask().execute();
    }
    class ForexTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            /* Showing the ProgressBar, Making the main design GONE */
            findViewById(R.id.loader).setVisibility(View.VISIBLE);
            findViewById(R.id.mainContainer).setVisibility(View.GONE);
            findViewById(R.id.errorText).setVisibility(View.GONE);
        }

        protected String doInBackground(String... args) {
            String response = HttpRequest.excuteGet("https://api.exchangeratesapi.io/latest?base=" + "TRY");
            return response;
        }

        @Override
        protected void onPostExecute(String result) {


            try {
                JSONObject jsonObj = new JSONObject(result);
                JSONObject rates = jsonObj.getJSONObject("rates");

                //JSONObject sys = jsonObj.getJSONObject("sys");
                // JSONObject wind = jsonObj.getJSONObject("wind");
                //JSONObject weather = jsonObj.getJSONArray("weather").getJSONObject(0);

                //Long updatedAt = jsonObj.getLong("date");
                //String updatedAtText = "Updated at: " + updatedAt;
                String CAD = rates.getString("CAD") ;
                String eur = rates.getString("EUR") ;
                String jpy = rates.getString("JPY") ;
                String usd = rates.getString("USD") ;
                String cny = rates.getString("CNY") ;
                String trys = rates.getString("TRY");
                //String tempMin = "Min Temp: " + main.getString("temp_min") + "°C";
                //String tempMax = "Max Temp: " + main.getString("temp_max") + "°C";
                //String pressure = main.getString("pressure");
                //String humidity = main.getString("humidity");

                //Long sunrise = sys.getLong("sunrise");
                //Long sunset = sys.getLong("sunset");
                //String windSpeed = wind.getString("speed");
                //String weatherDescription = weather.getString("description");

                //String address = jsonObj.getString("name") + ", " + sys.getString("country");


                /* Populating extracted data into our views */
                curr1.setText(CAD);
                curr2.setText(eur);
                curr3.setText(jpy);
                curr4.setText(usd);
                curr5.setText(cny);
                curr6.setText(trys);
                //updatedOn.setText(updatedAtText);
                //statusTxt.setText(weatherDescription.toUpperCase());
                //tempTxt.setText(temp);
                //temp_minTxt.setText(tempMin);
                //temp_maxTxt.setText(tempMax);
                //sunriseTxt.setText(new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(sunrise * 1000)));
                //sunsetTxt.setText(new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(sunset * 1000)));
                //windTxt.setText(windSpeed);
                //pressureTxt.setText(pressure);
                //humidityTxt.setText(humidity);

                /* Views populated, Hiding the loader, Showing the main design */
                findViewById(R.id.loader).setVisibility(View.GONE);
                findViewById(R.id.mainContainer).setVisibility(View.VISIBLE);


            } catch (JSONException e) {
                findViewById(R.id.loader).setVisibility(View.GONE);
                findViewById(R.id.errorText).setVisibility(View.VISIBLE);
            }

        }
    }
}

