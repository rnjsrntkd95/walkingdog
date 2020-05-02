package com.example.walkingdog_kotlin

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_check.*

class CheckFragment : Fragment() {

    var checkItemList = arrayListOf<CheckItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_check, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity!!.window.statusBarColor = (ContextCompat.getColor(context!!, R.color.mainBlue))
        

        switch_btn_to_walking.setOnClickListener {
            var intent = Intent(context, WalkingActivity::class.java)
            startActivity(intent)
        }

    }
}
