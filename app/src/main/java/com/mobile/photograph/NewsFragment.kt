package com.mobile.photograph

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.mobile.photograph.model.Post
import com.mobile.photograph.adapter.NewsRecyclerAdapter
import com.mobile.photograph.databinding.FragmentNewsBinding
class NewsFragment : Fragment() {

    private lateinit var binding: FragmentNewsBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    var postList = ArrayList<Post>()
    private lateinit var newsRecyclerAdapter: NewsRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    @SuppressLint("NotifyDataSetChanged")
    fun obtainData(){
        firestore.collection("Post").orderBy("currentDate", Query.Direction.DESCENDING).addSnapshotListener { snapshot, exception ->
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
                            val isClickControl = document.getBoolean("isFavorite") ?: true

                            val downloadedPost = Post(docId,userEmail,userComment,imageUrl,isClickControl)

                            postList.add(downloadedPost)
                        }
                        newsRecyclerAdapter.notifyDataSetChanged()
                    }else{
                        Toast.makeText(requireContext(), "there is no posts", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentNewsBinding.inflate(inflater, container, false)
        auth = Firebase.auth
        firestore = FirebaseFirestore.getInstance()
        val layoutManager = LinearLayoutManager(requireContext())

        obtainData()

        binding.recyclerView.layoutManager = layoutManager
        newsRecyclerAdapter = NewsRecyclerAdapter(requireContext(),postList)
        binding.recyclerView.adapter = newsRecyclerAdapter

        newsRecyclerAdapter.favoriteClicked = { docId,isClickControl ->
            addToFavorite(docId,isClickControl)
            obtainData()
        }


        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.navbar.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.favorites -> {
                    val action = NewsFragmentDirections.actionNewsFragmentToFavoritesFragment()
                    Navigation.findNavController(requireView()).navigate(action)
                    true
                }
                R.id.account -> {

                    val action = NewsFragmentDirections.actionNewsFragmentToAccountFragment()
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
        inflater.inflate(R.menu.options_menu,menu)
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.sharePhoto ->{
                val action = NewsFragmentDirections.actionNewsFragmentToSharePhotoFragment()
                Navigation.findNavController(requireView()).navigate(action)
            }

        }



        return super.onOptionsItemSelected(item)
    }
    fun addToFavorite(documentId : String,isClickControl: Boolean){
        val postRef = firestore.collection("Post").document(documentId)

        val newClickStatus = !isClickControl

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