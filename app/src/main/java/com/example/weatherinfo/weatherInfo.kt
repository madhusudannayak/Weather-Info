package com.example.weatherinfo

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import java.text.SimpleDateFormat
import java.util.*

class weatherInfo : AppCompatActivity() {
    lateinit var temp: TextView
    lateinit var address: TextView
    lateinit var status: TextView
    lateinit var temp_min: TextView
    lateinit var temp_max: TextView
    lateinit var updatedat: TextView
    lateinit var Sunrise: TextView
    lateinit var Sunset: TextView
    lateinit var windSpeed: TextView
    lateinit var bg: RelativeLayout
    lateinit var mainContainer: RelativeLayout
    lateinit var ChangeLocation: ImageView
    lateinit var progressBar: ProgressBar
    lateinit var sharedPreference: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_info)
        temp = findViewById(R.id.temp);
        address = findViewById(R.id.address)
        status = findViewById(R.id.status)
        temp_min = findViewById(R.id.temp_min)
        temp_max = findViewById(R.id.temp_max)
        updatedat = findViewById(R.id.updated_at)
        Sunrise = findViewById(R.id.sunrise)
        Sunset = findViewById(R.id.sunset)
        windSpeed = findViewById(R.id.wind)
        bg = findViewById(R.id.weather)
        progressBar = findViewById(R.id.loader)
        ChangeLocation = findViewById(R.id.changeLocation);
        mainContainer = findViewById(R.id.mainContainer);
        var getCity = intent.getStringExtra("city")
        sharedPreference =  getSharedPreferences("location", Context.MODE_PRIVATE)
        val location = sharedPreference.getString("city","")

        ShowInfo(location.toString())

        progressBar.setVisibility(View.VISIBLE);
        mainContainer.setVisibility(View.INVISIBLE);


        ChangeLocation.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }
    fun ShowInfo(city:String){
        val queue = Volley.newRequestQueue(this)
        val url = "https://api.openweathermap.org/data/2.5/weather?q=$city&units=metric&appid=8118ed6ee68db2debfaaa5a44c832918"
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener
            { response ->
                progressBar.setVisibility(View.GONE);
                mainContainer.setVisibility(View.VISIBLE);

//                val cityNotFound =response.getString("message")
//                Log.d("kkkkkkk",cityNotFound)
//                if(cityNotFound.isEmpty()){
//                    Toast.makeText(this,"City Not Found",Toast.LENGTH_SHORT).show()
//                }



                val weatherArray = response.getJSONArray("weather")
                val weatherObject = weatherArray.getJSONObject(0)
                val weather = weatherObject.getString("main")

                when {
                    weather.equals("clouds", ignoreCase = true) ->
                    {
                        bg.setBackgroundResource(R.drawable.clouds)
                        changeBgtoWhite()
                    }
                    weather.equals("haze", ignoreCase = true) ->
                    {
                        bg.setBackgroundResource(R.drawable.haze)
                        changeBgtoWhite()
                    }
                    weather.equals("drizzle", ignoreCase = true) ->
                    {
                        bg.setBackgroundResource(R.drawable.drizzle)
                        changeBgtoWhite()
                    }
                    weather.equals("rain", ignoreCase = true) ->
                    {
                        bg.setBackgroundResource(R.drawable.drizzle)
                        changeBgtoWhite()
                    }

                    else->{

                    }

                }

                status.setText(weather)
                val temp = response.getJSONObject("main")
                val tempInCel = temp.getInt("temp")
                val tempInFa = tempInCel*9/5+32;
                this.temp.setText(tempInCel.toString()+"°C"+"/"+tempInFa.toString()+"°F")
                val maxTemp = temp.getString("temp_max")+"°C"
                temp_max.setText("Max Temp: " +maxTemp)
                val minTemp = temp.getString("temp_min")+"°C"
                temp_min.setText("Min Temp: " +minTemp)
                val sys = response.getJSONObject("sys")
                val country = sys.getString("country")
                val city = response.getString("name")
                val location = country+", "+city
                address.setText(location)
                val sunrise = sys.getLong("sunrise")
                Sunrise.setText(SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunrise*1000)))
                val sunset = sys.getLong("sunset")
                Sunset.setText(SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunset*1000)))
                val wind = response.getJSONObject("wind")
                val speed = wind.getString("speed")
                windSpeed.setText(speed)
                val updatedAt:Long = response.getLong("dt")
                updatedat.setText("Updated at: "+ SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(
                    Date(updatedAt*1000)
                ))



                Log.d("dataaaaa", updatedAt.toString())
            },
            {
                Log.d("dataError", it.toString())
                val intent = Intent(this, MainActivity::class.java)
                var editor = sharedPreference.edit()
                editor.putString("city","")
                editor.commit()
                Toast.makeText(this,"City Not Found",Toast.LENGTH_SHORT).show()
                startActivity(intent)
            })

        queue.add(jsonObjectRequest)
    }
    fun changeBgtoWhite(){
        temp.setTextColor(Color.parseColor("#FFFFFF"))
        address.setTextColor(Color.parseColor("#FFFFFF"))
        status.setTextColor(Color.parseColor("#FFFFFF"))
        temp_min.setTextColor(Color.parseColor("#FFFFFF"))
        temp_max.setTextColor(Color.parseColor("#FFFFFF"))
        updatedat.setTextColor(Color.parseColor("#FFFFFF"))
        Sunrise.setTextColor(Color.parseColor("#FFFFFF"))
        Sunset.setTextColor(Color.parseColor("#FFFFFF"))
        windSpeed.setTextColor(Color.parseColor("#FFFFFF"))

    }
}