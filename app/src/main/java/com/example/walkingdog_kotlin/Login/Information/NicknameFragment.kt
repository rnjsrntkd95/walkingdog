package com.example.walkingdog_kotlin.Login.Information

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.walkingdog_kotlin.Login.RetrofitCreators
import com.example.walkingdog_kotlin.Login.SignUpActivity

import com.example.walkingdog_kotlin.R
import kotlinx.android.synthetic.main.fragment_email.*
import kotlinx.android.synthetic.main.fragment_nickname.*

class NicknameFragment : Fragment() {

    var nicknametest = "jspark"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_nickname, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        var flag=0

        nickname_edittext.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(nickname_edittext.text.toString() == nicknametest) {
                    flag=0
                    nickname_status_text.text = "이미 존재하는 닉네임입니다"
                    nickname_status_text.setTextColor(ContextCompat.getColor(context!!, R.color.red))
                }
                else if(nickname_edittext.text.isEmpty()) {
                    flag=0
                    nickname_status_text.text = ""
                }
                else if(nickname_edittext.text.isNotBlank()){
                    flag=1
                    nickname_status_text.text = "사용 가능한 닉네임입니다"
                    nickname_status_text.setTextColor(ContextCompat.getColor(context!!, R.color.green))
                }
            }
        })

        previous_btn_nickname.setOnClickListener {
            (activity as SignUpActivity).moveUserName()
        }

        next_btn_nickname.setOnClickListener {
            val retrofit = RetrofitCreators(context!!)
            retrofit.SetNicknameRetrofit(nickname_edittext.text.toString())


        }



    }

}
