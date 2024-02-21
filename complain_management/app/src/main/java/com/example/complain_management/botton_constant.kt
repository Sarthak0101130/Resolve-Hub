package com.example.complain_management

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.complain_management.databinding.ActivityBottonConstantBinding
import com.example.complain_management.databinding.ActivityServicesBinding

class botton_constant : AppCompatActivity() {
    private lateinit var binding:ActivityBottonConstantBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBottonConstantBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.vectorEk1.setOnClickListener {
            val intent= Intent(this,home_page_activity::class.java)
            startActivity(intent)
        }
    }
}