package com.example.walkingdog_kotlin

import com.mapbox.geojson.Point
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.widget.Button
import com.mapbox.android.core.location.LocationEngine
import com.mapbox.android.core.location.LocationEngineListener
import com.mapbox.android.core.location.LocationEnginePriority
import com.mapbox.android.core.location.LocationEngineProvider
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.annotations.Marker
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.CameraMode
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.RenderMode
import kotlinx.android.synthetic.main.activity_walking.*
import java.util.*
import kotlin.concurrent.timer

class WalkingActivity() : AppCompatActivity(), PermissionsListener, LocationEngineListener, MapboxMap.OnMapClickListener,
    Parcelable {

    private var time = 0
    private var timerTask: Timer? = null      // null을 허용
    private var isRunning = false
    private var lap = 1

    private lateinit var mapView: MapView
    private lateinit var map: MapboxMap
    private lateinit var startButton: Button
    private lateinit var permissionManager: PermissionsManager
    private lateinit var originLocation: Location
    private lateinit var originPosition: Point
    private lateinit var destinationPosition: Point

    private var locationEngine : LocationEngine? = null
    private var locationLayerPlugin : LocationLayerPlugin? = null
    private var destinationMarker: Marker? = null

    constructor(parcel: Parcel) : this() {
        time = parcel.readInt()
        isRunning = parcel.readByte() != 0.toByte()
        lap = parcel.readInt()
        originLocation = parcel.readParcelable(Location::class.java.classLoader)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_walking)

        playFab.setOnClickListener {
            isRunning = !isRunning

            if (isRunning) {
                start()
            } else {
                pause()
            }
        }

        resetFab.setOnClickListener {
            reset()
        }

        Mapbox.getInstance(applicationContext, getString(R.string.access_token))
        mapView = findViewById(R.id.mapView)
        startButton = findViewById(R.id.startButton)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync { mapboxMap ->
            map = mapboxMap
            enableLocation()
        }

        startButton.setOnClickListener {
            //launch our navigation UI

        }
    }   //onCreate

    private fun enableLocation() {
        if(PermissionsManager.areLocationPermissionsGranted(this)) {
            initializeLocationEngine()
            initializeLocationLayer()
        } else {
            permissionManager = PermissionsManager(this)
            permissionManager.requestLocationPermissions(this)
        }
    }

    @SuppressWarnings("MissingPermission")
    private fun initializeLocationEngine() {
        locationEngine = LocationEngineProvider(this).obtainBestLocationEngineAvailable()
        locationEngine?.priority = LocationEnginePriority.HIGH_ACCURACY
        locationEngine?.activate()

        val lastLocation = locationEngine?.lastLocation
        if(lastLocation != null) {
            originLocation = lastLocation
            setCameraPosition(lastLocation)
        } else {
            locationEngine?.addLocationEngineListener(this)
        }
    }

    @SuppressWarnings("MissingPermission")
    private fun initializeLocationLayer() {
        locationLayerPlugin = LocationLayerPlugin(mapView, map, locationEngine)
        locationLayerPlugin?.setLocationLayerEnabled(true)
        locationLayerPlugin?.cameraMode = CameraMode.TRACKING
        locationLayerPlugin?.renderMode = RenderMode.NORMAL
    }

    private fun setCameraPosition(location: Location) {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(
            LatLng(location.latitude, location.longitude), 13.0))
    }

    override fun onExplanationNeeded(permissionsToExplain: MutableList<String>?) {
        //Present a toast or a dialog expalning why they ned to grant access.
    }

    override fun onPermissionResult(granted: Boolean) {
        if(granted) {
            enableLocation()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        permissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onLocationChanged(location: Location?) {
        location?.let{
            originLocation = location
            setCameraPosition(location)
        }
    }
    @SuppressWarnings("MissingPermission")
    override fun onConnected() {
        locationEngine?.requestLocationUpdates()
    }

    @SuppressWarnings("MissingPermission")
    override fun onStart() {
        super.onStart()
        if(PermissionsManager.areLocationPermissionsGranted(this)) {
            locationEngine?.requestLocationUpdates()
            locationLayerPlugin?.onStart()
        }
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onStart()
    }

    override fun onPause() {
        super.onPause()
        mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        locationEngine?.removeLocationUpdates()
        locationLayerPlugin?.onStop()
        mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
        locationEngine?.deactivate()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if(outState != null) {
            mapView.onSaveInstanceState(outState)
        }
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }


    private fun start() {
        playFab.setBackgroundResource(R.drawable.pause_icon)   // 일시정지 이미지

        timerTask = timer(period=10) {       // 타이머 인터벌 10 ms
            time++

//            val min = time / 6000
            val sec = time / 100
            val milli = time % 100

            runOnUiThread {                   // UI 조작이 가능한 블럭
//                secTextView.text = "$sec"
//                milliTextView.text = "$milli"

                secTextView.text = "$sec"
                milliTextView.text = "$milli"

            }
        }
    }

    private fun pause() {
        playFab.setBackgroundResource(R.drawable.play_icon)   // 시작 이미지

        timerTask?.cancel()                  // 실행중인 타이머 취소
    }

    private fun reset() {
        timerTask?.cancel()       // 실행중인 타이머 취소

        // 모든 변수 초기화
        time = 0
        isRunning = false
        resetFab.setBackgroundResource(R.drawable.dog_house)
        secTextView.text = "0"
        milliTextView.text = "00"

        // 모든 랩타임 기록 삭제
        //lapLayout.removeAllViews()
        lap = 1
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(time)
        parcel.writeByte(if (isRunning) 1 else 0)
        parcel.writeInt(lap)
        parcel.writeParcelable(originLocation, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<WalkingActivity> {
        override fun createFromParcel(parcel: Parcel): WalkingActivity {
            return WalkingActivity(parcel)
        }

        override fun newArray(size: Int): Array<WalkingActivity?> {
            return arrayOfNulls(size)
        }
    }

    override fun onMapClick(point: LatLng) {
        destinationMarker = map.addMarker(MarkerOptions().position(point))
        destinationPosition = Point.fromLngLat(point.longitude, point.latitude)
        originPosition = Point.fromLngLat(originLocation.longitude, originLocation.latitude)

        startButton.isEnabled = true
        startButton.setBackgroundResource(R.color.mapboxBlue)
    }


}
