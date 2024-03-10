package com.example.complain_management

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.complain_management.databinding.HomePageBinding
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds

class home_page_activity : AppCompatActivity() {
    private lateinit var binding: HomePageBinding
    lateinit var mAdView1 : AdView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = HomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val userId = intent.getStringExtra("userId")

        Toast.makeText(this@home_page_activity,userId.toString(),Toast.LENGTH_SHORT).show()
        binding.rectangle8Ek2.setOnClickListener{
            val intent= Intent(this,edit_user_activity::class.java)
            intent.putExtra("userId",userId)
            startActivity(intent)
        }

        binding.rectangle10.setOnClickListener{
            val intent= Intent(this,edit_staff_activity::class.java)
            startActivity(intent)
        }

        binding.rectangle8.setOnClickListener {
            val intent= Intent(this,admin_complain_view::class.java)
            intent.putExtra("userId",userId)
            startActivity(intent)
            finish()
        }

        addAdView1()
    }

    private fun addAdView1() {
        MobileAds.initialize(this) {}

        mAdView1 = findViewById(R.id.image_1)
        val adRequest = AdRequest.Builder().build()
        mAdView1.loadAd(adRequest)
    }
}