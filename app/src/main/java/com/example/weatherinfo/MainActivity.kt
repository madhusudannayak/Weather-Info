package com.example.weatherinfo

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button = findViewById<Button>(R.id.find)
        val city = findViewById<TextInputEditText>(R.id.city)

        val sharedPreference =  getSharedPreferences("location", Context.MODE_PRIVATE)

        button.setOnClickListener{
            if (city.equals(""))
            {
                Toast.makeText(this,"Please Enter City Name", Toast.LENGTH_SHORT).show()
            }else
            {
                val intent = Intent(this, weatherInfo::class.java)
                intent.putExtra("city",city.text.toString())
                var editor = sharedPreference.edit()
                editor.putString("city",city.text.toString())
                editor.commit()
                startActivity(intent)
            }

        }



        // val location = sharedPreference.getString("username","defaultName")

        // Toast.makeText(this,location,Toast.LENGTH_SHORT).show()


    }
}