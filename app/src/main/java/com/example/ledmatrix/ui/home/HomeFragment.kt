package com.example.ledmatrix.ui.home

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.ledmatrix.R
import com.example.ledmatrix.databinding.ActivityHomeBinding
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_home.*
import androidx.fragment.app.replace
import com.example.advertise.AdvertiseFragment
import com.example.ledmatrix.ui.bluetooth.ScanDevicesFragment
import com.example.ledmatrix.ui.bluetooth.ScanDevicesFragment.Companion.m_bluetoothSocket
import com.example.ledmatrix.ui.bluetooth.ScanDevicesFragment.Companion.m_isConnected
import com.example.ledmatrix.ui.profile.ProfileFragment
import com.example.ledmatrix.ui.project.InfoFragment
import com.example.ledmatrix.utils.Utils.toast
import java.io.IOException

class HomeFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var storageReference: StorageReference


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_home, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        storageReference = FirebaseStorage.getInstance().getReference()
        auth = Firebase.auth
//        setupHomeInfo()
        home_profile_image.setOnClickListener {
//            setContentView(R.layout.activity_main)
            parentFragmentManager.commit {
                setReorderingAllowed(true)
                setCustomAnimations(
                    R.anim.slide_in,
                    R.anim.fade_out,
                    R.anim.fade_in,
                    R.anim.slide_out
                )
                replace<ProfileFragment>(R.id.fragment_commutor)
                addToBackStack(null)
            }
        }

        btnScanning.setOnClickListener {
            parentFragmentManager.commit {
                setReorderingAllowed(true)
                setCustomAnimations(
                    R.anim.slide_in,
                    R.anim.fade_out,
                    R.anim.fade_in,
                    R.anim.slide_out
                )
                replace<ScanDevicesFragment>(R.id.fragment_commutor)
                addToBackStack(null)
            }
        }

        btnDisconnect.setOnClickListener{
            disconnect()
        }

        iv_home_advertise.setOnClickListener {
            parentFragmentManager.commit {
                setReorderingAllowed(true)
                setCustomAnimations(
                    R.anim.slide_in,
                    R.anim.fade_out,
                    R.anim.fade_in,
                    R.anim.slide_out
                )
                replace<AdvertiseFragment>(R.id.fragment_commutor)
                addToBackStack(null)
            }
        }

        tv_home_info.setOnClickListener {
            parentFragmentManager.commit {
                setReorderingAllowed(true)
                setCustomAnimations(
                    R.anim.slide_in,
                    R.anim.fade_out,
                    R.anim.fade_in,
                    R.anim.slide_out
                )
                replace<InfoFragment>(R.id.fragment_commutor)
                addToBackStack(null)
            }
        }


    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_home)
////        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
//        storageReference = FirebaseStorage.getInstance().getReference()
//        auth = Firebase.auth
//        setupHomeInfo()
//
//        home_profile_image.setOnClickListener {
//            setContentView(R.layout.activity_main)
//            supportFragmentManager.commit {
//                setReorderingAllowed(true)
//                setCustomAnimations(
//                    R.anim.slide_in,
//                    R.anim.fade_out,
//                    R.anim.fade_in,
//                    R.anim.slide_out
//                )
//                replace<ProfileFragment>(R.id.fragment_container_view)
//                addToBackStack(null)
//            }
//        }
//    }

//    private fun setupHomeInfo() {
//        val mail = auth.currentUser?.email.toString()
//        val userName: List<String> = mail.split("@")
//        Log.e(TAG, "userName " + userName)
//        Log.e(TAG, "userName[1] = " + userName[0])
//
//        tv_home_welcome.setText("Welcome, " + userName[0].capitalize() + "!")
//
//        val profileRef: StorageReference =
//            storageReference.child("users/" + auth.currentUser?.uid + "/profile.jpg")
//        profileRef.downloadUrl.addOnSuccessListener(object : OnSuccessListener<Uri> {
//            override fun onSuccess(uri: Uri?) {
//                Picasso.get().load(uri).into(home_profile_image)
//            }
//
//        }).addOnFailureListener {
//            toast("Optional: Go to profile to setup infomation", requireActivity())
//        }
//    }
private fun disconnect(){
    if(m_bluetoothSocket != null){
        try{
            m_bluetoothSocket!!.close()
            toast("Disconnected",requireActivity())
            m_bluetoothSocket = null
            m_isConnected = false
        }catch (e: IOException){
            e.printStackTrace()
        }
    }
    //  finish()
}
    companion object {
        val TAG = "Main Activity"
    }
}
