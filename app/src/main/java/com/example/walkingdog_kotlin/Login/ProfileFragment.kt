package com.example.walkingdog_kotlin.Login

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import com.example.walkingdog_kotlin.*
import kotlinx.android.synthetic.main.activity_main.*

import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment : Fragment() {

        var isExisted : Boolean = true //현재 신청한 챌린지가 있는지 구분하는 변수

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
                if(isExisted) {     //신청한 챌린지가 존재한다면
                    var intent = Intent(context, MyChallengeActivity::class.java)
                    startActivity(intent)
                } else {            //신청한 챌린지가 없다면
                    //챌린지 프래그먼트 창으로 이동시킨다.
                    (activity as MainActivity).bottom_navigation.selectedItemId = R.id.action_challenge
                }

            }

        }
    }

