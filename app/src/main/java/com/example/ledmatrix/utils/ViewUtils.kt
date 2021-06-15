package com.example.ledmatrix.utils

import android.content.Context
import android.widget.Toast
import com.example.ledmatrix.R
import com.muddzdev.styleabletoastlibrary.StyleableToast

fun Context.toast(message: String){
    StyleableToast.makeText(this, message, R.style.toast_style).show()
}

fun Context.iToast(message: String){
    StyleableToast.makeText(this, message, R.style.itoast_style).show()

}