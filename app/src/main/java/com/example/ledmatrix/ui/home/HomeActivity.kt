package com.example.ledmatrix.ui.home

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.example.ledmatrix.R
import com.example.ledmatrix.databinding.ActivityHomeBinding
import com.example.ledmatrix.ui.profile.ProfileActivity
import com.example.ledmatrix.utils.toast
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_profile.*

class HomeActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var storageReference: StorageReference
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        storageReference = FirebaseStorage.getInstance().getReference()
        auth = Firebase.auth

        setupHomeInfo()

//        supportFragmentManager.commit {
//            setReorderingAllowed(true)
//            add<MainFragment>(R.id.frag_container)
//        }

        binding.homeProfileImage.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupHomeInfo() {
        val mail = auth.currentUser?.email.toString()
        val userName: List<String> = mail.split("@")
        Log.e(TAG, "userName " + userName)
        Log.e(TAG, "userName[1] = " + userName[0])

        tv_home_welcome.setText("Welcome, " + userName[0].capitalize() + "!")

        val profileRef: StorageReference = storageReference.child("users/" + auth.currentUser?.uid + "/profile.jpg")
        profileRef.downloadUrl.addOnSuccessListener(object: OnSuccessListener<Uri> {
            override fun onSuccess(uri: Uri?) {
                Picasso.get().load(uri).into(home_profile_image)
            }

        }).addOnFailureListener {
            toast("Optional: Go to profile to setup infomation")
        }
    }

    companion object{
        val TAG = "Main Activity"
    }


}