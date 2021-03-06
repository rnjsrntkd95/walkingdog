package com.example.walkingdog_kotlin.Login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.walkingdog_kotlin.Login.Model.LoginModel
import com.example.walkingdog_kotlin.MainActivity
import com.example.walkingdog_kotlin.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.kakao.auth.Session
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    //firebase Auth
    private lateinit var firebaseAuth: FirebaseAuth
    //google client
    private lateinit var googleSignInClient: GoogleSignInClient
    //private const val TAG = "GoogleActivity"
    private val RC_SIGN_IN = 99
    //Kakao Session Callback
    private var callback: MySessionCallback =
        MySessionCallback(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        this.window.statusBarColor = (ContextCompat.getColor(this,
            R.color.green1
        ))

        val pref = getSharedPreferences("pref", Context.MODE_PRIVATE)
        // 토큰 유무에 따른 화면 이동
        val userToken = pref.getString("userToken", "")
        if (userToken != null && userToken != "") {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        // 산책 체크 리스트 초기화
        val userCheckList = pref.getString("checkList", "0")
        if (userCheckList == "0" || userCheckList == null) {
            val edit = pref.edit()
            edit.putString("checkList", "//목줄//물//배변 봉투")
            edit.apply()
        }


//        btn_googleSignIn.setOnClickListener (this) // 구글 로그인 버튼
        btn_googleSignIn.setOnClickListener { signIn() }

        btn_kakaoCustomSignIn.setOnClickListener { btn_kakaoSignIn.performClick() }

        //Google 로그인 옵션 구성. requestIdToken 및 Email 요청
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.google_oauth_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        //firebase auth 객체
        firebaseAuth = FirebaseAuth.getInstance()

        //Kakao Login
        Session.getCurrentSession().addCallback(callback)
    }


    // onStart. 유저가 앱에 이미 구글 로그인을 했는지 확인
    public override fun onStart() {
        super.onStart()
        val account = GoogleSignIn.getLastSignedInAccount(this)
        if (account !== null) { // 이미 로그인 되어있을시 바로 메인 액티비티로 이동
            signOut()
        }
    } //onStart End

    // onActivityResult
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            Log.i("Log", "session get current session")
            return
        }
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)

            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("LoginActivity", "Google sign in failed", e)
            }
        }
    } // onActivityResult End

    // firebaseAuthWithGoogle
    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d("LoginActivity", "firebaseAuthWithGoogle:" + acct.id!!)

        //Google SignInAccount 객체에서 ID 토큰을 가져와서 Firebase Auth로 교환하고 Firebase에 인증
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.w("LoginActivity", "firebaseAuthWithGoogle 성공", task.exception)
                    val user = firebaseAuth?.currentUser
                    val email = user?.email.toString()
                    val password = user?.uid.toString()
                    val retrofit = RetrofitCreators(this)
                    val getToken = retrofit.LoginRetrofitCreator(email, password)

                    getToken.getLoginToken(email, password).enqueue(object : Callback<LoginModel> {
                        override fun onFailure(call: Call<LoginModel>, t: Throwable) {
                            Log.d("DEBUG", " Login Retrofit failed!!")
                            Log.d("DEBUG", t.message)
                            Toast.makeText(this@LoginActivity, "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                        }
                        override fun onResponse(call: Call<LoginModel>, response: Response<LoginModel>) {
                            Log.d("TAG", "Login Retrofit Success!!")
                            val token = response.body()?.loginToken
                            val nickname = response.body()?.nickname
                            val error = response.body()?.error

                            Log.d("토큰", token + nickname + error)

                            if (token != null) {
                                val pref = getSharedPreferences("pref", Context.MODE_PRIVATE)
                                val edit = pref.edit()
                                edit.putString("userToken", token)
                                edit.apply()

                                if (nickname != null && nickname != "주인님") {
                                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                                    startActivity(intent)
                                    finish()
                                } else {
                                    // 닉네임이 없으면 닉네임 설정 Activity 전송
                                    val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                                    startActivity(intent)
                                    finish()
                                }
                            } else {
                                Toast.makeText(this@LoginActivity, "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    })
                } else {
                    Log.w("LoginActivity", "firebaseAuthWithGoogle 실패", task.exception)
//                    Snackbar.make(login_layout, "로그인에 실패하였습니다.", Snackbar.LENGTH_SHORT).show()
                    Toast.makeText(getApplicationContext(), "로그인에 실패하였습니다.", Toast.LENGTH_LONG).show()
                }
            }
    }// firebaseAuthWithGoogle END


//    // toMainActivity
//    fun toMainActivity(user: FirebaseUser?) {
//        if (user != null) { // MainActivity 로 이동
//            startActivity(Intent(this, MainActivity::class.java))
//            finish()
//        }
//    } // toMainActivity End

    // signIn
    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    // signIn End

    override fun onClick(p0: View?) {
    }


    private fun signOut() { // 로그아웃
        // Firebase sign out
        firebaseAuth.signOut()

        // Google sign out
        googleSignInClient.signOut().addOnCompleteListener(this) {
            //updateUI(null)
        }
    }

    private fun revokeAccess() { //회원탈퇴
        // Firebase sign out
        firebaseAuth.signOut()
        googleSignInClient.revokeAccess().addOnCompleteListener(this) {
        }
    }

    //Kakao destory 메모리 누수 방지?
    override fun onDestroy() {
        super.onDestroy()
        Session.getCurrentSession().removeCallback(callback)
    }
}