package com.example.walkingdog_kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_add_item_pop_up_window.*

class AddItemPopUpWindow : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item_pop_up_window)

        ok_btn.setOnClickListener {
            finish()
        }

    }

}
