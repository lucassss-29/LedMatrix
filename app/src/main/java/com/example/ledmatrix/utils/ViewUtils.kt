package com.example.ledmatrix.utils

import android.content.Context
import android.text.BoringLayout
import android.view.View
import android.widget.Toast
import com.example.ledmatrix.R
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.UploadTask
import com.muddzdev.styleabletoastlibrary.StyleableToast

object Utils {
    fun showToast(msg: String?, ctx: Context?) {
        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show()
    }

    // custom green toast
    fun toast(msg: String?, ctx: Context) {
        StyleableToast.makeText(ctx!!, msg, R.style.toast_style).show()
    }

    // custom red toast
    fun iToast(msg: String?, ctx: Context?) {
        StyleableToast.makeText(ctx!!, msg, R.style.toast_style).show()
    }

    fun progressBar(v: View, status: String): View {
        if (status == "show") {
            v.visibility = View.VISIBLE
        }
        if (status == "hide") {
            v.visibility = View.INVISIBLE
        }
        return v
    }
}