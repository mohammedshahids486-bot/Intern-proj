package com.paryavarankavalu.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Looper
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.Locale
import kotlin.coroutines.resume

data class LocationData(
    val latitude: Double,
    val longitude: Double,
    val address: String = ""
)

class LocationHelper(private val context: Context) {

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
        ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(): LocationData? {
        if (!hasLocationPermission()) return null

        return suspendCancellableCoroutine { continuation ->
            val locationRequest = LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY, 5000L
            ).setWaitForAccurateLocation(false)
             .setMinUpdateIntervalMillis(2000L)
             .setMaxUpdates(1)
             .build()

            val callback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    fusedLocationClient.removeLocationUpdates(this)
                    val location = result.lastLocation
                    if (location != null) {
                        val address = getAddressFromLocation(location.latitude, location.longitude)
                        continuation.resume(
                            LocationData(location.latitude, location.longitude, address)
                        )
                    } else {
                        continuation.resume(null)
                    }
                }
            }

            fusedLocationClient.requestLocationUpdates(
                locationRequest, callback, Looper.getMainLooper()
            )

            continuation.invokeOnCancellation {
                fusedLocationClient.removeLocationUpdates(callback)
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun getLastKnownLocation(callback: (LocationData?) -> Unit) {
        if (!hasLocationPermission()) {
            callback(null)
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val address = getAddressFromLocation(location.latitude, location.longitude)
                callback(LocationData(location.latitude, location.longitude, address))
            } else {
                callback(null)
            }
        }.addOnFailureListener {
            callback(null)
        }
    }

    private fun getAddressFromLocation(lat: Double, lng: Double): String {
        return try {
            val geocoder = Geocoder(context, Locale.getDefault())
            @Suppress("DEPRECATION")
            val addresses = geocoder.getFromLocation(lat, lng, 1)
            if (!addresses.isNullOrEmpty()) {
                val addr = addresses[0]
                buildString {
                    addr.subLocality?.let { append("$it, ") }
                    addr.locality?.let { append("$it, ") }
                    addr.adminArea?.let { append(it) }
                }.trimEnd(',', ' ')
            } else {
                "Lat: ${String.format("%.4f", lat)}, Lng: ${String.format("%.4f", lng)}"
            }
        } catch (e: Exception) {
            "Lat: ${String.format("%.4f", lat)}, Lng: ${String.format("%.4f", lng)}"
        }
    }
}
