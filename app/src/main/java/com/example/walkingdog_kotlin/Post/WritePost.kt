package com.example.walkingdog_kotlin.Post

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.example.walkingdog_kotlin.Post.Model.PostModel
import com.example.walkingdog_kotlin.R
import kotlinx.android.synthetic.main.activity_write_post.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class WritePost : AppCompatActivity() {

    val Gallery = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_post)

        var comment = edit_comment.text

        writePost_back_btn.setOnClickListener {
            finish()
        }

        writePost_image.setOnClickListener {
            loadImage()
        }

        complete_btn.setOnClickListener {
            Toast.makeText(this, comment, Toast.LENGTH_SHORT).show()
            val pref = PreferenceManager.getDefaultSharedPreferences(this)
            val userToken = pref.getString("token", "5ebac6bd59e3d32080d6d941")
            val postCreateRetrofit = PostRetrofitCreators(this).PostRetrofitCreator()
            val image = loadImage() // 아직 이미지 받아온게 아
            postCreateRetrofit.createPost(comment.toString(), userToken, image).enqueue(object :
                Callback<PostModel> {
                override fun onFailure(call: Call<PostModel>, t: Throwable) {
                    Log.d("DEBUG", "Retrofit Failed!!")
                    Log.d("DEBUG", t.message)
                }
                override fun onResponse(
                    call: Call<PostModel>,
                    response: Response<PostModel>
                ) {
                    Log.d("DEBUG", "Retrofit Success!!")
                }
            })
        }
    }

    private fun loadImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(Intent.createChooser(intent, "Load Picture"), Gallery)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == Gallery) {
            if(resultCode == RESULT_OK) {
                var dataUri = data?.data
                try {
                    var bitmap : Bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, dataUri)
                    writePost_image.setImageBitmap(bitmap)

                } catch (e:Exception) {
                    Toast.makeText(this, "$e", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            //somthing wrong
        }
    }
}
