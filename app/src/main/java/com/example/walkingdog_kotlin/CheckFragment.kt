package com.example.walkingdog_kotlin

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Layout
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_check.*

class CheckFragment : Fragment() {

    var checkItemList = arrayListOf<CheckItem>(
        CheckItem("목줄"),
        CheckItem("식수"),
        CheckItem("입마개"),
        CheckItem("배변봉투")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_check, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity!!.window.statusBarColor = (ContextCompat.getColor(context!!, R.color.mainBlue))


        val checkItemAdapter = CheckListAdapter(context!!, checkItemList)
        checkListView.adapter = checkItemAdapter


        sub_item_btn.setOnClickListener {
            //checkItemList.removeAt(4)
            checkItemAdapter.notifyDataSetChanged()
        }

        add_item_btn.setOnClickListener {
            val window = PopupWindow(context)
            val view = layoutInflater.inflate(R.layout.activity_add_item_pop_up_window, null)
            window.contentView = view

            //checkItemList.add(CheckItem("간식"))
            checkItemAdapter.notifyDataSetChanged()
        }

        switch_btn_to_walking.setOnClickListener {
            var intent = Intent(context, WalkingActivity::class.java)
            startActivity(intent)
        }


    }
}
