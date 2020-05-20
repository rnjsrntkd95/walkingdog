package com.example.walkingdog_kotlin

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.core.app.ActivityCompat
import com.example.walkingdog_kotlin.Challenge.ChallengeFragment
import com.example.walkingdog_kotlin.Login.ProfileFragment
import com.example.walkingdog_kotlin.Post.FeedFragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.jar.Manifest

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
                var checkFragment = CheckFragment()
                supportFragmentManager.beginTransaction().replace(R.id.main_content_layout, checkFragment)
                    .commit()
            }
            R.id.action_challenge -> {
                var challengeFragment =
                    ChallengeFragment(
                        this
                    )
                supportFragmentManager.beginTransaction().replace(R.id.main_content_layout, challengeFragment)
                    .commit()
            }
            R.id.action_profile -> {
                var profileFragment = ProfileFragment()
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
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    mLocation = location
                    if (location != null) {
                        Log.d("TAG", "${location.latitude}, ${location.longitude}")
                    } else {
                        Log.d("TAG", "Location get failed!!")
                    }
                }
                .addOnFailureListener {
                    Log.d("TAG", "Location error is ${it.message}")
                    it.printStackTrace()
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getLastLocation()

















        val bottomNavigationView = findViewById<View>(R.id.bottom_navigation) as BottomNavigationView
        bottomNavigationView.setOnNavigationItemSelectedListener(this)

        //set default screen
        bottom_navigation.selectedItemId = R.id.action_home

        supportFragmentManager.beginTransaction().replace(R.id.main_content_layout,
            FeedFragment()
        )
            .commit()

        //액티비티에서 상태바 아이콘 흰색으로 만드는 코드
        //window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        //액티비티에서 상태바 배경 색 변경하는 코드1
        //window.statusBarColor = (ContextCompat.getColor(applicationContext, R.color.mainGray))

        //상태바 색 변경하는 코드2
        //getWindow().setStatusBarColor(Color.parseColor("#edf1f5"))

//       // Get All Breed Retrofit Test
//        val animalRetrofit = AnimalRetrofitCreators(this).BreedRetrofitCreator()
//        animalRetrofit.getAllBreed().enqueue(object : Callback<BreedListModel> {
//            override fun onFailure(call: Call<BreedListModel>, t: Throwable) {
//                Log.d("DEBUG", "Animal Retrofit Failed!!")
//                Log.d("DEBUG", t.message)
//            }
//
//
//
//            override fun onResponse(call: Call<BreedListModel>, response: Response<BreedListModel>) {
//                Log.d("DEBUG", "Animal Retrofit Success!!")
//
//                val breedList = response.body()
//                for (breed in breedList!!.breedList) {
//                    Log.d("TAG", breed.breed)
//                }
//
//            }
//
//        })

    }
}
