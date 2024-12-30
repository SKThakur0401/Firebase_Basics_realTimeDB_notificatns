package com.example.attempttwoforfirebase.ui.views

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.example.attempttwoforfirebase.R
import com.example.attempttwoforfirebase.data.model.Contact
import com.example.attempttwoforfirebase.databinding.FragmentDataEntryBinding
import com.example.attempttwoforfirebase.utils.Constants
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class DataEntryFragment : Fragment() {
    private lateinit var binding : FragmentDataEntryBinding
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference


    private var selectedImageUri : Uri ?= null          // For storing the image uri  (for later use..)


    private var myImageUploadedOnTheInternet_kaDownloadLink :String? =null      // Ignore this initially when revising

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentDataEntryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        databaseReference = FirebaseDatabase.getInstance().getReference("ContactList")
        // To get a reference to the database, this line will look for "ContactList" in the database,
        // and if "ContactList" does not already exist, then it will create one reference with this name
        // So it is basically return if existing reference or create new reference
        // Firebase database is very fast real-time database... it is used for storing data for quick access

        // For storing general data we have " FirebaseStorage" , for storing images and stuff...

        listener()
    }

    private fun listener() {

        binding.btnSendContactToDB.setOnClickListener{
            sendContactToDB()
        }

        binding.btnGotoDataDisplay.setOnClickListener{
            findNavController().navigate(R.id.action_dataEntryFragment_to_dataDisplayFragment)
        }

        // Below code is for the button to Pick image from gallery
        val pickImageLauncher =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                uri?.let {
                    selectedImageUri = it
                    binding.imgContactDP.setImageURI(it)
                    binding.btnUploadImg.visibility = View.VISIBLE
                }
            }

        binding.btnSelectImg.setOnClickListener{
            pickImageLauncher.launch("image/*")
        }
        // ----------------------------------------------------------------------------------------


        binding.btnUploadImg.setOnClickListener{
            uploadImageOnFirebaseStorage()
        }
    }

    private fun sendContactToDB(){

        val name = binding.etName.text.toString()
        val phoneNum = binding.etPhoneNum.text.toString()

        val id = databaseReference.push().key!!       // Generate new "key" each time so that unique
        // name can be given to each item (basically a unique id for each item
        // in a list...

        val contact = Contact(id, name, phoneNum, myImageUploadedOnTheInternet_kaDownloadLink)

        databaseReference.child(id).setValue(contact)
            .addOnSuccessListener {
                Toast.makeText(context, "Saved on DB!", Toast.LENGTH_SHORT).show()
            }

        binding.etName.setText(Constants.EMPTY_STRING)
        binding.etPhoneNum.setText(Constants.EMPTY_STRING)
    }

    private fun uploadImageOnFirebaseStorage(){

        if(selectedImageUri == null){
            Toast.makeText(context, "Please select an image first!", Toast.LENGTH_SHORT).show()
            return
        }

        storageReference = FirebaseStorage.getInstance().getReference("images/${System.currentTimeMillis()}")       // Now this is just like in the case of "Database" firebase storage is used for storing bulky data like images, videos, etc.
                                                            // This line will look for a reference in db with this name " images/${System.currentTimeMillis()} "
                                                    // which it will most likely not find! (because exact that time will never come again so each time new reference is generated ;)

        storageReference.putFile(selectedImageUri!!)                  // This line would upload the image to the storage reference therefore that image is uploaded on the internet firebase storage! So that image is on the internet now!
            .addOnSuccessListener {
                storageReference.downloadUrl.addOnSuccessListener {                                 // storageReference.downloadUrl  basically calls the " getDownloadUrl() " function! This function would give us a "link" at which our image is stored on the internet! So anyone with that link can view our uploaded image on the internet
                    Toast.makeText(context, "Image Uploaded on the internet!", Toast.LENGTH_SHORT).show()

                    myImageUploadedOnTheInternet_kaDownloadLink = it.toString()         // So this is the "link of the image" which is uploaded on the internet on my "Firebase-Storage" , this link can be used by anyone to see that image, since that image is on the internet
                }
            }
            .addOnCanceledListener {
                Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show()
            }
    }

}


