package com.example.walkingdog_kotlin.Comment.Model

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
import com.example.walkingdog_kotlin.Challenge.Model.ChallengeListModel
import com.example.walkingdog_kotlin.R
import com.example.walkingdog_kotlin.Challenge.Model.Challenge
import kotlinx.android.synthetic.main.fragment_challenge.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.text.SimpleDateFormat

class CommentFragment(context: Context) : Fragment() {

    var challengeList = arrayListOf<Challenge>(
    )


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_challenge, container, false)


        //프래그먼트에서 상태바 아이콘 검은색으로 변경하는 코드
        activity!!.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        //프래그먼트에서 상태바 배경색 변경하는 코드
        activity!!.window.statusBarColor = (ContextCompat.getColor(context!!,
            R.color.mainBlue
        ))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        var test = challengeTitle_Textview.text

        drawerMenu_icon.setOnClickListener {
            Toast.makeText(context, test, Toast.LENGTH_LONG).show()
        }

        val cAdapter =
            ChallengeRvAdapter(
                context!!,
                challengeList
            )
        cRecyclerView.adapter = cAdapter

        val lm = LinearLayoutManager(context)
        cRecyclerView.layoutManager = lm
        cRecyclerView.setHasFixedSize(true)

        val pref = context!!.getSharedPreferences("pref", MODE_PRIVATE)
        // Data
        val userToken = pref.getString("userToken", "none")

        val challengeRetrofit = ChallengeRetrofitCreators(context!!).ChallengeRetrofitCreator()
        challengeRetrofit.getChallengeList(userToken).enqueue(object : Callback<ChallengeListModel> {
            override fun onFailure(call: Call<ChallengeListModel>, t: Throwable) {
                Log.d("DEBUG", " Challenge Retrofit failed!!")
                Log.d("DEBUG", t.message)
            }

            override fun onResponse(call: Call<ChallengeListModel>, response: Response<ChallengeListModel>) {
                val challenges = response.body()?.challenges

                challenges!!.forEach(fun(element) {
                    challengeList.add(Challenge(element))
                })
                cAdapter.notifyDataSetChanged()
            }
        })
    }
}
