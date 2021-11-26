package com.example.weather_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EarthquakeAdapter(private val dataTanggal:ArrayList<String>,
                        private val dataJam: ArrayList<String>,
                        private val dataMagnitude: ArrayList<String>,
                        private val dataLokasi: ArrayList<String>,
                        private val dataDirasakan: ArrayList<String>) : RecyclerView.Adapter<EarthquakeAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tanggal : TextView = view.findViewById(R.id.tanggal_gempa)
        val jam : TextView = view.findViewById(R.id.jam_gempa)
        val lokasi : TextView = view.findViewById(R.id.lokasi_gempa)
        val dirasakan : TextView = view.findViewById(R.id.terdampak_gempa)
        val magnitude : TextView = view.findViewById(R.id.magnitude_gempa)

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): EarthquakeAdapter.ViewHolder {
        val  view = LayoutInflater.from(viewGroup.context).inflate(R.layout.earthquake_rv_model, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: EarthquakeAdapter.ViewHolder, position: Int) {
        viewHolder.tanggal.text = dataTanggal[position]
        viewHolder.jam.text = dataJam[position]
        viewHolder.lokasi.text = dataLokasi[position]
        viewHolder.dirasakan.text = dataDirasakan[position]
        viewHolder.magnitude.text = dataMagnitude[position]
    }

    override fun getItemCount() = dataMagnitude.size


}