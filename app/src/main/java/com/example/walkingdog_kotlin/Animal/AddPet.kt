package com.example.walkingdog_kotlin.Animal

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.walkingdog_kotlin.Animal.Model.BreedListModel
import com.example.walkingdog_kotlin.Animal.Model.Breed
import com.example.walkingdog_kotlin.Animal.Model.SetNewAnimalModel
import com.example.walkingdog_kotlin.MainActivity
import com.example.walkingdog_kotlin.R
import kotlinx.android.synthetic.main.activity_add_pet.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddPet : AppCompatActivity() {

    var breedList = arrayListOf<Breed>(
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_pet)



        val bAdapter = BreedAdapter(this, breedList) { breed ->
            Toast.makeText(this, "개의 품종은 ${breed.breed}이다.", Toast.LENGTH_SHORT).show()
        }

        breed_rv.adapter = bAdapter

        val lm = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        breed_rv.layoutManager = lm
        breed_rv.setHasFixedSize(true)

        // Get All Breed Retrofit Test
        val animalRetrofit = AnimalRetrofitCreators(this).AnimalRetrofitCreator()
        animalRetrofit.getAllBreed().enqueue(object : Callback<BreedListModel> {
            override fun onFailure(call: Call<BreedListModel>, t: Throwable) {
                Log.d("DEBUG", "Breed Retrofit Failed!!")
                Log.d("DEBUG", t.message)
            }
            override fun onResponse(call: Call<BreedListModel>, response: Response<BreedListModel>) {
                Log.d("DEBUG", "Breed Retrofit Success!!")

                val resList = response.body()
                for (breed in resList!!.breedList) {
                    breedList.add(Breed(breed.breed))
                }

                bAdapter.notifyDataSetChanged()
            }
        })




        previous_btn_addPet.setOnClickListener {
            finish()
        }

        finish_btn_addPet.setOnClickListener {
            val pref = getSharedPreferences("pref", MODE_PRIVATE)
            // Data
            val userToken = pref.getString("userToken", "none")
            val breed = "푸들"
            val animalName = "까까"
            val age = 11
            val weight = 10.3
            val gender = "남"

            val animalRetrofit = AnimalRetrofitCreators(this).AnimalRetrofitCreator()
            animalRetrofit.setNewAnimal(userToken!!, breed, animalName, age, weight, gender).enqueue(object : Callback<SetNewAnimalModel> {
                override fun onFailure(call: Call<SetNewAnimalModel>, t: Throwable) {
                    Log.d("DEBUG", " Add Pet Retrofit failed!!")
                    Log.d("DEBUG", t.message)
                }

                override fun onResponse(call: Call<SetNewAnimalModel>, response: Response<SetNewAnimalModel>) {
                    val error = response.body()?.error

                    if (error == null) {
                        val intent = Intent(this@AddPet, MainActivity::class.java)
                        startActivity(intent)
                        (this@AddPet as Activity).finish()
                    } else if (error == 1) {
                        val errItem = response.body()?.item
                        Toast.makeText(this@AddPet, "올바르지 않은 "+ errItem.toString() + " 입니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }
    }
}
