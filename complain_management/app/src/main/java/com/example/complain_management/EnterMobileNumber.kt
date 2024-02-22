package com.example.otpverification

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.complain_management.R
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import java.util.concurrent.TimeUnit

class EnterMobileNumber : AppCompatActivity() {
    var enternumber: EditText? = null
    var getotpbutton: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mobilenumber)
        enternumber = findViewById<EditText>(R.id.input_mobile_number)
        getotpbutton = findViewById<Button>(R.id.buttongetotp)
        val progressBar = findViewById<ProgressBar>(R.id.progressbar_sending_otp)
        getotpbutton.setOnClickListener(View.OnClickListener {
            if (!enternumber.getText().toString().trim { it <= ' ' }.isEmpty()) {
                if (enternumber.getText().toString().trim { it <= ' ' }.length == 10) {
                    progressBar.visibility = View.VISIBLE
                    getotpbutton.setVisibility(View.INVISIBLE)
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+91" + enternumber.getText().toString(),
                        60,
                        TimeUnit.SECONDS,
                        this@EnterMobileNumber,
                        object : OnVerificationStateChangedCallbacks() {
                            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                                progressBar.visibility = View.GONE
                                getotpbutton.setVisibility(View.VISIBLE)
                            }

                            override fun onVerificationFailed(e: FirebaseException) {
                                progressBar.visibility = View.GONE
                                getotpbutton.setVisibility(View.VISIBLE)
                                Toast.makeText(
                                    this@EnterMobileNumber,
                                    e.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            override fun onCodeSent(
                                backendotp: String,
                                forceResendingToken: ForceResendingToken
                            ) {
                                progressBar.visibility = View.GONE
                                getotpbutton.setVisibility(View.VISIBLE)
                                val intent = Intent(applicationContext, VerifyEnterOtp::class.java)
                                intent.putExtra("mobile", enternumber.getText().toString())
                                intent.putExtra("backendotp", backendotp)
                                startActivity(intent)
                            }
                        }
                    )

                    //                        Intent intent = new Intent(getApplicationContext(), VerifyEnterOtp.class);
                    //                        intent.putExtra("mobile", enternumber.getText().toString());
                    //                        startActivity(intent);
                } else {
                    Toast.makeText(
                        this@EnterMobileNumber,
                        "PLEASE ENTER A VALID NUMBER",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(this@EnterMobileNumber, "PLEASE ENTER A NUMBER", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }
}