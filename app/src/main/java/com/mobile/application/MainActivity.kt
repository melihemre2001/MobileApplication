package com.mobile.application

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment


class MainActivity : AppCompatActivity() {
private lateinit var navController: NavController



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