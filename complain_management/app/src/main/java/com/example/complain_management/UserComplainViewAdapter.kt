package com.example.complain_management

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text

class UserComplainViewAdapter(private val userList:ArrayList<UserComplain>) :
    RecyclerView.Adapter<UserComplainViewAdapter.MyViewHolder>() {



    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val itemView=LayoutInflater.from(parent.context).inflate(R.layout.user_view_compain_list,
        parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UserComplainViewAdapter.MyViewHolder, position: Int) {
        val currentitem=userList[position]

        holder.complain_type.text=currentitem.ComplainType
        holder.complain_subject.text=currentitem.ComplainSubject
        holder.verified.text=currentitem.verified
    }

    override fun getItemCount(): Int {
        return userList.size
    }
    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val complain_type:TextView=itemView.findViewById(R.id.type_text)
        val complain_subject: TextView=itemView.findViewById(R.id.subject_text)
        val verified:TextView=itemView.findViewById(R.id.verified_text)
    }
}