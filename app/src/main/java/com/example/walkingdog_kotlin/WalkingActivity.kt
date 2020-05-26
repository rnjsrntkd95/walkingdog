package com.example.walkingdog_kotlin

import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView

class WalkingActivity : AppCompatActivity() {
    val RequestPermissionCode = 1
    var mLocation: Location? = null
    var lat: Double = 0.0
    var lon: Double = 0.0
    private lateinit var fusedLocationClient: FusedLocationProviderClient
//    private lateinit var mapView: MapView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_walking)

        // 현재 위치
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
//        getLastLocation()

        initView()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    // 카카오맵 연결
    fun initView() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermission()
        } else {
            Log.d("NONO", "YES")
        }
        var mapView = MapView(this)
//        mapView = MapView(this)
        val mapViewContainer: ViewGroup = findViewById<View>(R.id.map_view) as ViewGroup
//        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(37.2984885, 127.029934), 1, true);
        mapViewContainer.addView(mapView)
        mapView.currentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading)

    }

    // 현재 위치의 위도, 경도를 가져오는 함수
    fun getLastLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermission()
        } else {
            val pref = getSharedPreferences("pref", Context.MODE_PRIVATE)

            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    mLocation = location
                    if (location != null) {
                        Log.d("TAG", "${location.latitude}, ${location.longitude}")
                        lat = location.latitude
                        lon = location.longitude

                        // Map에 현재 위도, 경도의 지도를 출력
//                        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(lat, lon), 1, true);
                        Log.d("Location222", lat.toString() + lon.toString())
                        val edit = pref.edit()
                        edit.putString("latitude", location.latitude.toString())
                        edit.putString("longitude", location.longitude.toString())
                        edit.commit()


                        val geocoder: Geocoder = Geocoder(this)
                        var address: List<Address>? = null

                        address = geocoder.getFromLocation(location.latitude, location.longitude, 10)

                        if (address != null) {
                            if (address.size == 0) {
                                Log.d("TAG", "해당 주소 없음")
                                // Default 처리 필요

                            } else {
                                Log.d("TAG", address.toString())
                                for (addressIndex in address) {
                                    if (addressIndex.adminArea != null &&
                                        addressIndex.locality != null &&
                                        addressIndex.thoroughfare != null){
                                        edit.putString("addressAdmin", address[0].adminArea.toString())
                                        edit.putString("addressLocality", address[0].locality.toString())
                                        edit.putString("addressThoroughfare", address[0].thoroughfare.toString())
                                        edit.commit()
                                        break
                                    }
                                }
                            }
                        }
                    } else {
                        Log.d("TAG", "Location get failed!!")
                        val latitude = pref.getString("latitude", "0").toDouble()
                        val longitude = pref.getString("longitude", "0").toDouble()
                    }
                }
                .addOnFailureListener {
                    Log.d("TAG", "Location error is ${it.message}")
                    it.printStackTrace()
                    val latitude = pref.getString("latitude", "0").toDouble()
                    val longitude = pref.getString("longitude", "0").toDouble()
                }
        }
    }
    // 위치 권한 설정
    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
            RequestPermissionCode
        )
        this.recreate()
    }
}

private operator fun MapView.CurrentLocationTrackingMode.invoke(trackingModeOnWithoutHeading: MapView.CurrentLocationTrackingMode) {

}







