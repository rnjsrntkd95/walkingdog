package com.example.walkingdog_kotlin.Post

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Gallery
import android.widget.Toast
import com.example.walkingdog_kotlin.R
import kotlinx.android.synthetic.main.activity_add_pet.*
import kotlinx.android.synthetic.main.activity_write_post.*
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
