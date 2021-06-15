package com.example.ledmatrix.ui.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.ledmatrix.R
import com.example.ledmatrix.databinding.ActivityProfileBinding
import com.example.ledmatrix.ui.authentication.Register.RegisterActivity
import com.example.ledmatrix.utils.toast
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_profile.*


class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var storageReference: StorageReference
    private lateinit var auth: FirebaseAuth
//    private lateinit var infoFirebase: RegisterActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile)
        storageReference = FirebaseStorage.getInstance().getReference()
        auth = Firebase.auth

        setupProfileInfo()


//        binding.ivProfileAvatar.setImageResource(R.drawable.ic_profile_avatar)
        binding.btnProfileChangeAvatar.setOnClickListener {
            val openGalleryIntent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            startActivityForResult(openGalleryIntent, 1000)
        }

        val profileRef: StorageReference = storageReference.child("users/" + auth.currentUser?.uid + "/profile.jpg")
        profileRef.downloadUrl.addOnSuccessListener(object: OnSuccessListener<Uri>{
            override fun onSuccess(uri: Uri?) {
                Picasso.get().load(uri).into(iv_profile_avatar)
            }

        })

    }

    private fun setupProfileInfo() {
        val profleEmail = auth.currentUser?.email.toString()

        val userName: List<String> = profleEmail?.split("@")

        tv_profile_name.setText(userName[0].capitalize())
        tv_profile_email.setText(profleEmail)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1000){
            if (resultCode == Activity.RESULT_OK){
                val imageUri = data?.data!!
//                binding.ivProfileAvatar.setImageURI(imageUri)
                uploadImageToFirebase(imageUri)
            }
        }
    }

    private fun uploadImageToFirebase(imageUri: Uri) {
        // upload image to firebase storage
        val fileRef: StorageReference = storageReference.child("users/" + auth.currentUser?.uid + "/profile.jpg")
        fileRef.putFile(imageUri).addOnSuccessListener(object :
            OnSuccessListener<UploadTask.TaskSnapshot> {
            override fun onSuccess(p0: UploadTask.TaskSnapshot?) {
                toast("Image Uploaded.")
                fileRef.downloadUrl.addOnSuccessListener(object: OnSuccessListener<Uri> {
                    override fun onSuccess(uri: Uri?) {
                        Picasso.get().load(uri).into(iv_profile_avatar)
                        home_profile_image.setImageURI(uri)
                    }

                })
            }
        }).addOnFailureListener{
            toast("Failed to upload the image.")
        }

    }
}