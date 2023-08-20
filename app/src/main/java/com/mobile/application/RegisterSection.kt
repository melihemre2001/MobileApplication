package com.mobile.application

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.mobile.application.databinding.FragmentRegisterSectionBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterSection : Fragment() {

    private lateinit var binding: FragmentRegisterSectionBinding
    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentRegisterSectionBinding.inflate(inflater,container,false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegister.setOnClickListener {

            val username:String = binding.fieldUsernameEditText.text.toString().trim()
            val email:String = binding.fieldEmailEditText.text.toString().trim()
            val password:String = binding.fieldPasswordEditText.text.toString().trim()
            val confirmPassword:String = binding.fieldConfirmPasswordEditText.text.toString().trim()

            if(isValidEmail(email)){
                Toast.makeText(requireContext(), "Geçersiz email", Toast.LENGTH_SHORT).show()

            }

            else if (password == confirmPassword){
                    database.child("username").push().setValue(username)
                    database.child("email").push().setValue(email)
                    database.child("password").push().setValue(password)

                    Toast.makeText(requireContext(), "Başarılı", Toast.LENGTH_SHORT).show()

                }
                else{
                    Toast.makeText(requireContext(), "Başarısız", Toast.LENGTH_SHORT).show()
                }

        }

    }
    private fun isValidEmail(email: String): Boolean{
        val regex = Regex("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}")
        return regex.matches(email)

    }



}