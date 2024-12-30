package com.example.attempttwoforfirebase.ui.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.attempttwoforfirebase.data.model.Contact
import com.example.attempttwoforfirebase.databinding.FragmentDataDisplayBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DataDisplayFragment : Fragment() {

    private lateinit var binding: FragmentDataDisplayBinding
    private lateinit var adapter: DataDisplayAdapter
    private lateinit var databaseRef : DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentDataDisplayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = DataDisplayAdapter()
        binding.rvContactList.adapter = adapter
        binding.rvContactList.setHasFixedSize(true)
        databaseRef = FirebaseDatabase.getInstance().getReference("ContactList")
        listen()
    }

    private fun listen(){

        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val contactList = arrayListOf<Contact>()

                for(data in snapshot.children){
                    val contact = data.getValue(Contact::class.java)
                    contactList.add(contact!!)
                }
                adapter.submitList(contactList)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show()
            }
        })
    }

}