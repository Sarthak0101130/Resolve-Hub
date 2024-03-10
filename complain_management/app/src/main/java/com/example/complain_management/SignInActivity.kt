package com.example.complain_management


import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.complain_management.databinding.ActivitySignInBinding
import com.google.android.play.integrity.internal.l
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.concurrent.TimeUnit

class SignInActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signInAdminButton.setOnClickListener {
            val intent = Intent(this@SignInActivity, Admin::class.java)
            startActivity(intent)
            finish()
        }
            firebaseAuth = FirebaseAuth.getInstance()



            binding.signInButton.setOnClickListener {
                val email = binding.signInEmailText.text.toString()
                val pass = binding.signInPasswordText.text.toString()

                if (email.isNotEmpty() && pass.isNotEmpty()) {

                    firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                        if (it.isSuccessful) {
                            val firebaseUser = FirebaseAuth.getInstance().currentUser
                            val userId = firebaseUser?.uid
                            val typeRef =
                                FirebaseDatabase.getInstance().getReference("Verification")
                                    .child(userId.toString())

                            typeRef.addListenerForSingleValueEvent(object :
                                ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        val verificationData = dataSnapshot.getValue(Verification::class.java)
                                        Toast.makeText(
                                            this@SignInActivity,
                                            "userId.toString()",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        if (verificationData != null) {
                                            runOnUiThread {
                                                Toast.makeText(this@SignInActivity, "Admin Type: ${verificationData.type}", Toast.LENGTH_SHORT).show()
                                            }
                                            when (verificationData.type) {
                                                "User" -> {
                                                    // Handle the case where the user is a regular user
                                                    Toast.makeText(
                                                        this@SignInActivity,
                                                        "user",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    when(verificationData.verified){
                                                        "No"->{
                                                            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                                                "+91"+verificationData.phone,
                                                                2,
                                                                TimeUnit.MINUTES,
                                                                this@SignInActivity,
                                                                object: PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                                                                    override fun onVerificationCompleted(
                                                                        phoneAuthCredential: PhoneAuthCredential
                                                                    ) {
                                                                        signInWithPhoneAuthCredential( phoneAuthCredential)
                                                                    }

                                                                    override fun onVerificationFailed(
                                                                        e: FirebaseException
                                                                    ) {
                                                                        Toast.makeText(this@SignInActivity,e.message,Toast.LENGTH_SHORT).show()
                                                                    }

                                                                    override fun onCodeSent(
                                                                        p0: String,
                                                                        p1: PhoneAuthProvider.ForceResendingToken
                                                                    ) {
                                                                        val intent=Intent(
                                                                            this@SignInActivity,
                                                                            OtpVerification::class.java
                                                                        )
                                                                        intent.putExtra("userId",userId)
                                                                        intent.putExtra("Token",p0)
                                                                        intent.putExtra("Phone","+91"+verificationData.phone)
                                                                        intent.putExtra("Type","User")
                                                                        startActivity(intent)
                                                                        finish()
                                                                    }
                                                                }
                                                            )

                                                        }
                                                        "Yes"->{
                                                            val intent = Intent(
                                                                this@SignInActivity,
                                                                user_home_page_activity::class.java
                                                            )
                                                            intent.putExtra("userId",userId)
                                                            startActivity(intent)
                                                            finish()
                                                        }
                                                        else->{
                                                            Toast.makeText(
                                                                this@SignInActivity,
                                                                "Unexpected verified type",
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                        }
                                                    }

                                                }

                                                "Admin" -> {
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
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential){
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) {task->
                if(task.isSuccessful){
                    Toast.makeText(this@SignInActivity, "Verification successful", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@SignInActivity, "Verification failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }