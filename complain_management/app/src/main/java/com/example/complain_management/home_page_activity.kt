package com.example.complain_management

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.complain_management.databinding.HomePageBinding
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds

class home_page_activity : AppCompatActivity() {
    private lateinit var binding: HomePageBinding
    lateinit var mAdView1 : AdView
    lateinit var mAdView2 : AdView
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
        binding.rectangle8.setOnClickListener {
            val intent= Intent(this,admin_complain_view::class.java)
            intent.putExtra("userId",userId)
            startActivity(intent)
            finish()
        }

        addAdView1()
        addAdView2()
    }

    private fun addAdView2() {
        MobileAds.initialize(this) {}

        mAdView2 = findViewById(R.id.image_2)
        val adRequest = AdRequest.Builder().build()
        mAdView2.loadAd(adRequest)
    }

    private fun addAdView1() {
        MobileAds.initialize(this) {}

        mAdView1 = findViewById(R.id.image_1)
        val adRequest = AdRequest.Builder().build()
        mAdView1.loadAd(adRequest)
    }
}