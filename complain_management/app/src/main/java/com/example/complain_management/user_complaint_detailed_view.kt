package com.example.complain_management

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.complain_management.databinding.ActivityAdminBinding
import com.example.complain_management.databinding.ActivityUserComplaintDetailedViewBinding

class user_complaint_detailed_view : AppCompatActivity() {
    private lateinit var binding:ActivityUserComplaintDetailedViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserComplaintDetailedViewBinding.inflate(layoutInflater)
        val complainId=intent.getStringExtra("complainId")
        val userId=intent.getStringExtra("userId")
        setContentView(binding.root)
    }
}