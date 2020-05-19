package com.example.walkingdog_kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.example.walkingdog_kotlin.Challenge.ChallengeFragment
import com.example.walkingdog_kotlin.Login.ProfileFragment
import com.example.walkingdog_kotlin.Post.FeedFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {


    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        when(p0.itemId) {
            R.id.action_home -> {
                var feedFragment = FeedFragment(this)
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView = findViewById<View>(R.id.bottom_navigation) as BottomNavigationView
        bottomNavigationView.setOnNavigationItemSelectedListener(this)

        //set default screen
        bottom_navigation.selectedItemId = R.id.action_home

        supportFragmentManager.beginTransaction().replace(R.id.main_content_layout,
            FeedFragment(this)
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
