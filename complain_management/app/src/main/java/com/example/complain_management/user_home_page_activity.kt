package com.example.complain_management

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
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
        Toast.makeText(this@user_home_page_activity,userId.toString(),Toast.LENGTH_SHORT).show()
        binding.userHomePageRegisterComplaintBox.setOnClickListener {
           val intent= Intent(this@user_home_page_activity,Register_Complaint::class.java)
            intent.putExtra("userId",userId)
            startActivity(intent)
            finish()
        }
        binding.userHomePageViewComplaintsBox.setOnClickListener {
            val intent=Intent(this@user_home_page_activity,User_View_Complain::class.java)
            intent.putExtra("userId",userId)
            startActivity(intent)
            finish()
        }
        userad1()
    }

    private fun userad1() {
        MobileAds.initialize(this) {}

        mAdView3 = findViewById(R.id.user_ad_1)
        val adRequest = AdRequest.Builder().build()
        mAdView3.loadAd(adRequest)
    }

}