package com.example.walkingdog_kotlin.Animal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.walkingdog_kotlin.Animal.Model.BreedListModel
import com.example.walkingdog_kotlin.Animal.Model.Breed
import com.example.walkingdog_kotlin.R
import kotlinx.android.synthetic.main.activity_add_pet.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddPet : AppCompatActivity() {

    var breedList = arrayListOf<Breed>(
//        Breed("요크셔테리어"),
//        Breed("시츄"),
//        Breed("불독"),
//        Breed("도베르만"),
//        Breed("진돗개"),
//        Breed("시바견")
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
        val animalRetrofit = AnimalRetrofitCreators(this).BreedRetrofitCreator()
        animalRetrofit.getAllBreed().enqueue(object : Callback<BreedListModel> {
            override fun onFailure(call: Call<BreedListModel>, t: Throwable) {
                Log.d("DEBUG", "Animal Retrofit Failed!!")
                Log.d("DEBUG", t.message)
            }
            override fun onResponse(call: Call<BreedListModel>, response: Response<BreedListModel>) {
                Log.d("DEBUG", "Animal Retrofit Success!!")

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
        }
    }
}
