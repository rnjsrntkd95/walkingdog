package com.example.walkingdog_kotlin

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.walkingdog_kotlin.model.MyFeedDTO

class Myfeed_RVAdapter(val context: Context, val myfeed_list: ArrayList<MyFeedDTO>):
    RecyclerView.Adapter<Myfeed_RVAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.myfeed_rv_item, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return myfeed_list.size
    }

    override fun onBindViewHolder(holder: Myfeed_RVAdapter.Holder, position: Int) {
        holder?.bind(myfeed_list [position], context)
    }

    inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        val myfeed_Photo = itemView?.findViewById<ImageView>(R.id.myfeed_profile_img)
        val myfeed_title = itemView?.findViewById<TextView>(R.id.myfeed_nickname)


        fun bind (myfeed_list: MyFeedDTO, context: Context) {
            /* dogPhoto의 setImageResource에 들어갈 이미지의 id를 파일명(String)으로 찾고,
            이미지가 없는 경우 안드로이드 기본 아이콘을 표시한다.*/
            if (myfeed_list.photo != "") {
                val resourceId = context.resources.getIdentifier(myfeed_list.photo, "drawable", context.packageName)
                myfeed_Photo?.setImageResource(resourceId)
            } else {
                myfeed_Photo?.setImageResource(R.mipmap.ic_launcher)
            }
            /* 나머지 TextView와 String 데이터를 연결한다. */
            myfeed_title?.text = myfeed_list.title


        }
    }

    constructor(parcel: Parcel) : this(
        TODO("context"),
        TODO("myfeed_list")
    ) {
    }

}



