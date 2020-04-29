/*사용자 이름을 입력하는 화면*/

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
import kotlinx.android.synthetic.main.fragment_username.*

class UserNameFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_username, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var flag=0

        name_edittext.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(s: Editable?) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(name_edittext.text.isNotEmpty() && name_edittext.text.isNotBlank())
                    flag=1
                else
                    flag=0
            }
        })

        next_btn_name.setOnClickListener {
            if(flag==1)
                (activity as SignUpActivity).moveNickname()
        }

        previous_btn_name.setOnClickListener {
            (activity as SignUpActivity).movePassword()
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

}
