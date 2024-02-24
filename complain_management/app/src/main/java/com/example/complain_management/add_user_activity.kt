package com.example.complain_management

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.complain_management.databinding.AddUserBinding
import com.example.complain_management.databinding.HomePageBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ValueEventListener
import java.util.concurrent.TimeUnit

class add_user_activity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: AddUserBinding
    private lateinit var database: FirebaseDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AddUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val userId = intent.getStringExtra("userId")
        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()
        binding.scrollableContent.newUser.setOnClickListener {
            val name = binding.scrollableContent.enterName.text.toString()
            val age = binding.scrollableContent.enterAge.text.toString()
            val email = binding.scrollableContent.enterEMailId.text.toString()
            val number = binding.scrollableContent.enterMobileNumber.text.toString()
            val flat_no = binding.scrollableContent.enterFlatNo.text.toString()
            val building_no = binding.scrollableContent.enterBuildingNo.text.toString()
            val building_name = binding.scrollableContent.enterBuildingName.text.toString()
            if (name.isEmpty() || age.isEmpty() || email.isEmpty() || number.isEmpty() || flat_no.isEmpty() || building_no.isEmpty() || building_name.isEmpty()) {
                if (name.isEmpty()) {
                    Toast.makeText(this, "Name Required", Toast.LENGTH_SHORT).show()
                }
                if (age.isEmpty()) {
                    Toast.makeText(this, "Age Required", Toast.LENGTH_SHORT).show()
                }
                if (email.isEmpty()) {
                    Toast.makeText(this, "Email Required", Toast.LENGTH_SHORT).show()
                }
                if (number.isEmpty()) {
                    Toast.makeText(this, "Number Required", Toast.LENGTH_SHORT).show()
                }
                if (flat_no.isEmpty()) {
                    Toast.makeText(this, "Flat No. Required", Toast.LENGTH_SHORT).show()
                }
                if (building_no.isEmpty()) {
                    Toast.makeText(this, "Building No. Required", Toast.LENGTH_SHORT).show()
                }
                if (building_name.isEmpty()) {
                    Toast.makeText(this, "Building Name Required", Toast.LENGTH_SHORT).show()
                }
            } else {
                auth.createUserWithEmailAndPassword(email, email).addOnCompleteListener {
                    if (it.isSuccessful) {
                        if (number.isNotEmpty() && number.length == 10) {
                            val userData = UserData(
                                name = name,
                                age = age,
                                email = email,
                                flatNo = flat_no,
                                buildingNo = building_no,
                                buildingName = building_name,
                                adminId = userId,
                                number = number,
                            )

                            val verificationData = Verification(
                                type = "User",
                                verified = "No",
                                phone = number
                            )
                            val userRef =
                                database.reference.child("UserData").child(auth.currentUser!!.uid)
                            userRef.setValue(userData)

                            val verificationRef = database.reference.child("Verification")
                                .child(auth.currentUser!!.uid)
                            verificationRef.setValue(verificationData)
                            val adminRef = database.getReference("Admin").child(userId!!)
                            // Retrieve existing user list and add the current user's UID
                            adminRef.addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    val updatedUsers: MutableList<String> = mutableListOf()
                                    // Add existing users from admin data (if available)
                                    if (snapshot.exists() && snapshot.child("uid").exists()) {
                                        val existingUsers =
                                            snapshot.child("uid").children.map { it.getValue(String::class.java)!! }
                                        updatedUsers.addAll(existingUsers)
                                    }
                                    // Add the current user's UID
                                    updatedUsers.add(auth.currentUser!!.uid)
                                    adminRef.child("uid").setValue(updatedUsers)
                                        .addOnSuccessListener {
                                            Toast.makeText(
                                                this@add_user_activity,
                                                "User data saved successfully",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                        .addOnFailureListener { exception ->
                                            Toast.makeText(
                                                this@add_user_activity,
                                                "Failed to save user data: ${exception.message}",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }

                                }
                                override fun onCancelled(databaseError: DatabaseError) {
                                    // Handle errors
                                    Toast.makeText(
                                        this@add_user_activity,
                                        "Failed to retrieve existing user data: ${databaseError.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                            })
                            val intent = Intent(this@add_user_activity, add_user_activity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            intent.putExtra("userId", userId)
                            startActivity(intent)
                            finish()
                        }
                    }

                }
            }
            binding.scrollableContent.addUserSubmitButton.setOnClickListener {
                val intent = Intent(this, home_page_activity::class.java)
                intent.putExtra("userId", userId)
                startActivity(intent)
                finish()
            }
        }
    }

}


