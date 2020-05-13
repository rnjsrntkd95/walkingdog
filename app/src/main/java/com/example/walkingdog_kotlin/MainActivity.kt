package com.example.walkingdog_kotlin

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.walkingdog_kotlin.Animal.AnimalRetrofit
import com.example.walkingdog_kotlin.Animal.AnimalRetrofitCreators
import com.example.walkingdog_kotlin.Animal.Model.BreedListModel
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import java.security.MessageDigest
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val animalRetrofit = AnimalRetrofitCreators(this).BreedRetrofitCreator()
        animalRetrofit.getAllBreed().enqueue(object : Callback<BreedListModel> {
            override fun onFailure(call: Call<BreedListModel>, t: Throwable) {
                Log.d("DEBUG", "Animal Retrofit Failed!!")
                Log.d("DEBUG", t.message)
            }

            override fun onResponse(call: Call<BreedListModel>, response: Response<BreedListModel>) {
                Log.d("DEBUG", "Animal Retrofit Success!!")

                val breedList = response.body()
                for (breed in breedList!!.breedList) {
                    Log.d("TAG", breed.breed)
                }

            }

        })

    }
}
