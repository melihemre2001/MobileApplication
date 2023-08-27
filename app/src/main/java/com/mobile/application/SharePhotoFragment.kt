package com.mobile.application

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.mobile.application.databinding.FragmentSharePhotoBinding
import java.util.UUID


class SharePhotoFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private lateinit var binding: FragmentSharePhotoBinding
    private lateinit var database: FirebaseFirestore
    var selectedPhoto: Uri?= null
    var selectedBitmap: Bitmap?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        auth = Firebase.auth
        storage = Firebase.storage
        database = Firebase.firestore

        binding = FragmentSharePhotoBinding.inflate(inflater,container,false)



        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.selectPhotoImage.setOnClickListener {
            selectPhoto(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)


        inflater.inflate(R.menu.options_menu,menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == R.id.quit){
            auth.signOut()
            val action = SharePhotoFragmentDirections.actionSharePhotoFragmentToLoginSection()
            Navigation.findNavController(requireView()).navigate(action)

        }


        return super.onOptionsItemSelected(item)

    }

    fun share(view: View){

        val uuid = UUID.randomUUID()
        val imageName = "${uuid}.jpg"

        val reference = storage.reference
        val imageReference = reference.child("images").child(imageName)

        if (selectedPhoto != null){
            imageReference.putFile(selectedPhoto!!).addOnSuccessListener { taskSnapshot ->

            }
        }
    }

    fun selectPhoto(view: View){
        activity?.let {
            if (ContextCompat.checkSelfPermission(it,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(it, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),1)
            }
            else{
                val galleryIntent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galleryIntent,2)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        if (requestCode == 1){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                val galleryIntent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galleryIntent,2)
            }
        }


        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null){

            selectedPhoto = data.data

            if (Build.VERSION.SDK_INT >= 28){
                activity?.let {
                    val source = ImageDecoder.createSource(it.contentResolver,selectedPhoto!!)
                    selectedBitmap = ImageDecoder.decodeBitmap(source)
                    binding.selectPhotoImage.setImageBitmap(selectedBitmap)
                }
            }
            else{
                activity?.let {
                    if (selectedPhoto != null){

                        selectedBitmap = MediaStore.Images.Media.getBitmap(it.contentResolver,selectedPhoto)
                        binding.selectPhotoImage.setImageBitmap(selectedBitmap)

                    }
                }
            }
        }


        super.onActivityResult(requestCode, resultCode, data)
    }
    fun createSmallBitmap(bitmapByUser: Bitmap, maxSize: Int) :Bitmap{
        var width = bitmapByUser.width
        var height = bitmapByUser.height

        val bitmapRatio : Double = width.toDouble() / height.toDouble()

        if (bitmapRatio > 1){
            width = maxSize
            val shortenedHeight = width / bitmapRatio
            height = shortenedHeight.toInt()
        }
        else{
            height = maxSize
            val shortenedWidth = height * bitmapRatio
            width = shortenedWidth.toInt()
        }

        return Bitmap.createScaledBitmap(bitmapByUser,width ,height,true)
    }

}