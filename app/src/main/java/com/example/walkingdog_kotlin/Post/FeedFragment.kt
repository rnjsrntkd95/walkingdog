package com.example.walkingdog_kotlin.Post

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.walkingdog_kotlin.Post.Model.PostListModel
import com.example.walkingdog_kotlin.R
import com.example.walkingdog_kotlin.model.FeedContent
import kotlinx.android.synthetic.main.fragment_feed.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FeedFragment(context: Context) : Fragment() {

//    var dogList = arrayListOf<Dog>(
//        Dog("chow cohw", "Male", "4", "dog00"),
//        Dog("chow cohw", "Male", "5", "dog01"),
//        Dog("chow cohw", "Male", "1", "dog02"),
//        Dog("chow cohw", "Male", "3", "dog03"),
//        Dog("chow cohw", "Male", "2", "dog04"),
//        Dog("chow cohw", "Male", "4", "dog05"),
//        Dog("chow cohw", "Male", "6", "dog06"),
//        Dog("chow cohw", "Male", "11","dog07"),
//        Dog("chow cohw", "Male", "7", "dog08"),
//        Dog("chow cohw", "Male", "7", "dog09"),
//        Dog("chow cohw", "Male", "7", "dog10"),
//        Dog("chow cohw", "Male", "7", "dog11"),
//        Dog("chow cohw", "Male", "7", "dog12"),
//        Dog("chow cohw", "Male", "7", "dog13")
//    )

    var feedList = arrayListOf<FeedContent>(
        FeedContent("dog00"),
        FeedContent("dog01"),
        FeedContent("dog02"),
        FeedContent("dog03"),
        FeedContent("dog04"),
        FeedContent("dog05"),
        FeedContent("dog06"),
        FeedContent("dog07"),
        FeedContent("dog08"),
        FeedContent("dog09")
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_feed, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val pref = context!!.getSharedPreferences("pref", MODE_PRIVATE)
        // Data
        val userToken = pref.getString("userToken", "none")
        val location = "인천광역시"

        val postRetrofit = PostRetrofitCreators(context!!).PostRetrofitCreator()
        postRetrofit.getTimeline(location, userToken!!).enqueue(object : Callback<PostListModel> {
            override fun onFailure(call: Call<PostListModel>, t: Throwable) {
                Log.d("DEBUG", " Timeline Retrofit failed!!")
                Log.d("DEBUG", t.message)            }

            override fun onResponse(call: Call<PostListModel>, response: Response<PostListModel>) {
                val posts = response.body()?.posts
                val error = response.body()?.error
                Log.d("TAG", posts!![0].toString())
                Log.d("TAG", posts!![1].toString())

                Log.d("TAG", "error: " + error)

            }

        })


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //프래그먼트에서 상태바 아이콘 검은색으로 변경하는 코드
        activity!!.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        //프래그먼트에서 상태바 배경색 변경하는 코드
        activity!!.window.statusBarColor = (ContextCompat.getColor(context!!,
            R.color.mainGray
        ))

//        val mAdapter = MainRvAdapter(context!!, dogList)
//        feed_recyclerview.adapter = mAdapter
//
//        val lm = LinearLayoutManager(context!!)
//        feed_recyclerview.layoutManager = lm
//        feed_recyclerview.setHasFixedSize(true)
        val fAdapter = FeedAdaptor(context!!, feedList)
        feed_recyclerview.adapter = fAdapter

        val lm = LinearLayoutManager(context!!)
        feed_recyclerview.layoutManager = lm
        feed_recyclerview.setHasFixedSize(true)

    }

}