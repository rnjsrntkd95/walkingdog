package com.example.walkingdog_kotlin.Post

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.walkingdog_kotlin.Post.Model.UploadImage
import com.example.walkingdog_kotlin.R

class uploadImageRvAdapter(val context: Context, val uploadImageList : ArrayList<UploadImage>) :
        RecyclerView.Adapter<uploadImageRvAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.upload_image_rv_item, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return uploadImageList.size
    }


    inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        val photo = itemView?.findViewById<ImageView>(R.id.uploadImageView)


        fun bind(uploadImage: UploadImage, context: Context) {
            photo?.setImageURI(uploadImage.url)
        }
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder?.bind(uploadImageList[position], context)
    }

}