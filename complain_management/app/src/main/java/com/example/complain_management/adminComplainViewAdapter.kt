package com.example.complain_management

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdminComplainViewAdapter(private val AdminComplainList:ArrayList<AdminComplain>) :
    RecyclerView.Adapter<AdminComplainViewAdapter.MyViewHolder>() {



    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val itemView=LayoutInflater.from(parent.context).inflate(R.layout.admin_view_complain_list,
            parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AdminComplainViewAdapter.MyViewHolder, position: Int) {
        val currentAdmin=AdminComplainList[position]

        val userComplain = currentAdmin.userComplain
        holder.complain_type.text = userComplain.ComplainType
        holder.complain_subject.text = userComplain.ComplainSubject
        holder.verified.text = userComplain.verified

        val userData=currentAdmin.userData
        holder.name.text=userData.name
        holder.number.text=userData.number
    }

    override fun getItemCount(): Int {
        return AdminComplainList.size
    }
    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val complain_type:TextView=itemView.findViewById(R.id.admin_view_complain_type_text)
        val complain_subject: TextView=itemView.findViewById(R.id.admin_view_complain_subject_text)
        val verified:TextView=itemView.findViewById(R.id.admin_view_complain_verified_text)
        val name:TextView=itemView.findViewById(R.id.admin_view_complain_name_text)
        val number:TextView=itemView.findViewById(R.id.admin_view_complain_number_text)
    }
}