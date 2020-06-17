package com.example.walkingdog_kotlin.Challenge

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.walkingdog_kotlin.Challenge.Model.ChallengeListModel
import com.example.walkingdog_kotlin.R
import com.example.walkingdog_kotlin.Challenge.Model.Challenge
import com.example.walkingdog_kotlin.Challenge.Model.ChallengeModel
import com.example.walkingdog_kotlin.MainActivity
import com.example.walkingdog_kotlin.ReformatDate
import kotlinx.android.synthetic.main.fragment_challenge.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChallengeFragment(context: Context) : Fragment() {

    var challengeList = arrayListOf<Challenge>(

    )
    var selected_challenge_title : String? = null
    var selected_challenge_content : String? = null
    var selected_challenge_period : String? = null
    var popularChallengeId : String? =null
    var participateChallengeId : String ? = null
    var error = 0

    val pref = context!!.getSharedPreferences("pref", MODE_PRIVATE)
    val userToken = pref.getString("userToken", "")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_challenge, container, false)


//        //프래그먼트에서 상태바 아이콘 검은색으로 변경하는 코드
//        activity!!.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
//        //프래그먼트에서 상태바 배경색 변경하는 코드
//        activity!!.window.statusBarColor = (ContextCompat.getColor(context!!,
//            R.color.mainBlue
//        ))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        var test = challengeTitle_Textview.text

        val cAdapter =
            ChallengeRvAdapter(context!!, challengeList) { challenge ->
                var intent = Intent(context, MyChallengeActivity::class.java)
                intent.putExtra("challengeId", challenge.id)
                startActivity(intent)
                (activity as MainActivity).finish()
            }
        cRecyclerView.adapter = cAdapter

        val lm = LinearLayoutManager(context)
        cRecyclerView.layoutManager = lm
        cRecyclerView.setHasFixedSize(true)
        //challenges sort by date
        val challengeRetrofit = ChallengeRetrofitCreators(context!!).ChallengeRetrofitCreator()
        challengeRetrofit.getChallengeList(userToken).enqueue(object : Callback<ChallengeListModel> {
            override fun onFailure(call: Call<ChallengeListModel>, t: Throwable) {
                Log.d("DEBUG", " Challenge Retrofit failed!!")
                Log.d("DEBUG", t.message)
            }
            override fun onResponse(call: Call<ChallengeListModel>, response: Response<ChallengeListModel>) {
                val challenges = response.body()?.challenges
                val popularChallenge = response.body()?.popularChallenge
                if(challenges == null || challenges!!.isEmpty()){
                    return
                }
                if(popularChallenge != null){
                    popularChallengeId = popularChallenge._id
                    challengeTitle_Textview.text = popularChallenge!!.title
                    challenge_period.text = ReformatDate("MM월 dd일", popularChallenge!!.start_date) + " ~ " +
                            ReformatDate("MM월 dd일", popularChallenge!!.end_date)
                    popular_challenge_content_tv.text = popularChallenge!!.content
                    personnel.text = popularChallenge!!.population.toString()
                }
                challenges!!.forEach(fun(element) {
                    challengeList.add(Challenge(element))
                })
                cAdapter.notifyDataSetChanged()
            }
        })

        create_challenge.setOnClickListener {
            var intent = Intent(context, Create_ChallengeActivity::class.java)
            startActivity(intent)
            (activity as MainActivity).finish()
        }

        popular_challenge_content_layout.setOnClickListener {
            val intent = Intent(context, MyChallengeActivity::class.java)
            intent.putExtra("challengeId", popularChallengeId)
            startActivity(intent)
            (activity as MainActivity).finish()
        }
    }
}
