package com.example.walkingdog_kotlin.Animal

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import android.widget.ToggleButton
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.walkingdog_kotlin.Animal.Model.BreedListModel
import com.example.walkingdog_kotlin.Animal.Model.Breed
import com.example.walkingdog_kotlin.MainActivity
import com.example.walkingdog_kotlin.R
import kotlinx.android.synthetic.main.activity_add_pet.*
import kotlinx.android.synthetic.main.breed_rv_item.*
import kotlinx.android.synthetic.main.feed_item.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class AddPet : AppCompatActivity() {

    var breedList = arrayListOf<Breed>(
        Breed("요크셔테리어"),
        Breed("시츄"),
        Breed("불독"),
        Breed("도베르만"),
        Breed("진돗개"),
        Breed("시바견")
    )

    var petName : String = ""
    var petWeight : Int = 0
    var petAge : Int = 0
    var petBreed : String = ""
    var petGender : String = ""

    val Gallery = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_pet)

        rv_image_area.setOnClickListener { loadImage() }

        val bAdapter = BreedAdapter(this, breedList) { breed ->
            petBreed = breed.breed
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

        pass_tv.setOnClickListener {
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        finish_btn_addPet.setOnClickListener {
            //Toast.makeText(this, petGender, Toast.LENGTH_SHORT).show()
            Toast.makeText(this, petBreed, Toast.LENGTH_SHORT).show()
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

        if(requestCode == Gallery) {
            if(resultCode == RESULT_OK) {
                var dataUri = data?.data
                try {
                    var bitmap : Bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, dataUri)
                    rv_image_area.setImageBitmap(bitmap)
                } catch (e:Exception) {
                    Toast.makeText(this, "$e", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            //somthing wrong
        }
    }
}
