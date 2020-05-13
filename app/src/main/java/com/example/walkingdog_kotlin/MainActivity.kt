package com.example.walkingdog_kotlin

import android.app.Activity
import android.content.pm.PackageManager
import android.util.Base64
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.walkingdog_kotlin.Animal.AnimalRetrofit
import com.example.walkingdog_kotlin.Animal.AnimalRetrofitCreators
import com.example.walkingdog_kotlin.Animal.Model.BreedListModel
import android.os.Bundle
import androidx.core.content.ContextCompat
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.walkingdog_kotlin.Login.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import java.security.MessageDigest
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {


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
                var challengeFragment = ChallengeFragment()
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView = findViewById<View>(R.id.bottom_navigation) as BottomNavigationView
        bottomNavigationView.setOnNavigationItemSelectedListener(this)

        //set default screen
        bottom_navigation.selectedItemId = R.id.action_home

        supportFragmentManager.beginTransaction().replace(R.id.main_content_layout, FeedFragment())
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
