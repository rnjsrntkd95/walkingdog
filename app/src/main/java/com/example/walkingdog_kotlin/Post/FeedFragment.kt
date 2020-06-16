package com.example.walkingdog_kotlin.Post

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.walkingdog_kotlin.Post.Model.FeedContent
import com.example.walkingdog_kotlin.Post.Model.PostListModel
import com.example.walkingdog_kotlin.Post.Model.PostModel
import com.example.walkingdog_kotlin.Post.Model.UserProfileModel
import com.example.walkingdog_kotlin.R
import com.example.walkingdog_kotlin.Statics
import kotlinx.android.synthetic.main.feed_item.*
import kotlinx.android.synthetic.main.fragment_feed.*
import kotlinx.android.synthetic.main.map_popup.*
import kotlinx.android.synthetic.main.map_popup.view.*
import net.daum.mf.map.api.MapView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class FeedFragment() : Fragment() {

    init {
    }

    var feedList = arrayListOf<FeedContent>(
        FeedContent(),
        FeedContent(),
        FeedContent()
    )


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_feed, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fAdapter = FeedAdaptor(context!!, feedList) { feedContent ->
        }

        feed_recyclerview.adapter = fAdapter

        val lm = LinearLayoutManager(context!!)
        feed_recyclerview.layoutManager = lm
        feed_recyclerview.setHasFixedSize(true)

        writeBtn.setOnClickListener {
            var intent = Intent(context, Statics::class.java)
            startActivity(intent)
        }

        filter_btn.setOnClickListener {
            val popupMenu : PopupMenu = PopupMenu(context, filter_btn)
            popupMenu.menuInflater.inflate(R.menu.post_popup_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                when(item.itemId) {
                    R.id.action_location ->
                        Toast.makeText(context, item.title, Toast.LENGTH_SHORT).show()
                    R.id.action_breed ->
                        Toast.makeText(context, item.title, Toast.LENGTH_SHORT).show()
                }
                true
            })
            popupMenu.show()
        }

        //// TimeLine Retrofit ////
        val pref = context!!.getSharedPreferences("pref", MODE_PRIVATE)
        // Data
        val userToken = pref.getString("userToken", "")
        Log.d("유저토큰을보자 타임라인", userToken)

        val addressAdmin = pref.getString("addressAdmin", "")
        val addressLocality = pref.getString("addressLocality", "")
        val addressThoroughfare = pref.getString("addressThoroughfare", "")
        val postRetrofit = PostRetrofitCreators(context!!).PostRetrofitCreator()
        postRetrofit.getTimeline(addressAdmin, addressLocality, addressThoroughfare).enqueue(object : Callback<PostListModel> {
            override fun onFailure(call: Call<PostListModel>, t: Throwable) {
                Log.d("DEBUG", " Timeline Retrofit failed!!")
                Log.d("DEBUG", t.message)
            }

            override fun onResponse(call: Call<PostListModel>, response: Response<PostListModel>) {
                val posts = response.body()?.posts
                val error = response.body()?.error
                Log.d("TAG", "error: " + error)
                posts!!.forEach(fun(element) {
                    feedList.add(FeedContent(element))
                })
                fAdapter.notifyDataSetChanged()
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

//        //프래그먼트에서 상태바 아이콘 검은색으로 변경하는 코드
//        activity!!.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        activity!!.window.statusBarColor = (ContextCompat.getColor(context!!,
            R.color.green2
        ))
    }
}
