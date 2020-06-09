package com.example.walkingdog_kotlin.Login

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.walkingdog_kotlin.*
import com.example.walkingdog_kotlin.Animal.AddPet
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment : Fragment() {

    private val OPEN_GALLERY=0
    var my_name: String? = null

    private lateinit var firebaseAuth: FirebaseAuth

    //google client
    private lateinit var googleSignInClient: GoogleSignInClient

    private val RC_SIGN_IN = 99

//    private var callback: SessionCallback =
//        SessionCallback(this)

    var addpet_list= arrayListOf<ProfileItem>(

        ProfileItem("dog00"),
        ProfileItem("dog01")
    )



    private var auth = FirebaseAuth.getInstance()


        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            return inflater.inflate(R.layout.fragment_profile, container, false)


        }


        override fun onActivityCreated(savedInstanceState: Bundle?) {


            val addpetAdapter=Addpet_RVAdapter(context!!, addpet_list)

            add_recyclerView.adapter=addpetAdapter
            val lm = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            add_recyclerView.layoutManager = lm
            add_recyclerView.setHasFixedSize(true)


            super.onActivityCreated(savedInstanceState)

            var sum_kcal=view?.findViewById<TextView>(R.id.profile_name)

            my_photos.setOnClickListener {
                val intent =Intent(context,MyPhotoActivity::class.java)
                startActivity(intent)
            }
            statics.setOnClickListener {
                val intent =Intent(context,Statics::class.java)
                startActivity(intent)
            }

//            settings.setOnClickListener {
//                val intent =Intent(context,Settings::class.java)
//                startActivity(intent)
//            }
            myprofile_img_btn.setOnClickListener(){

                loadImage()
            }

            logout_btn.setOnClickListener {
                var intent = Intent(context, LoginActivity::class.java)
                startActivity(intent)
                auth.signOut()
                Toast.makeText(context, "로그아웃 되었습니다.", Toast.LENGTH_LONG).show()
            }

            add_pet_btn.setOnClickListener{
                var intent = Intent(context, ProfileAddPetActivity::class.java)
                startActivity(intent)
            }


}
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode==OPEN_GALLERY){
            if(requestCode==RESULT_OK){

                var currentImageUrl:Uri?=data?.data

                try{
                    val bitmap=MediaStore.Images.Media.getBitmap(context?.contentResolver,currentImageUrl)
                    myprofile_img_btn.setImageBitmap(bitmap)
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }
        } else{
            Log.d("ActivityResult","something wrong")
        }
    }

    private fun loadImage(){
        val intent=Intent()
        intent.type="image/*"
        intent.action=Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent,"Load Picture"),OPEN_GALLERY)
    }

    private fun signOut() { // 로그아웃
        // Firebase sign out
        firebaseAuth.signOut()

        // Google sign out
  //      googleSignInClient.signOut().addOnCompleteListener(context) {
            //updateUI(null)
 //       }
    }

    }


