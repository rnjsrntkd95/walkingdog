package com.example.walkingdog_kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Adapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.walkingdog_kotlin.Login.ProfileFragment
import kotlinx.android.synthetic.main.fragment_profile.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var profileFragment = ProfileFragment()
        supportFragmentManager.beginTransaction().replace(R.id.content_layout, profileFragment).commit()
    }
}
