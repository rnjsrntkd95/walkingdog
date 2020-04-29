package com.example.walkingdog_kotlin.Login.Information

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.walkingdog_kotlin.Login.SignUpActivity

import com.example.walkingdog_kotlin.R
import kotlinx.android.synthetic.main.fragment_password.*

class PasswordFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var inputPassword = ""
        var confirmInputPassword = ""
        var flag = 0

        password_edittext.transformationMethod = PasswordTransformationMethod.getInstance()
        password_cofirm_edittext.transformationMethod = PasswordTransformationMethod.getInstance()

        password_edittext.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                inputPassword = password_edittext.text.toString()
            }
        })

        password_cofirm_edittext.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                confirmInputPassword = password_cofirm_edittext.text.toString()
                if(inputPassword.isNotEmpty() && inputPassword==confirmInputPassword) {
                    flag=1
                    password_status_text.setTextColor(ContextCompat.getColor(context!!, R.color.green))
                    password_status_text.text = "사용 가능한 비밀번호입니다"
                }
                else{
                    flag=0
                    password_status_text.setTextColor(ContextCompat.getColor(context!!, R.color.red))
                    password_status_text.text = "비밀번호를 확인해주세요"
                }

            }
        })

        previous_btn_password.setOnClickListener {
            (activity as SignUpActivity).moveEmail()
        }

        next_btn_password.setOnClickListener {
            if(flag == 1)
                (activity as SignUpActivity).moveUserName()
        }

    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }
}
