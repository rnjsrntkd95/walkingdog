package com.example.walkingdog_kotlin

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
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
import kotlinx.android.synthetic.main.activity_walking.*
import net.daum.mf.map.api.*
import java.util.*
import kotlin.concurrent.timer
import kotlin.math.*

class WalkingActivity : AppCompatActivity(), MapView.CurrentLocationEventListener,
    MapView.MapViewEventListener {
    private val RequestPermissionCode = 1
    private var mapView: MapView? = null
    private var polyline: MapPolyline? = null
    private var mapPoint: MapPoint? = null
    private var prevLat: Double? = null
    private var prevLon: Double? = null
    private var walkingDistance: Double = 0.0
    private var isStart: Boolean = false
    private var isPause: Boolean = false
    private var walkingTimer: Timer? = null
    private var tapTimer: Timer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_walking)

        // 현재 위치
        initView()

        playFab.setOnClickListener {
            toiletActivity()
        }
        resetFab.setOnClickListener {
            pauseWalking()
            finishWalking()
        }
        camera_btn.setOnClickListener {
            restartWalking()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView!!.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOff
    }

    // 카카오맵 연결
    private fun initView() {
        // 위치 권한 설정 확인
        isSetLocationPermission()

        mapView = MapView(this)
        val mapViewContainer: ViewGroup = findViewById<View>(R.id.map_view) as ViewGroup
        mapViewContainer.addView(mapView)

        mapView!!.setMapViewEventListener(this)
        // 현위치 트래킹 모드 ON
        mapView!!.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading
        mapView!!.setZoomLevel(0, true)
        mapView!!.setCustomCurrentLocationMarkerTrackingImage(R.drawable.labrador_icon, MapPOIItem.ImageOffset(50,50))
        mapView!!.setCustomCurrentLocationMarkerImage(R.drawable.labrador_icon, MapPOIItem.ImageOffset(50,50))
        mapView!!.setCurrentLocationEventListener(this)
        polyline = MapPolyline()
        polyline!!.tag = 1000
        polyline!!.lineColor = Color.argb(200, 103, 114, 241)
    }

    // 일시 정지
    private fun pauseWalking() {
        mapView!!.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOff
        isPause = true
    }
    // 재시작
    private fun restartWalking() {
        mapView!!.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading
    }

    // 산책 종료
    private fun finishWalking() {
        mapView!!.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOff
        val mapPointBounds = MapPointBounds(polyline!!.mapPoints)
        mapView!!.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds, 100))

        // 시작 지점 표시
        val startMarker = MapPOIItem()
        startMarker.itemName = ""
        startMarker.mapPoint = polyline!!.getPoint(0)
        startMarker.markerType = MapPOIItem.MarkerType.CustomImage
        startMarker.customImageResourceId = R.drawable.start_walking_icon
        startMarker.setCustomImageAnchor(0.5f, 0.5f)
        startMarker.isCustomImageAutoscale = false
        startMarker.isShowCalloutBalloonOnTouch = false
        mapView!!.addPOIItem(startMarker)


        // 경로 사진 캡처 추가

    }

    // 배변활동 표시
    private fun toiletActivity() {
        val marker: MapPOIItem = MapPOIItem()
        marker.itemName = ""
        marker.isShowCalloutBalloonOnTouch = false
        marker.mapPoint = mapPoint
        marker.markerType = MapPOIItem.MarkerType.CustomImage
        marker.customImageResourceId = R.drawable.toilet_activity
        marker.isCustomImageAutoscale = false
        marker.setCustomImageAnchor(0.5f, 1.0f)
        mapView!!.addPOIItem(marker)
    }

    // 위치 권한 설정 확인 함수
    private fun isSetLocationPermission() {
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

    override fun onCurrentLocationDeviceHeadingUpdate(p0: MapView?, p1: Float) {
    }

    override fun onCurrentLocationUpdate(p0: MapView?, p1: MapPoint?, p2: Float) {
        if (!isStart){
            return
        }
        val lat = p1!!.mapPointGeoCoord.latitude
        val lon = p1!!.mapPointGeoCoord.longitude

        if (isPause) {
            val pausePolyline = MapPolyline()
            pausePolyline.lineColor = Color.argb(200, 243, 136, 71)
            pausePolyline.addPoint(mapPoint)
            pausePolyline.addPoint(p1)
            p0!!.addPolyline(pausePolyline)
            isPause = false
        } else {
            val newPolyline = MapPolyline()
            newPolyline.lineColor = Color.argb(200, 103, 114, 241)
            if (mapPoint != null) {
                newPolyline.addPoint(mapPoint)
            }
            newPolyline.addPoint(p1)
            p0!!.addPolyline(newPolyline)
        }
        mapPoint = p1
        polyline!!.addPoint(p1)

        if (prevLat == null && prevLon == null) {
            prevLat = lat
            prevLon = lon
            return
        } else {
            val distance = haversine(prevLat!!, prevLon!!, lat, lon)
            walkingDistance += distance

            if (walkingDistance < 1000) {
                distanceId.text = String.format("%.1f", walkingDistance)
            } else {
                digitId.text = "km"
                distanceId.text = String.format("%.3f", meterToKillo(walkingDistance))
            }
            prevLat = lat
            prevLon = lon
        }
        Log.d("DISTANCE", walkingDistance.toString())
    }

    override fun onCurrentLocationUpdateCancelled(p0: MapView?) {
    }

    override fun onCurrentLocationUpdateFailed(p0: MapView?) {

    }


    // 위도, 경도를 거리로 변환 - 리턴 값: Meter 단위
    private fun haversine (lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val r = 6372.8;
        val dLat = Math.toRadians(lat2 - lat1);
        val dLon = Math.toRadians(lon2 - lon1);
        val rLat1 = Math.toRadians(lat1);
        val rLat2 = Math.toRadians(lat2);
        var dist = sin(dLat / 2).pow(2.0) + sin(dLon / 2).pow(2.0) * cos(rLat1) * cos(rLat2);
        dist =  2 * asin(sqrt(dist))

        return  r * dist * 1000
    }

    private fun meterToKillo(meter: Double): Double {
        return meter / 1000
    }

    override fun onMapViewDoubleTapped(p0: MapView?, p1: MapPoint?) {
    }

    override fun onMapViewInitialized(p0: MapView?) {
    }

    override fun onMapViewDragStarted(p0: MapView?, p1: MapPoint?) {
        val mode = mapView!!.currentLocationTrackingMode

        if (mode.toString() == "TrackingModeOff") {
            return
        }
        mapView!!.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeadingWithoutMapMoving

        tapTimer = timer(period = 10000, initialDelay = 3000) {
            mapView!!.currentLocationTrackingMode = mode
            cancel()
        }
    }

    override fun onMapViewMoveFinished(p0: MapView?, p1: MapPoint?) {
    }

    override fun onMapViewCenterPointMoved(p0: MapView?, p1: MapPoint?) {
    }

    override fun onMapViewDragEnded(p0: MapView?, p1: MapPoint?) {
    }

    override fun onMapViewSingleTapped(p0: MapView?, p1: MapPoint?) {
    }

    override fun onMapViewZoomLevelChanged(p0: MapView?, p1: Int) {
    }

    override fun onMapViewLongPressed(p0: MapView?, p1: MapPoint?) {
    }
}



