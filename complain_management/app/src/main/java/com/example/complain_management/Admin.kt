package com.example.complain_management

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.complain_management.databinding.ActivityAdminBinding
import com.example.complain_management.databinding.ActivitySignUpBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import java.util.concurrent.TimeUnit

class Admin : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityAdminBinding
    private lateinit var database: FirebaseDatabase
    private val emailPattern="[A-Za-z0-9._-]+@[a-z]+\\.+[a-z]+"
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth=FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        binding.servicebutton.setOnClickListener{
            val email=binding.adminemailEt.text.toString()
            val Password=binding.adminpass.text.toString()
            val cpassword=binding.adminconfirmPass.text.toString()
            val name=binding.name.text.toString()
            val number=binding.phone.text.toString()
            val organizationname=binding.organisationname.text.toString()
            if(email.isEmpty()||Password.isEmpty()||cpassword.isEmpty()||name.isEmpty()||number.isEmpty()||organizationname.isEmpty()){
                if(email.isEmpty()){
                    binding.adminemailEt.error="Enter your Email-id"
                }
                if(name.isEmpty()){
                    binding.adminemailEt.error="Enter your Name"
                }
                if(number.isEmpty()){
                    binding.adminemailEt.error="Enter your number"
                }
                if(organizationname.isEmpty()){
                    binding.adminemailEt.error="Enter your Organization Name"
                }
                if(Password.isEmpty()){
                    binding.adminpass.error="Enter your Password"
                }
                if(email.isEmpty()){
                    binding.adminconfirmPass.error="Re Enter your Password"
                }
                Toast.makeText(this,"Enter valid details", Toast.LENGTH_SHORT).show()
            }else if(!email.matches(emailPattern.toRegex())){
                binding.adminemailEt.error="Enter valid Email Address"
                Toast.makeText(this,"Enter valid Email Address", Toast.LENGTH_SHORT).show()
            }else if(Password.length<6){
                binding.adminpass.error="Choose password of atleast 6 characters"
                Toast.makeText(this,"Choose password of atleast 6 characters", Toast.LENGTH_SHORT).show()
            }
            else if(number.length!=10){
                binding.adminpass.error="Enter the valid number"
                Toast.makeText(this,"Enter the valid number", Toast.LENGTH_SHORT).show()
            }
            else if(Password!=cpassword){
                binding.adminconfirmPass.error="Password not matched,try again"
                Toast.makeText(this,"Password not matched,try again", Toast.LENGTH_SHORT).show()
            }else{
                auth.createUserWithEmailAndPassword(email,Password).addOnCompleteListener{
                    if(it.isSuccessful){
                        Toast.makeText(this,"Id build", Toast.LENGTH_SHORT).show()
                        val adminData = AdminData(
                            email = email,
                            name = name,
                            number = number,
                            organizationname = organizationname,
                        )
                        val verification=Verification(
                            type = "Admin",
                            phone = number,
                            verified = "No"

                        )
                        val typeRef=database.reference.child("Verification").child(auth.currentUser!!.uid)
                        val databaseRef=database.reference.child("Admin").child(auth.currentUser!!.uid)
                        databaseRef.setValue(adminData).addOnCompleteListener {
                            if(it.isSuccessful){
                                typeRef.setValue(verification).addOnCompleteListener {
                                    if(it.isSuccessful){
                                                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                                    "+91$number",
                                                    2,
                                                    TimeUnit.MINUTES,
                                                    this@Admin,
                                                    object: PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                                                        override fun onVerificationCompleted(
                                                            phoneAuthCredential: PhoneAuthCredential
                                                        ) {
                                                            signInWithPhoneAuthCredential( phoneAuthCredential)
                                                        }

                                                        override fun onVerificationFailed(
                                                            e: FirebaseException
                                                        ) {
                                                            Toast.makeText(this@Admin,e.message,Toast.LENGTH_SHORT).show()
                                                        }

                                                        override fun onCodeSent(
                                                            p0: String,
                                                            p1: PhoneAuthProvider.ForceResendingToken
                                                        ) {
                                                            val intent=Intent(
                                                                this@Admin,
                                                                OtpVerification::class.java
                                                            )
                                                            intent.putExtra("userId",auth.currentUser!!.uid)
                                                            intent.putExtra("Token",p0)
                                                            intent.putExtra("Phone","+91$number")
                                                            intent.putExtra("Type","Admin")
                                                            startActivity(intent)
                                                            finish()
                                                        }
                                                    }
                                                )
                                    }else{
                                        Toast.makeText(this,"Admin not registered",Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }else{
                                binding.adminconfirmPass.error=it.exception.toString()
                                Toast.makeText(this,it.exception.toString(), Toast.LENGTH_SHORT).show()
                            }
                        }
                    }else{
                        Toast.makeText(this,"Something went wrong, try again", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }

    }
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential){
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) {task->
                if(task.isSuccessful){
                    Toast.makeText(this@Admin, "Verification successful", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@Admin, "Verification failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}