package com.example.complain_management

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.complain_management.databinding.ActivityServicesBinding
import com.example.complain_management.databinding.UserHomePageBinding

class user_home_page_activity : AppCompatActivity() {
    private lateinit var binding:UserHomePageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = UserHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.userHomePageRegisterComplaintBox.setOnClickListener {
           val intent= Intent(this@user_home_page_activity,Register_Complaint::class.java)
            startActivity(intent)
            finish()
        }
    }
}