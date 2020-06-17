package com.example.walkingdog_kotlin.Login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.request.RequestOptions
import com.example.walkingdog_kotlin.*
import com.example.walkingdog_kotlin.Animal.AddPet
import com.example.walkingdog_kotlin.Challenge.ChallengeRetrofitCreators
import com.example.walkingdog_kotlin.Challenge.Model.myChallengeId
import com.example.walkingdog_kotlin.Login.Model.MyProfileModel
import com.example.walkingdog_kotlin.Walking.Statics
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProfileFragment (context: Context): Fragment() {

    val REQUEST_CODE = 100

    var addpetList = arrayListOf<ProfileItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val addpetAdapter = Addpet_RVAdapter(context!!, addpetList)
        add_recyclerView.adapter = addpetAdapter
        val lm = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        add_recyclerView.layoutManager = lm
        add_recyclerView.setHasFixedSize(true)

        myprofile_img_btn.setOnClickListener {
            openGalleryForImage()
        }

        user_nickname_view.setOnClickListener {
            val intent = Intent(context!!, SignUpActivity::class.java)
            startActivity(intent)
            (activity as MainActivity).finish()
        }

        // 프로필 정보 요청
        val pref = context!!.getSharedPreferences("pref", Context.MODE_PRIVATE)
        val userToken = pref.getString("userToken", "")
        val loginRetrofit = RetrofitCreators(context!!).ProfileRetrofit()
        loginRetrofit.getMyProfile(userToken!!).enqueue(object : Callback<MyProfileModel> {
            override fun onFailure(call: Call<MyProfileModel>, t: Throwable) {
                Log.d("DEBUG", "Profile Retrofit failed!!")
                Log.d("DEBUG", t.message)
            }

            override fun onResponse(call: Call<MyProfileModel>, response: Response<MyProfileModel>) {
                val error = response.body()?.error
                val profileURL = response.body()?.profile
                Log.d("ERROR", "$error")
                if (error == 1) {
                    return
                }
                // 프로필 이미지 로드
                if (profileURL != null || profileURL != "") {
                    GlideApp.with(context!!)
                        .load("${context!!.getString(R.string.server_url)}/${profileURL}")
                        .apply(RequestOptions.centerInsideTransform().override(500,500))
                        .into(myprofile_img_btn)
                    myprofile_img_btn.background = null
                }


                val userNickName = response.body()?.nickname
                val myPet = response.body()?.myPetList
                user_nickname_view.text = userNickName
                if (myPet != null && myPet.size != 0) {
                    myPet!!.forEach(fun(pet) {
                        addpetList.add(ProfileItem(pet.animalName))
                    })
                    addpetAdapter.notifyDataSetChanged()
                    add_pet_text_view.visibility = View.GONE
                }
            }
        })


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
            var intent = Intent(context, AddPet::class.java)
            startActivity(intent)
            (activity as MainActivity).finish()
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

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){
            //////////////첫번째방법//////////
//            myprofile_img_btn.setImageURI(data?.data) // handle chosen image
            //////////////////////////

            ///////////////////두번째방법/////////////////////
            val imageUri: Uri = data!!.data
            val bitmap =
                MediaStore.Images.Media.getBitmap(context!!.getContentResolver(), imageUri)

//            myprofile_img_btn.setImageBitmap(bitmap)

            ///////////////////////////////////////////

            ////////////////////비트맵을 drawable로 바꿔서 배경화면으로 설정하는 법/////////////////////////////
            val d: Drawable = BitmapDrawable(resources, bitmap)
            myprofile_img_btn.setBackground(d)



        }


    }

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE)
    }


}