package com.example.walkingdog_kotlin.Walking

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.hardware.Camera
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.Surface
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.walkingdog_kotlin.R
import kotlinx.android.synthetic.main.activity_camera.*
import java.io.File
import java.io.FileOutputStream

class Camera : AppCompatActivity() {

    var permission_list = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    var camera:Camera? = null
    var dirPath:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        //상태바 색을 그레이로 변경
        window.statusBarColor = (ContextCompat.getColor(applicationContext,
            R.color.green1
        ))

        //상태바 아이콘 색 플레그(흰색->검은색)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        //버전이 마시멜로우 이상 버전이라면 퍼미션 허용 여부 물어보기
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permission_list, 0)
        } else {
            init()
        }
    }

    //권한이 거부되었다면 종료하는 메소드
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        for (result in grantResults) {
            if(result == PackageManager.PERMISSION_DENIED) {
                return
            }
        }
        init()
    }

    fun init() {
        var tempPath = Environment.getExternalStorageDirectory().absolutePath
        //dirPath = "${tempPath}/Android/data/${packageName}"
        dirPath = "${tempPath}/DCIM/Camera"


        var file = File(dirPath)
        if(file.exists() == false) {
            file.mkdir()
        }

        camera = Camera.open()

        var degree = 0

        //화면 로테이션 보정
        when(windowManager.defaultDisplay.rotation) {
            Surface.ROTATION_0 -> {
                degree = 90
            }
            Surface.ROTATION_90 -> {
                degree = 0
            }
            Surface.ROTATION_180 -> {
                degree = 270
            }
            Surface.ROTATION_270 -> {
                degree = 180
            }
        }

        camera?.setDisplayOrientation(degree)
        camera?.setPreviewDisplay(surfaceView.holder)
        camera?.startPreview()


        close_btn.setOnClickListener {
            finish()
        }

        takePicture_btn.setOnClickListener {
            var callback1 = Callback1()
            camera?.takePicture(null, null, callback1)

        }
    }


    inner  class Callback1 : Camera.PictureCallback {
        override fun onPictureTaken(data: ByteArray?, camera: Camera?) {

            var bitmap = BitmapFactory.decodeByteArray(data, 0, data?.size!!)

            val matrix = Matrix()
            matrix.postRotate(90f)
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true)


            var filePath = "${dirPath}/temp_${System.currentTimeMillis()}.jpg"
            var file = File(filePath)
            var fos = FileOutputStream(file)

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.flush()
            fos.close()

            /*갤러리를 notify하는 코드*/
            val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            val f = File(filePath)
            val contentUri = Uri.fromFile(f)
            mediaScanIntent.data = contentUri
            this@Camera.sendBroadcast(mediaScanIntent)
            /*-----------------*/

        }
    }
}
