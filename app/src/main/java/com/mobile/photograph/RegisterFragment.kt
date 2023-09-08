package com.mobile.photograph

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.mobile.photograph.databinding.FragmentRegisterSectionBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterSectionBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentRegisterSectionBinding.inflate(inflater, container, false)

        binding.goLogin.setOnClickListener { view ->
            val actionBackupLogin = RegisterFragmentDirections.actionRegisterSectionToLoginSection()
            Navigation.findNavController(view).navigate(actionBackupLogin)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegister.setOnClickListener {

            val email: String = binding.fieldUsernameEditText.text.toString().trim()
            val username: String = binding.fieldEmailEditText.text.toString().trim()
            val password: String = binding.fieldPasswordEditText.text.toString().trim()
            val confirmPassword: String =
                binding.fieldConfirmPasswordEditText.text.toString().trim()

            if (username.isNotEmpty() && email.isNotEmpty()
                && password.isNotEmpty() && confirmPassword.isNotEmpty()
            ) {
                if (password == confirmPassword) {
                    createUser(email, password)
                } else {
                    Toast.makeText(requireContext(), "Sifreler uyusmuyor", Toast.LENGTH_SHORT)
                        .show()
                }

            } else {
                Toast.makeText(requireContext(), "Boş alan bırakmayın", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    }

    private fun isValidEmail(email: String): Boolean {
        val regex = Regex("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}")
        return regex.matches(email)

    }

    private fun createUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
            val action = RegisterFragmentDirections.actionRegisterSectionToLoginSection()
            Navigation.findNavController(requireView()).navigate(action)
        }
            .addOnFailureListener() {
                Toast.makeText(
                    requireContext(),
                    "Please check your information",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

}