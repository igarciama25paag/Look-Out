package com.igmata.lookout.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import android.widget.Toast
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

class Gps {
    companion object {

        // Azkenengo kokapena lortu
        fun getLocation(context: Context, onResult: (Double, Double) -> Unit) {

            // Kokapena lortzeko bezeroa
            val locationClient = LocationServices.getFusedLocationProviderClient(context)

            // Baimenak dituela ziurtatu
            if (
                ContextCompat.checkSelfPermission(context,Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
            ) {
                // Azken kokapena lortu
                locationClient.lastLocation.addOnSuccessListener { location ->
                    location?.let {
                        // Latitudea eta longitudea itzuli
                        onResult(it.latitude, it.longitude)
                    } ?: run {
                        // Cache-an azkenengo kokapenrik aurkitzen ez bada, kokapen berri bat lortu
                        requestNewLocation(locationClient, onResult)
                    }
                }
            } else Toast.makeText(context, "Kokapen baimenak behar dituzu", Toast.LENGTH_SHORT).show()
        }

        // Kokapen berria lortu
        @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
        private fun requestNewLocation(
            locationClient: FusedLocationProviderClient,
            onResult: (Double, Double) -> Unit
        ) {
            // Kokapena nola eskatzeko konfigurazioa
            val locationRequest = LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY,
                10000
            ).apply {
                setMinUpdateIntervalMillis(5000)
                setWaitForAccurateLocation(true)
            }.build()

            // Kokapenak eskatzen hasi bat lortu arte
            locationClient.requestLocationUpdates(
                locationRequest,
                object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult) {
                        locationResult.lastLocation?.let {
                            onResult(it.latitude, it.longitude)
                            locationClient.removeLocationUpdates(this)
                        }
                    }
                },
                Looper.getMainLooper()
            )
        }
    }
}