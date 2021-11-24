package com.example.weather_app

import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.weather_app.network.Config
import com.example.weather_app.network.WeatherModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navController = findNavController(R.id.fragment)

        bottomNavigationView.setupWithNavController(navController)

        fragmentStart()

    }

    fun fragmentStart(){
        val textLoc : TextView = findViewById(R.id.loc)
        val textStat : TextView = findViewById(R.id.status)
        val textTemp : TextView = findViewById(R.id.temp)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1001)
            Toast.makeText(this, "Restart App After Changing Permission", Toast.LENGTH_LONG).show()
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
//            textLoc.text = "${location?.latitude.toString()}, ${location?.longitude.toString()}"
            Config().getService().getModelWeather(location?.latitude.toString(), location?.longitude.toString(), "85ae236f03289edfdd907eb164c72d2e").enqueue(object : Callback<WeatherModel>{
                override fun onResponse(call: Call<WeatherModel>, response: Response<WeatherModel>) {
                    val call1 = response.body()
                    val suhu = call1?.main?.temp?.div(10)

                    textLoc.text = call1?.name
                    textStat.text = call1?.weather?.get(0)?.main
                    textTemp.text = suhu.toString().substringBefore(".") + "°C"
                }

                override fun onFailure(call: Call<WeatherModel>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "$t", Toast.LENGTH_LONG ).show()
                }

            })
        }
    }
}