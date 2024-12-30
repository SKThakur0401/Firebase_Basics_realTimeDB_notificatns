package com.example.attempttwoforfirebase.ui.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.attempttwoforfirebase.R
import com.example.attempttwoforfirebase.data.model.Contact
import com.example.attempttwoforfirebase.databinding.DataDisplayItemBinding

class DataDisplayAdapter() : ListAdapter<Contact, DataDisplayAdapter.DataDisplayViewHolder>(DiffUtil()) {


    inner class DataDisplayViewHolder(private val binding: DataDisplayItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(){
            val contact = getItem(adapterPosition)
            binding.tvName.text = contact.name
            binding.tvPhoneNumber.text = contact.phoneNum

            // Use Glide to load the image into the ImageView

            contact.imageUri?.let {imageUri ->
                Glide.with(binding.imageView.context)
                    .load(imageUri)
                    .into(binding.imageView)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataDisplayViewHolder {
        val binding = DataDisplayItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DataDisplayViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataDisplayViewHolder, position: Int) {
        holder.bind()
    }

    class DiffUtil : androidx.recyclerview.widget.DiffUtil.ItemCallback<Contact>()
    {
        override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            return oldItem == newItem
        }
    }
}

