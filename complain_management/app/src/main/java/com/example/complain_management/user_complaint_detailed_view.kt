package com.example.complain_management

import android.Manifest
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.complain_management.databinding.ActivityUserComplaintDetailedViewBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class user_complaint_detailed_view : AppCompatActivity() {
    private lateinit var binding:ActivityUserComplaintDetailedViewBinding
    private lateinit var database:FirebaseDatabase
    private lateinit var image: ImageView
    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference

    private fun checkAndRequestCameraPermission() {
        val cameraPermission = Manifest.permission.CAMERA
        val storagePermission = Manifest.permission.READ_EXTERNAL_STORAGE

        if (!checkPermission(cameraPermission) || !checkPermission(storagePermission)) {
            // Camera or storage permission not granted, request them
            requestCameraAndStoragePermissions()
        }
    }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraAndStoragePermissions() {
        val permissions = arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
        requestMultiplePermissionsLauncher.launch(permissions)
    }

    private val requestMultiplePermissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            // Check if both CAMERA and READ_EXTERNAL_STORAGE permissions are granted
            if (permissions[Manifest.permission.CAMERA] == true &&
                permissions[Manifest.permission.READ_EXTERNAL_STORAGE] == true
            ) {
                // Permissions granted, proceed with camera-related operations

            } else {
                // Handle the case when permissions are not granted
                // You might want to show a message to the user or request the permissions again
                Toast.makeText(this, "Camera and storage permissions are required.", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserComplaintDetailedViewBinding.inflate(layoutInflater)
        database=FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()
        storageRef=storage.getReferenceFromUrl("gs://resolve-hub-a8de8.appspot.com")
        val complainId=intent.getStringExtra("complainId")
        val userId=intent.getStringExtra("userId")
        Toast.makeText(this@user_complaint_detailed_view,complainId,Toast.LENGTH_SHORT).show()
        Toast.makeText(this@user_complaint_detailed_view,userId,Toast.LENGTH_SHORT).show()

        setContentView(binding.root)
        val userRef=database.reference.child("UserData").child(userId!!)
        val complaintUserRef=database.reference.child("ComplainUser").child(userId!!).child(complainId!!)
        val type=binding.userComplaintDetailedViewTypeText
        val subject=binding.userComplaintDetailedViewSubjectText
        val description=binding.userComplaintDetailedViewDescriptionText
        val complaintRegisteredTime=binding.userComplaintDetailedViewComplaintRegisteredText
        val complaintSolvedTime=binding.userComplaintDetailedViewComplaintResolvedDateText
        image=binding.userComplaintDetailedViewImageSubmittedImageView
        val complaintSolvedSubmitButton=binding.userComplaintDetailedViewComplaintResolvedSuccessfullyButton
        val feedbackButton=binding.userComplaintDetailedViewFeedbackButton
        val previousPageButton=binding.userComplaintDetailedViewBackToUserComplaintListButton
        checkAndRequestCameraPermission()
        complaintUserRef.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val complainData=snapshot.getValue(UserComplain::class.java)
                type.text= complainData?.ComplainType.toString()

                subject.text=complainData?.ComplainSubject.toString()
                description.text=complainData?.ComplainDescription.toString()
                complaintRegisteredTime.text=complainData?.complain_time.toString()
                complaintSolvedTime.text=complainData?.completed_solved_time.toString()
                val imageUrl= storageRef.child("$complainId.jpg")
                Toast.makeText(this@user_complaint_detailed_view,imageUrl.toString(),Toast.LENGTH_SHORT).show()
                imageUrl.downloadUrl.addOnSuccessListener { uri ->
                    // Load the image into the ImageView using an image loading library like Glide or Picasso
                    Picasso.get().load(uri).into(image)
                }.addOnFailureListener { exception ->
                    // Handle any errors that occur during the download
                    Log.e(TAG, "Error downloading image: ${exception.message}")
                }
                val complainSolved=complainData?.Complainsolved.toString()
                complaintSolvedSubmitButton.visibility=View.GONE
                if(complainSolved=="No"){
                    complaintSolvedSubmitButton.visibility= View.VISIBLE

                    complaintSolvedSubmitButton.setOnClickListener {
                        val currentTimeMillis = System.currentTimeMillis()

                        // Define the date format
                        val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())

                        // Format the timestamp into a string
                        val formattedDate = dateFormat.format(Date(currentTimeMillis))
                        val solved="Yes"

                        val updates=HashMap<String,Any>()
                        updates["complainsolved"]=solved
                        updates["completed_solved_time"]=formattedDate
                        complaintUserRef.updateChildren(updates)
                            .addOnCompleteListener {
                                Toast.makeText(this@user_complaint_detailed_view, "Values updated successfully", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener {
                                // Handle the update failure
                                Toast.makeText(this@user_complaint_detailed_view, "Failed to update values", Toast.LENGTH_SHORT).show()
                            }
                        userRef.addValueEventListener(object:ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val userData=snapshot.getValue(UserData::class.java)
                                val currentPendingCount = (userData?.ComplainsPending ) ?: 0
                                val newPendingCount = currentPendingCount - 1
                                val currentCompletedCount = (userData?.complainResolved ) ?: 0
                                val newCompletedCount = currentCompletedCount + 1

                                val updatesUser = HashMap<String, Any>()
                                updatesUser["ComplainsPending"] = newPendingCount
                                updatesUser["complainResolved"]=newCompletedCount

                                userRef.updateChildren(updates)
                                    .addOnCompleteListener {
                                        Toast.makeText(this@user_complaint_detailed_view,it.toString(),Toast.LENGTH_SHORT).show()
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(this@user_complaint_detailed_view,it.message,Toast.LENGTH_SHORT).show()
                                    }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }
                        })

                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@user_complaint_detailed_view, error.message, Toast.LENGTH_SHORT).show()
            }
        })



    }

}