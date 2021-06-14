package com.example.ledmatrix.ui.authentication

import android.os.Bundle
import android.util.Log
import android.view.InflateException
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ledmatrix.R

class SignupTabFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.signup_tab_fragment, container, false)
        return view
    }

    companion object{
        const val TAG = "SignupTabFragment"
    }
}