package com.mobile.photograph

import android.annotation.SuppressLint
import android.opengl.Visibility
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import com.mobile.photograph.R
import com.mobile.photograph.adapter.FavoritesRecyclerAdapter
import com.mobile.photograph.databinding.FragmentFavoritesBinding
import com.mobile.photograph.model.Post
import org.w3c.dom.Text
import java.util.zip.Inflater


class FavoritesFragment : Fragment() {

    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var favoritesRecyclerAdapter: FavoritesRecyclerAdapter
    var postList = ArrayList<Post>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        binding = FragmentFavoritesBinding.inflate(inflater,container,false)
        auth = Firebase.auth
        firestore = FirebaseFirestore.getInstance()

        obtainFavorites()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(postList.isEmpty()){
            binding.recyclerViewFavorites.visibility = View.GONE
            binding.favoritesEmpty.visibility = View.VISIBLE
        }
        else{
            binding.recyclerViewFavorites.visibility = View.VISIBLE
            binding.favoritesEmpty.visibility = View.GONE
        }


        val layoutManager = LinearLayoutManager(requireContext())

        binding.recyclerViewFavorites.layoutManager = layoutManager
        favoritesRecyclerAdapter = FavoritesRecyclerAdapter(requireContext(), postList)
        binding.recyclerViewFavorites.adapter = favoritesRecyclerAdapter
        favoritesRecyclerAdapter.favoriteClicked = { docId,isFavorite ->
            addToFavorite(docId,isFavorite)
            obtainFavorites()
            removeLastFavorite()
        }

        binding.navbarFav.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.home -> {
                    val action = FavoritesFragmentDirections.actionFavoritesFragmentToNewsFragment()
                    Navigation.findNavController(requireView()).navigate(action)
                    true
                }
                R.id.account -> {
                    val action = FavoritesFragmentDirections.actionFavoritesFragmentToAccountFragment()
                    Navigation.findNavController(requireView()).navigate(action)
                    true
                }
                else -> {false}
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.nav_menu,menu)
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun obtainFavorites(){

        firestore.collection("Post")
            .whereEqualTo("isFavorite",true)
            .orderBy("currentDate",Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, exception ->

                if (exception != null){
                    Toast.makeText(requireContext(), exception.localizedMessage, Toast.LENGTH_SHORT).show()
                }
                else{
                    if (snapshot != null){
                        if (!snapshot.isEmpty){
                            val documents = snapshot.documents
                            postList.clear()

                            for (document in documents){

                                val docId = document.getString("docId") ?:""
                                val userEmail = document.getString("userEmail") ?: ""
                                val userComment = document.getString("userComment") ?: ""
                                val imageUrl = document.getString("imageUrl") ?: ""
                                val isFavorite = document.getBoolean("isFavorite") ?: true

                                val downloadedPost = Post(docId,userEmail,userComment,imageUrl,isFavorite)

                                postList.add(downloadedPost)
                                if(postList.isEmpty()){
                                    binding.recyclerViewFavorites.visibility = View.GONE
                                    binding.favoritesEmpty.visibility = View.VISIBLE
                                }
                                else if (postList.isNotEmpty()){
                                    binding.recyclerViewFavorites.visibility = View.VISIBLE
                                    binding.favoritesEmpty.visibility = View.GONE
                                }

                            }

                            favoritesRecyclerAdapter.notifyDataSetChanged()
                        }
                    }
                }
            }
    }

    private fun removeLastFavorite() {
        if (postList.isNotEmpty()) {
            val lastFavorite = postList.last()
            postList.remove(lastFavorite)
            if(postList.isEmpty()){
                binding.recyclerViewFavorites.visibility = View.GONE
                binding.favoritesEmpty.visibility = View.VISIBLE
            }
            else if (postList.isNotEmpty()){
                binding.recyclerViewFavorites.visibility = View.VISIBLE
                binding.favoritesEmpty.visibility = View.GONE
            }
            favoritesRecyclerAdapter.notifyItemRemoved(postList.size)

        }


    }

    private fun addToFavorite(documentId : String, isFavorite: Boolean){
        val postRef = firestore.collection("Post").document(documentId)

        val newClickStatus = !isFavorite

        postRef.update("isFavorite", newClickStatus)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (newClickStatus) {
                        Toast.makeText(context, "Beğenildi", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Beğenilmekten çıkıldı", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Hata oluştu", Toast.LENGTH_SHORT).show()
                }
            }
    }
}