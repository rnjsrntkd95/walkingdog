package com.example.walkingdog_kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.walkingdog_kotlin.Login.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

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
    }
}
