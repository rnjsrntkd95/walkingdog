package com.example.walkingdog_kotlin

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.walkingdog_kotlin.model.Breed
import kotlinx.android.synthetic.main.activity_add_pet.*
import kotlinx.android.synthetic.main.breed_rv_item.*

class AddPet : AppCompatActivity() {

    var breedList = arrayListOf<Breed>(
        Breed("요크셔테리어"),
        Breed("시츄"),
        Breed("불독"),
        Breed("도베르만"),
        Breed("진돗개"),
        Breed("시바견")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_pet)

        val bAdapter = BreedAdapter(this, breedList) {breed ->
            Toast.makeText(this, "개의 품종은 ${breed.breed}이다.", Toast.LENGTH_SHORT).show()


        }
        breed_rv.adapter = bAdapter

        val lm = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        breed_rv.layoutManager = lm
        breed_rv.setHasFixedSize(true)

        previous_btn_addPet.setOnClickListener {
            finish()
        }



    }
}
