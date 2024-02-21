package com.example.complain_management

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.complain_management.databinding.HomePageBinding

class home_page_activity : AppCompatActivity() {
    private lateinit var binding: HomePageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val userId = intent.getStringExtra("userId")
        binding.rectangle8Ek2.setOnClickListener{
            val intent= Intent(this,edit_user_activity::class.java)
            intent.putExtra("userId",userId)
            startActivity(intent)
            finish()
        }
        binding.rectangle10.setOnClickListener{
            val intent= Intent(this,edit_staff_activity::class.java)
            startActivity(intent)
        }

        binding.rectangle11.setOnClickListener{
            val intent= Intent(this,edit_services_activity::class.java)
            startActivity(intent)
        }
    }
}