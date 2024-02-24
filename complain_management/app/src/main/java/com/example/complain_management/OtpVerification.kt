package com.example.complain_management

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.complain_management.databinding.ActivityMainBinding
import com.example.complain_management.databinding.ActivityOtpVerificationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

class OtpVerification : AppCompatActivity() {

    private lateinit var binding:ActivityOtpVerificationBinding

            override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityOtpVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val userId = intent.getStringExtra("userId")
        val token=intent.getStringExtra("Token")
                val phone=intent.getStringExtra("Phone")
                val inputnumber1=binding.inputotp1
                val inputnumber2=binding.inputotp2
                val inputnumber3=binding.inputotp3
                val inputnumber4=binding.inputotp4
                val inputnumber5=binding.inputotp5
                val inputnumber6=binding.inputotp6
                val progressbar=binding.progressbarVerifyOtp
                binding.buttonsubmitotp.setOnClickListener{
                    if (!inputnumber1.text.toString().trim { it <= ' ' }
                    .isEmpty() && !inputnumber2.text.toString().trim { it <= ' ' }
                    .isEmpty() && !inputnumber3.text.toString().trim { it <= ' ' }
                    .isEmpty() && !inputnumber4.text.toString().trim { it <= ' ' }
                    .isEmpty() && !inputnumber5.text.toString().trim { it <= ' ' }
                    .isEmpty() && !inputnumber6.text.toString().trim { it <= ' ' }
                    .isEmpty()) {
                        val entercodeotp = inputnumber1.text.toString()+
                        inputnumber2.text.toString() +
                        inputnumber3.text.toString() +
                        inputnumber4.text.toString() +
                        inputnumber5.text.toString() +
                        inputnumber6.text.toString()
                        if(token!=null){
                            progressbar.visibility= View.VISIBLE
                            binding.buttonsubmitotp.visibility=View.VISIBLE
                            val phoneAuthCredential=PhoneAuthProvider.getCredential(
                                token!!,entercodeotp
                            )
                            FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                                .addOnCompleteListener { task->
                                    progressbar.visibility=View.GONE
                                    binding.buttonsubmitotp.visibility=View.VISIBLE
                                    if(task.isSuccessful){
                                        val intent= Intent(this@OtpVerification,user_home_page_activity::class.java)
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                        startActivity(intent)
                                    }
                                    else {
                                Toast.makeText(
                                    this@OtpVerification,
                                    "Enter the Correct OTP",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                                }
                        }else {
                    Toast.makeText(this@OtpVerification, "CHECk CONNECTION", Toast.LENGTH_SHORT)
                        .show()
                }
                        Toast.makeText(this@OtpVerification,"OTP VERIFYING",Toast.LENGTH_SHORT).show()
                    }else {
                Toast.makeText(this@OtpVerification, "ENTER THE CORRECT OTP", Toast.LENGTH_SHORT)
                    .show()
            }
                }
                numberotpmove()
    }
}