package com.example.attempttwoforfirebase.ui.views

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.attempttwoforfirebase.R
import com.example.attempttwoforfirebase.databinding.ActivityMainBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var myRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        requestNotificationPermission()             // To request for notification permission (For Android 13+ only!)
                                                    // This request for notification permission would be made each
                                                    // time the user opens the app :) and he'll see permission request
                                                    // only if he hasn't granted yet (you can see the code of this function)


        FirebaseMessaging.getInstance().token           // This is an asynchronous step!! Always use ".addOnCompleteListener" to receive it's response
            .addOnCompleteListener { task ->            // otherwise it would just give some random response... since asynchronous operation response has not come
                if (task.isSuccessful) {
                    // Get new FCM registration token
                    val token = task.result
                    Log.d("TAG", "Token: $token")
                } else {
                    Log.w("TAG", "Fetching FCM registration token failed", task.exception)
                }
            }



        // Firebase-Realtime-Database BASICS
        // In firebase DB, data is stored in "key-value" pairs, where corresponding to each key there
        // can be an value , which will be "JSON" ! So u can basically store anything corresponding to a key
        // It is a non relational db

        myRef = FirebaseDatabase.getInstance().getReference("SampleKey")        // This looks in the database for an object with "path" --> "SampleKey"
                                                // If no object is found, it creates a "key" called "Users"

        myRef.setValue("This is a sample value corresponding to sample key, am using string, could be any data type / json")

    }



    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                1001
            )
        }
    }
}
