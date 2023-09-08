package com.mobile.photograph

import android.content.Context
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.navigation.Navigation
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.mobile.photograph.databinding.FragmentChangePasswordBinding


class ChangePasswordFragment : Fragment() {

    private lateinit var binding: FragmentChangePasswordBinding
    private lateinit var auth: FirebaseAuth
    private var authListener: FirebaseAuth.AuthStateListener? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentChangePasswordBinding.inflate(inflater,container,false)
        auth = FirebaseAuth.getInstance()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.navbarFav.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.home -> {
                   val action = ChangePasswordFragmentDirections.actionChangePasswordFragmentToNewsFragment()
                    Navigation.findNavController(requireView()).navigate(action)
                    true
                }
               R.id.favorites -> {
                   val action = ChangePasswordFragmentDirections.actionChangePasswordFragmentToFavoritesFragment()
                   Navigation.findNavController(requireView()).navigate(action)
                   true
               }
                R.id.account -> {
                    val action = ChangePasswordFragmentDirections.actionChangePasswordFragmentToAccountFragment()
                    Navigation.findNavController(requireView()).navigate(action)
                    true
                }
                else -> {false}
            }
        }



        authListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = auth.currentUser

            if (user != null) {
                binding.changePasswordBtn.setOnClickListener {
                    changePassword()
                }
            } else {
                val action = ChangePasswordFragmentDirections.actionChangePasswordFragmentToLoginSection()
                Navigation.findNavController(requireView()).navigate(action)
            }
        }

        auth.addAuthStateListener(authListener!!)

    }

    fun changePassword(){
        val user = auth.currentUser

        val oldPassword = binding.fieldPasswordEditText.text.toString()
        val newPassword = binding.newPasswordEditText.text.toString()
        val confirmPassword = binding.fieldConfirmPasswordEditText.text.toString()

        if (oldPassword.isNotEmpty() && newPassword.isNotEmpty() && confirmPassword.isNotEmpty()){
            if (newPassword == confirmPassword){

                    if (user != null){
                        val credential = EmailAuthProvider.getCredential(user.email!!,oldPassword)
                        Toast.makeText(requireContext(), user.email, Toast.LENGTH_SHORT).show()
                        user.reauthenticate(credential).addOnCompleteListener { reauthTask ->

                            if (reauthTask.isSuccessful){
                                user.updatePassword(newPassword).addOnCompleteListener { task ->

                                    if (task.isSuccessful){
                                        Toast.makeText(requireContext(), "Password Changed Successfully", Toast.LENGTH_SHORT).show()
                                        val action = ChangePasswordFragmentDirections.actionChangePasswordFragmentToAccountFragment()
                                        Navigation.findNavController(requireView()).navigate(action)
                                    } else {
                                        Log.e("FirebaseError", "Şifre güncelleme hatası: ${task.exception}")
                                        }
                                    }
                                }
                            else{
                                reauthTask.addOnFailureListener {  exception ->
                                    Toast.makeText(requireContext(), exception.localizedMessage, Toast.LENGTH_SHORT).show()

                                }
                            }
                        }
                      }
                    }
                }
            }
        }




