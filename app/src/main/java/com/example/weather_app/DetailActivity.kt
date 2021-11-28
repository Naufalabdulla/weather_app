package com.example.weather_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.weather_app.network.Config
import com.example.weather_app.network.ModelDetail
import com.example.weather_app.network.WeatherModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val location = intent.getStringExtra("QUERY")
        val backBtn = findViewById<ImageButton>(R.id.detail_back_btn)
        val textLoc : TextView = findViewById(R.id.loc_detail)
        val textStat : TextView = findViewById(R.id.status_detail)
        val textTemp : TextView = findViewById(R.id.temp_detail)
        val date : TextView = findViewById(R.id.date_detail)
        val textHumid : TextView = findViewById(R.id.humid_detail)
        val imgStat : ImageView = findViewById(R.id.statImg_detail)
        val sdf = SimpleDateFormat("dd/M/yyyy")
        val currentDate = sdf.format(Date())

        date.text = currentDate.toString()

        backBtn.setOnClickListener {
            onBackPressed()
        }


        Config().getServiceDetail().getModelDetail(location , "85ae236f03289edfdd907eb164c72d2e").enqueue(object : Callback<WeatherModel>{
            override fun onResponse(call: Call<WeatherModel>, response: Response<WeatherModel>) {
                val call1 = response.body()
                val suhu = call1?.main?.temp?.div(10)

                textLoc.text = call1?.name
                textStat.text = call1?.weather?.get(0)?.main
                textTemp.text = suhu.toString().substringBefore(".") + "°C"
                textHumid.text = call1?.main?.humidity.toString() + "%"

                Glide
                    .with(this@DetailActivity)
                    .load("https://openweathermap.org/img/wn/${call1?.weather?.get(0)?.icon}@4x.png")
                    .into(imgStat)
            }

            override fun onFailure(call: Call<WeatherModel>, t: Throwable) {
                Toast.makeText(this@DetailActivity, "$t", Toast.LENGTH_LONG ).show()
            }

        })
    }
}