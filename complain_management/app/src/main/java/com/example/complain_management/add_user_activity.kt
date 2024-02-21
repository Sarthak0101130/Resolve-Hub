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
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import java.util.concurrent.TimeUnit

class add_user_activity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding:AddUserBinding
    private lateinit var database:FirebaseDatabase
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
                if (number.isNotEmpty() && number.length == 10) {
                    generateOTP("+91$number",name, age, email, flat_no, building_no, building_name,userId.toString())
                    val intent=Intent(this,add_user_activity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.putExtra("userId", userId)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Invalid Phone Number", Toast.LENGTH_SHORT).show()
                }
            }
        }
        binding.scrollableContent.addUserSubmitButton.setOnClickListener {
            val intent=Intent(this,home_page_activity::class.java)
            intent.putExtra("userId",userId)
            startActivity(intent)
            finish()
        }
    }
        fun generateOTP(phoneNUmber:String,name:String, age:String, email:String, flat_no:String, building_no:String, building_name:String,userId:String){
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNUmber,
                2,
                TimeUnit.DAYS,
                this,
                object:PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                        // This method won't be called in your case since we're not performing authentication here
                    }
                    override fun onVerificationFailed(e: FirebaseException) {
                        // Handle error
                        Toast.makeText(
                            this@add_user_activity,
                            "Verification failed: ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onCodeSent(
                        verificationId: String,
                        token: PhoneAuthProvider.ForceResendingToken
                    ) {
                        saveUserDataToDatabase(name, age, email, flat_no, building_no, building_name,userId,verificationId)
                        // Save the verification ID for later use
                        val databaseReference = FirebaseDatabase.getInstance().reference.child("verificationId").child(phoneNUmber)
                        val verificationMap = mapOf(
                            "verificationId" to verificationId,
                            "timestamp" to ServerValue.TIMESTAMP
                        )
                        databaseReference.setValue(verificationMap)
                    }
                }
            )
        }

        private fun saveUserDataToDatabase(
            name: String,
            age: String,
            email: String,
            flatNo: String,
            buildingNo: String,
            buildingName: String,
            adminid:String,
            verificationId: String
        ) {
            // Save the other input data to the database
            val userRef = database.reference.child("admin").child(verificationId)
            userRef.child("name").setValue(name)
            userRef.child("age").setValue(age)
            userRef.child("email").setValue(email)
            userRef.child("flatNo").setValue(flatNo)
            userRef.child("buildingNo").setValue(buildingNo)
            userRef.child("buildingName").setValue(buildingName)
            userRef.child("type").setValue("user")
            userRef.child("adminId").setValue(adminid)
            Toast.makeText(this, "User data saved successfully", Toast.LENGTH_SHORT).show()
        }
        }