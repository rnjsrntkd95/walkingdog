package com.example.walkingdog_kotlin.Post

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.loader.content.CursorLoader
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.walkingdog_kotlin.MainActivity
import com.example.walkingdog_kotlin.Post.Model.CreatePostModel
import com.example.walkingdog_kotlin.Post.Model.PostModel
import com.example.walkingdog_kotlin.Post.Model.UploadImage
import com.example.walkingdog_kotlin.R
import com.google.android.gms.common.util.IOUtils
import kotlinx.android.synthetic.main.activity_write_post.*
import net.daum.mf.map.common.io.IoUtils
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okio.Okio
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class WritePost : AppCompatActivity() {

    val REQUEST_CODE = 200

    var uploadImageList = arrayListOf<UploadImage>()
    var images = ArrayList<MultipartBody.Part>()
    var walkingId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_post)

//        val extra = intent.extras
//        if (extra != null) {
//            val data = extra.getString("walkingId")
//            if (data != null) {
//                walkingId = data
//            } else {
//                val intent = Intent(this, MainActivity::class.java)
//                startActivity(intent)
//                finish()
//            }
//        } else {
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
//            finish()
//        }

        writePost_back_btn.setOnClickListener {
            finish()
        }
        complete_btn.setOnClickListener {
            var comment = RequestBody.create(MediaType.parse("text/plain"), edit_comment.text.toString())     // 새로운 피드 내용
            val pref = getSharedPreferences("pref", Context.MODE_PRIVATE)
            val userToken = pref.getString("userToken", "")

            val createPostRetrofit = PostRetrofitCreators(this).PostRetrofitCreator()
            createPostRetrofit.createPost(userToken!!, comment, images).enqueue(object : Callback<CreatePostModel> {
                override fun onFailure(call: Call<CreatePostModel>, t: Throwable) {
                    Log.d("DEBUG", " Create Post Retrofit failed!!")
                    Log.d("DEBUG", t.message)
                }

                override fun onResponse(call: Call<CreatePostModel>, response: Response<CreatePostModel>) {
                    val error = response.body()?.error

                    if (error == null) {
                        var intent = Intent(this@WritePost, MainActivity::class.java)
                        startActivity(intent)
                    }
                }
            })

            finish()
        }
        uploadImageTv.setOnClickListener {
            setupPermission()
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val mAdapter = uploadImageRvAdapter(this, uploadImageList)
        image_recycler_view.adapter = mAdapter

        val lm = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        image_recycler_view.layoutManager = lm
        image_recycler_view.setHasFixedSize(true)


        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {

            // 만약 여러 장의 이미지를 선택했을 경우
            if (data?.clipData != null) {
                var count = data.clipData!!.itemCount

                for (i in 0..count - 1) {
                    val imageUri: Uri = data.clipData!!.getItemAt(i).uri
                    //     iv_image.setImageURI(imageUri) Here you can assign your Image URI to the ImageViews
                    uploadImageList.add(UploadImage(imageUri))
                    val cursor: Cursor = contentResolver.query(Uri.parse(imageUri.toString()), null, null, null, null)
                    cursor.moveToNext()
                    val absolutePath = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA))
                    val file = File(absolutePath)
                    val surveyBody = RequestBody.create(MediaType.parse("image/*"), file)
                    images.add(MultipartBody.Part.createFormData("images", file.name, surveyBody))
                }
                mAdapter.notifyDataSetChanged()

            } else if (data?.data != null) {   //만약 한 장의 이미지를 선택했을 경우
                var imageUri: Uri = data.data!!
                val cursor: Cursor = contentResolver.query(Uri.parse(imageUri.toString()), null, null, null, null)
                cursor.moveToNext()
                val absolutePath = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA))


                //   iv_image.setImageURI(imageUri) Here you can assign the picked image uri to your imageview
                uploadImageList.add(UploadImage(imageUri))
                val file = File(absolutePath)
                val surveyBody = RequestBody.create(MediaType.parse("image/*"), file)
                images.add(MultipartBody.Part.createFormData("images", file.name, surveyBody))

                mAdapter.notifyDataSetChanged()
            }
        }


    }

    private fun openGalleryForImages() {        //기본 갤러리를 여는 함수

        var intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_PICK
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.type = MediaStore.Images.Media.CONTENT_TYPE
        intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        startActivityForResult(
            Intent.createChooser(intent, "Choose Pictures")
            , REQUEST_CODE
        )

    }

    private fun setupPermission() {
        val permission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1000)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode) {
            1000 -> {
                if (grantResults.isNotEmpty() && grantResults[0] != PackageManager.PERMISSION_GRANTED ) {
                    Toast.makeText(this, "저장 공간 권한을 허용해주세요!", Toast.LENGTH_LONG).show()
                } else {
                    openGalleryForImages()
                }
            }
        }

    }




}
