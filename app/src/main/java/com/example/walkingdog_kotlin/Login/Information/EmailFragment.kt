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
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.walkingdog_kotlin.Login.SignUpActivity

import com.example.walkingdog_kotlin.R
import kotlinx.android.synthetic.main.fragment_email.*


class EmailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_email, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var emailTest : String = "jspark2201@naver.com"
        var flag = 0

        previous_btn_email.setOnClickListener {
            (activity as SignUpActivity).finishActivity()
        }

        email_edittext.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) { //입력이 끝날 때 작동되는 함수

            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }   //입력하기 전에 작동되는 함수

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(email_edittext.text.toString() == emailTest) { //입력한 텍스트가 중복될 때.
                    flag=0
                    email_status_text.text = "이미 존재하는 이메일입니다"
                    email_status_text.setTextColor(ContextCompat.getColor(context!!, R.color.red))
                }
                else if(email_edittext.text.isEmpty()) {        //입력한 텍스트가 없을 때.
                    flag=0
                    email_status_text.text = ""
                }
                else if(email_edittext.text.isNotBlank()){      //입력한 텍스트가 사용가능할 때.
                    flag=1
                    email_status_text.text = "사용 가능한 이메일입니다"
                    email_status_text.setTextColor(ContextCompat.getColor(context!!, R.color.green))

                }
            }       //타이핑 되는 텍스트에 변화가 있을 때 작동하는 함수
        })

        //다음 버튼을 누르면 비밀번호 등록 프래그먼트로 이동하는 함수
        next_btn_email.setOnClickListener {
            if(flag == 1)
                (activity as SignUpActivity).movePassword()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }
}
