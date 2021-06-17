package com.example.ledmatrix.ui.authentication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.ledmatrix.R
import com.example.ledmatrix.ui.home.CommutorActivity
import com.example.ledmatrix.ui.home.HomeFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.login_tab_fragment.*


class LoginTabFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.login_tab_fragment, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTransforming()
        auth = Firebase.auth
        login_btn.setOnClickListener {
            val email = login_email.text.toString().trim()
            val password = login_pass.text.toString().trim()
            if(email != null && email != "" && password != null && password != ""){
            //login api
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(LoginFragmentDemo.TAG, "signInWithEmail:success")
                        val user = auth.currentUser
                        Toast.makeText(
                            getActivity(),
                            "Login success.",
                            Toast.LENGTH_SHORT
                        ).show()
                        val intent = Intent(activity, CommutorActivity::class.java)
                        startActivity(intent)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(LoginFragmentDemo.TAG, "signInWithEmail:failure", task.exception)

                        Toast.makeText(
                            getActivity(),
                            "Login failed." + task.exception?.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }else{
                Toast.makeText(
                    getActivity(),
                    "Login failed. " + "Please fill out all fields first.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

    private fun setupTransforming() {
        login_email.translationX = 800F
        login_pass.translationX = 800F
        login_forget_pass.translationX = 800F
        login_btn.translationX = 800F

        login_email.animate().translationX(0F).alpha(1F).setDuration(600).setStartDelay(100)
            .start()
        login_pass.animate().translationX(0F).alpha(1F).setDuration(600).setStartDelay(300)
            .start()
        login_forget_pass.animate().translationX(0F).alpha(1F).setDuration(600).setStartDelay(500)
            .start()
        login_btn.animate().translationX(0F).alpha(1F).setDuration(600).setStartDelay(700)
            .start()
    }

    companion object{
        const val TAG = "LoginTabFragment"
    }
}