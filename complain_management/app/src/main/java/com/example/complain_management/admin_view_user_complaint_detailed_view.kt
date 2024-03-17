package com.example.complain_management

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.complain_management.databinding.ActivityAdminViewUserComplaintDetailedViewBinding
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

class admin_view_user_complaint_detailed_view : AppCompatActivity() {
    private lateinit var binding: ActivityAdminViewUserComplaintDetailedViewBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var image: ImageView
    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference
    private lateinit var imageUrl:StorageReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminViewUserComplaintDetailedViewBinding.inflate(layoutInflater)
        database=FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()
        storageRef=storage.getReferenceFromUrl("gs://resolve-hub-a8de8.appspot.com")
        val complainId=intent.getStringExtra("complainId")
        val userId=intent.getStringExtra("userId")
        val adminId=intent.getStringExtra("adminId")
        Toast.makeText(this@admin_view_user_complaint_detailed_view,complainId,Toast.LENGTH_SHORT).show()
        Toast.makeText(this@admin_view_user_complaint_detailed_view,userId,Toast.LENGTH_SHORT).show()
        setContentView(binding.root)
        val userRef=database.reference.child("UserData").child(userId!!)
        val complaintUserRef=database.reference.child("ComplainUser").child(userId!!).child(complainId!!)
        val name=binding.adminViewUserComplaintDetailedViewNameText
        val number=binding.adminViewUserComplaintDetailedViewNumberText
        val type=binding.adminViewUserComplaintDetailedViewTypeText
        val subject=binding.adminViewUserComplaintDetailedViewSubjectText
        val description=binding.adminViewUserComplaintDetailedViewDescriptionText
        val complaintRegisteredTime=binding.adminViewUserComplaintDetailedViewComplaintRegisteredText
        val complaintSolvedTime=binding.adminViewUserComplaintDetailedViewComplaintResolvedDateText
        image=binding.adminViewUserComplaintDetailedViewImageSubmittedImageView
        val complainSolvedSubmitButton=binding.adminViewUserComplaintDetailedViewComplaintResolvedSuccessfullyButton
        val seeFeedbackButton=binding.adminViewUserComplaintDetailedViewFeedbackButton
        complainSolvedSubmitButton.visibility= View.GONE
        complaintUserRef.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val complainData=snapshot.getValue(UserComplain::class.java)
                type.text= complainData?.ComplainType.toString()

                subject.text=complainData?.ComplainSubject.toString()
                description.text=complainData?.ComplainDescription.toString()
                complaintRegisteredTime.text=complainData?.complain_time.toString()
                complaintSolvedTime.text=complainData?.completed_solved_time.toString()
                imageUrl= storageRef.child("$complainId.jpg")
                Toast.makeText(this@admin_view_user_complaint_detailed_view,imageUrl.toString(),Toast.LENGTH_SHORT).show()
                imageUrl.downloadUrl.addOnSuccessListener { uri ->
                    // Load the image into the ImageView using an image loading library like Glide or Picasso
                    Picasso.get().load(uri).into(image)
                }.addOnFailureListener { exception ->
                    // Handle any errors that occur during the download
                    Log.e(ContentValues.TAG, "Error downloading image: ${exception.message}")
                }
                val complainSolved=complainData?.Complainsolved.toString()

                userRef.addValueEventListener(object:ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val userData=snapshot.getValue(UserData::class.java)
                        name.text=userData?.name.toString()
                        number.text=userData?.number.toString()
                        if(complainSolved=="No"){
                            complainSolvedSubmitButton.visibility=View.VISIBLE
                            complainSolvedSubmitButton.setOnClickListener {
                                val currentTimeMillis = System.currentTimeMillis()

                                // Define the date format
                                val dateFormat =
                                    SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())

                                // Format the timestamp into a string
                                val formattedDate = dateFormat.format(Date(currentTimeMillis))
                                val solved = "Yes"

                                val updates = HashMap<String, Any>()
                                updates["complainsolved"] = solved
                                updates["completed_solved_time"] = formattedDate
                                database.reference.child("ComplainUser").child(userId!!).child(complainId!!).updateChildren(updates)
                                    .addOnCompleteListener {
                                        Toast.makeText(
                                            this@admin_view_user_complaint_detailed_view,
                                            "Values updated successfully",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    .addOnFailureListener {
                                        // Handle the update failure
                                        Toast.makeText(
                                            this@admin_view_user_complaint_detailed_view,
                                            "Failed to update values",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                val currentPendingCount = (userData?.ComplainsPending ) ?: 0
                                val newPendingCount = currentPendingCount - 1
                                val currentCompletedCount = (userData?.complainResolved ) ?: 0
                                val newCompletedCount = currentCompletedCount + 1

                                val updatesUser = HashMap<String, Any>()
                                updatesUser["complainsPending"] = newPendingCount
                                updatesUser["complainResolved"]=newCompletedCount

                                userRef.updateChildren(updatesUser)
                                    .addOnCompleteListener {
                                        Toast.makeText(this@admin_view_user_complaint_detailed_view,"$newCompletedCount  $newPendingCount",Toast.LENGTH_SHORT).show()

                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(this@admin_view_user_complaint_detailed_view,it.message,Toast.LENGTH_SHORT).show()
                                    }
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}