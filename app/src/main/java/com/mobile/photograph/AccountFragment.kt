package com.mobile.photograph

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.graphics.rotationMatrix
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mobile.photograph.databinding.FragmentAccountBinding
import com.mobile.photograph.model.Post
import java.time.Year
import java.util.zip.Inflater


class AccountFragment : Fragment() {

    private lateinit var binding: FragmentAccountBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    var postList = ArrayList<Post>()
    var post: Post? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAccountBinding.inflate(inflater,container,false)
        auth = FirebaseAuth.getInstance()



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
         val sharedPreferences = context?.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
         val userEmail = sharedPreferences?.getString("userEmail","")

        binding.userEmail.text = userEmail

        binding.quit.setOnClickListener {
            val action = AccountFragmentDirections.actionAccountFragmentToLoginSection()
            Navigation.findNavController(it).navigate(action)
            auth.signOut()

             }

        binding.changePassword.setOnClickListener {
            val action = AccountFragmentDirections.actionAccountFragmentToChangePasswordFragment()
            Navigation.findNavController(it).navigate(action)
        }

        binding.myAccount.setOnClickListener {
            binding.myAccountRightArrow.animate().apply {
                duration = 500
                rotationBy(90f)
            }
        }
    }
}