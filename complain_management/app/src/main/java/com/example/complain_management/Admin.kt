package com.example.complain_management

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.complain_management.databinding.ActivityAdminBinding
import com.example.complain_management.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

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
                        val databaseRef=database.reference.child("admin").child(auth.currentUser!!.uid)
                        val admin:admindata=admindata(email = email, name = name, number = number, organizationname = organizationname, uid = auth.currentUser!!.uid,type="admin")

                        databaseRef.setValue(admin).addOnCompleteListener {
                            if(it.isSuccessful){
                                val intent= Intent(this,services::class.java)
                                intent.putExtra("userId", auth.currentUser!!.uid)
                                startActivity(intent)
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
}