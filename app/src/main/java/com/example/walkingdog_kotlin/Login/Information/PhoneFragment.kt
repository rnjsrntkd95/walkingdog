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
import com.example.walkingdog_kotlin.Login.SignUpActivity

import com.example.walkingdog_kotlin.R
import kotlinx.android.synthetic.main.fragment_phone.*


class PhoneFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_phone, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        var flag=0
        var input_phone_num = ""

        phoneNum_edittext.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(s: Editable?) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (phoneNum_edittext.text.isNotEmpty() && phoneNum_edittext.text.isNotBlank()) {
                    flag=1
                }
                else {
                    flag=0
                }
            }
        })

        previous_btn_phoneNum.setOnClickListener {
            (activity as SignUpActivity).moveNickname()
        }

        next_btn_phoneNum.setOnClickListener {
            if(flag==1)
                (activity as SignUpActivity).startMain()
        }
    }

}
