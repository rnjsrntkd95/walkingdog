package com.example.walkingdog_kotlin.Login

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle

import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.walkingdog_kotlin.*
import com.example.walkingdog_kotlin.Animal.AddPet
import com.example.walkingdog_kotlin.Challenge.ChallengeRetrofitCreators

import com.example.walkingdog_kotlin.Challenge.Model.myChallengeId
import kotlinx.android.synthetic.main.activity_main.*

import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProfileFragment : Fragment() {
    private val OPEN_GALLERY = 0
    private val RC_SIGN_IN = 99


    var addpet_list = arrayListOf<ProfileItem>(

        ProfileItem("까까"),
        ProfileItem("뽀삐")
    )

    

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val addpetAdapter = Addpet_RVAdapter(context!!, addpet_list)

        add_recyclerView.adapter = addpetAdapter
        val lm = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        add_recyclerView.layoutManager = lm
        add_recyclerView.setHasFixedSize(true)

        logoutLayout.setOnClickListener {
            val pref = context!!.getSharedPreferences("pref", Context.MODE_PRIVATE)
            val edit = pref.edit()
            edit.remove("userToken")
            edit.apply()

            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
            Toast.makeText(context, "로그아웃 되었습니다.", Toast.LENGTH_LONG).show()
        }

        add_pet_btn.setOnClickListener {
            var intent = Intent(context, ProfileAddPetActivity::class.java)
            startActivity(intent)
        }

        walkingStaticLayout.setOnClickListener {
            val intent = Intent(context, Statics::class.java)
            startActivity(intent)
        }

//        myChallengeLayout.setOnClickListener {
//            val pref = context?.getSharedPreferences("pref", Context.MODE_PRIVATE)
//            val userToken = pref?.getString("userToken", "")
//        }

        walkingStaticLayout.setOnClickListener {
            val intent = Intent(context, Statics::class.java)
            startActivity(intent)
        }

        myChallengeLayout.setOnClickListener {
            val pref = context?.getSharedPreferences("pref", Context.MODE_PRIVATE)
            val userToken = pref?.getString("userToken", "")

            val challengeDetailRetrofit =
                ChallengeRetrofitCreators(context!!).ChallengeRetrofitCreator()
            challengeDetailRetrofit.getMyChallenge(userToken!!)
                .enqueue(object : Callback<myChallengeId> {
                    override fun onFailure(call: Call<myChallengeId>, t: Throwable) {
                        Log.d("DEBUG", " Challenge Retrofit failed!!")
                        Log.d("DEBUG", t.message)
                    }

                    override fun onResponse(
                        call: Call<myChallengeId>,
                        response: Response<myChallengeId>
                    ) {
                        val challenge = response.body()?.myChallenge
                        if (challenge == null || challenge == "") {
                            (activity as MainActivity).bottom_navigation.selectedItemId =
                                R.id.action_challenge
                        } else {
                            val intent = Intent(context, MyChallengeActivity::class.java)
                            intent.putExtra("challengeId", challenge)
                            startActivity(intent)
                            (activity as MainActivity).finish()
                        }
                    }
                })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == OPEN_GALLERY) {
            if (requestCode == RESULT_OK) {

                var currentImageUrl: Uri? = data?.data

                try {
                    val bitmap =
                        MediaStore.Images.Media.getBitmap(
                            context?.contentResolver,
                            currentImageUrl
                        )
                    myprofile_img_btn.setImageBitmap(bitmap)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } else {
            Log.d("ActivityResult", "something wrong")
        }
    }

    fun loadImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Load Picture"), OPEN_GALLERY)
    }

}