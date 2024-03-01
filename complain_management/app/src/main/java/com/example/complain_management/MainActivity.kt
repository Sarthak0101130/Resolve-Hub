package com.example.complain_management

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.complain_management.databinding.ActivityMainBinding
import com.example.complain_management.databinding.ActivitySignInBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mainGetStartedButton.setOnClickListener{
            val intent= Intent(this,SignInActivity::class.java)
            startActivity(intent)
        }
    }
}