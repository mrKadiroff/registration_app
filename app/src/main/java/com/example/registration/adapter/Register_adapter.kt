package com.example.registration.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.registration.databinding.RegisterLayoutBinding
import com.example.registration.models.RegisterModel

class Register_adapter(var list: List<RegisterModel>,var onItemClickListener: OnItemClickListener) : RecyclerView.Adapter<Register_adapter.Vh>() {


    inner class Vh(var registerLayoutBinding: RegisterLayoutBinding) : RecyclerView.ViewHolder(registerLayoutBinding.root){

        fun onBind(registerModel: RegisterModel) {



            registerLayoutBinding.profile.setImageURI(Uri.parse(registerModel.rasm))
            registerLayoutBinding.name.text = registerModel.ism
            registerLayoutBinding.phone.text = registerModel.telefon

            registerLayoutBinding.root.setOnClickListener {
                onItemClickListener.onItemClick(registerModel)
            }



        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Register_adapter.Vh {
        return Vh (RegisterLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: Register_adapter.Vh, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int = list.size

    interface OnItemClickListener{
        fun onItemClick(registerModel: RegisterModel)
    }



}