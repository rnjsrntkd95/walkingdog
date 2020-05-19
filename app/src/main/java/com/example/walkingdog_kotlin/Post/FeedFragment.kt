package com.example.walkingdog_kotlin.Post

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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

    var feedList = arrayListOf<FeedContent>(

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
                posts.forEach(fun(element) {
                    feedList.add(FeedContent(element))
                })

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

        val fAdapter = FeedAdaptor(context!!, feedList) { feedContent ->

        }

        feed_recyclerview.adapter = fAdapter

        val lm = LinearLayoutManager(context!!)
        feed_recyclerview.layoutManager = lm
        feed_recyclerview.setHasFixedSize(true)

        writeBtn.setOnClickListener {

        }

    }

}
