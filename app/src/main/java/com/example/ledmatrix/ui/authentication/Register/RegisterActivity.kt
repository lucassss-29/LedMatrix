package com.example.ledmatrix.ui.authentication.Register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.ledmatrix.R
import com.example.ledmatrix.databinding.ActivityRegisterBinding
import com.example.ledmatrix.ui.authentication.LoginActivity.LoginActivity
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    companion object{
        private val TAG = RegisterActivity::class.java.simpleName
    }
    lateinit var binding : ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_register)
        auth = Firebase.auth
        binding.btnRegisterRegisterPage.setOnClickListener {
            val email = binding.etEmailRegisterPage.text.toString().trim()
            val password = binding.etPasswordRegisterPage.text.toString().trim()
            val fullName = binding.etFullNameRegisterPage.text.toString().trim()
            val phone = binding.etPhoneRegisterPage.text.toString().trim()

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success")
                        val user = auth.currentUser
                        // Update fullname
                        user?.updateProfile(
                            UserProfileChangeRequest.Builder()
                                .setDisplayName(fullName)
                                .build()
                        )?.addOnCompleteListener(this) {task ->
                            var message: String;
                            if (!task.isSuccessful) {
                                message = "Account registration failed." + task.exception?.message.toString()
                            } else {
                                message = "Account registered successfully"
                            }
                            Toast.makeText(
                                baseContext,
                                message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext,
                            "Account registration failed." + task.exception?.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
        binding.btnBacktoLoginRegisterPage.setOnClickListener {
            val i = Intent(this, LoginActivity::class.java)
            startActivity(i)
        }
    }
}