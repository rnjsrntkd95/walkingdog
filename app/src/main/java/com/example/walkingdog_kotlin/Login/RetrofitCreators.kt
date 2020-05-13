package com.example.walkingdog_kotlin.Login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.walkingdog_kotlin.Login.Model.LoginModel
import com.example.walkingdog_kotlin.Login.Model.SetNicknameModel
import com.example.walkingdog_kotlin.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitCreators(val context: Context) {

    val serverUrl = context.getString(R.string.server_url)

    fun LoginRetrofitCreator(email: String, password: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl(serverUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val getToken = retrofit.create(LoginRetrofit::class.java)

        getToken.getLoginToken(email, password).enqueue(object : Callback<LoginModel> {
            override fun onFailure(call: Call<LoginModel>, t: Throwable) {
                Log.d("DEBUG", " Login Retrofit failed!!")
                Log.d("DEBUG", t.message)
            }

            override fun onResponse(call: Call<LoginModel>, response: Response<LoginModel>) {
                Log.d("TAG", "Login Retrofit Success!!")
                val token = response.body()?.loginToken
                val nickname = response.body()?.nickname

                if (token != null) {
                    val pref = context.getSharedPreferences("pref", Context.MODE_PRIVATE)
                    val edit = pref.edit()
                    edit.putString("userToken", token)
                    edit.commit()

                    if (nickname != null) {
                        val intent = Intent(context, SignUpActivity::class.java)
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        context.startActivity(intent)
                        (context as Activity).finish()
                    } else {
                        // 닉네임이 없으면 닉네임 설정 Activity 전송
                        val intent = Intent(context, SignUpActivity::class.java)
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        context.startActivity(intent)
                        (context as Activity).finish()
                    }
                } else {
                    Toast.makeText(context, "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    fun SetNicknameRetrofit(nickname: String) {
        val pref = context.getSharedPreferences("pref", Context.MODE_PRIVATE)
        val userToken = pref.getString("userToken", "none")

        val retrofit = Retrofit.Builder()
            .baseUrl(serverUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val nicknameRetrofit = retrofit.create(LoginRetrofit::class.java)

        nicknameRetrofit.setNickname(userToken!!, nickname).enqueue(object: Callback<SetNicknameModel> {
            override fun onFailure(call: Call<SetNicknameModel>, t: Throwable) {
                Log.d("DEBUG", " Nickname Retrofit failed!!")
                Log.d("DEBUG", t.message)
            }

            override fun onResponse(call: Call<SetNicknameModel>, response: Response<SetNicknameModel>) {
                val error = response.body()?.error
                Log.d("TAG", error.toString())

                if (error == 0) {
                    Log.d("TAG", "닉네임 등록에 성공하였습니다.")
                } else if (error == 11000) {
                    v
                    Log.d("TAG", "이미 등록된 닉네임입니다.")

                } else {
                    Log.d("TAG", "올바르지 않은 닉네임입니다.")
                }
            }

        })
    }
}