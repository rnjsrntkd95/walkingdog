package com.example.walkingdog_kotlin.Walking

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.walkingdog_kotlin.Post.WritePost
import androidx.core.content.ContextCompat
import com.example.walkingdog_kotlin.Camera
import com.example.walkingdog_kotlin.MainActivity
import com.example.walkingdog_kotlin.R
import com.example.walkingdog_kotlin.Statics
import com.example.walkingdog_kotlin.Walking.Model.CreateWalkingResultModel
import kotlinx.android.synthetic.main.activity_walking.*
import net.daum.mf.map.api.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.concurrent.timer
import kotlin.math.*

class WalkingActivity : AppCompatActivity(), MapView.CurrentLocationEventListener,
    MapView.MapViewEventListener, MapReverseGeoCoder.ReverseGeoCodingResultListener {
    private val RequestPermissionCode = 1
    private var mapView: MapView? = null
    private var polyline: MapPolyline? = null
    private var mapPoint: MapPoint? = null
    private var prevLat: Double? = null
    private var prevLon: Double? = null
    private var walkingDistance: Double = 0.0
    private var walkingCalorie: Double = 0.0
    private var isStart: Boolean = false
    private var isPause: Boolean = false
    private var tapTimer: Timer? = null
    private val route = ArrayList<ArrayList<Double>>()
    private val toiletLoc = ArrayList<ArrayList<Double>>()
    private val walkingAmounts = ArrayList<Double>()
    private var walkingTimer: Timer? = null
    private var runningDogImageTimer: Timer? = null
    private var runningDogImage = arrayOf(
        R.drawable.running_dog_1, R.drawable.running_dog_2
        , R.drawable.running_dog_3, R.drawable.running_dog_4, R.drawable.running_dog_5
        , R.drawable.running_dog_6, R.drawable.running_dog_7, R.drawable.running_dog_8
    )
    private var runningDogImageCounter: Int = 1
    private var time = 0
    private var isTimerRunning: Boolean = false
    private var getAddress: Boolean = false
    private var addressAdmin: String = ""
    private var addressLocality: String = ""
    private var addressThoroughfare: String = ""
    private var animal = ArrayList<String>()
    private var fullAmount = ArrayList<Double>()
    private var amountTest = 320.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_walking)

        var weights: DoubleArray? = null
        val extra = intent.extras

