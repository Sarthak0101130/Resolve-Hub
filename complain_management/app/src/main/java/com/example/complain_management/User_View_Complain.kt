package com.example.complain_management

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.complain_management.databinding.ActivityAdminBinding
import com.example.complain_management.databinding.UserViewCompainListBinding
import com.google.firebase.database.DatabaseReference
import com.example.complain_management.UserComplainViewAdapter
import com.example.complain_management.databinding.ActivityUserViewComplainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.checkerframework.checker.nullness.qual.NonNull

class User_View_Complain : AppCompatActivity(){

    private lateinit var database:FirebaseDatabase
    private lateinit var userRecyclerview:RecyclerView
    private lateinit var userArrayList:ArrayList<UserComplain>
    private lateinit var binding:ActivityUserViewComplainBinding
    private lateinit var userComplainViewAdapter: UserComplainViewAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserViewComplainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userRecyclerview=binding.userComplainList
        database= FirebaseDatabase.getInstance()
        val userId=intent.getStringExtra("userId")
        val complainRef=database.getReference("ComplainUser").child(userId!!)
        userRecyclerview.setHasFixedSize(true);
        userRecyclerview.layoutManager=LinearLayoutManager(this);

        userArrayList= ArrayList()
        userComplainViewAdapter= UserComplainViewAdapter(userArrayList)
        userRecyclerview.adapter=userComplainViewAdapter
        complainRef.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot){
                userArrayList.clear()

                for(compainSnapshot in dataSnapshot.children){
                    val userComplain=compainSnapshot.getValue(UserComplain::class.java)

                    userComplain?.let{
                        userArrayList.add(it)
                    }
                }
                userComplainViewAdapter.notifyDataSetChanged()
            }
            override fun onCancelled(databaseError: DatabaseError){
                Toast.makeText(this@User_View_Complain,"error",Toast.LENGTH_SHORT).show()
            }
        })

    }
}