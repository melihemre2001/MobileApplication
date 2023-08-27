package com.mobile.application

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mobile.application.databinding.FragmentLoginSectionBinding


class LoginSection : Fragment() {

    private lateinit var binding: FragmentLoginSectionBinding
    private var auth: FirebaseAuth? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginSectionBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignUp.setOnClickListener {
            val action = LoginSectionDirections.actionLoginSectionToRegisterSection()
            Navigation.findNavController(it).navigate(action)
        }
        binding.btnLogin.setOnClickListener {
            checkInputs()

            val inputEmail = binding.fieldUsernameEditText.text.toString()
            val inputPassword = binding.fieldPasswordEditText.text.toString()

            if (inputEmail.isNotEmpty() && inputPassword.isNotEmpty()) {
                loginUser(inputEmail, inputPassword)
            }
        }
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
        }
        if (inputEmail.isEmpty()) {
            result = true
            binding.fieldPasswordEditText.requestFocus()
            binding.fieldUsername.apply {
                isErrorEnabled = true
                error = "Enter email"
            }
        } else if (!isValidEmail(inputEmail)) {
            result = true
            binding.fieldUsername.apply {
                isErrorEnabled = true
                error = "Invalid email"
            }
        }

        if (!result) {
            binding.fieldPassword.isErrorEnabled = false
            binding.fieldUsername.isErrorEnabled = false
        }
    }

    private fun isValidEmail(email: String): Boolean {
        val regex = Regex("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}")
        return regex.matches(email)
    }

    private fun loginUser(email: String, password: String) {
        auth?.signInWithEmailAndPassword(email, password)?.addOnSuccessListener {

            Toast.makeText(requireContext(), "başarılı", Toast.LENGTH_SHORT).show()
            val action = LoginSectionDirections.actionLoginSectionToNewsFragment()
            Navigation.findNavController(requireView()).navigate(action)
        }
            ?.addOnFailureListener {
                Toast.makeText(requireContext(), "başarısız", Toast.LENGTH_SHORT).show()
            }
    }

}