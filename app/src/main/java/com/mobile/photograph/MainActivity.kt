package com.mobile.photograph

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {
private lateinit var navController: NavController
private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)




        initNavController()
    }

    private fun initNavController(){
        (supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment).also{
            navController = it.navController
        }
    }




}