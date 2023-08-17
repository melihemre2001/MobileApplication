package com.mobile.application

import android.content.Context

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.mobile.application.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            checkInputs()

        }
    }

    private fun handleKeyEvent(view: View, keyCode: Int): Boolean {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            return true
        }
        return false
    }

    private fun checkInputs() {
        var result = false
        val inputEmail = binding.fieldUsernameEditText.text.toString()
        val inputPassword = binding.fieldPasswordEditText.text.toString()

        if (inputPassword.isEmpty()) {
            result = true
            binding.fieldUsernameEditText.requestFocus()
            binding.fieldPassword.apply {
                isErrorEnabled = true
                error = "Enter password"
            }

            Toast.makeText(this, "Enter a username or password", Toast.LENGTH_LONG).show()
        }
        if (inputEmail.isEmpty()) {
            result = true
            binding.fieldPasswordEditText.requestFocus()
            binding.fieldUsername.apply {
                isErrorEnabled = true
                error = "Enter email"
            }
            Toast.makeText(this, "Enter a username or password", Toast.LENGTH_LONG).show()
        }
        else if(!isValidEmail(inputEmail)){
            result = true
            binding.fieldUsername.apply {
                isErrorEnabled = true
                error = "Invalid email"
            }
            Toast.makeText(this, "Enter a valid email", Toast.LENGTH_SHORT).show()
        }

        if (!result) {
            binding.fieldPassword.isErrorEnabled = false
            binding.fieldUsername.isErrorEnabled = false
            Toast.makeText(this, "Successful", Toast.LENGTH_SHORT).show()
        }
    }
    private fun isValidEmail(email: String): Boolean{
        val regex = Regex("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}")
        return regex.matches(email)

    }


}