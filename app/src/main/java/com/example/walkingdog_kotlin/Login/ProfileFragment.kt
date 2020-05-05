package com.example.walkingdog_kotlin.Login

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.walkingdog_kotlin.MyPhotoActivity

import com.example.walkingdog_kotlin.R
import kotlinx.android.synthetic.main.fragment_profile.*


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
        }
    }

