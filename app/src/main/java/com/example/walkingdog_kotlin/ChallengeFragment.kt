package com.example.walkingdog_kotlin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.walkingdog_kotlin.model.Challenge
import kotlinx.android.synthetic.main.fragment_challenge.*

class ChallengeFragment : Fragment() {

    var challengeList = arrayListOf<Challenge>(
        Challenge("경기대학교 챌린지","경기대학교 학생 챌린지 모여라~", "5월 1일 ~ 5월 7일", "30"),
        Challenge("광교 주민 챌린지","광교 반려견 산책 챌린지!!", "5월 1일 ~ 5월 14일", "70"),
        Challenge("말티즈 전용 챌린지","말티즈가 행복한 2주 챌린지!", "5월 4일 ~ 5월 18일", "50"),
        Challenge("수원시 챌린지","수원시 반려견 산책 챌린지에 도전하세요", "5월 11일 ~ 5월 25일", "170"),
        Challenge("직장인 모여라!","직장인들끼리 챌린지해요", "5월 11일 ~ 5월 25일", "120")
    )


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_challenge, container, false)


        //프래그먼트에서 상태바 아이콘 검은색으로 변경하는 코드
        activity!!.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        //프래그먼트에서 상태바 배경색 변경하는 코드
        activity!!.window.statusBarColor = (ContextCompat.getColor(context!!, R.color.mainBlue))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val cAdapter = ChallengeRvAdapter(context!!, challengeList)
        cRecyclerView.adapter = cAdapter

        val lm = LinearLayoutManager(context)
        cRecyclerView.layoutManager = lm
        cRecyclerView.setHasFixedSize(true)


    }

}