//        if (extra != null) {
//            val nameData = intent.getStringArrayListExtra("animalName")
//            val weightData = intent.getDoubleArrayExtra("animalWeight")
//            if (nameData != null && weightData != null) {
//                animal = nameData
//                weights = weightData
//
//            } else {
//                val intent = Intent(this, MainActivity::class.java)
//                startActivity(intent)
//                finish()
//                return
//            }
//        } else {
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
//            finish()
//            return
//        }
//
//        weights!!.forEach(fun(weight) {
//            fullAmount.add(((weight*30)+70)*1.4)
//        })

        pauseFab.visibility = View.GONE
        toiletFab.visibility = View.GONE

        // 현재 위치
        initView()

        playFab.setOnClickListener {
            if (!isPause) {
                isStart = true
            } else {
                isPause = false
            }
            timerSet()
            runningDog()
            pauseFab.visibility = View.VISIBLE
            toiletFab.visibility = View.VISIBLE
            playFab.visibility = View.GONE
            resetFab.visibility = View.GONE

        }
        pauseFab.setOnClickListener {
            isPause = true

            stopRunningDog()
            timerSet()
            pauseFab.visibility = View.GONE
            toiletFab.visibility = View.GONE
            playFab.visibility = View.VISIBLE
            resetFab.visibility = View.VISIBLE

        }
        resetFab.setOnClickListener {
            isStart = false
            if (walkingDistance != 0.0) {
                finishWalking()
                submitResult()
            }
            pauseFab.visibility = View.GONE
            toiletFab.visibility = View.GONE
            playFab.visibility = View.VISIBLE
            resetFab.visibility = View.VISIBLE

        }
        toiletFab.setOnClickListener {
            if (isStart && mapPoint != null) {
                toiletActivity()
            }
        }
        camera_btn.setOnClickListener {
            val intent = Intent(this, Camera::class.java)
            startActivity(intent)
        }

        this.window.statusBarColor = (ContextCompat.getColor(this, R.color.white))
        this.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }


    override fun onDestroy() {
        super.onDestroy()
    }

    fun findAddress() {
        val mapReverseGeoCoder =
            MapReverseGeoCoder(getString(R.string.kakao_app_key), mapPoint, this, this)

        mapReverseGeoCoder.startFindingAddress()
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
        mapView!!.setZoomLevel(0, true)
        mapView!!.setCustomCurrentLocationMarkerTrackingImage(
            R.drawable.labrador_icon,
            MapPOIItem.ImageOffset(50, 50)
        )
        mapView!!.setCustomCurrentLocationMarkerImage(
            R.drawable.labrador_icon,
            MapPOIItem.ImageOffset(50, 50)
        )
        mapView!!.currentLocationTrackingMode =
            MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading
        Log.d("트래킹", mapView!!.currentLocationTrackingMode.toString())
        mapView!!.setCurrentLocationEventListener(this)
        polyline = MapPolyline()
        polyline!!.tag = 1000
        polyline!!.lineColor = Color.argb(255, 103, 114, 241)
    }

    // 일시 정지
    private fun pauseWalking() {
        stopRunningDog()
        mapView!!.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOff
        isPause = true
    }

    // 재시작
    private fun restartWalking() {
        mapView!!.currentLocationTrackingMode =
            MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading
    }

    // 산책 종료
    private fun finishWalking() {
        stopRunningDog()
        mapView!!.addPolyline(polyline)
        mapView!!.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOff
        val mapPointBounds = MapPointBounds(polyline!!.mapPoints)
        mapView!!.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds, 100))

        // 시작 지점 표시
        val startMarker = MapPOIItem()
        startMarker.itemName = ""
        startMarker.mapPoint = polyline!!.getPoint(0)
        startMarker.markerType = MapPOIItem.MarkerType.CustomImage
        startMarker.customImageResourceId =
            R.drawable.start_walking_icon
        startMarker.setCustomImageAnchor(0.5f, 0.5f)
        startMarker.isCustomImageAutoscale = false
        startMarker.isShowCalloutBalloonOnTouch = false
        mapView!!.addPOIItem(startMarker)

        // 산책 충족량 확인
        fullAmount.forEach(fun(amount) {
            walkingAmounts.add((walkingCalorie / amount) * 100)
        })
    }

    private fun submitResult() {
        return
        val pref = getSharedPreferences("pref", MODE_PRIVATE)
        val userToken = pref.getString("userToken", "")
        val walkingRetrofit = WalkingRetrofitCreators(this).WalkingRetrofitCreator()
        walkingRetrofit.createWalking(
            userToken!!, walkingCalorie, walkingDistance, time, walkingAmounts,
            addressAdmin, addressLocality, addressThoroughfare, animal, route, toiletLoc
        ).enqueue(object : Callback<CreateWalkingResultModel> {
            override fun onFailure(call: Call<CreateWalkingResultModel>, t: Throwable) {
                Log.d("DEBUG", " Walking Retrofit failed!!")
                Log.d("DEBUG", t.message)
                Toast.makeText(
                    this@WalkingActivity,
                    "산책 등록에 실패하였습니다. 네트워크를 확인해주세요.",
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onResponse(
                call: Call<CreateWalkingResultModel>,
                response: Response<CreateWalkingResultModel>
            ) {
                val error = response.body()?.error
                val walkingId = response.body()?.walkingId
                Log.d("ERROR", error.toString())

                // 등록에 실패했을 때 후 처리

                // 산책 등록 후 처리 - 액티비티 이동
                if (error == null) {
                    val intent = Intent(this@WalkingActivity, Statics::class.java)
                    intent.putExtra("walkingId", walkingId)
                    startActivity(intent)
                    finish()
                }
            }
        })
    }

    // 배변활동 표시
    private fun toiletActivity() {
        val marker: MapPOIItem = MapPOIItem()
        marker.itemName = ""
        marker.isShowCalloutBalloonOnTouch = false
        marker.mapPoint = mapPoint
        marker.markerType = MapPOIItem.MarkerType.CustomImage
        marker.customImageResourceId =
            R.drawable.toilet_activity
        marker.isCustomImageAutoscale = false
        marker.setCustomImageAnchor(0.5f, 1.0f)
        mapView!!.addPOIItem(marker)
        toiletLoc.add(
            arrayListOf(
                marker.mapPoint.mapPointGeoCoord.latitude,
                marker.mapPoint.mapPointGeoCoord.longitude
            )
        )
    }

    // 위치 권한 설정 확인 함수
    private fun isSetLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermission()
        }
    }

    // 위치 권한 설정
    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            RequestPermissionCode
        )
        this.recreate()
    }

    override fun onCurrentLocationDeviceHeadingUpdate(p0: MapView?, p1: Float) {
    }

    override fun onCurrentLocationUpdate(p0: MapView?, p1: MapPoint?, p2: Float) {
        if (!isStart || isPause) {
            return
        }
        val lat = p1!!.mapPointGeoCoord.latitude
        val lon = p1!!.mapPointGeoCoord.longitude
        route.add(arrayListOf(lat, lon))

        mapPoint = p1
        polyline!!.addPoint(p1)
        p0!!.removePolyline(polyline)
        p0.addPolyline(polyline)

        if (prevLat == null && prevLon == null) {
            prevLat = lat
            prevLon = lon
            return
        } else {
            val distance = haversine(prevLat!!, prevLon!!, lat, lon)
            // 이동 거리 표시
            walkingDistance += distance
            if (walkingDistance < 1000) {
                distanceId.text = String.format("%.1f", walkingDistance)
            } else {
                digitId.text = "km"
                distanceId.text = String.format("%.3f", meterToKillo(walkingDistance))
            }
            // 소모 칼로리 표시
            walkingCalorie += distance * 0.026785714  // 1m당 소모 칼로리
            calorieView.text = String.format("%.2f", walkingCalorie)
            // 충족량 표시
            amountView.text = String.format("%.1f", walkingCalorie / amountTest * 100)

            prevLat = lat
            prevLon = lon
        }
        // 변환 주소 가져오기
        if (!getAddress) {
            findAddress()
        }
    }

    override fun onCurrentLocationUpdateCancelled(p0: MapView?) {
    }

    override fun onCurrentLocationUpdateFailed(p0: MapView?) {

    }


    // 위도, 경도를 거리로 변환 - 리턴 값: Meter 단위
    private fun haversine(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val r = 6372.8;
        val dLat = Math.toRadians(lat2 - lat1);
        val dLon = Math.toRadians(lon2 - lon1);
        val rLat1 = Math.toRadians(lat1);
        val rLat2 = Math.toRadians(lat2);
        var dist = sin(dLat / 2).pow(2.0) + sin(dLon / 2).pow(2.0) * cos(rLat1) * cos(rLat2);
        dist = 2 * asin(sqrt(dist))

        return r * dist * 1000
    }

    private fun meterToKillo(meter: Double): Double {
        return meter / 1000
    }


    private fun timerSet() {
        isTimerRunning = !isTimerRunning
        if (isTimerRunning)
            startTimer()
        else
            pauseTimer()
    }

    private fun startTimer() {
        walkingTimer = timer(period = 10) {
            time++
//            val hour = (time / 144000) % 24 // 1시간
            val min = (time / 6000) % 60 // 1분
            val sec = (time / 100) % 60 // 1초
            val milli = time % 100 // 0.01초

            runOnUiThread {
                if (min < 10) { // 분
                    minTextView.text = "0$min"
                } else {
                    minTextView.text = "$min"
                }

                if (sec < 10) { // 초
                    secTextView.text = "0$sec"
                } else {
                    secTextView.text = "$sec"
                }

                if (milli < 10) {
                    milliTextView.text = "0$milli"
                } else {
                    milliTextView.text = "$milli"
                }
            }
        }
    }

    private fun pauseTimer() {
        walkingTimer?.cancel()
    }

    private fun runningDog() {
        runningDogImageTimer = timer(period = 100) {
            runOnUiThread {
                runningDogImg.setBackgroundResource(runningDogImage[runningDogImageCounter++])
                if (runningDogImageCounter > 7) {
                    runningDogImageCounter = 0
                }
            }
        }
    }

    private fun stopRunningDog() {
        runningDogImageTimer!!.cancel()
        runningDogImg.setBackgroundResource(runningDogImage[0])
    }

    override fun onMapViewDoubleTapped(p0: MapView?, p1: MapPoint?) {
    }

    override fun onMapViewInitialized(p0: MapView?) {
    }

    override fun onMapViewDragStarted(p0: MapView?, p1: MapPoint?) {
    }

    override fun onMapViewMoveFinished(p0: MapView?, p1: MapPoint?) {
    }

    override fun onMapViewCenterPointMoved(p0: MapView?, p1: MapPoint?) {
    }

    override fun onMapViewDragEnded(p0: MapView?, p1: MapPoint?) {
        if (p0!!.currentLocationTrackingMode.toString() == "TrackingModeOff") {
            return
        }
        p0!!.currentLocationTrackingMode =
            MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeadingWithoutMapMoving

        if (tapTimer != null) {
            tapTimer!!.cancel()
        }
        tapTimer = timer(period = 3000, initialDelay = 3000) {
            p0!!.currentLocationTrackingMode =
                MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading
            cancel()
        }
    }

    override fun onMapViewSingleTapped(p0: MapView?, p1: MapPoint?) {
    }

    override fun onMapViewZoomLevelChanged(p0: MapView?, p1: Int) {
    }

    override fun onMapViewLongPressed(p0: MapView?, p1: MapPoint?) {
    }

    override fun onReverseGeoCoderFailedToFindAddress(p0: MapReverseGeoCoder?) {
    }

    override fun onReverseGeoCoderFoundAddress(p0: MapReverseGeoCoder?, p1: String?) {
        getAddress = true
        val address = p1!!.split(" ")
        addressAdmin = address[0]
        addressLocality = address[1]
        addressThoroughfare = address[2]
        val pref = getSharedPreferences("pref", MODE_PRIVATE)
        val edit = pref.edit()
        edit.putString("addressAdmin", address[0])
        edit.putString("addressLocality", address[1])
        edit.putString("addressThoroughfare", address[2])
        edit.apply()
    }

}