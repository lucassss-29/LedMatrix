package com.example.ledmatrix.ui.authentication

import android.os.Bundle
import android.util.Log
import android.view.InflateException
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.ledmatrix.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.signup_tab_fragment.*

class SignupTabFragment : Fragment() {
    private var auth: FirebaseAuth = Firebase.auth
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.signup_tab_fragment, container, false)
        Log.e(TAG, "view")
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth
        signup_btn.setOnClickListener {
            val email = signup_email.text.toString().trim()
            val password = signup_pass.text.toString().trim()
            val fullName = signup_name.text.toString().trim()
            val confirmPassword = signup_confirm_pass.text.toString().trim()
            if(email != null && email != "" &&
                password != null && password != "" &&
                fullName != null && fullName != "" &&
                confirmPassword != null && confirmPassword != ""){
                if(password != confirmPassword){
                    Toast.makeText(
                        getActivity(),
                        "Account registration failed. " + "Your password and confirm password don't match",
                        Toast.LENGTH_SHORT
                    ).show()
                }else{
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(requireActivity()) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(LoginFragmentDemo.TAG, "createUserWithEmail:success")
                                val user = auth.currentUser
                                // Update fullname
                                user?.updateProfile(
                                    UserProfileChangeRequest.Builder()
                                        .setDisplayName(fullName)
                                        .build()
                                )?.addOnCompleteListener(requireActivity()) { task ->
                                    var message: String;
                                    if (!task.isSuccessful) {
                                        message =
                                            "Account registration failed." + task.exception?.message.toString()
                                    } else {
                                        message = "Account registered successfully"
                                    }
                                    Toast.makeText(
                                        getActivity(),
                                        message,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(
                                    LoginFragmentDemo.TAG,
                                    "createUserWithEmail:failure",
                                    task.exception
                                )
                                Toast.makeText(
                                    getActivity(),
                                    "Account registration failed." + task.exception?.message.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }
            }else{
                Toast.makeText(
                    getActivity(),
                    "Account registration failed. " + "Please fill out all fields first.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    companion object{
        const val TAG = "SignupTabFragment"
    }
}