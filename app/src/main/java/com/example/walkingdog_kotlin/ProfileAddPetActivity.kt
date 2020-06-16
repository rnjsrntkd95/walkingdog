package com.example.walkingdog_kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.app.Activity
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.walkingdog_kotlin.Animal.AnimalRetrofitCreators
import com.example.walkingdog_kotlin.Animal.BreedAdapter
import com.example.walkingdog_kotlin.Animal.Model.BreedListModel
import com.example.walkingdog_kotlin.Animal.Model.Breed
import com.example.walkingdog_kotlin.Animal.Model.SetNewAnimalModel
import com.example.walkingdog_kotlin.Login.ProfileFragment
import kotlinx.android.synthetic.main.activity_profile_add_pet.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProfileAddPetActivity : AppCompatActivity() {

    var breedList = arrayListOf<Breed>(
    )

    var petName : String = ""
    var petWeight : Int = 0
    var petAge : Int = 0
    var petBreed : String = ""
    var petGender : String = ""

    val Gallery = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_add_pet)

        val bAdapter = BreedAdapter(this, breedList, View.OnClickListener {  }) { breed ->
            petBreed = breed.breed
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
                    breedList.add(Breed(breed.breed, false))
                }

                bAdapter.notifyDataSetChanged()
            }
        })

        previous_btn_addPet.setOnClickListener {
            finish()
        }

        finish_btn_addPet2.setOnClickListener {
            val pref = getSharedPreferences("pref", MODE_PRIVATE)
            // Data
            val userToken = pref.getString("userToken", "none")
            val breed = petBreed
            val animalName = petName
            val age = petAge
            val weight = petWeight
            val gender = petGender

            val animalRetrofit = AnimalRetrofitCreators(this).AnimalRetrofitCreator()
            animalRetrofit.setNewAnimal(userToken!!, breed, animalName, age, weight, gender).enqueue(object : Callback<SetNewAnimalModel> {
                override fun onFailure(call: Call<SetNewAnimalModel>, t: Throwable) {
                    Log.d("DEBUG", " Add Pet Retrofit failed!!")
                    Log.d("DEBUG", t.message)
                }

                override fun onResponse(call: Call<SetNewAnimalModel>, response: Response<SetNewAnimalModel>) {
                    val error = response.body()?.error

                    if (error == null) {
                        val intent = Intent(this@ProfileAddPetActivity, ProfileFragment::class.java)
                        startActivity(intent)
                        (this@ProfileAddPetActivity as Activity).finish()
                    } else if (error == 1) {
                        val errItem = response.body()?.item
                        Toast.makeText(this@ProfileAddPetActivity, "올바르지 않은 "+ errItem.toString() + " 입니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            })
            finish()
        }

        petName_editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                petName = petName_editText.text.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })

        petWeight_editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                petWeight = petWeight_editText.text.toString().toInt()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })

        petAge_editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                petAge = petAge_editText.text.toString().toInt()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })

        male_btn.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) {
                female_btn.isChecked = false
                male_btn.setBackgroundResource(R.drawable.male_breed_shape)
                petGender = male_btn.text.toString()
            } else{
                male_btn.setBackgroundResource(R.drawable.breed_shape)
                petGender = ""
            }
        }

        female_btn.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) {
                male_btn.isChecked = false
                female_btn.setBackgroundResource(R.drawable.female_breed_shape)
                petGender = female_btn.text.toString()
            } else{
                female_btn.setBackgroundResource(R.drawable.breed_shape)
                petGender = ""
            }
        }
    }   //onCreate

    private fun loadImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(Intent.createChooser(intent, "Load Picture"), Gallery)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

    }

}

