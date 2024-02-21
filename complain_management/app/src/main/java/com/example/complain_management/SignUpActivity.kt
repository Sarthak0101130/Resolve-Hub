//package com.example.complain_management
//
//import android.content.Intent
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.service.autofill.UserData
//import android.widget.EditText
//import android.widget.ImageButton
//import android.widget.TextView
//import android.widget.Toast
//import com.example.complain_management.databinding.ActivitySignUpBinding
//import com.google.common.base.Predicates.equalTo
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.database.DataSnapshot
//import com.google.firebase.database.DatabaseError
//import com.google.firebase.database.DatabaseReference
//import com.google.firebase.database.FirebaseDatabase
//import com.google.firebase.database.ValueEventListener
//
//class SignUpActivity : AppCompatActivity() {
//    private lateinit var auth:FirebaseAuth
//    private lateinit var binding: ActivitySignUpBinding
//    private lateinit var database: FirebaseDatabase
//    private val emailPattern="[A-Za-z0-9._-]+@[a-z]+\\.+[a-z]+"
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivitySignUpBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        auth=FirebaseAuth.getInstance()
//        database = FirebaseDatabase.getInstance()
//
//        binding.admbutton.setOnClickListener {
//            val intent=Intent(this,Admin::class.java)
//            startActivity(intent)
//        }
//
//        binding.button.setOnClickListener{
//            val email=binding.emailEt.text.toString()
//            val Password=binding.passET.text.toString()
//            val cpassword=binding.confirmPassEt.text.toString()
//            if(email.isEmpty()||Password.isEmpty()||cpassword.isEmpty()){
//                if(email.isEmpty()){
//                    binding.emailEt.error="Enter your Email-id"
//                }
//                if(Password.isEmpty()){
//                    binding.passET.error="Enter your Password"
//                }
//                if(email.isEmpty()){
//                    binding.confirmPassEt.error="Re Enter your Password"
//                }
//                Toast.makeText(this,"Enter valid details",Toast.LENGTH_SHORT).show()
//            }else if(!email.matches(emailPattern.toRegex())){
//                binding.emailEt.error="Enter valid Email Address"
//                Toast.makeText(this,"Enter valid Email Address",Toast.LENGTH_SHORT).show()
//            }else if(Password.length<6){
//                binding.passET.error="Choose password of atleast 6 characters"
//                Toast.makeText(this,"Choose password of atleast 6 characters",Toast.LENGTH_SHORT).show()
//            }
//            else if(Password!=cpassword){
//                binding.confirmPassEt.error="Password not matched,try again"
//                Toast.makeText(this,"Password not matched,try again",Toast.LENGTH_SHORT).show()
//            }else{
//                auth.createUserWithEmailAndPassword(email,Password).addOnCompleteListener{
//                    if(it.isSuccessful){
//                        val databaseRef=database.reference.child("admin").child(auth.currentUser!!.uid)
//                        val users:user=user(email,auth.currentUser!!.uid)
//
//                        databaseRef.setValue(users).addOnCompleteListener {
//                            if(it.isSuccessful){
//                                val intent=Intent(this,SignInActivity::class.java)
//                                startActivity(intent)
//                            }else{
//                                binding.confirmPassEt.error=it.exception.toString()
//                                Toast.makeText(this,it.exception.toString(),Toast.LENGTH_SHORT).show()
//                            }
//                        }
//                    }else{
//                        Toast.makeText(this,"Something went wrong, try again",Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }
//        }
//    }
//}