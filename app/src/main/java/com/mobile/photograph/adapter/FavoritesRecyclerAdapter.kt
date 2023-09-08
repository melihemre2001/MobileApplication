package com.mobile.photograph.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mobile.photograph.FavoritesFragment
import com.mobile.photograph.R
import com.mobile.photograph.model.Post
import com.squareup.picasso.Picasso

class FavoritesRecyclerAdapter(private val context: Context,private val postList: ArrayList<Post>) : RecyclerView.Adapter<FavoritesRecyclerAdapter.FavoritesHolder>() {
    inner class FavoritesHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val iconImageView: ImageView = itemView.findViewById(R.id.ic_favorite)


    }
    lateinit var favoriteClicked: ((String,Boolean)->Unit)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.recycler_row,parent,false)

        return FavoritesHolder(view)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: FavoritesHolder, position: Int) {
        val post = postList[position]
        holder.itemView.findViewById<TextView>(R.id.recycler_row_userEmail).text = post.userEmail
        holder.itemView.findViewById<TextView>(R.id.recycler_row_userComment).text = post.userComment

        Picasso.get().load(post.imageUrl).into(holder.itemView.findViewById<ImageView>(R.id.recycler_row_image))

        val iconResource = if (!post.isFavorite) {
            R.drawable.baseline_favorite_border_24

        } else {
            R.drawable.baseline_favorite_24
        }
        holder.iconImageView.setImageResource(iconResource)

        holder.itemView.findViewById<ImageView>(R.id.ic_favorite).setOnClickListener{

            favoriteClicked.invoke(post.docId,post.isFavorite)


        }
    }



}