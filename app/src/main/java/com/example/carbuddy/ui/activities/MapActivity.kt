package com.example.carbuddy.ui.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.location.Location
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.carbuddy.R
import com.example.carbuddy.databinding.ActivityMapBinding
import com.example.carbuddy.utils.LatLngUtil
import com.example.carbuddy.utils.onClick
import com.example.carbuddy.utils.toast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task

class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityMapBinding
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Task<Location>
    private lateinit var latLng: LatLng
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        inIt()
    }

    private fun inIt() {
        inItFusedLocation()
        obtainMapFragment()
        setOnClickListener()
    }

    private fun setOnClickListener() {
        binding.btnAddLocation.setOnClickListener {
            returnTheLatLng()
        }
    }

    private fun obtainMapFragment() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    @SuppressLint("MissingPermission")
    private fun inItFusedLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        lastLocation = fusedLocationClient.lastLocation
    }

    private fun returnTheLatLng() {
        if (::latLng.isInitialized) {
            val location = LatLngUtil.latLngToString(latLng)
            val resultIntent = Intent()
            resultIntent.putExtra("LOCATION", location)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        } else {
            toast("Select a location first")
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnMapClickListener {
            addMarker(it)
        }

        lastLocation.addOnSuccessListener { location ->
            location?.let {
                val currentLatLng = LatLng(it.latitude, it.longitude)
                binding.btnTakeToCurrentLocation.onClick {
                    mMap.clear()
                    mMap.addMarker(
                        MarkerOptions().position(currentLatLng).title("Current Location")
                            .draggable(false)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_location_marker))
                    )
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
                }
                mMap.addMarker(
                    MarkerOptions().position(currentLatLng).title("Location").draggable(false)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_location_marker))
                )
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
                this.latLng = currentLatLng
            }
        }
    }

    private fun addMarker(latLng: LatLng) {
        mMap.apply {
            clear()
            addMarker(
                MarkerOptions().position(latLng).title("Location").draggable(false)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_location_marker))
            )
            moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
        }
        this.latLng = latLng
    }

}