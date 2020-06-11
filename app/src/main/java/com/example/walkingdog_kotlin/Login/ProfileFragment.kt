package com.example.walkingdog_kotlin.Login

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import com.example.walkingdog_kotlin.*
import com.example.walkingdog_kotlin.Challenge.ChallengeRetrofitCreators
import com.example.walkingdog_kotlin.Challenge.Model.ChallengeUserListModel
import com.example.walkingdog_kotlin.Challenge.Model.myChallengeId
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_my_challenge.*

import kotlinx.android.synthetic.main.fragment_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProfileFragment : Fragment() {
        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            return inflater.inflate(R.layout.fragment_profile, container, false)
        }

        override fun onActivityCreated(savedInstanceState: Bundle?) {
            super.onActivityCreated(savedInstanceState)

            my_photos.setOnClickListener {
                val intent =Intent(context,MyPhotoActivity::class.java)
                startActivity(intent)
            }
            statics.setOnClickListener {
                val intent =Intent(context,Statics::class.java)
                startActivity(intent)
            }

            myChallenge_layout.setOnClickListener {
                val pref = context?.getSharedPreferences("pref", Context.MODE_PRIVATE)
                val userToken = pref?.getString("userToken", "5ebac6bd59e3d32080d6d941")

                val challengeDetailRetrofit = ChallengeRetrofitCreators(context!!).ChallengeRetrofitCreator()
                challengeDetailRetrofit.getMyChallenge(userToken!!).enqueue(object : Callback<myChallengeId> {
                    override fun onFailure(call: Call<myChallengeId>, t: Throwable) {
                        Log.d("DEBUG", " Challenge Retrofit failed!!")
                        Log.d("DEBUG", t.message)
                    }
                    override fun onResponse(call: Call<myChallengeId>, response: Response<myChallengeId>) {
                        val challenge = response.body()?.myChallenge!![0].challengeId
                        if (challenge == null) {
                            (activity as MainActivity).bottom_navigation.selectedItemId = R.id.action_challenge
                        } else {
                            var intent = Intent(context, MyChallengeActivity::class.java)
                            intent.putExtra("challengeId", challenge)
                            startActivity(intent)
                        }
                    }
                })
            }
        }
    }

