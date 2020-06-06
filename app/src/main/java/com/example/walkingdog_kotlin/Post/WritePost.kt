package com.example.walkingdog_kotlin.Post

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.walkingdog_kotlin.Post.Model.PostModel
import com.example.walkingdog_kotlin.Post.Model.UploadImage
import com.example.walkingdog_kotlin.R
import kotlinx.android.synthetic.main.activity_write_post.*
import okio.Okio
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class WritePost : AppCompatActivity() {

    val REQUEST_CODE = 200

    var uploadImageList = arrayListOf<UploadImage>(
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_post)



        writePost_back_btn.setOnClickListener {
            finish()
        }
        complete_btn.setOnClickListener {

            var inputText = edit_comment.text

            finish()
        }
        uploadImageTv.setOnClickListener {
            openGalleryForImages()
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
            if (data?.getClipData() != null) {
                var count = data.clipData!!.itemCount

                for (i in 0..count - 1) {
                    var imageUri: Uri = data.clipData!!.getItemAt(i).uri
                    //     iv_image.setImageURI(imageUri) Here you can assign your Image URI to the ImageViews
                    uploadImageList.add(UploadImage(imageUri))

                }
                mAdapter.notifyDataSetChanged()

            } else if (data?.getData() != null) {   //만약 한 장의 이미지를 선택했을 경우

                var imageUri: Uri = data.data!!
                //   iv_image.setImageURI(imageUri) Here you can assign the picked image uri to your imageview
            }
        }


    }

    private fun openGalleryForImages() {        //기본 갤러리를 여는 함수

//        if (Build.VERSION.SDK_INT < 19) {       //버전이 19미만일 때 기본 갤리리를 연다.
//            var intent = Intent()
//            intent.type = "image/*"
//            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
//            intent.action = Intent.ACTION_GET_CONTENT
//            startActivityForResult(
//                Intent.createChooser(intent, "Choose Pictures")
//                , REQUEST_CODE
//            )
//        }
//        else {                                  //버전이 19이상일 기본 갤리리를 연다.
//            var intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
//            intent.type = "image/*"
//            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
//            intent.addCategory(Intent.CATEGORY_OPENABLE)
//            startActivityForResult(intent, REQUEST_CODE)
//        }

        var intent = Intent()
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(intent, "Choose Pictures")
            , REQUEST_CODE
        )

    }

}
