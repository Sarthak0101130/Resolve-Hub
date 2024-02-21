package com.example.complain_management

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.complain_management.databinding.ActivitySignInBinding
import com.google.android.play.integrity.internal.l
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SignInActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textView.setOnClickListener {
            val intent = Intent(this@SignInActivity, Admin::class.java)
            startActivity(intent)
            finish()
        }
            firebaseAuth = FirebaseAuth.getInstance()



            binding.button.setOnClickListener {
                val email = binding.emailEt.text.toString()
                val pass = binding.passET.text.toString()

                if (email.isNotEmpty() && pass.isNotEmpty()) {

                    firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                        if (it.isSuccessful) {
                            val firebaseUser = FirebaseAuth.getInstance().currentUser
                            val userId = firebaseUser?.uid
                            val adminReference =
                                FirebaseDatabase.getInstance().getReference("admin")
                                    .child(userId.toString())

                            adminReference.addListenerForSingleValueEvent(object :
                                ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        val adminData = dataSnapshot.getValue(admindata::class.java)
                                        Toast.makeText(
                                            this@SignInActivity,
                                            "userId.toString()",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        if (adminData != null) {
                                            runOnUiThread {
                                                Toast.makeText(this@SignInActivity, "Admin Type: ${adminData.type}", Toast.LENGTH_SHORT).show()
                                            }
                                            when (adminData.type) {
                                                "user" -> {
                                                    // Handle the case where the user is a regular user
                                                    Toast.makeText(
                                                        this@SignInActivity,
                                                        "user",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    val intent = Intent(
                                                        this@SignInActivity,
                                                        user_home_page_activity::class.java
                                                    )
                                                    startActivity(intent)
                                                    finish()
                                                }

                                                "admin" -> {
                                                    // Handle the case where the user is an admin
                                                    Toast.makeText(
                                                        this@SignInActivity,
                                                        "Admin",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    val intent = Intent(
                                                        this@SignInActivity,
                                                        home_page_activity::class.java
                                                    )
                                                    intent.putExtra("userId",userId)
                                                    startActivity(intent)
                                                    finish()
                                                }

                                                else -> {
                                                    // Handle unexpected 'type' value
                                                    Toast.makeText(
                                                        this@SignInActivity,
                                                        "Unexpected user type",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            }
                                        } else {
                                            // Handle the case where 'adminData' is null
                                            Toast.makeText(
                                                this@SignInActivity,
                                                "Admin data is null",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                    else {
                                        // Handle the case where the user node doesn't exist in the database
                                        Toast.makeText(this@SignInActivity, "hi", Toast.LENGTH_SHORT).show()
                                    }
                                }
                                override fun onCancelled(error: DatabaseError) {
                                    // Handle onCancelled. This method is called if the database operation is canceled for some reason.
                                    Toast.makeText(this@SignInActivity, "Database Error: ${error.message}", Toast.LENGTH_SHORT).show()
                                }
                                })
                        }
                    }
                }
                else {
                    Toast.makeText(this, "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
