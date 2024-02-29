package com.example.complain_management

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.complain_management.databinding.ActivityRegisterComplaintBinding
import java.io.IOException

class Register_Complaint : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterComplaintBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_complaint)
//
//
//        val captureBtn = binding.requestComplaintScrollableContent.requestComplaintCaptureImage
//        val selectBtn = binding.requestComplaintScrollableContent.requestComplaintSelectImage
//
//        selectBtn.setOnClickListener(this::onClick)
//        captureBtn.setOnClickListener(View.OnClickListener { view: View? ->
//
//            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//            startActivityForResult(intent, 12)
//        })
//
//
//    }
//
//    fun getPermission() {
//        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(
//                this@Register_Complaint,
//                arrayOf<String>(Manifest.permission.CAMERA),
//                11
//            )
//        }
//    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<String?>,
//        grantResults: IntArray
//    ) {
//        if (requestCode == 11) {
//            if (grantResults.size > 0) {
//                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
//                    getPermission()
//                }
//            }
//        }
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        if (requestCode == 10) {
//            if (data != null) {
//                val uri = data.data
//                try {
//                    bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
//                    imageview.setImageBitmap(bitmap)
//                } catch (e: IOException) {
//                    e.printStackTrace()
//                }
//            }
//        } else if (requestCode == 12) {
//            assert(data != null)
//            imageview.setImageBitmap(bitmap)
//        }
//        super.onActivityResult(requestCode, resultCode, data)
//    }
//
//    private fun onClick(view: View) {
//        val intent = Intent()
//        intent.setAction(Intent.ACTION_GET_CONTENT)
//        intent.setType("image/*")
//        startActivityForResult(intent, 10)
    }
}