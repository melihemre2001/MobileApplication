package com.mobile.application

import android.view.View
import android.widget.TextView

fun TextView.visible() {
    this.visibility = View.VISIBLE
}

fun isValidEmail(email: String):Boolean{
    val emailRegex = Regex("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")
    return emailRegex.matches(email)
}