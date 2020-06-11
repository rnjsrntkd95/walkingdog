package com.example.walkingdog_kotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.walkingdog_kotlin.Login.LoginActivity
import com.example.walkingdog_kotlin.Login.SessionCallback
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_settings.*

class Settings : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    //google client
    private lateinit var googleSignInClient: GoogleSignInClient

    private val RC_SIGN_IN = 99

//    private var callback: SessionCallback =
//        SessionCallback(this)

    private var auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)


        //로그아웃
        logout.setOnClickListener {
            var intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            auth.signOut()
            Toast.makeText(this, "로그아웃 되었습니다.", Toast.LENGTH_LONG).show()
        }

        //회원탈퇴
//        sign_out.setOnClickListener {
//            var intent = Intent(this, LoginActivity::class.java)
//            startActivity(intent)
//            auth.revokeAccess()
//            Toast.makeText(this,"회원탈퇴가 완료되었습니다.",Toast.LENGTH_LONG).show()
//        }
//    }

          fun signOut() { // 로그아웃
            // Firebase sign out
            firebaseAuth.signOut()

            // Google sign out
            googleSignInClient.signOut().addOnCompleteListener(this) {
                //updateUI(null)
            }
        }

//        private fun revokeAccess() { //회원탈퇴
//            // Firebase sign out
//            firebaseAuth.signOut()
//
//            googleSignInClient.revokeAccess().addOnCompleteListener(this) {
//            }
//        }

    }
}
