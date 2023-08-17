package com.mobile.application

class AuthUtils {
    companion object{

        fun isValidEmail(email:String):Boolean{
            return email.endsWith("@gmail.com")
        }
        fun isValidPassword(password: String): Boolean{
            return password.length > 7
        }

    }
}