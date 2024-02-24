package com.example.complain_management

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.complain_management.databinding.ActivityMainBinding
import com.example.complain_management.databinding.ActivityOtpVerificationBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.FirebaseDatabase
import java.util.concurrent.TimeUnit

class OtpVerification : AppCompatActivity() {

    private lateinit var binding:ActivityOtpVerificationBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var inputnumber1: EditText
    private lateinit var inputnumber2: EditText
    private lateinit var inputnumber3: EditText
    private lateinit var inputnumber4: EditText
    private lateinit var inputnumber5: EditText
    private lateinit var inputnumber6: EditText
    var token: String? = null

            override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityOtpVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
                var type=intent.getStringExtra("Type")
        val userId = intent.getStringExtra("userId")
                token=intent.getStringExtra("Token")
                firebaseAuth = FirebaseAuth.getInstance()
                val phone=intent.getStringExtra("Phone")
                 inputnumber1=binding.inputotp1
                 inputnumber2=binding.inputotp2
                 inputnumber3=binding.inputotp3
                 inputnumber4=binding.inputotp4
                 inputnumber5=binding.inputotp5
                 inputnumber6=binding.inputotp6
                numberotpmove(
                )
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
                                        val updateMap = mapOf<String, Any>(
                                            "verified" to "Yes"
                                        )
                                        Toast.makeText(
                                            this@OtpVerification,
                                            "Entered verification process",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        val verificationRef=FirebaseDatabase.getInstance().getReference("Verification").child(userId!!)
                                        verificationRef.updateChildren(updateMap).addOnCompleteListener {
                                            Toast.makeText(
                                                this@OtpVerification,
                                                "verification process completer",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                            .addOnFailureListener { e->
                                                Toast.makeText(
                                                    this@OtpVerification,
                                                    e.message,
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        when(type!!){
                                            "User"->{
                                                val intent= Intent(this@OtpVerification,user_home_page_activity::class.java)
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                                intent.putExtra("UserId",userId)
                                                startActivity(intent)
                                            }
                                            "Admin"->{
                                                val intent= Intent(this@OtpVerification,home_page_activity::class.java)
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                                intent.putExtra("UserId",userId)
                                                startActivity(intent)
                                            }
                                            else->{
                                                Toast.makeText(this@OtpVerification,"Unexpected Type",Toast.LENGTH_SHORT).show()
                                            }
                                        }

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
                binding.textresendotp.setOnClickListener {
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+91$phone",
                        2,
                        TimeUnit.MINUTES,
                        this@OtpVerification,
                        object: PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                            override fun onVerificationCompleted(
                                phoneAuthCredential: PhoneAuthCredential
                            ) {
                                signInWithPhoneAuthCredential( phoneAuthCredential)
                            }

                            override fun onVerificationFailed(
                                e: FirebaseException
                            ) {
                                Toast.makeText(this@OtpVerification,e.message,Toast.LENGTH_SHORT).show()
                            }

                            override fun onCodeSent(
                                p0: String,
                                p1: PhoneAuthProvider.ForceResendingToken
                            ) {
                                token = p0
                        Toast.makeText(
                            this@OtpVerification,
                            "OTP sent sucessfully",
                            Toast.LENGTH_SHORT
                        ).show()
                            }
                        }
                    )

                }


}
    private fun numberotpmove() {
        inputnumber1!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString().trim { it <= ' ' }.isNotEmpty()) {
                    inputnumber2!!.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
        inputnumber2!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString().trim { it <= ' ' }.isNotEmpty()) {
                    inputnumber3!!.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
        inputnumber3!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString().trim { it <= ' ' }.isNotEmpty()) {
                    inputnumber4!!.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
        inputnumber4!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString().trim { it <= ' ' }.isNotEmpty()) {
                    inputnumber5!!.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
        inputnumber5!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString().trim { it <= ' ' }.isNotEmpty()) {
                    inputnumber6!!.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential){
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) {task->
                if(task.isSuccessful){
                    Toast.makeText(this@OtpVerification, "Verification successful", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@OtpVerification, "Verification failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    }
