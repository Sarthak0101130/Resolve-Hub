package com.example.complain_management

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.complain_management.databinding.ActivityServicesBinding
import com.example.complain_management.databinding.UserHomePageBinding
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds

class user_home_page_activity : AppCompatActivity() {
    private lateinit var binding:UserHomePageBinding
    lateinit var mAdView4 : AdView
    lateinit var mAdView3 : AdView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = UserHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val userId=intent.getStringExtra("userId")
        binding.userHomePageRegisterComplaintBox.setOnClickListener {
           val intent= Intent(this@user_home_page_activity,Register_Complaint::class.java)
            intent.putExtra(userId,"userId")
            startActivity(intent)
            finish()
        }

        userad1()
        userad2()
    }

    private fun userad2() {
        MobileAds.initialize(this) {}

        mAdView3 = findViewById(R.id.user_ad_1)
        val adRequest = AdRequest.Builder().build()
        mAdView3.loadAd(adRequest)
    }

    private fun userad1() {
        MobileAds.initialize(this) {}

        mAdView4 = findViewById(R.id.user_ad_2)
        val adRequest = AdRequest.Builder().build()
        mAdView4.loadAd(adRequest)
    }
}