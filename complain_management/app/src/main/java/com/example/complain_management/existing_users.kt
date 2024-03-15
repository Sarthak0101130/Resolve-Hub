package com.example.complain_management

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.complain_management.databinding.ActivityExistingUsersBinding
import com.example.complain_management.databinding.ActivityUserViewComplainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class existing_users : AppCompatActivity() {
    private lateinit var database: FirebaseDatabase
    private lateinit var userRecyclerview: RecyclerView
    private lateinit var userArrayList:ArrayList<UserData>
    private lateinit var binding: ActivityExistingUsersBinding
    private lateinit var existingUserAdapter: ExistingUserAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExistingUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userRecyclerview=binding.existingUserUsersList
        database= FirebaseDatabase.getInstance()
        val adminId=intent.getStringExtra("userId")
        val adminRef=database.getReference("Admin").child(adminId!!)
        val userRef=database.getReference("UserData")
        userRecyclerview.setHasFixedSize(true);
        userRecyclerview.layoutManager= LinearLayoutManager(this);
        Toast.makeText(this@existing_users,adminId.toString(),Toast.LENGTH_SHORT).show()
        userArrayList= ArrayList()
        existingUserAdapter= ExistingUserAdapter(userArrayList)
        userRecyclerview.adapter=existingUserAdapter
        adminRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val adminData = dataSnapshot.getValue(AdminData::class.java)
                userArrayList.clear()
                if (adminData?.uid != null) {
                    val userIdList = adminData.uid
                    for (userId in userIdList!!) {
                        if (userId != null) {
                            userRef.child(userId)
                                .addValueEventListener(object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        val userData=snapshot.getValue(UserData::class.java)
                                        userData?.let{
                                            userArrayList.add(it)

                                            }
                                        existingUserAdapter.notifyDataSetChanged()
                                        }

                                    override fun onCancelled(error: DatabaseError) {
                                        TODO("Not yet implemented")
                                    }
                                })
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })

    }
}