package com.example.complain_management

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import com.example.complain_management.databinding.EditUserBinding
import com.example.complain_management.databinding.HomePageBinding

class edit_user_activity : AppCompatActivity() {
    private lateinit var binding:EditUserBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_user)
        binding = EditUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val userId = intent.getStringExtra("userId")
        binding.editUserAddUserBox.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View){
            val intent= Intent(this@edit_user_activity,add_user_activity::class.java)
                intent.putExtra("userId",userId)
                startActivity(intent)
                finish()
            }
        })
    }
}