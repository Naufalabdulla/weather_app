package com.example.weather_app

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weather_app.network.Config
import com.example.weather_app.network.EarthquakeModel
import com.example.weather_app.network.WeatherModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {
    val dataTanggal = arrayListOf<String>()
    val dataJam = arrayListOf<String>()
    val dataMagnitude = arrayListOf<String>()
    val dataLokasi = arrayListOf<String>()
    val dataDirasakan = arrayListOf<String>()

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navController = findNavController(R.id.fragment)

        bottomNavigationView.setupWithNavController(navController)

        profileOnClickListener()
        fragmentStart()

    }

    override fun onBackPressed() {
        val a = Intent(Intent.ACTION_MAIN)
        a.addCategory(Intent.CATEGORY_HOME)
        a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(a)
    }

    fun profileOnClickListener(){
        val floatingActionButton = findViewById<FloatingActionButton>(R.id.floatingActionButton)

        floatingActionButton.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }

    fun fragmentGempaStart(){
        recyclerView = findViewById(R.id.rv_earthquake)

        Config().getGempaService().getModelEarthquake().enqueue(object : Callback<EarthquakeModel>{
            override fun onResponse(call: Call<EarthquakeModel>, response: Response<EarthquakeModel>) {
                val call1 = response.body()?.infogempa?.gempa

                for(list in call1!!.indices){
                    dataTanggal.add(call1[list]?.tanggal.toString())
                    dataJam.add(call1[list]?.jam.toString())
                    dataMagnitude.add(call1[list]?.magnitude.toString())
                    dataLokasi.add(call1[list]?.wilayah.toString())
                    dataDirasakan.add(call1[list]?.dirasakan.toString())

                    recyclerView.adapter = EarthquakeAdapter(dataTanggal,dataJam,dataMagnitude,dataLokasi,dataDirasakan)
                    recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
                    recyclerView.setHasFixedSize(true)
                }
            }

            override fun onFailure(call: Call<EarthquakeModel>, t: Throwable) {
                Toast.makeText(this@MainActivity, "$t", Toast.LENGTH_LONG ).show()
            }

        })
    }

    fun fragmentStart(){
        val textLoc : TextView = findViewById(R.id.loc)
        val textStat : TextView = findViewById(R.id.status)
        val textTemp : TextView = findViewById(R.id.temp)
        val date : TextView = findViewById(R.id.date)
        val textHumid : TextView = findViewById(R.id.humid)
        val imgStat : ImageView = findViewById(R.id.statImg)

        val sdf = SimpleDateFormat("dd/M/yyyy")
        val currentDate = sdf.format(Date())

        date.text = currentDate.toString()

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
                    textHumid.text = call1?.main?.humidity.toString() + "%"

                    Glide
                        .with(this@MainActivity)
                        .load("https://openweathermap.org/img/wn/${call1?.weather?.get(0)?.icon}@4x.png")
                        .into(imgStat)

                }

                override fun onFailure(call: Call<WeatherModel>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "$t", Toast.LENGTH_LONG ).show()
                }

            })
        }
    }
}