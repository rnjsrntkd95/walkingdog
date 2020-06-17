package com.example.walkingdog_kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.walkingdog_kotlin.model.MyFeedDTO
import kotlinx.android.synthetic.main.activity_my_photo.*
import kotlinx.android.synthetic.main.fragment_profile.*

class MyPhotoActivity : AppCompatActivity() {

    var myfeed_list = arrayListOf<MyFeedDTO>(

        MyFeedDTO("제목1","pets")
        ,MyFeedDTO("제목2","pets")
        , MyFeedDTO("제목1","pets")
        ,MyFeedDTO("제목2","pets")

    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_photo)

        val feedAdapter=Myfeed_RVAdapter(this, myfeed_list)
        myfeed_recyclerView.adapter=feedAdapter

        val lm = LinearLayoutManager(this)
        myfeed_recyclerView.layoutManager = lm
        myfeed_recyclerView.setHasFixedSize(true)

    }
}
