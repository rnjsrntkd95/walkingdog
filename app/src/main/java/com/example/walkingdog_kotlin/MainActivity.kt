package com.example.walkingdog_kotlin

import android.content.Context
import android.content.pm.PackageManager
import android.location.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.core.app.ActivityCompat
import com.example.walkingdog_kotlin.Challenge.ChallengeFragment
import com.example.walkingdog_kotlin.Profile.ProfileFragment
import com.example.walkingdog_kotlin.Post.FeedFragment
import com.example.walkingdog_kotlin.Walking.CheckFragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    val RequestPermissionCode = 1
    var mLocation: Location? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        when(p0.itemId) {
            R.id.action_home -> {
                var feedFragment = FeedFragment()
                supportFragmentManager.beginTransaction().replace(R.id.main_content_layout, feedFragment)
                    .commit()
            }
            R.id.action_Walking -> {
                var checkFragment =
                    CheckFragment()
                supportFragmentManager.beginTransaction().replace(R.id.main_content_layout, checkFragment)
                    .commit()
            }
            R.id.action_challenge -> {
                var challengeFragment =
                    ChallengeFragment(this)
                supportFragmentManager.beginTransaction().replace(R.id.main_content_layout, challengeFragment)
                    .commit()
            }
            R.id.action_profile -> {
                var profileFragment =
                    ProfileFragment(this)
                supportFragmentManager.beginTransaction().replace(R.id.main_content_layout, profileFragment)
                    .commit()
            }
        }
        return true
    }

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
                        val edit = pref.edit()
                        edit.putString("latitude", location.latitude.toString())
                        edit.putString("longitude", location.longitude.toString())
                        edit.commit()


                        val geocoder: Geocoder = Geocoder(this)
                        var address: List<Address>? = null
                        try {
                            address = geocoder.getFromLocation(location.latitude, location.longitude, 10)

                            if (address != null) {
                                if (address.size == 0) {
                                    Log.d("TAG", "해당 주소 없음")
                                    // Default 처리 필요

                                } else {
                                    for (addressIndex in address) {
                                        if (addressIndex.adminArea != null &&
                                            addressIndex.locality != null &&
                                            addressIndex.thoroughfare != null){
                                            edit.putString("addressAdmin", addressIndex.adminArea.toString())
                                            edit.putString("addressLocality", addressIndex.locality.toString())
                                            edit.putString("addressThoroughfare", addressIndex.thoroughfare.toString())
                                            edit.commit()
                                            break
                                        }
                                    }

                                }
                            }
                        }catch (e: Exception) {
                            Log.d("TAG", "Location get failed!!")
                        }
                    } else {
                        Log.d("TAG", "Location get failed!!")
                        val latitude = pref.getString("latitude", "37.3004755").toDouble()
                        val longitude = pref.getString("longitude", "127.034374").toDouble()
                    }
                }
                .addOnFailureListener {
                    Log.d("TAG", "Location error is ${it.message}")
                    it.printStackTrace()
                    val latitude = pref.getString("latitude", "37.3004755").toDouble()
                    val longitude = pref.getString("longitude", "127.034374").toDouble()
                }
        }
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
            RequestPermissionCode
        )
        this.recreate()
    }



    fun test() {
        bottom_navigation.selectedItemId = R.id.action_challenge
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getLastLocation()

        val bottomNavigationView = findViewById<View>(R.id.bottom_navigation) as BottomNavigationView
        bottomNavigationView.setOnNavigationItemSelectedListener(this)

        val extras = intent.extras
        if(extras != null) {
            val cFlag = extras.getBoolean("cFlag", false)
            val pFlag = extras.getBoolean("pFlag", false)
            if (cFlag) {
                bottom_navigation.selectedItemId = R.id.action_challenge
            } else if (pFlag) {
                bottom_navigation.selectedItemId = R.id.action_profile
            } else {
                bottom_navigation.selectedItemId = R.id.action_home
            }
        } else {
            //set default screen
            bottom_navigation.selectedItemId = R.id.action_home
        }
    }
}
