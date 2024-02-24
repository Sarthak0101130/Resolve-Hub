package com.example.otpverification

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.complain_management.R
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import java.util.concurrent.TimeUnit

class VerifyEnterOtp : AppCompatActivity() {
    var inputnumber1: EditText? = null
    var inputnumber2: EditText? = null
    var inputnumber3: EditText? = null
    var inputnumber4: EditText? = null
    var inputnumber5: EditText? = null
    var inputnumber6: EditText? = null
    var getotpbackend: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verifyenterotp)
        val verifybuttonclick = findViewById<Button>(R.id.buttonsubmitotp)
        inputnumber1 = findViewById<EditText>(R.id.inputotp1)
        inputnumber2 = findViewById<EditText>(R.id.inputotp2)
        inputnumber3 = findViewById<EditText>(R.id.inputotp3)
        inputnumber4 = findViewById<EditText>(R.id.inputotp4)
        inputnumber5 = findViewById<EditText>(R.id.inputotp5)
        inputnumber6 = findViewById<EditText>(R.id.inputotp6)
        val textView = findViewById<TextView>(R.id.textmobileshownumber)
        textView.text = String.format(
            "+91-%s", intent.getStringExtra("mobile")
        )
        getotpbackend = intent.getStringExtra("backendotp")
        val progressBarverifyotp = findViewById<ProgressBar>(R.id.progressbar_verify_otp)
        verifybuttonclick.setOnClickListener {
            if (!inputnumber1.getText().toString().trim { it <= ' ' }
                    .isEmpty() && !inputnumber2.getText().toString().trim { it <= ' ' }
                    .isEmpty() && !inputnumber3.getText().toString().trim { it <= ' ' }
                    .isEmpty() && !inputnumber4.getText().toString().trim { it <= ' ' }
                    .isEmpty() && !inputnumber5.getText().toString().trim { it <= ' ' }
                    .isEmpty() && !inputnumber6.getText().toString().trim { it <= ' ' }
                    .isEmpty()) {
                //
                val entercodeotp = inputnumber1.getText().toString() +
                        inputnumber2.getText().toString() +
                        inputnumber3.getText().toString() +
                        inputnumber4.getText().toString() +
                        inputnumber5.getText().toString() +
                        inputnumber6.getText().toString()

                if (getotpbackend != null) {
                    progressBarverifyotp.visibility = View.VISIBLE
                    verifybuttonclick.visibility = View.INVISIBLE
                    val phoneAuthCredential = PhoneAuthProvider.getCredential(
                        getotpbackend!!, entercodeotp
                    )
                    FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                        .addOnCompleteListener { task ->
                            progressBarverifyotp.visibility = View.GONE
                            verifybuttonclick.visibility = View.VISIBLE
                            if (task.isSuccessful) {
                                val intent = Intent(applicationContext, dashboard::class.java)
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                startActivity(intent)
                            } else {
                                Toast.makeText(
                                    this@VerifyEnterOtp,
                                    "Enter the Correct OTP",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                } else {
                    Toast.makeText(this@VerifyEnterOtp, "CHECk CONNECTION", Toast.LENGTH_SHORT)
                        .show()
                }

                //
                //                    Toast.makeText(VerifyEnterOtp.this,"OTP VERIFYING",Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this@VerifyEnterOtp, "ENTER THE CORRECT OTP", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        numberotpmove()
        val resendlabel = findViewById<TextView>(R.id.textresendotp)
        resendlabel.setOnClickListener {
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + intent.getStringExtra("mobile"),
                60,
                TimeUnit.SECONDS,
                this@VerifyEnterOtp,
                object : OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {}
                    override fun onVerificationFailed(e: FirebaseException) {
                        Toast.makeText(this@VerifyEnterOtp, e.message, Toast.LENGTH_SHORT).show()
                    }

                    override fun onCodeSent(
                        newbackendotp: String,
                        forceResendingToken: ForceResendingToken
                    ) {
                        getotpbackend = newbackendotp
                        Toast.makeText(
                            this@VerifyEnterOtp,
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
                if (!s.toString().trim { it <= ' ' }.isEmpty()) {
                    inputnumber2!!.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
        inputnumber2!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (!s.toString().trim { it <= ' ' }.isEmpty()) {
                    inputnumber3!!.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
        inputnumber3!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (!s.toString().trim { it <= ' ' }.isEmpty()) {
                    inputnumber4!!.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
        inputnumber4!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (!s.toString().trim { it <= ' ' }.isEmpty()) {
                    inputnumber5!!.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
        inputnumber5!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (!s.toString().trim { it <= ' ' }.isEmpty()) {
                    inputnumber6!!.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }
}