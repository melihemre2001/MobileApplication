package com.mobile.application

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mobile.application.databinding.FragmentNewsBinding
import com.mobile.application.databinding.FragmentRegisterSectionBinding


class NewsFragment : Fragment() {

    private lateinit var binding: FragmentNewsBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentNewsBinding.inflate(inflater, container, false)
        auth = Firebase.auth

        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)


        inflater.inflate(R.menu.options_menu,menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.sharePhoto){
            val action = NewsFragmentDirections.actionNewsFragmentToSharePhotoFragment()
            Navigation.findNavController(requireView()).navigate(action)
        }
        else if(item.itemId == R.id.quit){
            auth.signOut()
            val action = NewsFragmentDirections.actionNewsFragmentToLoginSection()
            Navigation.findNavController(requireView()).navigate(action)

        }


        return super.onOptionsItemSelected(item)

    }


}