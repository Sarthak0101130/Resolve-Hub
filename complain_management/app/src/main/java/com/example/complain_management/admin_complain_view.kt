package com.example.complain_management

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.complain_management.databinding.ActivityAdminComplainViewBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class admin_complain_view : AppCompatActivity() {
    private lateinit var database: FirebaseDatabase
    private lateinit var adminRecyclerview: RecyclerView
    private lateinit var adminArrayList: ArrayList<AdminComplain>
    private lateinit var binding: ActivityAdminComplainViewBinding
    private lateinit var adminComplainViewAdapter: AdminComplainViewAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminComplainViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adminRecyclerview = binding.adminComplainList
        database = FirebaseDatabase.getInstance()
        val adminId = intent.getStringExtra("userId")
        val adminRef = database.getReference("Admin")
        val complainUserRef = database.getReference("ComplainUser").child(adminId!!)
        adminRecyclerview.setHasFixedSize(true);
        adminRecyclerview.layoutManager = LinearLayoutManager(this);

        adminArrayList = ArrayList()
        adminComplainViewAdapter = AdminComplainViewAdapter(adminArrayList)
        adminRecyclerview.adapter = adminComplainViewAdapter
        adminRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(adminDataSnapshot: DataSnapshot) {
                for (adminSnapshot in adminDataSnapshot.children) {
                    val userId = adminSnapshot.key
                    if (userId != null) {
                        complainUserRef.child(userId)
                            .addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(complainDataSnapshot: DataSnapshot) {
                                    val userComplaintList = ArrayList<UserComplain>()
                                    for (complaintSnapshot in complainDataSnapshot.children) {
                                        val userComplain =
                                            complaintSnapshot.getValue(UserComplain::class.java)
                                        userComplain?.let {
                                            userComplaintList.add(it)
                                        }
                                    }
                                    adminComplainViewAdapter.notifyDataSetChanged()
                                }

                                override fun onCancelled(complainDatabaseError: DatabaseError) {
                                    Toast.makeText(
                                        this@admin_complain_view,
                                        "error",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            })
                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@admin_complain_view,
                    "error",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}

