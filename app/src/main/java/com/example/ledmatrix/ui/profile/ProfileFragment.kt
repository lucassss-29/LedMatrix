package com.example.ledmatrix.ui.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.ledmatrix.R
import com.example.ledmatrix.databinding.ActivityProfileBinding
import com.example.ledmatrix.ui.home.HomeFragment
import com.example.ledmatrix.utils.Utils
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_profile.*


class ProfileFragment : Fragment() {
//    private lateinit var binding: ActivityProfileBinding
    private lateinit var storageReference: StorageReference
    private lateinit var auth: FirebaseAuth
//    private lateinit var infoFirebase: RegisterActivity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_profile, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        storageReference = FirebaseStorage.getInstance().getReference()
        auth = Firebase.auth
        setupProfileInfo()

        btn_profile_change_avatar.setOnClickListener {
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
        btn_profile_back.setOnClickListener {
            parentFragmentManager.commit {
                setReorderingAllowed(true)
                setCustomAnimations(
                    R.anim.fade_in,
                    R.anim.slide_out
                )
                replace<HomeFragment>(R.id.fragment_commutor)
//                addToBackStack(null)
            }

        }
    }

//        binding.ivProfileAvatar.setImageResource(R.drawable.ic_profile_avatar)
//        binding.btnProfileChangeAvatar.setOnClickListener {
//            val openGalleryIntent = Intent(
//                Intent.ACTION_PICK,
//                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
//            )
//            startActivityForResult(openGalleryIntent, 1000)
//        }
//
//        val profileRef: StorageReference = storageReference.child("users/" + auth.currentUser?.uid + "/profile.jpg")
//        profileRef.downloadUrl.addOnSuccessListener(object: OnSuccessListener<Uri>{
//            override fun onSuccess(uri: Uri?) {
//                Picasso.get().load(uri).into(iv_profile_avatar)
//            }
//
//        })

//    }

    private fun setupProfileInfo() {
        val profleEmail = auth.currentUser?.email.toString()

        val userName: List<String> = profleEmail?.split("@")

        tv_profile_name.setText(userName[0].capitalize())
        tv_profile_email.setText(profleEmail)
    }
//
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
//
    private fun uploadImageToFirebase(imageUri: Uri) {
        // upload image to firebase storage
        val fileRef: StorageReference = storageReference.child("users/" + auth.currentUser?.uid + "/profile.jpg")
        fileRef.putFile(imageUri).addOnSuccessListener(object :
            OnSuccessListener<UploadTask.TaskSnapshot> {
            override fun onSuccess(p0: UploadTask.TaskSnapshot?) {
                Utils.toast("Image Uploaded.", activity!!)
                fileRef.downloadUrl.addOnSuccessListener(object: OnSuccessListener<Uri> {
                    override fun onSuccess(uri: Uri?) {
                        Picasso.get().load(uri).into(iv_profile_avatar)
//                        home_profile_image.setImageURI(uri)
                        Utils.toast("Automatically refresh page", activity!!)
                    }
                })
            }
        }).addOnFailureListener{
            Utils.iToast("Failed to upload the image.", activity)
        }

    }
}