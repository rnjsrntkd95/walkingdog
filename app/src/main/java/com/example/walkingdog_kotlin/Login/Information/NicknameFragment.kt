package com.example.walkingdog_kotlin.Login.Information

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.walkingdog_kotlin.Animal.AddPet
import com.example.walkingdog_kotlin.Login.LoginActivity
import com.example.walkingdog_kotlin.Login.Model.SetNicknameModel
import com.example.walkingdog_kotlin.Login.RetrofitCreators
import com.example.walkingdog_kotlin.Login.SignUpActivity
import com.example.walkingdog_kotlin.MainActivity

import com.example.walkingdog_kotlin.R
import kotlinx.android.synthetic.main.fragment_nickname.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NicknameFragment(context: Context) : Fragment() {

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


        previous_btn_nickname.setOnClickListener {
            (activity as SignUpActivity).finish()
        }

        next_btn_nickname.setOnClickListener {
            val pref = context!!.getSharedPreferences("pref", Context.MODE_PRIVATE)
            val userToken = pref.getString("userToken", "none")
            val nickname = nickname_edittext.text.toString()

            val retrofit = RetrofitCreators(context!!)
            val nicknameRetrofit = retrofit.SetNicknameRetrofit(nickname_edittext.text.toString())

            nicknameRetrofit.setNickname(userToken!!, nickname).enqueue(object: Callback<SetNicknameModel> {
                override fun onFailure(call: Call<SetNicknameModel>, t: Throwable) {
                    Log.d("DEBUG", " Nickname Retrofit failed!!")
                    Log.d("DEBUG", t.message)
                }

                override fun onResponse(call: Call<SetNicknameModel>, response: Response<SetNicknameModel>) {
                    val error = response.body()?.error
                    Log.d("TAG", error.toString())

                    if (error == 0) {
                        val intent = Intent(context, MainActivity::class.java)
                        intent.putExtra("pFlag", true)
                        startActivity(intent)
                        (activity as SignUpActivity).finish()
                    } else if (error == 11000) {
                        nickname_status_text.text = "이미 등록된 닉네임입니다."
                    } else if (error == 3) {
                        nickname_status_text.text = "10자 이내의 닉네임으로 입력해주세요."
                    }
                }
            })
        }
    }
}
